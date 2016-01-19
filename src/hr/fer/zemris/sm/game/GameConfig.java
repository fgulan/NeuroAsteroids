package hr.fer.zemris.sm.game;

/**
 * Created by Fredi Šarić on 19.01.16.
 */
public class GameConfig {

    private static GameConfig instance;

    private int numberOfStars;

    private int numberOfComments;

    private int fuelIncrease;

    private int ammoIncrease;

    private GameConfig() {
        numberOfComments = 5;
        numberOfStars = 1;

        //Default values
        fuelIncrease = Constants.STAR_FUEL;
        ammoIncrease = Constants.STAR_MISSILE;
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
}

