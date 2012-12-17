package com.telenor.cos.messaging.routers;

import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.account.AccountNewEvent;

@Component
public class NewAccountHandlerStub {

    /**
     * Stub method that throws exception to force a rollback
     * @param event the event
     */
    public void handle(AccountNewEvent event){
        throw new RuntimeException("Failed to process AccountNewEvent");
    }
}
