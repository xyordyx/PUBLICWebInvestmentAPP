package com.gmp.finsmart.thread;

import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;
import com.gmp.hmviking.ResponseJSON;
import com.gmp.web.dto.Investment;


import java.util.concurrent.*;

import static com.gmp.finsmart.FinSmartUtilParked.*;
import static com.gmp.finsmart.FinSmartUtilParked.updateInvestment;
import static com.gmp.hmviking.InvestmentUtil.getTime;
import static com.gmp.hmviking.InvestmentUtil.timesDiff;

public class FinSmartInvestorParked extends Thread {
    private QueueStructure queueStr;
    private Investment investment;
    private LoginJSON loginJSON;
    private String scheduleTime;
    private boolean flag;
    private ExecutorService poolSubmit;

    private static final String amountBigger = "INVESTMENTS.INVESTMENT_AMOUNT_IS_BIGGER_THAN_TARGET_INVOICE_AVAILABLE_BALANCE";
    private static final String notPublished = "INVESTMENTS.TARGET_INVOICE_NOT_PUBLISHED";

    public FinSmartInvestorParked(Investment investment, LoginJSON loginJSON,QueueStructure queueStructure, String scheduleTime,
                                  ExecutorService poolSubmit){
        this.investment = investment;
        this.loginJSON = loginJSON;
        this.queueStr = queueStructure;
        this.scheduleTime = scheduleTime;
        this.flag = true;
        this.poolSubmit = poolSubmit;
    }

    @Override
    public void run() {
        if(this.scheduleTime != null) {
            System.out.println(Thread.currentThread().getName() + ":Investor - scheduled - " + getTime());
            try {
                TimeUnit.MILLISECONDS.sleep(timesDiff(scheduleTime));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ":Investor - was awakened - " + getTime());
                flag = false;
            }
            this.scheduleTime = null;
        }
        if(investment.getOpportunity() != null && !Thread.currentThread().isInterrupted() && flag){
            System.out.println(Thread.currentThread().getName()+"Invest:"+getTime()+investment.getInvoiceNumber()+ " - STARTED");
            Future<Investment> future = poolSubmit.submit(callable);
            try {
                if(future.get() !=  null){
                    if(future.get().isCompleted()){
                        queueStr.setActualSize(queueStr.getActualSize()-1);
                        queueStr.getInvestmentList().set(queueStr.getInvestmentList().indexOf(future.get()),future.get());
                    }
                }
            } catch (InterruptedException e) {
                poolSubmit.shutdown();
                poolSubmit.shutdownNow();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    Callable<Investment> callable = new Callable<Investment>() {
        @Override
        public Investment call() {
            ResponseJSON responseJSON;
            double actualAmount;
            responseJSON = postToFinSmart(investment.getAmount(),investment,loginJSON);
            actualAmount = investment.getAmount();
            while(responseJSON.getMessage().replace('"',' ').equals(notPublished) &&
                    !Thread.currentThread().isInterrupted()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+"Submit:"+getTime()+investment.getInvoiceNumber()+ " - Stopped");
                }
                responseJSON = postToFinSmart(investment.getAmount(),investment,loginJSON);
            }
            if (responseJSON.getMessage().replace('"',' ').equals(amountBigger)
                    && !Thread.currentThread().isInterrupted()){
                investment = updateOpportunity(loginJSON,investment);
                responseJSON = postToFinSmart(investment.getOpportunity().getAvailableBalanceAmount(),
                        investment, loginJSON);
                actualAmount = investment.getOpportunity().getAvailableBalanceAmount();
                investment = updateInvestment(investment, responseJSON, 4);
                investment.setAdjustedAmount(actualAmount);
            } else investment = updateInvestment(investment, responseJSON, 1);

            investment.setCompleted(true);
            System.out.println(Thread.currentThread().getName()+"Invest:"+getTime()+
                    investment.getOpportunity().getPhysicalInvoices().get(0).getCode()+" "+
                    investment.getOpportunity().getDebtor().getCompanyName()+" STATUS: "+
                    investment.getStatus()+ " MSG:"+investment.getMessage()+" Amount:"+actualAmount);
            return investment;
        }
    };
}


