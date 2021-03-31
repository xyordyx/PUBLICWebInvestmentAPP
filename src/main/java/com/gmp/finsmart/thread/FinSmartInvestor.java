package com.gmp.finsmart.thread;

import com.gmp.hmviking.LoginJSON;
import com.gmp.finsmart.FinSmartUtil;
import com.gmp.hmviking.QueueStructure;
import com.gmp.persistence.model.User;
import com.gmp.service.IReportService;
import com.gmp.web.dto.Investment;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.gmp.hmviking.InvestmentUtil.*;

public class FinSmartInvestor implements Callable<Investment> {
    private QueueStructure queueStr;
    private Investment investment;
    private LoginJSON loginJSON;
    Instant start = null;
    private String scheduleTime;
    @Autowired
    private IReportService reportService;
    private User userId;
    private String systemId;
    private int timeRequest;
    private boolean sleep;

    public FinSmartInvestor(QueueStructure queueStr, Investment investment, LoginJSON loginJSON, String scheduleTime,
                            IReportService reportService, User userId, String systemId, int timeRequest,  boolean sleep){
        this.queueStr = queueStr;
        this.investment = investment;
        this.loginJSON = loginJSON;
        this.scheduleTime = scheduleTime;
        this.reportService = reportService;
        this.userId = userId;
        this.systemId = systemId;
        this.timeRequest = timeRequest;
        this.sleep = sleep;
    }

    @Override
    public Investment call() {
        if(scheduleTime != null) {
            try {
                System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " scheduled - " + getTime());
                TimeUnit.MILLISECONDS.sleep(timesDiff(scheduleTime));
                System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " STARTED - " + getTime());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " was awakened - " + getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        start = Instant.now();
        System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " STARTED - " + getTime());
        while (!queueStr.isCancelled()) {
            synchronized (queueStr) {
                /*try {
                    queueStr.wait();
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+": Investor stopped - " + getTime());
                    queueStr.setActualSize(queueStr.getActualSize()-1);
                    break;
                }*/
                try {
                    if(!sleep){
                        TimeUnit.MILLISECONDS.sleep(timeRequest);
                    }else TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+": Investor stopped - " + getTime());
                    queueStr.setActualSize(queueStr.getActualSize()-1);
                    break;
                }
                if(queueStr.getQueue().size() > 0){
                    if(queueStr.getQueue().element().size() > 0){
                        investment = FinSmartUtil.waitForInvoiceInvest(queueStr.getQueue().element(), investment);
                        try {
                            if(investment.getOpportunity() != null) {
                                investment = FinSmartUtil.generateAndSubmit(investment,loginJSON);
                                if(investment.isCompleted()){
                                    System.out.println("Customer " + investment.getOpportunity().getDebtor().getCompanyName()+" Invoice: "+
                                            investment.getOpportunity().getPhysicalInvoices().get(0).getCode() + " - Status: " +
                                            investment.getStatus() +" - Message: "+
                                            investment.getMessage()+" - "+getTime());
                                    queueStr.setActualSize(queueStr.getActualSize()-1);
                                    reportService.updateInvestmentStatus(investment,userId,systemId);
                                    return investment;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(minutesElapsed(start, Instant.now()) >= 15){
                System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+
                        " investor stopped after 15 minutes - "+getTime());
                investment.setStatus("false");
                investment.setMessage("Invoice could not be found");
                reportService.updateInvestmentStatus(investment,userId,systemId);
                return investment;
            }
        }
        return null;
    }
}


