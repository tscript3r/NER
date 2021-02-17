package pl.tscript3r.ner.fx.window;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.exception.NotFoundNERException;
import pl.tscript3r.ner.exception.WindowBuildNERException;
import pl.tscript3r.ner.fx.dispatch.event.StageCreationExceptionEvent;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WindowContainer {

    private final Map<Long, WindowWrapper> windowsMap = new ConcurrentHashMap<>();
    private final ApplicationContext applicationContext;
    private final StageBuilder stageBuilder;
    private final SceneBuilder sceneBuilder;
    private Long nextId = 1000L;

    public Long build(Windows window) {
        return build(new Stage(), window);
    }

    public Long buildModal(Stage parentStage, Windows window) {
        Long id = build(window);
        Stage stage = windowsMap.get(id).getStage();
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        return id;
    }

    public Long build(Stage stage, Windows window) {
        try {
            stage = stageBuilder.build(window, stage);
            Scene scene = sceneBuilder.build(window);
            window.applyCustomStyling(stage, scene);
            stage.setScene(scene);
            return save(stage, window);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            applicationContext.publishEvent(StageCreationExceptionEvent.get(e));
            throw new WindowBuildNERException("Window creation failed with following message: " + e.getMessage(), e);
        }

    }

    private Long save(Stage stage, Windows window) {
        WindowWrapper windowWrapper = new WindowWrapper(stage, window);
        Long id = getNextId();
        windowsMap.put(id, windowWrapper);
        return id;
    }

    private Long getNextId() {
        return nextId++;
    }

    public void show(Long id) {
        Optional<WindowWrapper> windowWrapperOptional = getById(id);
        if (windowWrapperOptional.isPresent()) {
            WindowWrapper windowWrapper = windowWrapperOptional.get();
            windowWrapper.getStage().show();
        } else
            publishNotFoundException(id);
    }

    private void publishNotFoundException(Long id) {
        NotFoundNERException exception = new NotFoundNERException("Could not find window with id=" + id.toString());
        applicationContext.publishEvent(StageCreationExceptionEvent.get(exception));
        throw exception;
    }

    private Optional<WindowWrapper> getById(Long id) {
        if (windowsMap.containsKey(id))
            return Optional.of(windowsMap.get(id));
        return Optional.empty();
    }

    public void hide(Long id) {
        Optional<WindowWrapper> windowWrapperOptional = getById(id);
        if (windowWrapperOptional.isPresent()) {
            WindowWrapper windowWrapper = windowWrapperOptional.get();
            windowWrapper.getStage().hide();
        } else
            log.warn("Requested hiding window with invalid id={}", id);
    }

    public void destroy(Long id) {
        Optional<WindowWrapper> windowWrapperOptional = getById(id);
        if (windowWrapperOptional.isPresent()) {
            WindowWrapper windowWrapper = windowWrapperOptional.get();
            windowWrapper.getStage().hide();
            windowsMap.remove(id);
        } else
            log.warn("Requested destroying window with invalid id={}", id);
    }

    public void hide(Windows window) {
        windowsMap.forEach((key, windowWrapper) -> {
            if (windowWrapper.getStageType() == window)
                windowWrapper.getStage().hide();
        });
    }

    public Stage getStageById(Long id) {
        return windowsMap.get(id).getStage();
    }

    public Optional<Stage> getStageByWindowType(Windows windows) {
        return windowsMap.values().stream()
                .filter(windowWrapper -> windowWrapper.getStageType() == windows)
                .map(WindowWrapper::getStage)
                .findFirst();
    }

    public void showAndWait(Long id) {
        Optional<WindowWrapper> windowWrapperOptional = getById(id);
        if (windowWrapperOptional.isPresent()) {
            WindowWrapper windowWrapper = windowWrapperOptional.get();
            windowWrapper.getStage().showAndWait();
        } else
            publishNotFoundException(id);
    }

}
