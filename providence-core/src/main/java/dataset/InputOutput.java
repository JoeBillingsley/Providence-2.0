package dataset;

/**
 * Represents the known data (inputs) and the data that the system will try to estimate (outputs) for a particular project.
 * <p>
 * Created by Joseph Billingsley on 03/11/2015.
 */
public class InputOutput {

    private final Double[] input, output;

    /**
     * Constructs an InputOutput object for a particular project by splitting the project data into input and output
     * columns.
     *
     * @param project             The data of a particular project.
     * @param outputColumnIndexes The indexes of the output columns.
     */
    public InputOutput(Project project, int[] outputColumnIndexes) {
        // region Argument checks
        if (outputColumnIndexes.length == 0)
            throw new IllegalArgumentException("The output column index collection cannot be empty.");
        // endregion

        Double[] projectData = project.getData();

        input = new Double[projectData.length - outputColumnIndexes.length];
        output = new Double[outputColumnIndexes.length];

        int nextInput = 0, nextOutput = 0;
        boolean next = false;

        for (int i = 0; i < projectData.length; i++) {
            for (int outputIndex : outputColumnIndexes) {
                if (i == outputIndex) {
                    output[nextOutput++] = projectData[i];
                    next = true;
                    break;
                }

                next = false;
            }

            if (next) continue;

            input[nextInput++] = projectData[i];
        }

    }

    public Double[] getInputs() {
        return input;
    }

    public Double[] getOutputs() {
        return output;
    }
}
