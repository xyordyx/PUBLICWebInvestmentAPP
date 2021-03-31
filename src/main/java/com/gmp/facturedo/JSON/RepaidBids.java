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
        "count",
        "paid",
        "profit"
})
public class RepaidBids {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("paid")
    private Paid paid;
    @JsonProperty("profit")
    private Profit profit;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("paid")
    public Paid getPaid() {
        return paid;
    }

    @JsonProperty("paid")
    public void setPaid(Paid paid) {
        this.paid = paid;
    }

    @JsonProperty("profit")
    public Profit getProfit() {
        return profit;
    }

    @JsonProperty("profit")
    public void setProfit(Profit profit) {
        this.profit = profit;
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