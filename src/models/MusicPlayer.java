package models;

import java.io.File;
import javax.sound.sampled.*;

public class MusicPlayer {
    private final String SOUND_DIRECTORY = "src/assets/sounds/";

    void playMusic(char track){
        String musicLocation = "";
        switch(track){
            case '0':   musicLocation = SOUND_DIRECTORY.concat("adjust-sound.wav");
                break;
            case '1':   musicLocation = SOUND_DIRECTORY.concat("change-marker.wav");
                break;
            case '2':   musicLocation = SOUND_DIRECTORY.concat("click-square.wav");
                break;
            case '3':   musicLocation = SOUND_DIRECTORY.concat("exit-menu.wav");
                break;
            case '4':   musicLocation = SOUND_DIRECTORY.concat("open-menu.wav");
                break;
            case '5':   musicLocation = SOUND_DIRECTORY.concat("title-theme.wav");
                break;
            case '6':   musicLocation = SOUND_DIRECTORY.concat("waiting.wav");
                break;
            case '7':   musicLocation = SOUND_DIRECTORY.concat("you-lose.wav");
                break;
            case '8':   musicLocation = SOUND_DIRECTORY.concat("you-tied.wav");
                break;
            case '9':   musicLocation = SOUND_DIRECTORY.concat("you-won.wav");
                break;
            default:    musicLocation = SOUND_DIRECTORY.concat("waiting.wav");
                break;
        }//end switch

        File musicPath = new File(musicLocation);
        try{
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch(Exception ex){

        }
    }
}
