package custom_controls;

/**
 * A custom text field which restricts the text that can be entered to positive integers and decimals.
 *
 * Created by Joseph Billingsley on 15/03/2016.
 */
public class DecimalTextField extends IntegerTextField {
    @Override
    protected boolean validate(String text) {
        // Allows decimal values
        return super.validate(text) || (text.equals("") && !getText().contains("") && !getText().isEmpty());
    }
}
