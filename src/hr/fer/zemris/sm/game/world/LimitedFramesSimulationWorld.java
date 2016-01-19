package hr.fer.zemris.sm.game.world;

import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Missile;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.models.Star;

/**
 *
 * Created by Fredi Šarić on 18.12.15.
 */
public class LimitedFramesSimulationWorld extends GameWorld {

    private int starsCollected;

    private int asteroidsDestroyed;

    private int shootsFired;

    private int frames;

    private final int maxFrames;

    private boolean run;

    public LimitedFramesSimulationWorld(int width, int height, int numberOfComets, int numOfStars, int maxFrames) {
        super(width, height, numberOfComets, numOfStars);
        this.maxFrames = maxFrames;
        this.starsCollected = 0;
        this.asteroidsDestroyed = 0;
        this.shootsFired = 0;
        this.frames = 0;
        this.run = true;
        registerFireListener(() -> shootsFired++);
        registerGameOverListener(() -> run = false);

    }

    @Override
    protected void handleNewStarGraphics(Star sprite) {}

    @Override
    protected void handleNewCommetGraphics(Asteroid sprite) {}

    @Override
    protected void handleMissileStateGraphics() {}

    @Override
    protected void handleGraphicUpdate(Sprite spite) {}

    @Override
    protected void initializeGraphics() {}

    @Override
    protected void cleanupSprites() {
        spriteManager.cleanupSprites();
    }

    @Override
    protected void asteroidDestroyed() {
        asteroidsDestroyed++;
    }

    @Override
    protected void starCollected() {
        starsCollected++;
    }

    public int getStarsCollected() {
        return starsCollected;
    }

    public int getAsteroidsDestroyed() { return asteroidsDestroyed;  }

    public int getShootsFired() { return shootsFired; }

    public int getFrames() {
        return frames;
    }

    @Override
    protected void handleMissileGraphics(Missile missile) {}

    @Override
    public void play() {
        while(run && frames < maxFrames) {
            frames++;
            newFrameStep();
        }
    }

    @Override
    public void pause() {}

    @Override
    protected void handleFuelChangeGraphics() {}

    @Override
    protected void handlePointsUpdateGraphics() {

    }
}
