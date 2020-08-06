package com.github.howtobuildapp.libservice.platform;

import java.util.ArrayList;

public class TimerManager {

    private class TimerItem{
        protected TimerInterface target;
        protected int delay;//seconds
        protected boolean repeat;
        protected long timestamp;
    }

    private ArrayList<TimerItem> items;

    private static TimerManager defaultInstance;

    private TimerManager(){
        items = new ArrayList<TimerItem>();
    }

    public static TimerManager defaultManager(){
        if (defaultInstance == null) {
            defaultInstance = new TimerManager();
        }
        return defaultInstance;
    }

    public void schedule(int delay, boolean repeat, TimerInterface target){
        if (target == null) {
            return;
        }
        TimerItem item = new TimerItem();
        item.target = target;
        item.delay = delay;
        item.repeat = repeat;
        item.timestamp = System.currentTimeMillis();
        items.add(item);
    }

    public void remove(TimerInterface target){
        if (target == null) {
            return;
        }
        ArrayList<TimerItem> rms = new ArrayList<>();
        for(TimerItem item : items) {
            if (item.target == target) {
                rms.add(item);
            }
        }
        items.removeAll(rms);
    }

    //called from app main thread
    public void tickFromMainThread(){
        long now = System.currentTimeMillis();
        ArrayList<TimerItem> rms = new ArrayList<>();
        for(TimerItem item : items) {
            if (now - item.timestamp > item.delay * 1000) {
                item.target.fired();
                if (item.repeat) {
                    item.timestamp = now;
                } else {
                    rms.add(item);
                }
            }
        }
        items.removeAll(rms);
    }
}
