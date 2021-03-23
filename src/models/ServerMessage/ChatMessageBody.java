package models.ServerMessage;

import java.io.Serializable;
import java.util.UUID;

public class ChatMessageBody implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID from;
    private String message;

    public ChatMessageBody(UUID from, String message){
        this.from = from;
        this.message = message;
    }

    public UUID getFrom(){ return this.from; }
    public String getMessage(){ return this.message; }
}
