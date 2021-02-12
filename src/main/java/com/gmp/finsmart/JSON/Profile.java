package com.gmp.finsmart.JSON;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.Transient;

public class Profile {
    @Transient
    private int v1Id;
    @Transient
    private String _id;

    @JsonGetter("v1Id")
    public int getV1Id() {
        return v1Id;
    }

    public void setV1Id(int v1Id) {
        this.v1Id = v1Id;
    }

    @JsonGetter("_id")
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
