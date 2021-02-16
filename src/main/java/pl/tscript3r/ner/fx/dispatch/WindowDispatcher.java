package pl.tscript3r.ner.fx.dispatch;

import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.config.ConfigurationFacade;
import pl.tscript3r.ner.fx.controller.importing.ImportProgressController;
import pl.tscript3r.ner.fx.dispatch.event.AbstractEvent;
import pl.tscript3r.ner.fx.dispatch.event.ImportEvent;
import pl.tscript3r.ner.fx.dispatch.event.StageReadyEvent;
import pl.tscript3r.ner.fx.window.WindowContainer;
import pl.tscript3r.ner.fx.window.Windows;

import static pl.tscript3r.ner.fx.window.Windows.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WindowDispatcher implements ApplicationListener<AbstractEvent<?>> {

    private final static Windows START_STAGE = MAIN;
    private final WindowContainer windowContainer;
    private final ConfigurationFacade configurationFacade;
    private final ImportProgressController importProgressController;
    private Stage initialStage;

    public Long show(Windows window) {
        Long id = windowContainer.build(window);
        windowContainer.show(id);
        return id;
    }

    public Long show(Windows window, Stage existingStage) {
        Long id = windowContainer.build(existingStage, window);
        windowContainer.show(id);
        return id;
    }

    @Override
    public void onApplicationEvent(AbstractEvent abstractEvent) {
        if (abstractEvent instanceof StageReadyEvent)
            handleStageReadyEvent((StageReadyEvent) abstractEvent);

        if (abstractEvent instanceof ImportEvent)
            handleImportEvent((ImportEvent) abstractEvent);
    }

    private void handleStageReadyEvent(StageReadyEvent stageReadyEvent) {
        if (configurationFacade.isFirstStart()) {
            initialStage = stageReadyEvent.get();
            show(IMPORT_DIALOG);
        } else
            show(START_STAGE, stageReadyEvent.get());
    }

    private void handleImportEvent(ImportEvent importEvent) {
        switch (importEvent.get()) {
            case ABORT_IMPORT:
            case DIALOG_DENIED:
                windowContainer.hide(IMPORT_DIALOG);
                windowContainer.hide(IMPORT_FILE);
                if (initialStage != null)
                    show(START_STAGE, initialStage);
                else
                    show(START_STAGE);
                break;
            case DIALOG_AGREED:
                windowContainer.hide(IMPORT_DIALOG);
                show(IMPORT_FILE);
                break;
            case START_IMPORT:
                windowContainer.hide(IMPORT_FILE);
                Long id = windowContainer.build(IMPORT_PROGRESS);
                importProgressController.setup(windowContainer.getStageById(id));
                windowContainer.show(id);
                break;
            case IMPORTED:
                windowContainer.hide(IMPORT_PROGRESS);
                show(START_STAGE);
                break;
        }
    }

}
