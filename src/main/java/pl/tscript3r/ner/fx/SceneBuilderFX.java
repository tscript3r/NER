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
import pl.tscript3r.ner.fx.stage.LoadingStage;

public class SceneBuilderFX extends Application {

    private ConfigurableApplicationContext context;
    private LoadingStage loadingStage;

    @Override
    public void init() {
        ApplicationContextInitializer<GenericApplicationContext> initializer = genericApplicationContext -> {
            genericApplicationContext.registerBean(Application.class, () -> SceneBuilderFX.this);
            genericApplicationContext.registerBean(Parameters.class, this::getParameters);
            genericApplicationContext.registerBean(HostServices.class, this::getHostServices);
        };
        Platform.runLater(() -> {
            loadingStage = new LoadingStage();
            loadingStage.show();
        });
        this.context = new SpringApplicationBuilder().sources(ApplicationLauncher.class)
                .initializers(initializer)
                .build().run(getParameters()
                        .getRaw()
                        .toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) {
        this.context.publishEvent(new StageReadyEvent(primaryStage));
        if (loadingStage != null)
            loadingStage.hide();
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }

}