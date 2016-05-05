package model_library;

import dataset.DataSet;
import error_metrics.ErrorMetric;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Model of a test set. Contains information on the test set and the results of running it through the system.
 * <p>
 * Created by Joseph Billingsley on 16/03/2016.
 */
public class TestSet {

    private List<Double> results;
    private String title;

    private DataSet testSet;
    private ObservableList<ErrorMetric> metrics;

    /**
     * @param title   The name of the test set.
     * @param testSet The source data set.
     */
    public TestSet(String title, DataSet testSet) {
        this.title = title;
        this.testSet = testSet;
    }

    // region Getters and setters
    public DataSet getDataSet() {
        return testSet;
    }

    public String getTitle() {
        return title;
    }

    public List<Double> getResults() {
        return results;
    }

    public void setResults(List<Double> results) {
        this.results = results;
    }

    public ObservableList<ErrorMetric> getMetrics() {
        return metrics;
    }

    public void setMetrics(ObservableList<ErrorMetric> metrics) {
        this.metrics = metrics;
    }
    // endregion
}
