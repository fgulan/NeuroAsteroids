package hr.fer.zemris.sm.game.physics;

import java.util.Random;

public class Vector extends AbstractVector {
    
	private int dimensions;
	private double[] elements;

	public Vector(double... elems) {
		this(false, elems);
	}

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
    
    public IVector newInstance(int dimension) {
        double[] components = new double[dimension];
        return new Vector(components);
    }
    
	public static Vector parseSimple(String input) {
		String[] elems = input.split("\\s+");
		double[] components = new double[elems.length];
		for (int i = 0; i < components.length; i++) {
			try {
				components[i] = Double.parseDouble(elems[i]);
			} catch (Exception e) {
			    //TODO excpetion
			}
		}
		return new Vector(true, components);
	}
}
