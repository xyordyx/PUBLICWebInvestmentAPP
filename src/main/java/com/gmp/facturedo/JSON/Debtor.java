package com.gmp.facturedo.JSON;

import javax.persistence.Transient;

public class Debtor {
    @Transient
    private String rating;
    @Transient
    private String id;
    @Transient
    private String official_name;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficial_name() {
        return official_name;
    }

    public void setOfficial_name(String official_name) {
        this.official_name = official_name;
    }
}
