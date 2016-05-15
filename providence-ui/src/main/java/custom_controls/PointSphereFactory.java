package custom_controls;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import utils.Normalise;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory for handling the efficient creation of many points. Maintains a pool of sphere and tooltip objects that are
 * assigned to spheres as required. Intended for use with the ScientificScatterGraph component where a very great number
 * of points are required.
 * <p>
 * Created by Joseph Billingsley on 02/02/2016.
 */
public class PointSphereFactory {

    private List<Sphere> spheres = new ArrayList<>();
    private List<Tooltip> tooltips = new ArrayList<>();

    private List<IAxis> axis;

    private int nextFreeSphere = 0;

    /**
     * Initialises the factory to the size provided. The provided axis information is used for constructing of tooltips.
     *
     * @param initialSize The number of points that is expected will be required
     * @param axis        The names of the axis labels.
     */
    public PointSphereFactory(int initialSize, List<IAxis> axis) {
        this.axis = axis;

        for (int i = 0; i < initialSize; i++) {
            spheres.add(new Sphere());
            tooltips.add(new Tooltip());
        }

        freeAll();
    }

    /**
     * Gets a point, creating a new point if there are not enough points in the object pool. Points are positioned
     * in the correct position relative to the axis.
     *
     * @param radius     The radius of the point.
     * @param color      The colour of the point.
     * @param axisLength The length of each axis.
     * @param point      The position of the point
     * @return The constructed point
     */
    public Sphere getSphere(int radius, Color color, int axisLength, Double[] point) {

        for (int i = 0; i < point.length; i++) {
            if (point[i] > axis.get(i).getUpperBound() || point[i] < axis.get(i).getLowerBound())
                return null;
        }

        if (nextFreeSphere == spheres.size()) {
            spheres.add(new Sphere());
            tooltips.add(new Tooltip());
        }

        Sphere sphere = spheres.get(nextFreeSphere);

        Tooltip tooltip = tooltips.get(nextFreeSphere);
        Tooltip.install(sphere, tooltip);

        sphere.setRadius(radius);
        sphere.setMaterial(new PhongMaterial(color));

        String tooltipText = String.format(
                "%1$s: %2$s\n%3$s: %4$s\n",
                axis.get(0).getName(),
                point[0],
                axis.get(1).getName(),
                point[1]);

        Double[] positionedPoint = calculatePosition(axisLength, axis, point);

        sphere.setTranslateX(positionedPoint[0]);
        sphere.setTranslateY(positionedPoint[1]);

        if (point.length > 2) {
            sphere.setTranslateZ(positionedPoint[2]);
            tooltipText += axis.get(2).getName() + ": " + point[2];
        }

        tooltip.setText(tooltipText);

        nextFreeSphere++;

        return sphere;
    }

    private Double[] calculatePosition(int axisLength, List<IAxis> axis, Double[] point) {
        Double[] newPoint = new Double[point.length];

        for (int i = 0; i < axis.size(); i++) {
            newPoint[i] = Normalise.scaleFeature(point[i], axis.get(i).getLowerBound(), axis.get(i).getUpperBound());
            newPoint[i] = -axisLength * newPoint[i];
        }

        return newPoint;
    }

    /**
     * Marks the whole population of cached points as free.
     */
    public void freeAll() {
        nextFreeSphere = 0;
    }
}
