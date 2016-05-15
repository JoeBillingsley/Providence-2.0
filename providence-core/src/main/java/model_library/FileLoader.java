package model_library;

import javafx.concurrent.Service;

import java.io.File;

/**
 * Represents any service that has to load a file on a separate thread and perform an action in order to build the
 * resulting output.
 * <p>
 * Created by Joseph Billingsley on 13/03/2016.
 */
public abstract class FileLoader<T> extends Service<T> {
    private File file;

    /**
     * @return The file to act upon.
     */
    protected File getFile() {
        return file;
    }

    /**
     * Sets the file to act upon.
     *
     * @param file The file to load and perform a transformative action on.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return The formatted name of the file to act upon.
     */
    protected String getFileName() {
        String name = getFile().getName();
        return name.substring(0, name.lastIndexOf('.'));
    }
}
