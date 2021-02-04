package pl.tscript3r.ner.fx.stage;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class LoadingStage {

    private final Stage stage;

    public LoadingStage() {
        this.stage = buildStage(buildScene());
    }

    public void show() {
        stage.show();
    }

    private Stage buildStage(Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        return stage;
    }

    private Scene buildScene() {
        Image image1 = new Image("/icon.png", 200, 200, true, true, true);
        ImageView imageView = new ImageView(image1);
        ProgressBar progressBar = new ProgressBar();
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color: #ffffff;");
        p.setCenter(imageView);
        p.setBottom(progressBar);
        Scene scene = new Scene(p, 250, 250);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setAutomaticallyColorPanes(true);
        jMetro.setScene(scene);
        return scene;
    }

    public void hide() {
        stage.hide();
        stage.close();
    }

}
