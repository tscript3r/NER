package pl.tscript3r.ner.fx.controller.main;

import javafx.application.HostServices;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.fx.util.SearchFacade;
import pl.tscript3r.ner.fx.util.SearchTextField;
import pl.tscript3r.ner.migrate.MigrateFacade;
import pl.tscript3r.ner.order.OrderEntity;

import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class MainController implements DisposableBean {

    private final HostServices hostServices;
    private final MigrateFacade migrateFacade;
    private final SearchFacade searchFacade;
    @FXML
    public TextField textField;

    @FXML
    public GridPane gridPane;
    @FXML
    public TableView<OrderEntity> tableView;
    @FXML
    public ProgressIndicator progressIndicator;
    private SearchTextField searchField;

    public MainController(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") HostServices hostServices,
                          MigrateFacade migrateFacade, SearchFacade searchFacade) {
        this.hostServices = hostServices;
        this.migrateFacade = migrateFacade;
        this.searchFacade = searchFacade;
    }

    @FXML
    public void initialize() throws SQLException {
        searchField = new SearchTextField(textField, this::updateRows);
        gridPane.setStyle("-fx-background-color: #ffffff;");
        addColumns();
        addInitialRows();
    }

    private void addColumns() {
        tableView.getColumns().add(getOrderColumns());
        tableView.getColumns().add(getClientColumns());
    }

    private TableColumn<OrderEntity, String> getClientColumns() {
        var customer = getColumn("Kunde");
        var company = getColumn("Firma");
        company.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getCompany())
        );
        var contact = getColumn("Kontaktperson");
        contact.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getContactName())
        );
        var street = getColumn("Strasse");
        street.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getStreet())
        );
        var city = getColumn("Ort");
        city.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getClient().getCity())
        );
        var postcode = getColumn("Postleitzahl");
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
        var order = getColumn("Auftrag");
        var dateOfReceipt = getColumn("Datum des Erhalts");
        dateOfReceipt.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(date2String(cellData.getValue().getDateOfReceipt()))
        );
        var description = getColumn("Beschreibung");
        description.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDescription())
        );
        var dateOfCompletion = getColumn("Datum der Erledigung");
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
    public void destroy() throws Exception {
        searchField.destroy();
    }

}
