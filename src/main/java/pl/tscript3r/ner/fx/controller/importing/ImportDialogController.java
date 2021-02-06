package pl.tscript3r.ner.fx.controller.importing;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.fx.dispatch.event.ImportEvent;
import pl.tscript3r.ner.fx.dispatch.event.ImportEventType;

import static pl.tscript3r.ner.fx.dispatch.event.ImportEventType.DIALOG_AGREED;
import static pl.tscript3r.ner.fx.dispatch.event.ImportEventType.DIALOG_DENIED;

@Component
@RequiredArgsConstructor
public class ImportDialogController {

    private final ApplicationContext applicationContext;

    @FXML
    private Button disagreeButton;

    @FXML
    private Button agreeButton;

    @FXML
    public void initialize() {
        agreeButton.requestFocus();
        disagreeButton.setOnMouseClicked(mouseEvent -> importData(false));
        agreeButton.setOnMouseClicked(mouseEvent -> importData(true));
    }

    private void importData(boolean decision) {
        ImportEventType importEventType = decision ? DIALOG_AGREED : DIALOG_DENIED;
        applicationContext.publishEvent(ImportEvent.get(importEventType));
    }

}