package com.github.howtobuildapp.libvm;

public interface ViewInterface {

    void showLoading();

    void hideLoading();

    void showHud(String msg);

    void showAlert(String title, String msg);

    void operationFinished(int serial, boolean isSucceed);

    //you could add your own common methods to your project
}
