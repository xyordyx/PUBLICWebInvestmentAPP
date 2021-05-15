package com.gmp.facturedo.thread;

import com.gmp.hmviking.LoginJSON;
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

import static com.gmp.facturedo.FacturedoUtil.generateAndSubmit;
import static com.gmp.facturedo.FacturedoUtil.waitForInvoiceInvest;
import static com.gmp.hmviking.InvestmentUtil.*;

public class FacturedoInvestor implements Callable<Investment> {
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

    public FacturedoInvestor(QueueStructure queueStr, Investment investment, LoginJSON loginJSON, String scheduleTime,
                             IReportService reportService, User userId, String systemId,int timeRequest){
        this.queueStr = queueStr;
        this.investment = investment;
        this.loginJSON = loginJSON;
        this.scheduleTime = scheduleTime;
        this.reportService = reportService;
        this.userId = userId;
        this.systemId = systemId;
        this.timeRequest = timeRequest;
    }

    @Override
    public Investment call() {
        if(scheduleTime != null) {
            System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " scheduled - " + getTime());
            try {
                Thread.sleep(timesDiff(scheduleTime));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " was awakened - " + getTime());
            }
            scheduleTime = null;
        }
        System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " STARTED - " + getTime());
        start = Instant.now();
        while (!queueStr.isCancelled()) {
            synchronized (queueStr) {
                if(queueStr.getQueueResults().size() > 0){
                    if(queueStr.getQueueResults().element().size() > 0){
                        investment = waitForInvoiceInvest(queueStr.getQueueResults().element(), investment);
                        if(investment.getResults() != null) {
                            try {
                                investment = generateAndSubmit(investment,loginJSON);
                                if(investment.isCompleted()){
                                    System.out.println("Customer " + investment.getResults().getDebtor().getOfficial_name()+" Invoice: "+
                                            investment.getResults().getOperation().getId() + " - Status: " +
                                            investment.getStatus() +" - Message: "+
                                            investment.getMessage()+" - "+getTime());
                                    queueStr.setActualSize(queueStr.getActualSize()-1);
                                    reportService.updateInvestmentStatus(investment,userId,systemId);
                                    return investment;
                                }
                                Thread.sleep(this.timeRequest);
                            } catch (IOException | InterruptedException e) {
                                System.out.println(Thread.currentThread().getName()+": Investor stopped - " + getTime());
                                queueStr.setActualSize(queueStr.getActualSize()-1);
                            }
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


