package pl.tscript3r.ner.fx.dispatch.event;

import javafx.stage.Stage;

public class StageReadyEvent extends AbstractEvent<Stage> {

    private StageReadyEvent(Stage source) {
        super(source);
    }

    public static StageReadyEvent get(Stage stage) {
        return new StageReadyEvent(stage);
    }

}
