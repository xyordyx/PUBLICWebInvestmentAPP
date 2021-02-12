package com.gmp.finsmart.thread;

import com.gmp.hmviking.QueueStructure;

import java.text.ParseException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static com.gmp.hmviking.InvestmentUtil.*;

public class FinSmartUpdater implements Runnable {
    private QueueStructure queueStr;
    Instant start = null;
    private FinSmartSeeker seeker;
    private String scheduleTime;

    public QueueStructure getQueueStr() {
        return queueStr;
    }

    public FinSmartUpdater(FinSmartSeeker seeker, String scheduleTime) {
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
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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
