package hr.fer.zemris.sm.game.world;

import java.io.Serializable;
import java.util.*;

import static hr.fer.zemris.sm.game.Constants.*;

import hr.fer.zemris.sm.game.GameConfig;
import hr.fer.zemris.sm.game.controllers.IController;
import hr.fer.zemris.sm.game.controllers.Input;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Missile;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.models.Star;
import hr.fer.zemris.sm.game.models.Ship.Direction;
import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;

/**
 * GameWorld bastract class is implementation of NeuroAsteroids game world. It implements physical part of world.
 * For implementing graphics world override initialize method and sets GameEvent listeners.
 */
public abstract class GameWorld implements Serializable {
    //Need to implement serializable just to store IController

    protected SpriteManager spriteManager;

    protected boolean paused;
    protected Ship ship;
    protected int width;
    protected int height;
    protected final int numberOfComets;
    protected final int numberOfStars;
    protected boolean gameOver = false;
    public boolean bounceAsteroids = true;

    protected int fuelLeft = FUEL_START;
    protected int missilesLeft = NUMBER_OF_MISSILES;

    private int fuelIncrease = GameConfig.getInstance().getFuelIncrease();
    private int ammoIncrease = GameConfig.getInstance().getAmmoIncrease();

    private final static int MARGIN = 30;
    private int missileCharge = MISSILE_CHARGE_TIME;

    private Map<String, List<GameWorldListener>> listenersMap;

    private IController controller;

    /**
     * GamewWolrd constructor.
     * @param width Width of world.
     * @param height height of world
     * @param numberOfComets Number of asteroids on scene.
     * @param numberOfStars Number of stars on scene.
     */
    public GameWorld(int width, int height, int numberOfComets, int numberOfStars) {
        super();
        this.width = width;
        this.height = height;
        this.numberOfComets = numberOfComets;
        this.numberOfStars = numberOfStars;
        this.spriteManager = new SpriteManager();

        listenersMap = new HashMap<>();
    }

    /**
     * Initialize game world.
     */
    public void initialize() {
        generateShipSprite();
        generateCometsSprite();
        generateNewStars();
    }

    /**
     * Generates ship sprite.
     */
    private void generateShipSprite() {
        ship = new Ship();
        ship.translateX(width / 2);
        ship.translateY(3 * height / 4);
        spriteManager.addShipSprite(ship);
        notifyListeners(GameEvent.SHIP_CREATED, new GameEvent(ship));
    }

    /**
     * Generates stars on scene.
     */
    private void generateNewStars() {
        if (spriteManager.getStars().size() >= numberOfStars) {
            return;
        }

        Random rnd = new Random();
        int count = numberOfStars - spriteManager.getStars().size();
        for (int i = 0; i < count; i++) {
            Star star = new Star();

            double newX = rnd.nextInt(width);
            if (newX > width - star.getCollisionBounds().getRadius()) {
                newX = width - star.getCollisionBounds().getRadius();
            }

            double newY = rnd.nextInt(3 * height / 4);
            if (newY > height - star.getCollisionBounds().getRadius()) {
                newY = height - star.getCollisionBounds().getRadius();
            }
            star.translateX(newX);
            star.translateY(newY);
            spriteManager.addStarSprites(star);
            notifyListeners(GameEvent.STAR_ADDED, new GameEvent(star));
        }
    }

    /**
     * Generates asteroids on scene.
     */
    private void generateCometsSprite() {
        Random rnd = new Random();
        for (int i = 0; i < numberOfComets; i++) {
            Asteroid sprite = new Asteroid(Vector.random2D(-1, 1));

            double newX = rnd.nextInt(width);
            if (newX > width - sprite.getCollisionBounds().getRadius()) {
                newX = width - sprite.getCollisionBounds().getRadius();
            }

            double newY = rnd.nextInt(2 * height / 5);
            if (newY > height - sprite.getCollisionBounds().getRadius()) {
                newY = height - sprite.getCollisionBounds().getRadius();
            }

            sprite.translateX(newX);
            sprite.translateY(newY);
            addNewAsteroid(sprite);
        }
    }

    /**
     * Adds new asteroid on scene.
     * @param sprite New Asteroid.
     */
    private void addNewAsteroid(Asteroid sprite) {
        spriteManager.addAsteroidSprites(sprite);
        notifyListeners(GameEvent.ASTEROID_ADDED, new GameEvent(sprite));
    }

    /**
     * Game world frame step.
     */
    protected void newFrameStep() {
        if (missileCharge < MISSILE_CHARGE_TIME) {
            missileCharge += MISSILE_CHARGE_DELTA;
        }
        handleInput();
        updateSprites();
        checkCollisions();
        cleanupSprites();
        generateNewComets();
        generateNewStars();
    }

    /**
     * Generates new asteroids.
     */
    private void generateNewComets() {
        if (spriteManager.getAsteroids().size() < numberOfComets) {
            Random random = new Random();
            Asteroid sprite = new Asteroid(Vector.random2D(-1, 1));
            sprite.translateX(-80 * random.nextDouble());
            sprite.translateY(-80 * random.nextDouble());
            addNewAsteroid(sprite);
        }
    }

    /**
     * Updates all sprites on scene.
     */
    private void updateSprites() {
        spriteManager.getAllSprites().forEach(this::handleSpriteUpdate);
    }

    /**
     * Handle update for each type of sprite.
     * @param sprite Sprite to update.
     */
    protected void handleSpriteUpdate(Sprite sprite) {
        if (sprite instanceof Missile) {
            removeMissile(sprite);
        } else if (sprite instanceof Ship) {
            handleWalls(sprite);
        } else if (sprite instanceof Asteroid) {
            handleWalls(sprite);
        }
        sprite.update();
    }

    /**
     * Handle game world bounds for given sprite.
     * @param sprite Input sprite.
     */
    private void handleWalls(Sprite sprite) {
        Circle circle = sprite.getCollisionBounds();

        double velocityX = sprite.getVelocity().get(0);
        if (circle.getCenterX() > (width + MARGIN) && velocityX > 0) {
            sprite.translateX(-MARGIN);
        } else if ((circle.getCenterX() + MARGIN) < 0 && velocityX < 0) {
            sprite.translateX(width + MARGIN);
        }

        double velocityY = sprite.getVelocity().get(1);
        if (circle.getCenterY() > (height + MARGIN) && velocityY > 0) {
            sprite.translateY(-MARGIN);
        } else if ((circle.getCenterY() + MARGIN) < 0 && velocityY < 0) {
            sprite.translateY(height + MARGIN);
        }
    }

    /**
     * Handle game world bounds for given missile sprite
     * @param sprite Missile sprite.
     */
    private void removeMissile(Sprite sprite) {
        Circle circle = sprite.getCollisionBounds();
        boolean passedX = circle.getCenterX() > width + MARGIN || circle.getCenterX() + MARGIN < 0;
        boolean passedY = circle.getCenterY() > height + MARGIN || circle.getCenterY() + MARGIN < 0;

        if (passedY || passedX) {
            spriteManager.addSpritesToBeRemoved(sprite);
        }
    }

    /**
     * Checks fuel.
     * @return true if there is left fuel, false otherwise.
     */
    private boolean checkFuel() {
        if (fuelLeft <= 0) {
            fuelLeft = 0;
            return false;
        }
        return true;
    }

    /**
     * Handle controller input.
     */
    private void handleInput() {
        if (gameOver) {
            return;
        }
        List<Input> inputs = controller.getInput();
        if (inputs == null || inputs.size() == 0) {
            fuelLeft -= FUEL_STAY;
        } else {
            if (inputs.size() == 1 && inputs.contains(Input.FIRE)) {
                fuelLeft -= FUEL_STAY;
            }

            boolean hasFuel = checkFuel();
            boolean hasRemovedFuel = false;

            for (Input input : inputs) {
                switch (input) {
                    case MOVE:
                        if (hasFuel) {
                            ship.move();
                            fuelLeft -= FUEL_MOVE;
                            hasRemovedFuel = true;
                        }
                        break;
                    case FIRE:
                        if (missileCharge >= MISSILE_CHARGE_TIME) {
                            fireMissile();
                            missileCharge = 0;
                        }
                        break;
                    case LEFT:
                        if (hasFuel) {
                            ship.rotate(Direction.COUNTER_CLOCKWISE);
                            if (hasRemovedFuel) {
                                fuelLeft -= FUEL_ROTATE;
                                hasRemovedFuel = true;
                            }
                        }
                        break;
                    case RIGHT:
                        if (hasFuel) {
                            ship.rotate(Direction.CLOCKWISE);
                            if (hasRemovedFuel) {
                                fuelLeft -= FUEL_ROTATE;
                                hasRemovedFuel = true;
                            }
                        }
                        break;
                }
            }
        }
        notifyListeners(GameEvent.FUEL_CHANGE, null);
    }

    /**
     * Fire missile.
     */
    private void fireMissile() {
        if (missilesLeft == 0) {
            return;
        }
        missilesLeft--;
        Missile missile = new Missile(ship.getCurrentAngle(), MISSILE_SPEED);
        missile.translateX(ship.getCollisionBounds().getCenterX());
        missile.translateY(ship.getCollisionBounds().getCenterY());

        spriteManager.addMissileSprites(missile);
        notifyListeners(GameEvent.MISSILE_FIRED, new GameEvent(missile));
    }

    /**
     * Check for collision between sprites.
     */
    private void checkCollisions() {
        List<Sprite> sprites = spriteManager.getAllSprites();
        int size = sprites.size();

        for (int i = 0; i < size - 1; i++) {
            Sprite first = sprites.get(i);
            for (int j = i + 1; j < size; j++) {
                Sprite second = sprites.get(j);
                if (first.intersects(second) && handleCollision(first, second)) {
                    break;
                }
            }
        }
    }

    /**
     * Handle collision for given sprites.
     * @param spriteA First sprite
     * @param spriteB Second sprite
     * @return true if two sprites really were in collision, false otherwise.
     */
    private boolean handleCollision(Sprite spriteA, Sprite spriteB) {
        if (spriteA instanceof Asteroid && spriteB instanceof Asteroid) {
            Asteroid first = (Asteroid) spriteA;
            Asteroid second = (Asteroid) spriteB;
            if (bounceAsteroids) {
                first.bounceOf(second);
            }
            return true;
        } else if ((spriteA instanceof Missile && spriteB instanceof Asteroid)
                || (spriteB instanceof Missile && spriteA instanceof Asteroid)) {

            spriteManager.addSpritesToBeRemoved(spriteA, spriteB);
            notifyListeners(GameEvent.ASTEROID_DESTROYED, null);
            return true;

        } else if ((spriteA instanceof Ship && spriteB instanceof Asteroid)
                || (spriteB instanceof Ship && spriteA instanceof Asteroid)) {

            spriteManager.addSpritesToBeRemoved(ship);
            gameOver = true;
            notifyListeners(GameEvent.GAME_OVER, null);   //Ship is destroyed
            return true;

        } else if ((spriteA instanceof Ship && spriteB instanceof Star)
                || (spriteB instanceof Ship && spriteA instanceof Star)) {

            updateResources();

            if (spriteA instanceof Star) {
                spriteManager.addSpritesToBeRemoved(spriteA);
                notifyListeners(GameEvent.STAR_COLLECTED, new GameEvent(spriteA));
            } else {
                spriteManager.addSpritesToBeRemoved(spriteB);
                notifyListeners(GameEvent.STAR_COLLECTED, new GameEvent(spriteB));
            }
            return true;
        }
        return false;
    }

    /**
     * Update ship fuel and missiles count.
     */
    private void updateResources() {
        fuelLeft += fuelIncrease;
        if(fuelLeft > FUEL_START) {
            fuelLeft = FUEL_START;
        }

        missilesLeft += ammoIncrease;
        if(missilesLeft > NUMBER_OF_MISSILES) {
            missilesLeft = NUMBER_OF_MISSILES;
        }
    }

    /**
     * Sets game controller.
     * @param controller Game controller.
     */
    public void setController(IController controller) {
        this.controller = controller;
    }

    /**
     * Gets current game controller.
     * @return Current game controller.
     */
    public IController getController() {
        return controller;
    }

    /**
     * Gets current game sprite manager.
     * @return Current game sprite manager.
     */
    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    /**
     * Returns left fuel.
     * @return left fuel.
     */
    public int getFuelLeft() {
        return fuelLeft;
    }

    /**
     * Returns missiles left.
     * @return Missiles left,
     */
    public int getMissilesLeft() {
        return missilesLeft;
    }

    /**
     * Adds game world listener.
     * @param event GameEvent id.
     * @param listener GameWorldListener listener object.
     */
    public void addListener(String event, GameWorldListener listener) {
        List<GameWorldListener> listeners = listenersMap.get(event);
        if( listeners == null ) {
            listeners = new ArrayList<>();
            listenersMap.put(event, listeners);
        }
        listeners.add(listener);
    }

    /**
     * Notify registered listeners for given event type.
     * @param eventType Event type.
     * @param event Game event.
     */
    private void notifyListeners(String eventType, GameEvent event) {
        List<GameWorldListener> listeners = listenersMap.get(eventType);
        if(listeners != null) {
            listeners.forEach(l -> l.worldEvent(event));
        }
    }

    //*******************************************
    //Abstract methods
    //*******************************************

    /**
     * Play game.
     */
    public abstract void play();

    /**
     * Pause game.
     */
    public abstract void pause();

    /**
     * Stop game.
     */
    public abstract void stop();

    /**
     * Checks if there is any sprites to be removed and if, removes them.
     */
    protected abstract void cleanupSprites();
}
