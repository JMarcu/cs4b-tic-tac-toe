
package models.ServerMessage;

public class RefreshTokenMessageBody {
    private String refreshToken;

    public RefreshTokenMessageBody(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken(){ return refreshToken; }
}
