package custom_controls;

import evolve_nn.IUpdateListener;
import evolve_nn.PopulationInformation;
import evolve_nn.SEESolution;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.List;

/**
 * Provides a 3 dimensional scatter graph of a series of points that are expected to be updated frequently. The graph
 * can be scaled up or down using the scroll wheel and supports rotation by clicking and dragging the graph area.
 * <p>
 * The graph contains a nested subscene so can be used in a mixed 2D and 3D environment.
 * <p>
 * Created by Joseph Billingsley on 01/02/2016.
 * Zooming and draggable movement adapted from source code on http://stackoverflow.com/questions/31073007
 */
public class ScientificScatterGraph<S extends SEESolution> extends Pane implements IUpdateListener<S> {

    private final Rotate rotateX;
    private final Rotate rotateY;

    private double mouseX, mouseY, mouseOldX, mouseOldY;

    private int axisLength = 250;

    private Group root;
    private Group visiblePoints = new Group();

    private PointSphereFactory sphereFactory;

    public ScientificScatterGraph() {

        VBox.setVgrow(this, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);

        root = new Group();
        root.setTranslateY(axisLength);
        root.setTranslateX(axisLength);

        makeZoomable(root);

        rotateX = new Rotate(20, new Point3D(.5, 0, 0));
        rotateY = new Rotate(-45, new Point3D(0, .5, 0));

        SubScene scene = new SubScene(root, 1200, 1200, true, SceneAntialiasing.BALANCED);
        scene.setManaged(false);

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateX(-axisLength / 2);
        camera.setTranslateY(-axisLength / 2);
        scene.setCamera(camera);

        scene.setOnMousePressed(event -> {
            mouseOldX = event.getX();
            mouseOldY = event.getY();
        });

        scene.setOnMouseDragged(event -> {
            mouseX = event.getX();
            mouseY = event.getY();

            double rX = rotateX.getAngle() - (mouseY - mouseOldY);
            double rY = rotateY.getAngle() - (mouseX - mouseOldX);

            rotateX.setAngle(rX);
            rotateY.setAngle(rY);

            mouseOldX = mouseX;
            mouseOldY = mouseY;
        });

        this.getChildren().add(scene);
    }

    /**
     * Adds the axis which define the bounds of the graph. Must be called before any points are added.
     *
     * @param axis The axis to construct the graph with.
     */
    public void setMetrics(List<IAxis> axis) {
        this.sphereFactory = new PointSphereFactory(100, axis);

        Group box = makeAxisBox(axis, axisLength);
        box.getTransforms().addAll(rotateX, rotateY);
        box.getChildren().add(visiblePoints);

        root.getChildren().add(box);
    }

    private Group makeAxisBox(List<IAxis> axis, int size) {
        Group group = new Group();

        double halfStep = .5 * size;

        int arrowProtrusion = 75;

        Color colour = Color.TRANSPARENT;

        // Side faces
        Group axisOne = new Group();

        Group wallLeft = makeWall(size, colour);
        wallLeft.setTranslateX(-size);
        wallLeft.setTranslateY(-size);

        Text axisOneLabel = new Text();
        axisOneLabel.setText(axis.get(0).getName());
        axisOneLabel.setTranslateX(-size - arrowProtrusion / 2);
        axisOneLabel.setCache(true);

        Line axisOneArrow = new Line();
        axisOneArrow.setCache(true);
        axisOneArrow.setEndX(-size - arrowProtrusion);

        axisOne.getChildren().addAll(wallLeft, axisOneArrow, axisOneLabel);

        Group axisTwo = new Group();

        Group wallRight = makeWall(size, colour);
        wallRight.setTranslateX(-halfStep);
        wallRight.setTranslateY(-size);
        wallRight.setTranslateZ(-halfStep);
        wallRight.setRotationAxis(Rotate.Y_AXIS);
        wallRight.setRotate(90);

        Text axisTwoLabel = new Text();
        axisTwoLabel.setText(axis.get(1).getName());
        axisTwoLabel.setTranslateY(-size - arrowProtrusion / 2);
        axisTwoLabel.setCache(true);

        Line axisTwoArrow = new Line();
        axisTwoArrow.setCache(true);
        axisTwoArrow.setEndY(-size - arrowProtrusion);

        axisTwo.getChildren().addAll(wallRight, axisTwoArrow, axisTwoLabel);

        // Bottom face
        Group axisThree = new Group();

        Line axisThreeArrow = new Line();
        axisThreeArrow.setCache(true);
        axisThreeArrow.setEndX(-size - arrowProtrusion);
        axisThreeArrow.setRotationAxis(Rotate.Y_AXIS);
        axisThreeArrow.setRotate(90);
        axisThreeArrow.setTranslateX(halfStep + arrowProtrusion / 2);
        axisThreeArrow.setTranslateZ(-halfStep - arrowProtrusion / 2);

        Text axisThreeLabel = new Text();
        axisThreeLabel.setText(axis.get(2).getName());
        axisThreeLabel.setTranslateZ(-size - arrowProtrusion / 2);
        axisThreeLabel.setCache(true);

        Group wallBottom = makeWall(size, colour);
        wallBottom.setTranslateX(-size);
        wallBottom.setTranslateY(-halfStep);
        wallBottom.setTranslateZ(-halfStep);
        wallBottom.setRotationAxis(Rotate.X_AXIS);
        wallBottom.setRotate(90);

        axisThree.getChildren().addAll(wallBottom, axisThreeArrow, axisThreeLabel);
        // endregion

        group.getChildren().addAll(
                axisOne, axisTwo, axisThree);

        return group;
    }

    private Group makeWall(int size, Color colour) {
        Group wall = new Group();

        Rectangle background = new Rectangle(size, size);
        background.setFill(colour);

        wall.getChildren().add(background);

        int separation = 10;
        int lineWidth = 1;

        Color lineColor = Color.BLACK;

        for (int x = 0; x <= size; x += size / separation) {
            Line line = new Line(0, 0, 0, size);
            line.setTranslateX(x);
            formatGridLine(line, lineColor, lineWidth);

            wall.getChildren().add(line);
        }

        for (int y = 0; y <= size; y += size / separation) {
            Line line = new Line(0, 0, size, 0);
            line.setTranslateY(y);
            formatGridLine(line, lineColor, lineWidth);

            wall.getChildren().addAll(line);
        }

        return wall;
    }

    private Line formatGridLine(Line line, Color color, int lineWidth) {
        line.setStroke(color);
        line.setFill(color);
        line.setStrokeWidth(lineWidth);

        line.setTranslateZ(0);

        return line;
    }

    private void makeZoomable(Node control) {
        final double maxScale = 20, minScale = 0.1;

        control.addEventFilter(ScrollEvent.ANY, event -> {

            double delta = 1.2;
            double scale = control.getScaleX();

            if (event.getDeltaY() < 0) {
                scale = scale / delta; // Zoom out
            } else {
                scale *= delta; // Zoom in
            }

            if (scale > maxScale) scale = maxScale;
            else if (scale < minScale) scale = minScale;

            control.setScaleX(scale);
            control.setScaleY(scale);

            event.consume();
        });
    }

    /**
     * Constructs a representation of the provided point as a particular colour.
     *
     * @param point The point to represent.
     * @param color The colour to make the point.
     * @return The id that identifies the point in the graph.
     */
    public int add(Double[] point, Color color) {
        Sphere sphere = sphereFactory.getSphere(4, color, axisLength, point);

        if (sphere == null)
            return -1;

        visiblePoints.getChildren().add(sphere);

        return visiblePoints.getChildren().size() - 1;
    }

    /**
     * Constructs a representation of the provided points all of a particular colour.
     *
     * @param points The points to represent.
     * @param color  The colour to make the points
     */
    public void addAll(List<Double[]> points, Color color) {
        for (Double[] point : points) {
            add(point, color);
        }
    }

    /**
     * Removes the point matching the provided index.
     *
     * @param index The index of the point to remove.
     */
    public void removePoint(int index) {
        visiblePoints.getChildren().remove(index);
    }

    /**
     * Removes all points from the graph.
     */
    public void clear() {
        visiblePoints.getChildren().clear();
        sphereFactory.freeAll();
    }

    @Override
    public void onUpdate(PopulationInformation<S> populationInformation) {
        clear();

        addAll(populationInformation.getDominatedPoints(), Color.GREEN);
        addAll(populationInformation.getNonDominatedPoints(), Color.RED);
    }
}
