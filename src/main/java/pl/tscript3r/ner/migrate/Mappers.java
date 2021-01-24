package pl.tscript3r.ner.migrate;

import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.ner.client.ClientEntity;
import pl.tscript3r.ner.item.ItemEntity;
import pl.tscript3r.ner.order.OrderEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    static Function<ResultSet, OrderEntity> orderEntityMapper = resultSet -> {
        var orderEntity = new OrderEntity();
        orderEntity.setDateOfReceipt(dateExceptionSuppressor(resultSet, 5));
        orderEntity.setDescription(stringExceptionSuppressor(resultSet, 9));
        var stringDate = stringExceptionSuppressor(resultSet, 10);
        if (stringDate != null && !stringDate.isEmpty()) {
            stringDate = normalizeStringDate(stringDate);
            try {
                orderEntity.setDateOfCompletion(LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("dd.MM.yy")));
            } catch (DateTimeException e) {
                log.error(e.getMessage(), e);
            }
        }
        return orderEntity;
    };

    private static String normalizeStringDate(String input) {
        return Arrays.stream(input.split("\\."))
                .map(s -> s.length() < 2 ? "0" + s : s.length() >= 3 ? s.substring(s.length() - 2) : s)
                .collect(Collectors.joining("."));
    }

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

    private static LocalDate dateExceptionSuppressor(ResultSet resultSet, int column) {
        try {
            return resultSet.getDate(column).toLocalDate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
