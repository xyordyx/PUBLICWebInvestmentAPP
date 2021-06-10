package com.gmp.facturedo.thread;

import com.gmp.facturedo.FacturedoCIGSeeker;
import com.gmp.facturedo.FacturedoUtil;
import com.gmp.facturedo.JSON.Auctions;
import com.gmp.facturedo.JSON.Results;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;

import java.time.Instant;
import java.util.HashMap;

import static com.gmp.hmviking.InvestmentUtil.*;


public class FacturedoSeeker extends Thread {
    private QueueStructure queueStructure;
    Instant start = null;
    private LoginJSON loginJSON;
    private String scheduleTime;
    private int timeRequest;

    private int i=0;

    private boolean flag;
    private HashMap<Integer, Results> auctionsMap;

    public FacturedoSeeker(QueueStructure queueStr, LoginJSON loginJSON, String scheduleTime, int timeRequest,
                           HashMap<Integer, Results> auctionsMap){
        this.queueStructure = queueStr;
        this.loginJSON = loginJSON;
        this.scheduleTime = scheduleTime;
        this.timeRequest = timeRequest;
        this.flag = true;
        this.auctionsMap = auctionsMap;
    }

    @Override
    public void run() {
        FacturedoCIGSeeker seeker = new FacturedoCIGSeeker(loginJSON);
        Auctions jsonList;
        if(this.scheduleTime != null) {
            System.out.println(Thread.currentThread().getName() + ":Seeker - scheduled - " + getTime());
            try {
                Thread.sleep(timesDiff(scheduleTime));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ":Seeker - was awakened - " + getTime());
                flag = false;
            }
            this.scheduleTime = null;
        }
        System.out.println(Thread.currentThread().getName() + ": Seeker - STARTED with timeRequest:"+timeRequest+ " - "+ getTime());
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
                //jsonList = FacturedoUtil.getOpportunities(i);
                jsonList = seeker.getJsonList();
                threadSeeker.interrupt();
            }
            auctionsMap = FacturedoUtil.processAuctions(jsonList,auctionsMap);
            System.out.println(Thread.currentThread().getName()+": Seeker - "+jsonList.getCount()+
                    " opportunities in queue - "
                    + getTime()+"   "+FacturedoUtil.getResultCode(jsonList.getResults()));
            if (minutesElapsed(start, Instant.now()) >= 15) {
                System.out.println(Thread.currentThread().getName()+": Seeker - OP seeker stopped after 15 minutes - "
                        + getTime());
                break;
            }
            i++;
        }
        System.out.println(Thread.currentThread().getName()+": Seeker - OP seeker stopped - " + getTime());
    }

    public QueueStructure getStatus(){
        return this.queueStructure;
    }
}