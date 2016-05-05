package controller;

import controller.base.Modal;
import controller.custom_controls.ListToList;
import error_metrics.ErrorMetric;
import error_metrics.LogarithmicStandardDeviationError;
import error_metrics.MeanMagnitudeRelativeError;
import error_metrics.PRED25Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model_library.Model;
import search_parameters.SearchParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * The controller for the Error Metrics selection modal dialogue. Provides functionality for choosing error metrics.
 * <p>
 * Created by Joseph Billingsley on 24/02/2016.
 */
public class ErrorMetricsSelectionController extends Modal {

    @FXML private ListToList<ErrorMetric> errorMetrics;

    @FXML private Button back;
    @FXML private Button next;

    private Model model;
    private SearchParameters searchParameters;

    @Override
    protected void initialise() {
        getStage().setTitle("Choose three error metrics to evaluate");

        next.setOnAction(event -> {
            model.setLastRunSettings(searchParameters);
            loadScene("CalculateEstimate");
        });
        back.setOnAction(event -> loadScene("EvolutionSettings"));
    }

    @Override
    protected void whenContextAdded() {
        model = (Model) getContext().get(Model.TAG);
        searchParameters = (SearchParameters) getContext().get(SearchParameters.TAG);

        ObservableList<ErrorMetric> modelMetrics = searchParameters.getErrorMetrics();
        ObservableList<ErrorMetric> loadedMetrics = FXCollections.observableArrayList();

        loadedMetrics.addAll(loadErrorMetrics());
        for (ErrorMetric modelMetric : modelMetrics) {
            for (int i = 0; i < loadedMetrics.size(); i++) {
                if(modelMetric.getName().equals(loadedMetrics.get(i).getName()))
                    loadedMetrics.remove(i);
            }
        }

        errorMetrics.addChangeListener((listOneItems, listTwoItems) -> {
            next.setDisable(true);
            if (listTwoItems.size() == 3) next.setDisable(false);
        });

        errorMetrics.setListOneValues(loadedMetrics);
        errorMetrics.setListTwoValues(modelMetrics);
    }

    private List<ErrorMetric> loadErrorMetrics() {
        List<ErrorMetric> errorMetrics = new ArrayList<>();

        errorMetrics.add(new PRED25Error());
        errorMetrics.add(new MeanMagnitudeRelativeError());
        errorMetrics.add(new LogarithmicStandardDeviationError());

        return errorMetrics;
    }
}
