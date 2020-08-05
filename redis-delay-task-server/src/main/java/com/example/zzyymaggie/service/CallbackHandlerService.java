package com.example.zzyymaggie.service;

import com.alibaba.fastjson.JSONObject;
import com.example.zzyymaggie.entity.MessageDTO;
import com.example.zzyymaggie.entity.TaskInfo;
import com.example.zzyymaggie.service.callback.CallbackHandler;
import com.example.zzyymaggie.service.callback.CallbackHandlerFactory;
import com.example.zzyymaggie.util.CallbackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CallbackHandlerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackHandlerService.class);

    @Async
    public void handler(String message) {
        MessageDTO messageDTO = JSONObject.parseObject(message, MessageDTO.class);
        TaskInfo taskInfo = messageDTO.getTaskInfo();
        //如果（当前时间 - 到期时间戳）> 2 * interval 就打印一个warning日志
        int delay = taskInfo.getInterval() * 2;
        if ((System.currentTimeMillis() - taskInfo.getExpiredTime()) > delay && delay > 0) {
            LOGGER.warn(message + "执行时间超过" + delay + "ms");
        }
        try {
            CallbackType callbackType = CallbackType.fromType(messageDTO.getCallbackType());
            CallbackHandler callbackHandler = CallbackHandlerFactory.getCallbackHandler(callbackType);
            callbackHandler.handle(messageDTO);
        }catch (Exception ex) {
            LOGGER.error("", ex);
        }
    }
}
