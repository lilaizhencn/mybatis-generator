package org.mybatis.generator.ui.util;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.ui.model.DatabaseConfig;
import org.mybatis.generator.ui.model.GeneratorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class ConfigHelper {
  private static final Logger LOG = LoggerFactory.getLogger(ConfigHelper.class);
  private static final String BASE_DIR = "config";
  private static final String CONFIG_FILE = "sqlite3.db";

  public static void init() throws Exception {
    File file = new File(BASE_DIR);
    if (!file.exists()) {
      file.mkdir();
    }
    File uiConfigFile = new File(BASE_DIR + File.separator + CONFIG_FILE);
    if (!uiConfigFile.exists()) {
      initTable(uiConfigFile);
    }
  }

  private static void initTable(File uiConfigFile) throws Exception {
    Connection conn = null;
    Statement stat = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      stat.executeUpdate(
          "create table dbs(id integer primary key autoincrement, name nvarchar(64) unique not null, value ntext not null)");
      stat.executeUpdate(
          "create table generator_config(name nvarchar(64) unique not null, value ntext not null)");
    } catch (Exception e) {
      LOG.error("createTable error: ", e);
      uiConfigFile.delete();
    } finally {
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }

  public static List<DatabaseConfig> loadDatabaseConfig() throws Exception {
    Connection conn = null;
    Statement stat = null;
    ResultSet rs = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      String sql = "SELECT * FROM dbs";
      rs = stat.executeQuery(sql);
      LOG.info("sql: {}", sql);
      List<DatabaseConfig> configs = new ArrayList<>();
      while (rs.next()) {
        int id = rs.getInt("id");
        String value = rs.getString("value");
        DatabaseConfig databaseConfig = JSON.parseObject(value, DatabaseConfig.class);
        databaseConfig.setId(id);
        configs.add(databaseConfig);
      }
      return configs;
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }

  public static void saveDatabaseConfig(DatabaseConfig dbConfig) throws Exception {
    String configName = dbConfig.getName();
    Connection conn = null;
    Statement stat = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      ResultSet rs = stat.executeQuery("SELECT * from dbs where name = '" + configName + "'");
      if (rs.next()) {
        throw new Exception("名称已经存在, 请使用其它名字");
      }
      String jsonStr = JSON.toJSONString(dbConfig);
      String sql =
          String.format("INSERT INTO dbs (name, value) values('%s', '%s')", configName, jsonStr);
      LOG.info("sql: {}", sql);
      stat.executeUpdate(sql);
    } finally {
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }

  public static void updateDatabaseConfig(Integer primaryKey, DatabaseConfig dbConfig)
      throws Exception {
    Connection conn = null;
    Statement stat = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      String jsonStr = JSON.toJSONString(dbConfig);
      String sql = String.format("UPDATE dbs SET name = '%s', value = '%s' where id = %d",
          dbConfig.getName(), jsonStr, primaryKey);
      LOG.info("sql: {}", sql);
      stat.executeUpdate(sql);
    } finally {
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }

  public static void deleteDatabaseConfig(DatabaseConfig databaseConfig) throws Exception {
    Connection conn = null;
    Statement stat = null;
    ResultSet rs = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      String sql = String.format("delete from dbs where id=%d", databaseConfig.getId());
      LOG.info("sql: {}", sql);
      stat.executeUpdate(sql);
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }

  public static void saveGeneratorConfig(GeneratorConfig generatorConfig) throws Exception {
    Connection conn = null;
    Statement stat = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      String jsonStr = JSON.toJSONString(generatorConfig);
      String sql = String.format("INSERT INTO generator_config values('%s', '%s')",
          generatorConfig.getName(), jsonStr);
      stat.executeUpdate(sql);
    } finally {
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }

  public static GeneratorConfig loadGeneratorConfig(String name) throws Exception {
    Connection conn = null;
    Statement stat = null;
    ResultSet rs = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      String sql = String.format("SELECT * FROM generator_config where name='%s'", name);
      LOG.info("sql: {}", sql);
      rs = stat.executeQuery(sql);
      GeneratorConfig generatorConfig = null;
      if (rs.next()) {
        String value = rs.getString("value");
        generatorConfig = JSON.parseObject(value, GeneratorConfig.class);
      }
      return generatorConfig;
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }

  public static List<GeneratorConfig> loadGeneratorConfigs() throws Exception {
    Connection conn = null;
    Statement stat = null;
    ResultSet rs = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      String sql = "SELECT * FROM generator_config";
      LOG.info("sql: {}", sql);
      rs = stat.executeQuery(sql);
      List<GeneratorConfig> configs = new ArrayList<>();
      while (rs.next()) {
        String value = rs.getString("value");
        configs.add(JSON.parseObject(value, GeneratorConfig.class));
      }
      return configs;
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }

  public static int deleteGeneratorConfig(String name) throws Exception {
    Connection conn = null;
    Statement stat = null;
    try {
      conn = ConnectionManager.getConnection();
      stat = conn.createStatement();
      String sql = String.format("DELETE FROM generator_config where name='%s'", name);
      LOG.info("sql: {}", sql);
      return stat.executeUpdate(sql);
    } finally {
      if (stat != null) {
        stat.close();
      }
      if (conn != null) {
        conn.close();
      }
    }
  }
}
