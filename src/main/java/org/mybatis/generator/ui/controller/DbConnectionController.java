package org.mybatis.generator.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.mybatis.generator.ui.model.DatabaseConfig;
import org.mybatis.generator.ui.util.ConfigHelper;
import org.mybatis.generator.ui.util.DbUtils;
import org.mybatis.generator.ui.util.StringUtils;
import org.mybatis.generator.ui.view.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

@SuppressWarnings("restriction")
public class DbConnectionController extends BaseFXController {
  private static final Logger LOG = LoggerFactory.getLogger(DbConnectionController.class);

  @FXML
  private TextField nameField;
  @FXML
  private TextField hostField;
  @FXML
  private TextField portField;
  @FXML
  private TextField userNameField;
  @FXML
  private TextField passwordField;
  @FXML
  private TextField schemaField;
  @FXML
  private ChoiceBox<String> dbTypeChoice;
  private MainUIController mainUIController;
  private boolean isUpdate = false;
  private Integer primayKey;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 默认选中第一个
    this.dbTypeChoice.getSelectionModel().selectFirst();
    // 根据数据库类型设置默认端口
    this.dbTypeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (StringUtils.isEmpty(this.portField.getText()) || this.portField.getText().equals(DbUtils.getDefaultPort(oldVal != null ? oldVal : null))) {
        this.portField.setText(DbUtils.getDefaultPort(newVal));
      }
    });
  }

  @FXML
  void saveConnection() {
    DatabaseConfig config = this.extractConfigForUI(true);
    if (config == null) {
      return;
    }
    if (StringUtils.isEmpty(config.getName()) || StringUtils.containsWhitespace(config.getName())) {
      AlertUtil.showErrorAlert(config.getName() + "格式不正确");
      return;
    }
    try {
      if (this.isUpdate) {
        ConfigHelper.updateDatabaseConfig(this.primayKey, config);
      } else {
        ConfigHelper.saveDatabaseConfig(config);
      }
      this.getDialogStage().close();
      this.mainUIController.loadLeftDBTree();
    } catch (Exception e) {
      LOG.error("saveConnection error: ", e);
      AlertUtil.showErrorAlert(e.getMessage());
    }
  }

  @FXML
  void testConnection() {
    DatabaseConfig config = this.extractConfigForUI(false);
    if (config == null) {
      return;
    }
    try {
      DbUtils.getConnection(config);
      AlertUtil.showInfoAlert("连接成功");
    } catch (Exception e) {
      LOG.error("DbUtil.getConnection error: ", e);
      AlertUtil.showWarnAlert("连接失败, " + e.getMessage());
    }
  }

  @FXML
  void cancel() {
    this.getDialogStage().close();
  }

  void setMainUIController(MainUIController controller) {
    this.mainUIController = controller;
  }

  private DatabaseConfig extractConfigForUI(boolean checkName) {
    String name = this.nameField.getText();
    String host = this.hostField.getText();
    String port = this.portField.getText();
    String userName = this.userNameField.getText();
    String password = this.passwordField.getText();
    String dbType = this.dbTypeChoice.getValue();
    String schema = this.schemaField.getText();
    DatabaseConfig config = new DatabaseConfig();
    config.setName(name);
    config.setDbType(dbType);
    config.setHost(host);
    config.setPort(port);
    config.setUsername(userName);
    config.setPassword(password);
    config.setSchema(schema);
    if (checkName && StringUtils.isAnyEmpty(name)) {
      AlertUtil.showWarnAlert("名称必填");
      return null;
    }
    if (StringUtils.isAnyEmpty(host, port, userName, dbType, schema)) {
      AlertUtil.showWarnAlert("名称,密码以外其他字段必填");
      return null;
    }
    return config;
  }

  public void setConfig(DatabaseConfig config) {
    this.isUpdate = true;
    this.primayKey = config.getId();
    this.nameField.setText(config.getName());
    this.hostField.setText(config.getHost());
    this.portField.setText(config.getPort());
    this.userNameField.setText(config.getUsername());
    this.passwordField.setText(config.getPassword());
    String dbType = StringUtils.isEmpty(config.getDbType()) ? "MySQL" : config.getDbType();
    this.dbTypeChoice.setValue(dbType);
    this.portField.setText(config.getPort());
    this.schemaField.setText(config.getSchema());
  }
}
