package controllers;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import models.Player;
import models.SceneCallback.InjectPlayerCallback;
import models.SceneCallback.LaunchMainMenuCallback;
import models.SceneCallback.ReturnToCallback;
import services.AuthService;
import services.AuthService.RegistrationResult;

public class Register {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button registerBtn;
    @FXML private Button returnBtn;
    @FXML private Pane root;

    private InjectPlayerCallback injectPlayerCB;
    private LaunchMainMenuCallback launchMainMenuCB;
    private ReturnToCallback returnToCB;

    @FXML void initialize(){
        root.getStylesheets().add(getClass().getResource("/styles/color-theme.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/styles/register.css").toExternalForm());

        this.registerBtn.disableProperty().bind(usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
    }

    @FXML protected void returnButtonClicked(ActionEvent e){
        returnToCB.returnTo();
    }

    @FXML protected void registerButtonClicked(ActionEvent e){
        try {
            AuthService.getInstance().register(
                usernameField.getText(), 
                passwordField.getText(), 
                new BiConsumer<RegistrationResult, Player>(){
                    @Override
                    public void accept(RegistrationResult result, Player player) {
                        System.out.println("Inside Consumer: " + player);
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                System.out.println("Inside Runnable");
                                switch(result){
                                    case PASSWORD_FAILS_REQUIREMENTS:
                                        break;
                                    case SUCCESS:
                                        Register.this.injectPlayerCB.injectPlayer(player);
                                        Register.this.launchMainMenuCB.launchMainMenu();
                                        break;
                                    case UNKNOWN_ERROR:
                                        break;
                                    case USERNAME_ALREADY_EXISTS:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    }
                }
            );
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public Pane getRoot(){ return this.root; }

    public void setInjectPlayerCB(InjectPlayerCallback injectPlayerCB){
        this.injectPlayerCB = injectPlayerCB;
    }

    public void setLaunchMainMenuCB(LaunchMainMenuCallback launchMainMenuCB){ this.launchMainMenuCB = launchMainMenuCB; }

    public void setReturnToCB(ReturnToCallback returnToCB){this.returnToCB = returnToCB;}
}