package models;

import java.io.File;
import javax.sound.sampled.*;

public class MusicPlayer {
    public enum Track{
        adjustSound, changeMarker, clickSquare, exitMenu, openMenu, title, waiting, lose, tie, win;
    }

    private final String SOUND_DIRECTORY = "src/assets/sounds/";
    Clip clip;

    {
        try{
            clip = AudioSystem.getClip();
        } catch(Exception ex){

        }
    }

    String getMusicLocation(Track track)
    {
        String musicLocation = "";
        switch(track){
            case adjustSound:   musicLocation = SOUND_DIRECTORY.concat("adjust-sound.wav");
                break;
            case changeMarker:  musicLocation = SOUND_DIRECTORY.concat("change-marker.wav");
                break;
            case clickSquare:   musicLocation = SOUND_DIRECTORY.concat("click-square.wav");
                break;
            case exitMenu:      musicLocation = SOUND_DIRECTORY.concat("exit-menu.wav");
                break;
            case openMenu:      musicLocation = SOUND_DIRECTORY.concat("open-menu.wav");
                break;
            case title:         musicLocation = SOUND_DIRECTORY.concat("title-theme.wav");
                break;
            case waiting:       musicLocation = SOUND_DIRECTORY.concat("waiting.wav");
                break;
            case lose:          musicLocation = SOUND_DIRECTORY.concat("you-lose.wav");
                break;
            case tie:           musicLocation = SOUND_DIRECTORY.concat("you-tied.wav");
                break;
            case win:           musicLocation = SOUND_DIRECTORY.concat("you-won.wav");
                break;
            default:
                break;
        }//end switch

        return musicLocation;
    }

    public void playMusic(Track track){
        String musicLocation = getMusicLocation(track);

        if(clip.isRunning()){
            clip.close();
        }

        File musicPath = new File(musicLocation);
        try{
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                //clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{
                System.out.println("Can't find the file");
            }
        } catch(Exception ex){

        }
    }

    public void playSFX(Track track){
        String musicLocation = getMusicLocation(track);

        File musicPath = new File(musicLocation);
        try{
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                //clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{
                System.out.println("Can't find the file");
            }
        } catch(Exception ex){

        }
    }

    /*
    public void playMusicOnce(Track track){
        String musicLocation = getMusicLocation(track);

        if(clip.isRunning()){
            clip.close();
        }

        File musicPath = new File(musicLocation);
        try{
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                //clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{
                System.out.println("Can't find the file");
            }
        } catch(Exception ex){

        }
        
    } */
}
