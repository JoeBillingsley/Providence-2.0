package controller.custom_controls;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * A modification of the original JavaFX spinner. Commits changes on losing focus of the spinner rather than only when
 * pressing enter.
 * <p>
 * Created by Joseph Billingsley on 07/03/2016.
 * Based on StackOverflow answer 32340476 by kleopatra, 02/09/2015
 */
public class SensibleSpinner<T> extends Spinner<T> {

    public SensibleSpinner() {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) return;

            commitEditorText();
        });
    }

    private void commitEditorText() {
        if (!isEditable())
            return;

        String text = getEditor().getText();

        SpinnerValueFactory<T> valueFactory = getValueFactory();

        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();

            if (converter != null && NumberUtils.isNumber(text)) {
                T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }

}
