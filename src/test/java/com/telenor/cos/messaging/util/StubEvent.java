package com.telenor.cos.messaging.util;


import com.telenor.cos.messaging.event.Event;

@SuppressWarnings("serial")
public class StubEvent extends Event {
    public boolean isStub() {
        return true;
    }

    @Override
    public String toString(){
        return "this is a stub";
    }
}
