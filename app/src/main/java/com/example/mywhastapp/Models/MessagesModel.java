package com.example.mywhastapp.Models;

public class MessagesModel {
    String msgId, message;    // msgId is User Id who is sending the message
    String messageId;          //messageId is Id for the particular message
    Long timeStamp;

    public MessagesModel(String msgId, String message, Long timeStamp) {
        this.msgId = msgId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessagesModel(String msgId, String message, String messageId, Long timeStamp) {
        this.msgId = msgId;
        this.message = message;
        this.messageId = messageId;
        this.timeStamp = timeStamp;
    }                                        //No use Yet

    public MessagesModel(String msgId, String message) {
        this.msgId = msgId;
        this.message = message;
    }
    public MessagesModel(){                                       // Empty Contructor to works with Firebase

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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
