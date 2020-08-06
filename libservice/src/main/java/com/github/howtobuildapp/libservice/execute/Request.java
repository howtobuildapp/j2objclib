package com.github.howtobuildapp.libservice.execute;

import java.util.HashMap;
import java.util.Map;

public abstract class Request {
    private Map<String, String> params; //key value params
    private int timeout;//timeout in seconds

    protected Request() {
        this.params = new HashMap<>();
        this.timeout = 30;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getKindString(){
        return this.getClass().getSimpleName();
    }
}
