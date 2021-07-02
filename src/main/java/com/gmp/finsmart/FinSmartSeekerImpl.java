package com.gmp.finsmart;

import com.gmp.finsmart.JSON.Opportunities;

import java.util.List;

public class FinSmartSeekerImpl implements Runnable{

    private String token;
    private volatile List<Opportunities> jsonList;

    public FinSmartSeekerImpl(String token) {
        this.token = token;
    }

    @Override
    public void run() {
        this.jsonList = FinSmartCIG.getOpportunitiesJSON(token);
    }

    public List<Opportunities> getJsonList() {
        return jsonList;
    }
}
