package com.github.howtobuildapp.libservice.storage;

import com.github.howtobuildapp.libservice.execute.Callback;
import com.github.howtobuildapp.libservice.execute.EM;
import com.github.howtobuildapp.libservice.execute.ExecuteInterface;
import com.github.howtobuildapp.libservice.execute.Request;
import com.github.howtobuildapp.libservice.execute.Response;
import com.github.howtobuildapp.libservice.execute.SyncExecuteInterface;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StoreManagerTest {

    class StoreExecutor implements SyncExecuteInterface {
        Map<String, StoreItem> data;

        public StoreExecutor(){
            data = new HashMap<>();
        }

        @Override
        public Response executeRequestSync(Request req) {
            StoreKVResponse response = new StoreKVResponse();
            StoreKVRequest request = (StoreKVRequest)req;
            StoreItem item = request.getItem();
            assertNotNull(request.getFrom());

            int op = request.getOperation();
            if (op == StoreKVRequest.operationGet) {
                assertNotNull(request.getItem());
                StoreItem newitem = data.get(item.getKey() );
                response.setErrno(newitem == null ? -1 : 0);
                response.setResult(newitem);
            } else if (op == StoreKVRequest.operationStore) {
                assertNotNull(request.getItem());

                StoreItem newitem = new StoreItem();
                newitem.setKey(item.getKey());
                newitem.setExpire(item.getExpire());
                newitem.setTimestamp(item.getTimestamp() );
                newitem.setValue(item.getValue());
                data.put(item.getKey(), newitem);

            } else if (op == StoreKVRequest.operationRemove) {
                assertNotNull(request.getItem());
                data.remove(item.getKey());
            } else if (op == StoreKVRequest.operationRemoveAllExpired) {
                long now = System.currentTimeMillis();
                List<String> list = new ArrayList<>();
                for (Map.Entry<String, StoreItem> entry : data.entrySet() ) {
                    StoreItem itemn = entry.getValue();
                    if (itemn.getExpire() > 0 && now - itemn.getTimestamp() > itemn.getExpire() * 1000) {
                        list.add(entry.getKey());
                    }
                }
                for (String k : list) {
                    data.remove(k);
                }
            }
            return response;
        }
    }

    class DataItem {
        protected String name;
        protected  int age;
        protected  String des;
    }

    @Test
    void storeObj() {
        StoreExecutor executor = new StoreExecutor();
        EM.mainSyncManager.registerExecutorForRequest(new StoreKVRequest(), executor);
        DataItem item = new DataItem();
        item.name = "name1";
        item.age = 19;
        item.des = "fkdsfjoasd";

        String s2 = "sfidoapf";

        StoreManager.defaultManager().storeObjWithExp("k1", item, 0);
        DataItem itemnew = (DataItem)StoreManager.defaultManager().getObj("k1", DataItem.class);
        assertEquals(item.name, itemnew.name);
        assertEquals(item.age, itemnew.age);
        assertEquals(item.des, itemnew.des);
        assertEquals(1, executor.data.size());
        System.out.println(executor.data);
        System.out.println(executor.data.get("k1").getValue());

        StoreManager.defaultManager().remove("k1");
        assertNull(StoreManager.defaultManager().getObj("k1", DataItem.class));

        StoreManager.defaultManager().storeObjWithExp("k1", item, 1);
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNull(StoreManager.defaultManager().getObj("k1", DataItem.class));
        assertEquals(0, executor.data.size());


        StoreManager.defaultManager().storeStrWithExp("k2", s2, 1);
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StoreManager.defaultManager().removeAllExpired();
        assertEquals(0, executor.data.size());
        assertNull(StoreManager.defaultManager().getStr("k2"));
    }
}