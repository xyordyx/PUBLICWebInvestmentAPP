package com.gmp.finsmart;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.hmviking.ResponseJSON;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static com.gmp.hmviking.InvestmentUtil.getTime;


public class FinSmartCIGPost {
    private static String authenticationPath="/authentications";
    private static String opportunitiesPath="/opportunities";
    private static String financialTransactionsPath="/financial-transactions";
    private static String profilesPath="/profiles";
    private static String invoices="/invoices";
    private static String smartURLv1 = "https://api.finsmart.pe/api/v1";


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofMillis(500))
            .build();

    public static ResponseJSON executeInvestment2(String urlParameters, String token) {
        ResponseJSON responseJSON = null;
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(urlParameters))
                .uri(URI.create(smartURLv1+financialTransactionsPath))
                .setHeader("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+token)
                .build();

        HttpResponse response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == HttpURLConnection.HTTP_CREATED){
                String json = "{\"status\":true}";
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                responseJSON = objectMapper.readValue(json, ResponseJSON.class);
            }else{
                String json = "{\"status\":false,\"message\":\""+response.body()+"\"}";
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                responseJSON = objectMapper.readValue(json,ResponseJSON.class);
                System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+"ERROR RESPONSE: "
                        +responseJSON.getMessage()+" Payload:"+urlParameters);
            }
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()+getTime()+e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Interrupted"+Thread.currentThread().getName()+getTime()+e.getMessage());
        }
        return responseJSON;
    }

}
