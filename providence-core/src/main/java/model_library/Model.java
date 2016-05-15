package model_library;

import dataset.DataSet;
import ensemble.IEnsemble;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import search_parameters.SearchParameters;

/**
 * A view-model for the ModelLibrary controller. Contains the information required to run a data set through the system
 * and to launch a particular model again. Contains information on the settings used for this model on the last run of
 * the system.
 * <p>
 * Created by Joseph Billingsley on 16/02/2016.
 */
public class Model {

    public static final String TAG = "MODEL";

    private String title;
    private DataSet dataSet;
    private int numberOfOutliers;

    private ObservableList<TestSet> testSets;
    private ObjectProperty<IEnsemble> ensembleProperty;

    private SearchParameters lastRunSettings;

    /**
     * @param name The name of the model.
     * @param dataSet The source data set to run through the system.
     */
    public Model(String name, DataSet dataSet) {
        setDataSet(dataSet);
        setTitle(name);

        testSets = FXCollections.observableArrayList();
        ensembleProperty = new SimpleObjectProperty<>();
    }

    // region Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public int getNumberOfOutliers() {
        return numberOfOutliers;
    }

    public void setNumberOfOutliers(int numberOfOutliers) {
        this.numberOfOutliers = numberOfOutliers;
    }

    public ObjectProperty<IEnsemble> getEnsembleProperty() {
        return ensembleProperty;
    }

    public IEnsemble getEnsemble() {
        return ensembleProperty.get();
    }

    public void setEnsemble(IEnsemble ensemble) {
        this.ensembleProperty.setValue(ensemble);
    }

    public void addTestSet(TestSet testSet) {
        testSets.add(testSet);
    }

    public ObservableList<TestSet> getTestSets() {
        return testSets;
    }

    public SearchParameters getLastRunSettings() {
        return lastRunSettings;
    }

    public void setLastRunSettings(SearchParameters lastRunSettings) {
        this.lastRunSettings = lastRunSettings;
    }
    // endregion
}
