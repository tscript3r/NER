package pl.tscript3r.ner.migrate;

import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.ner.client.ClientEntity;
import pl.tscript3r.ner.items.ItemEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Slf4j
public final class Mappers {

    static Function<ResultSet, ClientEntity> clientEntityMapper = resultSet -> {
        var clientEntity = new ClientEntity();
        clientEntity.setExternalId(integerExceptionSuppressor(resultSet, 1));
        clientEntity.setCompany(stringExceptionSuppressor(resultSet, 2));
        clientEntity.setContactName(stringExceptionSuppressor(resultSet, 4));
        clientEntity.setStreet(stringExceptionSuppressor(resultSet, 5));
        clientEntity.setCity(stringExceptionSuppressor(resultSet, 6));
        clientEntity.setState(stringExceptionSuppressor(resultSet, 7));
        clientEntity.setPostcode(stringExceptionSuppressor(resultSet, 8));
        clientEntity.setCountry(stringExceptionSuppressor(resultSet, 9));
        return clientEntity;
    };

    static Function<ResultSet, ItemEntity> itemEntityMapper = resultSet -> {
        var itemEntity = new ItemEntity();
        itemEntity.setExternalId(integerExceptionSuppressor(resultSet, 1));
        itemEntity.setName(stringExceptionSuppressor(resultSet, 2));
        itemEntity.setPrice(integerExceptionSuppressor(resultSet, 3));
        itemEntity.setDescription(stringExceptionSuppressor(resultSet, 4));
        return itemEntity;
    };

    private static String stringExceptionSuppressor(ResultSet resultSet, int column) {
        try {
            return resultSet.getString(column);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    private static Integer integerExceptionSuppressor(ResultSet resultSet, int column) {
        try {
            return resultSet.getInt(column);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }

}
