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
        "amounts",
        "investments_count",
        "estimated_profit",
})
public class Portfolio {

    @JsonProperty("amounts")
    private Amounts amounts;
    @JsonProperty("estimated_profit")
    private EstimatedProfit estimated_profit;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("amounts")
    public Amounts getAmounts() {
        return amounts;
    }

    @JsonProperty("amounts")
    public void setAmounts(Amounts amounts) {
        this.amounts = amounts;
    }

    @JsonProperty("estimated_profit")
    public EstimatedProfit getEstimatedProfit() {
        return estimated_profit;
    }

    @JsonProperty("estimated_profit")
    public void setEstimatedProfit(EstimatedProfit estimated_profit) {
        this.estimated_profit = estimated_profit;
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