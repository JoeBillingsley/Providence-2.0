package dataset.arff_parser;

import java.util.List;
import java.util.function.Function;

/**
 * Represents a attribute in a '.arff' file.
 * <p>
 * Created by Joseph Billingsley on 07/12/2015.
 */
public class Attribute {

    private final String name;
    private final Function<String, Double> eval;
    private List<String> allowedValues;

    /**
     * Creates an attribute with the name of the attribute and the means to parse data items related to the attribute.
     *
     * @param name The name of the attribute.
     * @param eval A function to parse data items connected to the attribute.
     */
    public Attribute(String name, Function<String, Double> eval) {
        this.name = name;
        this.eval = eval;
    }

    /**
     * Creates an attribute with the name of the attribute and the means to parse data items related to the attribute.
     *
     * @param name          Te name of the attribute.
     * @param eval          A function to parse data items connected to the attribute.
     * @param allowedValues A list of acceptable values for the type.
     */
    public Attribute(String name, Function<String, Double> eval, List<String> allowedValues) {
        this.name = name;
        this.eval = eval;
        this.allowedValues = allowedValues;
    }

    /**
     * @return The name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * @return A function that can parse data items related to this attribute.
     */
    public Function<String, Double> getEval() {
        return eval;
    }

    /**
     * Returns the list of values that can be parsed by the evaluation function. If null then the evaluation function
     * can parse any strings that can be parsed into a double.
     *
     * @return The list of values that can be parsed by the evaluation function or null.
     */
    public List<String> getAllowedValues() {
        return allowedValues;
    }
}
