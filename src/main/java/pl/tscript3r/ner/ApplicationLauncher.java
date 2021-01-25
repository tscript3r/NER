package pl.tscript3r.ner;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.tscript3r.ner.fx.SceneBuilderFX;

@SpringBootApplication
public class ApplicationLauncher {

    public static void main(String[] args) {
        Application.launch(SceneBuilderFX.class, args);
    }

}
