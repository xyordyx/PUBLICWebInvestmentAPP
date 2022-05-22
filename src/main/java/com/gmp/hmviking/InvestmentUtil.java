package com.gmp.hmviking;

import com.gmp.investmentAPP.investmentAPPUtil;
import com.gmp.investmentAPP.JSON.PhysicalInvoices;
import com.gmp.web.dto.Investment;
import com.gmp.web.dto.InvestmentForm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Future;

public class InvestmentUtil {
    public static String getTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss SSS ");
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone).plus(329, ChronoUnit.MILLIS);
        return dtf.format(now);
    }

    public static StringBuilder readResponse(HttpURLConnection con) throws IOException {
        BufferedReader br;
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response;
    }

    public static long timesDiff(String time2){
        if(time2 == null){
            return 0;
        }
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format1.parse(getNowTime());
            date2 = format2.parse(time2);
            if(date2.before(date1)){
                Calendar c = Calendar.getInstance();
                c.setTime(date2);
                c.add(Calendar.DATE, 1);
                date2 = c.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2.getTime() - date1.getTime();
    }

    public static long minutesElapsed(Instant start, Instant actual){
        return Duration.between(start, actual).toMinutes();
        //return Duration.between(start, actual).toMillis();
    }

    public static String getNowTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        return dtf.format(now);
    }

    public static List<Investment> getInvestCollection(InvestmentForm investmentForm){
        List<Integer> amountList = investmentAPPUtil.parseInt(investmentForm.getAmount());
        List<String> currencyList = investmentAPPUtil.parseString(investmentForm.getCurrency());
        List<String> invoiceList = investmentAPPUtil.parseString(investmentForm.getInvoiceNumber());
        if(currencyList.size()-1 == amountList.size() && amountList.size() == invoiceList.size()) {
            List<Investment> listInv = new ArrayList<>();
            for (int i = 0; i < invoiceList.size(); i++) {
                Investment inv = new Investment();
                PhysicalInvoices code = new PhysicalInvoices();
                code.setCode(invoiceList.get(i));
                inv.setFormCode(code);
                inv.setInvoiceNumber(invoiceList.get(i));
                inv.setAmount(amountList.get(i));
                inv.setCurrency(currencyList.get(i));
                listInv.add(inv);
            }
            return listInv;
        }
        return null;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<Investment>  setInProgress(List<Investment> investmentList){
        for(int i=0;i<investmentList.size();i++){
            investmentList.get(i).setStatus("inProgress");
        }
        return investmentList;
    }

    public static List<Investment> updateInvestment(List<Investment> investmentList){
        for(int i=0; i<investmentList.size(); i++){
                if(investmentList.get(i).getUIState() == null){
                    investmentList.get(i).setUIState("true");
                }else investmentList.get(i).setUIState("false");
        }
        return investmentList;
    }
}
