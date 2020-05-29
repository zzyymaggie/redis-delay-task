package com.example.zzyymaggie.listener;

import java.util.List;

public interface MessageListener {
    void consumeMessage(List<String> ids);
}
