package com.gmp.finsmart.JSON;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.Transient;
import java.util.List;

public class Invoice {
    @Transient
    private Debtor debtor;
    @Transient
    private List<PhysicalInvoices> physicalInvoices;
    @Transient
    private String _id;

    @Override
    public String toString() {
        return "Invoice{" +
                ", physicalInvoices=" + physicalInvoices +
                ", _id='" + _id + '\'' +
                '}';
    }

    @JsonGetter("debtor")
    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    @JsonGetter("physicalInvoices")
    public List<PhysicalInvoices> getPhysicalInvoices() {
        return physicalInvoices;
    }

    public void setPhysicalInvoices(List<PhysicalInvoices> physicalInvoices) {
        this.physicalInvoices = physicalInvoices;
    }

    @JsonGetter("_id")
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
