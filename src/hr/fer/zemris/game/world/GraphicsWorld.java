package hr.fer.zemris.game.world;

import hr.fer.zemris.game.controllers.IController;
import hr.fer.zemris.game.models.Asteroid;
import hr.fer.zemris.game.models.Missile;
import hr.fer.zemris.game.models.Sprite;
import hr.fer.zemris.game.nodes.*;
import hr.fer.zemris.game.world.Listeners.ExplosionListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;


import java.util.HashMap;
import java.util.Map;

public class GraphicsWorld extends GameWorld {

    private int fps;
    private Group sceneNodes;
    private Label scoreLabel;
    private Map<Sprite, GameNode> nodes;
    private Timeline gameLoop;

    public GraphicsWorld(int fps, int width, int height, int numberOfCommets, IController controller) {
        super(width, height, numberOfCommets, controller);
        this.fps = fps;
        this.nodes = new HashMap<>();
        buildGameLoop();
    }

    private final void buildGameLoop() {
        final Duration frameDuration = Duration.millis(1000.0 / fps);
        final KeyFrame frame = new KeyFrame(frameDuration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newFrameStep();
            }
        });
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        gameLoop = timeline;
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
    protected void initializeGraphics() {
        sceneNodes = new Group();

        // Initialize asteroids
        for (Asteroid sprite : spriteManager.getAsteroids()) {
            AsteroidNode asteroid = new AsteroidNode(sprite);

            sprite.getCollisionBounds().setFill(Color.RED);
            sceneNodes.getChildren().add(asteroid.getNode());
            //sceneNodes.getChildren().add(asteroid.getSprite().getBounds());
            //sceneNodes.getChildren().add(asteroid.getSprite().getCollisionBounds());

            nodes.put(asteroid.getSprite(), asteroid);
        }

        // Initialize ship
        ShipNode shipNode = new ShipNode(ship);
        //sceneNodes.getChildren().add(ship.getCollisionBounds());
        sceneNodes.getChildren().add(shipNode.getNode());
        nodes.put(ship, shipNode);
        shipNode.getSprite().getBounds().setFill(Color.RED);
        //sceneNodes.getChildren().add(shipNode.getSprite().getBounds());

        scoreLabel = new Label();
        scoreLabel.textProperty().bind(new SimpleStringProperty("Score: ").concat(destroyedAsteroids));
        scoreLabel.setFont(Font.font("Cambria", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 25));
        scoreLabel.setTextFill(Color.BLUE);
        sceneNodes.getChildren().add(scoreLabel);
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

    public Group getGameSurface() {
        return sceneNodes;
    }

    @Override
    protected void handleMissileGraphics(Missile missile) {
        MissileNode node = new MissileNode(missile);
        node.getSprite().getCollisionBounds().setFill(Color.RED);
        //sceneNodes.getChildren().add(node.getSprite().getCollisionBounds());
        //sceneNodes.getChildren().add(node.getSprite().getBounds());
        sceneNodes.getChildren().add(node.getNode());

        nodes.put(missile, node);
    }

    @Override
    protected void asteroidDestroyed() {
        scoreLabel.textProperty().bind(new SimpleStringProperty("Score: ").concat(destroyedAsteroids));
        for(ExplosionListener el : explosionListeners) {
            el.exploded();
        }
    }
}
