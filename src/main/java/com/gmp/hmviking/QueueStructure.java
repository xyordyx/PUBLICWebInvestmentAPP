package com.gmp.hmviking;

import com.gmp.facturedo.JSON.Results;
import com.gmp.finsmart.JSON.Opportunities;

import java.util.*;

public class QueueStructure {
    private Queue<List<Opportunities>> queue;
    private Queue<List<Results>> queueResults;
    private int actualSize;
    private boolean isCancelled;
    private HashMap<String,Double> balance;
    private String updateDate;

    public QueueStructure(int actualSize, double availableSoles, double availableDollar) {
        this.queue = new LinkedList<>();
        this.queueResults = new LinkedList<>();
        this.actualSize = actualSize;
        this.isCancelled = false;
        this.balance = new HashMap<>();
        this.balance.put("pen",availableSoles);
        this.balance.put("usd",availableDollar);
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public HashMap<String, Double> getBalance() {
        return balance;
    }

    public void setBalance(HashMap<String, Double> balance) {
        this.balance = balance;
    }

    public Queue<List<Results>> getQueueResults() {
        return queueResults;
    }

    public void setQueueResults(Queue<List<Results>> queueResults) {
        this.queueResults = queueResults;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public Queue<List<Opportunities>> getQueue() {
        return queue;
    }

    public void setQueue(Queue<List<Opportunities>> queue) {
        this.queue = queue;
    }

    public int getActualSize() {
        return actualSize;
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }
}
