package clustering;

import dataset.Project;

import java.util.List;

/**
 * Created by Owner on 09/06/2016.
 */
public interface IClustering {

    Cluster[] run(List<Project> points, int k);
}
