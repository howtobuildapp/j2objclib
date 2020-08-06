package com.github.howtobuildapp.libservice.platform;

import java.util.Map;

public interface NotificationInterface {
    void onReceiveNotification(String notification, Map<String, String> params);
}
