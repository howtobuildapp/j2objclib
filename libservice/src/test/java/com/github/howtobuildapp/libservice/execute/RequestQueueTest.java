package com.github.howtobuildapp.libservice.execute;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.github.howtobuildapp.libservice.execute.ExecutorManagerTest.sleep;
import static org.junit.jupiter.api.Assertions.*;

class RequestQueueTest {

    private RequestQueue mainQuene;

    class Request3 extends Request{
        ;
    }

    class Executor3 implements ExecuteInterface{

        @Override
        public void executeRequest(final Request req, Object sender, Callback callback) {

            final Response response = new Response() {
                @Override
                public int getErrno() {
                    return super.getErrno();
                }
            };
            final Callback cb = callback;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                        response.setErrno(3);
                        response.setResult(req);
                        try {
                            URL url = new URL("https://baidu.com");
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.connect();
                            System.out.println(con.getResponseCode() + "+ " + System.currentTimeMillis());
                            System.out.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cb.onComplete(req, response);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        }

        @Override
        public void cancel(Object sender) {

        }
    }

    @BeforeEach
    void setUp() {
        if (mainQuene == null) {
            mainQuene = new RequestQueue();
            mainQuene.setRealExecutor(new Executor3());
        }
        EM.mainManager.registerExecutorForKey("Request3", mainQuene);
    }

    @Test
    void setRealExecutor() {
        Request3 request3 = new Request3();

        EM.mainManager.executeRequest(request3, null, new Callback() {
            @Override
            public void onComplete(Request req, Response resp) {
                assertEquals(3, resp.getErrno());
            }
        });
        mainQuene.resume();
        sleep(200);
        mainQuene.suspend();
        mainQuene.setMaxCount(20);
        for (int i=0;i<20;i++) {
            EM.mainManager.executeRequest(request3, null, null);
        }
        mainQuene.resume();
        //sleep(20000);
        sleep(2000);
    }

}