import clustering.KMeansClustering;
import clustering.OutlierDetector;
import clustering.PlusPlusInitialisation;
import custom_controls.GraphSeries;
import custom_controls.ScientificScatterGraph;
import dataset.*;
import dataset.arff_parser.DataSetReader;
import error_metrics.LogarithmicStandardDeviationError;
import error_metrics.MeanMagnitudeRelativeError;
import error_metrics.PRED25Error;
import evolve_nn.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class Main extends Application {
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load file
        String sourcePath = "./cocomo-sdr.arff";

        File sourceFile = new File(sourcePath);

        DataSet ds = DataSetReader.open(sourceFile);

        // Remove outliers
        KMeansClustering clustering = new KMeansClustering(new PlusPlusInitialisation());
        OutlierDetector outlierDetector = new OutlierDetector(clustering);

        List<Project> outliers = outlierDetector.getOutliers(ds.getProjects());

        for (Project outlier : outliers) {
            ds.removeProject(outlier);
        }

        // Identify interesting features
        List<Feature> outputFeatures = ds.getFeatures().subList(3, 4);

        // Split into testing and training sets
        TrainingData td = new TrainingData(ds, outputFeatures);
        InputOutput[][] trainingSet = td.getSplitOfRatio(0.4);

        // Define the problem
        List errorMetrics = asList(
                new LogarithmicStandardDeviationError(),
                new MeanMagnitudeRelativeError(),
                new PRED25Error());

        SEEProblem problem = new SEEProblem(trainingSet[0], errorMetrics);
        RHaDMOEA rHaDMOEA = new RHaDMOEA(problem, 100, new MLPCrossOver(0.2, 300), new GaussianMutation(0.2));

        AlgorithmRunner<SEESolution> runner = new AlgorithmRunner<>(rHaDMOEA);

        // Display results as they are generated
        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        ParallelCoordinatePlot<SEESolution> pcp = new ParallelCoordinatePlot(
                new NumberAxis(0, 5, 1), new NumberAxis(0, 5, 1), new NumberAxis(0, 1, .2));

        // 3D graph
        ScientificScatterGraph<SEESolution> ssc = new ScientificScatterGraph<>();
        ssc.setMetrics(errorMetrics);

        // Graph series
        GraphSeries<SEESolution> gs = new GraphSeries<>();
        gs.setMetrics(errorMetrics);

        runner.addUpdateListener(ssc);
        runner.addUpdateListener(gs);
        runner.addUpdateListener(pcp);

//        root.getChildren().add(gs);
//        root.getChildren().add(ssc);
        root.getChildren().add(pcp);

        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            runner.runFor(100);

            // Set the aspiration point then run another 100 epochs
            rHaDMOEA.setAspirationPoint(new Double[]{0.2, 0.2, 0.2});
            runner.runFor(100);

            // Move the aspiration point and run a final 50 epochs
            rHaDMOEA.setAspirationPoint(new Double[]{0.5, 0.4, 0.4});

            PopulationInformation<SEESolution> finalPopulation = runner.runFor(50);

            System.out.println("Done");
        }).start();
    }
}
