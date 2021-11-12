package com.gmp.finsmart.thread;

import com.gmp.finsmart.FinSmartCIG;
import com.gmp.finsmart.FinSmartUtilParked;
import com.gmp.finsmart.JSON.Opportunities;
import com.gmp.hmviking.InvestmentBlock;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;
import com.gmp.persistence.model.User;
import com.gmp.service.IReportService;
import com.gmp.web.dto.Investment;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.gmp.finsmart.FinSmartUtilParked.getInvestmentDiff;
import static com.gmp.hmviking.InvestmentUtil.*;


public class FinSmartSeekerParked extends Thread {
    private QueueStructure queueStructure;

    private LoginJSON loginJSON;
    private InvestmentBlock investmentBlock;
    public String scheduleTime;

    private List<Investment> investmentList;
    @Autowired
    private IReportService reportService;
    private User userId;
    private String systemId;
    private ExecutorService pool;
    private ExecutorService poolSubmit;
    private boolean flag;

    public FinSmartSeekerParked(LoginJSON loginJSON, InvestmentBlock investmentBlock, QueueStructure queueStructure,
                                IReportService reportService, User id, ExecutorService pool, ExecutorService poolSubmit){
        this.loginJSON = loginJSON;
        this.investmentBlock = investmentBlock;
        this.queueStructure = queueStructure;
        this.investmentList = investmentBlock.getInvestmentList();
        this.reportService = reportService;
        this.userId = id;
        this.systemId = investmentBlock.getSystem();
        this.pool = pool;
        this.poolSubmit = poolSubmit;
        this.flag = true;
        this.scheduleTime = investmentBlock.getScheduledTime();
    }

    @Override
    public void run() {
        //TEST OPPORTUNITIES
        //List<Opportunities> opportunities = FinSmartUtilParked.getOpportunities(false);

        List<Opportunities> opportunities = FinSmartCIG.getOpportunitiesJSON(loginJSON.getAccessToken());
        while(opportunities == null) opportunities = FinSmartCIG.getOpportunitiesJSON(loginJSON.getAccessToken());
        List<Investment> currentInvests = FinSmartUtilParked.checkForParked(Thread.currentThread().getName(),
                opportunities, investmentList);
        if (!currentInvests.isEmpty()) {
            for(Investment inv : currentInvests){
                FinSmartInvestorParked investorThread = new FinSmartInvestorParked(inv, loginJSON, queueStructure,
                        scheduleTime, poolSubmit);
                pool.execute(investorThread);
                if(investmentBlock.isScheduled()){
                    inv.setStatus("scheduled");
                }else inv.setStatus("inProgress");
                queueStructure.getInvestmentList().add(inv);
            }
        }
        if(queueStructure.getInvestmentList().size() != investmentList.size()){
            for(Investment inv : getInvestmentDiff(investmentList,currentInvests)){
                queueStructure.getInvestmentList().add(inv);
            }
        }
    }

}