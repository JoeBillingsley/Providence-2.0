import controller.base.Context;
import controller.base.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The entry point of the system. Loads the first scene.
 * <p>
 * Created by Joseph Billingsley on 20/10/2015.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Controller controller = new Launcher();
        controller.setStage(stage);
        controller.setContext(new Context());
        controller.loadScene("ModelLibrary");

        stage.show();
    }

    private class Launcher extends Controller {
        @Override
        protected void initialise() throws Exception {}

        @Override
        protected void whenContextAdded() {}
    }
}
