package models.ServerMessage;

import services.AuthService.RegistrationResult;

public class RegistrationResultMessageBody {
    
    private RegistrationResult result;

    public RegistrationResultMessageBody(RegistrationResult result){
        this.result = result;
    }

    public RegistrationResult getResult(){ return result; }
}
