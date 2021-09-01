package com.gmp.finsmart;

import com.gmp.finsmart.JSON.Opportunities;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.ResponseJSON;
import com.gmp.web.dto.Investment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.gmp.hmviking.InvestmentUtil.getTime;


public class FinSmartUtilParked {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static ResponseJSON postToFinSmart(double amount, Investment investment,
                                              LoginJSON json){
        String parameters;
        ResponseJSON responseJSON = null;
        parameters = generateJSONInvest(amount, investment.getCurrency(), investment.getOpportunity().getId());
        while(responseJSON == null){
            responseJSON = FinSmartCIG.executeInvestment1(parameters,json.getAccessToken());
        }
        return responseJSON;
    }

    public static Investment updateOpportunity(LoginJSON loginJSON, Investment parked){
        List<Opportunities> opportunities = FinSmartCIG.getOpportunitiesJSON(loginJSON.getAccessToken());
        while(opportunities == null){
            opportunities = FinSmartCIG.getOpportunitiesJSON(loginJSON.getAccessToken());
        }
        return FinSmartUtilParked.updateParkedOpp(Thread.currentThread().getName(),
                    opportunities,parked);
    }

    public static Investment updateInvestment(Investment investment, ResponseJSON responseJSON, int status){
        if(responseJSON == null || status == 3) {
            investment.setStatus("false");
            investment.setMessage("AMOUNT AVAILABLE IS 0.00");
        }else{
            if (responseJSON.isStatus() && status == 1) {
                investment.setStatus("true");
            } else if (!responseJSON.isStatus() && status == 1) {
                investment.setStatus("false");
                investment.setMessage(responseJSON.getMessage());
            } else if (status == 2) {
                investment.setAutoAdjusted(true);
                investment.setAdjustedAmount(investment.getOpportunity().getAvailableBalanceAmount());
                investment.setStatus("true");
                investment.setMessage("");
            } else if (responseJSON.isStatus() && status == 4) {
                investment.setAutoAdjusted(true);
                investment.setStatus("true");
                investment.setMessage("");
            } else if (!responseJSON.isStatus() && status == 4) {
                investment.setStatus("false");
                investment.setMessage(responseJSON.getMessage());
            }
        }
        return investment;
    }

    public static String generateJSONInvest(double amount, String currency, String invoice_id){
        return "{\"amount\":\""+amount+"\",\"currency\":\""+currency+"\",\"invoice\":\""+invoice_id+
                "\",\"type\":\"investment\"}";
    }

    public static List<Investment> checkForParked(String threadName, List<Opportunities> opportunities,
                                                  List<Investment> invList){
        StringBuilder concat = new StringBuilder();
        List<Investment> investmentList = new ArrayList<>();
        for(Investment inv : invList){
            for(Opportunities op : opportunities){
                concat.append(op.getPhysicalInvoices().get(0).getCode()).append("(").append(op.getAvailableBalanceAmount())
                        .append(")").append(" ");
                if(op.getPhysicalInvoices().contains(inv.getFormCode()) && op.getCurrency().equals(inv.getCurrency())){
                    inv.setOpportunity(op);
                    investmentList.add(inv);
                }
            }
        }
        System.out.println(threadName+"Seeker:"+ getTime()+concat);
        return investmentList;
    }

    public static Investment updateParkedOpp(String threadName, List<Opportunities> opportunities,
                                            Investment parked){
        StringBuilder concat = new StringBuilder();
            for(Opportunities op : opportunities){
                concat.append(op.getPhysicalInvoices().get(0).getCode()).append("(").append(op.getAvailableBalanceAmount())
                        .append(")").append(" ");
                if(op.getPhysicalInvoices().contains(parked.getFormCode()) && op.getCurrency().equals(parked.getCurrency())){
                    parked.setOpportunity(op);
                    return parked;
                }
            }
        System.out.println(threadName+"Seeker:"+ getTime()+concat);
        return null;
    }

    public static List<Opportunities> getOpportunities(boolean i) {
        Type founderListType = new TypeToken<ArrayList<Opportunities>>(){}.getType();
            try {
                if(i) {
                    return gson.fromJson(new String(Files.readAllBytes(Paths.get("src/test/resources/oppInProgress.json"))), founderListType);
                }
                else{
                    return gson.fromJson(new String(Files.readAllBytes(Paths.get("src/test/resources/opportunities.json"))),
                            founderListType);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    public static List<Investment> getInvestmentDiff(List<Investment> ListA, List<Investment> ListB){
        List<Investment> temp = new ArrayList<>();
        for(Investment LA : ListA){
            if(!ListB.contains(LA)){
                LA.setStatus("cancelled");
                temp.add(LA);
            }
        }
        return temp;
    }
}
