package controller.custom_controls;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Raises an alert with an expandable content area listing detailed information on the exception that occurred.
 * <p>
 * Created by Joseph Billingsley on 13/02/2016.
 * Based on source code by Marko Jakob: http://code.makery.ch/blog/javafx-dialogs-official/
 */
public class ExceptionAlert extends Alert {

    public ExceptionAlert(Exception exception) {
        super(AlertType.ERROR);

        VBox container = new VBox();

        Label label = new Label("The stack trace of the exception was: ");

        TextArea detail = new TextArea("Error");
        detail.setEditable(false);
        detail.setWrapText(true);

        container.getChildren().add(label);
        container.getChildren().add(detail);

        getDialogPane().setExpandableContent(container);

        setHeaderText(null);
        initStyle(StageStyle.UTILITY);

        StringWriter esw = new StringWriter();
        PrintWriter out = new PrintWriter(esw);

        exception.printStackTrace(out);

        detail.setText(esw.toString());
    }
}
