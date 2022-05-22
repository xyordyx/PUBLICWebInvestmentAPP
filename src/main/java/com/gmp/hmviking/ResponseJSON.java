package com.gmp.hmviking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Transient;
import java.util.List;

public class ResponseJSON {
    @Transient
    private boolean status;
    @Transient
    private String message;
    @Transient
    private List<String> non_field_errors = null;

    @JsonCreator
    public ResponseJSON(@JsonProperty("status") boolean status,@JsonProperty("message") String message) {
        this.status = status;
        this.message = message;
    }

    @JsonGetter("status")
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @JsonGetter("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getNon_field_errors() {
        return non_field_errors;
    }

    @JsonProperty("non_field_errors")
    public void setNon_field_errors(List<String> non_field_errors) {
        this.non_field_errors = non_field_errors;
    }
}
