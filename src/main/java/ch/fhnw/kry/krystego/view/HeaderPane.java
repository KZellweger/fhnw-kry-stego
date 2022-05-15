package ch.fhnw.kry.krystego.view;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class HeaderPane extends AnchorPane implements View {

    private final Button importContainmentImage = new Button("Import Containment");
    private final Button importImage = new Button("Import Image");
    private final Button hideImage = new Button("Hide Image");
    private final Button revealImage = new Button("Reveal Image");
    private final Button saveResult = new Button("Save Result");
    private final HBox hBox = new HBox(importImage, importContainmentImage, hideImage, saveResult, revealImage);
    private final Stage stage = new Stage();
    private final FileChooser fc = new FileChooser();
    private final RootPanel rootPanel;
    public HeaderPane(RootPanel rootPanel) {
        this.rootPanel = rootPanel;
        init();
    }

    @Override
    public void initializeControls() {
        importImage.setId("IMAGE");
        importContainmentImage.setId("CONTAINMENT");
        revealImage.setId("UNVEIL");
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(5);
        getChildren().add(hBox);
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png", "*.jpg"));
    }

    @Override
    public void setupEventHandlers()
    {
        importContainmentImage.setOnAction(this::loadImageAction);
        importImage.setOnAction(this::loadImageAction);
        revealImage.setOnAction( e -> {
            File f = fc.showOpenDialog(stage);
            if(f != null){
                try {
                    rootPanel.revealImage(ImageIO.read(f));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        hideImage.setOnAction(e -> rootPanel.hideImage());
        saveResult.setOnAction(e -> {
            File f = fc.showSaveDialog(stage);
            if(f != null){
                rootPanel.saveResult(f);
            }
        });
    }

    private void loadImageAction(ActionEvent event){
        File f = fc.showOpenDialog(stage);
        if(f != null){
            try {
                String btnId = ((Button)event.getSource()).getId();
                rootPanel.addImage(ImageIO.read(f), btnId);
            } catch (IOException e) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setHeaderText("Could not load image");
                err.setContentText(e.getMessage());
                err.showAndWait();
            }
        }
    }
}
