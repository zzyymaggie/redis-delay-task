package com.example.zzyymaggie.service.callback;

import com.example.zzyymaggie.util.CallbackType;
import com.qq.tars.client.Communicator;
import com.qq.tars.client.CommunicatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class CallbackHandlerFactory {
    private static final Map<CallbackType, CallbackHandler> strategies = new HashMap<>();

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @PostConstruct
    private void init() {
        strategies.put(CallbackType.HTTP, new HttpCallbackHandler(asyncRestTemplate));
        Communicator communicator = CommunicatorFactory.getInstance().getCommunicator();
        strategies.put(CallbackType.TARSRPC, new TarsRPCCallbackHandler(communicator));
    }

    public static CallbackHandler getCallbackHandler(CallbackType callbackType) {
        if (callbackType == null) {
            throw new IllegalArgumentException("callbackType should not be empty.");
        }
        return strategies.get(callbackType);
    }
}
