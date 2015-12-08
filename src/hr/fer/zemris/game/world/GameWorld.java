package hr.fer.zemris.game.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import hr.fer.zemris.game.controllers.IController;
import hr.fer.zemris.game.controllers.Input;
import hr.fer.zemris.game.managers.SpriteManager;
import hr.fer.zemris.game.models.Asteroid;
import hr.fer.zemris.game.models.Missile;
import hr.fer.zemris.game.models.Ship;
import hr.fer.zemris.game.models.Sprite;
import hr.fer.zemris.game.models.Ship.Direction;
import hr.fer.zemris.game.physics.IVector;
import hr.fer.zemris.game.physics.Vector;
import hr.fer.zemris.game.world.Listeners.ExplosionListener;
import hr.fer.zemris.game.world.Listeners.FireListener;
import hr.fer.zemris.game.world.Listeners.GameOverListener;
import javafx.scene.shape.Circle;

public abstract class GameWorld {

    protected SpriteManager spriteManager;

    protected boolean paused;
    protected Ship ship;
    protected int width;
    protected int height;
    protected final int numberOfCommets;
    protected boolean gameOver = false;
    protected int destroyedAsteroids = 0;
    public boolean bounceAsteroids = true;

    private final static float MISSILE_SPEED = 7.5f;
    private final static int MISSILE_CHARGE_TIME = 38;
    private final static int MISSILE_CHARE_DELTA = 2;
    private int missileCharge = MISSILE_CHARGE_TIME;

    protected List<GameOverListener> gameOverListeners;
    protected List<FireListener> fireListeners;
    protected List<ExplosionListener> explosionListeners;

    private IController controller;

    public GameWorld(int width, int height, int numberOfCommets, IController controller) {
        super();
        this.width = width;
        this.height = height;
        this.numberOfCommets = numberOfCommets;
        this.spriteManager = new SpriteManager();
        gameOverListeners = new ArrayList<>();
        explosionListeners = new ArrayList<>();
        fireListeners = new ArrayList<>();
        this.controller = controller;
    }

    public void initialize() {
        generateCommetsSprite();
        generateShipSprite();
        initializeGraphics();
    }

    private void generateShipSprite() {
        ship = new Ship();
        ship.translateX(width / 2);
        ship.translateY(3 * height / 4);
        spriteManager.addShipSprite(ship);
    }

    private void generateCommetsSprite() {
        Random rnd = new Random();
        for (int i = 0; i < numberOfCommets; i++) {
            Asteroid sprite = new Asteroid(Vector.random2D(-2, 2));

            double newX = rnd.nextInt(width);
            if (newX > width - sprite.getCollisionBounds().getRadius()) {
                newX = width - sprite.getCollisionBounds().getRadius();
            }

            double newY = rnd.nextInt(height - 300);
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
            missileCharge += MISSILE_CHARE_DELTA;
        }
        handleInput();
        updateSprites();
        checkCollisions();
        cleanupSprites();
        generateNewCommets();
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

    protected abstract void handleNewCommetGraphics(Asteroid sprite);

    private void updateSprites() {
        for (Sprite sprite : spriteManager.getAllSprites()) {
            handleSpriteUpdate(sprite);
        }
    }

    private void handleSpriteUpdate(Sprite sprite) {
        if (sprite instanceof Missile) {
            removeMissile((Missile) sprite);
        } else if (sprite instanceof Ship) {
            handleWalls(sprite);
        } else if (sprite instanceof Asteroid) {
            handleWalls(sprite);
        }
        // System.out.println(sprite.getClass() + " : " + sprite.getBounds().getTranslateX() + " - " +
        // sprite.getBounds().getTranslateY());
        sprite.update();
        handleGraphicUpdate(sprite);
    }

    protected abstract void handleGraphicUpdate(Sprite spite);

    protected abstract void initializeGraphics();

    private void handleWalls(Sprite sprite) {
        Circle circle = sprite.getCollisionBounds();
        double radius = circle.getRadius();

        double velocityX = sprite.getVelocity().get(0);
        if (circle.getCenterX() > (width + 2 * radius) && velocityX > 0) {
            sprite.translateX(-2 * radius);
        } else if ((circle.getCenterX() + 2 * radius) < 0 && velocityX < 0) {
            sprite.translateX(width + 2 * radius);
        }

        double velocityY = sprite.getVelocity().get(1);
        if (circle.getCenterY() > (height + 2 * radius) && velocityY > 0) {
            sprite.translateY(-2 * radius);
        } else if ((circle.getCenterY() + radius) < 0 && velocityY < 0) {
            sprite.translateY(height + 2 * radius);
        }
    }

    private void removeMissile(Sprite sprite) {
        Circle circle = sprite.getCollisionBounds();
        double radius = circle.getRadius();

        boolean passedX = circle.getCenterX() > width + radius || circle.getCenterX() + radius < 0;
        boolean passedY = circle.getCenterY() > height + radius || circle.getCenterY() + radius < 0;

        if (passedY || passedX) {
            spriteManager.addSpritesToBeRemoved(sprite);
        }
    }


    private void handleInput() {
        if (gameOver) {
            return;
        }

        for (Input input : controller.getInput()) {
            switch (input) {
            case MOVE:
                ship.move();
                break;
            case FIRE:
                if (missileCharge >= MISSILE_CHARGE_TIME) {
                    fireMissile();
                    missileCharge = 0;
                }
                break;
            case LEFT:
                ship.rotate(Direction.COUNTER_CLOCKWISE);
                break;
            case RIGHT:
                ship.rotate(Direction.CLOCKWISE);
                break;
            }
        }
    }

    private void fireMissile() {
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
                if (first.intersects(second)) {
                    handleCollision(first, second);
                    break;
                }
            }
        }
    }

    protected void handleCollision(Sprite spriteA, Sprite spriteB) {
        if (spriteA instanceof Asteroid && spriteB instanceof Asteroid) {
            Asteroid first = (Asteroid) spriteA;
            Asteroid second = (Asteroid) spriteB;
            if (bounceAsteroids) {
                first.bounceOf(second);
            }
        } else if ((spriteA instanceof Missile && spriteB instanceof Asteroid)
                || (spriteB instanceof Missile && spriteA instanceof Asteroid)) {
            spriteManager.addSpritesToBeRemoved(spriteA, spriteB);
            destroyedAsteroids++;
            asteroidDestroyed();

        } else if ((spriteA instanceof Ship && spriteB instanceof Asteroid)
                || (spriteB instanceof Ship && spriteA instanceof Asteroid)) {
            spriteManager.addSpritesToBeRemoved(ship);
            gameOver = true;
            notifyListeners();
        }
    }

    protected abstract void cleanupSprites();

    protected abstract void asteroidDestroyed();

    protected abstract void handleMissileGraphics(Missile missile);

    public abstract void play();

    public abstract void pause();

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

    private Double distance(Sprite first, Sprite second) {
        double dx = first.getCenter().get(0) - second.getCenter().get(0);
        double dy = first.getCenter().get(1) - second.getCenter().get(1);
        return Math.sqrt(dx * dx + dy * dy);
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
}
