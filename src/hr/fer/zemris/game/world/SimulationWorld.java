package hr.fer.zemris.game.world;

import hr.fer.zemris.game.models.Asteroid;
import hr.fer.zemris.game.models.Missile;
import hr.fer.zemris.game.models.Sprite;

public class SimulationWorld extends GameWorld {

    public SimulationWorld(int width, int height, int numberOfCommets) {
        super(width, height, numberOfCommets);
    }

    @Override
    protected void handleNewCommetGraphics(Asteroid sprite) {
       // Empty implementation
    }

    @Override
    protected void handleGraphicUpdate(Sprite spite) {
        // Empty implementation
    }

    @Override
    protected void initializeGraphics() {
        // Empty implementation
    }

    @Override
    protected void cleanupSprites() {
        spriteManager.cleanupSprites();
    }

    @Override
    protected void handleMissileGraphics(Missile missile) {
        // Empty implementation
    }

    @Override
    public void play() {
        int i = 0;
        while (i < 20000) {
            while (paused);
            newFrameStep();
            //getNearestAsteroidsToShip();
            i++;
        }
        System.out.println("IgraGotova " +i);
    }

    @Override
    public void pause() {
        paused = !paused;
    }

    @Override
    protected void asteroidDestroyed() {
        System.out.println("Unisten asterid");
    }

}
