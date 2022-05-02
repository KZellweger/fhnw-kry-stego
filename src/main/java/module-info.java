module ch.fhnw.kry.krystego {
    requires javafx.controls;
    requires javafx.fxml;


    opens ch.fhnw.kry.krystego to javafx.fxml;
    exports ch.fhnw.kry.krystego;
}