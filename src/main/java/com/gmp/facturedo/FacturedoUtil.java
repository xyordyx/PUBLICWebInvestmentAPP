package com.gmp.facturedo;

import com.gmp.facturedo.JSON.Auctions;
import com.gmp.facturedo.JSON.Results;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.ResponseJSON;
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
            "  \"count\": 11, \n" +
            "  \"previous\": null, \n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"D\", \n" +
            "        \"id\": \"6f31a2e1-d09e-4b5c-acc9-bce16a611c35\", \n" +
            "        \"official_name\": \"H.A INGENIERIA Y CONSTRUCCION S.A.C.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"1.54000\", \n" +
            "      \"end_date\": \"2021-04-14T21:06:32.032930Z\", \n" +
            "      \"max_monthly_discount_rate\": \"1.72000\", \n" +
            "      \"tir\": \"22.70894\", \n" +
            "      \"start_date\": \"2021-04-07T16:00:18.011181Z\", \n" +
            "      \"gross_advance_amount\": 28677, \n" +
            "      \"percentage_completed\": 0.9779565505457335, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-08-03\", \n" +
            "        \"cost_time_priority\": 1, \n" +
            "        \"creation_dt\": \"2021-04-06T23:04:11.932229Z\", \n" +
            "        \"currency\": \"USD\", \n" +
            "        \"amount\": \"32442.18\", \n" +
            "        \"id\": \"903b2059-5901-4d95-b516-3d9c5161ffd6\"\n" +
            "      }, \n" +
            "      \"id\": 5264\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"E\", \n" +
            "        \"id\": \"b3b91f5b-57a4-4514-979f-d7c0d415a12d\", \n" +
            "        \"official_name\": \"PHOENIX FOODS S.A.C.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"1.72000\", \n" +
            "      \"end_date\": \"2021-04-14T18:47:55.353787Z\", \n" +
            "      \"max_monthly_discount_rate\": \"1.98000\", \n" +
            "      \"tir\": \"26.52609\", \n" +
            "      \"start_date\": \"2021-04-12T22:01:30.623523Z\", \n" +
            "      \"gross_advance_amount\": 133144, \n" +
            "      \"percentage_completed\": 0.21097248092291054, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-08-09\", \n" +
            "        \"cost_time_priority\": 1, \n" +
            "        \"creation_dt\": \"2021-04-12T21:07:09.114794Z\", \n" +
            "        \"currency\": \"PEN\", \n" +
            "        \"amount\": \"150000.00\", \n" +
            "        \"id\": \"9902c33c-6f26-496f-a01d-8a3fca4f934a\"\n" +
            "      }, \n" +
            "      \"id\": 5294\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"C-\", \n" +
            "        \"id\": \"2757e5a4-82b9-4628-9941-0d169b395bbd\", \n" +
            "        \"official_name\": \"IMPORT NOTEBOOK S.A.C.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"1.41000\", \n" +
            "      \"end_date\": \"2021-04-14T18:20:24.107742Z\", \n" +
            "      \"max_monthly_discount_rate\": \"1.54000\", \n" +
            "      \"tir\": \"20.12846\", \n" +
            "      \"start_date\": \"2021-04-09T21:58:37.471794Z\", \n" +
            "      \"gross_advance_amount\": 52020, \n" +
            "      \"percentage_completed\": 0.4241855055747789, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-07-28\", \n" +
            "        \"cost_time_priority\": 2, \n" +
            "        \"creation_dt\": \"2021-04-09T16:15:51.724133Z\", \n" +
            "        \"currency\": \"USD\", \n" +
            "        \"amount\": \"59224.15\", \n" +
            "        \"id\": \"2f16ab75-c417-48f2-8d47-f48b4876fb5c\"\n" +
            "      }, \n" +
            "      \"id\": 5285\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"C-\", \n" +
            "        \"id\": \"2757e5a4-82b9-4628-9941-0d169b395bbd\", \n" +
            "        \"official_name\": \"IMPORT NOTEBOOK S.A.C.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"1.41000\", \n" +
            "      \"end_date\": \"2021-04-14T18:51:37.790500Z\", \n" +
            "      \"max_monthly_discount_rate\": \"1.54000\", \n" +
            "      \"tir\": \"20.12846\", \n" +
            "      \"start_date\": \"2021-04-09T21:59:07.690601Z\", \n" +
            "      \"gross_advance_amount\": 49577, \n" +
            "      \"percentage_completed\": 0.04774351009540714, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-07-25\", \n" +
            "        \"cost_time_priority\": 2, \n" +
            "        \"creation_dt\": \"2021-04-09T16:15:19.580961Z\", \n" +
            "        \"currency\": \"USD\", \n" +
            "        \"amount\": \"56488.36\", \n" +
            "        \"id\": \"34c39664-339e-464c-be1b-5f54296caeeb\"\n" +
            "      }, \n" +
            "      \"id\": 5286\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"A\", \n" +
            "        \"id\": \"661f5e4c-136a-4711-b95e-8c437f66c880\", \n" +
            "        \"official_name\": \"AGP PERU S.A.C.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"0.88000\", \n" +
            "      \"end_date\": \"2021-04-14T19:58:26.330436Z\", \n" +
            "      \"max_monthly_discount_rate\": \"0.93000\", \n" +
            "      \"tir\": \"11.74891\", \n" +
            "      \"start_date\": \"2021-04-14T15:59:33.669564Z\", \n" +
            "      \"gross_advance_amount\": 19829, \n" +
            "      \"percentage_completed\": 0.8506258510262746, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-06-25\", \n" +
            "        \"cost_time_priority\": 1, \n" +
            "        \"creation_dt\": \"2021-04-13T23:48:29.870896Z\", \n" +
            "        \"currency\": \"USD\", \n" +
            "        \"amount\": \"23089.24\", \n" +
            "        \"id\": \"f1963ef1-b93b-4dab-8c7d-707833ed5a6b\"\n" +
            "      }, \n" +
            "      \"id\": 5307\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"A\", \n" +
            "        \"id\": \"71363a51-e45d-42b2-9262-135eea61081b\", \n" +
            "        \"official_name\": \"AGRICOLA Y GANADERA CHAVIN DE HUANTAR S.A.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"0.88000\", \n" +
            "      \"end_date\": \"2021-04-14T22:59:00Z\", \n" +
            "      \"max_monthly_discount_rate\": \"0.93000\", \n" +
            "      \"tir\": \"11.61612\", \n" +
            "      \"start_date\": \"2021-04-14T21:00:03.459442Z\", \n" +
            "      \"gross_advance_amount\": 75494, \n" +
            "      \"percentage_completed\": 1.1389310408774207, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-07-21\", \n" +
            "        \"cost_time_priority\": 2, \n" +
            "        \"creation_dt\": \"2021-04-14T20:43:09.330710Z\", \n" +
            "        \"currency\": \"PEN\", \n" +
            "        \"amount\": \"82957.00\", \n" +
            "        \"id\": \"486531da-5532-4ffe-9647-5f1d3ee6bf87\"\n" +
            "      }, \n" +
            "      \"id\": 5314\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"A\", \n" +
            "        \"id\": \"661f5e4c-136a-4711-b95e-8c437f66c880\", \n" +
            "        \"official_name\": \"AGP PERU S.A.C.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"0.88000\", \n" +
            "      \"end_date\": \"2021-04-14T22:59:00Z\", \n" +
            "      \"max_monthly_discount_rate\": \"0.93000\", \n" +
            "      \"tir\": \"11.74891\", \n" +
            "      \"start_date\": \"2021-04-14T21:00:05.858201Z\", \n" +
            "      \"gross_advance_amount\": 2974, \n" +
            "      \"percentage_completed\": 0.8194250168123739, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-08-03\", \n" +
            "        \"cost_time_priority\": 1, \n" +
            "        \"creation_dt\": \"2021-04-14T18:59:39.948051Z\", \n" +
            "        \"currency\": \"USD\", \n" +
            "        \"amount\": \"3433.80\", \n" +
            "        \"id\": \"e928db84-cadb-429b-a878-2161b6dd9393\"\n" +
            "      }, \n" +
            "      \"id\": 5315\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"C+\", \n" +
            "        \"id\": \"7cb47c30-79b1-40ac-bf48-60b5e7210acf\", \n" +
            "        \"official_name\": \"LA RIAZA HOLDING S.A.C.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"1.19000\", \n" +
            "      \"end_date\": \"2021-04-14T22:59:00Z\", \n" +
            "      \"max_monthly_discount_rate\": \"1.27000\", \n" +
            "      \"tir\": \"15.80061\", \n" +
            "      \"start_date\": \"2021-04-14T21:00:08.443137Z\", \n" +
            "      \"gross_advance_amount\": 7153, \n" +
            "      \"percentage_completed\": 2.748540472529009, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-05-30\", \n" +
            "        \"cost_time_priority\": 2, \n" +
            "        \"creation_dt\": \"2021-04-14T16:41:21.625548Z\", \n" +
            "        \"currency\": \"PEN\", \n" +
            "        \"amount\": \"8307.00\", \n" +
            "        \"id\": \"34eff906-66e8-4280-bbfe-533c49a47ff2\"\n" +
            "      }, \n" +
            "      \"id\": 5316\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"C-\", \n" +
            "        \"id\": \"2757e5a4-82b9-4628-9941-0d169b395bbd\", \n" +
            "        \"official_name\": \"IMPORT NOTEBOOK S.A.C.\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"1.41000\", \n" +
            "      \"end_date\": \"2021-04-14T18:44:01.351638Z\", \n" +
            "      \"max_monthly_discount_rate\": \"1.54000\", \n" +
            "      \"tir\": \"20.12846\", \n" +
            "      \"start_date\": \"2021-04-09T21:58:12.607866Z\", \n" +
            "      \"gross_advance_amount\": 59073, \n" +
            "      \"percentage_completed\": 0.06250825250114266, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-07-31\", \n" +
            "        \"cost_time_priority\": 2, \n" +
            "        \"creation_dt\": \"2021-04-09T16:16:21.245297Z\", \n" +
            "        \"currency\": \"USD\", \n" +
            "        \"amount\": \"67199.80\", \n" +
            "        \"id\": \"43fa2fe3-1ae7-40a0-8fdb-68a3cb044d1b\"\n" +
            "      }, \n" +
            "      \"id\": 5284\n" +
            "    }, \n" +
            "    {\n" +
            "      \"status\": 1, \n" +
            "      \"debtor\": {\n" +
            "        \"rating\": \"C+\", \n" +
            "        \"id\": \"7131a793-df98-412a-963d-6e62d42af3fc\", \n" +
            "        \"official_name\": \"ANDINA PLAST S R L\"\n" +
            "      }, \n" +
            "      \"min_monthly_discount_rate\": \"1.19000\", \n" +
            "      \"end_date\": \"2021-04-14T18:03:15.331645Z\", \n" +
            "      \"max_monthly_discount_rate\": \"1.27000\", \n" +
            "      \"tir\": \"16.35089\", \n" +
            "      \"start_date\": \"2021-04-12T22:01:35.565970Z\", \n" +
            "      \"gross_advance_amount\": 125704, \n" +
            "      \"percentage_completed\": 0.021841150639597785, \n" +
            "      \"operation\": {\n" +
            "        \"payment_date\": \"2021-05-24\", \n" +
            "        \"cost_time_priority\": 1, \n" +
            "        \"creation_dt\": \"2021-04-12T20:51:55.964565Z\", \n" +
            "        \"currency\": \"USD\", \n" +
            "        \"amount\": \"146245.71\", \n" +
            "        \"id\": \"4eec4c35-af91-4e76-86bc-6e7df7404e11\"\n" +
            "      }, \n" +
            "      \"id\": 5298\n" +
            "    }\n" +
            "  ], \n" +
            "  \"next\": \"https://fact2-api-prod.herokuapp.com/v2/auctions?embed=operation%2Ctir%2Cpercentage_completed%2Cgross_advance_amount%2Cdebtor&operation__status=6&page=2&status=1\"\n" +
            "}\n";

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
        ResponseJSON response;
        String parameters;
        if((investment.getResults().getPercentage_completed()*100) < 100){
            parameters = generateJSONInvest(investment.getResults().getId(),investment.getAmount(),
                    investment.getResults().getMax_monthly_discount_rate());
            System.out.println("Invoice: "+investment.getResults().getId()+" Buyer: "+
                            investment.getResults().getDebtor().getOfficial_name()+" - "+getTime());
            response = executeInvestment(parameters,loginJSON.getToken());
            if(response.isStatus()){
                investment.setStatus("true");
            }else{
                investment.setStatus("false");
                investment.setMessage(response.getMessage());
            }
        }else {
            investment.setMessage("Invoice completed");
        }
        investment.setCompleted(true);
        return investment;
    }

    public static String generateJSONInvest(int id, double amount, double discount){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount);
        jsonObject.put("auction", id);
        jsonObject.put("min_discount_rate", round(discount,2));
        jsonObject.put("discount_rate", round(discount,2));
        System.out.println("JSON created for:"+amount+" "+id+"     - OK - "+getTime());
        return jsonObject.toJSONString();
    }

    public static Auctions getOpportunities(int i){
        if(i>2 && i <50){
            return gson.fromJson(op2, Auctions.class);
        }
        else return gson.fromJson(op, Auctions.class);
    }
}
