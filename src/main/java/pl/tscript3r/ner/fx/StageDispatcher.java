package pl.tscript3r.ner.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import static pl.tscript3r.ner.fx.StageDispatcher.StageData.MAIN;

@Component
public class StageDispatcher implements ApplicationListener<StageReadyEvent> {

    private final static StageData START_STAGE_RESOURCE = MAIN;
    private final ApplicationContext applicationContext;

    public StageDispatcher(ApplicationContext applicationContext,
                           @Value("classpath:/icon.png") Resource icon) {
        this.applicationContext = applicationContext;
        this.icon = icon;
    }

    private final Resource icon;

    public void show(StageData stageData) {
        show(new Stage(), stageData);
    }

    public void show(Stage stage, StageData stageData) {
        try {
            Parent root = loadFXML(getResource(stageData));
            Scene scene = new Scene(root, 600, 600);
            applySceneStyling(scene);
            applyStageSettings(stage);
            stageData.setup(stageData, stage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Resource getResource(StageData stage) {
        return applicationContext.getResource(stage.getUrl());
    }

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        show(stageReadyEvent.getStage(), START_STAGE_RESOURCE);
    }

    private void applySceneStyling(Scene scene) {
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setAutomaticallyColorPanes(true);
        jMetro.setScene(scene);

    }

    private void applyStageSettings(Stage stage) throws IOException {
        stage.getIcons().add(new Image(icon.getInputStream()));
        stage.initStyle(StageStyle.UNIFIED);
        String applicationTitle = "NER";
        stage.setTitle(applicationTitle);
    }

    private Parent loadFXML(Resource fxml) throws IOException {
        URL url = fxml.getURL();
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        return fxmlLoader.load();
    }

    public enum StageData {

        MAIN("main/Main");

        private final String uri;

        StageData(String uri) {
            this.uri = uri;
        }

        String getUrl() {
            return "classpath:/fx/" + uri + ".fxml";
        }

        void setup(StageData stageData, Stage stage) {
            switch (stageData) {
                case MAIN:
                    stage.setMaximized(true);
            }
        }

    }

}
