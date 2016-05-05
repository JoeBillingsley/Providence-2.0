package controller.custom_controls;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * A custom text field which only allows valid positive integers to be entered.
 * <p>
 * Created by Joseph Billingsley on 15/03/2016.
 */
public class IntegerTextField extends javafx.scene.control.TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text))
            super.replaceText(start, end, text);
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text))
            super.replaceSelection(text);
    }

    protected boolean validate(String text) {
        // Allows numbers and backspace
        return NumberUtils.isNumber(text) || text.equals("");
    }
}
