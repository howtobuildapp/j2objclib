package com.github.howtobuildapp.libservice.execute;

import java.util.HashMap;
import java.util.Map;

public class SyncExecutorManager implements SyncExecuteInterface {

    private Map<String, SyncExecuteInterface> registerInfo;
    private static SyncExecutorManager defaultInstance;

    protected SyncExecutorManager(){
        registerInfo = new HashMap<String, SyncExecuteInterface>();
    }

    public static SyncExecutorManager defaultManager(){
        if (defaultInstance == null) {
            defaultInstance = new SyncExecutorManager();
        }
        return defaultInstance;
    }

    public void registerExecutorForKey(String key, SyncExecuteInterface executor){
        if (key == null || executor == null) {
            return;
        }
        registerInfo.put(key, executor);
    }

    public void unregisterExecutorForKey(String key){
        if (key == null) {
            return;
        }
        registerInfo.remove(key);
    }

    public void registerExecutorForRequest(Request req, SyncExecuteInterface executor){
        if (req == null || executor == null) {
            return;
        }
        registerExecutorForKey(req.getKindString(), executor);
    }

    public void unregisterExecutorForRequest(Request req){
        if (req == null) {
            return;
        }
        unregisterExecutorForKey(req.getKindString());
    }

    @Override
    public Response executeRequestSync(Request req) {
        if (req == null || req == null) {
            return null;
        }
        String key = req.getKindString();
        SyncExecuteInterface executor = registerInfo.get(key);
        if (executor == null) {
            return null;
        }
        return executor.executeRequestSync(req);
    }
}
