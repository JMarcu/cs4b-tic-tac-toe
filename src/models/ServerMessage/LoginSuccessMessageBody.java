package models.ServerMessage;

public class LoginSuccessMessageBody {
    private String jwt;
    private String refreshToken;
    
    public LoginSuccessMessageBody(String jwt, String refreshToken){
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }

    public String getJWT(){ return jwt; }
    public String getRefreshToken(){ return refreshToken; }
}
