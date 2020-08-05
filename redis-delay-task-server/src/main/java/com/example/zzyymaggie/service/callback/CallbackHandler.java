package com.example.zzyymaggie.service.callback;

import com.example.zzyymaggie.entity.MessageDTO;

/**
 * 回调处理策略
 */
public interface CallbackHandler {
    void handle(MessageDTO messageDTO);
}