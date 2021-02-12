package com.gmp.hmviking;

import com.gmp.facturedo.JSON.Results;
import com.gmp.finsmart.JSON.Opportunities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class QueueStructure {
    private Queue<List<Opportunities>> queue;
    private Queue<List<Results>> queueResults;
    private int actualSize;
    private boolean isCancelled;

    public QueueStructure(int actualSize) {
        this.queue = new LinkedList<>();
        this.queueResults = new LinkedList<>();
        this.actualSize = actualSize;
        this.isCancelled = false;
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
