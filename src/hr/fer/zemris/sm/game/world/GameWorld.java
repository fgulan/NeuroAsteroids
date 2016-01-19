package hr.fer.zemris.sm.game.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static hr.fer.zemris.sm.game.Constants.*;

import hr.fer.zemris.sm.game.controllers.IController;
import hr.fer.zemris.sm.game.controllers.Input;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Missile;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.models.Star;
import hr.fer.zemris.sm.game.models.Ship.Direction;
import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.physics.Vector;
import hr.fer.zemris.sm.game.world.listeners.ExplosionListener;
import hr.fer.zemris.sm.game.world.listeners.FireListener;
import hr.fer.zemris.sm.game.world.listeners.GameOverListener;
import hr.fer.zemris.sm.game.world.listeners.StarListener;
import javafx.scene.shape.Circle;

public abstract class GameWorld implements Serializable {
//Need to implement serilizable just to store IController

    protected SpriteManager spriteManager;

    protected boolean paused;
    protected Ship ship;
    protected int width;
    protected int height;
    protected final int numberOfCommets;
    protected final int numberOfStars;
    protected boolean gameOver = false;
    protected int points = 0;
    public boolean bounceAsteroids = true;
    protected int starsCollected = 0;

    protected int fuelLeft = FUEL_START;
    protected int missilesLeft = NUMBER_OF_MISSILES;

    private final static int MARGIN = 30;
    private int missileCharge = MISSILE_CHARGE_TIME;

    protected List<StarListener> starListeners;
    protected List<GameOverListener> gameOverListeners;
    protected List<FireListener> fireListeners;
    protected List<ExplosionListener> explosionListeners;

    private IController controller;

    public GameWorld(int width, int height, int numberOfCommets, int numberOfStars) {
        super();
        this.width = width;
        this.height = height;
        this.numberOfCommets = numberOfCommets;
        this.numberOfStars = numberOfStars;
        this.spriteManager = new SpriteManager();

        gameOverListeners = new ArrayList<>();
        explosionListeners = new ArrayList<>();
        fireListeners = new ArrayList<>();
        starListeners = new ArrayList<>();
    }

    public void initialize() {
        generateCommetsSprite();
        generateShipSprite();
        generateNewStars();
        initializeGraphics();
    }

    private void generateShipSprite() {
        ship = new Ship();
        ship.translateX(width / 2);
        ship.translateY(3 * height / 4);
        spriteManager.addShipSprite(ship);
    }

    private void generateNewStars() {
        if (spriteManager.getStars().size() >= numberOfStars) {
            return;
        }

        Random rnd = new Random();
        int count = numberOfStars - spriteManager.getStars().size();
        for (int i = 0; i < count; i++) {
            Star sprite = new Star();

            double newX = rnd.nextInt(width);
            if (newX > width - sprite.getCollisionBounds().getRadius()) {
                newX = width - sprite.getCollisionBounds().getRadius();
            }

            double newY = rnd.nextInt(3 * height / 4);
            if (newY > height - sprite.getCollisionBounds().getRadius()) {
                newY = height - sprite.getCollisionBounds().getRadius();
            }
            sprite.translateX(newX);
            sprite.translateY(newY);
            spriteManager.addStarSprites(sprite);
            handleNewStarGraphics(sprite);
        }
    }

    private void generateCommetsSprite() {
        Random rnd = new Random();
        for (int i = 0; i < numberOfCommets; i++) {
            Asteroid sprite = new Asteroid(Vector.random2D(-1, 1));

            double newX = rnd.nextInt(width);
            if (newX > width - sprite.getCollisionBounds().getRadius()) {
                newX = width - sprite.getCollisionBounds().getRadius();
            }

            double newY = rnd.nextInt(3 * height / 4);
            if (newY > height - sprite.getCollisionBounds().getRadius()) {
                newY = height - sprite.getCollisionBounds().getRadius();
            }

            sprite.translateX(newX);
            sprite.translateY(newY);
            spriteManager.addAsteroidSprites(sprite);
        }
    }

    protected void newFrameStep() {
        if (missileCharge < MISSILE_CHARGE_TIME) {
            missileCharge += MISSILE_CHARGE_DELTA;
        }
        handleInput();
        updateSprites();
        checkCollisions();
        cleanupSprites();
        generateNewCommets();
        generateNewStars();
    }

    private void generateNewCommets() {
        if (spriteManager.getAsteroids().size() < numberOfCommets) {
            Random random = new Random();
            Asteroid sprite = new Asteroid(Vector.random2D(-1, 1));
            sprite.translateX(-80 * random.nextDouble());
            sprite.translateY(-80 * random.nextDouble());
            spriteManager.addAsteroidSprites(sprite);
            handleNewCommetGraphics(sprite);
        }
    }

    private void updateSprites() {
        for (Sprite sprite : spriteManager.getAllSprites()) {
            handleSpriteUpdate(sprite);
        }
    }

    private void handleSpriteUpdate(Sprite sprite) {
        if (sprite instanceof Missile) {
            removeMissile(sprite);
        } else if (sprite instanceof Ship) {
            handleWalls(sprite);
        } else if (sprite instanceof Asteroid) {
            handleWalls(sprite);
        }

        sprite.update();
        handleGraphicUpdate(sprite);
    }

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

    private void removeMissile(Sprite sprite) {
        Circle circle = sprite.getCollisionBounds();
        boolean passedX = circle.getCenterX() > width + MARGIN || circle.getCenterX() + MARGIN < 0;
        boolean passedY = circle.getCenterY() > height + MARGIN || circle.getCenterY() + MARGIN < 0;

        if (passedY || passedX) {
            spriteManager.addSpritesToBeRemoved(sprite);
        }
    }

    private boolean checkFuel() {
        if (fuelLeft <= 0) {
            fuelLeft = 0;
            return false;
        }
        return true;
    }

    private void handleInput() {
        if (gameOver) {
            return;
        }
        List<Input> inputs = controller.getInput();
        if (inputs == null || inputs.size() == 0) {
            fuelLeft -= FUEL_STAY;
            handleFuelChangeGraphics();
            return;
        }
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
                    if(hasRemovedFuel) {
                        fuelLeft -= FUEL_ROTATE;
                        hasRemovedFuel = true;
                    }
                };
                break;
            case RIGHT:
                if (hasFuel) {
                    ship.rotate(Direction.CLOCKWISE);
                    if(hasRemovedFuel) {
                        fuelLeft -= FUEL_ROTATE;
                        hasRemovedFuel = true;
                    }
                };
                break;
            }
        }
        handleFuelChangeGraphics();
    }

    private void fireMissile() {
        if (missilesLeft == 0) {
            return;
        }
        missilesLeft--;
        handleMissileStateGraphics();
        Missile missile = new Missile(ship.getCurrentAngle(), MISSILE_SPEED);
        missile.translateX(ship.getCollisionBounds().getCenterX());
        missile.translateY(ship.getCollisionBounds().getCenterY());

        spriteManager.addMissileSprites(missile);
        handleMissileGraphics(missile);
        for (FireListener fireListener : fireListeners) {
            fireListener.fired();
        }
    }

    protected void checkCollisions() {
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

    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
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
            updatePoints(ASTEROID_SCORE);

            asteroidDestroyed();
            return true;
        } else if ((spriteA instanceof Ship && spriteB instanceof Asteroid)
                || (spriteB instanceof Ship && spriteA instanceof Asteroid)) {
            spriteManager.addSpritesToBeRemoved(ship);
            gameOver = true;
            notifyListeners();
            return true;
        } else if ((spriteA instanceof Ship && spriteB instanceof Star)
                || (spriteB instanceof Ship && spriteA instanceof Star)) {
            if (spriteA instanceof Star) {
                spriteManager.addSpritesToBeRemoved(spriteA);
            } else {
                spriteManager.addSpritesToBeRemoved(spriteB);
            }
            updatePoints(STAR_SCORE);
            starsCollected++;
            starCollected();
            notifyStarListeners();
            return true;
        }
        return false;
    }

    public void registerStarCollectedListeners(StarListener listener) {
        starListeners.add(listener);
    }

    private void notifyStarListeners() {
        fuelLeft += STAR_FUEL;
        missilesLeft += STAR_MISSILE;

        if (fuelLeft > FUEL_START) {
            fuelLeft = FUEL_START;
        }
        if (missilesLeft > NUMBER_OF_MISSILES) {
            missilesLeft = NUMBER_OF_MISSILES;
        }
        starCollected();
        for (StarListener listener : starListeners) {
            listener.starCollected();
        }
    }

    private void notifyListeners() {
        for (GameOverListener gameOverListener : gameOverListeners) {
            gameOverListener.gameOver();
        }
    }

    public boolean registerGameOverListener(GameOverListener listener) {
        return gameOverListeners.add(listener);
    }

    public IVector getShipPosition() {
        return new Vector(ship.getCollisionBounds().getCenterX(), ship.getCollisionBounds().getCenterY());
    }

    private void updatePoints(int count) {
        points += count;
        handlePointsUpdateGraphics();
    }

    public void registerFireListener(FireListener fl) {
        fireListeners.add(fl);
    }

    public void registerExplosionListener(ExplosionListener el) {
        explosionListeners.add(el);
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public IController getController() {
        return controller;
    }

    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    public int getPoints() {
        return points;
    }

    public int getCollectedStars() {
        return starsCollected;
    }

    public int getFuelLeft() {
        return fuelLeft;
    }

    public int getMissilesLeft() {
        return missilesLeft;
    }

    protected abstract void handleGraphicUpdate(Sprite spite);

    protected abstract void initializeGraphics();

    protected abstract void handleNewCommetGraphics(Asteroid sprite);

    protected abstract void handleMissileStateGraphics();

    protected abstract void cleanupSprites();

    protected abstract void asteroidDestroyed();

    protected abstract void starCollected();

    protected abstract void handleMissileGraphics(Missile missile);

    public abstract void play();

    public abstract void pause();

    protected abstract void handleFuelChangeGraphics();

    protected abstract void handlePointsUpdateGraphics();

    protected abstract void handleNewStarGraphics(Star sprite);

}
