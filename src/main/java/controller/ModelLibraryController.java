package controller;

import controller.base.Context;
import controller.base.Controller;
import controller.custom_controls.ExceptionAlert;
import controller.custom_controls.ModelCell;
import controller.custom_controls.TestSetCell;
import dataset.DataSet;
import dataset.InputOutput;
import dataset.TrainingData;
import ensemble.IEnsemble;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import model_library.*;
import search_parameters.SearchParameters;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the Model Library screen. Provides functionality for adding, removing and training models and test
 * data.
 * <p>
 * Created by Joseph Billingsley on 17/12/2015.
 */
public class ModelLibraryController extends Controller {

    // Models pane
    @FXML private ListView<Model> modelsList;
    @FXML private Button addModel;
    @FXML private Button removeModel;

    // Test pane
    @FXML private ListView<TestSet> testList;
    @FXML private Button addTest;
    @FXML private Button removeTest;

    // Main data pane
    @FXML private TableView<Double[]> projectsTable;

    @FXML private Button train;
    @FXML private Button test;
    @FXML private Button query;

    private Model selectedModel;
    private TestSet selectedTest;

    protected void initialise() throws Exception {
        ModelRepository modelsRepo = new ModelRepository();

        ChangeListener<Throwable> loaderException = (observable, oldValue, newValue) -> {
            // If the same exception is triggered twice in a row, new value is not set
            // until the second time this property has changed.
            if (newValue == null) return;

            ExceptionAlert alert = new ExceptionAlert((Exception) newValue);
            alert.setTitle("Unable to parse file");
            alert.setContentText("There is an issue in the file you selected and it could not be added to the library.");
            alert.showAndWait();
        };

        // region Model
        ModelLoadingService modelLoader = new ModelLoadingService();
        modelLoader.setOnSucceeded(event -> {
            Model model = modelLoader.getValue();
            modelsRepo.add(model);
            modelsList.getSelectionModel().select(model);
        });

        modelLoader.exceptionProperty().addListener(loaderException);

        modelsList.setItems(modelsRepo.getReadOnlyModels());
        modelsList.setCellFactory((view) -> new ModelCell());

        modelsList.getSelectionModel().selectedItemProperty().addListener((view, oldValue, newValue) -> {
            test.setDisable(true);
            query.setDisable(true);
            removeModel.setDisable(true);
            addTest.setDisable(true);
            removeTest.setDisable(true);
            train.setDisable(true);

            //noinspection Duplicates
            if (newValue == null) {
                selectedModel = null;
                selectedTest = null;

                testList.getItems().clear();
                return;
            }

            selectedModel = newValue;

            train.setDisable(false);
            removeModel.setDisable(false);

            testList.setItems(selectedModel.getTestSets());

            setDataSet(newValue.getDataSet());

            if(selectedModel.getEnsemble() != null) {
                query.setDisable(false);
                addTest.setDisable(false);
            }

            selectedModel.getEnsembleProperty().addListener((o, oldEnsemble, newEnsemble) -> {
                if (newEnsemble != null) {
                    query.setDisable(false);
                    addTest.setDisable(false);
                }

                if (testList.getItems() != null) {
                    for (TestSet testSet : testList.getItems()) {
                        testSet.setResults(null);
                    }

                    testList.refresh();
                }
            });
        });

        addModel.setOnAction(event -> runLoader(modelLoader));
        removeModel.setOnAction(event -> {
            projectsTable.getColumns().clear();
            modelsRepo.remove(modelsList.getSelectionModel().getSelectedIndex());
        });
        // endregion

        // region Test
        TestLoadingService testLoader = new TestLoadingService();
        testLoader.setOnSucceeded(event -> {
            TestSet testSet = testLoader.getValue();

            if (testSet.getDataSet().getFeatures().size() != selectedModel.getDataSet().getFeatures().size()) {
                ExceptionAlert alert = new ExceptionAlert(new RuntimeException());
                alert.setTitle("File structure mismatch");
                alert.setContentText("The provided test file does not have the same structure as the model");
                alert.showAndWait();

                return;
            }

            selectedModel.addTestSet(testSet);
            testList.getSelectionModel().select(testSet);
        });

        testLoader.exceptionProperty().addListener(loaderException);

        testList.setCellFactory((view) -> new TestSetCell());

        testList.getSelectionModel().selectedItemProperty().addListener((view, oldValue, newValue) -> {

            test.setDisable(true);
            removeTest.setDisable(true);

            // noinspection Duplicates
            if (newValue == null) {
                selectedTest = null;
                return;
            }

            selectedTest = newValue;

            test.setDisable(false);
            removeTest.setDisable(false);
        });

        addTest.setOnAction(event -> runLoader(testLoader));
        removeTest.setOnAction(event -> {
            if (selectedModel != null)
                selectedModel.getTestSets().remove(testList.getSelectionModel().getSelectedIndex());
        });
        // endregion

        train.setOnAction((event) -> {
            if (selectedModel == null)
                return;

            Context context = new Context();
            context.add(Model.TAG, selectedModel);

            if (selectedModel.getLastRunSettings() != null)
                context.add(SearchParameters.TAG, selectedModel.getLastRunSettings());

            setContext(context);

            loadScene("InputOutputSelection", true);
        });

        test.setOnAction((event) -> {
            if (selectedModel == null || selectedTest == null)
                return;

            final IEnsemble ensemble = selectedModel.getEnsemble();

            SearchParameters lastRunSettings = selectedModel.getLastRunSettings();

            DataSet testSet = selectedTest.getDataSet();

            TrainingData trainingData = new TrainingData(testSet, lastRunSettings.outputColumnsProperty());

            InputOutput[] inputOutputs = trainingData.getAll();

            Double[] estimates = new Double[inputOutputs.length];
            Double[] actuals = new Double[inputOutputs.length];

            for (int i = 0; i < inputOutputs.length; i++) {
                InputOutput inputOutput = inputOutputs[i];

                estimates[i] = ensemble.getResult(inputOutput.getInputs());
                actuals[i] = inputOutput.getOutputs()[0];
            }

            List<Double> results = lastRunSettings.getErrorMetrics()
                    .stream()
                    .map(errorMetric -> errorMetric.error(actuals, estimates))
                    .collect(Collectors.toList());

            selectedTest.setMetrics(lastRunSettings.getErrorMetrics());
            selectedTest.setResults(results);

            testList.refresh();
        });

        query.setOnAction(event -> {
            if (selectedModel == null)
                return;

            loadScene("QueryInput", true);
        });
    }

    private void setDataSet(DataSet dataSet) {
        projectsTable.getColumns().clear();

        for (int i = 0; i < dataSet.getFeatures().size(); i++) {
            TableColumn<Double[], Double> column = new TableColumn<>();

            final int colNo = i;
            column.setText(dataSet.getFeatures().get(i).getName());
            column.setCellValueFactory(
                    cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()[colNo]));

            projectsTable.getColumns().add(column);
        }

        ObservableList<Double[]> projects = FXCollections.observableArrayList(dataSet.getProjects());
        projectsTable.setItems(projects);
    }

    private void runLoader(FileLoader loader) {
        final File file = new FileChooser().showOpenDialog(getStage());

        if (file == null)
            return;

        loader.reset();
        loader.setFile(file);
        loader.start();
    }

    @Override
    protected void whenContextAdded() {
    }
}
