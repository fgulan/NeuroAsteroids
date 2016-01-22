package hr.fer.zemris.sm.game.world;

import hr.fer.zemris.sm.game.models.Sprite;

/**
 * Created by Fredi Šarić on 22.01.16.
 */
public class GameEvent {

    public static final String MISSILE_FIRED = "MISSILE_FIRED";

    public static final String FUEL_CHANGE = "FUEL_CHANGE";

    public static final String STAR_COLLECTED = "STAR_COLLECTED";

    public static final String STAR_ADDED = "STAR_ADDED";

    public static final String ASTEROID_DESTROYED = "ASTEROID_DESTROYED";

    public static final String ASTEROID_ADDED = "ASTEROID_ADDED";

    public static final String SHIP_CREATED = "SHIP_CREATED";

    public static final String SHIP_DESTROYED = "SHIP_DESTROYED";

    public static final String GAME_OVER = "GAME_OVER";

    private Sprite eventCauseSprite;

    GameEvent(Sprite sprite) {
        eventCauseSprite = sprite;
    }

    public Sprite getEventCauseSprite() {
        return eventCauseSprite;
    }
}
