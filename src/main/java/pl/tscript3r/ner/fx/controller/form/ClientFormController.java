package pl.tscript3r.ner.fx.controller.form;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class ClientFormController {

    @FXML
    private TextField contactTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField postcodeTextField;

    @FXML
    private Button abortButton;

    @FXML
    private javafx.scene.control.Button applyButton;

    @FXML
    public void initialize() {

    }

}
