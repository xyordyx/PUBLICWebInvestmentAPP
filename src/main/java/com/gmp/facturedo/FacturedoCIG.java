package com.gmp.facturedo;

import com.gmp.facturedo.JSON.Auctions;
import com.gmp.facturedo.JSON.AuctionsDeposits;
import com.gmp.facturedo.JSON.Balance;
import com.gmp.finsmart.JSON.Opportunities;
import com.gmp.hmviking.LoginJSON;
import com.gmp.persistence.model.FactUser;
import com.gmp.web.dto.FactUserDto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gmp.hmviking.InvestmentUtil.readResponse;


public class FacturedoCIG {



    private static String factBidURL = "https://fact2-api-prod.herokuapp.com/v1/factoring/bids";
    private static String factAuthURL = "https://fact2-api-prod.herokuapp.com/v1/auth";
    private static String factBalanceURL = "https://fact2-api-prod.herokuapp.com/v1/biz/entities/e1ed1fee-86f5-4bc4-911e-d1d6bb9cfe44/balance";
    private static String factOpportunities ="https://fact2-api-prod.herokuapp.com/v2/auctions?page=1&operation__status=6&status=1&embed=operation,tir,percentage_completed,gross_advance_amount,debtor";
    private static String factInvestments = "https://fact2-api-prod.herokuapp.com/v2/a209f254-0ab1-4288-b487-9f0868e88af5/investments?page=1&status=on_time&ordering=-modified_dt&embed=debtor,operation,repaid_amount,payment_date";
    private static String factBalanceTransactionURL = "https://fact2-api-prod.herokuapp.com/v1/biz/balance-transactions";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static JsonObject executeInvestment(JSONObject urlParameters, String token) throws IOException {
        HttpURLConnection connection;
        //Create connection
        URL url = new URL(factBidURL);
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Content-length", "" + urlParameters.size());
        connection.setRequestProperty("Authorization", "JWT "+token);

        connection.setUseCaches (false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = urlParameters.toJSONString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        //Send request
        DataOutputStream wr = new DataOutputStream (
                connection.getOutputStream ());
        wr.writeBytes (urlParameters.toJSONString());
        wr.flush ();
        wr.close ();

        JsonObject responseJSON;
        int responseCode = connection.getResponseCode();
        StringBuilder resp = readResponse(connection);
        if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
            responseJSON = gson.fromJson("{\"status\":true,\"message\":\"\"}",
                    JsonElement.class).getAsJsonObject();
        } else if(responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){
            responseJSON = gson.fromJson("{status:false,message:WRONG INVOICE ID/INTERNAL ERROR}",
                    JsonElement.class).getAsJsonObject();
        }
        else {
            responseJSON = gson.fromJson("{status:false,message:"+resp+"}",
                    JsonElement.class).getAsJsonObject();
        }
        System.out.print("Response from Facturedo: "+responseJSON.toString());
        return responseJSON;
    }

    public static Auctions getOpportunitiesJSON(LoginJSON loginJSON, int timeRequest) {
        URL url;
        try {
            url = new URL(factOpportunities);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(timeRequest);
            con.setReadTimeout(timeRequest);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "JWT "+loginJSON.getToken());
            return gson.fromJson(readResponse(con).toString(), Auctions.class);
        } catch (MalformedURLException | ProtocolException e ) {
            e.printStackTrace();
        }  catch (SocketTimeoutException e) {
            System.out.println("More than " + timeRequest + "milliseconds elapsed on request");
        }catch (Throwable e) {
            System.out.println(e);
        }
        return null;
    }

    public static Auctions getOpportunitiesJSON(LoginJSON loginJSON) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(factOpportunities)
                .method("GET", null)
                .addHeader("Authorization", "JWT "+loginJSON.getToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            response.close();;
            return gson.fromJson(resp, Auctions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Balance getBalance(LoginJSON loginJSON){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(factBalanceURL)
                .method("GET", null)
                .addHeader("Authorization", "JWT "+loginJSON.getToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            response.close();;
            return gson.fromJson(resp, Balance.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LoginJSON getAuthentications(FactUserDto user) throws IOException {
        final String json = "{\"email\":\""+user.getEmail()+"\",\"password\":\""+user.getPassword()+"\"}";
        String resp = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(factAuthURL)
                .method("POST", body)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code() == 200) {
            resp = response.body().string();
        }
        response.close();
        return gson.fromJson(resp, LoginJSON.class);
    }

    public static LoginJSON getAuthentications(FactUser factUser) throws IOException {
        final String json = "{\"email\":\""+factUser.getEmail()+"\",\"password\":\""+factUser.getPassword()+"\"}";
        String resp = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(factAuthURL)
                .method("POST", body)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code() == 200) {
            resp = response.body().string();
        }
        response.close();
        return gson.fromJson(resp, LoginJSON.class);
    }

    public static Auctions getInvestmentsJSON(LoginJSON loginJSON) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(factInvestments)
                .method("GET", null)
                .addHeader("Authorization", "JWT "+loginJSON.getToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            response.close();;
            return gson.fromJson(resp, Auctions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AuctionsDeposits getBalanceTransactions(LoginJSON loginJSON){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(factBalanceTransactionURL)
                .method("GET", null)
                .addHeader("Authorization", "JWT "+loginJSON.getToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            response.close();;
            return gson.fromJson(resp, AuctionsDeposits.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
