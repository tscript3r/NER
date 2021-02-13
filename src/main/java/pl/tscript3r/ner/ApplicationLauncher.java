package pl.tscript3r.ner;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import pl.tscript3r.ner.fx.ApplicationFX;

@SpringBootApplication
@EnableCaching
public class ApplicationLauncher {

    public static void main(String[] args) {
        Application.launch(ApplicationFX.class, args);
    }

}
