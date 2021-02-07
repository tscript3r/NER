package pl.tscript3r.ner.fx.controller.importing;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.fx.dispatch.event.ImportEvent;
import pl.tscript3r.ner.migrate.MigrateFacade;

import java.io.File;

import static pl.tscript3r.ner.fx.dispatch.event.ImportEventType.START_IMPORT;

@Component
@RequiredArgsConstructor
public class ImportFileController {

    private final ApplicationContext applicationContext;
    private final MigrateFacade migrateFacade;

    @Getter
    @Setter
    private Window parent;

    @FXML
    private TextField fileTextField;

    @FXML
    private Button openFileDialogButton;

    @FXML
    private Button abortButton;

    @FXML
    private Button importButton;

    private FileChooser fileChooser;

    private File file;

    @FXML
    public void initialize() {
        abortButton.setDisable(false);
        importButton.setDisable(true);
        openFileDialogButton.setDisable(false);
        openFileDialogButton.requestFocus();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("MS Access dokument (*.mdb)", "*.mdb");
        fileChooser.getExtensionFilters()
                .add(extFilter);
        openFileDialogButton.setOnMouseClicked(this::onOpenFileDialog);
        abortButton.setOnMouseClicked(this::onAbort);
        importButton.setOnMouseClicked(this::onImport);
    }

    private void onOpenFileDialog(MouseEvent mouseEvent) {
        File chosenFile = fileChooser.showOpenDialog(parent);
        if (chosenFile != null && chosenFile.exists()) {
            fileTextField.setText(chosenFile.getAbsolutePath());
            importButton.setDisable(false);
            file = chosenFile;
        } else {
            importButton.setDisable(true);
            fileChooser.setTitle("");
            file = null;
        }
    }

    private void onAbort(MouseEvent mouseEvent) {
        abortButton.setDisable(true);
        importButton.setDisable(true);
        openFileDialogButton.setDisable(true);
        SpringApplication.exit(applicationContext, () -> 0);
        Platform.exit();
    }

    private void onImport(MouseEvent mouseEvent) {
        abortButton.setDisable(true);
        importButton.setDisable(true);
        migrateFacade.setFileUrl(file.getAbsolutePath());
        applicationContext.publishEvent(ImportEvent.get(START_IMPORT));
    }

}
