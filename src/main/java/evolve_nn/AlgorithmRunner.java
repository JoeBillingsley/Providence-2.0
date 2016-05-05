package evolve_nn;

import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Runs an {@link AbstractEvolutionaryAlgorithm} synchronously or asynchronously. Has pausing, continuing and stopping
 * functionality and can be set up to run forever or to stop after a specified number of generations.
 * <p>
 * To maintain thread safety the population provided to listeners is read only.
 * <p>
 * Created by Joseph Billingsley on 02/03/2016.
 */
public class AlgorithmRunner<S extends Solution<?>> {

    public static final int RUN_FOREVER = -1;

    private AbstractEvolutionaryAlgorithm<S> algorithm;
    private List<Consumer<List<S>>> updateListeners = new ArrayList<>();
    private List<Consumer<List<S>>> finishedListeners = new ArrayList<>();

    private volatile boolean stop = false, pause = true;

    /**
     * Creates an algorithm runner for the provided evolutionary algorithm.
     *
     * @param algorithm The algorithm to run.
     */
    public AlgorithmRunner(AbstractEvolutionaryAlgorithm<S> algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Runs the {@link AbstractEvolutionaryAlgorithm} provided in the constructor.
     *
     * @param stopAfter   The number of epochs after which the algorithm should stop running. If
     *                    AlgorithmRunner.RUN_FOREVER is provided the algorithm will run until stop is called.
     * @param runSync     If true, runs the algorithm on the same thread as the caller. Otherwise runs the algorithm on
     *                    a daemon thread.
     * @param startPaused If true the algorithm will not run until {@link #continueRun()} is called.
     */
    public void start(final int stopAfter, boolean runSync, boolean startPaused) {

        if (startPaused)
            pause();
        else
            continueRun();

        boolean runForever = stopAfter == RUN_FOREVER;

        Thread run = new Thread(() -> {
            int currGen = stopAfter;

            List<S> offspringPopulation;
            List<S> matingPopulation;

            List<S> population = algorithm.createInitialPopulation();
            population = algorithm.evaluatePopulation(population);

            while (true) {

                if (stop) {
                    for (Consumer<List<S>> finishedListener : finishedListeners) {
                        List<S> immutablePopulation = Collections.unmodifiableList(population);
                        finishedListener.accept(immutablePopulation);
                    }

                    return;
                }

                if (pause) continue;

                matingPopulation = algorithm.selection(population);
                offspringPopulation = algorithm.reproduction(matingPopulation);
                offspringPopulation = algorithm.evaluatePopulation(offspringPopulation);
                population = algorithm.replacement(population, offspringPopulation);
                algorithm.nextGeneration();

                for (Consumer<List<S>> listener : updateListeners) {
                    List<S> immutablePopulation = Collections.unmodifiableList(population);
                    listener.accept(immutablePopulation);
                }

                if (runForever) continue;

                if (currGen > 0) currGen--;
                else if (currGen == 0) stop();
            }
        });

        run.setDaemon(true);
        run.start();

        if (runSync) {
            try {
                run.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Brings the algorithm out of a paused state and into the running stage.
     */
    public void continueRun() {
        pause = false;
    }

    /**
     * Pauses the running of the algorithm at the end of the current epoch.
     */
    public void pause() {
        pause = true;
    }

    /**
     * Stops the algorithm at the end of the current epoch. 'Finished' listeners will be called when the runner has
     * stopped. The algorithm cannot be continued after stopped and the daemon thread associated with the runner will be
     * released.
     */
    public void stop() {
        stop = true;
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

    /**
     * Adds a consumer that will be called when the algorithm stops running. The listener will receive an unmodifiable
     * version of the final population.
     *
     * @param listener The consumer to call.
     */
    public void addFinishedListener(Consumer<List<S>> listener) {
        finishedListeners.add(listener);
    }
}
