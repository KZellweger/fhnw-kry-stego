package ch.fhnw.kry.krystego;

import ch.fhnw.kry.krystego.controller.StegoCalculator;
import ch.fhnw.kry.krystego.view.RootPanel;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent rootPanel = new RootPanel();
        Scene scene = new Scene(rootPanel);

        primaryStage.titleProperty().setValue("Kry-Stego Demo");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
