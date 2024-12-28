package ru.netology.data;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.sql.DriverManager;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.AllArgsConstructor;
import lombok.Data;

public class DataBaseHelper {
    private static Connection connection;
    private static QueryRunner runner;

    @SneakyThrows
    public static void getConnection() {
        runner = new QueryRunner();
        connection = DriverManager.getConnection(System.getProperty("dbUrl"), "app", "pass");
    }

    @SneakyThrows
    public static void setDown() {
        getConnection();
        var sqlUpdateOne = "DELETE FROM credit_request_entity;";
        var sqlUpdateTwo = "DELETE FROM payment_entity;";
        var sqlUpdateThree = "DELETE FROM order_entity;";
        runner.update(connection, sqlUpdateOne);
        runner.update(connection, sqlUpdateTwo);
        runner.update(connection, sqlUpdateThree);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentEntity {
        private String id;
        private int amount;
        private Timestamp created;
        private String status;
        private String transaction_id;
    }

    @SneakyThrows
    public static List<PaymentEntity> getPayments() {
        getConnection();
        var sqlQuery = "SELECT * FROM payment_entity ORDER BY created DESC;";
        ResultSetHandler<List<PaymentEntity>> resultHandler = new BeanListHandler<>(PaymentEntity.class);
        return runner.query(connection, sqlQuery, resultHandler);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreditRequestEntity {
        private String id;
        private String bank_id;
        private Timestamp created;
        private String status;
    }

    @SneakyThrows
    public static List<CreditRequestEntity> getCreditsRequest() {
        getConnection();
        var sqlQuery = "SELECT * FROM credit_request_entity ORDER BY created DESC;";
        ResultSetHandler<List<CreditRequestEntity>> resultHandler = new BeanListHandler<>(CreditRequestEntity.class);
        return runner.query(connection, sqlQuery, resultHandler);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderEntity {
        private String id;
        private Timestamp created;
        private String credit_id;
        private String payment_id;
    }

    @SneakyThrows
    public static List<OrderEntity> getOrders() {
        getConnection();
        var sqlQuery = "SELECT * FROM order_entity ORDER BY created DESC;";
        ResultSetHandler<List<OrderEntity>> resultHandler = new BeanListHandler<>(OrderEntity.class);
        return runner.query(connection, sqlQuery, resultHandler);
    }
}