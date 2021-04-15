package com.gmp.facturedo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.facturedo.JSON.*;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.ResponseJSON;
import com.gmp.persistence.model.FactUser;
import com.gmp.web.dto.FactUserDto;

import com.google.gson.*;

import okhttp3.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.gmp.hmviking.InvestmentUtil.getTime;
import static com.gmp.hmviking.InvestmentUtil.readResponse;


public class FacturedoCIG {



    private static String factBidURL = "https://fact2-api-prod.herokuapp.com/v1/factoring/bids";
    private static String factAuthURL = "https://fact2-api-prod.herokuapp.com/v1/auth";
    private static String factBalanceURL = "https://fact2-api-prod.herokuapp.com/v1/biz/entities/e1ed1fee-86f5-4bc4-911e-d1d6bb9cfe44/balance";
    private static String factOpportunities ="https://fact2-api-prod.herokuapp.com/v2/auctions?page=1&operation__status=6&status=1&embed=operation,tir,percentage_completed,gross_advance_amount,debtor";
    private static String factInvestments = "https://fact2-api-prod.herokuapp.com/v2/a209f254-0ab1-4288-b487-9f0868e88af5/investments?page=1&status=on_time&ordering=-modified_dt&embed=debtor,operation,repaid_amount,payment_date";
    private static String factBalanceTransactionURL = "https://fact2-api-prod.herokuapp.com/v1/biz/balance-transactions";
    private static String factCompletedInvestments = "https://fact2-api-prod.herokuapp.com/v2/a209f254-0ab1-4288-b487-9f0868e88af5/investments?page=1&status=paid&ordering=-modified_dt&embed=debtor,operation,repaid_amount,payment_date";
    private static String factBusinessRel = "https://fact2-api-prod.herokuapp.com/v1/biz/business-rels/";
    private static String factDashboardURL = "https://fact2-api-prod.herokuapp.com/v1/factoring/buyer/a209f254-0ab1-4288-b487-9f0868e88af5/dashboard";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static ResponseJSON executeInvestment(String urlParameters, String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String stringResponse = null;
        ResponseJSON responseJSON = null;
        HttpPost httpPost = new HttpPost(factBidURL);
        try {
            StringEntity entity = new StringEntity(urlParameters);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            //httpPost.setHeader("Content-Length", String.valueOf(entity.getContentLength()));
            httpPost.setHeader("Authorization", "JWT "+token);

            response = client.execute(httpPost);
            ObjectMapper objectMapper = new ObjectMapper();
            if(response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                String json = "{\"status\":true}";
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                responseJSON = objectMapper.readValue(json,ResponseJSON.class);
            }else{
                stringResponse = EntityUtils.toString(response.getEntity());
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                String json = "{\"status\":false,\"message\":\""+objectMapper
                        .readValue(stringResponse,ResponseJSON.class).getNon_field_errors().get(0)+"\"}";
                responseJSON = objectMapper.readValue(json,ResponseJSON.class);
                System.out.println("FINSMART ERROR RESPONSE: "+responseJSON.getMessage() + " - "+getTime());
            }
            client.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseJSON;
    }


    public static Auctions opp(LoginJSON loginJSON) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();
        String stringResponse = null;
        HttpGet httpGet = new HttpGet(factOpportunities);
        try {
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
            httpGet.addHeader("Accept", "application/json, text/plain, */*");
            //httpPost.setHeader("Content-Length", String.valueOf(entity.getContentLength()));
            httpGet.addHeader("Authorization", "JWT "+loginJSON.getToken());

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(250)
                    .setConnectTimeout(250)
                    .setSocketTimeout(250)
                    .build();
            httpGet.setConfig(requestConfig);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    httpGet.abort();
                }
            };
            new Timer(true).schedule(task, 600);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            stringResponse = EntityUtils.toString(response.getEntity());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(stringResponse,Auctions.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            System.out.println("Opportunities facturedo: 250 milliseconds elapsed on request - "+getTime());
        }catch (SSLException e) {
            System.out.println("Socket closed - "+getTime());
        }catch (InterruptedIOException e) {
            System.out.println("Connection has been shut down - "+getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Auctions getOpportunitiesJSON(LoginJSON loginJSON) {
        URL url;
        try {
            url = new URL(factOpportunities);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(250);
            con.setReadTimeout(250);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "JWT "+loginJSON.getToken());
            Auctions auctions= gson.fromJson(readResponse(con).toString(), Auctions.class);
            return auctions;
        } catch (MalformedURLException | ProtocolException e ) {
            e.printStackTrace();
        }  catch (SocketTimeoutException e) {
            System.out.println("Opportunities facturedo: 250 milliseconds elapsed on request - "+getTime());
        }catch (Throwable e) {
            System.out.println(e);
        }
        return null;
    }

    public static Auctions getOpportunitiesJSONTIME(LoginJSON loginJSON) {
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

    public static Auctions getCompletedInvestJSON(LoginJSON loginJSON) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(factCompletedInvestments)
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

    public static BusinessRel getBusinessRel(LoginJSON loginJSON, String businessRel) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(factBusinessRel+businessRel)
                .method("GET", null)
                .addHeader("Authorization", "JWT "+loginJSON.getToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            response.close();;
            return gson.fromJson(resp, BusinessRel.class);
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

    public static Dashboard getDashboard(LoginJSON loginJSON){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(factDashboardURL)
                .method("GET", null)
                .addHeader("Authorization", "JWT "+loginJSON.getToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            response.close();;
            return gson.fromJson(resp, Dashboard.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
