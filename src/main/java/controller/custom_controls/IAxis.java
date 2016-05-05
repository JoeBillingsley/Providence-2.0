package controller.custom_controls;

/**
 * Implementations of this interface can be used to produced the axis for 2D or 3D graphs.
 */
public interface IAxis {
    String getName();

    Double getUpperBound();

    Double getLowerBound();
}
