package lab.client.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import lab.client.mvc.view.VisualisationView;

public class VisualisationController extends Controller {
    
    private VisualisationView view = new VisualisationView();

    @FXML
    private Pane visualisation_view_canvas_pane;
    @FXML
    private ScrollPane visualisation_view_canvas_box;

    private VisualisationController() {

    }

    private static VisualisationController controller;

    public static VisualisationController getInstance() {
        if (controller == null)
            controller = new VisualisationController();
        else
            controller.view.resume();
        return controller;
    }

    @Override
    public void stop() {
        view.stop();
    }

    @FXML
    public void initialize() {
        view.initialize(visualisation_view_canvas_pane, visualisation_view_canvas_box);
    }

}
