import base.Context;
import base.Controller;
import custom_controls.GraphSeries;
import custom_controls.IAxis;
import custom_controls.ScientificScatterGraph;
import dataset.TrainingData;
import ensemble.ParetoEnsemble;
import error_metrics.ErrorMetric;
import evolve_nn.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import model_library.Model;
import search_parameters.SearchParameters;
import writers.SolutionFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The controller for the Calculate Estimate screen. Provides functionality for interactive discovery of optimal points.
 * <p>
 * Created by Joseph Billingsley on 22/12/2015.
 */
public class CalculateEstimateController extends Controller {

    @FXML
    private Button run;
    @FXML
    private Button pause;

    @FXML
    private Button addPreferencePoint;
    @FXML
    private Button save;
    @FXML
    private Button buildEnsemble;

    @FXML
    private ScientificScatterGraph graph;
    @FXML
    private GraphSeries graphSeries;

    @FXML
    private Text generationCount;

    private int generation;

    private Model model;

    private List<SEESolution> dominatedSolutions;
    private List<SEESolution> nonDominatedSolutions;

    private Double[] aspirationPoint;
    private int aspirationPointIndex = -1;

    @Override
    protected void initialise() throws Exception {
    }

    @Override
    protected void whenContextAdded() {
        Context context = getContext();

        SearchParameters searchParameters = (SearchParameters) context.get(SearchParameters.TAG);
        model = (Model) context.get(Model.TAG);

        TrainingData trainingData = new TrainingData(model.getDataSet(), searchParameters.outputColumnsProperty());

        List<ErrorMetric> errorMetrics = searchParameters.getErrorMetrics();
        SEEProblem problem = new SEEProblem(trainingData.getAll(), errorMetrics);

        RHaDMOEA algorithm =
                new RHaDMOEA(
                        problem,
                        searchParameters.getPopulationSize(),
                        new MLPCrossOver(searchParameters.getProbabilityOfCrossover(),
                                searchParameters.getAnnealTime()),
                        new GaussianMutation(searchParameters.getProbabilityOfMutation()),
                        .2);

        AlgorithmRunner<SEESolution> runner = new AlgorithmRunner<>(algorithm);

        List<IAxis> axis = errorMetrics.stream().collect(Collectors.toList());
        graph.setMetrics(axis);
        graphSeries.setMetrics(axis);

        int nondominatedIdx = graphSeries.addSeries("Nondominated");
        int dominatedIdx = graphSeries.addSeries("Dominated");

//        runner.addUpdateListener(solutions -> Platform.runLater(() -> {
//
//            Ranking<SEESolution> fronts = new DominanceRanking<>();
//            fronts = fronts.computeRanking(solutions);
//
//            nonDominatedSolutions = fronts.getSubfront(0);
//
//            dominatedSolutions = new ArrayList<>();
//            for (int i = 1; i < fronts.getNumberOfSubfronts(); i++) {
//                dominatedSolutions.addAll(fronts.getSubfront(i));
//            }
//
//            List<Double[]> nonDominatedPoints =
//                    nonDominatedSolutions.stream()
//                            .map(SEESolution::getObjectives)
//                            .collect(Collectors.toList());
//
//            List<Double[]> dominatedPoints =
//                    dominatedSolutions.stream()
//                            .map(SEESolution::getObjectives)
//                            .collect(Collectors.toList());
//
//            generation++;
//            generationCount.setText("Running for " + generation + " generations");
//
//            // 3d graph
//            graph.clear();
//            graph.addAll(nonDominatedPoints, Color.RED);
//            graph.addAll(dominatedPoints, Color.ORANGE);
//
//            if (aspirationPoint != null)
//                aspirationPointIndex = graph.add(aspirationPoint, Color.GREEN);
//
//            graphSeries.updateSeries(nondominatedIdx, nonDominatedPoints);
//            graphSeries.updateSeries(dominatedIdx, dominatedPoints);
//        }));

//        runner.start(AlgorithmRunner.RUN_FOREVER, false, true);
//
//        run.setOnAction(event -> {
//            run.setDisable(true);
//            pause.setDisable(false);
//            buildEnsemble.setDisable(false);
//            save.setDisable(false);
//
//            runner.continueRun();
//        });
//
//        pause.setOnAction(event -> {
//            run.setDisable(false);
//            pause.setDisable(true);
//
//            runner.pause();
//        });
//
//        addPreferencePoint.setOnAction(event -> {
//            // Pause
//            run.setDisable(false);
//            pause.setDisable(true);
//            runner.pause();
//
//            // Get aspiration point
//            SetAspirationPointController aspirationPointController =
//                    (SetAspirationPointController) loadSceneForResult("SetAspirationPoint", result -> {
//                        this.aspirationPoint = (Double[]) result;
//
//                        if (aspirationPointIndex > -1)
//                            graph.removePoint(aspirationPointIndex);
//
//                        aspirationPointIndex = graph.add(aspirationPoint, Color.GREEN);
//                        algorithm.setAspirationPoint(aspirationPoint);
//                    });
//
//            for (int i = 0; i < errorMetrics.size(); i++) {
//                ErrorMetric errorMetric = errorMetrics.get(i);
//
//                double initialValue = 0;
//                if (aspirationPoint != null)
//                    initialValue = aspirationPoint[i];
//
//                aspirationPointController.setValues(i, initialValue, errorMetric.getLowerBound(), errorMetric.getUpperBound());
//            }
//        });

        save.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("txt", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );

            File file = fileChooser.showSaveDialog(getStage());

            if (file == null)
                return;

            SolutionFileWriter solutionFileWriter = new SolutionFileWriter();

            try {
                solutionFileWriter.open(file);
                solutionFileWriter.write(errorMetrics);
                solutionFileWriter.write("Non dominated solutions", nonDominatedSolutions);
                solutionFileWriter.write("Dominated solutions", dominatedSolutions);
                solutionFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        buildEnsemble.setOnAction(event -> {
            ParetoEnsemble ensemble = new ParetoEnsemble(nonDominatedSolutions);

            if (model.getEnsemble() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initStyle(StageStyle.UTILITY);

                alert.setTitle("Overwrite ensemble?");
                alert.setContentText("An ensemble has already been created for this model. Are you sure you want to overwrite it?");

                alert.setHeaderText(null);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK)
                    model.setEnsemble(ensemble);

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);

                model.setEnsemble(ensemble);

                alert.setTitle("Ensemble created");
                alert.setContentText("The ensemble has been created succesfully!");

                alert.setHeaderText(null);

                alert.showAndWait();
            }
        });
    }
}
