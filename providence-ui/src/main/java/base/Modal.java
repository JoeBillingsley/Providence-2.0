package base;

/**
 * Provides the basic structure for a modal dialogue to be used for a view.
 *
 * Created by Joseph Billingsley on 24/02/2016.
 */
public abstract class Modal<T> extends Controller<T> {
    @Override
    protected boolean isModal() {
        return true;
    }
}
