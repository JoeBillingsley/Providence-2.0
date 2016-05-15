package evolve_nn;

import org.uma.jmetal.solution.Solution;

import java.util.List;

/**
 * Represents an evolutionary algorithm. Implementations of this class can run through the evolutionary algorithm
 * process.
 * <p>
 * Created by Joseph Billingsley on 26/02/2016.
 * Based on 'AbstractEvolutionaryAlgorithm' from the JMetal library.
 */
public abstract class AbstractEvolutionaryAlgorithm<S extends Solution<?>> {

    /**
     * Called at the end of an evolutionary epoch. Perform any changes necessary to be made before the next epoch
     * is ran.
     */
    protected abstract void nextGeneration();

    /**
     * Produces an initial population of solutions. Implementations should ensure that the population are produced
     * randomly and across the entire solution space.
     *
     * @return A list of solutions to use as the initial population.
     */
    protected abstract List<S> createInitialPopulation();

    /**
     * Assigns a fitness to each solution in the population.
     *
     * @param population The population.
     * @return The population with assigned fitness's.
     */
    protected abstract List<S> evaluatePopulation(List<S> population);

    /**
     * Returns a subset of the provided population based on the fitness of the solutions.
     *
     * @param population The current population.
     * @return A subset of the provided population.
     */
    protected abstract List<S> selection(List<S> population);

    /**
     * Produces an offspring population based on the provided parent population.
     *
     * @param population The parent population.
     * @return An offspring population.
     */
    protected abstract List<S> reproduction(List<S> population);

    /**
     * Determines the population for the next epoch.
     *
     * @param population          The initial population.
     * @param offspringPopulation The offspring population produced during the reproduction stage.
     * @return The population to be used in the next epoch.
     */
    protected abstract List<S> replacement(List<S> population, List<S> offspringPopulation);

}
