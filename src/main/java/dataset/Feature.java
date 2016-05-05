package dataset;

import dataset.arff_parser.Attribute;

import java.util.List;

/**
 * Represents an attribute and it's data in an arff file or a column in a data set.
 * <p>
 * Created by Joseph Billingsley on 14/02/2016.
 */
public class Feature {

    private Attribute attribute;
    private List<Double> data;

    public Feature(Attribute attribute, List<Double> data) {
        this.attribute = attribute;
        this.data = data;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getName() {
        return attribute.getName();
    }

    public Double get(int i) {
        return data.get(i);
    }

    public List<Double> getAll() {
        return data;
    }

    public void remove(int index) {
        data.remove(index);
    }

    @Override
    public String toString() {
        return getName();
    }
}
