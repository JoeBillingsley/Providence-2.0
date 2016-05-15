package model_library;

import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The repository where models can be saved and retrieved from.
 * <p>
 * Created by Joseph Billingsley on 19/01/2016.
 */
public class ModelRepository {

    private final ReadOnlyListWrapper<Model> models = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    /**
     * Adds a model to the repository.
     *
     * @param model The model to add.
     */
    public void add(Model model) {
        models.add(model);
    }

    /**
     * Removes a model from the repository.
     *
     * @param index The index of the model to remove.
     */
    public void remove(int index) {
        models.remove(index);
    }

    /**
     * Gets a model from out of the repository.
     *
     * @param index The index of the model to retrieve.
     * @return The model to retrieve.
     */
    public Model get(int index) {
        return models.get(index);
    }

    /**
     * @return An observable list of all models in the repository.
     */
    public ObservableList<Model> getReadOnlyModels() {
        return models.getReadOnlyProperty();
    }
}
