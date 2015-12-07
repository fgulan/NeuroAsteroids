package hr.fer.zemris.game.sound;

import javafx.scene.media.AudioClip;
import static hr.fer.zemris.game.Constants.*;

/**
 * Created by doctor on 06.12.15..
 */
public class EffectsSoundManager {

    private static EffectsSoundManager instance;

    AudioClip explosion;
    AudioClip fire;

    public static EffectsSoundManager getInstance() {
        if(instance == null) {
            instance = new EffectsSoundManager();
        }
        return instance;
    }

    public EffectsSoundManager() {
        String explosionAudioClip = ClassLoader.getSystemResource(EXPLOSION_CLIP).toExternalForm();
        explosion = new AudioClip(explosionAudioClip);

        String fireAudioClip = ClassLoader.getSystemResource(FIRE_CLIP).toExternalForm();
        fire = new AudioClip(fireAudioClip);
    }

    public void setVolume(double volume) {
        explosion.setVolume(volume);
        fire.setVolume(volume);
    }

    public void playExplosion() {
        explosion.play();
    }

    public void playFire() {
        fire.play();
    }
}
