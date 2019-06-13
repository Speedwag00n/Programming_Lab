package lab.client.mvc.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lab.client.LocationsContainer;
import lab.client.mvc.controller.Controllers;
import lab.client.mvc.controller.LocationCreationController;
import lab.client.mvc.graphic.LocationCanvas;
import lab.client.settings.Settings;
import lab.locations.Location;
import lab.server.database.changes.DatabaseChange;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class VisualisationView {

    private Timeline updatingTimeline;
    private Pane visualisationPane;
    private ScrollPane visualisationContainer;

    public void initialize(Pane visualisationPane, ScrollPane visualisationContainer) {

        this.visualisationPane = visualisationPane;
        this.visualisationContainer = visualisationContainer;

        drawAll();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(1000),
                        event -> {
                            DatabaseChange change = LocationsContainer.doChange();
                            if (change != null) {
                                switch (change.getType()) {
                                    case ADDED:
                                        drawLocation(change.getLocations().get(0), 0);
                                        break;
                                    case REMOVED:
                                        deleteLocation(change.getLocations().get(0));
                                        break;
                                    case UPDATED:
                                        deleteLocation(change.getLocations().get(1));
                                        drawLocation(change.getLocations().get(1), 0);
                                        break;
                                }
                            }
                        })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        updatingTimeline = timeline;
    }

    public void stop() {
        clearAll();
        updatingTimeline.stop();
    }

    public void resume() {
        drawAll();
        updatingTimeline.play();
    }

    private void clearAll() {
        visualisationPane.getChildren().clear();
    }

    private void drawAll() {
        visualisationContainer.setVvalue(1.0);
        List<Location> locations = LocationsContainer.getInstance().getLocations();
        int delay = 0;
        for (Location location : locations) {
            drawLocation(location, delay);
            delay++;
        }
    }

    private void drawLocation(Location location, int delay) {
        ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
        LocationCanvas canvas = new LocationCanvas(location, delay);
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("items-list-context-menu");
        MenuItem editItem = new MenuItem(resources.getString("context_menu_edit"));
        editItem.getStyleClass().add("items-list-context-menu-item");
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());
                Stage stage = (Stage) visualisationPane.getScene().getWindow();
                try {
                    Controllers.changeSceneBody("/resources/location_creation_view.fxml", stage, resources.getString("window_title_location_update_page"), new LocationCreationController(canvas.getLocation()));
                }
                catch (IOException e) {

                }
            }
        });
        if (canvas.getLocation().getOwner().equals(Settings.getUsername()))
            contextMenu.getItems().add(editItem);
        canvas.setOnContextMenuRequested(event -> contextMenu.show(canvas.getScene().getWindow(), event.getScreenX(), event.getScreenY()));
        visualisationPane.getChildren().add(canvas);
    }

    public void deleteLocation(Location deletingLocation) {
        for (Node node : visualisationPane.getChildren()) {
            Location location = ((LocationCanvas)node).getLocation();
            if (location.equals(deletingLocation)) {
                visualisationPane.getChildren().remove(node);
                break;
            }
        }
    }

}
