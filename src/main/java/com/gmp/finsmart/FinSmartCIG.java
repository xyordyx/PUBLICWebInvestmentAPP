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
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.SSLException;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
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

    public static ResponseJSON executeInvestment1(String urlParameters, String token) {
        URL url;
        ResponseJSON responseJSON = null;
        String json;
        try {
            url = new URL(smartURLv1+financialTransactionsPath);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+" Investment Initialization");
            con.setRequestMethod("POST");

            con.setRequestProperty("Accept", "application/json, text/plain, */*");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+token);
            //con.setConnectTimeout(500);

            con.setDoOutput(true);

            try(OutputStream os = con.getOutputStream()){
                byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int code = con.getResponseCode();
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+"CODE RESPONSE: "+code);
            BufferedReader br;
            if (100 <= code && code <= 399) {
                json = "{\"status\":true}";
            }
            else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String responseLine;
                StringBuilder response = new StringBuilder();
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                json = "{\"status\":false,\"message\":\""+response+"\"}";
                System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+"ERROR RESPONSE: "
                        +response+" Payload:"+urlParameters);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            responseJSON = objectMapper.readValue(json,ResponseJSON.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }catch (java.net.SocketTimeoutException e) {
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseJSON;
    }

    public static ResponseJSON executeInvestment2(String urlParameters, String token) {
        ResponseJSON responseJSON = null;
        Response response = null;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                //.callTimeout(3000,TimeUnit.MILLISECONDS)
                .build();
        System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+" Investment Initialization");
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), urlParameters);
        Request request = new Request.Builder()
                .url(smartURLv1+financialTransactionsPath)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", "Bearer "+token)
                .post(body)
                .build();
        Call call = client.newCall(request);
        try {
            response = call.execute();
            if(response.code() == HttpURLConnection.HTTP_CREATED) {
                String json = "{\"status\":true,\"message\":\"\"}";
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                responseJSON = objectMapper.readValue(json,ResponseJSON.class);
            }else{
                String json = "{\"status\":false,\"message\":\""+response.body().string()+"\"}";
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                responseJSON = objectMapper.readValue(json,ResponseJSON.class);
                System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+"ERROR RESPONSE: "
                        +responseJSON.getMessage()+" Payload:"+urlParameters);
                response.body().close();
            }
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+e.getMessage());
        }
        return responseJSON;
    }

    public static ResponseJSON executeInvestment(String urlParameters, String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response;
        String stringResponse;
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
                System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+"ERROR RESPONSE: "
                        +responseJSON.getMessage()+" Payload:"+urlParameters);
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

    public static List<Opportunities> getOpportunitiesJSON4(String token) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String stringResponse;
        List<Opportunities> opportunities = new ArrayList<>();
        HttpGet httpGet = new HttpGet(smartURLv1+opportunitiesPath);
        try {
            httpGet.addHeader("Accept", "application/json");
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Authorization", "Bearer "+token);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(500)
                    .setConnectTimeout(500)
                    .setSocketTimeout(500)
                    .build();
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            stringResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Opportunities[] op = objectMapper.readValue(stringResponse, Opportunities[].class);
            opportunities = new ArrayList<>(Arrays.asList(op));
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
        return opportunities;
    }

    public static List<Opportunities> getOpportunitiesJSON3(String token) {
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

    public static List<Opportunities> getOpportunitiesJSON2(String token){
        System.setProperty("http.keepAlive", "false");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(500, TimeUnit.MILLISECONDS)
                //.writeTimeout(250, TimeUnit.MILLISECONDS)
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
            System.setProperty("http.keepAlive", "false");
            url = new URL(smartURLv1+opportunitiesPath);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(250);
            //con.setReadTimeout(250);
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
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static LoginJSON getAuthentications(SmartUserDto user) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse;
        HttpPost httpPost = new HttpPost(smartURLv1+authenticationPath);
        final String json = "{\"email\":\""+user.getEmail()+"\",\"actualPassword\":\""+user.getPassword()+"\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);

        if(response.getStatusLine().getStatusCode() == 201) {
            stringResponse = EntityUtils.toString(response.getEntity());
        }else stringResponse = "false";
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
