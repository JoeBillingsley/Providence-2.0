package dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a data set that can be used for training machine learners with.
 * Provides methods for forming monte-carlo cross-validation sets.
 * <p>
 * Created by Joseph Billingsley on 02/11/2015.
 */
public class TrainingData {

    private final DataSet srcDataSet;
    private List<InputOutput> trainingData;

    // TODO: Add support for training, testing and validation sets

    /**
     * Constructs a training set from a source data set and the features to be estimated.
     *
     * @param dataSet        The data set to construct a training set from.
     * @param outputFeatures The features to be estimated.
     */
    public TrainingData(DataSet dataSet, List<Feature> outputFeatures) {
        this.srcDataSet = dataSet;
        setOutputFeatures(outputFeatures);
    }

    private void setOutputFeatures(List<Feature> outputFeatures) {

        List<Feature> features = srcDataSet.getFeatures();
        int[] outputIndexes = new int[outputFeatures.size()];

        for (int i = 0; i < outputFeatures.size(); i++) {
            String outputName = outputFeatures.get(i).getName();

            for (int j = 0; j < features.size(); j++) {
                String featureName = features.get(j).getName();

                if (outputName.equals(featureName)) {
                    outputIndexes[i] = j;
                    break;
                }
            }
        }

        setOutputFeatures(outputIndexes);
    }

    private void setOutputFeatures(int[] outputIndexes) {
        List<Project> projects = srcDataSet.getProjects();

        trainingData = new ArrayList<>();

        for (Project project : projects) {
            trainingData.add(new InputOutput(project, outputIndexes));
        }
    }

    /**
     * Returns all of the constructed training data rows.
     *
     * @return All constructed training data rows.
     */
    public InputOutput[] getAll() {
        return trainingData.toArray(new InputOutput[trainingData.size()]);
    }

    /**
     * Splits the dataset into two samples - the training set and the testing set. The sizes of the two samples is
     * determined by the ratio provided to the function.
     * <p>
     * The training set and testing set are constructed randomly so repeated calls to this function will not return the
     * same results but a row will not appear in both the training set and the testing set in one call.
     *
     * @param trainingSetToTestingSet A number between 0 and 1 that represents the relative size of the training set to
     *                                the testing set.
     * @return Two samples of the dataset - the first is the training set the second the testing set.
     */
    public InputOutput[][] getSplitOfRatio(double trainingSetToTestingSet) {
        // region Argument checks
        if (trainingSetToTestingSet < 0 || trainingSetToTestingSet > 1)
            throw new IllegalArgumentException("The provided ratio must be between 0 and 1: " + trainingSetToTestingSet);
        // endregion

        Collections.shuffle(trainingData);

        int sizeOfTrainingSet = (int) Math.floor(trainingData.size() * trainingSetToTestingSet);

        List<InputOutput> trainingSet = trainingData.subList(0, sizeOfTrainingSet);
        List<InputOutput> testingSet = trainingData.subList(sizeOfTrainingSet, trainingData.size());

        return new InputOutput[][]
                {
                        trainingSet.toArray(new InputOutput[trainingSet.size()]),
                        testingSet.toArray(new InputOutput[testingSet.size()])
                };
    }
}
