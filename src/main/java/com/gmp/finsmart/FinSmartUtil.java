package com.gmp.finsmart;

import com.gmp.finsmart.JSON.*;

import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.ResponseJSON;
import com.gmp.web.dto.Investment;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.*;
import java.util.stream.Collectors;

import static com.gmp.finsmart.FinSmartCIG.*;
import static com.gmp.hmviking.InvestmentUtil.getTime;


public class FinSmartUtil {

    private static String op = "[]";
    private static String op2 = "[\n" +
            "  {\n" +
            "    \"bankAccount\": \"5f98e5f0d305f934581de54c\", \n" +
            "    \"status\": \"partially sold\", \n" +
            "    \"minimumDuration\": 154, \n" +
            "    \"tem\": \"1.2351\", \n" +
            "    \"debtorRuc\": \"20479675971\", \n" +
            "    \"debtorAddress\": \"cal.huayna capac nro. 1795 lambayeque - chiclayo - la victoria\", \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"expirationDate\": \"2021-03-31\", \n" +
            "    \"updatedAt\": \"2020-10-28T22:27:27.269Z\", \n" +
            "    \"debtorCompanyName\": \"leoncito sociedad anonima\", \n" +
            "    \"debtorContactEmail\": \"rbarrantes@grupoleoncito.com.pe\", \n" +
            "    \"netAmount\": \"122151.00\", \n" +
            "    \"createdAt\": \"2020-10-28T18:04:45.234Z\", \n" +
            "    \"advancePercentage\": \"90.00\", \n" +
            "    \"reservationPercentage\": \"10.00\", \n" +
            "    \"isConfirming\": true, \n" +
            "    \"distributionPercentage\": \"88.22\", \n" +
            "    \"paymentDate\": \"2021-03-31\", \n" +
            "    \"tea\": \"15.87\", \n" +
            "    \"debtorContactName\": \"ruben barrantes becerra\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fa72\", \n" +
            "      \"companyName\": \"leoncito sociedad anonima\", \n" +
            "      \"companyRuc\": \"20478154284\"\n" +
            "    },\n"+
            "    \"advanceAmount\": \"109935.90\", \n" +
            "    \"availableBalancePercentage\": \"22.71\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"109130.00\", \n" +
            "        \"code\": \"F003-085094\", \n" +
            "        \"netAmount\": \"109130.00\", \n" +
            "        \"retentionAmount\": \"0.00\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"totalAmount\": \"13021.00\", \n" +
            "        \"code\": \"F003-085095\", \n" +
            "        \"netAmount\": \"13021.00\", \n" +
            "        \"retentionAmount\": \"0.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"debtorContactPhone\": \"923441113\", \n" +
            "    \"availableBalanceAmount\": \"100.00\", \n" +
            "    \"toBeCollectedIn\": 154, \n" +
            "    \"emailLog\": [\n" +
            "      {\n" +
            "        \"type\": \"CONFORMIDAD_DEUDOR\", \n" +
            "        \"createdAt\": \"2020-10-28T19:29:15.552Z\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"tdmPercentage\": \"1.40\", \n" +
            "    \"emissionDate\": \"2020-10-27\", \n" +
            "    \"_id\": \"5f99b2bda3a3f80008d21951\"\n" +
            "  } \n" +
            "]\n";
    private static String op3 = "[\n" +
            "  {\n" +
            "    \"bankAccount\": \"5f98e5f0d305f934581de54c\", \n" +
            "    \"status\": \"partially sold\", \n" +
            "    \"minimumDuration\": 154, \n" +
            "    \"tem\": \"1.2351\", \n" +
            "    \"debtorRuc\": \"20479675971\", \n" +
            "    \"debtorAddress\": \"cal.huayna capac nro. 1795 lambayeque - chiclayo - la victoria\", \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"expirationDate\": \"2021-03-31\", \n" +
            "    \"updatedAt\": \"2020-10-28T22:27:27.269Z\", \n" +
            "    \"debtorCompanyName\": \"leoncito sociedad anonima\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fa72\", \n" +
            "      \"companyName\": \"leoncito sociedad anonima\", \n" +
            "      \"companyRuc\": \"20478154284\"\n" +
            "    },\n"+
            "    \"debtorContactEmail\": \"rbarrantes@grupoleoncito.com.pe\", \n" +
            "    \"netAmount\": \"122151.00\", \n" +
            "    \"createdAt\": \"2020-10-28T18:04:45.234Z\", \n" +
            "    \"advancePercentage\": \"90.00\", \n" +
            "    \"reservationPercentage\": \"10.00\", \n" +
            "    \"isConfirming\": true, \n" +
            "    \"distributionPercentage\": \"88.22\", \n" +
            "    \"paymentDate\": \"2021-03-31\", \n" +
            "    \"tea\": \"15.87\", \n" +
            "    \"debtorContactName\": \"ruben barrantes becerra\", \n" +
            "    \"advanceAmount\": \"109935.90\", \n" +
            "    \"availableBalancePercentage\": \"22.71\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"109130.00\", \n" +
            "        \"code\": \"F003-085094\", \n" +
            "        \"netAmount\": \"109130.00\", \n" +
            "        \"retentionAmount\": \"0.00\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"totalAmount\": \"13021.00\", \n" +
            "        \"code\": \"F003-085095\", \n" +
            "        \"netAmount\": \"13021.00\", \n" +
            "        \"retentionAmount\": \"0.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"debtorContactPhone\": \"923441113\", \n" +
            "    \"availableBalanceAmount\": \"100.00\", \n" +
            "    \"toBeCollectedIn\": 154, \n" +
            "    \"emailLog\": [\n" +
            "      {\n" +
            "        \"type\": \"CONFORMIDAD_DEUDOR\", \n" +
            "        \"createdAt\": \"2020-10-28T19:29:15.552Z\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"tdmPercentage\": \"1.40\", \n" +
            "    \"emissionDate\": \"2020-10-27\", \n" +
            "    \"_id\": \"5f99b2bda3a3f80008d21951\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"bankAccount\": \"5f98e5f0d305f934581de651\", \n" +
            "    \"minimumDuration\": 16, \n" +
            "    \"tem\": \"1.2204\", \n" +
            "    \"debtorRuc\": \"20100209641\", \n" +
            "    \"debtorAddress\": \"caja arequipa\", \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"expirationDate\": \"2020-11-13\", \n" +
            "    \"updatedAt\": \"2020-10-28T22:26:18.270Z\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fa72\", \n" +
            "      \"companyName\": \"caja municipal de ahorro y credito de arequipa s.a. - caja arequipa\", \n" +
            "      \"companyRuc\": \"20478154284\"\n" +
            "    },\n"+
            "    \"debtorCompanyName\": \"caja municipal de ahorro y credito de arequipa s.a. - caja arequipa\", \n" +
            "    \"netAmount\": \"33941.44\", \n" +
            "    \"createdAt\": \"2020-10-22T02:44:42.000Z\", \n" +
            "    \"advancePercentage\": \"85.00\", \n" +
            "    \"reservationPercentage\": \"15.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"distributionPercentage\": \"67.80\", \n" +
            "    \"paymentDate\": \"2020-11-13\", \n" +
            "    \"tea\": \"15.67\", \n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"advanceAmount\": \"28850.22\", \n" +
            "    \"v1IdGroup\": 4769, \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"38569.44\", \n" +
            "        \"code\": \"E001-11\", \n" +
            "        \"netAmount\": \"33941.44\", \n" +
            "        \"retentionAmount\": \"4628.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"toBeCollectedIn\": 16, \n" +
            "    \"emailLog\": [\n" +
            "      {\n" +
            "        \"type\": \"CONFORMIDAD_DEUDOR\", \n" +
            "        \"createdAt\": \"2020-10-21T22:53:37.000Z\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"type\": \"CESION_DERECHOS\", \n" +
            "        \"createdAt\": \"2020-10-21T22:04:31.000Z\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"type\": \"PRE_LIQUIDACION\", \n" +
            "        \"createdAt\": \"2020-10-21T22:04:31.000Z\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"tdmPercentage\": \"1.80\", \n" +
            "    \"emissionDate\": \"2020-10-14\", \n" +
            "    \"_id\": \"5f98e5f3d305f934581e052f\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"bankAccount\": \"5f98e5f0d305f934581de5a5\", \n" +
            "    \"minimumDuration\": 33, \n" +
            "    \"tem\": \"1.1231\", \n" +
            "    \"debtorRuc\": \"20431871808\", \n" +
            "    \"debtorAddress\": \"av. armendaris nro. 480 int. 402 - miraflores - lima\", \n" +
            "    \"currency\": \"usd\", \n" +
            "    \"expirationDate\": \"2020-11-30\", \n" +
            "    \"updatedAt\": \"2020-10-28T22:25:55.273Z\", \n" +
            "    \"debtorCompanyName\": \"perurail s.a.\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fa72\", \n" +
            "      \"companyName\": \"perurail s.a.\", \n" +
            "      \"companyRuc\": \"20478154284\"\n" +
            "    },\n"+
            "    \"netAmount\": \"2060.12\", \n" +
            "    \"createdAt\": \"2020-10-27T03:54:20.000Z\", \n" +
            "    \"advancePercentage\": \"85.00\", \n" +
            "    \"reservationPercentage\": \"15.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"distributionPercentage\": \"40.11\", \n" +
            "    \"paymentDate\": \"2020-11-30\", \n" +
            "    \"tea\": \"14.34\", \n" +
            "    \"status\": \"partially sold\", \n" +
            "    \"advanceAmount\": \"1751.10\", \n" +
            "    \"v1IdGroup\": 4806, \n" +
            "    \"availableBalancePercentage\": \"1.00\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"2341.12\", \n" +
            "        \"code\": \"0002-000291\", \n" +
            "        \"netAmount\": \"2060.12\", \n" +
            "        \"retentionAmount\": \"281.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"availableBalanceAmount\": \"17.55\", \n" +
            "    \"toBeCollectedIn\": 33, \n" +
            "    \"emailLog\": [\n" +
            "      {\n" +
            "        \"type\": \"CONFORMIDAD_DEUDOR\", \n" +
            "        \"createdAt\": \"2020-10-27T19:52:35.000Z\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"type\": \"CESION_DERECHOS\", \n" +
            "        \"createdAt\": \"2020-10-27T15:06:21.000Z\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"type\": \"PRE_LIQUIDACION\", \n" +
            "        \"createdAt\": \"2020-10-27T15:06:21.000Z\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"tdmPercentage\": \"2.80\", \n" +
            "    \"emissionDate\": \"2020-10-01\", \n" +
            "    \"_id\": \"5f98e5f3d305f934581e0547\"\n" +
            "  }, \n" +
            "{\n" +
            "    \"bankAccount\": \"5f98e5f0d305f934581de78a\", \n" +
            "    \"status\": \"partially sold\", \n" +
            "    \"minimumDuration\": 118, \n" +
            "    \"tem\": \"1.0430\", \n" +
            "    \"debtorRuc\": \"20603080590\", \n" +
            "    \"debtorAddress\": \"av. manuel olguin 375 urb. los granados dpto. 503 limalima- santiago de surco\", \n" +
            "    \"currency\": \"usd\", \n" +
            "    \"expirationDate\": \"2021-02-24\", \n" +
            "    \"updatedAt\": \"2020-10-29T16:43:00.435Z\", \n" +
            "    \"debtorCompanyName\": \"orocom s.a.c.\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fa72\", \n" +
            "      \"companyName\": \"orocom s.a.c.\", \n" +
            "      \"companyRuc\": \"20478154284\"\n" +
            "    },\n"+
            "    \"debtorContactEmail\": \"ksoto@orocom.pe\", \n" +
            "    \"netAmount\": \"192000.00\", \n" +
            "    \"createdAt\": \"2020-10-28T01:32:56.000Z\", \n" +
            "    \"advancePercentage\": \"93.00\", \n" +
            "    \"reservationPercentage\": \"7.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"distributionPercentage\": \"73.45\", \n" +
            "    \"paymentDate\": \"2021-02-24\", \n" +
            "    \"tea\": \"13.26\", \n" +
            "    \"debtorContactName\": \"kathy soto\", \n" +
            "    \"advanceAmount\": \"178560.00\", \n" +
            "    \"v1IdGroup\": 4820, \n" +
            "    \"availableBalancePercentage\": \"28.19\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"200000.00\", \n" +
            "        \"code\": \"F001-00000398\", \n" +
            "        \"netAmount\": \"192000.00\", \n" +
            "        \"retentionAmount\": \"8000.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"debtorContactPhone\": \"993485930\", \n" +
            "    \"availableBalanceAmount\": \"50338.64\", \n" +
            "    \"toBeCollectedIn\": 118, \n" +
            "    \"emailLog\": [\n" +
            "      {\n" +
            "        \"type\": \"CESION_DERECHOS\", \n" +
            "        \"createdAt\": \"2020-10-27T21:12:07.000Z\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"type\": \"PRE_LIQUIDACION\", \n" +
            "        \"createdAt\": \"2020-10-27T21:12:07.000Z\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"type\": \"CONFORMIDAD_DEUDOR\", \n" +
            "        \"createdAt\": \"2020-10-28T16:17:07.275Z\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"type\": \"CONFORMIDAD_DEUDOR\", \n" +
            "        \"createdAt\": \"2020-10-28T17:37:20.303Z\"\n" +
            "      }, \n" +
            "      {\n" +
            "        \"type\": \"DESEMBOLSO\", \n" +
            "        \"createdAt\": \"2020-10-28T23:19:21.022Z\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"tdmPercentage\": \"1.42\", \n" +
            "    \"emissionDate\": \"2020-10-26\", \n" +
            "    \"_id\": \"5f98e5f3d305f934581e0550\"\n" +
            "  }\n" +
            "]\n";

    private static final String amountBigger = " INVESTMENTS.INVESTMENT_AMOUNT_IS_BIGGER_THAN_TARGET_INVOICE_AVAILABLE_BALANCE ";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<String> parseString(String formField){
        return Arrays.stream(formField.split(","))
                .filter(s -> (s != null && s.length() > 0))
                .collect(Collectors.toList());
    }

    public static List<Integer> parseInt(String formField){
        return Arrays.stream(formField.split(","))
                .filter(s -> (s != null && s.length() > 0))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static Investment generateAndSubmit(Investment investment, LoginJSON loginJSON) throws IOException {
        String parameters;
        ResponseJSON responseJSON;
        if(investment.getOpportunity().getAvailableBalanceAmount() >= investment.getAmount()){
            parameters = generateJSONInvest(investment.getAmount(), investment.getCurrency(),investment.getOpportunity().get_id());
            responseJSON = executeInvestment(parameters,loginJSON.getAccessToken());
            System.out.println("Invoice: "+investment.getOpportunity().getPhysicalInvoices().get(0).getCode()+" Buyer: "+
                    investment.getOpportunity().getDebtor().getCompanyName()+" - "+getTime());
            if(responseJSON.isStatus()){
                investment.setStatus("true");
                investment.setCompleted(true);
            }else{
                if(responseJSON.getMessage().replace('"',' ').equals(amountBigger)){
                    investment.setCompleted(false);
                }else {
                    investment.setStatus("false");
                    investment.setMessage(responseJSON.getMessage());
                    investment.setCompleted(true);
                }
            }
        }else {
            //Feature: Auto investment to the current Invoice amount available
            if(investment.getOpportunity().getAvailableBalanceAmount() > 0){
                parameters = generateJSONInvest(investment.getOpportunity().getAvailableBalanceAmount(),
                        investment.getCurrency(),investment.getOpportunity().get_id());
                responseJSON = executeInvestment(parameters,loginJSON.getAccessToken());
                System.out.println("AUTO ADJUSTMENT Invoice: "+investment.getOpportunity().getPhysicalInvoices().get(0).getCode()+" Buyer: "+
                        investment.getOpportunity().getDebtorCompanyName()+
                        " Amount: "+investment.getOpportunity().getDebtor().getCompanyName()+" "+getTime());
                if(responseJSON.isStatus()){
                    investment.setAutoAdjusted(true);
                    investment.setAdjustedAmount(investment.getOpportunity().getAvailableBalanceAmount());
                    investment.setStatus("true");
                }else{
                    investment.setStatus("false");
                    investment.setMessage(responseJSON.getMessage());
                }
            }else{
                investment.setStatus("false");
                investment.setMessage("AMOUNT AVAILABLE IS 0.00");
            }
            investment.setCompleted(true);
        }
        return investment;
    }

    public static String generateJSONInvest(double amount, String currency, String invoice_id){
        return "{\"amount\":\""+amount+"\",\"currency\":\""+currency+"\",\"invoice\":\""+invoice_id+
                "\",\"type\":\"investment\"}";
    }

    public static Investment waitForInvoiceInvest(List<Opportunities> opportunities, Investment formInvestment){
        if(!opportunities.isEmpty()){
            formInvestment=getOpportunityMatch(opportunities,formInvestment);
        }
        return formInvestment;
    }

    public static Investment getOpportunityMatch(List<Opportunities> opportunities, Investment inv){
        for(Opportunities OP : opportunities){
            for(PhysicalInvoices physicalInvoices : OP.getPhysicalInvoices()){
                if(physicalInvoices.getCode().equals(inv.getInvoiceNumber()) && OP.getCurrency().equals(inv.getCurrency())){
                    inv.setOpportunity(OP);
                    return inv;
                }
            }
        }
        return inv;
    }

    public static List<Opportunities> getOpportunities(int i){
        Type founderListType = new TypeToken<ArrayList<Opportunities>>(){}.getType();
        if(i>2 && i <5){
            return gson.fromJson(op2, founderListType);
        }
        else if (i>5 && i <10){
            return gson.fromJson(op3, founderListType);
        }
        else return gson.fromJson(op, founderListType);
    }

    public static double calculateROI(double tem, int days, double amount){
        return BigDecimal.valueOf(((tem/30)*amount*days)*0.01)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static Map<String, InvoiceTransactions> indexInvoices(List<InvoiceTransactions> invoices){
        Map<String,InvoiceTransactions> invoicesIndex = new HashMap<String,InvoiceTransactions>();
        for(InvoiceTransactions inv : invoices){
            invoicesIndex.put(inv.get_id(),inv);
        }
        return invoicesIndex;
    }

    public static String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
