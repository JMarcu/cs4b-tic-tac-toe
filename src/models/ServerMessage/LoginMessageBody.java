package models.ServerMessage;

public class LoginMessageBody {
    private String encryptedPassword;
    private String username;

    public LoginMessageBody(String username, String encryptedPassword){
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getEncryptedPassword(){ return encryptedPassword; }
    public String getUsername(){ return username; }
}