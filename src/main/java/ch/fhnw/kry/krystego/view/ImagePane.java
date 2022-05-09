package ch.fhnw.kry.krystego.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;


public class ImagePane extends Pane implements View {

    private final Label imageSize = new Label();
    private final Label name = new Label();
    private final ImageView imageView = new ImageView();
    private final HBox hb = new HBox(name, imageSize);
    private final VBox vb = new VBox(hb, imageView);
    private BufferedImage rawImage;

    public ImagePane(BufferedImage rawImage, String id) {
        this.rawImage = rawImage;
        Image image = SwingFXUtils.toFXImage(rawImage, null);
        name.setText(id);
        imageSize.setText(getImageSize(image));
        imageView.setImage(image);
        init();
    }

    @Override
    public void initializeControls() {
        imageView.setFitHeight(600);
        imageView.setFitWidth(960);
        imageView.setPreserveRatio(true);

        hb.setPadding(new Insets(10));
        hb.setSpacing(6);
        vb.setPadding(new Insets(10));
        vb.setSpacing(6);

        hb.setAlignment(Pos.CENTER_LEFT);
        getChildren().add(vb);
    }

    public BufferedImage getRawImage(){
        return rawImage;
    }

    public void updateImage(BufferedImage raw){
        Image image = SwingFXUtils.toFXImage(raw, null);
        imageView.setImage(image);
    }

    private String getImageSize(Image image){
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("Width: ")
                .append(image.getWidth())
                .append(" Height: ")
                .append(image.getHeight())
                .toString();
    }
}
