package com.github.howtobuildapp.libservice.platform;

public interface AppLifecycleInterface {
    void onAppStarted();
    void onAppEnteredBackground();
    void onAppExit();
}
