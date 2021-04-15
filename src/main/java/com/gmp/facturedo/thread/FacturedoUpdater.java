package com.gmp.facturedo.thread;

import com.gmp.hmviking.QueueStructure;

import java.text.ParseException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static com.gmp.hmviking.InvestmentUtil.*;

public class FacturedoUpdater implements Runnable {
    private QueueStructure queueStr;
    Instant start = null;
    private FacturedoSeeker seeker;
    private String scheduleTime;

    public QueueStructure getQueueStr() {
        return queueStr;
    }

    public FacturedoUpdater(FacturedoSeeker seeker, String scheduleTime) {
        this.scheduleTime = scheduleTime;
        this.seeker = seeker;
    }

    @Override
    public void run() {
        if(scheduleTime != null) {
            try {
                System.out.println(Thread.currentThread().getName() + ":UI Updater - scheduled - " + getTime());
                TimeUnit.MILLISECONDS.sleep(timesDiff(scheduleTime));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ":UI Updater - was awakened - " + getTime());
            }
        }
        System.out.println(Thread.currentThread().getName() + ":UI Updater - Started - " + getTime());
        start = Instant.now();
        this.queueStr = seeker.getStatus();
        while (queueStr.getActualSize()!=0 && !queueStr.isCancelled()) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ":UI Updater - Updater stopped - " + getTime());
                break;
            }
            if (minutesElapsed(start, Instant.now()) >= 15) {
                System.out.println(Thread.currentThread().getName() + ":UI Updater - Updater stopped after 15 minutes - " + getTime());
                break;
            }
            this.queueStr = seeker.getStatus();
        }
    }
}
