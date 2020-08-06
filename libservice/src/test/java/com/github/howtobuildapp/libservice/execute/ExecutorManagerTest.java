package com.github.howtobuildapp.libservice.execute;

import static org.junit.jupiter.api.Assertions.*;

class ExecutorManagerTest {
    class Request1 extends Request {

    }
    class Request2 extends Request {

    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        EM.mainManager.registerExecutorForRequest(new Request1(), new ExecuteInterface() {
            @Override
            public void executeRequest(Request req, Object sender, Callback callback) {
                Response response = new Response() {
                    @Override
                    public int getErrno() {
                        return super.getErrno();
                    }
                };
                response.setErrno(1);
                response.setResult(req);
                callback.onComplete(req, response);
            }

            @Override
            public void cancel(Object sender) {

            }
        });

        EM.mainManager.registerExecutorForKey("Request2", new ExecuteInterface() {
            @Override
            public void executeRequest(Request req, Object sender, Callback callback) {
                Response response = new Response() {
                    @Override
                    public int getErrno() {
                        return super.getErrno();
                    }
                };
                response.setErrno(2);
                response.setResult(req);
                callback.onComplete(req, response);
            }

            @Override
            public void cancel(Object sender) {

            }
        });
    }

    @org.junit.jupiter.api.Test
    void registerExecutorForRequest() {

        final Request1 request1 = new Request1();
        final Request2 request2 = new Request2();
        EM.mainManager.executeRequest(request1, null, new Callback() {
            @Override
            public void onComplete(Request req, Response resp) {
                assertEquals(1, resp.getErrno());
                assertEquals(request1, resp.getResult());
            }
        });

        EM.mainManager.executeRequest(request2, null, new Callback() {
            @Override
            public void onComplete(Request req, Response resp) {
                assertEquals(2, resp.getErrno());
                assertEquals(request2, resp.getResult());
            }
        });


        sleep(200);
    }

    @org.junit.jupiter.api.Test
    void unregisterExecutorForRequest() {
        EM.mainManager.unregisterExecutorForKey("Request2");
        final Request2 request2 = new Request2();
        final boolean[] executed = {false};
        EM.mainManager.executeRequest(request2, null, new Callback() {
            @Override
            public void onComplete(Request req, Response resp) {
                assertEquals(2, resp.getErrno());
                assertEquals(request2, resp.getResult());
                executed[0] = true;
            }
        });

        sleep(200);
        assertEquals(false, executed[0]);
    }

    public static void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void executeRequest() {
    }

    @org.junit.jupiter.api.Test
    void cancel() {
    }
}