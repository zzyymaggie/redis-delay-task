package com.example.zzyymaggie.entity;

public class MessageDTO {
    //callback类型，枚举值
    private int callbackType;

    //支持http://，lb://， rpc:三种，存储的时候encode一下，以免特殊字符导致解析异常
    private String callbackUrl;

    private TaskInfo taskInfo;

    public int getCallbackType() {
        return callbackType;
    }

    public void setCallbackType(int callbackType) {
        this.callbackType = callbackType;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }
}
