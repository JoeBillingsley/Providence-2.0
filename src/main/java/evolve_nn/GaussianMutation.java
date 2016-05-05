package evolve_nn;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import utils.GaussianRandom;

/**
 * A mutation operator for problems represented as multi layer perceptrons. Modifies a weight by a random number
 * drawn from a Gaussian distribution with a mean of 0 and a variance of 0.1.
 * <p>
 * Created by Joseph Billingsley on 06/11/2015.
 */
public class GaussianMutation implements MutationOperator<SEESolution> {

    private final double mutationProbability;
    private final JMetalRandom random;

    /**
     * @param mutationProbability The likelihood to modify a weight.
     */
    public GaussianMutation(double mutationProbability) {
        this.mutationProbability = mutationProbability;
        random = JMetalRandom.getInstance();
    }

    @Override
    public SEESolution execute(SEESolution seeSolution) {

        for (int i = 0; i < seeSolution.getNumberOfVariables(); i++) {
            if (random.nextDouble(0, 1) > mutationProbability)
                continue;

            GaussianRandom random = GaussianRandom.getInstance();
            seeSolution.setVariableValue(i, seeSolution.getVariableValue(i) + random.nextDouble(0, 0.1));
        }

        return seeSolution;
    }
}
