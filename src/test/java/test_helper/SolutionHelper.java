package test_helper;

import evolve_nn.SEESolution;

import java.util.ArrayList;

public class SolutionHelper {

    public static SEESolution makeSolution(Double[] objective) {

        SEESolution solution = new SEESolution(objective.length, null);

        for (int i = 0; i < objective.length; i++) {
            solution.setObjective(i, objective[i]);
        }

        return solution;
    }

    public static ArrayList<SEESolution> makeSolutions(Double[]... objectiveList) {
        ArrayList<SEESolution> solutions = new ArrayList<>();

        for (Double[] objective : objectiveList) {
            solutions.add(makeSolution(objective));
        }

        return solutions;
    }
}
