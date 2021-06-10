package com.gmp.facturedo;

import com.gmp.facturedo.JSON.Auctions;
import com.gmp.hmviking.LoginJSON;

public class FacturedoCIGSeeker implements Runnable{

    private LoginJSON token;
    private volatile Auctions jsonList;

    public FacturedoCIGSeeker(LoginJSON token) {
        this.token = token;
    }

    @Override
    public void run() {
        this.jsonList = FacturedoCIG.opp(token);
    }

    public Auctions getJsonList() {
        return jsonList;
    }
}
