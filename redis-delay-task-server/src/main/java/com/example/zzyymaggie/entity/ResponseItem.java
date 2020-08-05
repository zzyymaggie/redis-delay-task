package com.example.zzyymaggie.entity;

public class ResponseItem {
    private String body;

    private boolean success;

    private String message;

    public ResponseItem(String body) {
        this.body = body;
    }

    public ResponseItem success() {
        this.success = true;
        return this;
    }

    public ResponseItem failure(String message) {
        this.success = false;
        this.message = message;
        return this;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
