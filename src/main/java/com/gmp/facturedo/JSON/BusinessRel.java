package com.gmp.facturedo.JSON;

import javax.persistence.Transient;

public class BusinessRel {
    @Transient
    private String seller_entity;
    @Transient
    private String id;
    @Transient
    private String debtor_entity;
    @Transient
    private String debtor_ruc;

    public String getSeller_entity() {
        return seller_entity;
    }

    public void setSeller_entity(String seller_entity) {
        this.seller_entity = seller_entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebtor_entity() {
        return debtor_entity;
    }

    public void setDebtor_entity(String debtor_entity) {
        this.debtor_entity = debtor_entity;
    }

    public String getDebtor_ruc() {
        return debtor_ruc;
    }

    public void setDebtor_ruc(String debtor_ruc) {
        this.debtor_ruc = debtor_ruc;
    }
}
