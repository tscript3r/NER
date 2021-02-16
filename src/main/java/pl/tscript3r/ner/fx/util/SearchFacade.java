package pl.tscript3r.ner.fx.util;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.client.ClientFacade;
import pl.tscript3r.ner.order.OrderEntity;
import pl.tscript3r.ner.order.OrderFacade;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchFacade implements DisposableBean {

    @Setter
    private Double rowLimit = 200D;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final ClientFacade clientFacade;
    private final OrderFacade orderFacade;

    public void latest(ProgressIndicator progressIndicator, TableView<OrderEntity> tableView) {
        executor.submit(() -> Platform.runLater(() -> {
            progressIndicator.setVisible(true);
            tableView.getItems().clear();
            List<OrderEntity> orderEntityList = orderFacade.getLatest(rowLimit);
            tableView.setItems(FXCollections.observableArrayList(orderEntityList));
            fixNotUpdatingTableRowsBug(tableView);
            progressIndicator.setVisible(false);

        }));
    }

    private void fixNotUpdatingTableRowsBug(TableView<OrderEntity> tableView) {
        try {
            TableColumn<?, ?> firstColumn = tableView.getColumns().get(0);
            double width = firstColumn.getWidth();
            firstColumn.setVisible(false);
            firstColumn.setVisible(true);
            firstColumn.setPrefWidth(width);
        } catch (IndexOutOfBoundsException ignored) {
        }
        tableView.refresh();
    }

    public void search(String searchPhrase, ProgressIndicator progressIndicator, TableView<OrderEntity> tableView) {
        executor.submit(() -> {
            progressIndicator.setVisible(true);
            Set<OrderEntity> ordersFromClients = clientFacade.search(searchPhrase, rowLimit)
                    .stream()
                    .flatMap(clientEntity -> orderFacade.getFromClientAndFromPhrase(searchPhrase, clientEntity))
                    .limit(rowLimit.intValue())
                    .collect(Collectors.toSet());
            tableView.getItems().clear();
            tableView.setItems(FXCollections.observableArrayList(ordersFromClients));
            fixNotUpdatingTableRowsBug(tableView);
            progressIndicator.setVisible(false);
        });
    }

    @Override
    public void destroy() {
        executor.shutdownNow();
    }

}
