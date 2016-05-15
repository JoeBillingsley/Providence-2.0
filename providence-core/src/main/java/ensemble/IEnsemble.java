package ensemble;

/**
 * Represents a collection of estimation making units that can produce a single estimate based on the combined results of
 * passing a value into all units.
 * <p>
 * Created by Joseph Billingsley on 23/12/2015.
 */
public interface IEnsemble {
    /**
     * Produces a combined result from the units in the ensemble.
     *
     * @param inputs The data to provide to each unit.
     * @return The produced estimate based on the predictions of units in the ensemble.
     */
    Double getResult(Double[] inputs);
}
