package co.edu.eafit;

public class Message {

    private Integer messageType;

    public Message(Integer messageType) {
        this.messageType = messageType;
    }

    public Message() {
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
}
