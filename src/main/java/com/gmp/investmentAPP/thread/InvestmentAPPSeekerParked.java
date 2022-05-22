package com.gmp.investmentAPP.thread;

import com.gmp.investmentAPP.investmentAPPCIG;
import com.gmp.investmentAPP.investmentAPPUtilParked;
import com.gmp.investmentAPP.JSON.Opportunities;
import com.gmp.hmviking.InvestmentBlock;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;
import com.gmp.persistence.model.User;
import com.gmp.service.IReportService;
import com.gmp.web.dto.Investment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.gmp.investmentAPP.investmentAPPUtilParked.getInvestmentDiff;


public class investmentAPPSeekerParked extends Thread {
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

    public investmentAPPSeekerParked(LoginJSON loginJSON, InvestmentBlock investmentBlock, QueueStructure queueStructure,
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
        //List<Opportunities> opportunities = investmentAPPUtilParked.getOpportunities(false);

        List<Opportunities> opportunities = investmentAPPCIG.getOpportunitiesJSON(loginJSON.getAccessToken());
        while(opportunities == null) opportunities = investmentAPPCIG.getOpportunitiesJSON(loginJSON.getAccessToken());
        List<Investment> currentInvests = investmentAPPUtilParked.checkForParked(Thread.currentThread().getName(),
                opportunities, investmentList);
        if (!currentInvests.isEmpty()) {
            for(Investment inv : currentInvests){
                investmentAPPInvestorParked investorThread = new investmentAPPInvestorParked(inv, loginJSON, queueStructure,
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