package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.world.GameWorld;

/**
 * Created by Fredi Šarić on 11.01.16.
 */
public abstract class AbstractPhenotypeController implements IController {

    GameWorld world;

    IPhenotype phenotype;

    public AbstractPhenotypeController(IPhenotype phenotype) {
        this.phenotype = phenotype;
    }

    @Override
    public void setWorld(GameWorld world) {
        this.world = world;
    }

    @Override
    public void disconnect() {
        world = null;
    }

}
