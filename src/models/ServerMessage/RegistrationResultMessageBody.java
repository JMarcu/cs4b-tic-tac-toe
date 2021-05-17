package models.ServerMessage;

import models.Player;
import services.AuthService.RegistrationResult;

public class RegistrationResultMessageBody {
    
    private Player player;
    private RegistrationResult result;

    public RegistrationResultMessageBody(RegistrationResult result){
        this.player = null;
        this.result = result;
    }

    public RegistrationResultMessageBody(RegistrationResult result, Player player){
        this.player = player;
        this.result = result;
    }

    public Player getPlayer(){ return player; }
    public RegistrationResult getResult(){ return result; }
}
