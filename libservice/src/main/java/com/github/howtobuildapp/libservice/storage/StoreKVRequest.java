package com.github.howtobuildapp.libservice.storage;

import com.github.howtobuildapp.libservice.execute.Request;

public class StoreKVRequest extends Request {

    public static int operationStore = 1;
    public static int operationGet = 2;
    public static int operationRemove = 3;
    public static int operationRemoveAllExpired = 4;

    private StoreItem item;
    private String from;

    public StoreItem getItem() {
        return item;
    }

    public void setItem(StoreItem item) {
        this.item = item;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setOperation(int operation) {
        this.getParams().put("operation", ""+operation);
    }

    public int getOperation(){
        int op = 0;
        String val = this.getParams().get("operation");
        if (val != null) {
            op = Integer.parseInt(val);
        }
        return op;
    }
}
