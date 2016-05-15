package dataset.arff_parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the nominal specification attribute type and provides utilities for parsing strings into the nominal
 * specification type. Nominal specification types follow the structure {(String | 'String'),...} for example {'A', 'B', 'C'}
 * or {1, 2, 3}.
 * <p>
 * Created by Joseph Billingsley on 07/12/2015.
 */
public class NominalSpecification {

    private final ArrayList<String> components;

    private NominalSpecification() {
        components = new ArrayList<>();
    }

    /**
     * Parses a string following the structure {{(String | 'String')} into a nominal specification object.
     *
     * @param string The string to parse which dictates the structure of the type.
     * @return An object which can be used to parse strings that follow the type.
     */
    public static NominalSpecification parse(String string) {
        // NominalSpecification structure
        // {(String | 'String'),...}

        // Should be surrounded by '{' and '}'
        if (string.charAt(0) != '{' || string.charAt(string.length() - 1) != '}')
            throw new RuntimeException("Nominal Specification types must have opening and closing curly brackets: " + string);

        string = string.substring(1, string.length() - 1);
        String[] components = string.split(",");

        NominalSpecification nominalSpecification = new NominalSpecification();

        for (String component : components) {
            nominalSpecification.addComponent(component.toLowerCase().replace(" ", ""));
        }

        return nominalSpecification;
    }

    private void addComponent(String attribute) {
        components.add(attribute.toLowerCase());
    }

    public List<String> getReadonlyComponents() {
        return Collections.unmodifiableList(components);
    }

    /**
     * Used to parse data that follows the nominal specification type into a numerical value. The data provided must follow
     * the specification of the attribute the object was constructed from. For example if the attribute dictates that the
     * type must follow the structure {'A', 'B', 'C'} then the value 'B' would return 1 but 'D' would cause a runtime
     * error.
     *
     * @param data The string to parse the value of.
     * @return A numerical value representing how the data fits into the type.
     * @throws RuntimeException If the data provided does not fit the nominal specification type.
     */
    public double getValue(String data) {
        int result = components.indexOf(data.toLowerCase().replace(" ", ""));

        if (result == -1)
            throw new RuntimeException("An unexpected value was encountered when parsing nominal specification type: " + data);

        return result;
    }

}
