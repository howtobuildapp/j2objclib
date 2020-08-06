package com.github.howtobuildapp.libvm;

public interface ViewModelInterface {
    void setView(ViewInterface view);
    void cancel();
    //int someAsyncOperation();//return a operation serial id.

    //you could add your own common methods to your project
}