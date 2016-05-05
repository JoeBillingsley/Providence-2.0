package evolve_nn;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.Ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An implementation of a preference based variant of the Harmonic Distance MultiObjective Evolutionary Algorithm.
 * <p>
 * Created by Joseph Billingsley on 09/11/2015.
 * Extended from 'NSGAII.java' from the JMetal library.
 */
public class RHaDMOEA extends AbstractEvolutionaryAlgorithm<SEESolution> {

    private final int populationSize;

    private final SEEProblem problem;
    private final GaussianMutation mutationOperator;
    private final MLPCrossOver crossoverOperator;

    private Double[] aspirationPoint;
    private Double nonRDominanceThreshold;

    /**
     * @param problem           The problem to solve.
     * @param populationSize    The size of the population.
     * @param crossOverOperator The operator to use to mix information between solutions in the populations.
     * @param mutationOperator  The operator to use to move solutions to new regions of the solution space.
     */
    public RHaDMOEA(SEEProblem problem, int populationSize, MLPCrossOver crossOverOperator, GaussianMutation mutationOperator) {
        this(problem, populationSize, crossOverOperator, mutationOperator, .2);
    }

    /**
     * @param problem                The problem to solve.
     * @param populationSize         The size of the population.
     * @param crossOverOperator      The operator to use to mix information between solutions in the populations.
     * @param mutationOperator       The operator to use to move solutions to new regions of the solution space.
     * @param nonRDominanceThreshold The non r-Dominance threshold to use when ranking solutions according to preference. See
     *                               {@link RDominanceRanking} for more information.
     */
    public RHaDMOEA(SEEProblem problem, int populationSize, MLPCrossOver crossOverOperator, GaussianMutation mutationOperator, Double nonRDominanceThreshold) {
        super();

        this.problem = problem;

        this.crossoverOperator = crossOverOperator;
        this.mutationOperator = mutationOperator;
        this.populationSize = populationSize;
        this.nonRDominanceThreshold = nonRDominanceThreshold;
    }

    @Override
    protected void nextGeneration() {
        crossoverOperator.incrementGeneration();
    }

    @Override
    protected List<SEESolution> createInitialPopulation() {
        List<SEESolution> population = new ArrayList<>(populationSize);

        for (int i = 0; i < populationSize; i++) {
            SEESolution newIndividual = problem.createSolution();
            population.add(newIndividual);
        }

        return population;
    }

    @Override
    protected List<SEESolution> evaluatePopulation(List<SEESolution> population) {
        population.forEach(problem::evaluate);
        return population;
    }

    @Override
    protected List<SEESolution> selection(List<SEESolution> population) {
        return population;
    }

    @Override
    protected List<SEESolution> reproduction(List<SEESolution> population) {
        List<SEESolution> offspringPopulation = new ArrayList<>();
        JMetalRandom random = JMetalRandom.getInstance();

        for (int i = 0; i < populationSize; i++) {

            int parentOneId, parentTwoId, parentThrId;

            parentOneId = random.nextInt(0, population.size() - 1);

            do {
                parentTwoId = random.nextInt(0, population.size() - 1);
            } while (parentTwoId == parentOneId);

            do {
                parentThrId = random.nextInt(0, population.size() - 1);
            } while (parentThrId == parentTwoId || parentThrId == parentOneId);

            List<SEESolution> parentSolutions = new ArrayList<>();

            parentSolutions.add(population.get(parentOneId));
            parentSolutions.add(population.get(parentTwoId));
            parentSolutions.add(population.get(parentThrId));

            List<SEESolution> children = crossoverOperator.execute(parentSolutions);

            offspringPopulation.addAll(children.stream()
                    .map(mutationOperator::execute)
                    .collect(Collectors.toList()));
        }

        return offspringPopulation;
    }

    @Override
    protected List<SEESolution> replacement(List<SEESolution> parentPopulation, List<SEESolution> offspringPopulation) {
        List<SEESolution> jointPopulation = new ArrayList<>();

        jointPopulation.addAll(parentPopulation);
        jointPopulation.addAll(offspringPopulation);

        List<SEESolution> population = new ArrayList<>();

        // r-Dominance
        Ranking<SEESolution> rDominanceRanking = new RDominanceRanking(aspirationPoint, nonRDominanceThreshold);
        Ranking<SEESolution> preference = rDominanceRanking.computeRanking(jointPopulation);
        List<SEESolution> remainingFront = addRankingToPopulation(population, preference);

        // Crowding distance
        HarmonicCrowdingDistance crowdingDistance = new HarmonicCrowdingDistance(2);
        crowdingDistance.setSearchPopulation(jointPopulation);
        crowdingDistance.computeDensityEstimator(remainingFront);

        CrowdingDistanceComparator<SEESolution> crowdingDistanceComparator = new CrowdingDistanceComparator<>(crowdingDistance);

        Collections.sort(remainingFront, crowdingDistanceComparator);

        int remaining = populationSize - population.size();
        for (int i = 0; i < remaining; i++) {
            population.add(remainingFront.get(i));
        }

        return population;
    }

    /**
     * Adds all fronts into the population that can fit completely in, in order of rank.
     *
     * @return The first front that does not fit into the population
     */
    private List<SEESolution> addRankingToPopulation(List<SEESolution> population, Ranking<SEESolution> ranking) {
        int subfrontIndex = 0;

        while (population.size() <= populationSize) {
            int remainingSpaces = populationSize - population.size();

            List<SEESolution> subfront = ranking.getSubfront(subfrontIndex);
            subfrontIndex++;

            if (subfront.size() < remainingSpaces) {
                population.addAll(subfront);
            } else {
                return subfront;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Sets the aspiration point to push the population towards. If null, no preference calculations will be performed.
     *
     * @param aspirationPoint The aspiration point to push the population towards.
     */
    public void setAspirationPoint(Double[] aspirationPoint) {
        this.aspirationPoint = aspirationPoint;
    }
}
