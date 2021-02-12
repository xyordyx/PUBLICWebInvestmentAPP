package com.gmp.finsmart.JSON;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.Transient;
import java.util.List;

public class FinancialTransactions {
    @Transient
    private List<Transactions> financialTransactions;

    @JsonGetter("financialTransactions")
    public List<Transactions> getFinancialTransactions() {
        return financialTransactions;
    }

    public void setFinancialTransactions(List<Transactions> financialTransactions) {
        this.financialTransactions = financialTransactions;
    }
}
