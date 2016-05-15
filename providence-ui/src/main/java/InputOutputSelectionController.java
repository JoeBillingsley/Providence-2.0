import base.Context;
import base.Modal;
import custom_controls.ListToList;
import dataset.Feature;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model_library.Model;
import search_parameters.SearchParameters;
import search_parameters.SearchParametersFactory;

import java.util.List;

/**
 * The controller for the Input Output Selection modal dialogue. Provides functionality for choosing which columns to
 * include in the evaluation of the training data.
 * <p>
 * Created by Joseph Billingsley on 10/01/2016.
 */
public class InputOutputSelectionController extends Modal {

    @FXML private ListToList<Feature> inputToOutput;

    @FXML private Button next;

    private Model model;
    private SearchParameters searchParameters;

    @Override
    protected void initialise() throws Exception {
        getStage().setTitle("Choose the output column to estimate");

        next.setOnAction((event) -> {
            model.setLastRunSettings(searchParameters);
            loadScene("EvolutionSettings");
        });
    }

    @Override
    protected void whenContextAdded() {
        Context context = getContext();

        model = (Model) context.get(Model.TAG);

        if (context.has(SearchParameters.TAG)) {
            searchParameters = ((SearchParameters) context.get(SearchParameters.TAG)).copy();
        } else {
            List<Feature> features = model.getDataSet().getFeatures();
            searchParameters = SearchParametersFactory.getDefault();
            searchParameters.inputColumnsProperty().addAll(features);

            model.setLastRunSettings(searchParameters);
        }

        context.forceAdd(SearchParameters.TAG, searchParameters);

        inputToOutput.addChangeListener((inputs, outputs) -> {
            if (outputs.size() == 1)
                next.setDisable(false);
            else
                next.setDisable(true);
        });

        inputToOutput.setListOneValues(searchParameters.inputColumnsProperty());
        inputToOutput.setListTwoValues(searchParameters.outputColumnsProperty());
    }
}
