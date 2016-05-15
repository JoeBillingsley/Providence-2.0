package search_parameters;

import dataset.Feature;
import error_metrics.ErrorMetric;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for the data required to configure a search.
 * <p>
 * Created by Joseph Billingsley on 30/11/2015.
 */
public class SearchParameters {

    public static final String TAG = "SEARCH_PARAMS";

    private final IntegerProperty populationSize;
    private final DoubleProperty probabilityOfCrossover, probabilityOfMutation;
    private final IntegerProperty annealTime;

    private ObservableList<Feature> inputColumns;
    private ObservableList<Feature> outputColumns;
    private ObservableList<ErrorMetric> errorMetrics;

    /**
     * @param populationSize         The size of the population to use.
     * @param probabilityOfCrossover The likelihood for a crossover operation to occur when generating offspring.
     * @param probabilityOfMutation  The likelihood for a mutation operation to occur when generating offspring.
     * @param annealTime             The number of generations after which the system should exploit found solutions more
     *                               than exploring the solution space.
     */
    public SearchParameters(int populationSize, double probabilityOfCrossover, double probabilityOfMutation, int annealTime) {
        this.inputColumns = FXCollections.observableArrayList();
        this.outputColumns = FXCollections.observableArrayList();
        this.errorMetrics = FXCollections.observableArrayList();

        this.populationSize = new SimpleIntegerProperty(populationSize);
        this.probabilityOfCrossover = new SimpleDoubleProperty(probabilityOfCrossover);
        this.probabilityOfMutation = new SimpleDoubleProperty(probabilityOfMutation);
        this.annealTime = new SimpleIntegerProperty(annealTime);
    }

    // region Getters and Setters
    public ObservableList<Feature> inputColumnsProperty() {
        return inputColumns;
    }

    public ObservableList<Feature> outputColumnsProperty() {
        return outputColumns;
    }

    public double getProbabilityOfCrossover() {
        return probabilityOfCrossover.get();
    }

    public DoubleProperty probabilityOfCrossoverProperty() {
        return probabilityOfCrossover;
    }

    public double getProbabilityOfMutation() {
        return probabilityOfMutation.get();
    }

    public DoubleProperty probabilityOfMutationProperty() {
        return probabilityOfMutation;
    }

    public int getAnnealTime() {
        return annealTime.get();
    }

    public IntegerProperty annealTimeProperty() {
        return annealTime;
    }

    public int getPopulationSize() {
        return populationSize.get();
    }

    public IntegerProperty populationSizeProperty() {
        return populationSize;
    }

    public ObservableList<ErrorMetric> getErrorMetrics() {
        return errorMetrics;
    }
    // endregion

    /**
     * @return A deep copy of the object.
     */
    public SearchParameters copy() {
        SearchParameters searchParametersCopy =
                new SearchParameters(getPopulationSize(), getProbabilityOfCrossover(), getProbabilityOfMutation(), getAnnealTime());

        searchParametersCopy.inputColumnsProperty().setAll(inputColumnsProperty());
        searchParametersCopy.outputColumnsProperty().setAll(outputColumnsProperty());
        searchParametersCopy.getErrorMetrics().setAll(getErrorMetrics());

        return searchParametersCopy;
    }
}
