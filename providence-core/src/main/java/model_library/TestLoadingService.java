package model_library;

import dataset.DataSet;
import dataset.arff_parser.DataSetReader;
import javafx.concurrent.Task;

/**
 * Loads a test set out of an arff file and initialises it with the required values.
 * <p>
 * Created by Joseph Billingsley on 13/03/2016.
 */
public class TestLoadingService extends FileLoader<TestSet> {
    @Override
    protected Task<TestSet> createTask() {
        return new Task<TestSet>() {
            @Override
            protected TestSet call() throws Exception {
                DataSetReader dsr = new DataSetReader(getFile());
                DataSet open = dsr.open();

                return new TestSet(getFileName(), open);
            }
        };
    }
}
