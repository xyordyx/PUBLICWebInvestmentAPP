package com.gmp.finsmart.thread;

import com.gmp.finsmart.FinSmartSeekerImpl;
import com.gmp.finsmart.FinSmartUtil;
import com.gmp.finsmart.JSON.Opportunities;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;
import com.gmp.persistence.model.User;
import com.gmp.service.IReportService;
import com.gmp.web.dto.Investment;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.gmp.hmviking.InvestmentUtil.*;


public class FinSmartSeekerThread extends Thread {
    private QueueStructure queueStructure;
    Instant start = null;
    private LoginJSON loginJSON;
    private String scheduleTime;
    private int timeRequest;
    private static volatile List<Opportunities> jsonList = null;
    private boolean flag;

    private List<Investment> investmentList;
    @Autowired
    private IReportService reportService;
    private User userId;
    private String systemId;
    private ExecutorService pool;

    private int i=0;

    public FinSmartSeekerThread(LoginJSON loginJSON, String scheduleTime, int timeRequest,QueueStructure queueStructure,
                                List<Investment> investList,IReportService reportService, User id, String systemId){
        this.loginJSON = loginJSON;
        this.scheduleTime = scheduleTime;
        this.timeRequest = timeRequest;
        this.flag = true;
        this.queueStructure = queueStructure;
        this.investmentList = investList;
        this.reportService = reportService;
        this.userId = id;
        this.systemId = systemId;
    }

    @Override
    public void run() {
        pool = Executors.newFixedThreadPool(investmentList.size());
        FinSmartSeekerImpl seeker = new FinSmartSeekerImpl(loginJSON.getAccessToken());
        if(this.scheduleTime != null) {
            System.out.println(Thread.currentThread().getName() + ":Seeker - scheduled - " + getTime());
            try {
                Thread.sleep(timesDiff(scheduleTime));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": Seeker - was awakened - " + getTime());
                flag = false;
            }
            this.scheduleTime = null;
        }
        queueStructure.setScheduled(false);
        System.out.println(Thread.currentThread().getName()+ "Seeker:"+getTime() +" STARTED with timeRequest:"+timeRequest);
        start = Instant.now();
        while (queueStructure.getActualSize() !=0 && flag) {
            jsonList = null;
            while(jsonList == null){
                Thread threadSeeker = new Thread(seeker);
                threadSeeker.start();
                try {
                    Thread.sleep(this.timeRequest);
                } catch (InterruptedException e) {
                    flag = false;
                }
                //jsonList = FinSmartUtil.getOpportunities(i);
                jsonList = seeker.getJsonList();
                threadSeeker.interrupt();
            }
            Investment currentInvest = FinSmartUtil.waitForInvoiceInvest(Thread.currentThread().getName(),
                    jsonList,investmentList);
            if(currentInvest != null){
                FinSmartInvestorThread investorThread = new FinSmartInvestorThread(currentInvest,loginJSON,
                        reportService, userId, systemId, queueStructure);
                pool.execute(investorThread);
                investmentList.remove(currentInvest);
            }
            if (minutesElapsed(start, Instant.now()) >= 15) {
                System.out.println(Thread.currentThread().getName()+": Seeker - OP seeker stopped after 15 minutes - "
                        + getTime());
                flag = false;
                break;
            }
            i++;
        }
        System.out.println(Thread.currentThread().getName()+": Seeker - OP seeker stopped - " + getTime());
    }
}