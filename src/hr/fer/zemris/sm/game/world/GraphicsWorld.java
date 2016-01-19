package hr.fer.zemris.sm.game.world;

import hr.fer.zemris.sm.game.Constants;
import hr.fer.zemris.sm.game.controllers.IController;
import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Missile;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.models.Star;
import hr.fer.zemris.sm.game.nodes.*;
import hr.fer.zemris.sm.game.world.listeners.ExplosionListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class GraphicsWorld extends GameWorld {

    private int fps;
    private Group sceneNodes;
    private Label scoreLabel;
    private Map<Sprite, GameNode> nodes;
    private Timeline gameLoop;
    private SimpleMetroArcGauge missileGauge = new SimpleMetroArcGauge();
    ProgressBar fuelGauge = new ProgressBar();

    public GraphicsWorld(int fps, int width, int height, int numberOfCommets, int numberOfStars) {
        super(width, height, numberOfCommets, numberOfStars);
        this.fps = fps;
        this.nodes = new HashMap<>();
        buildGameLoop();
    }

    private final void buildGameLoop() {
        final Duration frameDuration = Duration.millis(1000.0 / fps);
        final KeyFrame frame = new KeyFrame(frameDuration, event -> newFrameStep());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        gameLoop = timeline;
    }

    @Override
    protected void initializeGraphics() {
        sceneNodes = new Group();

        // Initialize asteroids
        for (Asteroid sprite : spriteManager.getAsteroids()) {
            AsteroidNode asteroid = new AsteroidNode(sprite);

            sprite.getCollisionBounds().setFill(Color.RED);
            sceneNodes.getChildren().add(asteroid.getNode());
            //sceneNodes.getChildren().add(asteroid.getSprite().getBounds());
            //sceneNodes.getChildren().add(asteroid.getSprite().getCollisionBounds());

            nodes.put(sprite, asteroid);
        }

        for (Star star : spriteManager.getStars()) {
            StarNode node = new StarNode(star);
            // node.getSprite().getCollisionBounds().setFill(Color.RED);
            // sceneNodes.getChildren().add(star.getCollisionBounds());
            sceneNodes.getChildren().add(node.getNode());
            nodes.put(star, node);
        }

        // Initialize ship
        ShipNode shipNode = new ShipNode(ship);
        //sceneNodes.getChildren().add(ship.getCollisionBounds());
        sceneNodes.getChildren().add(shipNode.getNode());
        nodes.put(ship, shipNode);
        shipNode.getSprite().getBounds().setFill(Color.RED);
        //sceneNodes.getChildren().add(shipNode.getSprite().getBounds());

        scoreLabel = new Label();
        scoreLabel.textProperty().bind(new SimpleStringProperty("Score: ").concat(points));
        scoreLabel.setFont(Font.font("Cambria", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 25));
        scoreLabel.setTextFill(Color.BLUE);
        sceneNodes.getChildren().add(scoreLabel);
        initMissileGauge(sceneNodes);
        initFuelGauge(sceneNodes);
    }

    private void initFuelGauge(Group sceneNodes) {
        fuelGauge.setRotate(-90);
        fuelGauge.setLayoutX(-30);
        fuelGauge.setLayoutY(80);
        sceneNodes.getChildren().add(fuelGauge);
    }

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

    public Group getGameSurface() {
        return sceneNodes;
    }

    @Override
    protected void handleGraphicUpdate(Sprite spite) {
        nodes.get(spite).update();
    }

    @Override
    protected void handleNewCommetGraphics(Asteroid sprite) {
        AsteroidNode asteroid = new AsteroidNode(sprite);

        sprite.getCollisionBounds().setFill(Color.RED);
        sceneNodes.getChildren().add(asteroid.getNode());
        //sceneNodes.getChildren().add(asteroid.getSprite().getBounds());
        //sceneNodes.getChildren().add(asteroid.getSprite().getCollisionBounds());
        nodes.put(asteroid.getSprite(), asteroid);
    }

    @Override
    protected void handleFuelChangeGraphics() {
        float progress = fuelLeft / (float) Constants.FUEL_START;
        if (progress < 0) progress = 0.0f;
        fuelGauge.setProgress(progress);
    }

    @Override
    protected void cleanupSprites() {
        for (Sprite sprite : spriteManager.getSpritesToBeRemovec()) {
            nodes.get(sprite).explode(new ExplosionHandler() {
                @Override
                public void handle(GameNode node) {
                    sceneNodes.getChildren().remove(node.getNode());
                    nodes.remove(sprite);
                }
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

    public void stop() {
        gameLoop.stop();
    }

    @Override
    protected void handleMissileGraphics(Missile missile) {
        MissileNode node = new MissileNode(missile);
        sceneNodes.getChildren().add(node.getNode());
        nodes.put(missile, node);
    }

    @Override
    protected void asteroidDestroyed() {
        for(ExplosionListener el : explosionListeners) {
            el.exploded();
        }
    }

    @Override
    protected void starCollected() {
        scoreLabel.textProperty().bind(new SimpleStringProperty("Score: ").concat(points));
        missileGauge.setValue(missilesLeft);
    };
    
    @Override
    protected void handleNewStarGraphics(Star sprite) {
        if (sceneNodes == null) {
            return;
        }
        StarNode node = new StarNode(sprite);
        sceneNodes.getChildren().add(node.getNode());
        nodes.put(sprite, node);
    }

    @Override
    protected void handleMissileStateGraphics() {
        missileGauge.setValue(missilesLeft);
    }

    @Override
    protected void handlePointsUpdateGraphics() {
        scoreLabel.textProperty().bind(new SimpleStringProperty("Score: ").concat(points));
    }
}
