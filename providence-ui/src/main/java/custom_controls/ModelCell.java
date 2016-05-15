package custom_controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model_library.Model;

import java.io.IOException;

/**
 * A cell containing information on a particular model. For use in a ListView.
 * <p>
 * Created by Joseph Billingsley on 18/01/2016.
 */
public class ModelCell extends ListCell<Model> {

    @FXML Text title;
    @FXML Text outliers;
    @FXML Text features;
    @FXML Text projects;

    @FXML Pane container;

    @Override
    protected void updateItem(Model item, boolean empty) {
        super.updateItem(item, empty);

        // region Argument checks
        if (empty || item == null) {

            setText(null);
            setGraphic(null);

            return;
        }
        // endregion

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/custom_controls/ModelCell.fxml"));
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        title.setText(item.getTitle());
        outliers.setText(String.format("%d outliers", item.getNumberOfOutliers()));
        features.setText(String.format("%d features", item.getDataSet().getFeatures().size()));
        projects.setText(String.format("%d projects", item.getDataSet().getProjects().size()));

        setGraphic(container);
    }
}
