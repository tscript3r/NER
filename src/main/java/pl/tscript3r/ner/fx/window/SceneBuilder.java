package pl.tscript3r.ner.fx.window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@RequiredArgsConstructor
class SceneBuilder {

    private final ApplicationContext applicationContext;

    Scene build(Windows stage) throws IOException {
        Parent parent = loadFXML(getResource(stage.getUrl()));
        Scene scene = new Scene(parent);
        applySceneStyling(scene);
        return scene;
    }

    private void applySceneStyling(Scene scene) {
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setAutomaticallyColorPanes(true);
        jMetro.setScene(scene);
    }

    private Parent loadFXML(Resource fxml) throws IOException {
        URL url = fxml.getURL();
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        return fxmlLoader.load();
    }

    private Resource getResource(String url) {
        return applicationContext.getResource(url);
    }

}
