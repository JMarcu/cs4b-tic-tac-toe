package models.ServerMessage;

import java.io.Serializable;

public class Message<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    private T body;
    private MessageType type;
    
    public Message(T body, MessageType type){
        this.body = body;
        this.type = type;
    }

    public T getBody(){ return body; }
    public MessageType getType(){ return type; }
}
