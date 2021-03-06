package pl.tscript3r.ner.fx;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import pl.tscript3r.ner.ApplicationLauncher;
import pl.tscript3r.ner.fx.dispatch.event.StageReadyEvent;
import pl.tscript3r.ner.fx.window.LoadingWindow;

public class ApplicationFX extends Application {

    private ConfigurableApplicationContext context;
    private LoadingWindow loadingWindow;

    @Override
    public void init() {
        ApplicationContextInitializer<GenericApplicationContext> initializer = genericApplicationContext -> {
            genericApplicationContext.registerBean(Application.class, () -> ApplicationFX.this);
            genericApplicationContext.registerBean(Parameters.class, this::getParameters);
            genericApplicationContext.registerBean(HostServices.class, this::getHostServices);
        };
        Platform.runLater(() -> {
            loadingWindow = new LoadingWindow();
            loadingWindow.show();
        });
        this.context = new SpringApplicationBuilder().sources(ApplicationLauncher.class)
                .initializers(initializer)
                .build().run(getParameters()
                        .getRaw()
                        .toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) {
        this.context.publishEvent(StageReadyEvent.get(primaryStage));
        if (loadingWindow != null)
            loadingWindow.hide();
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }

}