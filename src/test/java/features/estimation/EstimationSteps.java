package features.estimation;

import clustering.OutlierDetector;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dataset.DataSet;
import dataset.Feature;
import dataset.InputOutput;
import dataset.TrainingData;
import dataset.arff_parser.DataSetReader;
import ensemble.ParetoEnsemble;
import error_metrics.ErrorMetric;
import error_metrics.LogarithmicStandardDeviationError;
import error_metrics.MeanMagnitudeRelativeError;
import error_metrics.PRED25Error;
import evolve_nn.*;
import utils.Variance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Joseph Billingsley on 26/10/2015.
 */
public class EstimationSteps {

    // NOTE: Removed rubbish from files e.g. '?'s is desharnis

    private double crossoverProbability, mutationProbability;
    private int generations, annealTime;
    private int populationSize;

    private DataSet ds;

    private ErrorMetric objOne = new LogarithmicStandardDeviationError();
    private ErrorMetric objTwo = new MeanMagnitudeRelativeError();
    private ErrorMetric objThr = new PRED25Error();

    private List<Double> objOneVals = new ArrayList<>();
    private List<Double> objTwoVals = new ArrayList<>();
    private List<Double> objThrVals = new ArrayList<>();

    private Feature effortFeature;

    @Given("a syntactically valid .arff file is at '([^\']*)'$")
    public void produceTrainingSet(String filePath) throws IOException {
        File file = new File(filePath);

        DataSetReader dsr = new DataSetReader(file);
        ds = dsr.open();
    }

    @And("^the actual effort is contained in the feature '(.*)'$")
    public void findOutputFeature(String outputFeature) {

        List<Feature> features = ds.getFeatures();

        for (int i = 0; i < features.size(); i++) {
            Feature feature = features.get(i);

            if (feature.getName().equals(outputFeature))
                effortFeature = feature;
        }
    }

    @And("^the probability of a crossover step occurring is (\\d+\\.\\d+), the probability of a mutation step occurring is (\\d+\\.\\d+)$")
    public void setMutationParameters(double crossoverProbability, double mutationProbability) {
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
    }

    @And("^the evolution is run for (\\d+) generations and the population have an anneal time of (\\d+) generations")
    public void setEvolutionLimits(int generations, int annealTime) {
        this.generations = generations;
        this.annealTime = annealTime;
    }

    @And("^the population size is (\\d+)$")
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    @When("^the outliers are removed$")
    public void theOutliersAreRemoved() {
        OutlierDetector od = new OutlierDetector();

        List<Double[]> projects = ds.getProjects();
        List<Double[]> outliers = od.getOutliers(projects);

        projects.removeAll(outliers);
    }

    @And("^the model is trained using (\\d+)% of the data, and tested using Monte Carlo cross validation with (\\d+) splits$")
    public void evolveModel(int percentageOfTrainingData, int splits) {

        ArrayList<Feature> outputFeatures = new ArrayList<>();
        outputFeatures.add(effortFeature);

        TrainingData shuffler = new TrainingData(ds, outputFeatures);

        for (int i = 0; i < splits; i++) {
            InputOutput[][] split = shuffler.getSplitOfRatio(percentageOfTrainingData / 100.0);

            InputOutput[] trainingSet = split[0];
            InputOutput[] testingSet = split[1];

            List<ErrorMetric> errorMetrics = new ArrayList<>();
            errorMetrics.add(objOne);
            errorMetrics.add(objTwo);
            errorMetrics.add(objThr);

            SEEProblem problem = new SEEProblem(trainingSet, errorMetrics);

            RHaDMOEA algorithm =
                    new RHaDMOEA(
                            problem,
                            populationSize,
                            new MLPCrossOver(crossoverProbability, annealTime),
                            new GaussianMutation(mutationProbability));

            AlgorithmRunner<SEESolution> runner = new AlgorithmRunner<>(algorithm);

            runner.addFinishedListener(results -> {
                Double[] actuals = new Double[testingSet.length];
                Double[] estimates = new Double[testingSet.length];

                for (int j = 0; j < testingSet.length; j++) {
                    InputOutput inputOutputPairs = testingSet[j];
                    actuals[j] = inputOutputPairs.getOutputs()[0];

                    ParetoEnsemble paretoEnsemble = new ParetoEnsemble(results);
                    estimates[j] = paretoEnsemble.getResult(inputOutputPairs.getInputs());
                }

                objOneVals.add(objOne.error(actuals, estimates));
                objTwoVals.add(objTwo.error(actuals, estimates));
                objThrVals.add(objThr.error(actuals, estimates));
            });

            runner.start(generations, true, false);
        }

        // Print and assert averages separately as otherwise we will not always see the results
        Double[] objOneArr = new Double[objOneVals.size()];
        Double[] objTwoArr = new Double[objTwoVals.size()];
        Double[] objThrArr = new Double[objThrVals.size()];

        objOneVals.toArray(objOneArr);
        objTwoVals.toArray(objTwoArr);
        objThrVals.toArray(objThrArr);

        System.out.println("LSD: " + calcAverage(objOneVals) + " +- " + Variance.calculateStandardDeviation(objOneArr));
        System.out.println("MMRE: " + calcAverage(objTwoVals) + " +- " + Variance.calculateStandardDeviation(objTwoArr));
        System.out.println("PRED25: " + calcAverage(objThrVals) + " +- " + Variance.calculateStandardDeviation(objThrArr));
    }

    @Then("^the mean average LSD is less than (\\d+\\.\\d+)$")
    public void theAverageLSDIsLessThan(double upperBound) {
        double average = calcAverage(objOneVals);
        assertTrue(average < upperBound);
    }

    @And("^the mean average MMRE is less than (\\d+\\.\\d+)$")
    public void theAverageMMREIsLessThan(double upperBound) {
        double average = calcAverage(objTwoVals);
        assertTrue(average < upperBound);
    }

    @And("^the mean average PRED25 is greater than (\\d+\\.\\d+)$")
    public void theAveragePREDIsLessThan(double lowerBound) {
        double average = calcAverage(objThrVals);
        assertTrue(average > lowerBound);
    }

    private double calcAverage(List<Double> values) {
        double sum = 0;

        for (Double value : values) {
            sum += value;
        }

        return sum / values.size();
    }
}
