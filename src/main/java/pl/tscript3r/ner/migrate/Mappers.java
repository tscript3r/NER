package pl.tscript3r.ner.migrate;

import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.ner.client.ClientEntity;
import pl.tscript3r.ner.client.ClientImported;
import pl.tscript3r.ner.item.ItemEntity;
import pl.tscript3r.ner.item.ItemExternalRelations;
import pl.tscript3r.ner.item.ItemImported;
import pl.tscript3r.ner.order.OrderEntity;
import pl.tscript3r.ner.order.OrderImported;

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

    static Function<ResultSet, ClientImported> clientMapper = resultSet -> {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setCompany(stringExceptionSuppressor(resultSet, 2));
        clientEntity.setContactName(stringExceptionSuppressor(resultSet, 4));
        clientEntity.setStreet(stringExceptionSuppressor(resultSet, 5));
        clientEntity.setCity(stringExceptionSuppressor(resultSet, 6));
        clientEntity.setState(stringExceptionSuppressor(resultSet, 7));
        clientEntity.setPostcode(stringExceptionSuppressor(resultSet, 8));
        clientEntity.setCountry(stringExceptionSuppressor(resultSet, 9));
        return new ClientImported(longExceptionSuppressor(resultSet, 1), clientEntity);
    };

    static Function<ResultSet, ItemImported> itemEntityMapper = resultSet -> {
        ItemEntity itemEntity = new ItemEntity();
        Integer externalId = integerExceptionSuppressor(resultSet, 1);
        ItemImported itemImported = new ItemImported(externalId, itemEntity);
        itemEntity.setName(stringExceptionSuppressor(resultSet, 2));
        itemEntity.setPrice(integerExceptionSuppressor(resultSet, 3));
        itemEntity.setDescription(stringExceptionSuppressor(resultSet, 4));
        return itemImported;
    };

    static Function<ResultSet, OrderImported> orderMapper = resultSet -> {
        OrderEntity orderEntity = new OrderEntity();
        OrderImported oderImported = new OrderImported(integerExceptionSuppressor(resultSet, 1),
                longExceptionSuppressor(resultSet, 2), orderEntity);
        orderEntity.setDateOfReceipt(dateExceptionSuppressor(resultSet, 5));
        String description = stringExceptionSuppressor(resultSet, 9);
        if (description != null && !description.isEmpty())
            orderEntity.setDescription(description.replaceAll("[^A-Za-z0-9. ,]", ""));
        String stringDate = stringExceptionSuppressor(resultSet, 10);
        if (stringDate != null && !stringDate.isEmpty()) {
            stringDate = normalizeStringDate(stringDate);
            try {
                orderEntity.setDateOfCompletion(LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("dd.MM.yy")));
            } catch (DateTimeException e) {
                log.error("Could not parse date for order with external ID: {}, error message: {}",
                        oderImported.getExternalId(), e.getMessage());
            }
        }
        return oderImported;
    };

    static Function<ResultSet, ItemExternalRelations> itemRelationsMapper = resultSet -> {
        Integer itemId = integerExceptionSuppressor(resultSet, 3);
        Integer quantity = integerExceptionSuppressor(resultSet, 4);
        Integer orderId = integerExceptionSuppressor(resultSet, 2);
        return new ItemExternalRelations(orderId, itemId, quantity);
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

    private static Long longExceptionSuppressor(ResultSet resultSet, int column) {
        try {
            return resultSet.getLong(column);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return -1L;
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
