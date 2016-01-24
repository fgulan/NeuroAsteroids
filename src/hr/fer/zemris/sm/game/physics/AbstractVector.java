package hr.fer.zemris.sm.game.physics;

/**
 * Represents abstract vector class. Implements IVector interface.
 */
public abstract class AbstractVector implements IVector {

    @Override
    public abstract double get(int index);

    @Override
    public abstract IVector set(int index, double value);

    @Override
    public abstract int getDimension();

    @Override
    public IVector add(IVector other) {
        if (this.getDimension() != other.getDimension()) {
            throw new RuntimeException("Vector dimension doesn't match!");
        }
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            this.set(i, this.get(i) + other.get(i));
        }
        return this;
    }

    @Override
    public IVector nAdd(IVector other) {
        return this.copy().add(other);
    }

    @Override
    public IVector sub(IVector other) {
        if (this.getDimension() != other.getDimension()) {
            throw new RuntimeException("Vector dimension doesn't match!");
        }
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            this.set(i, this.get(i) - other.get(i));
        }
        return this;
    }
    @Override
    public IVector nSub(IVector other) {
        return this.copy().sub(other);
    }

    @Override
    public IVector scalarMultiply(double byValue) {
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            this.set(i, this.get(i) * byValue);
        }
        return this;
    }

    @Override
    public IVector nScalarMultiply(double byValue) {
        return this.copy().scalarMultiply(byValue);
    }
    
    @Override
    public abstract IVector copy();

    @Override
    public double norm() {
        double sum = 0;
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            sum += Math.pow(this.get(i), 2);
        }
        return Math.sqrt(sum);
    }

    @Override
    public IVector normalize() {
        double norm = this.norm();
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            this.set(i, this.get(i) / norm);
        }
        return this;
    }

    @Override
    public IVector nNormalize() {
        return this.copy().normalize();
    }

    @Override
    public double scalarProduct(IVector other) {
        if (this.getDimension() != other.getDimension()) {
            throw new RuntimeException("Vector dimension doesn't match!");
        }
        double sum = 0;
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            sum += this.get(i) * other.get(i);
        }
        return sum;
    }

    public String toString() {
        return toString(3);
    }

    public String toString(int decimals) {
        int size = this.getDimension();
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < size; i++) {
            builder.append(String.format("%." + decimals + "f", this.get(i)));
            if (i < size - 1) {
                builder.append("; ");
            }
        }
        builder.append(String.format("]%n"));
        return builder.toString();
    }
}
