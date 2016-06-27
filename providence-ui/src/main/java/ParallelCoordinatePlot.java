import evolve_nn.IUpdateListener;
import evolve_nn.PopulationInformation;
import evolve_nn.SEESolution;
import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.List;

public class ParallelCoordinatePlot<S extends SEESolution> extends Chart implements IUpdateListener<S> {

    private final Axis[] axes;
    private List<Path> paths = new ArrayList<>();

    private double spacing;

    public ParallelCoordinatePlot(Axis... axes) {
        this.axes = axes;

        for (Axis axis : axes) {
            axis.setSide(Side.LEFT);
        }

        getChartChildren().addAll(axes);
    }

    public Axis[] getAxes() {
        return axes;
    }

    @Override
    protected void layoutChartChildren(double top, double left, double width, double height) {
        Axis[] axes = getAxes();
        int numberOfAxis = axes.length;

        spacing = width / (numberOfAxis - 1);

        double axisWidth = 10, axisHeight = height - 5;

        for (int i = 0; i < numberOfAxis; i++) {
            axes[i].resizeRelocate(i * spacing + axisWidth, 0, axisWidth, axisHeight);
        }
    }

    @Override
    public void onUpdate(PopulationInformation<S> populationInformation) {
        getChartChildren().removeAll(paths);
        paths.clear();

        for (Double[] nonDominatedPoint : populationInformation.getNonDominatedPoints()) {
            // TODO: Reuse path objects where possible
            Path path = new Path();

            MoveTo start = new MoveTo(10, getAxes()[0].getDisplayPosition(nonDominatedPoint[0]));
            path.getElements().add(start);

            for (int i = 1; i < nonDominatedPoint.length; i++) {
                LineTo lineTo = new LineTo(i * spacing + 10, getAxes()[i].getDisplayPosition(nonDominatedPoint[i]));
                path.getElements().add(lineTo);
            }

            paths.add(path);
        }

        getChartChildren().addAll(paths);
    }


}
