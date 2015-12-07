package hr.fer.zemris.game.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static hr.fer.zemris.game.Constants.*;


/**
 *
 * Created by doctor on 05.12.15..
 */
public class BackgroundSoundManager {

    private Media backgroundMusic;

    private MediaPlayer player;

    private static BackgroundSoundManager instance;

    private BackgroundSoundManager() {
        String music = ClassLoader.getSystemResource(BACKGROUND_MUSIC).toExternalForm();
        backgroundMusic = new Media(music);
        player = new MediaPlayer(backgroundMusic);
    }

    public static BackgroundSoundManager getInstance() {
        if(instance == null) {
            instance = new BackgroundSoundManager();
        }
        return instance;
    }

    public void changeBackground(String background) {
        backgroundMusic = new Media(ClassLoader.getSystemResource(background).toExternalForm());
        player = new MediaPlayer(backgroundMusic);
        loopBackground();
    }

    public void loopBackground() {
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    public void playOnlyOnce() {
        player.setCycleCount(1);
        player.play();
    }

    public void stop() {
        player.stop();
    }

    public void pause() {
        player.pause();
    }

    public void setVolume(double volume){
        if(volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Illegal volume");
        }
        player.volumeProperty().set(volume);
    }

    public double getVolume() {
        return player.getVolume();
    }

}
