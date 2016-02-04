package hr.fer.zemris.sm.game.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Missile;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.models.Star;

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
    /**
     * List of all asteroid objects currently on scene.
     */
    private List<Asteroid> ASTEROIDS = new ArrayList<>();
    /**
     * List of all missile objects currently on scene.
     */
    private List<Missile> MISSILES = new ArrayList<>();
    /**
     * List of all star objects currently on scene.
     */
    private List<Star> STARS = new ArrayList<>();

    /**
     * Ship sprite
     */
    private Ship ship;
    
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

    /**
     * Adds given sprite as ship sprite
     * @param sprite Ship sprite.
     */
    public void addShipSprite(Ship sprite) {
        GAME_OBJECTS.add(sprite);
        ship = sprite;
    }

    /**
     * Adds given array of sprites as asteroid sprites.
     * @param sprites Asteroid sprite (or array of them)
     */
    public void addAsteroidSprites(Asteroid ... sprites) {
        if (sprites.length > 1) {
            List<Asteroid> asteroids = Arrays.asList((Asteroid[]) sprites);
            ASTEROIDS.addAll(asteroids);
            GAME_OBJECTS.addAll(asteroids);
        } else {
            ASTEROIDS.add(sprites[0]);
            GAME_OBJECTS.add(sprites[0]);
        }
    }

    /**
     * List of all asteroids on scene.
     * @return List of all asteroids on scene.
     */
    public List<Asteroid> getAsteroids() {
        return ASTEROIDS;
    }

    /**
     * List of all missiles on scene.
     * @return List of all missiles on scene.
     */
    public List<Missile> getMissiles() {
        return MISSILES;
    }

    /**
     * List of all bonus stars on scene.
     * @return List of all bonus stars on scene.
     */
    public List<Star> getStars() {
        return STARS;
    }

    /**
     * Gets ship sprite.
     * @return Ship sprite.
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Adds a given array of sprites as Missile sprites.
     * @param sprites Array of Missile sprites, or Missile sprite
     */
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
     * Adds a given array of sprites as Star sprites.
     * @param sprites Array of Star sprites, or Star sprite
     */
    public void addStarSprites(Star... sprites) {
        if (sprites.length > 1) {
            List<Star> stars = Arrays.asList((Star[]) sprites);
            STARS.addAll(stars);
            GAME_OBJECTS.addAll(stars);
        } else {
            STARS.add(sprites[0]);
            GAME_OBJECTS.add(sprites[0]);
        }
    }
    
    /**
     * Returns set of sprites to be removed.
     * @return Set of sprites.
     */
    public Set<Sprite> getSpritesToBeRemoved() {
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
        STARS.removeAll(REMOVE_SPRITES);
        REMOVE_SPRITES.clear();
    }
}
