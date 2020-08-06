package com.github.howtobuildapp.libservice.platform;

import java.util.ArrayList;

public class AppLifecycleManager implements AppLifecycleInterface {

    private ArrayList<AppLifecycleInterface> registerInfo;

    private static AppLifecycleManager defaultInstance;

    private AppLifecycleManager(){
        registerInfo = new ArrayList<>();
    }

    public static AppLifecycleManager defaultManager(){
        if (defaultInstance == null) {
            defaultInstance = new AppLifecycleManager();
        }
        return defaultInstance;
    }

    public void register(AppLifecycleInterface service) {
        if (service == null) {
            return;
        }

        registerInfo.add(service);
    }

    public void unregister(AppLifecycleInterface servcie) {
        registerInfo.remove(servcie);
    }

    @Override
    public void onAppStarted() {
        for (AppLifecycleInterface servcie : registerInfo) {
            servcie.onAppStarted();
        }
    }

    @Override
    public void onAppEnteredBackground() {
        for (AppLifecycleInterface servcie : registerInfo) {
            servcie.onAppEnteredBackground();
        }
    }

    @Override
    public void onAppExit() {
        for (AppLifecycleInterface servcie : registerInfo) {
            servcie.onAppExit();
        }
    }

}
