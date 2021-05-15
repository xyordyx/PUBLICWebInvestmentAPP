package com.gmp.finsmart.thread;

import com.gmp.finsmart.FinSmartUtil;
import com.gmp.hmviking.LoginJSON;
import com.gmp.finsmart.JSON.Opportunities;
import com.gmp.hmviking.QueueStructure;

import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gmp.finsmart.FinSmartCIG.*;
import static com.gmp.hmviking.InvestmentUtil.*;


public class FinSmartSeeker extends Thread {
    private QueueStructure queueStr;
    Instant start = null;
    private LoginJSON loginJSON;
    private String scheduleTime;
    private int timeRequest;

    private int i=0;

    public FinSmartSeeker(QueueStructure queueStr, LoginJSON loginJSON, String scheduleTime, int timeRequest){
        this.queueStr = queueStr;
        this.loginJSON = loginJSON;
        this.scheduleTime = scheduleTime;
        this.timeRequest = timeRequest;
    }

    @Override
    public void run() {
        List<Opportunities> jsonList;
        int temp = 0;
        if(this.scheduleTime != null) {
            System.out.println(Thread.currentThread().getName() + ":Seeker - scheduled - " + getTime());
            try {
                Thread.sleep(timesDiff(scheduleTime));
                this.scheduleTime = null;
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": Seeker - was awakened - " + getTime());
            }
        }
        System.out.println(Thread.currentThread().getName() + ":Seeker - STARTED with timeRequest:"+timeRequest+ " - "+ getTime());
        start = Instant.now();
        while (queueStr.getActualSize()!=0) {
            try {
                synchronized (queueStr) {
                    if (queueStr.getQueue().size() > 0) {
                        queueStr.getQueue().clear();
                    }
                    jsonList = getOpportunitiesJSON(loginJSON.getAccessToken());
                    //jsonList = FinSmartUtil.getOpportunities(i);
                    if(jsonList != null) {
                        if (jsonList.size() > 0) {
                            queueStr.getQueue().add(jsonList);
                            queueStr.setUpdateDate(getTime());
                            queueStr.notifyAll();
                            System.out.println(Thread.currentThread().getName()+": Seeker - Added opportunities to queue - " + getTime());
                        /*if(temp != jsonList.size()){
                            temp = jsonList.size();
                            System.out.println(Thread.currentThread().getName()+": Seeker - Added opportunities to queue - " + getTime());
                        }*/
                        }
                    }
                    i++;
                }
                Thread.sleep(this.timeRequest);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+": Seeker - OP seeker stopped - " + getTime());
                break;
            }
            if (minutesElapsed(start, Instant.now()) >= 15) {
                System.out.println(Thread.currentThread().getName()+": Seeker - OP seeker stopped after 15 minutes - " + getTime());
                break;
            }
        }
    }

    public QueueStructure getStatus(){
        return this.queueStr;
    }
}