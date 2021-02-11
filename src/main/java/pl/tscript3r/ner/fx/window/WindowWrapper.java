package pl.tscript3r.ner.fx.window;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WindowWrapper {

    private final Stage stage;
    private final Windows stageType;

}
