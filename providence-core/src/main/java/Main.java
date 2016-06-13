import clustering.KMeansClustering;
import clustering.OutlierDetector;
import clustering.PlusPlusInitialisation;
import dataset.*;
import dataset.arff_parser.DataSetReader;
import error_metrics.ErrorMetric;
import error_metrics.LogarithmicStandardDeviationError;
import error_metrics.MeanMagnitudeRelativeError;
import error_metrics.PRED25Error;
import evolve_nn.*;
import org.uma.jmetal.util.point.util.DominanceDistance;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

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
        List<ErrorMetric> errorMetrics = new ArrayList<>();
        errorMetrics.add(new LogarithmicStandardDeviationError());
        errorMetrics.add(new MeanMagnitudeRelativeError());
        errorMetrics.add(new PRED25Error());

        SEEProblem problem = new SEEProblem(trainingSet[0], errorMetrics);
        RHaDMOEA rHaDMOEA = new RHaDMOEA(problem, 100, new MLPCrossOver(0.2, 300), new GaussianMutation(0.2));

        // Run the algorithm for 100 epochs
        AlgorithmRunner<SEESolution> runner = new AlgorithmRunner<>(rHaDMOEA);
        runner.runFor(100);

        // Set the aspiration point then run another 100 epochs
        rHaDMOEA.setAspirationPoint(new Double[]{0.2, 0.2, 0.2});
        runner.runFor(100);

        // Move the aspiration point and run a final 50 epochs
        rHaDMOEA.setAspirationPoint(new Double[]{0.5, 0.4, 0.3});

        List<SEESolution> finalPopulation = runner.runFor(50);
        List nonDominatedSolutions = new DominanceRanking<SEESolution>().computeRanking(finalPopulation).getSubfront(0);

        System.out.println("Done");

        // Display results as they are generated
        // TODO
    }
}
