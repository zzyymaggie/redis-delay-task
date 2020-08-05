package com.example.zzyymaggie.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.zzyymaggie.protocol.task.FeedbackPrx;
import com.example.zzyymaggie.protocol.task.TaskInfoReq;
import com.qq.tars.client.Communicator;
import com.qq.tars.client.CommunicatorFactory;
import com.qq.tars.common.support.Holder;
import com.example.zzyymaggie.entity.MessageDTO;
import com.example.zzyymaggie.entity.ResponseItem;
import com.example.zzyymaggie.entity.TaskInfo;
import com.example.zzyymaggie.task.DelayTaskProducer;
import com.example.zzyymaggie.util.CallbackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/delay")
@RestController
public class DelayTaskController {
    @Autowired
    private DelayTaskProducer delayTaskProducer;

    /**
     * 生产消息
     */
    @PostMapping("/produce")
    public List<ResponseItem> produceMessage(@RequestBody List<MessageDTO> messageDTOS) {
        List<ResponseItem> resultList = new ArrayList<>();
        for (MessageDTO messageDTO : messageDTOS) {
            ResponseItem responseItem = this.checkParameter(messageDTO);
            resultList.add(responseItem);
            if(!responseItem.isSuccess()) {
                continue;
            }
            TaskInfo taskInfo = messageDTO.getTaskInfo();
            long expiredTime = messageDTO.getTaskInfo().getExpiredTime();
            if (expiredTime <= 0) {
                long now = new Date().getTime();
                expiredTime = now + taskInfo.getInterval();
                taskInfo.setExpiredTime(expiredTime);
            }
            String msgInfo = JSONObject.toJSONString(messageDTO);
            delayTaskProducer.produce(msgInfo, expiredTime);
        }
        return resultList;
    }

    private ResponseItem checkParameter(MessageDTO messageDTO) {
        String body = JSON.toJSONString(messageDTO);
        ResponseItem responseItem = new ResponseItem(body);
        if(!CallbackType.isCorrectType(messageDTO.getCallbackType())) {
            responseItem.failure("callbackType不存在");
            return responseItem;
        }
        if(StringUtils.isEmpty(messageDTO.getCallbackUrl())) {
            responseItem.failure("callbackUrl不能为空");
            return responseItem;
        }
        if(messageDTO.getTaskInfo() == null) {
            responseItem.failure("taskInfo字段不能为null");
            return responseItem;
        }
        if(messageDTO.getTaskInfo().getInterval() <= 0 && messageDTO.getTaskInfo().getExpiredTime() <= 0) {
            responseItem.failure("interval和expiredTime至少有一个大于0");
            return responseItem;
        }
        return responseItem.success();
    }

    /**
     * 模拟feedback接口
     *
     * @param taskInfo
     * @return
     */
    @PostMapping("/feedback")
    public String mockCallbackUrl(@RequestBody TaskInfo taskInfo) {
        return "success";
    }

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(DelayTaskController.class);

    @PostMapping("/test")
    public void test() {
        String url = "http://localhost:8093/delay/feedback";
        //异步发送
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setMsgBody("test");
        //设置Header
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<Object> httpEntity = new HttpEntity<>(taskInfo, headers);
        ListenableFuture<ResponseEntity<String>> entity = asyncRestTemplate.postForEntity(url, httpEntity, String.class);
        entity.addCallback(result -> LOGGER.info(result.getBody()),(e) -> LOGGER.error(e.getMessage()));
    }

    /**
     * 直连访问
     */
    @PostMapping("/test2")
    public void test2() {
        Communicator communicator = CommunicatorFactory.getInstance().getCommunicator("tars.tarsregistry.QueryObj@tcp -h\n" +
                "192.168.0.48 -p 17890");
        FeedbackPrx proxy = communicator.stringToProxy(FeedbackPrx.class, "DemoApp.DemoServer.FeedbackService@tcp -h 127.0.0.1 -p 17602 -t 60000");
        TaskInfoReq taskInfo = new TaskInfoReq();
        taskInfo.setMsgBody("test");
        Holder<String> holder = new Holder<>();
        int retVal = proxy.feedback(taskInfo, holder);
    }

    /**
     * 注册中心访问
     */
    @PostMapping("/test3")
    public void test3() {
        Communicator communicator = CommunicatorFactory.getInstance().getCommunicator("tars.tarsregistry.QueryObj@tcp -h\n" +
                "192.168.0.1 -p 17890");
        FeedbackPrx proxy = communicator.stringToProxy(FeedbackPrx.class, "DemoApp.DemoServer.FeedbackService");
        TaskInfoReq taskInfo = new TaskInfoReq();
        taskInfo.setMsgBody("test");
        Holder<String> holder = new Holder<>();
        int retVal = proxy.feedback(taskInfo, holder);
    }
}
