package models;

import java.io.File;
import javax.sound.sampled.*;

public class MusicPlayer {
    public enum Track{
        adjustSound, changeMarker, clickSquare, exitMenu, openMenu, title, waiting, lose, tie, win, vsAi, vsHuman, tickTock;
    }

    private final String SOUND_DIRECTORY = "src/assets/sounds/";
    private boolean shouldPlay = true;
    private boolean shouldPlaySFX = true;
    private String musicLocation = "";

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
            case vsAi:          musicLocation = SOUND_DIRECTORY.concat("vs-ai.wav");
                break;
            case vsHuman:       musicLocation = SOUND_DIRECTORY.concat("vs-human.wav");
                break;
            case tickTock:      musicLocation = SOUND_DIRECTORY.concat("tick-tock.wav");
            default:
                break;
        }//end switch

        return musicLocation;
    }

    public void playMusic(Track track){
        musicLocation = getMusicLocation(track);

        if(shouldPlay){

            if(clip.isRunning()){
                clip.close();
            }

            File musicPath = new File(musicLocation);
            try{
                if(musicPath.exists()){
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    //clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    //VOLUME ADJUSTMENT
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    double gain = 0.25; // % of max volume
                    float dB = (float)(Math.log(gain) /  Math.log(10.0) * 20.0);
                    gainControl.setValue(dB);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                else{
                    System.out.println("Can't find the file");
                }
            } catch(Exception ex){

            }
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
                //VOLUME ADJUSTMENT
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                double gain = 0.25; // % of max volume
                float dB = (float)(Math.log(gain) /  Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                clip.start();
                //clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{
                System.out.println("Can't find the file");
            }
        } catch(Exception ex){

        }
    }

    public void setShouldPlay(boolean bool)
    {
        shouldPlay = bool;
        if (shouldPlay)
        {
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
        else
        {
            if(clip.isRunning()){
                clip.close();
            }
        }
    }
    
    public boolean getShouldPlay()
    {
        return shouldPlay;
    }

    public void setShouldPlaySFX(boolean bool)
    {
        shouldPlaySFX = bool;
    }

    public boolean getShouldPlaySFX()
    {
        return shouldPlaySFX;
    }
}
