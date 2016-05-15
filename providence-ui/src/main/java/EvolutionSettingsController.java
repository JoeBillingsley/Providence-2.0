import base.Modal;
import custom_controls.DecimalTextField;
import custom_controls.IntegerTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.util.converter.NumberStringConverter;
import model_library.Model;
import search_parameters.SearchParameters;

/**
 * The controller for the Evolution Settings modal dialogue. Provides functionality that allows the evolutionary algorithm
 * to be tweaked.
 * <p>
 * Created by Joseph Billingsley on 30/11/2015.
 */
public class EvolutionSettingsController extends Modal {

    @FXML private IntegerTextField populationSize;
    @FXML private DecimalTextField probabilityOfCrossover;
    @FXML private DecimalTextField probabilityOfMutation;
    @FXML private IntegerTextField annealTime;

    @FXML private Button back;
    @FXML private Button next;

    private Model model;
    private SearchParameters searchParameters;

    protected void initialise() {
        getStage().setTitle("Set the settings for the evolutionary algorithm");

        next.setOnAction(event -> {

            String alertText = null;
            if (searchParameters.getPopulationSize() > 1000)
                alertText = "The 'Population size' field cannot exceed 1000";

            if (searchParameters.getProbabilityOfCrossover() > 1)
                alertText = "The Probability of crossover' field cannot exceed 1";

            if (searchParameters.getProbabilityOfMutation() > 1)
                alertText = "The 'Probability of mutation' field cannot exceed 1";

            if (populationSize.getText().isEmpty()
                    || probabilityOfCrossover.getText().isEmpty()
                    || probabilityOfMutation.getText().isEmpty()
                    || annealTime.getText().isEmpty())
                alertText = "There can not be any empty fields";

            if (alertText == null) {
                model.setLastRunSettings(searchParameters);
                loadScene("ErrorMetricsSelection");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid value!");
                alert.setHeaderText(null);
                alert.setContentText(alertText);
                alert.showAndWait();
            }

        });
        back.setOnAction(event -> loadScene("InputOutputSelection"));
    }

    @Override
    protected void whenContextAdded() {
        model = (Model) getContext().get(Model.TAG);
        searchParameters = (SearchParameters) getContext().get(SearchParameters.TAG);

        populationSize.textProperty().bindBidirectional(searchParameters.populationSizeProperty(), new NumberStringConverter());
        probabilityOfCrossover.textProperty().bindBidirectional(searchParameters.probabilityOfCrossoverProperty(), new NumberStringConverter());
        probabilityOfMutation.textProperty().bindBidirectional(searchParameters.probabilityOfMutationProperty(), new NumberStringConverter());
        annealTime.textProperty().bindBidirectional(searchParameters.annealTimeProperty(), new NumberStringConverter());
    }
}