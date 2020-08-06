package com.github.howtobuildapp.libservice.execute;

public interface ExecuteInterface {
    void executeRequest(Request req, Object sender, Callback callback);
    void cancel(Object sender);
}
