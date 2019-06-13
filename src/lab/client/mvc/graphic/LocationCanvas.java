package lab.client.mvc.graphic;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lab.locations.Location;
import lab.locations.position.CoordsPair;
import lab.objects.items.Item;

import java.util.ArrayList;

public class LocationCanvas extends Canvas {

    private static int itemWidth = 30;
    private static int itemHeight = 30;
    private static int backgroundImageWidth = 30;
    private static int backgroundImageHeight = 30;
    private static int animationPathHeight = 50;
    private static float animationSpeed = 0.3F;

    private Timeline timeline;
    private AnimationTimer timer;
    private int animationDelay;

    private Location visualisingLocation;

    public LocationCanvas(Location visualisingLocation, int animationDelay) {
        this.animationDelay = animationDelay;
        this.visualisingLocation = visualisingLocation;
        draw();
    }

    public Location getLocation() {
        return visualisingLocation;
    }

    public void draw() {
        int width = visualisingLocation.getPosition().getRightTopPoint().getX() - visualisingLocation.getPosition().getLeftBottomPoint().getX();
        int height = visualisingLocation.getPosition().getRightTopPoint().getY() - visualisingLocation.getPosition().getLeftBottomPoint().getY();

        int x = visualisingLocation.getPosition().getLeftBottomPoint().getX();
        int y = 9999 - visualisingLocation.getPosition().getRightTopPoint().getY();

        this.setWidth(width);
        this.setHeight(height);
        this.setLayoutX(x);
        this.setLayoutY(y);
        GraphicsContext context = this.getGraphicsContext2D();

        String backgroundPath = "/resources/img/backgrounds/location_background_" + (int)(Math.random()*5 + 1) + ".png";
        Image backgroundImage = new Image(backgroundPath);
        int ownerHashCode = Math.abs(visualisingLocation.getOwner().hashCode());
        Color borderColor = new Color(ownerHashCode % 256 / 100, (ownerHashCode / 4) % 256 / 100, (ownerHashCode / 8) % 256 / 100, 1);

        DoubleProperty animationX = new SimpleDoubleProperty();
        DoubleProperty animationY = new SimpleDoubleProperty();

        ArrayList<CoordsPair> startPositions = new ArrayList<>();
        for (Item item : visualisingLocation.getItems()) {
            startPositions.add(new CoordsPair((int)(Math.random()*(width - itemWidth)), (int)(Math.random()*(height - itemHeight - animationPathHeight))));
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 0; i < width; i = i + backgroundImageWidth){
                    for (int j = 0; j < height; j = j + backgroundImageHeight){
                        context.drawImage(backgroundImage, i, j, backgroundImageWidth, backgroundImageHeight);
                    }
                }
                context.setStroke(borderColor);
                context.setLineWidth(5);
                context.strokeLine(0 , 0, 0, height);
                context.strokeLine(0, height, width, height);
                context.strokeLine(width, height, width, 0);
                context.strokeLine(width, 0, 0, 0);

                for (int k = 0; k < visualisingLocation.getItems().size(); k++) {
                    Image itemImage  = visualisingLocation.getItems().get(0).getIcon();
                    context.drawImage(itemImage, animationX.doubleValue() + startPositions.get(k).getX(), animationY.doubleValue() + startPositions.get(k).getY(), itemWidth, itemHeight);
                }


            }
        };
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(animationX, 0),
                        new KeyValue(animationY, 0)
                ),
                new KeyFrame(Duration.seconds(animationSpeed),
                        new KeyValue(animationX, 0),
                        new KeyValue(animationY, animationPathHeight)
                )
        );
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(animationDelay * animationSpeed)));
        delay.setOnFinished(event -> {timer.start();});
        timeline.setDelay(new Duration(animationDelay * 1000 * animationSpeed));
        timeline.setOnFinished(event -> {timer.stop();});
        this.timer = timer;
        this.timeline = timeline;
        delay.play();
        timeline.play();
    }

}
