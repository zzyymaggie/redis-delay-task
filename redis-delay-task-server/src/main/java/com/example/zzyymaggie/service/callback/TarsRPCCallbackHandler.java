package com.example.zzyymaggie.service.callback;


import com.example.zzyymaggie.entity.TaskInfo;
import com.example.zzyymaggie.protocol.task.FeedbackPrx;
import com.example.zzyymaggie.protocol.task.FeedbackPrxCallback;
import com.example.zzyymaggie.protocol.task.TaskInfoReq;
import com.qq.tars.client.Communicator;
import com.example.zzyymaggie.entity.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class TarsRPCCallbackHandler implements CallbackHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TarsRPCCallbackHandler.class);
    private Communicator communicator;

    public TarsRPCCallbackHandler(Communicator communicator) {
        this.communicator = communicator;
    }

    @Override
    public void handle(MessageDTO messageDTO) {
        TaskInfo taskInfo = messageDTO.getTaskInfo();
        String callbackUrl = messageDTO.getCallbackUrl();
        //callbackUrl = "DemoApp.DemoServer.FeedbackService@tcp -h 127.0.0.1 -p 17602 -t 60000";
        FeedbackPrx proxy = communicator.stringToProxy(FeedbackPrx.class, callbackUrl);
        TaskInfoReq taskInfoReq = new TaskInfoReq();
        BeanUtils.copyProperties(taskInfo, taskInfoReq);
        proxy.async_feedback(new FeedbackPrxCallback() {
            @Override
            public void callback_feedback(int ret, String msg) {
                if (ret != 0) {
                    LOGGER.info("ret={},msg={}", ret, msg);
                }
            }

            @Override
            public void callback_exception(Throwable ex) {
                LOGGER.error("", ex);
            }

            @Override
            public void callback_expired() {
                LOGGER.error("expired");
            }
        }, taskInfoReq);
    }
}
