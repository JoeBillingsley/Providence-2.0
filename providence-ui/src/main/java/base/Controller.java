package base;

import custom_controls.ExceptionAlert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Provides utilities for displaying new full sized and modal screens. Allows for information to be shared between separate
 * screens by passing context between each scene.
 * <p>
 * Created by Joseph Billingsley on 01/12/2015.
 */
public abstract class Controller<T> {

    private Context context;
    private Stage stage;

    private T result;
    private List<Consumer<T>> listeners = new ArrayList<>();

    /**
     * Called when the controller has been loaded and displayed. Any exceptions thrown here will be caught and displayed
     * as an error message.
     *
     * @throws Exception
     */
    protected abstract void initialise() throws Exception;

    /**
     * Called when the context is available in {@link Controller#getContext()}
     */
    protected abstract void whenContextAdded();

    /**
     * Returns true if the controller is to be treated as a modal i.e. in a separate stage and not full screen.
     * Should not be overridden. If you want to use a modal dialogue extend the {@link Modal} class.
     *
     * @return True if the dialogue should be treated as a modal, false otherwise.
     */
    protected boolean isModal() {
        return false;
    }

    /**
     * A convenience method over {@link Controller#loadScene(String, boolean)}. Loads the requested view closing the
     * current screen if the view is not a modal dialogue.
     *
     * @param view The view to load.
     * @return The controller of the new view after it has been initialised and the context set.
     * @see Controller#loadScene(String, boolean)
     */
    public Controller loadScene(String view) {
        return loadScene(view, false);
    }

    /**
     * Makes the controller load the requested view. Uses views from the resources/view folder so any views that are
     * nested deeper than this will need to be prefixed with the appendix. For example to load a custom control the view
     * argument would be 'custom_controls/ListToList'. The file extension should not be included.
     *
     * @param view          The view to load.
     * @param persistParent True if the current scene should remain open once the new scene has been opened.
     * @return The controller of the new view after it has been initialised and the context set.
     */
    public Controller loadScene(String view, boolean persistParent) {

        view = "/view/" + view + ".fxml";

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(view));
            Parent root = loader.load();

            Controller controller = loader.getController();

            if (controller.isModal()) {
                Stage modalStage = new Stage();
                controller.setStage(modalStage);

                modalStage.setScene(new Scene(root));
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.initOwner(stage.getOwner());

                modalStage.show();

                if (!persistParent)
                    stage.close();
            } else {
                controller.setStage(stage);
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
            }

            controller.getStage().setTitle("Providence - Software Effort Estimation Tool");

            Image image = new Image("file:src/main/resources/icon.png");
            controller.getStage().getIcons().add(image);

            controller.initialise();
            controller.setContext(context);

            return controller;
        } catch (Exception e) {
            ExceptionAlert alert = new ExceptionAlert(e);
            alert.setTitle("Unexpected error!");
            alert.setContentText("An unexpected error occurred.");
            alert.showAndWait();
        }

        return null;
    }

    public Controller loadSceneForResult(String view, Consumer<T> result) {
        Controller controller = loadScene(view, true);
        controller.setOnResult(result);

        return controller;
    }

    protected void finish() {
        for (Consumer<T> listener : listeners) {
            listener.accept(getResult());
        }

        getStage().close();
    }

    // region Getters and setters

    private void setOnResult(Consumer<T> listener) {
        listeners.add(listener);
    }

    private T getResult() {
        return result;
    }

    protected void setResult(T result) {
        this.result = result;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        whenContextAdded();
    }
    // endregion
}
