package pl.tscript3r.ner.fx.window;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class LoadingWindow {

    private final Stage stage;

    public LoadingWindow() {
        this.stage = buildStage(buildScene());
    }

    public void show() {
        stage.show();
    }

    private Stage buildStage(Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(getIcon());
        stage.initStyle(StageStyle.UNDECORATED);
        return stage;
    }

    private Scene buildScene() {
        Image image1 = getIcon();
        ImageView imageView = new ImageView(image1);
        imageView.setOpacity(0.9);
        ProgressBar progressBar = new ProgressBar();
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color: #ffffff;");
        p.setStyle("-fx-border-color: #dadada");
        p.setCenter(imageView);
        p.setBottom(progressBar);
        Scene scene = new Scene(p, 250, 250);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setAutomaticallyColorPanes(true);
        jMetro.setScene(scene);
        return scene;
    }

    private Image getIcon() {
        return new Image("/icon.png", 150, 150, true, true, true);
    }

    public void hide() {
        stage.hide();
        stage.close();
    }

}
