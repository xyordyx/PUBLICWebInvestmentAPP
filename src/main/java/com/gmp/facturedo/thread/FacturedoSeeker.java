package com.gmp.facturedo.thread;

import com.gmp.facturedo.JSON.Results;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;

import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gmp.facturedo.FacturedoCIG.getOpportunitiesJSON;
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
        if(scheduleTime != null) {
            try {
                System.out.println(Thread.currentThread().getName() + "FactuSeeker - scheduled - " + getTime());
                TimeUnit.MILLISECONDS.sleep(timesDiff(scheduleTime));
                System.out.println(Thread.currentThread().getName() + ":Seeker - STARTED with timeRequest:"+timeRequest+ " - "+ getTime());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "FactuSeeker - was awakened - " + getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        start = Instant.now();
        List<Results> jsonList;
        int temp = 0;
        System.out.println(Thread.currentThread().getName() + ":Seeker - STARTED with timeRequest:"+timeRequest+ " - "+ getTime());
        while (queueStr.getActualSize()!=0 && !queueStr.isCancelled()) {
            synchronized (queueStr) {
                if (queueStr.getQueueResults().size() > 0) {
                    queueStr.getQueueResults().clear();
                }
                jsonList = getOpportunitiesJSON(loginJSON,timeRequest).getResults();
                //jsonList = FacturedoUtil.getOpportunities(i).getResults();
                if(jsonList != null) {
                    if (jsonList.size() > 0) {
                        queueStr.getQueueResults().add(jsonList);
                        System.out.println(Thread.currentThread().getName()+": FactuSeeker - Added opportunities to queue - " + getTime());
                        queueStr.notifyAll();
                         /*if(temp != jsonList.size()){
                            temp = jsonList.size();
                            System.out.println(Thread.currentThread().getName()+": Seeker - Added opportunities to queue - " + getTime());
                        }*/
                    }
                }
                i++;
            }
            if (minutesElapsed(start, Instant.now()) >= 15) {
                System.out.println(Thread.currentThread().getName()+": FactuSeeker - OP seeker stopped after 15 minutes - " + getTime());
                break;
            }
            if(!sleep){
                try {
                    TimeUnit.MILLISECONDS.sleep(timeRequest);
                    //System.out.println(Thread.currentThread().getName()+ getTime());
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+": FactuSeeker - OP seeker stopped - " + getTime());
                    break;
                }
            }
        }
    }

    public QueueStructure getStatus(){
        return this.queueStr;
    }
}