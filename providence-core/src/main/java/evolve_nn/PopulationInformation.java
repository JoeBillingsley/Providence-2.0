package evolve_nn;

import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;
import utils.PopulationUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Owner on 19/06/2016.
 */
public class PopulationInformation<S extends SEESolution> {

    private int generation;

    private List<S> population;
    private List<S> nonDominatedPopulation;

    /**
     * @param generation The generation of the population. The initial population is generation 0.
     * @param population
     */
    public PopulationInformation(int generation, List<S> population) {
        this.generation = generation;
        this.population = Collections.unmodifiableList(population);
    }

    public List<S> getPopulation() {
        return population;
    }

    public List<Double[]> getDominatedPoints() {
        return PopulationUtils.solutionsToPoints(getPopulation());
    }

    public List<S> getNonDominatedPopulation() {
        if (nonDominatedPopulation == null)
            nonDominatedPopulation = new DominanceRanking<S>().computeRanking(getPopulation()).getSubfront(0);

        return nonDominatedPopulation;
    }

    public List<Double[]> getNonDominatedPoints() {
        return PopulationUtils.solutionsToPoints(getNonDominatedPopulation());
    }

    public int getGeneration() {
        return generation;
    }
}
