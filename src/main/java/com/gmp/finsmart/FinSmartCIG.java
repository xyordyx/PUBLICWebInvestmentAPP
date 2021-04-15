package com.gmp.finsmart;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.finsmart.JSON.*;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.ResponseJSON;
import com.gmp.persistence.model.SmartUser;
import com.gmp.web.dto.SmartUserDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import okhttp3.*;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gmp.hmviking.InvestmentUtil.getTime;
import static com.gmp.hmviking.InvestmentUtil.readResponse;


public class FinSmartCIG{
    private static String authenticationPath="/authentications";
    private static String opportunitiesPath="/opportunities";
    private static String financialTransactionsPath="/financial-transactions";
    private static String profilesPath="/profiles";
    private static String invoices="/invoices";
    private static String smartURLv1 = "https://api.finsmart.pe/api/v1";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static ResponseJSON executeInvestment(String urlParameters, String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String stringResponse = null;
        ResponseJSON responseJSON = null;
        HttpPost httpPost = new HttpPost(smartURLv1+financialTransactionsPath);
        try {
            StringEntity entity = new StringEntity(urlParameters);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", "Bearer "+token);

            response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                String json = "{\"status\":true}";
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                responseJSON = objectMapper.readValue(json,ResponseJSON.class);
            }else{
                stringResponse = EntityUtils.toString(response.getEntity());
                String json = "{\"status\":false,\"message\":\""+stringResponse+"\"}";
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

    public static FinancialTransactions getFinancialTransactions(String token){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String stringResponse;
        FinancialTransactions financialTransactions = null;
        HttpGet getRequest = new HttpGet(smartURLv1+financialTransactionsPath);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+token);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            stringResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            financialTransactions = objectMapper.readValue(stringResponse, FinancialTransactions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return financialTransactions;
    }

    public static List<InvoiceTransactions> getInvoices(String token){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String stringResponse;
        List<InvoiceTransactions> invoiceTransactions = new ArrayList<>();
        HttpGet getRequest = new HttpGet(smartURLv1+invoices);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+token);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            stringResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            InvoiceTransactions[] op = objectMapper.readValue(stringResponse, InvoiceTransactions[].class);
            invoiceTransactions = new ArrayList<>(Arrays.asList(op));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoiceTransactions;
    }

    public static List<Opportunities> getOpportunitiesJSON1(String token,int timeRequest) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String stringResponse;
        List<Opportunities> opportunities = new ArrayList<>();
        HttpGet getRequest = new HttpGet(smartURLv1+opportunitiesPath);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+token);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            if(response.getEntity().getContentLength() > 2){
                stringResponse = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Opportunities[] op = objectMapper.readValue(stringResponse, Opportunities[].class);
                opportunities = new ArrayList<>(Arrays.asList(op));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return opportunities;
    }

    public static List<Opportunities> getOpportunities2(String token, int timeRequest){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(timeRequest, TimeUnit.MILLISECONDS)
                .readTimeout(timeRequest, TimeUnit.MILLISECONDS)
                //.writeTimeout(timeRequest, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url(smartURLv1+opportunitiesPath)
                .method("GET", null)
                .addHeader("Authorization", "Bearer "+token)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Type founderListType = new TypeToken<ArrayList<Opportunities>>(){}.getType();
            return gson.fromJson(response.body().string(), founderListType);
        } catch (IllegalStateException | JsonSyntaxException | IOException exception) {
            System.out.println("JsonSyntaxException: "+exception);
        }
        return null;
    }

    public static List<Opportunities> getOpportunitiesJSON(String token) {
        URL url;
        try {
            url = new URL(smartURLv1+opportunitiesPath);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(250);
            con.setReadTimeout(250);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+token);
            Type founderListType = new TypeToken<ArrayList<Opportunities>>(){}.getType();
            return gson.fromJson(readResponse(con).toString(), founderListType);
        } catch (MalformedURLException | ProtocolException e ) {
            e.printStackTrace();
        }  catch (SocketTimeoutException e) {
            System.out.println("Opportunities finsmart: 250 milliseconds elapsed on request - "+getTime());
        }catch (Throwable e) {
            System.out.println(e);
        }
        return null;
    }

    public static LoginJSON getAuthentications(SmartUserDto user) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse = null;
        HttpPost httpPost = new HttpPost(smartURLv1+authenticationPath);
        final String json = "{\"email\":\""+user.getEmail()+"\",\"actualPassword\":\""+user.getPassword()+"\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);

        if(response.getStatusLine().getStatusCode() == 201) {
            stringResponse = EntityUtils.toString(response.getEntity());
        }
        client.close();

        return new ObjectMapper()
                .readerFor(LoginJSON.class)
                .readValue(stringResponse);
    }

    public static LoginJSON getAuthentications(SmartUser smartUser) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse = null;
        HttpPost httpPost = new HttpPost(smartURLv1+authenticationPath);
        final String json = "{\"email\":\""+smartUser.getEmail()+"\",\"actualPassword\":\""+smartUser.getPassword()+"\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);

        if(response.getStatusLine().getStatusCode() == 201) {
            stringResponse = EntityUtils.toString(response.getEntity());
        }
        client.close();

        return new ObjectMapper()
                .readerFor(LoginJSON.class)
                .readValue(stringResponse);
    }

}
