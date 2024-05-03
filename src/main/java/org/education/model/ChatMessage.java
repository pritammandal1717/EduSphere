package org.education.model;

public class ChatMessage {
    private String content;
    private String sender;
    private MassageType type;
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public MassageType getType() {
        return type;
    }
    public void setType(MassageType type) {
        this.type = type;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public enum MassageType{
        CHAT,LEAVE,JOIN
    }
}
