package ch.fhnw.kry.krystego.view;

import ch.fhnw.kry.krystego.controller.StegoCalculator;
import ch.fhnw.kry.krystego.controller.StegoRevealer;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RootPanel extends BorderPane {

    private final FlowPane flowPane = new FlowPane();
    private final ScrollPane scrollPane = new ScrollPane();

    public RootPanel() {
        super();
        HeaderPane headerPane = new HeaderPane(this);
        flowPane.setOrientation(Orientation.HORIZONTAL);
        scrollPane.setContent(flowPane);
        setTop(headerPane);
        setCenter(scrollPane);
        setMargin(headerPane, new Insets(10));
        this.setPrefSize(1000, 800);
    }


    public void addImage(BufferedImage read, String btnId) {
        ImagePane imagePane = new ImagePane(read, btnId);
        imagePane.setId(btnId);
        ObservableList<Node> nodes = flowPane.getChildren();
        flowPane.getChildren().stream()
                .filter(n -> n.getId().equals(btnId))
                .findFirst()
                .ifPresentOrElse(n -> {
                    int idx = nodes.indexOf(n);
                    nodes.set(idx, imagePane);
                }, () -> {
                    nodes.add(imagePane);
                });
    }

    public void hideImage(){
        ObservableList<Node> nodes = flowPane.getChildren();
        Node imageNode = nodes.stream().filter(n -> n.getId().equals("IMAGE")).findFirst().orElseThrow(() -> new IllegalStateException("Cannot hide nonexistent Image"));
        Node containmentNode = nodes.stream().filter(n -> n.getId().equals("CONTAINMENT")).findFirst().orElseThrow(() -> new IllegalStateException("Cannot hide into nonexistent Image"));
        BufferedImage image = ((ImagePane) imageNode).getRawImage();
        BufferedImage containment = ((ImagePane) containmentNode).getRawImage();
        StegoCalculator calculator = new StegoCalculator(image, containment, this::updateResultImage);
        Thread th = new Thread(calculator);
        th.start();
    }

    public void revealImage(BufferedImage hidden){
        StegoRevealer reveal = new StegoRevealer(hidden, this::updateResultImage);
        Thread th = new Thread(reveal);
        th.start();
    }

    public void updateResultImage(BufferedImage bufferedImage){
        addImage(bufferedImage, "RESULT");
    }

    public void saveResult(File f) {
        flowPane.getChildren().stream()
                .filter(n -> n.getId().equals("RESULT"))
                .findFirst()
                .ifPresent(n -> {
                    ImagePane pane = (ImagePane) n;
                    BufferedImage raw = pane.getRawImage();
                    try {
                        ImageIO.write(raw, "jpg", f);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
