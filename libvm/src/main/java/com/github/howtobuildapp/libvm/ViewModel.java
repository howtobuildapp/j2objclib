package com.github.howtobuildapp.libvm;

import com.google.j2objc.annotations.Weak;

public class ViewModel implements ViewInterface, ViewModelInterface {

    @Weak
    private ViewInterface view = null;

    private static int serial = 0;

    public static int getNextSerial() {
        ViewModel.serial++;
        if (ViewModel.serial < 100000000) {
            return ViewModel.serial;
        }
        ViewModel.serial = 0;
        return 0;
    }

    // delegate view methods to real view by default
    @Override
    public void showLoading() {
        if (this.view != null) {
            this.view.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (this.view != null) {
            this.view.hideLoading();
        }
    }

    @Override
    public void showHud(String msg) {
        if (this.view != null) {
            this.view.showHud(msg);
        }
    }

    @Override
    public void showAlert(String title, String msg) {
        if (this.view != null) {
            this.view.showAlert(title, msg);
        }
    }

    @Override
    public void operationFinished(int serial, boolean isSucceed) {
        if (this.view != null) {
            this.view.operationFinished(serial, isSucceed);
        }
    }

    // view model methods
    @Override
    public void setView(ViewInterface view) {
        this.view = view;
    }

    @Override
    public void cancel() {
        this.view = null;
    }

    // possible async operation
    /*public int refreshData() {
        int serial = getNextSerial();
        Some.AsyncOperation(new Callback{
            @Override
            public void onComplete() {
                if (this.view != null){
                    this.view.operationFinished(serial, true);
                }
            }
        });
        return serial;
    }*/
}
