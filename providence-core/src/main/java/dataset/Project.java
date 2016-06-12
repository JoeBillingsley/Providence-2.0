package dataset;

/**
 * Represents what is known about a single project.
 * <p>
 * Created by Joseph Billingsley on 12/06/2016.
 */
public class Project {

    private int id;
    private Double[] data;

    public Project(int id, Double[] data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public Double[] getData() {
        return data;
    }
}
