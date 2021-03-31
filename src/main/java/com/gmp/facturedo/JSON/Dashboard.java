package com.gmp.facturedo.JSON;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "in_auction_operations",
        "active_bids",
        "repaid_bids",
        "portfolio",
        "deposits_amount",
        "withdrawals_amount"
})
public class Dashboard {

    @JsonProperty("repaid_bids")
    private RepaidBids repaid_bids;
    @JsonProperty("portfolio")
    private Portfolio portfolio;
    @JsonProperty("deposits_amount")
    private DepositsAmount deposits_amount;
    @JsonProperty("withdrawals_amount")
    private WithdrawalsAmount withdrawals_amount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("repaid_bids")
    public RepaidBids getRepaidBids() {
        return repaid_bids;
    }

    @JsonProperty("repaid_bids")
    public void setRepaidBids(RepaidBids repaidBids) {
        this.repaid_bids = repaidBids;
    }

    @JsonProperty("portfolio")
    public Portfolio getPortfolio() {
        return portfolio;
    }

    @JsonProperty("portfolio")
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    @JsonProperty("deposits_amount")
    public DepositsAmount getDepositsAmount() {
        return deposits_amount;
    }

    @JsonProperty("deposits_amount")
    public void setDepositsAmount(DepositsAmount depositsAmount) {
        this.deposits_amount = depositsAmount;
    }

    @JsonProperty("withdrawals_amount")
    public WithdrawalsAmount getWithdrawalsAmount() {
        return withdrawals_amount;
    }

    @JsonProperty("withdrawals_amount")
    public void setWithdrawalsAmount(WithdrawalsAmount withdrawalsAmount) {
        this.withdrawals_amount = withdrawalsAmount;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}