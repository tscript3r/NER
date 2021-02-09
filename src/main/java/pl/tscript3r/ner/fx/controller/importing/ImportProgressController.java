package pl.tscript3r.ner.fx.controller.importing;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.exception.NERException;
import pl.tscript3r.ner.fx.dispatch.event.ImportEvent;
import pl.tscript3r.ner.fx.dispatch.event.ImportEventType;
import pl.tscript3r.ner.migrate.MigrateFacade;

@Component
@RequiredArgsConstructor
public class ImportProgressController {

    private final MigrateFacade migrateFacade;
    private final ApplicationContext applicationContext;
    private Stage stage;

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }

    public void setup(Stage stage) {
        this.stage = stage;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    migrateFacade.connect(progressBar);
                } catch (Exception e) {
                    throw new NERException(e.getMessage(), e);
                }
                return null;
            }
        };
        task.setOnSucceeded(workerStateEvent -> stage.hide());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        stage.setOnShown(windowEvent -> Platform.runLater(thread::start));
        stage.setOnHidden(windowEvent -> applicationContext.publishEvent(ImportEvent.get(ImportEventType.IMPORTED)));
    }

}
