package hr.fer.zemris.sm.game.world;

import hr.fer.zemris.sm.game.Constants;
import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Missile;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.models.Star;
import hr.fer.zemris.sm.game.nodes.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.labs.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.labs.scene.control.gauge.linear.elements.Segment;

import java.util.HashMap;
import java.util.Map;

/**
 * Graphics world class is graphical representation if NeuroAsteroids game.
 */
public class GraphicsWorld extends GameWorld {

    private int fps;
    private int points = 0;
    private Group sceneNodes;
    private Label scoreLabel;
    private Map<Sprite, GameNode> nodes;
    private Timeline gameLoop;
    private SimpleMetroArcGauge missileGauge = new SimpleMetroArcGauge();
    ProgressBar fuelGauge = new ProgressBar();

    /**
     * Creates fame world with given width and height, number of asteroids and stars, frames per second.
     * @param fps Frames per second.
     * @param width Game world width.
     * @param height Game world height.
     * @param numberOfAsteroids Number of asteroids on scene.
     * @param numberOfStars Number of stars on scene.
     */
    public GraphicsWorld(int fps, int width, int height, int numberOfAsteroids, int numberOfStars) {
        super(width, height, numberOfAsteroids, numberOfStars);
        this.fps = fps;
        this.nodes = new HashMap<>();

        buildGameLoop();
    }

    @Override
    public void initialize() {
        super.initialize();
        initializeGraphics();

        //On fuel change update fuel indicator
        addListener(GameEvent.FUEL_CHANGE, e -> handleFuelChangeGraphics());

        //When missile is fired update missile gouge and add missile to the screen
        addListener(GameEvent.MISSILE_FIRED, e -> {
            handleMissileStateGraphics();
            handleMissileGraphics((Missile) e.getEventCauseSprite());
        });

        addListener(GameEvent.ASTEROID_ADDED, event -> handleNewAsteroidGraphics((Asteroid) event.getEventCauseSprite()));

        addListener(GameEvent.STAR_ADDED, event -> handleNewStarGraphics((Star) event.getEventCauseSprite()));

        addListener(GameEvent.ASTEROID_DESTROYED, e -> {
            points += Constants.ASTEROID_SCORE;
            handlePointsUpdateGraphics();
        });

        addListener(GameEvent.STAR_COLLECTED, e -> {
            //Star will be removed from screen when cleanup sprites is called
            points += Constants.STAR_SCORE;
            handlePointsUpdateGraphics();
            handleMissileStateGraphics();   //Update missile gouge
        });

        addListener(GameEvent.GAME_OVER, e -> stop());
    }

    /**
     * Creates game loop with given number of frames per second.
     */
    private void buildGameLoop() {
        final Duration frameDuration = Duration.millis(1000.0 / fps);
        final KeyFrame frame = new KeyFrame(frameDuration, event -> newFrameStep());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        gameLoop = timeline;
    }

    /**
     * Initialize graphics.
     */
    private void initializeGraphics() {
        sceneNodes = new Group();

        // Initialize asteroids
        for (Asteroid sprite : spriteManager.getAsteroids()) {
            AsteroidNode asteroid = new AsteroidNode(sprite);

            sprite.getCollisionBounds().setFill(Color.RED);
            sceneNodes.getChildren().add(asteroid.getNode());
            nodes.put(sprite, asteroid);
        }

        for (Star star : spriteManager.getStars()) {
            StarNode node = new StarNode(star);
            sceneNodes.getChildren().add(node.getNode());
            nodes.put(star, node);
        }

        // Initialize ship
        ShipNode shipNode = new ShipNode(ship);
        sceneNodes.getChildren().add(shipNode.getNode());
        nodes.put(ship, shipNode);
        shipNode.getSprite().getBounds().setFill(Color.RED);

        scoreLabel = new Label();
        scoreLabel.textProperty().bind(new SimpleStringProperty("Score: ").concat(points));
        scoreLabel.setFont(Font.font("Cambria", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 25));
        scoreLabel.setTextFill(Color.BLUE);
        sceneNodes.getChildren().add(scoreLabel);
        initMissileGauge(sceneNodes);
        initFuelGauge(sceneNodes);
    }

    /**
     * Creates fuel gauge on screen.
     * @param sceneNodes Scene nodes.
     */
    private void initFuelGauge(Group sceneNodes) {
        fuelGauge.setRotate(-90);
        fuelGauge.setLayoutX(-30);
        fuelGauge.setLayoutY(80);
        sceneNodes.getChildren().add(fuelGauge);
    }

    /**
     * Creates missile gauge on screen.
     * @param sceneNodes Scene nodes.
     */
    private void initMissileGauge(Group sceneNodes) {
        missileGauge.setScaleX(0.5);
        missileGauge.setScaleY(0.5);
        missileGauge.getStyleClass().add("colorscheme-red-to-green-6");
        int count = 6;
        for (int i = 0; i < count; i++) {
            Segment lSegment = new PercentSegment(missileGauge, i*100.0/count, (i+1)*100.0/count);
            missileGauge.segments().add(lSegment);
        }
        missileGauge.setMinValue(0);
        missileGauge.setMaxValue(Constants.NUMBER_OF_MISSILES);
        missileGauge.setValue(missilesLeft);
        missileGauge.setLayoutX(-10);
        missileGauge.setLayoutY(0);
        sceneNodes.getChildren().add(missileGauge);
    }

    /**
     * Returns game surface.
     * @return Game surface.
     */
    public Group getGameSurface() {
        return sceneNodes;
    }

    @Override
    protected void handleSpriteUpdate(Sprite sprite) {
        super.handleSpriteUpdate(sprite);
        handleGraphicUpdate(sprite);
    }

    /**
     * Handle graphic update for given sprite.
     * @param sprite Sprite.
     */
    private void handleGraphicUpdate(Sprite sprite) {
        nodes.get(sprite).update();
    }

    /**
     * Creates new asteroid on screen.
     * @param sprite Asteroid sprite.
     */
    private void handleNewAsteroidGraphics(Asteroid sprite) {
        if (sceneNodes == null) {
            return;
        }
        AsteroidNode asteroid = new AsteroidNode(sprite);
        sprite.getCollisionBounds().setFill(Color.RED);
        sceneNodes.getChildren().add(asteroid.getNode());
        nodes.put(asteroid.getSprite(), asteroid);
    }

    /**
     * Creates new missile on screen.
     * @param missile Missile sprite.
     */
    private void handleMissileGraphics(Missile missile) {
        if (sceneNodes == null) {
            return;
        }
        MissileNode node = new MissileNode(missile);
        sceneNodes.getChildren().add(node.getNode());
        nodes.put(missile, node);
    }

    /**
     * Creates new star on screen.
     * @param sprite Star sprite
     */
    private void handleNewStarGraphics(Star sprite) {
        if (sceneNodes == null) {
            return;
        }
        StarNode node = new StarNode(sprite);
        sceneNodes.getChildren().add(node.getNode());
        nodes.put(sprite, node);
    }

    /**
     * Updates fuel gauge.
     */
    private void handleFuelChangeGraphics() {
        float progress = fuelLeft / (float) Constants.FUEL_START;
        if (progress < 0) progress = 0.0f;
        fuelGauge.setProgress(progress);
    }

    /**
     * Updates missile gaguge on screen.
     */
    protected void handleMissileStateGraphics() {
        missileGauge.setValue(missilesLeft);
    }

    /**
     * Updates collected points on screen.
     */
    private void handlePointsUpdateGraphics() {
        scoreLabel.textProperty().bind(new SimpleStringProperty("Score: ").concat(points));
    }

    /**
     * Gets current collected points.
     * @return Collected points.
     */
    public int getPoints() {
        return points;
    }

    @Override
    protected void cleanupSprites() {
        for (Sprite sprite : spriteManager.getSpritesToBeRemoved()) {
            nodes.get(sprite).explode(node -> {
                sceneNodes.getChildren().remove(node.getNode());
                nodes.remove(sprite);
            });
        }
        spriteManager.cleanupSprites();
    }

    @Override
    public void play() {
        paused = false;
        gameLoop.play();
    }

    @Override
    public void pause() {
        paused = !paused;
        if (paused) {
            gameLoop.pause();
        } else {
            gameLoop.play();
        }
    }

    @Override
    public void stop() {
        gameLoop.stop();
    }
}
