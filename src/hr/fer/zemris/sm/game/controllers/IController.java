package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.Disconnectable;
import hr.fer.zemris.sm.game.world.GameWorld;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Every class that implements this interface should be able to
 * get an input for the {@link GameWorld} to perform an action.
 *
 */
public interface IController extends Disconnectable, Serializable {

    public List<Input> getInput();

    public void setWorld(GameWorld world);
}
