package com.github.howtobuildapp.libservice.execute;

public class RequestQueueItem {
    private Request request;
    private Callback callback;
    private Object sender;

    private long enqueueTimestamp;
    private long startTimestamp;
    private long endTimestamp;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    public long getEnqueueTimestamp() {
        return enqueueTimestamp;
    }

    public void setEnqueueTimestamp(long enqueueTimestamp) {
        this.enqueueTimestamp = enqueueTimestamp;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public long getWaitingTime(){
        return startTimestamp - enqueueTimestamp;
    }

    public long getRealTime(){
        return endTimestamp - startTimestamp;
    }

    public long getTotalTime(){
        return endTimestamp - enqueueTimestamp;
    }
}
