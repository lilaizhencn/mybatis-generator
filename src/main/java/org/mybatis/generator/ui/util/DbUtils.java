package org.mybatis.generator.ui.util;

import org.mybatis.generator.ui.model.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DbUtils.class);
    private static final int DB_CONNECTION_TIMEOUTS_SECONDS = 1;

    public static Connection getConnection(DatabaseConfig config) throws Exception {
        String url = getConnectionUrlWithSchema(config);

        Properties props = new Properties();
        props.setProperty("user", config.getUsername());
        props.setProperty("password", config.getPassword());

        DriverManager.setLoginTimeout(DB_CONNECTION_TIMEOUTS_SECONDS);

        String driverClass = getDriverClass(config.getDbType());
        Class<?> clazz = Class.forName(driverClass);
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();
        Connection connection = driver.connect(url, props);
        LOG.info("getConnection, connection url: {}", url);
        return connection;
    }

    public static List<String> getTableNames(DatabaseConfig config) throws Exception {
        String url = getConnectionUrlWithSchema(config);
        LOG.info("getTableNames, connection url: {}", url);
        Connection connection = getConnection(config);
        try {
            List<String> tables = new ArrayList<>();
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(config.getSchema(), null, "%", new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                tables.add(rs.getString(3));
            }
            return tables;
        } finally {
            connection.close();
        }
    }

    public static String getConnectionUrlWithSchema(DatabaseConfig dbConfig) {
        String dbType = dbConfig.getDbType();
        String connectionUrl;
        if ("PostgreSQL".equalsIgnoreCase(dbType)) {
            connectionUrl = String.format("jdbc:postgresql://%s:%s/%s",
                    dbConfig.getHost(), dbConfig.getPort(), dbConfig.getSchema());
        } else {
            // MySQL (default for backward compatibility)
            connectionUrl = String.format(
                    "jdbc:mysql://%s:%s/%s?characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=UTC",
                    dbConfig.getHost(), dbConfig.getPort(), dbConfig.getSchema());
        }
        LOG.info("getConnectionUrlWithSchema, connection url: {}", connectionUrl);
        return connectionUrl;
    }

    public static String getDriverClass(String dbType) {
        if ("PostgreSQL".equalsIgnoreCase(dbType)) {
            return "org.postgresql.Driver";
        }
        return "com.mysql.cj.jdbc.Driver";
    }

    public static String getDefaultPort(String dbType) {
        if ("PostgreSQL".equalsIgnoreCase(dbType)) {
            return "5432";
        }
        return "3306";
    }
}
