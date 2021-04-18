package models.ServerMessage;

public class AuthenticationRequestMessage extends Message {
    private String token;

    public AuthenticationRequestMessage(String token){
        super(MessageType.AUTHENTICATION_REQUEST);
        this.token = token;
    }

    public String getToken(){ return token; }
}
