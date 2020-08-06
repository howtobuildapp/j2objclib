package com.github.howtobuildapp.libservice.execute;

import com.google.gson.JsonElement;

public class JSONResponse extends Response {

    public JsonElement getJsonResult(){
        Object ret = super.getResult();
        if ( ret != null && ret instanceof JsonElement) {
            return (JsonElement)ret;
        }
        return null;
    }
}
