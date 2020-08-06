package com.github.howtobuildapp.libservice.storage;

import com.github.howtobuildapp.libservice.execute.Response;

public class StoreKVResponse extends Response {
    public StoreItem getResultItem() {
        if (this.getResult() != null && this.getResult() instanceof StoreItem) {
            return (StoreItem)this.getResult();
        }
        return null;
    }
}
