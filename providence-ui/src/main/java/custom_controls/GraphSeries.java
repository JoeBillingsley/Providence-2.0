package custom_controls;

import javafx.fxml.FXMLLoader;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A series of Scatter Charts optimised for use with frequently changing data. Enough charts are created to be able to
 * support the number of metrics that are added to the series.
 * <p>
 * This component is optimised for use with frequently changing data. Internally it reuses {@link javafx.scene.chart.XYChart.Series}
 * objects to prevent excessive garbage collection. Furthermore chart animations are not displayed.
 * <p>
 * Created by Joseph Billingsley on 08/03/2016.
 */
public class GraphSeries extends VBox {

    private List<ScatterChart<Number, Number>> charts;
    private List<List<XYChart.Series<Number, Number>>> seriesLists;

    private List<IAxis> metrics;

    public GraphSeries() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/custom_controls/GraphSeries.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        charts = new ArrayList<>();
        seriesLists = new ArrayList<>();
    }

    /**
     * Adds the provided metrics and constructs the necessary charts. Each metric will be compared against each other
     * metrics once.
     *
     * @param metrics The different metrics to consider.
     */
    public void addMetrics(List<IAxis> metrics) {
        this.metrics = metrics;

        for (int i = 0; i < metrics.size(); i++) {
            for (int j = i + 1; j < metrics.size(); j++) {
                ScatterChart<Number, Number> chart = new ScatterChart<>(new NumberAxis(), new NumberAxis());

                chart.setAnimated(false);

                chart.getXAxis().setLabel(metrics.get(i).getName());
                chart.getYAxis().setLabel(metrics.get(j).getName());

                charts.add(chart);
            }
        }

        this.getChildren().addAll(charts);
    }

    /**
     * Defines a series of data that will be common across all metrics.
     *
     * @param name The name of the metric that uniquely identifies it.
     * @return The index of the series.
     */
    public int addSeries(String name) {

        List<XYChart.Series<Number, Number>> seriesList = new ArrayList<>();

        for (int i = 0; i < metrics.size(); i++) {
            for (int j = i + 1; j < metrics.size(); j++) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();

                // Due to a bug in JavaFX there must be a point at the time the series is first rendered otherwise the
                // legend will not display correctly
                series.getData().add(new XYChart.Data<>(0.0, 0.0));

                series.setName(name);
                seriesList.add(series);
            }
        }

        for (int i = 0; i < charts.size(); i++) {
            charts.get(i).getData().add(seriesList.get(i));
        }

        seriesLists.add(seriesList);

        return seriesLists.size() - 1;
    }

    /**
     * Adds data to a series that has already been constructed in {@link custom_controls.GraphSeries#addSeries(String)}.
     *
     * @param index  The index of the series.
     * @param points The points to display in the series. The dimension of the points corresponds to the defined metrics.
     *               For example if 3 metrics have been defined using {@link custom_controls.GraphSeries#addMetrics(List)}
     *               then each point should be of length 3 as well.
     */
    public void updateSeries(int index, List<Double[]> points) {

        List<XYChart.Series<Number, Number>> seriesList = seriesLists.get(index);

        for (int i = 0; i < metrics.size(); i++) {
            for (int j = i + 1; j < metrics.size(); j++) {

                XYChart.Series<Number, Number> series = seriesList.get(i + j - 1);

                int diff = points.size() - series.getData().size();

                // Add missing points
                if (diff > 0) {
                    for (int k = 0; k < diff; k++) {
                        series.getData().add(new XYChart.Data<>());
                    }
                }

                // Remove extra points
                if (diff < 0) {

                    int start = series.getData().size() + diff;
                    int end = series.getData().size();

                    series.getData().remove(start, end);
                }

                for (int k = 0; k < points.size(); k++) {
                    Double[] point = points.get(k);

                    series.getData().get(k).setXValue(point[i]);
                    series.getData().get(k).setYValue(point[j]);
                }

            }
        }
    }
}
