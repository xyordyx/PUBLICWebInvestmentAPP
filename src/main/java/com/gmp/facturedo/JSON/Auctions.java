package com.gmp.facturedo.JSON;

import javax.persistence.Transient;
import java.util.List;

public class Auctions {
    @Transient
    private int count;
    @Transient
    private List<Results> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResultsList(List<Results> results) {
        this.results = results;
    }
}
