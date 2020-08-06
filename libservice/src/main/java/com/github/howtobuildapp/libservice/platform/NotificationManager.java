package com.github.howtobuildapp.libservice.platform;

import com.github.howtobuildapp.libservice.execute.Callback;
import com.github.howtobuildapp.libservice.execute.EM;
import com.github.howtobuildapp.libservice.execute.Request;
import com.github.howtobuildapp.libservice.execute.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager implements NotificationInterface {
    private HashMap<String, ArrayList<NotificationInterface>> registerInfo;

    private static NotificationManager defaultInstance;
    public static NotificationManager defaultManager(){
        if (defaultInstance == null) {
            defaultInstance = new NotificationManager();
        }
        return defaultInstance;
    }

    private NotificationManager(){
        registerInfo = new HashMap<>();
    }

    public void registerNotification(String notificationName, NotificationInterface obj) {
        if (notificationName == null || obj ==null) {
            return;
        }
        ArrayList list = registerInfo.get(notificationName);
        if (list == null) {
            list = new ArrayList();
            registerInfo.put(notificationName, list);
        }
        list.add(obj);
    }

    public void unregisterNotification(String notificationName, NotificationInterface obj) {
        if (notificationName == null || obj ==null) {
            return;
        }
        ArrayList list = registerInfo.get(notificationName);
        if (list == null) {
            return;
        }
        list.remove(obj);
    }

    public void postNotification(final String notificationName, final Map<String, String> params) {
        if (notificationName == null) {
            return;
        }
        NotificationRequest request = new NotificationRequest();
        request.setNotificationName(notificationName);
        if (params != null) {
            request.getParams().putAll(params);
        }

        //post to services
        EM.mainManager.executeRequest(new SwitchToMainThreadRequest(), null, new Callback() {
            @Override
            public void onComplete(Request req, Response resp) {
                onReceiveNotification(notificationName, params);
            }
        });

        //post to app
        EM.mainManager.executeRequest(request, null, null);
    }


    //called by app
    @Override
    public void onReceiveNotification(String notificationName, Map<String, String> params) {
        if (notificationName == null) {
            return;
        }
        List<NotificationInterface> services = registerInfo.get(notificationName);
        for (NotificationInterface service : services) {
            service.onReceiveNotification(notificationName, params);
        }
    }
}
