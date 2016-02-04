package hr.fer.zemris.sm.game;

/**
 * Class for storing game configs
 *
 * Created by Fredi Šarić on 19.01.16.
 */
public class GameConfig {

    private static GameConfig instance;

    private int numberOfStars;

    private int numberOfComments;

    private int fuelIncrease;

    private int ammoIncrease;

    private double acceleration;

    private double deceleration;

    private GameConfig() {
        numberOfComments = 5;
        numberOfStars = 1;
        fuelIncrease = Constants.STAR_FUEL;
        ammoIncrease = Constants.STAR_MISSILE;
        acceleration = Constants.ACCELERATION_STEP;
        deceleration = Constants.DECELERATION_STEP;
    }

    public static GameConfig getInstance() {
        if(instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public int getFuelIncrease() {
        return fuelIncrease;
    }

    public int getAmmoIncrease() {
        return ammoIncrease;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public void setNumberOfStars(int numberOfStars) {
        if(numberOfStars < 1) return;
        this.numberOfStars = numberOfStars;
    }

    public void setNumberOfComments(int numberOfComments) {
        if(numberOfComments < 0) return;
        this.numberOfComments = numberOfComments;
    }

    public void setFuelIncrease(int fuelIncrease) {
        this.fuelIncrease = fuelIncrease;
    }

    public void setAmmoIncrease(int ammoIncrease) {
        this.ammoIncrease = ammoIncrease;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void setDeceleration(double deceleration) {
        this.deceleration = deceleration;
    }
}

