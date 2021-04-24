package models.ServerMessage;

public class AuthenticationResultMessageBody {
    private boolean success;

    public AuthenticationResultMessageBody(){
        this(false);
    }

    public AuthenticationResultMessageBody(boolean success){
        this.success = success;
    }

    public boolean getSuccess(){
        return this.success;
    }
}
