import base.Modal;
import custom_controls.SensibleSpinner;
import error_metrics.ErrorMetric;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import search_parameters.SearchParameters;

/**
 * The controller for the Set Aspiration Point modal dialogue. Provides functionality for specifying the aspiration point.
 * <p>
 * Created by Joseph Billingsley on 07/03/2016.
 */
public class SetAspirationPointController extends Modal<Double[]> {

    @FXML private Text metricOne;
    @FXML private SensibleSpinner<Double> spinnerOne;

    @FXML private Text metricTwo;
    @FXML private SensibleSpinner<Double> spinnerTwo;

    @FXML private Text metricThree;
    @FXML private SensibleSpinner<Double> spinnerThree;

    @FXML private Button done;
    @FXML private Button cancel;

    @Override
    protected void initialise() throws Exception {

        getStage().setTitle("Choose the aspiration point");

        cancel.setOnAction(event -> getStage().close());

        done.setOnAction(event -> {
                    setResult(new Double[]{
                            spinnerOne.getValue(),
                            spinnerTwo.getValue(),
                            spinnerThree.getValue()});

                    finish();
                }
        );
    }

    @Override
    protected void whenContextAdded() {
        SearchParameters searchParameters = (SearchParameters) getContext().get(SearchParameters.TAG);

        ObservableList<ErrorMetric> errorMetrics = searchParameters.getErrorMetrics();
        metricOne.setText(errorMetrics.get(0).getName());
        metricTwo.setText(errorMetrics.get(1).getName());
        metricThree.setText(errorMetrics.get(2).getName());
    }

    /**
     * Sets the initial values of the error metric corresponding to the provided id.
     *
     * @param errorMetricId The id of the error metric.
     * @param initialValue  The initial value, usually the same same as the lower bound.
     * @param lowerBound    The lower bound of the error metric.
     * @param upperBound    The upper bound of the error metric.
     */
    public void setValues(int errorMetricId, Double initialValue, Double lowerBound, Double upperBound) {
        Spinner<Double> spinner;

        switch (errorMetricId) {
            case 0:
                spinner = spinnerOne;
                break;
            case 1:
                spinner = spinnerTwo;
                break;
            case 2:
                spinner = spinnerThree;
                break;
            default:
                throw new IllegalArgumentException(
                        "Spinner not found. The error metric ID must be between 0 and 2: " + errorMetricId);
        }

        spinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(lowerBound, upperBound, initialValue, 0.1));
    }
}
