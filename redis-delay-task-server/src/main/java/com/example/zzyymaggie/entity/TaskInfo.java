package com.example.zzyymaggie.entity;

public class TaskInfo {
    //消息类型
    private String topic;
    //消息体
    private String msgBody;

    //延时间隔，单位：毫秒
    private int interval;

    //客户端定义得到期时间戳，如果为null或0，用当前时间戳+interval
    private long expiredTime;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
}