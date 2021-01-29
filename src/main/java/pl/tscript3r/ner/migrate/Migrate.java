package pl.tscript3r.ner.migrate;

import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.ner.client.ClientEntity;
import pl.tscript3r.ner.migrate.utils.Progress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

import static pl.tscript3r.ner.migrate.Mappers.clientEntityMapper;

@Slf4j
public class Migrate {

    public static final String CLIENTS_TABLE = "Kunden";
    public static final String ITEMS_TABLE = "Teile";
    public static final String ORDERS_TABLE = "Arbeitsaufträge";
    private final TableMigrate tableMigrate;

    private Migrate(Connection connection, Progress progress) {
        this.tableMigrate = new TableMigrate(connection, progress);
    }

    public static Migrate connect(String file, Progress progress) throws SQLException {
        log.info("Creating connection to DB");
        var connection = DriverManager.getConnection("jdbc:ucanaccess://" + file);
        return new Migrate(connection, progress);
    }

    public void init() throws SQLException {
        tableMigrate.sumRows(CLIENTS_TABLE, ITEMS_TABLE, ORDERS_TABLE);
    }

    public void migrateClients(Consumer<ClientEntity> clientEntityConsumer) throws SQLException {
        tableMigrate.migrate(CLIENTS_TABLE, clientEntityMapper, clientEntityConsumer);
    }

}
