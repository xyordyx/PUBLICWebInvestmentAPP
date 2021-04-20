package com.gmp.facturedo.thread;

import com.gmp.facturedo.FacturedoUtil;
import com.gmp.facturedo.JSON.Auctions;
import com.gmp.facturedo.JSON.Results;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;

import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gmp.facturedo.FacturedoCIG.getOpportunitiesJSON;
import static com.gmp.facturedo.FacturedoCIG.opp;
import static com.gmp.hmviking.InvestmentUtil.*;


public class FacturedoSeeker extends Thread {
    private QueueStructure queueStr;
    Instant start = null;
    private LoginJSON loginJSON;
    private String scheduleTime;
    private int timeRequest;
    private boolean sleep;

    private int i=0;

    public FacturedoSeeker(QueueStructure queueStr, LoginJSON loginJSON, String scheduleTime, int timeRequest,
                           boolean sleep){
        this.queueStr = queueStr;
        this.loginJSON = loginJSON;
        this.scheduleTime = scheduleTime;
        this.timeRequest = timeRequest;
        this.sleep = sleep;
    }

    @Override
    public void run() {
        Auctions jsonList;
        List<Results> tempList;
        int temp = 0;
        if(this.scheduleTime != null) {
            System.out.println(Thread.currentThread().getName() + ":Seeker - scheduled - " + getTime());
            try {
                Thread.sleep(timesDiff(scheduleTime));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ":Seeker - was awakened - " + getTime());
            }
            this.scheduleTime = null;
        }
        System.out.println(Thread.currentThread().getName() + ": Seeker - STARTED with timeRequest:"+timeRequest+ " - "+ getTime());
        start = Instant.now();
        while (queueStr.getActualSize()!=0 && !queueStr.isCancelled()) {
            try {
                synchronized (queueStr) {
                    if (queueStr.getQueueResults().size() > 0) {
                        queueStr.getQueueResults().clear();
                    }
                    jsonList = opp(loginJSON);
                    //jsonList = getOpportunitiesJSON(loginJSON);
                    //jsonList = FacturedoUtil.getOpportunities(i);
                    if(jsonList != null) {
                        if (jsonList.getResults() != null) {
                            tempList = jsonList.getResults();
                            if (tempList.size() > 0) {
                                queueStr.getQueueResults().add(tempList);
                                System.out.println(Thread.currentThread().getName() + ": FactuSeeker - Added opportunities to queue - " + getTime());
                                queueStr.notifyAll();
                         /*if(temp != jsonList.size()){
                            temp = jsonList.size();
                            System.out.println(Thread.currentThread().getName()+": Seeker - Added opportunities to queue - " + getTime());
                        }*/
                            }
                        }
                    }
                    i++;
                }
                if(!this.sleep){
                    Thread.sleep(this.timeRequest);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+": Seeker - OP seeker stopped - " + getTime());
                break;
            }
            if (minutesElapsed(start, Instant.now()) >= 15) {
                System.out.println(Thread.currentThread().getName()+": FactuSeeker - OP seeker stopped after 15 minutes - " + getTime());
                break;
            }
        }
    }

    public QueueStructure getStatus(){
        return this.queueStr;
    }
}