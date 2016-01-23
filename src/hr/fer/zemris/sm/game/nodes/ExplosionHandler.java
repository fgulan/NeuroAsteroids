package hr.fer.zemris.sm.game.nodes;

/**
 * NeuroAsteroids sprite explosion handler.
 */
public interface ExplosionHandler {

    /**
     * Handles sprite explosion for given node.
     * @param node Game node.
     */
    public void handle(GameNode node);

}
