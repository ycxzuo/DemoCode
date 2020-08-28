package com.yczuoxin.reactor.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyEventSource {

    private List<MyEventListener> listeners = new ArrayList<>();

    public void register(MyEventListener listener){
        listeners.add(listener);
    }

    public void pushEvent(MyEvent event) {
        listeners.forEach(listener -> listener.onPushEvent(event));
    }

    public void stopped(){
        listeners.forEach(MyEventListener::onEventStopped);
    }

    @Data
    @AllArgsConstructor
    public static class MyEvent{
        private Date time;
        private String message;
    }

}
