package models.ServerMessage;

public class AuthenticationRequestMessageBody {
    private String token;

    public AuthenticationRequestMessageBody(){
        this("");
    }

    public AuthenticationRequestMessageBody(String token){
        this.token = token;
    }

    public String getToken(){ return token; }
}
