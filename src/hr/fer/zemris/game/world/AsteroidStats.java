package hr.fer.zemris.game.world;

import hr.fer.zemris.game.physics.IVector;

public class AsteroidStats {
    
    private IVector speed;
    private IVector center;
    
    public AsteroidStats(IVector speed, IVector center) {
        super();
        this.speed = speed;
        this.center = center;
    }

    public IVector getSpeed() {
        return speed;
    }

    public void setSpeed(IVector speed) {
        this.speed = speed;
    }

    public IVector getCenter() {
        return center;
    }

    public void setCenter(IVector center) {
        this.center = center;
    }
    
    
}
