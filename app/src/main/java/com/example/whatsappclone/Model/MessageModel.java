package com.example.whatsappclone.Model;

public class MessageModel {

    String uid;
    String message;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    String messageId;
    Long timeStamp;

    public MessageModel(String uid, String message, Long timeStamp) {
        this.uid = uid;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessageModel(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }

    public MessageModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
