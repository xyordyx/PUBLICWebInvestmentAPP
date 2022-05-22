package com.gmp.investmentAPP;

import com.gmp.investmentAPP.JSON.Opportunities;

import java.util.List;

public class investmentAPPSeekerImpl implements Runnable{

    private String token;
    private volatile List<Opportunities> jsonList;

    public investmentAPPSeekerImpl(String token) {
        this.token = token;
    }

    @Override
    public void run() {
        this.jsonList = investmentAPPCIG.getOpportunitiesJSON(token);
    }

    public List<Opportunities> getJsonList() {
        return jsonList;
    }
}
