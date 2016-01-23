package hr.fer.zemris.sm.game.physics;

import java.util.Random;

/**
 * Implements AbstractVector class. Simple n-dimensional vector representation.
 */
public class Vector extends AbstractVector {

	/**
	 * Vector dimension.
	 */
	private int dimensions;
	/**
	 * Vector components.
	 */
	private double[] elements;

	/**
	 * Vector constructor. Creates Vector from given array of doubles. Array is copied.
	 * @param elems Vector components.
	 */
	public Vector(double... elems) {
		this(false, elems);
	}

	/**
	 * Creates Vector with given array of components. If useGiven array is not copied.
	 * @param useGiven Copy given array.
	 * @param elems Vector components.
	 */
	public Vector(boolean useGiven, double... elems) {
		if (useGiven) {
			this.elements = elems;
		} else {
			this.elements = new double[elems.length];
			for (int i = elems.length - 1; i >= 0; i--) {
				this.elements[i] = elems[i];
			}
		}
		dimensions = elems.length;
	}

	@Override
	public double get(int index) {
		if (index < 0 || index >= dimensions) {
            //TODO excpetion
		}
		return elements[index];
	}

	@Override
	public IVector set(int index, double value) {
		if (index < 0 || index >= dimensions) {
            //TODO excpetion
		}
		this.elements[index] = value;
		return this;
	}

	@Override
	public int getDimension() {
		return dimensions;
	}
	
	public static Vector random2D(int min, int max) {
	    Random random = new Random();
	    double x = min + (max - min) * random.nextDouble();
	    double y = min + (max - min) * random.nextDouble();
	    return new Vector(x, y);
	}
	
    @Override
    public IVector copy() {
        int size = this.getDimension();
        IVector vector = this.newInstance(size);
        for (int i = 0; i < size; i++) {
            vector.set(i, this.get(i));
        }
        return vector;
    }

    private IVector newInstance(int dimension) {
        double[] components = new double[dimension];
        return new Vector(components);
    }
}
