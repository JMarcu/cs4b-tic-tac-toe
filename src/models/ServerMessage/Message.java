package models.ServerMessage;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private Serializable body;
    private MessageType type;
    
    public Message(Serializable body, MessageType type){
        this.body = body;
        this.type = type;
    }

    public Serializable getBody(){ return body; }
    public MessageType getType(){ return type; }
}
