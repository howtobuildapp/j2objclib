package com.github.howtobuildapp.libservice.platform;

import com.github.howtobuildapp.libservice.execute.Request;

public class NotificationRequest extends Request {
    private String notificationName;

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }
}
