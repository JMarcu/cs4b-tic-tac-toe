package models.ServerMessage;

import com.google.gson.Gson;

public class Message {
    private String body;
    private MessageType type;

    public Message(){
        this.body = "";
        this.type = MessageType.UNKNOWN;
    }

    public Message(Object body, MessageType type){
        this(new Gson().toJson(body), type);
    }

    public Message(String body, MessageType type){
        this.body = body;
        this.type = type;
    }
    
    public String getBody(){ return body; }
    public MessageType getType(){ return type; }
}
