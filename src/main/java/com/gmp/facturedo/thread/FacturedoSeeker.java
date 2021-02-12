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

    private int i=0;

    public FacturedoSeeker(QueueStructure queueStr, LoginJSON loginJSON, String scheduleTime){
        this.queueStr = queueStr;
        this.loginJSON = loginJSON;
        this.scheduleTime = scheduleTime;
    }

    @Override
    public void run() {
        if(scheduleTime != null) {
            try {
                System.out.println(Thread.currentThread().getName() + "FactuSeeker - scheduled - " + getTime());
                TimeUnit.MILLISECONDS.sleep(timesDiff(scheduleTime));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "FactuSeeker - was awakened - " + getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        start = Instant.now();
        List<Results> jsonList;
        while (queueStr.getActualSize()!=0 && !queueStr.isCancelled()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+": FactuSeeker - OP seeker stopped - " + getTime());
                break;
            }
            if (minutesElapsed(start, Instant.now()) >= 15) {
                System.out.println(Thread.currentThread().getName()+": FactuSeeker - OP seeker stopped after 15 minutes - " + getTime());
                break;
            }
            synchronized (queueStr) {
                if (queueStr.getQueueResults().size() > 0) {
                    queueStr.getQueueResults().remove();
                }
                jsonList = getOpportunitiesJSON(loginJSON).getResults();
                //jsonList = FacturedoUtil.getOpportunities(i).getResults();
                if(jsonList != null) {
                    if (jsonList.size() > 0) {
                        queueStr.getQueueResults().add(jsonList);
                        System.out.println(Thread.currentThread().getName()+": FactuSeeker - Added opportunities to queue - " + getTime());
                        queueStr.notifyAll();
                    } else System.out.println("No opportunities were found - " + getTime());
                }
                i++;
            }
        }
    }

    public QueueStructure getStatus(){
        return this.queueStr;
    }
}