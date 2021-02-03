package pl.tscript3r.ner.fx.util;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import pl.tscript3r.ner.client.ClientFacade;
import pl.tscript3r.ner.order.OrderEntity;
import pl.tscript3r.ner.order.OrderFacade;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchFacade implements DisposableBean {

    private static final int LIMIT = 200;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final ClientFacade clientFacade;
    private final OrderFacade orderFacade;

    public void latest(ProgressIndicator progressIndicator, TableView<OrderEntity> tableView) {
        executor.submit(() -> {
            progressIndicator.setVisible(true);
            tableView.getItems().clear();
            tableView.getItems().addAll(orderFacade.getLatest());
            progressIndicator.setVisible(false);
        });
    }

    public void search(String searchFraze, ProgressIndicator progressIndicator, TableView<OrderEntity> tableView) {
        executor.submit(() -> {
            progressIndicator.setVisible(true);
            var ordersFromClients = clientFacade.search(searchFraze, LIMIT)
                    .stream()
                    .flatMap(clientEntity -> orderFacade.getFromClientAndFromPhrase(searchFraze, clientEntity))
                    .limit(LIMIT)
                    .collect(Collectors.toSet());
            tableView.getItems().clear();
            tableView.getItems().addAll(ordersFromClients);
            progressIndicator.setVisible(false);
        });
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdownNow();
    }
}
