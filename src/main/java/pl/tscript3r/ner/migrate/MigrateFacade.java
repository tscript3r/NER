package pl.tscript3r.ner.migrate;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.tscript3r.ner.client.ClientEntity;
import pl.tscript3r.ner.client.ClientFacade;
import pl.tscript3r.ner.exception.NERException;
import pl.tscript3r.ner.item.ItemExternalRelations;
import pl.tscript3r.ner.migrate.utils.Progress;
import pl.tscript3r.ner.order.OrderEntity;
import pl.tscript3r.ner.order.OrderFacade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class MigrateFacade {

    private final AtomicBoolean ready = new AtomicBoolean(false);
    private final OrderFacade orderFacade;
    private final ClientFacade clientFacade;
    private final Map<Long, Long> clientExternalId2Local = new ConcurrentHashMap<>();
    private Migrate migrate;
    private final Map<Integer, Long> orderExternalId2Local = new ConcurrentHashMap<>();
    private String absolutePath;

    public void connect(ProgressBar progressBar) {
        try {
            Progress progress = new Progress(d -> Platform.runLater(() -> progressBar.setProgress(d)));
            migrate = Migrate.connect(absolutePath, progress);
            migrate.init();
            Platform.runLater(() -> progressBar.setProgress(0));
            migrateClients();
            migrateOrders();
            migrateItems();
            cleanUp();
            ready.set(true);
        } catch (SQLException e) {
            ready.set(true);
            try {
                cleanUp();
            } catch (SQLException ignored) {
            }
            throw new NERException("Exception during importing data, message: " + e.getMessage(), e);
        }
    }

    public void migrateClients() throws SQLException {
        clientExternalId2Local.clear();
        migrate.migrateClients(clientImported -> {
            clientFacade.save(clientImported.getClientEntity());
            clientExternalId2Local.put(clientImported.getExternalId(), clientImported.getClientEntity().getId());
        });
    }

    public void migrateOrders() throws SQLException {
        migrate.migrateOrders(orderImported -> {
            Optional<ClientEntity> clientEntityOptional =
                    clientFacade.getById(clientExternalId2Local.get(orderImported.getClientId()));
            if (clientEntityOptional.isPresent()) {
                ClientEntity clientEntity = clientEntityOptional.get();
                orderImported.getOrderEntity().setClient(clientEntity);
                OrderEntity savedOrderEntity = orderFacade.save(orderImported.getOrderEntity());
                orderExternalId2Local.put(orderImported.getExternalId(), savedOrderEntity.getId());
            } else
                log.warn("Could not find client ID for order with external ID={}",
                        orderImported.getExternalId());
        });
    }

    public void migrateItems() throws SQLException {
        List<ItemExternalRelations> itemRelations = new ArrayList<>();
        migrate.migrateItemRelations(itemRelations::add);
        migrate.migrateItems(itemImported -> {
            Optional<ItemExternalRelations> itemExternalRelations = findByItemId(itemRelations,
                    itemImported.getExternalId());
            if (itemExternalRelations.isPresent()) {
                Long orderId = orderExternalId2Local.get(itemExternalRelations.get().getOrderExternalId());
                Optional<OrderEntity> orderEntity = orderFacade.getById(orderId);
                if (orderEntity.isPresent()) {
                    orderEntity.get().addItem(itemImported.getItemEntity());
                    orderFacade.save(orderEntity.get());
                } else
                    log.warn("Could not find orderEntity with local ID={}", itemImported.getExternalId());
            } else
                log.warn("Could not find item with external ID={}", itemImported.getExternalId());
        });
    }

    private Optional<ItemExternalRelations> findByItemId(List<ItemExternalRelations> itemRelations, Integer itemId) {
        return itemRelations.stream()
                .filter(itemExternalRelations -> itemExternalRelations.getItemExternalId().equals(itemId))
                .findFirst();
    }

    public void setFileUrl(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean isReady() {
        return ready.get();
    }

    public void cleanUp() throws SQLException {
        clientExternalId2Local.clear();
        orderExternalId2Local.clear();
        migrate.cleanUp();
    }

}
