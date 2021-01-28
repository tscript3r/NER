package pl.tscript3r.ner.fx.util;

import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SearchComboBox<T> {

    private final ComboBox<T> comboBox;
    private Function<String, List<T>> onValueChanged;
    private Consumer<T> onSelected;

    public SearchComboBox(ComboBox<T> comboBox, Function<String, List<T>> onValueChanged, Consumer<T> onSelected) {
        this.comboBox = comboBox;
        this.onValueChanged = onValueChanged;
        this.onSelected = onSelected;
    }

}
