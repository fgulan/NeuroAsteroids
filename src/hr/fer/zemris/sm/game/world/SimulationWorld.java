package hr.fer.zemris.sm.game.world;

import hr.fer.zemris.sm.game.controllers.IController;
import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Missile;
import hr.fer.zemris.sm.game.models.Sprite;

public class SimulationWorld extends GameWorld {

    private volatile boolean run;

    private int frameCount;


    public SimulationWorld(int width, int height, int numberOfCommets, IController controller) {
        super(width, height, numberOfCommets, controller);
        run = true;
        registerGameOverListener(() -> run = false);
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

        while (run) {
            //while (paused);
            frameCount++;
            newFrameStep();
            //getNearestAsteroidsToShip();
        }
    }

    @Override
    public void pause() {
        paused = !paused;
    }

    @Override
    protected void asteroidDestroyed() {
        //System.out.println("Unisten asteroid");
    }

    public int getFrameCount() {
        return frameCount;
    }
}
