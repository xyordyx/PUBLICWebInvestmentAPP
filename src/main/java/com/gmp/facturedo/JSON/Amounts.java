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
        "PEN",
        "USD"
})
public class Amounts {

    @JsonProperty("PEN")
    private Float PEN;
    @JsonProperty("USD")
    private Float USD;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("PEN")
    public Float getPEN() {
        return PEN;
    }

    @JsonProperty("PEN")
    public void setPEN(Float pEN) {
        this.PEN = pEN;
    }

    @JsonProperty("USD")
    public Float getUSD() {
        return USD;
    }

    @JsonProperty("USD")
    public void setUSD(Float USD) {
        this.USD = USD;
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