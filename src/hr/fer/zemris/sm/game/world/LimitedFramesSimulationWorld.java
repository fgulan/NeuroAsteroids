package hr.fer.zemris.sm.game.world;

/**
 *  Simulation world for network training.
 *
 *  It has ability to stop run after some period of time.
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

        addListener(GameEvent.GAME_OVER, e -> run = false);
        addListener(GameEvent.MISSILE_FIRED, e -> shootsFired++);
        addListener(GameEvent.ASTEROID_DESTROYED, e -> asteroidsDestroyed++);
        addListener(GameEvent.STAR_COLLECTED, e -> starsCollected++);
    }

    @Override
    protected void cleanupSprites() {
        spriteManager.cleanupSprites();
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
    public void play() {
        while(run && frames < maxFrames) {
            frames++;
            newFrameStep();
        }
    }

    @Override
    public void pause() {}

    @Override
    public void stop() {};
}
