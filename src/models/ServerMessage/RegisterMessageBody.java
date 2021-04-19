package models.ServerMessage;

public class RegisterMessageBody {
    private String encryptedPassword;
    private String username;

    public RegisterMessageBody(String username, String encryptedPassword){
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getEncryptedPassword(){ return encryptedPassword; }
    public String getUsername(){ return username; }
}
