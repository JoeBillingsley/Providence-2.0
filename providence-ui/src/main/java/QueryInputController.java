import base.Modal;
import custom_controls.DecimalTextField;
import dataset.Feature;
import dataset.arff_parser.Attribute;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import model_library.Model;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.function.Function;

/**
 * The controller for the Query Input modal dialogue. Allows data to be entered in the same structure of the original
 * data.
 * <p>
 * Created by Joseph Billingsley on 18/03/2016.
 */
public class QueryInputController extends Modal {

    @FXML private VBox contentPane;
    @FXML private Button finish;

    private List<Feature> features;
    private String[] formValues;

    private Model model;

    @Override
    protected void initialise() throws Exception {
        getStage().setTitle("Provide values to produce an estimate");

        finish.setOnAction(event -> {
            int size = features.size();
            Double[] values = new Double[size];

            for (int i = 0; i < size; i++) {
                Function<String, Double> eval = features.get(i).getAttribute().getEval();
                values[i] = eval.apply(formValues[i]);
            }

            Double estimate = model.getEnsemble().getResult(values);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Produced estimate");
            alert.initStyle(StageStyle.UTILITY);
            alert.setHeaderText(null);
            alert.setContentText(String.format("Estimate: %1$.4f time units", estimate));

            alert.initOwner(getStage());

            alert.showAndWait();
        });
    }

    @Override
    protected void whenContextAdded() {
        model = (Model) getContext().get(Model.TAG);

        features = model.getLastRunSettings().inputColumnsProperty();
        formValues = new String[features.size()];

        int i = 0;
        for (Feature feature : features) {
            final int index = i;

            Attribute attribute = feature.getAttribute();

            VBox group = new VBox();
            group.setSpacing(4);

            Text text = new Text(attribute.getName());
            group.getChildren().add(text);

            List<String> allowedValues = attribute.getAllowedValues();

            if (allowedValues != null) {
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.setPrefWidth(contentPane.getPrefWidth());

                comboBox.getItems().addAll(allowedValues);

                comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == null) return;

                    formValues[index] = newValue;
                    finish.setDisable(!checkFields(formValues));
                });

                group.getChildren().add(comboBox);
            } else {
                // If not eval then double
                DecimalTextField textField = new DecimalTextField();
                textField.textProperty().addListener((value) -> {
                    formValues[index] = textField.getText();
                    finish.setDisable(!checkFields(formValues));
                });

                group.getChildren().add(textField);
            }

            contentPane.getChildren().add(group);

            i++;
        }
    }

    @Contract(pure = true)
    private boolean checkFields(String[] fields) {
        for (String field : fields) {
            if(field == null || field.isEmpty())
                return false;
        }

        return true;

    }
}
