package controllers;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import models.Player;
import models.SceneCallback.LaunchLobbyFinderCallback;
import models.SceneCallback.ReturnToCallback;
import services.AuthService;
import services.AuthService.RegistrationResult;

public class Register {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button registerBtn;
    @FXML private Button returnBtn;
    @FXML private ScrollPane root;

    private Consumer<Boolean> injectOnlineCB;
    private Consumer<Player> injectPlayerCB;
    private LaunchLobbyFinderCallback launchLobbyFinderCB;
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
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                switch(result){
                                    case PASSWORD_FAILS_REQUIREMENTS:
                                        break;
                                    case SUCCESS:
                                        Register.this.injectOnlineCB.accept(true);
                                        Register.this.injectPlayerCB.accept(player);
                                        Register.this.launchLobbyFinderCB.launchLobbyFinder();
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

    public ScrollPane getRoot(){ return this.root; }

    public void setInjectOnlineCB(Consumer<Boolean> injectOnlineCB){
        this.injectOnlineCB = injectOnlineCB;
    }

    public void setInjectPlayerCB(Consumer<Player> injectPlayerCB){
        this.injectPlayerCB = injectPlayerCB;
    }

    public void setLaunchLobbyFinderCB(LaunchLobbyFinderCallback launchLobbyFinderCB){ 
        this.launchLobbyFinderCB = launchLobbyFinderCB; 
    }

    public void setReturnToCB(ReturnToCallback returnToCB){this.returnToCB = returnToCB;}
}