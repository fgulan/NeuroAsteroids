package hr.fer.zemris.sm.game.sound;

import hr.fer.zemris.sm.game.Constants;
import javafx.scene.media.AudioClip;

/**
 * Created by doctor on 06.12.15..
 */
public class EffectsSoundManager {

    private static EffectsSoundManager instance;

    AudioClip explosion;
    AudioClip fire;
    AudioClip shipExploded;

    public static EffectsSoundManager getInstance() {
        if(instance == null) {
            instance = new EffectsSoundManager();
        }
        return instance;
    }

    public EffectsSoundManager() {
        String explosionAudioClip = ClassLoader.getSystemResource(Constants.EXPLOSION_CLIP).toExternalForm();
        explosion = new AudioClip(explosionAudioClip);

        String fireAudioClip = ClassLoader.getSystemResource(Constants.FIRE_CLIP).toExternalForm();
        fire = new AudioClip(fireAudioClip);

        String shipExplodedAudioClip = ClassLoader.getSystemResource(Constants.SHIP_EXPLODED_CLIP).toExternalForm();
        shipExploded = new AudioClip(shipExplodedAudioClip);
    }

    public void setVolume(double volume) {
        explosion.setVolume(0);
        fire.setVolume(volume);
        shipExploded.setVolume(volume);
    }

    public double getVolume() {
        return fire.getVolume();
    }

    public void playExplosion() {
        explosion.play();
    }

    public void playFire() {
        fire.play();
    }

    public void playShipExploded() {
        shipExploded.play();
    }
}
