package controller.custom_controls;

import error_metrics.ErrorMetric;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model_library.TestSet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * A cell containing information on a particular test set. For use in a ListView.
 * <p>
 * Created by Joseph Billingsley on 16/03/2016.
 */
public class TestSetCell extends ListCell<TestSet> {

    @FXML private Text title;
    @FXML private Text errorMetricOne, errorMetricTwo, errorMetricThr;
    @FXML private Pane container;

    @Override
    protected void updateItem(TestSet item, boolean empty) {
        super.updateItem(item, empty);

        // region Argument checks
        if (empty || item == null) {

            setText(null);
            setGraphic(null);

            return;
        }
        // endregion

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/custom_controls/TestSetCell.fxml"));
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        title.setText(item.getTitle());

        List<ErrorMetric> errorMetrics = item.getMetrics();
        List<Double> results = item.getResults();

        if (results != null && errorMetrics != null) {
            errorMetricOne.setText(errorMetrics.get(0).getName() + " : " + trimResult(results.get(0), 10));
            errorMetricTwo.setText(errorMetrics.get(1).getName() + " : " + trimResult(results.get(1), 10));
            errorMetricThr.setText(errorMetrics.get(2).getName() + " : " + trimResult(results.get(2), 10));
        } else {
            errorMetricOne.setWrappingWidth(150);
            errorMetricOne.setText("The performance of the model has not yet been evaluated on this data set");
        }

        setGraphic(container);
    }

    @NotNull
    private String trimResult(Double result, int targetLength) {
        String s = result.toString();
        return s.substring(0, Math.min(s.length(), targetLength));
    }
}
