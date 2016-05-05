package model_library;

import clustering.OutlierDetector;
import dataset.DataSet;
import dataset.arff_parser.DataSetReader;
import javafx.concurrent.Task;

import java.util.Arrays;
import java.util.List;

/**
 * Handles loading, parsing of .arff files and performs initial pre-processing on loaded models.
 * <p>
 * Created by Joseph Billingsley on 09/02/2016.
 */
public class ModelLoadingService extends FileLoader<Model> {

    @Override
    protected Task<Model> createTask() {
        return new Task<Model>() {
            @Override
            protected Model call() throws Exception {
                // Parse file
                DataSetReader dsr = new DataSetReader(getFile());
                DataSet ds = dsr.open();

                List<Double[]> projectsData = ds.getProjects();

                // Find outliers
                List<Double[]> outliers
                        = new OutlierDetector().getOutliers(projectsData);

                // Remove outliers
                for (int i = 0; i < projectsData.size(); i++) {
                    for (Double[] outlier : outliers) {
                        if (Arrays.equals(projectsData.get(i), outlier))
                            ds.removeProject(i);
                    }
                }

                Model model = new Model(getFileName(), ds);
                model.setNumberOfOutliers(outliers.size());

                return model;
            }
        };
    }
}
