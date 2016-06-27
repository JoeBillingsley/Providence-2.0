package evolve_nn;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Runs an {@link AbstractEvolutionaryAlgorithm} synchronously.
 * <p>
 * Created by Joseph Billingsley on 02/03/2016.
 */
public class AlgorithmRunner<S extends SEESolution> {

    private AtomicReference<PopulationInformation<S>> valueUpdate = new AtomicReference<>();

    private int recentGeneration = 0;
    private List<S> population;

    private AbstractEvolutionaryAlgorithm<S> algorithm;

    private List<IUpdateListener<S>> updateListeners = new ArrayList<>();

    /**
     * Creates an algorithm runner for the provided evolutionary algorithm.
     *
     * @param algorithm The algorithm to run.
     */
    public AlgorithmRunner(AbstractEvolutionaryAlgorithm<S> algorithm) {
        this.algorithm = algorithm;

        restart();
    }

    public void restart() {
        this.recentGeneration = 0;
        this.population = algorithm.createInitialPopulation();
    }

    /**
     * Runs the {@link AbstractEvolutionaryAlgorithm} provided in the constructor for a single epoch. Upon completion
     * the last population is returned.
     *
     * @return The updated population after the epoch.
     */
    public PopulationInformation<S> step() {
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
    public PopulationInformation<S> runFor(final int generations) {

        List<S> offspringPopulation;
        List<S> matingPopulation;

        population = algorithm.evaluatePopulation(population);

        for (int i = 0; i < generations; i++) {
            recentGeneration++;

            matingPopulation = algorithm.selection(population);
            offspringPopulation = algorithm.reproduction(matingPopulation);
            offspringPopulation = algorithm.evaluatePopulation(offspringPopulation);
            population = algorithm.replacement(population, offspringPopulation);
            algorithm.nextGeneration();

            PopulationInformation<S> populationInformation = new PopulationInformation<>(recentGeneration, population);

            if (Platform.isFxApplicationThread()) {
                informListeners(updateListeners, populationInformation);
            } else {
                Platform.runLater(() -> informListeners(updateListeners, populationInformation));
            }
        }

        return new PopulationInformation<>(generations, population);
    }

    private void informListeners(Iterable<IUpdateListener<S>> updateListeners, PopulationInformation<S> populationInformation) {
        for (IUpdateListener<S> listener : updateListeners) {
            listener.onUpdate(populationInformation);
        }
    }

    /**
     * Adds a consumer that will be called at the end of every epoch. The listener will receive an object containing an
     * unmodifiable version of the current population.
     *
     * @param listener The consumer to call.
     */

    public void addUpdateListener(IUpdateListener<S> listener) {
        updateListeners.add(listener);
    }
}
