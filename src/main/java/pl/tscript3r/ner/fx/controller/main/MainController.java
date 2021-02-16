package pl.tscript3r.ner.fx.controller.main;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.fx.util.SearchFacade;
import pl.tscript3r.ner.fx.util.SearchTextField;
import pl.tscript3r.ner.order.OrderEntity;

import java.time.LocalDate;

@Component
public class MainController implements DisposableBean {

    private final int TABLE_ROWS = 200;
    private double lastSliderValue = -1;

    private final SearchFacade searchFacade;
    private SearchTextField searchField;

    @FXML
    private TextField textField;

    @FXML
    private TableView<OrderEntity> tableView;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Slider slider;

    public MainController(SearchFacade searchFacade) {
        this.searchFacade = searchFacade;
    }

    @FXML
    public void initialize() {
        searchField = new SearchTextField(textField, this::updateRows);
        addColumns();
        addInitialRows();
        initializeSlider();
    }

    private void addColumns() {
        tableView.getColumns().add(getOrderColumns());
        tableView.getColumns().add(getClientColumns());
    }

    private TableColumn<OrderEntity, String> getClientColumns() {
        TableColumn<OrderEntity, String> customer = getColumn("Kunde");
        TableColumn<OrderEntity, String> company = getColumn("Firma");
        company.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getCompany())
        );
        TableColumn<OrderEntity, String> contact = getColumn("Kontaktperson");
        contact.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getContactName())
        );
        TableColumn<OrderEntity, String> street = getColumn("Strasse");
        street.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getStreet())
        );
        TableColumn<OrderEntity, String> city = getColumn("Ort");
        city.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getCity())
        );
        TableColumn<OrderEntity, String> postcode = getColumn("Postleitzahl");
        postcode.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getPostcode())
        );
        customer.getColumns().addAll(company, contact, street, city, postcode);
        return customer;
    }

    private TableColumn<OrderEntity, String> getColumn(String columnDisplayName) {
        return new TableColumn<>(columnDisplayName);
    }

    private TableColumn<OrderEntity, String> getOrderColumns() {
        TableColumn<OrderEntity, String> order = getColumn("Auftrag");
        TableColumn<OrderEntity, String> dateOfReceipt = getColumn("Datum des Erhalts");
        dateOfReceipt.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(date2String(cellData.getValue().getDateOfReceipt()))
        );
        TableColumn<OrderEntity, String> description = getColumn("Beschreibung");
        description.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDescription())
        );
        TableColumn<OrderEntity, String> dateOfCompletion = getColumn("Datum der Erledigung");
        dateOfCompletion.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(date2String(cellData.getValue().getDateOfCompletion()))
        );
        order.getColumns().addAll(dateOfReceipt, dateOfCompletion, description);
        return order;
    }

    private String date2String(LocalDate date) {
        return date != null ? date.toString() : "";
    }

    private void addInitialRows() {
        searchFacade.latest(progressIndicator, tableView);
    }

    private void updateRows(String s) {
        if (s.isEmpty())
            searchFacade.latest(progressIndicator, tableView);
        else
            searchFacade.search(s, progressIndicator, tableView);
    }

    @Override
    public void destroy() {
        searchField.destroy();
    }

    private void initializeSlider() {
        slider.setValue(TABLE_ROWS);
        slider.setMin(100);
        slider.setValue(200);
        slider.setMax(500);
        slider.setOnMouseClicked(event -> {
            if (lastSliderValue != slider.getValue()) {
                searchFacade.setRowLimit(slider.getValue());
                updateRows(textField.getText());
                lastSliderValue = slider.getValue();
            }
        });
    }

}
