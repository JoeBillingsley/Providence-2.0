package evolve_nn;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import utils.GaussianRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * A cross over operator for problems represented as multi layer perceptrons which takes three random parents from the population
 * and produces one child.
 * <p>
 * The relative weighting of each parent changes according to the generations to allow the operator to become increasingly
 * exploitative over time.
 * <p>
 * Created by Joseph Billingsley on 05/11/2015.
 */
public class MLPCrossOver implements CrossoverOperator<SEESolution> {

    private final int annealTime;
    private final JMetalRandom random;
    private double crossoverProbability;
    private int generation = 0;

    /**
     * @param crossoverProbability The likelihood to modify a weight in the network.
     * @param annealTime           The number of generations after which the operator will begin to start exploiting
     *                             existing solutions rather than exploring new parts of the solution space.
     */
    public MLPCrossOver(double crossoverProbability, int annealTime) {
        this.crossoverProbability = crossoverProbability;
        this.annealTime = annealTime;
        this.random = JMetalRandom.getInstance();

        if (crossoverProbability < 0 || crossoverProbability > 1)
            throw new IllegalArgumentException("The crossover probability must be between 0 and 1. " + crossoverProbability);
    }

    @Override
    public List<SEESolution> execute(List<SEESolution> parentSolutions) {
        // region Argument checks
        if (parentSolutions.size() != 3)
            throw new IllegalArgumentException("The population must have three solutions");
        // endregion

        SEESolution
                parentOne = parentSolutions.get(0),
                parentTwo = parentSolutions.get(1),
                parentThr = parentSolutions.get(2);

        SEESolution child = (SEESolution) parentOne.copy();

        double slope = slope(annealTime, generation);

        int numberOfVariables = parentOne.getNumberOfVariables();
        for (int i = 0; i < numberOfVariables; i++) {
            if (random.nextDouble(0, 1) > crossoverProbability)
                continue;

            Double value = parentOne.getVariableValue(i) + slope * (parentTwo.getVariableValue(i) - parentThr.getVariableValue(i));
            child.setVariableValue(i, value);
        }

        ArrayList<SEESolution> result = new ArrayList<>();
        result.add(child);

        return result;
    }

    /**
     * Informs the cross over operator that a new generation of solutions has been produced.
     */
    public void incrementGeneration() {
        generation++;
    }

    private double slope(int annealTime, int generation) {
        double standardDeviation = 2 - (1 / (1 + Math.exp(annealTime - generation)));
        return GaussianRandom.getInstance().nextDouble(0, standardDeviation);
    }
}
