package com.github.howtobuildapp.libservice.platform;


import com.github.howtobuildapp.libservice.execute.Callback;
import com.github.howtobuildapp.libservice.execute.EM;
import com.github.howtobuildapp.libservice.execute.ExecuteInterface;
import com.github.howtobuildapp.libservice.execute.JSONResponse;
import com.github.howtobuildapp.libservice.execute.Request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NotificationManagerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void postNotification() {
        EM.mainManager.registerExecutorForRequest(new SwitchToMainThreadRequest(), new ExecuteInterface() {
            @Override
            public void executeRequest(Request req, Object sender, Callback callback) {
                final  Callback cb = callback;
                final Request r = req;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cb.onComplete(r, new JSONResponse());
                    }
                });
                t.start();
            }

            @Override
            public void cancel(Object sender) {

            }
        });
        NotificationManager.defaultManager().registerNotification("noti", new NotificationInterface() {
            @Override
            public void onReceiveNotification(String notification, Map<String, String> params) {
                assertEquals("noti", notification);
                assertEquals("1", params.get("p1"));
            }
        });
        Map<String, String> p = new HashMap<>();
        p.put("p1", "1");
        NotificationManager.defaultManager().postNotification("noti", p);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}