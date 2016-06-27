package evolve_nn;

/**
 * Created by Joseph Billingsley on 19/06/2016.
 */
public interface IUpdateListener<S extends SEESolution> {
    void onUpdate(PopulationInformation<S> populationInformation);
}
