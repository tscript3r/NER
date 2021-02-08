package pl.tscript3r.ner.migrate;

import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.ner.migrate.utils.Progress;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
class TableMigrate {

    private final Progress progress;
    private final Connection connection;

    TableMigrate(Connection connection, Progress progress) {
        this.progress = progress;
        this.connection = connection;
    }

    public void sumRows(String... tables) throws SQLException {
        log.info("Summing total rows count");
        for (String table : tables)
            progress.addTotalCount(getRows(table));
    }

    private int getRows(String table) throws SQLException {
        Statement statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table); // very old DB, COUNT wont work
        resultSet.last();
        int rowsCount = resultSet.getRow();
        resultSet.close();
        return rowsCount;
    }

    public <E> void migrate(String table, Function<ResultSet, E> mapper,
                            Consumer<E> entityConsumer) throws SQLException {
        log.info("Migrating data from table={}", table);
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        log.info("Executing 'SELECT * FROM {}' query", table);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
        resultSet.first();
        while (resultSet.next()) {
            progress.inc();
            entityConsumer.accept(mapper.apply(resultSet));
        }
        resultSet.close();
    }

}
