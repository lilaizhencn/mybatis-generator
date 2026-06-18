package org.mybatis.generator.ui.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager {
  private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);
  private static final String DB_URL = "jdbc:sqlite:./config/sqlite3.db";

  public static Connection getConnection() throws Exception {
    Class.forName("org.sqlite.JDBC");
    File file = new File(DB_URL.substring("jdbc:sqlite:".length())).getAbsoluteFile();
    LOG.info("database FilePath :{}", file.getAbsolutePath());
    Connection conn = DriverManager.getConnection(DB_URL);
    return conn;
  }
}
