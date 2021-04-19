package models.ServerMessage;

public class LogoutMessageBody {
    private String refreshToken;

    public LogoutMessageBody(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken(){ return refreshToken; }
}