package com.gmp.web.controller;

import com.gmp.facturedo.FacturedoCIG;
import com.gmp.facturedo.JSON.*;
import com.gmp.facturedo.thread.FacturedoSeeker;
import com.gmp.facturedo.thread.FacturedoUpdater;
import com.gmp.finsmart.JSON.*;
import com.gmp.hmviking.InvestmentBlock;
import com.gmp.finsmart.thread.*;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;
import com.gmp.persistence.dao.FactUserRepository;
import com.gmp.persistence.dao.SmartUserRepository;
import com.gmp.persistence.model.*;
import com.gmp.service.IReportService;
import com.gmp.service.IUserService;
import com.gmp.web.dto.*;
import com.gmp.finsmart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mobile.device.Device;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.gmp.hmviking.InvestmentUtil.*;
import static com.gmp.hmviking.InvestmentUtil.updateInvestment;

@Controller
public class HMVikingController {
    private LoginJSON loginJSON;
    private InvestmentBlock investmentBlockInv;
    private ExecutorService pool;
    private FacturedoUpdater updaterFact;
    @Autowired
    private FactUserRepository factUserRepository;
    @Autowired
    private IUserService userService;
    @Autowired
    private IReportService reportService;
    private boolean flag=false;
    private AtomicBoolean enabled = new AtomicBoolean(false);
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private SmartUserRepository smartRepository;
    private HttpSession schedulerSession;
    private Double currencyFactor = 3.60;
    private FinsmartData finsmartData;
    private User userId;
    private Double balancePEN;
    private Double balanceUSD;
    private List<InvoiceTransactions> invoices;

    private HashMap<Integer,Results> auctMap;
    private int actualSize;
    private volatile QueueStructure queueStructure;

    @GetMapping({"/waitForInvoice"})
    public String waitForInvoice(HttpSession session,Device device)  {
        auctMap = new HashMap<>();
        schedulerSession = session;
        actualSize = investmentBlockInv.getInvestmentList().size();
        pool = Executors.newFixedThreadPool(actualSize);
        queueStructure = new QueueStructure(investmentBlockInv.getInvestmentList().size(),
                balancePEN, balanceUSD,investmentBlockInv.getSystem(),investmentBlockInv.isScheduled());
        if(investmentBlockInv.getSystem().equals("HMFINSMART")) {
            FinSmartSeekerThread seeker = new FinSmartSeekerThread(loginJSON,investmentBlockInv.getScheduledTime(),
                    investmentBlockInv.getTimeRequest(),queueStructure,investmentBlockInv.getInvestmentList(),
                    reportService,userId,investmentBlockInv.getSystem());
            pool.execute(seeker);
        }else if(investmentBlockInv.getSystem().equals("HMFACTUREDO")){
            FacturedoSeeker seeker = new FacturedoSeeker(queueStructure,loginJSON, investmentBlockInv.getScheduledTime(),
                    investmentBlockInv.getTimeRequest(),auctMap);
            updaterFact = new FacturedoUpdater(seeker, investmentBlockInv.getScheduledTime());
            pool.execute(seeker);
            pool.execute(updaterFact);
        }
        session.setAttribute("investmentsList", investmentBlockInv.getInvestmentList());
        enabled.set(true);
        if(device.isMobile()){
            if(investmentBlockInv.isScheduled()){
                return "redirect:/publicIndex?schedule=true";
            }else return "redirect:/publicIndex";
        }
        if(investmentBlockInv.isScheduled()){
            return "redirect:/investPage?schedule=true";
        }else return "redirect:/investPage";
    }

    @RequestMapping(value="/cancelTransactions.json",method = RequestMethod.GET)
    public @ResponseBody void cancelTransactions(){
        this.pool.shutdownNow();
        this.pool.shutdown();
    }

    @RequestMapping(value="/getDataUX.json",method = RequestMethod.GET)
    public @ResponseBody
    QueueStructure manualSendDataUX(){
        if(enabled.get()){
            updateInvestment(queueStructure.getInvestmentList());
            if(actualSize == queueStructure.getInvestmentList().size()){
                queueStructure.setTransactionStatus(true);
                enabled.set(false);
            }
            return queueStructure;
        }
        return null;
    }

    @Scheduled(fixedDelay = 10000)
    private void sendDataUX() {
        if(enabled.get()){
            if(investmentBlockInv.getSystem().equals("HMFINSMART")) {
                if(!flag && !queueStructure.isScheduled()){
                    investmentBlockInv.setInvestmentList(setInProgress(investmentBlockInv.getInvestmentList()));
                    flag = true;
                    simpMessagingTemplate.convertAndSend("/finSmart/investments", investmentBlockInv);
                }
            }
            else if(investmentBlockInv.getSystem().equals("HMFACTUREDO")){
                if(updaterFact.getQueueStr()!=null && !flag && investmentBlockInv.isScheduled()){
                    investmentBlockInv.setInvestmentList(setInProgress(investmentBlockInv.getInvestmentList()));
                    flag = true;
                    simpMessagingTemplate.convertAndSend("/finSmart/investments", investmentBlockInv);
                }
            }
            updateInvestment(queueStructure.getInvestmentList());
            if(actualSize == queueStructure.getInvestmentList().size()){
                queueStructure.setTransactionStatus(true);
                enabled.set(false);
            }
            simpMessagingTemplate.convertAndSend("/finSmart/investments", queueStructure);
        }
    }

    @GetMapping({"/finsmart"})
    public String finSmartForm(Model model, @AuthenticationPrincipal User user, HttpSession session) throws IOException{
        userId = user;
        investmentBlockInv = new InvestmentBlock();
        investmentBlockInv.setSystem("HMFINSMART");
        enabled.set(false);
        flag=false;
        if(pool != null){
            pool.shutdownNow();
        }
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        SmartUser smartUser = userService.getFinsmartProfile(user);
        if(smartUser == null){
            return "redirect:/smartUserProfile";
        }
        loginJSON = FinSmartCIG.getAuthentications(smartUser);
        if(loginJSON == null){
            return "redirect:/smartUserProfile?failed=true";
        }
        System.out.println("Login successfully - OK - "+getTime());
        model.addAttribute("investmentForm", new InvestmentForm());
        FinancialTransactions financialTransactions = FinSmartCIG.getFinancialTransactions(loginJSON.getAccessToken());
        invoices = FinSmartCIG.getInvoices(loginJSON.getAccessToken());
         finsmartData = reportService.processFinancialTransactions(financialTransactions,invoices,userId);
        System.out.println("Financial transactions processed - OK - "+getTime());

        balancePEN = finsmartData.getSolesAmountAvailable();
        balanceUSD = finsmartData.getDollarAmountAvailable();
        model.addAttribute("balancePEN", balancePEN);
        model.addAttribute("balanceUSD", balanceUSD);
        model.addAttribute("totalInvestedPEN", formatter.format(finsmartData.getSolesCurrentInvested()));
        model.addAttribute("totalInvestedUSD", formatter.format(finsmartData.getDollarCurrentInvested()));
        model.addAttribute("totalInvested", formatter.format(finsmartData.getSolesCurrentInvested() +
                (finsmartData.getDollarCurrentInvested()*currencyFactor)));
        model.addAttribute("totalDepositsPEN", formatter.format(finsmartData.getSolesTotalDeposited()));
        model.addAttribute("totalDepositsUSD", formatter.format(finsmartData.getDollarTotalDeposited()));
        model.addAttribute("totalDeposits", formatter.format(finsmartData.getSolesTotalDeposited() +
                (finsmartData.getDollarTotalDeposited()*currencyFactor)));
        model.addAttribute("totalProfitPEN", formatter.format(finsmartData.getSolesTotalProfit()));
        model.addAttribute("totalProfitUSD", formatter.format(finsmartData.getDollarTotalProfit()));
        model.addAttribute("totalProfit", formatter.format(finsmartData.getSolesTotalProfit() +
                (finsmartData.getDollarTotalProfit()*currencyFactor)));
        model.addAttribute("expectedProfitPEN", formatter.format(finsmartData.getSolesProfitExpected()));
        model.addAttribute("expectedProfitUSD", formatter.format(finsmartData.getDollarProfitExpected()));
        model.addAttribute("expectedProfit", formatter.format(finsmartData.getSolesProfitExpected() +
                (finsmartData.getDollarProfitExpected()*currencyFactor)));
        model.addAttribute("onRiskPEN", formatter.format(finsmartData.getSolesOnRisk()));
        model.addAttribute("onRiskUSD", formatter.format(finsmartData.getDollarOnRisk()));
        model.addAttribute("totalOnRisk", formatter.format(finsmartData.getSolesOnRisk() +
                (finsmartData.getDollarOnRisk()*currencyFactor)));

        session.setAttribute("finalizedInv",reportService.getFinalizedInvoices(invoices));
        session.setAttribute("investments",finsmartData.getCurrentInvestmentsIndex());
        session.setAttribute("latestInvestments",reportService.getAllByUserAndSystemId(userId,investmentBlockInv.getSystem()));

        return "finsmart";
    }

    @PostMapping(value = "/finsmart")
    public String finSmartForm(@ModelAttribute("investmentForm") InvestmentForm investmentForm, HttpServletRequest request,
                               Model model) {
        HttpSession session = request.getSession();
        List<Investment> temp = getInvestCollection(investmentForm);
        if(temp == null){
            model.addAttribute("error", "true");
            return "redirect:/finsmart";
        }
        investmentBlockInv.setInvestmentList(temp);
        if(!investmentForm.getTimeRequest().equals("")){
            investmentBlockInv.setTimeRequest((int)(Double.parseDouble(investmentForm.getTimeRequest())*1000));
        }else investmentBlockInv.setTimeRequest(500);
        if(!investmentForm.getScheduledTime().replace(",","").equals("none")){
            investmentBlockInv.setScheduledTime(investmentForm.getScheduledTime().replace(",",""));
            investmentBlockInv.setScheduled(true);
        }else investmentBlockInv.setScheduled(false);
        session.setAttribute("finSmartCollection", investmentBlockInv);
        return "redirect:/waitForInvoice";
    }

    @GetMapping({"/facturedo"})
    public String factuForm(@AuthenticationPrincipal User user, Model model, HttpSession session) throws IOException {
        userId = user;
        investmentBlockInv = new InvestmentBlock();
        investmentBlockInv.setSystem("HMFACTUREDO");
        enabled.set(false);
        flag=false;
        if(pool != null){
            pool.shutdownNow();
        }
        FactUser factUser = userService.getFactuProfile(user);
        if(factUser == null){
            return "redirect:/factUserProfile";
        }
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        loginJSON = FacturedoCIG.getAuthentications(factUser);
        if(loginJSON == null){
            return "redirect:/factUserProfile?failed=true";
        }
        System.out.println("Login successfully - OK - "+ getTime());
        FacturedoData factuData = new FacturedoData(loginJSON);
        model.addAttribute("investmentForm", new InvestmentForm());
        balancePEN = factuData.getSolesAmountAvailable();
        balanceUSD = factuData.getDollarAmountAvailable();
        model.addAttribute("balancePEN", balancePEN);
        model.addAttribute("balanceUSD", balanceUSD);
        model.addAttribute("totalInvestedPEN", factuData.getSolesCurrentInvested());
        model.addAttribute("totalInvestedUSD", factuData.getDollarCurrentInvested());
        model.addAttribute("totalInvested", factuData.getSolesCurrentInvested() +
                (factuData.getDollarCurrentInvested()*currencyFactor));

        model.addAttribute("totalDepositsPEN", factuData.getSolesTotalTransferred());
        model.addAttribute("totalDepositsUSD", factuData.getDollarTotalTransferred());
        model.addAttribute("totalDeposits", formatter.format(factuData.getSolesTotalTransferred() +
                (factuData.getDollarTotalTransferred()*currencyFactor)));

        model.addAttribute("totalProfitPEN", ((balancePEN+factuData.getSolesCurrentInvested()-factuData.getSolesTotalTransferred())));
        model.addAttribute("totalProfitUSD", ((balanceUSD+factuData.getDollarCurrentInvested()-factuData.getDollarTotalTransferred())));
        model.addAttribute("totalProfit", formatter.format(
                (balancePEN+factuData.getSolesCurrentInvested()-factuData.getSolesTotalTransferred()) +
                ((balanceUSD+factuData.getDollarCurrentInvested()-factuData.getDollarTotalTransferred()))*currencyFactor));

        model.addAttribute("expectedProfitPEN", factuData.getSolesProfitExpected());
        model.addAttribute("expectedProfitUSD", factuData.getDollarProfitExpected());
        model.addAttribute("expectedProfit", formatter.format(factuData.getSolesProfitExpected() +
                (factuData.getDollarProfitExpected()*currencyFactor)));

        session.setAttribute("latestInvestments",reportService.getAllByUserAndSystemId(userId,investmentBlockInv.getSystem()));
        session.setAttribute("factuInvestments",reportService.getProcessedResultsFactu(factuData,loginJSON).getResultsInProgress());
        session.setAttribute("factuFinalizedInv",reportService.getFactuFinalizedInvoices(loginJSON));

        return "facturedo";
    }

    @PostMapping(value = "/facturedo")
    public String factuForm(@ModelAttribute("investmentForm") InvestmentForm investmentForm, HttpServletRequest request,
                            Model model) {
        HttpSession session = request.getSession();
        List<Investment> temp = getInvestCollection(investmentForm);
        if(temp == null){
            model.addAttribute("error", "true");
            return "redirect:/facturedo";
        }
        investmentBlockInv.setInvestmentList(temp);
        if(!investmentForm.getTimeRequest().equals("")){
            investmentBlockInv.setTimeRequest((int)(Double.parseDouble(investmentForm.getTimeRequest())*1000));
        }else investmentBlockInv.setTimeRequest(500);
        if(!investmentForm.getScheduledTime().replace(",","").equals("none")){
            investmentBlockInv.setScheduledTime(investmentForm.getScheduledTime().replace(",",""));
            investmentBlockInv.setScheduled(true);
        }else investmentBlockInv.setScheduled(false);
        session.setAttribute("finSmartCollection", investmentBlockInv);
        return "redirect:/waitForInvoice";
    }

    @RequestMapping(value="/smartTestConnection",method = RequestMethod.GET)
    public @ResponseBody boolean testConnection(@ModelAttribute("smartUser") SmartUserDto smartUserDto) {
        try {
            return FinSmartCIG.getAuthentications(smartUserDto) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @GetMapping({"/monthlyAnalysis"})
    public String monthlyAnalysis(HttpSession session){
        session.setAttribute("latestInvestments",reportService.getAllByUserAndSystemId(userId,investmentBlockInv.getSystem()));
        session.setAttribute("finalizedInv",reportService.getFinalizedInvoices(invoices));
        session.setAttribute("investments",finsmartData.getCurrentInvestmentsIndex());
        return "monthlyAnalysis";
    }

    @RequestMapping(value="/historicalData.json",method = RequestMethod.GET)
    public @ResponseBody List<HistoricalData> historicalData(){
        return reportService.getMonthlyAnalysis(userId);
    }

    @RequestMapping(value="/weeklyData.json",method = RequestMethod.GET)
    public @ResponseBody List<WeeklyDataModel> weeklyData(){
        List<WeeklyDataModel> a = reportService.getDataModel(userId);
        return reportService.getDataModel(userId);
    }

    @GetMapping({"/smartUserProfile"})
    public String smartUserProfile(Model model, @AuthenticationPrincipal User user) {
        SmartUser smartUser = smartRepository.findSmartUserByUser(user);
        if(smartUser != null){
            smartUser.setPassword("");
            model.addAttribute("smartUser", smartUser);
        }else model.addAttribute("smartUser", new SmartUser());
        return "smartUserProfile";
    }

    @PostMapping(value = "/smartUserProfile")
    public String smartUserProfile(@ModelAttribute("smartUser") SmartUserDto smartUserDto, @AuthenticationPrincipal User user) {
        LoginJSON auth;
        try {
            auth = FinSmartCIG.getAuthentications(smartUserDto);
        } catch (IOException e) {
            return "redirect:/smartUserProfile?saved=false";
        }
        if(auth !=null) {
            SmartUser smartUser = new SmartUser();
            smartUser.setPassword(smartUserDto.getPassword());
            smartUser.setEmail(smartUserDto.getEmail());
            smartUser.setUser(user);
            userService.saveFinsmartProfile(smartUser);
            return "redirect:/smartUserProfile?saved=true";
        }else return "redirect:/smartUserProfile?saved=false";
    }

    @GetMapping({"/factUserProfile"})
    public String factUserProfile(Model model, @AuthenticationPrincipal User user) {
        FactUser factUser = factUserRepository.findFactUserByUser(user);
        if(factUser != null){
            factUser.setPassword("");
            model.addAttribute("factUser", factUser);
        }else model.addAttribute("factUser", new FactUser());
        return "factUserProfile";
    }

    @PostMapping(value = "/factUserProfile")
    public String factUserProfile(@ModelAttribute("factUser") FactUserDto factUserDto, @AuthenticationPrincipal User user) {
        LoginJSON auth;
        try {
            auth = FacturedoCIG.getAuthentications(factUserDto);
        } catch (IOException e) {
            return "redirect:/factUserProfile?saved=false";
        }
        if(auth !=null) {
            FactUser factUser = new FactUser();
            factUser.setPassword(factUserDto.getPassword());
            factUser.setEmail(factUserDto.getEmail());
            factUser.setUser(user);
            userService.saveFacturedoProfile(factUser);
            return "redirect:/factUserProfile?saved=true";
        }else return "redirect:/factUserProfile?saved=false";
    }

    @RequestMapping(value="/factuTestConnection",method = RequestMethod.GET)
    public @ResponseBody boolean testConnection(@ModelAttribute("factUser") FactUserDto factUserDto) {
        try {
            return FacturedoCIG.getAuthentications(factUserDto) != null;
        } catch (IOException e) {
            return false;
        }
    }

}
