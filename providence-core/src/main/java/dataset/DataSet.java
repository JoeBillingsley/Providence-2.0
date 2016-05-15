package dataset;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all data known about a set of projects.
 * <p>
 * Created by Joseph Billingsley on 14/02/2016.
 */
public class DataSet {
    List<Feature> features = new ArrayList<>();
    List<Double[]> projects = new ArrayList<>();

    /**
     * Adds a feature to the dataset. Equivalent to a adding a column in a table.
     *
     * @param feature The feature to add.
     * @throws IllegalArgumentException If the new feature does not have the same number of projects as previous features
     */
    public void addFeature(Feature feature) {
        // region Argument checks
        if (features.size() > 0 && features.get(0).getAll().size() != feature.getAll().size())
            throw new IllegalArgumentException(
                    "The number of projects in each feature must be the same:" +
                            " Expected: " + features.get(0).getAll().size() +
                            " Actual: " + feature.getAll().size());
        // endregion

        features.add(feature);
        projects = new ArrayList<>();

        int numberOfProjects = features.get(0).getAll().size();

        for (int i = 0; i < numberOfProjects; i++) {

            Double[] project = new Double[features.size()];
            for (int j = 0; j < features.size(); j++) {
                project[j] = features.get(j).get(i);
            }

            projects.add(project);
        }
    }

    /**
     * @return All stored features. Equivalent to returning all columns in a table.
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * @param index The index of the project.
     * @return The project at the provided index. Equivalent to returning a row in a table.
     */
    public Double[] getProject(int index) {
        return projects.get(index);
    }

    /**
     * @return All stored projects. Equivalent to returning all rows in a table.
     */
    public List<Double[]> getProjects() {
        return projects;
    }

    /**
     * Removes a project from the data set.
     *
     * @param index The project to remove.
     */
    public void removeProject(int index) {
        for (Feature feature : features) {
            feature.remove(index);
        }

        projects.remove(index);
    }
}
