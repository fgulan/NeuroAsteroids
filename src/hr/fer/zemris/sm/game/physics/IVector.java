package hr.fer.zemris.sm.game.physics;

/**
 * IVector interface represents n-dimensional vector.
 */
public interface IVector {

    /**
     * Returns vector's component at given index.
     * @param index Component index, from 0 to dimension - 1
     * @return Vector's component at given index.
     */
    double get(int index);

    /**
     * Sets given value at given index at vector.
     * @param index Vector component index.
     * @param value new Value
     * @return Current IVector instance.
     */
    IVector set(int index, double value);

    /**
     * Returns current vector dimension
     * @return Current vector dimension.
     */
    int getDimension();

    /**
     * Modifies current vector by adding given vector.
     * @param other Vector to add.
     * @return Current IVector instance.
     */
    IVector add(IVector other);

    /**
     * Immutable - Copies current vector and adds given vector.
     * @param other Vector to add.
     * @return New IVector created by adding current and given vector.
     */
    IVector nAdd(IVector other);

    /**
     * Modifies current vector by subtracting it with given vector.
     * @param other Vector to subtract with.
     * @return Current IVector instance.
     */
    IVector sub(IVector other);

    /**
     * Immutable - Copies current vector and subtracts it with given vector.
     * @param other Vector to subtract with.
     * @return New IVector instance.
     */
    IVector nSub(IVector other);

    /**
     * Copies current IVector.
     * @return Copied IVectr instance.
     */
    IVector copy();

    /**
     * Multiplies current vector with given value.
     * @param byValue Value to multiply with.
     * @return Current IVector instance.
     */
    IVector scalarMultiply(double byValue);

    /**
     * Immutable - Multiplies current vector with given value.
     * @param byValue Value to multiply with.
     * @return New copied IVector instance.
     */
    IVector nScalarMultiply(double byValue);

    /**
     * Returns norm of current vector.
     * @return Norm of current vector.
     */
    double norm();

    /**
     * Normalize current vector.
     * @return Current IVector instance.
     */
    IVector normalize();

    /**
     * Immutable - Normalize current vector.
     * @return New copied IVector instance
     */
    IVector nNormalize();

    /**
     * Calculates scalar product of current and given vector.
     * @param other Another vector.
     * @return Scalar product of current and given vector.
     */
    double scalarProduct(IVector other);
}
