package com.github.howtobuildapp.libservice.execute;

public interface TableInterface {
    int rowCount();
    int columnCount();
    String colNameAt(int col);
    String colDescAt(int col);

    String valueAt(int row, int col);
    String valueAt(int row, String colName);
}
