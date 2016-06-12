package dataset.arff_parser;

import dataset.DataSet;
import dataset.Feature;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Provides utilities for parsing arff files into data sets.
 * <p>
 * Created by Joseph Billingsley on 02/12/2015.
 */
public class DataSetReader {

    private static int lineNo;

    public static DataSet open(File file) throws IOException {
        return open(new BufferedReader(new FileReader(file)));
    }

    /**
     * Parses the arff file provided in the constructor into a @link {dataset.DataSet}
     *
     * @return A dataset object with the information from the arff file.
     * @throws IOException If the file cannot be read
     */
    public static DataSet open(BufferedReader reader) throws IOException {

        lineNo = 0;
        List<Attribute> attributes = new ArrayList<>();

        Map<String, List<Double>> data = new HashMap<>();

        boolean inDataRegion = false;

        String line;
        while ((line = reader.readLine()) != null) {

            lineNo++;

            // Remove comments
            line = line.replaceAll("%.*", "");

            String[] words = line.split(" ");
            if (line.isEmpty() || words.length < 1)
                continue;

            String keyword = words[0];

            if (inDataRegion) {
                parseData(attributes, data, line);
                continue;
            }

            if (keyword.equalsIgnoreCase("@attribute")) {
                Attribute attribute = parseAttribute(line);
                attributes.add(attribute);

                data.put(attribute.getName(), new ArrayList<>());
            }

            if (keyword.equalsIgnoreCase("@data")) {

                if (attributes.size() == 0)
                    throw new RuntimeException("The file does not contain any attributes before the data element");

                inDataRegion = true;
            }
        }

        if (attributes.size() == 0)
            throw new RuntimeException("The file does not contain any attributes");

        DataSet dataSet = new DataSet();

        // Match on attribute to maintain original ordering of attributes
        for (Attribute attribute : attributes) {
            String title = attribute.getName();

            List<Double> column = data.get(title);
            dataSet.addFeature(new Feature(attribute, column));
        }

        return dataSet;
    }

    private static void parseData(List<Attribute> attributes, Map<String, List<Double>> data, String line) {

        String[] values = line.split(",");

        // Check the data is of the correct size
        if (values.length < attributes.size())
            throw new RuntimeException("Error at line " + lineNo + ". There are not as many values as attributes");

        if (values.length > attributes.size())
            throw new RuntimeException("Error at line " + lineNo + ". There are more values than attributes");

        // Apply all of the attributes and store the value in the correct column
        for (int i = 0; i < attributes.size(); i++) {
            String value = values[i];
            Attribute attribute = attributes.get(i);

            Function<String, Double> eval = attribute.getEval();

            List<Double> column = data.get(attribute.getName());
            column.add(eval.apply((value)));
        }
    }

    private static Attribute parseAttribute(String attribute) {

        // Attribute structure
        // @attribute <attribute-name>|'<attribute-name>' <datatype>
        //             String                              DataType

        String[] words = attribute.split(" ");

        StringBuilder name = new StringBuilder();
        int namePos = 1;

        if (words[namePos].startsWith("'")) {

            for (int i = namePos; i < words.length; i++) {
                String word = words[i];
                name.append(word);
                namePos++;

                if (word.endsWith("'"))
                    break;

                // Add spaces back for display purpose
                name.append(" ");
            }
        } else {
            name.append(words[namePos++]);
        }

        StringBuilder dataType = new StringBuilder();
        for (int i = namePos; i < words.length; i++) {
            dataType.append(words[i]);
        }

        if (dataType.length() == 0)
            throw new IllegalArgumentException("Error at line " + lineNo + ". No datatype specified in attribute: " + attribute);

        return new Attribute(
                name.toString(),
                parseDataType(dataType.toString()),
                parseAcceptableValues(dataType.toString()));
    }

    private static Function<String, Double> parseDataType(String dataType) {
        // DataType structure
        // numeric | <nominal-specification> | date[<date-format>] | string
        // Double     NominalSpecification     Not supported         Not supported
        // The 'string' and 'date' types are not supported as they cannot be sensibly converted into a double

        if (dataType.equalsIgnoreCase("numeric") || dataType.equalsIgnoreCase("real")) {
            return Double::parseDouble;
        }

        if (dataType.startsWith("{")) {
            return (x) -> NominalSpecification.parse(dataType).getValue(x);
        }

        throw new RuntimeException("Error at line " + lineNo + ". Unrecognized data type: " + dataType);
    }

    @Nullable
    private static List<String> parseAcceptableValues(String dataType) {
        // DataType structure
        // numeric | <nominal-specification> | date[<date-format>] | string
        // Double     NominalSpecification     Not supported         Not supported
        // The 'string' and 'date' types are not supported as they cannot be sensibly converted into a double

        if (dataType.equalsIgnoreCase("numeric") || dataType.equalsIgnoreCase("real"))
            return null;

        if (dataType.startsWith("{"))
            return NominalSpecification.parse(dataType).getReadonlyComponents();

        throw new RuntimeException("Error at line " + lineNo + ". Unrecognized data type: " + dataType);
    }
}
