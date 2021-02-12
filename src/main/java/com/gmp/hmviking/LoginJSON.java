package com.gmp.hmviking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Transient;

public class LoginJSON {
    @Transient
    private String accessToken;
    @Transient
    private String token;
    @Transient
    private String id;

    @JsonCreator
    public LoginJSON(@JsonProperty("accessToken") String accessToken, @JsonProperty("token")String token,
                     @JsonProperty("id")String id) {
        this.accessToken = accessToken;
        this.token = token;
        this.id = id;
    }

    @JsonGetter("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonGetter("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
