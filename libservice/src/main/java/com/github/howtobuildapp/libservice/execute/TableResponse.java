package com.github.howtobuildapp.libservice.execute;

public class TableResponse extends Response {
    public TableInterface getTableResult(){
        Object ret = super.getResult();
        if ( ret != null && ret instanceof TableInterface) {
            return (TableInterface) ret;
        }
        return null;
    }
}
