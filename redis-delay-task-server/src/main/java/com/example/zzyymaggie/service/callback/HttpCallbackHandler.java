package com.example.zzyymaggie.service.callback;

import com.example.zzyymaggie.entity.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

public class HttpCallbackHandler implements CallbackHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpCallbackHandler.class);
    private AsyncRestTemplate asyncRestTemplate;

    public HttpCallbackHandler(AsyncRestTemplate asyncRestTemplate){
        this.asyncRestTemplate = asyncRestTemplate;
    }

    @Override
    public void handle(MessageDTO messageDTO) {
        //异步发送
        String url = messageDTO.getCallbackUrl();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<Object> httpEntity = new HttpEntity<>(messageDTO.getTaskInfo(), headers);

        ListenableFuture<ResponseEntity<String>> entity = asyncRestTemplate.postForEntity(url, httpEntity, String.class);
        entity.addCallback(result -> {
            if (result.getStatusCode() != HttpStatus.OK) {
                LOGGER.info(result.getBody());
            }
        }, (e) -> {
            LOGGER.error(e.getMessage());
        });
    }
}
