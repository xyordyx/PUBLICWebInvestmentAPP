package com.gmp.facturedo.JSON;

import javax.persistence.Transient;
import java.util.List;

public class AuctionsDeposits {
    @Transient
    private int count;
    @Transient
    private List<ResultsDeposits> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ResultsDeposits> getResults() {
        return results;
    }

    public void setResultsList(List<ResultsDeposits> results) {
        this.results = results;
    }
}
