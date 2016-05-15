package custom_controls;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * Provides two list views where items can be moved from one to another on double click or on using the control buttons.
 * <p>
 * Created by Joseph Billingsley on 11/01/2016.
 */
public class ListToList<T> extends AnchorPane {

    @FXML private ListView<T> listOne;
    @FXML private ListView<T> listTwo;

    @FXML private Button listOneToListTwo;
    @FXML private Button listTwoToListOne;

    private ArrayList<BiConsumer<ObservableList<T>, ObservableList<T>>> listeners = new ArrayList<>();

    /**
     * Creates an instance of the ListToList control.
     */
    public ListToList() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/custom_controls/ListToList.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addTransferEvent(listOne, listTwo);
        addTransferEvent(listTwo, listOne);

        addClickTransferEvent(listOneToListTwo, listOne, listTwo);
        addClickTransferEvent(listTwoToListOne, listTwo, listOne);

        setCellFactory(view -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null)
                    setText(item.toString());
                else
                    setText("");
            }
        });
    }

    private void addTransferEvent(ListView<T> from, ListView<T> to) {
        from.setOnMouseClicked((event -> {
            if (event.getClickCount() >= 2) {
                moveElement(from, to);
            }
        }));
    }

    private void addClickTransferEvent(Button btn, ListView<T> from, ListView<T> to) {
        btn.setOnAction((event) -> moveElement(from, to));
    }

    private void moveElement(ListView<T> from, ListView<T> to) {
        T selectedItem = from.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
            return;

        from.getItems().remove(selectedItem);
        to.getItems().add(0, selectedItem);

        informChangeListeners();
    }

    public void setListOneValues(ObservableList<T> values) {
        listOne.setItems(values);
        informChangeListeners();
    }

    public void setListTwoValues(ObservableList<T> values) {
        listTwo.setItems(values);
        informChangeListeners();
    }

    private void informChangeListeners() {
        for (BiConsumer<ObservableList<T>, ObservableList<T>> listener : listeners) {
            listener.accept(listOne.getItems(), listTwo.getItems());
        }
    }

    /**
     * Sets the cell factory for both lists.
     *
     * @param cellFactory The cell factory to set.
     */
    public void setCellFactory(Callback<ListView<T>, ListCell<T>> cellFactory) {
        listOne.setCellFactory(cellFactory);
        listTwo.setCellFactory(cellFactory);
    }

    /**
     * Adds a listener that is called when an item is moved from one list to another.
     *
     * @param listener The listener to notify.
     */
    public void addChangeListener(BiConsumer<ObservableList<T>, ObservableList<T>> listener) {
        listeners.add(listener);
    }
}
