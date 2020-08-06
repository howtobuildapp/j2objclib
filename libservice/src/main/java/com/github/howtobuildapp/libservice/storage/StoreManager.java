package com.github.howtobuildapp.libservice.storage;

import com.github.howtobuildapp.libservice.execute.EM;
import com.github.howtobuildapp.libservice.execute.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreManager {

    private HashMap<String, StoreItem> data;
    protected String name;

    private static StoreManager defaultInstance;

    private StoreManager(){
        data = new HashMap<>();
    }

    public static StoreManager defaultManager(){
        if (defaultInstance == null) {
            defaultInstance = new StoreManager();
            defaultInstance.name = "default";
        }
        return defaultInstance;
    }

    public void storeStr(String key, String str){
        storeStrWithExp(key, str, 0);
    }

    public void storeStrWithExp(String key, String str, int seconds){
        if (key == null || str == null) {
            return;
        }
        long now = System.currentTimeMillis();
        StoreItem item = new StoreItem();
        item.setKey(key);
        item.setTimestamp(now);
        item.setValue(str);
        item.setExpire(seconds);
        data.put(key, item);

        StoreKVRequest request = new StoreKVRequest();
        request.setItem(item);
        request.setFrom(this.name);
        request.setOperation(StoreKVRequest.operationStore);

        EM.mainSyncManager.executeRequestSync(request);
    }

    public String getStr(String key) {
        if (key == null) {
            return null;
        }
        StoreItem item = data.get(key);
        if (item == null) {
            item = new StoreItem();
            item.setKey(key);
            StoreKVRequest request = new StoreKVRequest();
            request.setItem(item);
            request.setFrom(this.name);
            request.setOperation(StoreKVRequest.operationGet);
            Response response = EM.mainSyncManager.executeRequestSync(request);
            if (response == null || !(response instanceof StoreKVResponse)) {
                return null;
            }
            StoreKVResponse kvResponse = (StoreKVResponse) response;
            if (!kvResponse.isOK()) {
                return null;
            }
            item = kvResponse.getResultItem();
            if (item == null) {
                return null;
            }
            data.put(key, item);
        }
        long now = System.currentTimeMillis();
        if (item.getExpire() > 0 && now - item.getTimestamp() > item.getExpire() * 1000) {
            remove(key);
            return null;
        }
        return item.getValue();
    }

    public void remove(String key) {
        if (key == null) {
            return;
        }
        data.remove(key);
        StoreItem item = new StoreItem();
        item.setKey(key);

        StoreKVRequest request = new StoreKVRequest();
        request.setItem(item);
        request.setFrom(this.name);
        request.setOperation(StoreKVRequest.operationRemove);
        EM.mainSyncManager.executeRequestSync(request);
    }

    public void storeObj(String key, Object obj){
        storeObjWithExp(key, obj, 0);
    }

    public void storeObjWithExp(String key, Object obj, int seconds){
        if (key == null || obj == null) {
            return;
        }
        Gson gson = new Gson();
        storeStrWithExp(key, gson.toJson(obj), seconds);
    }

    public Object getObj(String key, Class cls) {
        String jstr = getStr(key);
        if (jstr == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(jstr, cls);
    }

    public void removeAllExpired(){
        List<String> list = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (Map.Entry<String, StoreItem> entry : data.entrySet() ) {
            StoreItem item = entry.getValue();
            if (item.getExpire() > 0 && now - item.getTimestamp() > item.getExpire() * 1000) {
                list.add(entry.getKey());
            }
        }
        for (String k : list) {
            data.remove(k);
        }

        StoreKVRequest request = new StoreKVRequest();
        request.setFrom(this.name);
        request.setOperation(StoreKVRequest.operationRemoveAllExpired);
        EM.mainSyncManager.executeRequestSync(request);
    }
}
