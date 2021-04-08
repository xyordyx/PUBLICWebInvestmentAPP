package com.gmp.web.controller;

import com.gmp.facturedo.FacturedoCIG;
import com.gmp.facturedo.JSON.*;
import com.gmp.facturedo.thread.FacturedoInvestor;
import com.gmp.facturedo.thread.FacturedoSeeker;
import com.gmp.facturedo.thread.FacturedoUpdater;
import com.gmp.finsmart.JSON.*;
import com.gmp.hmviking.InvestmentBlock;
import com.gmp.finsmart.thread.FinSmartInvestor;
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

import static com.gmp.finsmart.FinSmartCIG.*;
import static com.gmp.hmviking.InvestmentUtil.*;

@Controller
public class HMVikingController {
    private LoginJSON loginJSON;
    private InvestmentBlock investmentBlockInv;
    private ExecutorService pool;
    private FinSmartUpdater updaterFin;
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

    private List<InvoiceTransactions> invoices;

    @GetMapping({"/waitForInvoice"})
    public String waitForInvoice(HttpSession session,Device device)  {
        schedulerSession = session;
        pool = Executors.newFixedThreadPool(investmentBlockInv.getInvestmentList().size()+2);
        QueueStructure queueStructure = new QueueStructure(investmentBlockInv.getInvestmentList().size(),
                finsmartData.getSolesAmountAvailable(), finsmartData.getDollarAmountAvailable());
        if(investmentBlockInv.getSystem().equals("HMFINSMART")) {
            FinSmartSeeker seeker = new FinSmartSeeker(queueStructure, loginJSON, investmentBlockInv.getScheduledTime(),
                    investmentBlockInv.getTimeRequest(),investmentBlockInv.isSleep());
            updaterFin = new FinSmartUpdater(seeker, investmentBlockInv.getScheduledTime());
            pool.execute(seeker);
            pool.execute(updaterFin);
        }else if(investmentBlockInv.getSystem().equals("HMFACTUREDO")){
            FacturedoSeeker seeker = new FacturedoSeeker(queueStructure,loginJSON, investmentBlockInv.getScheduledTime(),
                    investmentBlockInv.getTimeRequest(),investmentBlockInv.isSleep());
            updaterFact = new FacturedoUpdater(seeker, investmentBlockInv.getScheduledTime());
            pool.execute(seeker);
            pool.execute(updaterFact);
        }
        List<Future<Investment>> listOfThreads = new ArrayList<>();
        for(Investment investment : investmentBlockInv.getInvestmentList()) {
            if(investmentBlockInv.isScheduled()){
                investment.setStatus("Scheduled");
            } else investment.setStatus("inProgress");
            if(investmentBlockInv.getSystem().equals("HMFINSMART")) {
                Callable<Investment> callableInvestor = new FinSmartInvestor(queueStructure,investment,loginJSON,
                        investmentBlockInv.getScheduledTime(), reportService, userId, investmentBlockInv.getSystem(),
                        investmentBlockInv.getTimeRequest(),investmentBlockInv.isSleep());
                Future<Investment> futureCounterResult = pool.submit(callableInvestor);
                listOfThreads.add(futureCounterResult);
            }else if(investmentBlockInv.getSystem().equals("HMFACTUREDO")){
                Callable<Investment> callableInvestor = new FacturedoInvestor(queueStructure,investment,loginJSON,
                        investmentBlockInv.getScheduledTime(), reportService, userId, investmentBlockInv.getSystem(),
                        investmentBlockInv.getTimeRequest());
                Future<Investment> futureCounterResult = pool.submit(callableInvestor);
                listOfThreads.add(futureCounterResult);
            }
        }
        session.setAttribute("threadList", listOfThreads);
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
        if(investmentBlockInv.getSystem().equals("HMFINSMART")) {
            if(!investmentBlockInv.isScheduled()){
                updaterFin.getQueueStr().setCancelled(true);
            }
        }else{
            if(!investmentBlockInv.isScheduled()){
                updaterFact.getQueueStr().setCancelled(true);
            }
        }
        pool.shutdownNow();
    }

    @RequestMapping(value="/getDataUX.json",method = RequestMethod.GET)
    public @ResponseBody
    InvestmentBlock manualSendDataUX(){
        if(enabled.get()){
            List<Future<Investment>> listOfInvestments = (List<Future<Investment>>) schedulerSession.getAttribute("threadList");
            if(investmentBlockInv.getSystem().equals("HMFINSMART")) {
                if(updaterFin.getQueueStr()!=null && !flag && investmentBlockInv.isScheduled()){
                    investmentBlockInv.setInvestmentList(setInProgress(investmentBlockInv.getInvestmentList()));
                    flag = true;
                    return investmentBlockInv;
                }
            }else{
                if(updaterFact.getQueueStr()!=null && !flag && investmentBlockInv.isScheduled()){
                    investmentBlockInv.setInvestmentList(setInProgress(investmentBlockInv.getInvestmentList()));
                    flag = true;
                    return investmentBlockInv;
                }
            }
            for (Future<Investment> future : listOfInvestments){
                if(future.isDone()){
                    try {
                        investmentBlockInv.setInvestmentList(updateInvestment(future.get(),
                                investmentBlockInv.getInvestmentList()));
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if(isDone(listOfInvestments)){
                    investmentBlockInv.setTransactionStatus(true);
                    enabled.set(false);
                }
            }
            return investmentBlockInv;
        }
        return null;
    }

    @Scheduled(fixedDelay = 15000)
    private void sendDataUX() {
        if(enabled.get()){
            List<Future<Investment>> listOfInvestments = (List<Future<Investment>>) schedulerSession.getAttribute("threadList");
            if(investmentBlockInv.getSystem().equals("HMFINSMART")) {
                if(updaterFin.getQueueStr()!=null && !flag && investmentBlockInv.isScheduled()){
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
            for (Future<Investment> future : listOfInvestments){
                if(future.isDone()){
                    try {
                        investmentBlockInv.setInvestmentList(updateInvestment(future.get(),
                                investmentBlockInv.getInvestmentList()));
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if(isDone(listOfInvestments)){
                    investmentBlockInv.setTransactionStatus(true);
                    enabled.set(false);
                }
            }
            simpMessagingTemplate.convertAndSend("/finSmart/investments", investmentBlockInv);
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
        FinancialTransactions financialTransactions = getFinancialTransactions(loginJSON.getAccessToken());
        invoices = getInvoices(loginJSON.getAccessToken());
        finsmartData = reportService.processFinancialTransactions(financialTransactions,invoices,userId);
        System.out.println("Financial transactions processed - OK - "+getTime());

        model.addAttribute("balancePEN", finsmartData.getSolesAmountAvailable());
        model.addAttribute("balanceUSD", finsmartData.getDollarAmountAvailable());
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
        if(!investmentForm.getScheduledTime().replace(",","").equals("none")){
            investmentBlockInv.setScheduledTime(investmentForm.getScheduledTime().replace(",",""));
            investmentBlockInv.setScheduled(true);
            return "redirect:/waitForInvoice";
        }
        if(!investmentForm.getTimeRequest().equals("")){
            investmentBlockInv.setTimeRequest((int)(Double.parseDouble(investmentForm.getTimeRequest())*1000));
        }else investmentBlockInv.setTimeRequest(500);
        investmentBlockInv.setScheduled(false);
        investmentBlockInv.setSleep(investmentForm.isSleep());
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
        model.addAttribute("balancePEN", factuData.getSolesAmountAvailable());
        model.addAttribute("balanceUSD", factuData.getDollarAmountAvailable());
        model.addAttribute("totalInvestedPEN", factuData.getSolesCurrentInvested());
        model.addAttribute("totalInvestedUSD", factuData.getDollarCurrentInvested());
        model.addAttribute("totalInvested", factuData.getSolesCurrentInvested() +
                (factuData.getDollarCurrentInvested()*currencyFactor));

        model.addAttribute("totalDepositsPEN", factuData.getSolesTotalTransferred());
        model.addAttribute("totalDepositsUSD", factuData.getDollarTotalTransferred());
        model.addAttribute("totalDeposits", formatter.format(factuData.getSolesTotalTransferred() +
                (factuData.getDollarTotalTransferred()*currencyFactor)));

        model.addAttribute("totalProfitPEN", factuData.getSolesTotalProfit());
        model.addAttribute("totalProfitUSD", factuData.getDollarTotalProfit());
        model.addAttribute("totalProfit", formatter.format(factuData.getSolesTotalProfit() +
                (factuData.getDollarTotalProfit()*currencyFactor)));

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
        if(!investmentForm.getScheduledTime().replace(",","").equals("none")){
            investmentBlockInv.setScheduledTime(investmentForm.getScheduledTime().replace(",",""));
            investmentBlockInv.setScheduled(true);
            return "redirect:/waitForInvoice";
        }
        investmentBlockInv.setScheduled(false);
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
