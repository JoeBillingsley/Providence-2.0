package evolve_nn;

import neural_network.INeuralNetwork;
import org.uma.jmetal.solution.Solution;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single solution to the problem of software effort estimation.
 * <p>
 * Created by Joseph Billingsley on 22/10/2015.
 */
public class SEESolution implements Solution<Double> {

    private final INeuralNetwork network;
    private final Double[] objectives;
    private final Map<Object, Object> attributes;

    /**
     * @param numberOfObjectives The number of objectives/error metrics to evaluate the solution with.
     * @param network            The neural network to initialise the solution with.
     */
    public SEESolution(int numberOfObjectives, INeuralNetwork network) {
        this.network = network;

        objectives = new Double[numberOfObjectives];
        attributes = new HashMap<>();
    }

    // region Getters and Setters
    public INeuralNetwork getNeuralNetwork() {
        return network;
    }

    @Override
    public void setObjective(int index, double value) {
        objectives[index] = value;
    }

    @Override
    public double getObjective(int index) {
        return objectives[index];
    }

    public Double[] getObjectives() {
        return objectives;
    }

    @Override
    public Double getVariableValue(int index) {

        int weightLengths = network.getWeights().length;

        if (index < weightLengths)
            return network.getWeight(index);

        return network.getThreshold(index - weightLengths);
    }

    @Override
    public void setVariableValue(int index, Double value) {

        int weightLengths = network.getWeights().length;

        if (index < weightLengths) {
            network.setWeight(index, value);
        } else {
            network.setThreshold(index - weightLengths, value);
        }
    }

    @Override
    public String getVariableValueString(int index) {
        return getVariableValue(index).toString();
    }

    @Override
    public int getNumberOfVariables() {
        return network.getWeights().length + network.getThresholds().length - 1;
    }

    @Override
    public int getNumberOfObjectives() {
        return objectives.length;
    }

    @Override
    public Solution<Double> copy() {
        return new SEESolution(
                getNumberOfObjectives(),
                network.copy());
    }

    @Override
    public void setAttribute(Object id, Object value) {
        attributes.put(id, value);
    }

    @Override
    public Object getAttribute(Object id) {
        return attributes.get(id);
    }

    //endregion
}
