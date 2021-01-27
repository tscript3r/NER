package pl.tscript3r.ner.fx.controller.main;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import jfxtras.styles.jmetro.JMetroStyleClass;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    private final HostServices hostServices;

    @FXML
    public GridPane gridPane;

    @FXML
    public ComboBox<String> searchComboBox;

    public MainController(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize() {
        gridPane.getStyleClass().add(JMetroStyleClass.BACKGROUND);
    }

}
