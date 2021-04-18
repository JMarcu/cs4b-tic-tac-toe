package models.ServerMessage;

public class AuthenticationResultMessage extends Message {
    private boolean success;

    public AuthenticationResultMessage(){
        this(false);
    }

    public AuthenticationResultMessage(boolean success){
        super(MessageType.AUTHENTICATION_RESULT);
        this.success = success;
    }

    public boolean getSuccess(){
        return this.success;
    }
}
