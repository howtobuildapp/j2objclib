package com.github.howtobuildapp.libservice.execute;

import java.util.HashMap;
import java.util.Map;

public class ExecutorManager implements ExecuteInterface {
    private Map<String, ExecuteInterface> registerInfo;

    private static ExecutorManager defaultInstance;

    protected ExecutorManager(){
        registerInfo = new HashMap<>();
    }

    public static ExecutorManager defaultManager(){
        if (defaultInstance == null) {
            defaultInstance = new ExecutorManager();
        }
        return defaultInstance;
    }

    public void registerExecutorForKey(String key, ExecuteInterface executor){
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

    public void registerExecutorForRequest(Request req, ExecuteInterface executor){
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
    public void executeRequest(Request req, Object sender, Callback callback) {
        if (req == null) {
            return;
        }
        String key = req.getKindString();
        ExecuteInterface executor = registerInfo.get(key);
        if (executor == null) {
            return;
        }
        executor.executeRequest(req, sender, callback);
    }

    @Override
    public void cancel(Object sender) {
        if (sender == null) {
            return;
        }
        for (Map.Entry<String, ExecuteInterface> entry : registerInfo.entrySet()){
            entry.getValue().cancel(sender);
        }
    }
}
