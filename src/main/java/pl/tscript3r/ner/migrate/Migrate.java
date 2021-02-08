package pl.tscript3r.ner.migrate;

import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.ner.client.ClientImported;
import pl.tscript3r.ner.item.ItemExternalRelations;
import pl.tscript3r.ner.item.ItemImported;
import pl.tscript3r.ner.migrate.utils.Progress;
import pl.tscript3r.ner.order.OrderImported;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

import static pl.tscript3r.ner.migrate.Mappers.*;

@Slf4j
public class Migrate {

    public static final String CLIENTS_TABLE = "Kunden";
    public static final String ITEMS_TABLE = "Teile";
    public static final String ORDERS_TABLE = "Arbeitsaufträge";
    public static final String ITEM_RELATIONS_TABLE = "Arbeitsauftragsteile";

    private final TableMigrate tableMigrate;
    private final Connection connection;

    private Migrate(Connection connection, Progress progress) {
        this.tableMigrate = new TableMigrate(connection, progress);
        this.connection = connection;
    }

    public static Migrate connect(String file, Progress progress) throws SQLException {
        log.info("Creating connection to DB");
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + file);
        return new Migrate(connection, progress);
    }

    public void init() throws SQLException {
        tableMigrate.sumRows(CLIENTS_TABLE, ORDERS_TABLE, ITEMS_TABLE, ITEM_RELATIONS_TABLE);
    }

    public void migrateClients(Consumer<ClientImported> clientEntityConsumer) throws SQLException {
        tableMigrate.migrate(CLIENTS_TABLE, clientMapper, clientEntityConsumer);
    }

    public void migrateOrders(Consumer<OrderImported> orderImportedConsumer) throws SQLException {
        tableMigrate.migrate(ORDERS_TABLE, orderMapper, orderImportedConsumer);
    }

    public void migrateItems(Consumer<ItemImported> itemImportedConsumer) throws SQLException {
        tableMigrate.migrate(ITEMS_TABLE, itemEntityMapper, itemImportedConsumer);
    }

    public void migrateItemRelations(Consumer<ItemExternalRelations> itemExternalRelationsConsumer) throws SQLException {
        tableMigrate.migrate(ITEM_RELATIONS_TABLE, itemRelationsMapper, itemExternalRelationsConsumer);
    }

    public void cleanUp() throws SQLException {
        if (!connection.isClosed())
            connection.close();
    }

}
