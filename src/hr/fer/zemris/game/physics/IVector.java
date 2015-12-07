package hr.fer.zemris.game.physics;

public interface IVector {

    double get(int index);
    IVector set(int index, double value);
    int getDimension();
    IVector add(IVector other);
    IVector nAdd(IVector other);
    IVector sub(IVector other);
    IVector nSub(IVector other);
    IVector copy();
    IVector scalarMultiply(double byValue);
    IVector nScalarMultiply(double byValue);
    double norm();
    IVector normalize();
    IVector nNormalize();
    double cosine(IVector other);
    double scalarProduct(IVector other);
    double[] toArray();
    public IVector reverse(int index);
}
