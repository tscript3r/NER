package pl.tscript3r.ner.fx.util;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SearchTextField {

    private static final long KEY_DELAY = 300;

    @Getter
    private final TextField textField;
    private final Runnable task;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Future<?> latestFuture;
    private String lastValue = "";

    public SearchTextField(TextField textField, Consumer<String> onValueChanged) {
        this.textField = textField;
        textField.setOnKeyPressed(this::onKeyPressed);
        task = () -> {
            String currentValue = textField.getText();
            if (!currentValue.equals(lastValue))
                onValueChanged.accept(textField.getText());
            lastValue = currentValue;
        };
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if (latestFuture != null && !latestFuture.isDone())
            latestFuture.cancel(true);
        latestFuture = executor.schedule(task, KEY_DELAY, TimeUnit.MILLISECONDS);
    }

    public void destroy() {
        executor.shutdownNow();
    }

}
