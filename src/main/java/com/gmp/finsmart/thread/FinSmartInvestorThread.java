package com.gmp.finsmart.thread;

import com.gmp.finsmart.FinSmartUtil;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;
import com.gmp.persistence.model.User;
import com.gmp.service.IReportService;
import com.gmp.web.dto.Investment;
import org.springframework.beans.factory.annotation.Autowired;

import static com.gmp.hmviking.InvestmentUtil.*;

public class FinSmartInvestorThread extends Thread {
    private QueueStructure queueStr;
    private Investment investment;
    private LoginJSON loginJSON;
    @Autowired
    private IReportService reportService;
    private User userId;
    private String systemId;

    public FinSmartInvestorThread(Investment investment, LoginJSON loginJSON, IReportService reportService, User userId,
                                  String systemId, QueueStructure queueStructure){
        this.investment = investment;
        this.loginJSON = loginJSON;
        this.reportService = reportService;
        this.userId = userId;
        this.systemId = systemId;
        this.queueStr = queueStructure;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"Invest:"+getTime()+investment.getInvoiceNumber()+ " - STARTED");
        if(investment.getOpportunity() != null){
            investment = FinSmartUtil.generateAndSubmit(investment,loginJSON, queueStr.getBalance());
            if(investment.isCompleted()){
                queueStr.setActualSize(queueStr.getActualSize()-1);
                reportService.updateInvestmentStatus(investment,userId,systemId);
                queueStr.getInvestmentList().add(investment);
            }
        }
     }

    public void updateBalance(){
        if(this.investment.isAutoAdjusted()){
            if(this.investment.getCurrency().equals("pen")){
                this.queueStr.getBalance().put("pen",
                        (this.queueStr.getBalance().get("pen"))-this.investment.getAdjustedAmount());
            }else  this.queueStr.getBalance().put("usd",
                    (this.queueStr.getBalance().get("usd"))-this.investment.getAdjustedAmount());
        }else {
            if(this.investment.getCurrency().equals("pen")){
                this.queueStr.getBalance().put("pen",
                        (this.queueStr.getBalance().get("pen"))-this.investment.getAmount());
            }else  this.queueStr.getBalance().put("usd",
                    (this.queueStr.getBalance().get("usd"))-this.investment.getAmount());
        }
    }
}


