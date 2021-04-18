package models.ServerMessage;

public class Message {
    private MessageType type;

    protected Message(){
        this.type = MessageType.UNKNOWN;
    }

    protected Message(MessageType type){
        this.type = type;
    }
    
    public MessageType getType(){ return type; }
}
