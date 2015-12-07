package hr.fer.zemris.game.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sprite manager is responsible for managing and holding all sprite objects.
 * @author Filip Gulan.
 *
 */
public class SpriteManager {
    /**
     * List of all game objects currently on scene.
     */
    private final static List<Sprite> GAME_OBJECTS = new ArrayList<>();
    
    /**
     * List of sprites to check collision between them.
     */
    private final static List<Sprite> CHECK_COLLISION = new ArrayList<>();
    
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
     * Adds given list of sprites to the game scene.
     * @param sprites List of sprites to add.
     */
    public void addSprites(Sprite... sprites) {
        GAME_OBJECTS.addAll(Arrays.asList(sprites));
    }
    
    /**
     * Removes given sprites from game scene.
     * @param sprites List of sprites to remove.
     */
    public void removeSprites(Sprite... sprites) {
        GAME_OBJECTS.removeAll(Arrays.asList(sprites));
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
     * Returns a list of sprite objects to check for collision between them.
     * It is a copy of all current sprite objects on scene.
     * @return List of sprites to check for collision between them.
     */
    public List<Sprite> getCollisionsToCheck() {
        return CHECK_COLLISION;
    }
    
    /**
     * Clears the list of sprites to check for collision.
     */
    public void resetCollisions() {
        CHECK_COLLISION.clear();
        CHECK_COLLISION.addAll(GAME_OBJECTS);
    }
    
    /**
     * Removes all sprites contained in remove list from game scene.
     */
    public void cleanupSprites() {
        GAME_OBJECTS.removeAll(REMOVE_SPRITES);
        REMOVE_SPRITES.clear();
    }

}
