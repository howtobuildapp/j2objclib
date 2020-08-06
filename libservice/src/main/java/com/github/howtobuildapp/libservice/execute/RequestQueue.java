package com.github.howtobuildapp.libservice.execute;

import java.util.ArrayList;
import java.util.List;

public class RequestQueue implements ExecuteInterface {

    private ExecuteInterface realExecutor;
    private List<RequestQueueItem> items;
    private List<RequestQueueItem> executing;
    private int maxCount;
    private boolean isReady;

    public RequestQueue() {
        items = new ArrayList<>();
        executing = new ArrayList<>();
        maxCount = 5;
    }

    public void setRealExecutor(ExecuteInterface realExecutor) {
        this.realExecutor = realExecutor;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void suspend(){
        isReady = false;
    }

    public void resume(){
        isReady = true;
        mayExecute();
    }

    private void mayExecute(){
        if (realExecutor == null || !isReady || items.size() < 1 ||
        items.size() == executing.size() || executing.size() >= maxCount) {
            return;
        }
        long now = System.currentTimeMillis();
        for(int i=0;i<items.size();i++){
            final RequestQueueItem item = items.get(i);
            if (executing.contains(item) || executing.size() >= maxCount ||
                    now - item.getEnqueueTimestamp() > item.getRequest().getTimeout() * 1000) {
                continue;
            }
            item.setStartTimestamp(now);
            executing.add(item);
            realExecutor.executeRequest(item.getRequest(), item.getSender(), new Callback() {
                @Override
                public void onComplete(Request req, Response resp) {
                    finishItem(item, resp);
                }
            });
        }
    }

    private void finishItem(RequestQueueItem item, Response resp) {
        executing.remove(item);
        items.remove(item);
        if (item.getCallback() != null) {
            item.getCallback().onComplete(item.getRequest(), resp);
        }
        mayExecute();
    }

    @Override
    public void executeRequest(Request req, Object sender, Callback callback) {
        RequestQueueItem item = new RequestQueueItem();
        item.setCallback(callback);
        item.setEnqueueTimestamp(System.currentTimeMillis());
        item.setRequest(req);
        item.setSender(sender);
        items.add(item);
        mayExecute();
    }

    @Override
    public void cancel(Object sender) {
        ArrayList rms = new ArrayList();
        for (RequestQueueItem item : items) {
            if (sender == null || sender == item.getSender() ) {
                rms.add(item);
            }
        }
        executing.removeAll(rms);
        items.removeAll(rms);
        mayExecute();
    }
}
