package writers;

import error_metrics.ErrorMetric;
import evolve_nn.SEESolution;
import org.uma.jmetal.solution.Solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Provides utilities for writing lists of solutions to a file. Prefer to use this class where possible to allow standardised
 * solution result files.
 */
public class SolutionFileWriter {

    private boolean append;
    private BufferedWriter writer;

    /**
     * Opens a file for writing. Must be called before any {@link #write(List)} or {@link #write(List)} instructions.
     *
     * @param file The file to open.
     * @throws IOException If the file does not exist or can not be opened.
     */
    public void open(File file) throws IOException {
        append = false;
        writer = new BufferedWriter(new FileWriter(file));
    }

    /**
     * Writes the provided metrics to the file separated by commas.
     *
     * @param errorMetrics The error metrics to write.
     * @throws IOException If the file to write to has not been opened.
     */
    public void write(List<ErrorMetric> errorMetrics) throws IOException {
        if (append)
            writer.newLine();

        for (ErrorMetric errorMetric : errorMetrics) {
            writer.write(errorMetric.getName() + ", ");
        }

        append = true;
    }

    /**
     * Writes the provided solutions to the file on new lines. The title is placed before the list of solutions and is
     * intended to be used to provide information about the source of the solutions for example 'Non-dominated solutions'.
     *
     * @param title     The title to write before the solutions.
     * @param solutions The solutions to write to the file.
     * @throws IOException If the file to write to is not open.
     */
    public void write(String title, List<SEESolution> solutions) throws IOException {
        if (append)
            writer.newLine();

        writer.newLine();
        writer.write(title);

        for (Solution solution : solutions) {
            writer.newLine();

            for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
                writer.write(solution.getObjective(i) + ", ");
            }
        }

        append = true;
    }

    /**
     * @throws IOException If the file to write to is not open.
     */
    public void close() throws IOException {
        writer.close();
        writer = null;
    }

}
