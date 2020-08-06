package com.github.howtobuildapp.libservice.execute;

public abstract class Response {
    private int errno;//0=ok
    private String errmsg;
    private Object result;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isOK() {
        return errno == 0;
    }
}
