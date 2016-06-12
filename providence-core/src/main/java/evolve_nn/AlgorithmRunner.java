package evolve_nn;

import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Runs an {@link AbstractEvolutionaryAlgorithm} synchronously.
 * <p>
 * Created by Joseph Billingsley on 02/03/2016.
 */
public class AlgorithmRunner<S extends Solution<?>> {

    private AbstractEvolutionaryAlgorithm<S> algorithm;

    private List<Consumer<List<S>>> updateListeners = new ArrayList<>();

    /**
     * Creates an algorithm runner for the provided evolutionary algorithm.
     *
     * @param algorithm The algorithm to run.
     */
    public AlgorithmRunner(AbstractEvolutionaryAlgorithm<S> algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Runs the {@link AbstractEvolutionaryAlgorithm} provided in the constructor for a single epoch. Upon completion
     * the last population is returned.
     *
     * @return The updated population after the epoch.
     */
    public List<S> step() {
        return runFor(1);
    }

    /**
     * Runs the {@link AbstractEvolutionaryAlgorithm} provided in the constructor for the provided number of epochs.
     * Upon completion the last population is returned.
     *
     * @param generations The number of epochs after which the algorithm should stop running. If
     *                    the value AlgorithmRunner.RUN_FOREVER is provided the algorithm will run until stop is called.
     * @return The most recent population.
     */
    public List<S> runFor(final int generations) {

        List<S> offspringPopulation;
        List<S> matingPopulation;

        List<S> population = algorithm.createInitialPopulation();
        population = algorithm.evaluatePopulation(population);

        for (int i = 0; i < generations; i++) {
            matingPopulation = algorithm.selection(population);
            offspringPopulation = algorithm.reproduction(matingPopulation);
            offspringPopulation = algorithm.evaluatePopulation(offspringPopulation);
            population = algorithm.replacement(population, offspringPopulation);
            algorithm.nextGeneration();

            List<S> immutablePopulation = Collections.unmodifiableList(population);

            for (Consumer<List<S>> listener : updateListeners) {
                listener.accept(immutablePopulation);
            }
        }

        return population;
    }

    /**
     * Adds a consumer that will be called at the end of every epoch. The listener will receive an unmodifiable version
     * of the current population.
     *
     * @param listener The consumer to call.
     */
    public void addUpdateListener(Consumer<List<S>> listener) {
        updateListeners.add(listener);
    }
}
