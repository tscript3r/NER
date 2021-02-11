package pl.tscript3r.ner.fx.window;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class StageBuilder {

    private final Image iconImage;

    public StageBuilder(@Value("classpath:/icon.png") Resource icon) throws IOException {
        this.iconImage = new Image(icon.getInputStream());
    }

    Stage build(Windows stage) {
        return build(stage, new Stage());
    }

    Stage build(Windows stage, Stage existingStage) {
        applyStageSettings(existingStage, stage.getTitle());
        existingStage.initStyle(stage.getStageStyle());
        existingStage.setWidth(stage.getWidth());
        existingStage.setHeight(stage.getHeight());
        return existingStage;
    }

    private void applyStageSettings(Stage stage, String title) {
        stage.getIcons().add(iconImage);
        stage.setTitle(title);
    }

}
