package com.gmp.finsmart;

import com.gmp.finsmart.JSON.Opportunities;

import java.util.List;

import static com.gmp.finsmart.FinSmartCIG.getOpportunitiesJSON;

public class FinSmartCIGSeeker implements Runnable{

    private String token;
    private volatile List<Opportunities> jsonList;

    public FinSmartCIGSeeker(String token) {
        this.token = token;
    }

    @Override
    public void run() {
        this.jsonList = getOpportunitiesJSON(token);
    }

    public List<Opportunities> getJsonList() {
        return jsonList;
    }
}
