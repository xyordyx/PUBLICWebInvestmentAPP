package com.gmp.facturedo;

import com.gmp.facturedo.JSON.Auctions;
import com.gmp.facturedo.JSON.Results;
import com.gmp.hmviking.LoginJSON;
import com.gmp.web.dto.Investment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.gmp.facturedo.FacturedoCIG.executeInvestment;
import static com.gmp.hmviking.InvestmentUtil.getTime;
import static com.gmp.hmviking.InvestmentUtil.round;


public class FacturedoUtil {

    private static String op = "{}";
    private static String op2 = "{\n" +
            "    \"count\": 6,\n" +
            "    \"next\": null,\n" +
            "    \"previous\": null,\n" +
            "    \"results\": [\n" +
            "        {\n" +
            "            \"id\": 4681,\n" +
            "            \"end_date\": \"2021-01-22T16:59:00Z\",\n" +
            "            \"operation\": {\n" +
            "                \"id\": \"7c6438pp-e871-4ae9-b4c9-3e2e9b37c5d7\",\n" +
            "                \"amount\": \"99997.00\",\n" +
            "                \"creation_dt\": \"2021-01-21T20:32:18.825852Z\",\n" +
            "                \"currency\": \"PEN\",\n" +
            "                \"cost_time_priority\": 2,\n" +
            "                \"payment_date\": \"2021-05-19\"\n" +
            "            },\n" +
            "            \"start_date\": \"2021-01-22T14:58:34.584956Z\",\n" +
            "            \"status\": 1,\n" +
            "            \"max_monthly_discount_rate\": \"1.02000\",\n" +
            "            \"debtor\": {\n" +
            "                \"id\": \"71363a51-e45d-42b2-9262-135eea61081b\",\n" +
            "                \"official_name\": \"AGRICOLA Y GANADERA CHAVIN DE HUANTAR SA\",\n" +
            "                \"rating\": \"B+\"\n" +
            "            },\n" +
            "            \"percentage_completed\": 0.25932262647339566,\n" +
            "            \"tir\": \"12.95056\",\n" +
            "            \"gross_advance_amount\": 90098\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 4684,\n" +
            "            \"end_date\": \"2021-01-22T16:59:00Z\",\n" +
            "            \"operation\": {\n" +
            "                \"id\": \"d1b851pp-5654-424e-9316-89c5000f58b8\",\n" +
            "                \"amount\": \"58203.00\",\n" +
            "                \"creation_dt\": \"2021-01-22T14:18:47.357658Z\",\n" +
            "                \"currency\": \"PEN\",\n" +
            "                \"cost_time_priority\": 2,\n" +
            "                \"payment_date\": \"2021-04-25\"\n" +
            "            },\n" +
            "            \"start_date\": \"2021-01-22T14:58:39.531316Z\",\n" +
            "            \"status\": 1,\n" +
            "            \"max_monthly_discount_rate\": \"1.55000\",\n" +
            "            \"debtor\": {\n" +
            "                \"id\": \"2757e5a4-82b9-4628-9941-0d169b395bbd\",\n" +
            "                \"official_name\": \"IMPORT NOTEBOOK SOCIEDAD ANONIMA CERRADA - IMPORT NOTEBOOK S.A.C.\",\n" +
            "                \"rating\": \"C-\"\n" +
            "            },\n" +
            "            \"percentage_completed\": 2.5704748366803774,\n" +
            "            \"tir\": \"19.70325\",\n" +
            "            \"gross_advance_amount\": 49596\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 4685,\n" +
            "            \"end_date\": \"2021-01-22T16:59:00Z\",\n" +
            "            \"operation\": {\n" +
            "                \"id\": \"b05672pp-a06d-4219-bc10-494d506f657f\",\n" +
            "                \"amount\": \"40268.58\",\n" +
            "                \"creation_dt\": \"2021-01-22T14:19:34.062989Z\",\n" +
            "                \"currency\": \"PEN\",\n" +
            "                \"cost_time_priority\": 2,\n" +
            "                \"payment_date\": \"2021-04-24\"\n" +
            "            },\n" +
            "            \"start_date\": \"2021-01-22T14:58:40.589778Z\",\n" +
            "            \"status\": 1,\n" +
            "            \"max_monthly_discount_rate\": \"1.55000\",\n" +
            "            \"debtor\": {\n" +
            "                \"id\": \"2757e5a4-82b9-4628-9941-0d169b395bbd\",\n" +
            "                \"official_name\": \"IMPORT NOTEBOOK SOCIEDAD ANONIMA CERRADA - IMPORT NOTEBOOK S.A.C.\",\n" +
            "                \"rating\": \"C-\"\n" +
            "            },\n" +
            "            \"percentage_completed\": 3.138133341103794,\n" +
            "            \"tir\": \"19.84483\",\n" +
            "            \"gross_advance_amount\": 34318\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 4686,\n" +
            "            \"end_date\": \"2021-01-22T16:59:00Z\",\n" +
            "            \"operation\": {\n" +
            "                \"id\": \"21c718pp-d872-4087-be80-db46ad5015ea\",\n" +
            "                \"amount\": \"40805.00\",\n" +
            "                \"creation_dt\": \"2021-01-22T14:20:12.073799Z\",\n" +
            "                \"currency\": \"USD\",\n" +
            "                \"cost_time_priority\": 2,\n" +
            "                \"payment_date\": \"2021-04-23\"\n" +
            "            },\n" +
            "            \"start_date\": \"2021-01-22T14:58:43.318313Z\",\n" +
            "            \"status\": 1,\n" +
            "            \"max_monthly_discount_rate\": \"1.55000\",\n" +
            "            \"debtor\": {\n" +
            "                \"id\": \"2757e5a4-82b9-4628-9941-0d169b395bbd\",\n" +
            "                \"official_name\": \"IMPORT NOTEBOOK SOCIEDAD ANONIMA CERRADA - IMPORT NOTEBOOK S.A.C.\",\n" +
            "                \"rating\": \"C-\"\n" +
            "            },\n" +
            "            \"percentage_completed\": 0.3227633319910257,\n" +
            "            \"tir\": \"20.27050\",\n" +
            "            \"gross_advance_amount\": 34766\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 4679,\n" +
            "            \"end_date\": \"2021-01-22T15:08:47.741856Z\",\n" +
            "            \"operation\": {\n" +
            "                \"id\": \"fe27capp-72df-4b0d-8803-edc01163928b\",\n" +
            "                \"amount\": \"73114.41\",\n" +
            "                \"creation_dt\": \"2021-01-20T22:08:55.093959Z\",\n" +
            "                \"currency\": \"PEN\",\n" +
            "                \"cost_time_priority\": 1,\n" +
            "                \"payment_date\": \"2021-05-18\"\n" +
            "            },\n" +
            "            \"start_date\": \"2021-01-21T15:08:11.021513Z\",\n" +
            "            \"status\": 1,\n" +
            "            \"max_monthly_discount_rate\": \"1.19000\",\n" +
            "            \"debtor\": {\n" +
            "                \"id\": \"b182abb5-3d75-41d7-a764-4107e99a65c2\",\n" +
            "                \"official_name\": \"INMOBILIARIA ESPERANZA REAL SOCIEDAD ANONIMA - INMOBILIARIA ESPERANZA REAL S.A.\",\n" +
            "                \"rating\": \"B-\"\n" +
            "            },\n" +
            "            \"percentage_completed\": 0.5576736975694389,\n" +
            "            \"tir\": \"15.25271\",\n" +
            "            \"gross_advance_amount\": 62249\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 4678,\n" +
            "            \"end_date\": \"2021-01-22T14:13:43.373311Z\",\n" +
            "            \"operation\": {\n" +
            "                \"id\": \"d9ac1epp-62f7-4df1-8155-4bc95b067e33\",\n" +
            "                \"amount\": \"37757.06\",\n" +
            "                \"creation_dt\": \"2021-01-20T22:41:21.398405Z\",\n" +
            "                \"currency\": \"USD\",\n" +
            "                \"cost_time_priority\": 1,\n" +
            "                \"payment_date\": \"2021-03-19\"\n" +
            "            },\n" +
            "            \"start_date\": \"2021-01-21T15:08:09.271886Z\",\n" +
            "            \"status\": 1,\n" +
            "            \"max_monthly_discount_rate\": \"0.97000\",\n" +
            "            \"debtor\": {\n" +
            "                \"id\": \"160044d8-b94c-47d5-8e80-d0f224dfc61c\",\n" +
            "                \"official_name\": \"FUNDO LOS PALTOS SOCIEDAD ANONIMA CERRADA\",\n" +
            "                \"rating\": \"A-\"\n" +
            "            },\n" +
            "            \"percentage_completed\": 0.5219953935696723,\n" +
            "            \"tir\": \"12.28152\",\n" +
            "            \"gross_advance_amount\": 32129\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Investment waitForInvoiceInvest(List<Results> results, Investment formInvestment){
        if(!results.isEmpty()){
            for(Results rst : results){
                if(Arrays.stream(rst.getOperation().getId().split("-")).findFirst().get()
                        .equals(formInvestment.getInvoiceNumber())
                        && rst.getOperation().getCurrency().equals(formInvestment.getCurrency())){
                    formInvestment.setResults(rst);
                    return formInvestment;
                }
            }
        }
        return formInvestment;
    }

    public static Investment generateAndSubmit(Investment investment, LoginJSON loginJSON) throws IOException {
        JsonObject response;
        JSONObject parameters;
        if((investment.getResults().getPercentage_completed()*100) < 100){
            parameters = generateJSONInvest(investment.getResults().getId(),investment.getAmount(),
                    investment.getResults().getMax_monthly_discount_rate());
            System.out.println("Invoice: "+investment.getResults().getId()+" Buyer: "+
                            investment.getResults().getDebtor().getOfficial_name()+" - "+getTime());
            response = executeInvestment(parameters,loginJSON.getToken());
            if(response.get("status").toString().equals("true")){
                investment.setStatus("true");
                investment.setCompleted(true);
            }else{
                investment.setStatus("false");
                investment.setMessage(response.get("message").toString());
                investment.setCompleted(true);
            }
        }
        return investment;
    }

    public static JSONObject generateJSONInvest(int id, double amount, double discount){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount);
        jsonObject.put("auction", id);
        jsonObject.put("min_discount_rate", round(discount,2));
        jsonObject.put("discount_rate", round(discount,2));
        System.out.println("JSON created for:"+amount+" "+id+"     - OK - "+getTime());
        return jsonObject;
    }

    public static Auctions getOpportunities(int i){
        if(i>2 && i <10){
            return gson.fromJson(op2, Auctions.class);
        }
        else return gson.fromJson(op, Auctions.class);
    }
}
