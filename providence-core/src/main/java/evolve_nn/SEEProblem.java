package evolve_nn;

import dataset.InputOutput;
import error_metrics.ErrorMetric;
import neural_network.FeedForwardPerceptron;
import neural_network.INeuralNetwork;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;

import java.util.List;

/**
 * Represents the software effort estimation problem to solve. Assigns a fitness to solutions according to how well they
 * estimate values from a training set.
 * <p>
 * Created by Joseph Billingsley on 05/11/2015.
 */
public class SEEProblem extends AbstractGenericProblem<SEESolution> {

    private static final int NUMBER_OF_HIDDEN_NODES = 9;
    private static final int NUMBER_OF_HIDDEN_LAYERS = 1;
    private static final int NUMBER_OF_OUTPUTS = 1;

    private final List<ErrorMetric> errorMetrics;
    private final InputOutput[] trainingSet;

    /**
     * @param trainingSet  The training set to evaluate the performance of the solutions with.
     * @param errorMetrics The metrics to use when calculating how good an estimate is.
     */
    public SEEProblem(InputOutput[] trainingSet, List<ErrorMetric> errorMetrics) {
        this.trainingSet = trainingSet;
        this.errorMetrics = errorMetrics;

        setNumberOfObjectives(errorMetrics.size());
    }

    @Override
    public void evaluate(SEESolution solution) {
        INeuralNetwork network = solution.getNeuralNetwork();

        Double[] actuals = new Double[trainingSet.length];
        Double[] estimates = new Double[trainingSet.length];

        for (int i = 0; i < trainingSet.length; i++) {
            InputOutput inputOutput = trainingSet[i];

            actuals[i] = inputOutput.getOutputs()[0];
            estimates[i] = network.executeNetwork(inputOutput.getInputs())[0];
        }

        for (int i = 0; i < errorMetrics.size(); i++) {
            ErrorMetric errorMetric = errorMetrics.get(i);
            double error = errorMetric.error(actuals, estimates);

            if (errorMetric.isMinimising()) {
                solution.setObjective(i, error);
            } else {
                solution.setObjective(i, 1 - error);
            }
        }
    }

    @Override
    public SEESolution createSolution() {
        return new SEESolution(errorMetrics.size(),
                new FeedForwardPerceptron(
                        trainingSet[0].getInputs().length,
                        NUMBER_OF_HIDDEN_NODES,
                        NUMBER_OF_OUTPUTS,
                        NUMBER_OF_HIDDEN_LAYERS,
                        1.0)
        );
    }
}
