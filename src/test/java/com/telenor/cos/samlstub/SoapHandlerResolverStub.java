package com.telenor.cos.samlstub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * This class ensures that the stub handler {@link SoapHandlerStub}
 * is invoked when request/responses for Fitnesse are processed
 * @author t798435
 * @since 23 Nov 2011
 */
@Component("SoapHandlerResolverStub")
public class SoapHandlerResolverStub implements HandlerResolver {

    @Autowired
    private SoapHandlerStub handler;

    @SuppressWarnings("rawtypes")
    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(handler);
        return handlerChain;
    }
}