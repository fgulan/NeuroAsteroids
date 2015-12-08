package hr.fer.zemris.game.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.game.models.Asteroid;
import hr.fer.zemris.game.models.Missile;
import hr.fer.zemris.game.models.Ship;
import hr.fer.zemris.game.models.Sprite;

/**
 * Sprite manager is responsible for managing and holding all sprite objects.
 * @author Filip Gulan.
 *
 */
public class SpriteManager {
    /**
     * List of all game objects currently on scene.
     */
    private List<Sprite> GAME_OBJECTS = new ArrayList<>();
    
    private List<Asteroid> ASTEROIDS = new ArrayList<>();

    private List<Missile> MISSILES = new ArrayList<>();

    private static Ship ship;
    
    /**
     * Sprites to remove.
     */
    private final static Set<Sprite> REMOVE_SPRITES = new HashSet<>();
    
    /**
     * Returns a list of all currently visible sprites on scene.
     * @return List of all sprites.
     */
    public List<Sprite> getAllSprites() {
        return GAME_OBJECTS;
    }
    
    public void addShipSprite(Ship sprite) {
        GAME_OBJECTS.add(sprite);
        ship = sprite;
    }
    
    public void addAsteroidSprites(Asteroid... sprites) {
        if (sprites.length > 1) {
            List<Asteroid> asteroids = Arrays.asList((Asteroid[]) sprites);
            ASTEROIDS.addAll(asteroids);
            GAME_OBJECTS.addAll(asteroids);
        } else {
            ASTEROIDS.add(sprites[0]);
            GAME_OBJECTS.add(sprites[0]);
        }
    }
    
    public List<Asteroid> getAsteroids() {
        return ASTEROIDS;
    }
    
    public List<Missile> getMissiles() {
        return MISSILES;
    }
    
    public Ship getShip() {
        return ship;
    }
    
    public void addMissileSprites(Missile... sprites) {
        if (sprites.length > 1) {
            List<Missile> missiles = Arrays.asList((Missile[]) sprites);
            MISSILES.addAll(missiles);
            GAME_OBJECTS.addAll(missiles);
        } else {
            MISSILES.add(sprites[0]);
            GAME_OBJECTS.add(sprites[0]);
        }
    }
    
    /**
     * Returns set of sprites to be removed.
     * @return Set of sprites.
     */
    public Set<Sprite> getSpritesToBeRemovec() {
        return REMOVE_SPRITES;
    }
    
    /**
     * Adds given sprites to the list of sprites to be removed.
     * @param sprites Sprites to be removed.
     */
    public void addSpritesToBeRemoved(Sprite... sprites) {
        if (sprites.length > 1) {
            REMOVE_SPRITES.addAll(Arrays.asList((Sprite[]) sprites));
        } else {
            REMOVE_SPRITES.add(sprites[0]);
        }
    }
    
    /**
     * Removes all sprites contained in remove list from game scene.
     */
    public void cleanupSprites() {
        GAME_OBJECTS.removeAll(REMOVE_SPRITES);
        ASTEROIDS.removeAll(REMOVE_SPRITES);
        MISSILES.removeAll(REMOVE_SPRITES);
        REMOVE_SPRITES.clear();
    }

}
