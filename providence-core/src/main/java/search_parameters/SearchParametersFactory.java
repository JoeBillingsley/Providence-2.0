package search_parameters;

import org.jetbrains.annotations.Contract;

/**
 * Helper class for producing search parameters objects. Constructs search parameter objects with sensible values
 * for most fields.
 * <p>
 * Created by Joseph Billingsley on 01/12/2015.
 */
public class SearchParametersFactory {

    /**
     * Constructs a new search parameters object with a sensible value for most fields. Does not populate the input
     * or output column properties.
     *
     * @return A search parameters object with a sensible configuration.
     */
    @Contract(" -> !null")
    public static SearchParameters getDefault() {
        int populationSize = 100;
        double probabilityOfCrossover = 0.9, probabilityOfMutation = 0.03;
        int annealTime = 50;

        return new SearchParameters(populationSize, probabilityOfCrossover, probabilityOfMutation, annealTime);
    }
}
