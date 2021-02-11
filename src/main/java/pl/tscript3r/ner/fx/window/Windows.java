package pl.tscript3r.ner.fx.window;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import static javafx.stage.StageStyle.TRANSPARENT;
import static javafx.stage.StageStyle.UNIFIED;

@Getter
public enum Windows {

    MAIN("main/Main", "NER", 600, 600, UNIFIED),
    IMPORT_DIALOG("dialogs/ImportDialog", "Import", 170, 500, TRANSPARENT),
    IMPORT_FILE("dialogs/ImportFile", "Import", 224, 600, TRANSPARENT),
    IMPORT_PROGRESS("dialogs/ImportProgress", "Import", 100, 500, TRANSPARENT);

    private final String uri;
    private final String title;
    private final Integer height;
    private final Integer width;
    private final StageStyle stageStyle;

    Windows(String uri, String title, Integer height, Integer width, StageStyle stageStyle) {
        this.uri = uri;
        this.title = title;
        this.height = height;
        this.width = width;
        this.stageStyle = stageStyle;
    }

    public String getUrl() {
        return "classpath:/fx/" + uri + ".fxml";
    }

    public String getTitle() {
        return title;
    }

    public void applyCustomStyling(Stage stage, Scene scene) {
        switch (this) {
            case MAIN:
                stage.setMaximized(true);
                break;
            case IMPORT_DIALOG:
                stage.initModality(Modality.APPLICATION_MODAL);
                break;
        }
    }

}
