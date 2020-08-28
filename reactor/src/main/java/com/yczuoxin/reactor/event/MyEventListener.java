package com.yczuoxin.reactor.event;

public interface MyEventListener {
    void onPushEvent(MyEventSource.MyEvent event);

    void onEventStopped();
}
