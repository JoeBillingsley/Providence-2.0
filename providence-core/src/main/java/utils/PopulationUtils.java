package utils;

import evolve_nn.SEESolution;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Owner on 19/06/2016.
 */
public class PopulationUtils {

    public static <S extends SEESolution> List<Double[]> solutionsToPoints(Collection<S> solutions) {
        return solutions.stream()
                .map(S::getObjectives)
                .collect(Collectors.toList());
    }

}
