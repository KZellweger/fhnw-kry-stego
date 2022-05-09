package ch.fhnw.kry.krystego.view;

public interface View {
    default void init() {
        initializeSelf();
        initializeControls();
        setupEventHandlers();
    }

    default void initializeSelf(){
    }

    void initializeControls();

    default void setupEventHandlers() {
    }


}
