package com.gmp.web.controller;

import com.gmp.investmentV1APP.FacturedoCIG;
import com.gmp.investmentV1APP.JSON.*;
import com.gmp.investmentV1APP.thread.FacturedoSeeker;
import com.gmp.investmentV1APP.thread.FacturedoUpdater;
import com.gmp.investmentAPP.JSON.*;
import com.gmp.hmviking.InvestmentBlock;
import com.gmp.investmentAPP.thread.*;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;
import com.gmp.persistence.dao.FactUserRepository;
import com.gmp.persistence.dao.SmartUserRepository;
import com.gmp.persistence.model.*;
import com.gmp.service.IReportService;
import com.gmp.service.IUserService;
import com.gmp.web.dto.*;
import com.gmp.investmentAPP.*;
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

@Controller
public class HMVikingController {
    private LoginJSON loginJSON;
    private InvestmentBlock investmentBlockInv;
    private ExecutorService pool;
    private ExecutorService poolInvestor;
    private ExecutorService poolSubmit;
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
    //private double currencyFactor = investmentAPPUtil.getExchangeRate();
    private double currencyFactor = 4.10;
    private investmentAPPData investmentAPPData;
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
        poolInvestor = Executors.newFixedThreadPool(investmentBlockInv.getInvestmentList().size());
        poolSubmit = Executors.newFixedThreadPool(investmentBlockInv.getInvestmentList().size());
        queueStructure = new QueueStructure(investmentBlockInv.getInvestmentList().size(),
                balancePEN, balanceUSD,investmentBlockInv.getSystem(),investmentBlockInv.isScheduled());
        if(investmentBlockInv.getSystem().equals("HMinvestmentAPP")) {
            investmentAPPSeekerParked seeker = new investmentAPPSeekerParked(loginJSON,investmentBlockInv,
                    queueStructure,reportService,userId, poolInvestor, poolSubmit);
            pool.execute(seeker);
        }else if(investmentBlockInv.getSystem().equals("HMFACTUREDO")){
            FacturedoSeeker seeker = new FacturedoSeeker(queueStructure,loginJSON, investmentBlockInv.getScheduledTime(),auctMap);
            updaterFact = new FacturedoUpdater(seeker, investmentBlockInv.getScheduledTime());
            pool.execute(seeker);
            pool.execute(updaterFact);
        }
        session.setAttribute("investmentsList", investmentBlockInv.getInvestmentList());
        enabled.set(true);
        if(device.isMobile()){
            return "redirect:/publicIndex";
        }
        return "redirect:/investPage";
    }

    @RequestMapping(value="/cancelTransactions.json",method = RequestMethod.GET)
    public @ResponseBody void cancelTransactions(){
        Thread.currentThread().interrupt();
        this.poolInvestor.shutdown();
        this.poolSubmit.shutdown();
        this.pool.shutdown();
        this.poolInvestor.shutdownNow();
        this.pool.shutdownNow();
        this.poolSubmit.shutdownNow();
    }

    @RequestMapping(value="/getDataUX.json",method = RequestMethod.GET)
    public @ResponseBody
    QueueStructure manualSendDataUX(){
        if(enabled.get()){
            return queueStructure;
        }
        return null;
    }

    @Scheduled(fixedDelay = 5000)
    private void sendDataUX() {
        if(enabled.get()){
            if(queueStructure.getActualSize() == 0){
                queueStructure.setTransactionStatus(true);
                enabled.set(false);
            }
            simpMessagingTemplate.convertAndSend("/investmentAPP/investments", queueStructure);
        }
    }

    @GetMapping({"/investmentAPP"})
    public String investmentAPPForm(Model model, @AuthenticationPrincipal User user, HttpSession session) throws IOException{
        userId = user;
        investmentBlockInv = new InvestmentBlock();
        investmentBlockInv.setSystem("HMinvestmentAPP");
        enabled.set(false);
        flag=false;
        if(pool != null){
            pool.shutdownNow();
        }
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        SmartUser smartUser = userService.getinvestmentAPPProfile(user);
        if(smartUser == null){
            return "redirect:/smartUserProfile";
        }
        loginJSON = investmentAPPCIG.getAuthentications(smartUser);
        if(loginJSON == null){
            return "redirect:/smartUserProfile?failed=true";
        }
        System.out.println("Login successfully - OK - "+getTime());
        model.addAttribute("investmentForm", new InvestmentForm());
        FinancialTransactions financialTransactions = investmentAPPCIG.getFinancialTransactions(loginJSON.getAccessToken());
        invoices = investmentAPPCIG.getInvoices(loginJSON.getAccessToken());
        investmentAPPData = reportService.processFinancialTransactions(financialTransactions,invoices,userId,currencyFactor);
        System.out.println("Financial transactions processed - OK - "+getTime());

        balancePEN = investmentAPPData.getSolesAmountAvailable();
        balanceUSD = investmentAPPData.getDollarAmountAvailable();
        model.addAttribute("balancePEN", balancePEN);
        model.addAttribute("balanceUSD", balanceUSD);
        model.addAttribute("totalInvestedPEN", formatter.format(investmentAPPData.getSolesCurrentInvested()));
        model.addAttribute("totalInvestedUSD", formatter.format(investmentAPPData.getDollarCurrentInvested()));
        model.addAttribute("totalInvested", formatter.format(investmentAPPData.getSolesCurrentInvested() +
                (investmentAPPData.getDollarCurrentInvested()*currencyFactor)));
        model.addAttribute("totalDepositsPEN", formatter.format(investmentAPPData.getSolesTotalDeposited()));
        model.addAttribute("totalDepositsUSD", formatter.format(investmentAPPData.getDollarTotalDeposited()));
        model.addAttribute("totalDeposits", formatter.format(investmentAPPData.getSolesTotalDeposited() +
                (investmentAPPData.getDollarTotalDeposited()*currencyFactor)));
        model.addAttribute("totalProfitPEN", formatter.format(investmentAPPData.getSolesTotalProfit()));
        model.addAttribute("totalProfitUSD", formatter.format(investmentAPPData.getDollarTotalProfit()));
        model.addAttribute("totalProfit", formatter.format(investmentAPPData.getSolesTotalProfit() +
                (investmentAPPData.getDollarTotalProfit()*currencyFactor)));
        model.addAttribute("expectedProfitPEN", formatter.format(investmentAPPData.getSolesProfitExpected()));
        model.addAttribute("expectedProfitUSD", formatter.format(investmentAPPData.getDollarProfitExpected()));
        model.addAttribute("expectedProfit", formatter.format(investmentAPPData.getSolesProfitExpected() +
                (investmentAPPData.getDollarProfitExpected()*currencyFactor)));
        model.addAttribute("onRiskPEN", formatter.format(investmentAPPData.getSolesOnRisk()));
        model.addAttribute("onRiskUSD", formatter.format(investmentAPPData.getDollarOnRisk()));
        model.addAttribute("totalOnRisk", formatter.format(investmentAPPData.getSolesOnRisk() +
                (investmentAPPData.getDollarOnRisk()*currencyFactor)));

        session.setAttribute("finalizedInv",investmentAPPData.getInvoiceTransactionsList());
        session.setAttribute("investments",investmentAPPData.getCurrentInvestmentsIndex());
        session.setAttribute("latestInvestments", investmentAPPData.getLastInvestments());
        model.addAttribute("currencyFactor", currencyFactor);
        return "investmentAPP";
    }

    @PostMapping(value = "/investmentAPP")
    public String investmentAPPForm(@ModelAttribute("investmentForm") InvestmentForm investmentForm, HttpServletRequest request,
                               Model model) {
        HttpSession session = request.getSession();
        List<Investment> temp = getInvestCollection(investmentForm);
        if(temp == null){
            model.addAttribute("error", "true");
            return "redirect:/investmentAPP";
        }
        investmentBlockInv.setInvestmentList(temp);
        if(!investmentForm.getScheduledTime().replace(",","").equals("none")){
            investmentBlockInv.setScheduledTime(investmentForm.getScheduledTime().replace(",",""));
            investmentBlockInv.setScheduled(true);
        }else investmentBlockInv.setScheduled(false);
        session.setAttribute("investmentAPPCollection", investmentBlockInv);
        return "redirect:/waitForInvoice";
    }

    /*@GetMapping({"/facturedo"})
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

        //session.setAttribute("latestInvestments",reportService.getAllByUserAndSystemId(userId,investmentBlockInv.getSystem()));
        session.setAttribute("factuInvestments",reportService.getProcessedResultsFactu(factuData,loginJSON).getResultsInProgress());
        session.setAttribute("factuFinalizedInv",reportService.getFactuFinalizedInvoices(loginJSON));

        return "facturedo";
    }*/

    @PostMapping(value = "/facturedo")
    public String factuForm(@ModelAttribute("investmentForm") InvestmentForm investmentForm, HttpServletRequest request,
                            Model model) {
        HttpSession session = request.getSession();
        List<Investment> temp = getInvestCollection(investmentForm);
        if(temp == null){
            model.addAttribute("error", "true");
            return "investmentV1APP";
        }
        investmentBlockInv.setInvestmentList(temp);
        if(!investmentForm.getScheduledTime().replace(",","").equals("none")){
            investmentBlockInv.setScheduledTime(investmentForm.getScheduledTime().replace(",",""));
            investmentBlockInv.setScheduled(true);
        }else investmentBlockInv.setScheduled(false);
        session.setAttribute("investmentAPPCollection", investmentBlockInv);
        return "redirect:/waitForInvoice";
    }

    @RequestMapping(value="/smartTestConnection",method = RequestMethod.GET)
    public @ResponseBody boolean testConnection(@ModelAttribute("smartUser") SmartUserDto smartUserDto) {
        try {
            return investmentAPPCIG.getAuthentications(smartUserDto) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @GetMapping({"/monthlyAnalysis"})
    public String monthlyAnalysis(HttpSession session){
        session.setAttribute("latestInvestments",investmentAPPData.getLastInvestments());
        session.setAttribute("finalizedInv",investmentAPPData.getInvoiceTransactionsList());
        session.setAttribute("investments",investmentAPPData.getCurrentInvestmentsIndex());
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
            auth = investmentAPPCIG.getAuthentications(smartUserDto);
        } catch (IOException e) {
            return "redirect:/smartUserProfile?saved=false";
        }
        if(auth !=null) {
            SmartUser smartUser = new SmartUser();
            smartUser.setPassword(smartUserDto.getPassword());
            smartUser.setEmail(smartUserDto.getEmail());
            smartUser.setUser(user);
            userService.saveinvestmentAPPProfile(smartUser);
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
