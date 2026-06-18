package org.mybatis.generator.ui.controller;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.mybatis.generator.ui.bridge.MybatisGeneratorBridge;
import org.mybatis.generator.ui.model.DatabaseConfig;
import org.mybatis.generator.ui.model.GeneratorConfig;
import org.mybatis.generator.ui.util.ConfigHelper;
import org.mybatis.generator.ui.util.DbUtils;
import org.mybatis.generator.ui.util.FilenameUtils;
import org.mybatis.generator.ui.util.StringUtils;
import org.mybatis.generator.ui.view.AlertUtil;
import org.mybatis.generator.ui.view.UIProgressCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

@SuppressWarnings("restriction")
public class MainUIController extends BaseFXController {
  private static final Logger LOG = LoggerFactory.getLogger(MainUIController.class);

  @FXML
  private TreeView<String> leftDBTree;
  @FXML
  private ProgressBar progressBar;

  @FXML
  private Label connectionLabel;
  @FXML
  private Label configsLabel;

  @FXML
  private TextField tableNameField;
  @FXML
  private CheckBox shardingCheckBox;
  @FXML
  private TextField deleteTablePre;
  @FXML
  private TextField projectFolderField;

  @FXML
  private CheckBox buildModelCheckBox;
  @FXML
  private CheckBox overrideModelCheckBox;
  @FXML
  private TextField modelTargetPackage;
  @FXML
  private TextField modelTargetProject;

  @FXML
  private CheckBox buildExampleCheckBox;
  @FXML
  private CheckBox overrideExampleCheckBox;
  @FXML
  private TextField exampleTargetPackage;
  @FXML
  private TextField exampleTargetProject;

  @FXML
  private CheckBox buildMapperCheckBox;
  @FXML
  private CheckBox overrideMapperCheckBox;
  @FXML
  private TextField mapperTargetPackage;
  @FXML
  private TextField mapperTargetProject;

  @FXML
  private CheckBox buildXMLCheckBox;
  @FXML
  private CheckBox overrideXMLCheckBox;
  @FXML
  private TextField xmlTargetPackage;
  @FXML
  private TextField xmlTargetProject;

  @FXML
  private CheckBox buildServiceCheckBox;
  @FXML
  private CheckBox overrideServiceCheckBox;
  @FXML
  private TextField serviceTargetPackage;
  @FXML
  private TextField serviceTargetProject;

  @FXML
  private CheckBox buildServiceImplCheckBox;
  @FXML
  private CheckBox overrideServiceImplCheckBox;
  @FXML
  private TextField serviceImplTargetPackage;
  @FXML
  private TextField serviceImplTargetProject;

  @FXML
  private CheckBox buildControllerCheckBox;
  @FXML
  private CheckBox overrideControllerCheckBox;
  @FXML
  private TextField controllerTargetPackage;
  @FXML
  private TextField controllerTargetProject;

  // 打开的项目cache
  private Map<String, TreeItem<?>> openStatusMap = new ConcurrentHashMap<>();
  // Current selected databaseName
  private String databaseName;
  // Current selected databaseConfig
  private DatabaseConfig selectedDatabaseConfig;
  // Current selected tableName
  private String tableName;

  @SuppressWarnings("unchecked")
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ImageView dbImage = new ImageView("icons/computer.png");
    dbImage.setFitHeight(40);
    dbImage.setFitWidth(40);
    this.connectionLabel.setGraphic(dbImage);
    this.connectionLabel.setOnMouseClicked(event -> {
      DbConnectionController controller =
          (DbConnectionController) this.loadFXMLPage("新建数据库连接", FXMLPage.NEW_CONNECTION);
      controller.setMainUIController(this);
      controller.showDialogStage();
    });
    ImageView configImage = new ImageView("icons/config-list.png");
    configImage.setFitHeight(40);
    configImage.setFitWidth(40);
    this.configsLabel.setGraphic(configImage);
    this.configsLabel.setOnMouseClicked(event -> {
      GeneratorConfigController controller =
          (GeneratorConfigController) this.loadFXMLPage("配置", FXMLPage.GENERATOR_CONFIG);
      controller.setMainUIController(this);
      controller.showDialogStage();
    });

    // 默认包名前缀
    this.modelTargetPackage.setText("com.surprising.wallet.xxx.domain");
    this.exampleTargetPackage.setText("com.surprising.wallet.xxx.criteria");
    this.mapperTargetPackage.setText("com.surprising.wallet.xxx.data");
    this.xmlTargetPackage.setText("mybatis.mapper.xxx");
    this.serviceTargetPackage.setText("com.surprising.wallet.xxx.service");
    this.serviceImplTargetPackage.setText("com.surprising.wallet.xxx.service.impl");
    this.controllerTargetPackage.setText("com.surprising.wallet.xxx.rest.controller.admin.v1");

    this.leftDBTree.setShowRoot(false);
    this.leftDBTree.setRoot(new TreeItem<>());
    Callback<TreeView<String>, TreeCell<String>> defaultCellFactory =
        TextFieldTreeCell.forTreeView();
    this.leftDBTree.setCellFactory((TreeView<String> tv) -> {
      TreeCell<String> cell = defaultCellFactory.call(tv);
      cell.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
        // reset
        cell.setContextMenu(null);
        int level = this.leftDBTree.getTreeItemLevel(cell.getTreeItem());
        TreeCell<String> treeCell = (TreeCell<String>) event.getSource();
        TreeItem<String> treeItem = treeCell.getTreeItem();
        if (treeItem == null) {
          return;
        }
        if (level == 1) {
          final ContextMenu contextMenu = new ContextMenu();
          TreeItem<?> openTreeItem = this.openStatusMap.get(treeItem.getValue());
          if (openTreeItem == null) {
            MenuItem itemEdit = new MenuItem("编辑连接");
            itemEdit.setOnAction(event1 -> {
              // reset
              cell.setContextMenu(null);
              DatabaseConfig selectedConfig = (DatabaseConfig) treeItem.getGraphic().getUserData();
              DbConnectionController controller =
                  (DbConnectionController) this.loadFXMLPage("编辑数据库连接", FXMLPage.NEW_CONNECTION);
              controller.setMainUIController(this);
              controller.setConfig(selectedConfig);
              controller.showDialogStage();
            });
            MenuItem itemDelete = new MenuItem("删除连接");
            itemDelete.setOnAction(event1 -> {
              if (AlertUtil.showAndWaitWarnAlert("确认删除？", ButtonType.NO, ButtonType.OK)) {
                // reset
                cell.setContextMenu(null);
                DatabaseConfig selectedConfig =
                    (DatabaseConfig) treeItem.getGraphic().getUserData();
                try {
                  ConfigHelper.deleteDatabaseConfig(selectedConfig);
                  this.loadLeftDBTree();
                } catch (Exception e) {
                  AlertUtil.showErrorAlert("Delete connection failed! Reason: " + e.getMessage());
                }
              }
            });
            contextMenu.getItems().addAll(itemEdit, itemDelete);
          } else {
            MenuItem itemClose = new MenuItem("关闭连接");
            itemClose.setOnAction(event1 -> {
              // reset
              cell.setContextMenu(null);
              if (this.databaseName != null && this.databaseName.equals(treeItem.getValue())) {
                this.databaseName = null;
                this.selectedDatabaseConfig = null;
                this.tableName = null;
                this.tableNameField.setText(null);
              }
              treeItem.setExpanded(false);
              this.openStatusMap.remove(treeItem.getValue());
              treeItem.getChildren().clear();
            });
            contextMenu.getItems().addAll(itemClose);
          }
          cell.setContextMenu(contextMenu);
        }
        if (event.getClickCount() == 2) {
          // reset
          cell.setContextMenu(null);
          if (level == 1) {
            DatabaseConfig selectedConfig = (DatabaseConfig) treeItem.getGraphic().getUserData();
            try {
              List<String> tables = DbUtils.getTableNames(selectedConfig);
              if (tables != null && tables.size() > 0) {
                ObservableList<TreeItem<String>> children = treeItem.getChildren();
                children.clear();
                for (String tableName : tables) {
                  TreeItem<String> newTreeItem = new TreeItem<>();
                  ImageView imageView = new ImageView("icons/table.png");
                  imageView.setFitHeight(16);
                  imageView.setFitWidth(16);
                  newTreeItem.setGraphic(imageView);
                  newTreeItem.setValue(tableName);
                  children.add(newTreeItem);
                }
              } else {
                AlertUtil.showInfoAlert("该数据源没有表");
                return;
              }
              treeItem.setExpanded(true);
              this.openStatusMap.put(treeItem.getValue(), treeItem);
            } catch (Exception e) {
              LOG.error(e.getMessage(), e);
              AlertUtil.showErrorAlert(e.getMessage());
            }
          } else if (level == 2) {
            this.databaseName = treeItem.getParent().getValue();
            this.selectedDatabaseConfig =
                (DatabaseConfig) treeItem.getParent().getGraphic().getUserData();
            String tableName = treeCell.getTreeItem().getValue();
            this.tableName = tableName;
            this.tableNameField.setText(tableName);
          }
        }
      });
      return cell;
    });
    this.progressBar.setProgress(0.0);
    this.loadLeftDBTree();
  }

  void loadLeftDBTree() {
    TreeItem<String> rootTreeItem = this.leftDBTree.getRoot();
    List<TreeItem<String>> treeItemList = rootTreeItem.getChildren();
    Map<String, TreeItem<String>> treeItemMap = new HashMap<>();
    if (treeItemList != null) {
      for (TreeItem<String> treeItem : treeItemList) {
        treeItemMap.put(treeItem.getValue(), treeItem);
      }
    }
    rootTreeItem.getChildren().clear();
    try {
      List<DatabaseConfig> dbConfigs = ConfigHelper.loadDatabaseConfig();
      for (DatabaseConfig dbConfig : dbConfigs) {
        TreeItem<String> treeItem = treeItemMap.remove(dbConfig.getName());
        if (treeItem == null) {
          treeItem = new TreeItem<>();
          treeItem.setValue(dbConfig.getName());
          ImageView dbImage = new ImageView("icons/database.png");
          dbImage.setFitHeight(16);
          dbImage.setFitWidth(16);
          dbImage.setUserData(dbConfig);
          treeItem.setGraphic(dbImage);
        } else {
          treeItem.getGraphic().setUserData(dbConfig);
        }
        rootTreeItem.getChildren().add(treeItem);
      }
      for (String name : treeItemMap.keySet()) {
        this.openStatusMap.remove(name);
      }
    } catch (Exception e) {
      LOG.error("connect db failed, reason: {}", e);
      AlertUtil.showErrorAlert(e.getMessage() + "\n" + StringUtils.getStackTrace(e));
    }
  }

  @FXML
  public void chooseProjectFolder() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedFolder = directoryChooser.showDialog(this.getPrimaryStage());
    if (selectedFolder != null) {
      this.projectFolderField.setText(selectedFolder.getAbsolutePath());
    }
  }

  @FXML
  public void generateCode() {
    if (StringUtils.isEmpty(this.tableName)) {
      AlertUtil.showWarnAlert("请先在左侧选择数据库表");
      return;
    }
    String validate = this.validateConfig();
    if (validate != null) {
      AlertUtil.showErrorAlert(validate);
      return;
    }
    if (!this.buildModelCheckBox.isSelected() && !this.buildExampleCheckBox.isSelected()
        && !this.buildMapperCheckBox.isSelected() && !this.buildXMLCheckBox.isSelected()
        && !this.buildServiceCheckBox.isSelected() && !this.buildServiceImplCheckBox.isSelected()
        && !this.buildControllerCheckBox.isSelected()) {
      AlertUtil.showWarnAlert("请至少选择一个生成类型");
      return;
    }
    GeneratorConfig generatorConfig = this.getGeneratorConfigFromUI();
    String deleteTablePre = generatorConfig.getDeleteTablePre();
    if (!StringUtils.isEmpty(deleteTablePre)
        && !this.tableName.startsWith(deleteTablePre.toLowerCase())) {
      AlertUtil.showWarnAlert("将要去除的名称前缀不合法");
      return;
    }
    if (!this.checkDirs(generatorConfig)) {
      return;
    }

    if (AlertUtil.showAndWaitWarnAlert("确认生成？", ButtonType.NO, ButtonType.OK)) {
      MybatisGeneratorBridge bridge = new MybatisGeneratorBridge();
      bridge.setGeneratorConfig(generatorConfig);
      bridge.setDatabaseConfig(this.selectedDatabaseConfig);
      UIProgressCallback callback = new UIProgressCallback(this.progressBar);
      bridge.setProgressCallback(callback);
      try {
        bridge.generate();
      } catch (Exception e) {
        LOG.error("generate error: ", e);
        AlertUtil.showErrorAlert(e.getMessage());
      }
    }
  }

  private String validateConfig() {
    String projectFolder = this.projectFolderField.getText();
    if (StringUtils.isEmpty(projectFolder)) {
      return "项目目录不能为空";
    }
    if (this.buildModelCheckBox.isSelected()) {
      if (StringUtils.isEmpty(this.modelTargetPackage.getText())) {
        return "实体包名不能为空";
      }
    }
    if (this.buildExampleCheckBox.isSelected()) {
      if (StringUtils.isEmpty(this.exampleTargetPackage.getText())) {
        return "Example包名不能为空";
      }
    }
    if (this.buildMapperCheckBox.isSelected()) {
      if (StringUtils.isEmpty(this.modelTargetPackage.getText())
          || StringUtils.isEmpty(this.exampleTargetPackage.getText())) {
        return "Mapper依赖实体和Example包名";
      }
      if (StringUtils.isEmpty(this.mapperTargetPackage.getText())) {
        return "Mapper包名不能为空";
      }
    }
    if (this.buildXMLCheckBox.isSelected()) {
      if (StringUtils.isEmpty(this.modelTargetPackage.getText())
          || StringUtils.isEmpty(this.mapperTargetPackage.getText())) {
        return "XML依赖实体和Mapper包名";
      }
      if (StringUtils.isEmpty(this.xmlTargetPackage.getText())) {
        return "XML包名不能为空";
      }
    }
    if (this.buildServiceCheckBox.isSelected()) {
      if (StringUtils.isEmpty(this.modelTargetPackage.getText())
          || StringUtils.isEmpty(this.exampleTargetPackage.getText())) {
        return "服务依赖实体和Example包名";
      }
      if (StringUtils.isEmpty(this.serviceTargetPackage.getText())) {
        return "服务包名不能为空";
      }
    }
    if (this.buildServiceImplCheckBox.isSelected()) {
      if (StringUtils.isEmpty(this.modelTargetPackage.getText())
          || StringUtils.isEmpty(this.exampleTargetPackage.getText())
          || StringUtils.isEmpty(this.mapperTargetPackage.getText())
          || StringUtils.isEmpty(this.serviceTargetPackage.getText())) {
        return "服务实现依赖实体、Example、Mapper和服务包名";
      }
      if (StringUtils.isEmpty(this.serviceImplTargetPackage.getText())) {
        return "服务实现包名不能为空";
      }
    }
    if (this.buildControllerCheckBox.isSelected()) {
      if (StringUtils.isEmpty(this.modelTargetPackage.getText())
          || StringUtils.isEmpty(this.exampleTargetPackage.getText())
          || StringUtils.isEmpty(this.serviceTargetPackage.getText())) {
        return "Controller依赖实体、Example和服务包名";
      }
      if (StringUtils.isEmpty(this.controllerTargetPackage.getText())) {
        return "Controller包名不能为空";
      }
    }
    return null;
  }

  @FXML
  public void saveGeneratorConfig() {
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("保存当前配置");
    dialog.setContentText("请输入配置名称");
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      String name = result.get();
      if (StringUtils.isEmpty(name) || StringUtils.containsWhitespace(name)) {
        AlertUtil.showErrorAlert(name + "格式不正确");
        return;
      }
      LOG.info("user choose config name: {}", name);
      try {
        GeneratorConfig generatorConfig = this.getGeneratorConfigFromUI();
        generatorConfig.setName(name);
        GeneratorConfig oldGeneratorConfig = ConfigHelper.loadGeneratorConfig(name);
        if (oldGeneratorConfig != null) {
          AlertUtil.showErrorAlert("配置名称" + name + "已存在");
          return;
        }
        ConfigHelper.saveGeneratorConfig(generatorConfig);
      } catch (Exception e) {
        LOG.error("save config error: ", e);
        AlertUtil.showErrorAlert("保存配置失败: " + e.getMessage());
      }
    }
  }

  private GeneratorConfig getGeneratorConfigFromUI() {
    GeneratorConfig generatorConfig = new GeneratorConfig();
    generatorConfig.setTableName(this.tableName);
    generatorConfig.setSharding(this.shardingCheckBox.isSelected());
    generatorConfig.setDeleteTablePre(this.deleteTablePre.getText());
    generatorConfig.setProjectFolder(this.projectFolderField.getText());
    generatorConfig.setBuildModel(this.buildModelCheckBox.isSelected());
    generatorConfig.setOverrideModel(this.overrideModelCheckBox.isSelected());
    generatorConfig.setModelPackage(this.modelTargetPackage.getText());
    generatorConfig.setModelFolder(this.modelTargetProject.getText());

    generatorConfig.setBuildExample(this.buildExampleCheckBox.isSelected());
    generatorConfig.setOverrideExample(this.overrideExampleCheckBox.isSelected());
    generatorConfig.setExamplePackage(this.exampleTargetPackage.getText());
    generatorConfig.setExampleFolder(this.exampleTargetProject.getText());

    generatorConfig.setBuildMapper(this.buildMapperCheckBox.isSelected());
    generatorConfig.setOverrideMapper(this.overrideMapperCheckBox.isSelected());
    generatorConfig.setMapperPackage(this.mapperTargetPackage.getText());
    generatorConfig.setMapperFolder(this.mapperTargetProject.getText());

    generatorConfig.setBuildXML(this.buildXMLCheckBox.isSelected());
    generatorConfig.setOverrideXML(this.overrideXMLCheckBox.isSelected());
    generatorConfig.setXmlPackage(this.xmlTargetPackage.getText());
    generatorConfig.setXmlFolder(this.xmlTargetProject.getText());

    generatorConfig.setBuildService(this.buildServiceCheckBox.isSelected());
    generatorConfig.setOverrideService(this.overrideServiceCheckBox.isSelected());
    generatorConfig.setServicePackage(this.serviceTargetPackage.getText());
    generatorConfig.setServiceFolder(this.serviceTargetProject.getText());

    generatorConfig.setBuildServiceImpl(this.buildServiceImplCheckBox.isSelected());
    generatorConfig.setOverrideServiceImpl(this.overrideServiceImplCheckBox.isSelected());
    generatorConfig.setServiceImplPackage(this.serviceImplTargetPackage.getText());
    generatorConfig.setServiceImplFolder(this.serviceImplTargetProject.getText());

    generatorConfig.setBuildController(this.buildControllerCheckBox.isSelected());
    generatorConfig.setOverrideController(this.overrideControllerCheckBox.isSelected());
    generatorConfig.setControllerPackage(this.controllerTargetPackage.getText());
    generatorConfig.setControllerFolder(this.controllerTargetProject.getText());

    return generatorConfig;
  }

  public void setGeneratorConfigIntoUI(GeneratorConfig generatorConfig) {
    this.deleteTablePre.setText(generatorConfig.getDeleteTablePre());
    this.shardingCheckBox.setSelected(generatorConfig.isSharding());
    this.projectFolderField.setText(generatorConfig.getProjectFolder());

    this.buildModelCheckBox.setSelected(generatorConfig.isBuildModel());
    this.overrideModelCheckBox.setSelected(generatorConfig.isOverrideModel());
    this.modelTargetPackage.setText(generatorConfig.getModelPackage());
    this.modelTargetProject.setText(generatorConfig.getModelFolder());

    this.buildExampleCheckBox.setSelected(generatorConfig.isBuildExample());
    this.overrideExampleCheckBox.setSelected(generatorConfig.isOverrideExample());
    this.exampleTargetPackage.setText(generatorConfig.getExamplePackage());
    this.exampleTargetProject.setText(generatorConfig.getExampleFolder());

    this.buildMapperCheckBox.setSelected(generatorConfig.isBuildMapper());
    this.overrideMapperCheckBox.setSelected(generatorConfig.isOverrideMapper());
    this.mapperTargetPackage.setText(generatorConfig.getMapperPackage());
    this.mapperTargetProject.setText(generatorConfig.getMapperFolder());

    this.buildXMLCheckBox.setSelected(generatorConfig.isBuildXML());
    this.overrideXMLCheckBox.setSelected(generatorConfig.isOverrideXML());
    this.xmlTargetPackage.setText(generatorConfig.getXmlPackage());
    this.xmlTargetProject.setText(generatorConfig.getXmlFolder());

    this.buildServiceCheckBox.setSelected(generatorConfig.isBuildService());
    this.overrideServiceCheckBox.setSelected(generatorConfig.isOverrideService());
    this.serviceTargetPackage.setText(generatorConfig.getServicePackage());
    this.serviceTargetProject.setText(generatorConfig.getServiceFolder());

    this.buildServiceImplCheckBox.setSelected(generatorConfig.isBuildServiceImpl());
    this.overrideServiceImplCheckBox.setSelected(generatorConfig.isOverrideServiceImpl());
    this.serviceImplTargetPackage.setText(generatorConfig.getServiceImplPackage());
    this.serviceImplTargetProject.setText(generatorConfig.getServiceImplFolder());

    this.buildControllerCheckBox.setSelected(generatorConfig.isBuildController());
    this.overrideControllerCheckBox.setSelected(generatorConfig.isOverrideController());
    this.controllerTargetPackage.setText(generatorConfig.getControllerPackage());
    this.controllerTargetProject.setText(generatorConfig.getControllerFolder());
  }

  /**
   * 检查并创建不存在的文件夹
   */
  private boolean checkDirs(GeneratorConfig config) {
    if (!this.checkDirs(FilenameUtils.normalize(config.getProjectFolder()), "项目目录")) {
      return false;
    }
    if (config.isBuildModel() && !StringUtils.isEmpty(config.getModelFolder())) {
      if (!this.checkDirs(
          FilenameUtils.normalize(
              config.getProjectFolder().concat(File.separator).concat(config.getModelFolder())),
          "实体目录")) {
        return false;
      }
    }
    if (config.isBuildExample() && !StringUtils.isEmpty(config.getExampleFolder())) {
      if (!this.checkDirs(
          FilenameUtils.normalize(
              config.getProjectFolder().concat(File.separator).concat(config.getExampleFolder())),
          "Example目录")) {
        return false;
      }
    }
    if (config.isBuildMapper() && !StringUtils.isEmpty(config.getMapperFolder())) {
      if (!this.checkDirs(
          FilenameUtils.normalize(
              config.getProjectFolder().concat(File.separator).concat(config.getMapperFolder())),
          "Mapper目录")) {
        return false;
      }
    }
    if (config.isBuildXML() && !StringUtils.isEmpty(config.getXmlFolder())) {
      if (!this.checkDirs(
          FilenameUtils.normalize(
              config.getProjectFolder().concat(File.separator).concat(config.getXmlFolder())),
          "XML目录")) {
        return false;
      }
    }
    if (config.isBuildService() && !StringUtils.isEmpty(config.getServiceFolder())) {
      if (!this.checkDirs(
          FilenameUtils.normalize(
              config.getProjectFolder().concat(File.separator).concat(config.getServiceFolder())),
          "服务目录")) {
        return false;
      }
    }
    if (config.isBuildServiceImpl() && !StringUtils.isEmpty(config.getServiceImplFolder())) {
      if (!this.checkDirs(FilenameUtils.normalize(
          config.getProjectFolder().concat(File.separator).concat(config.getServiceImplFolder())),
          "服务实现目录")) {
        return false;
      }
    }
    if (config.isBuildController() && !StringUtils.isEmpty(config.getControllerFolder())) {
      if (!this.checkDirs(FilenameUtils.normalize(
          config.getProjectFolder().concat(File.separator).concat(config.getControllerFolder())),
          "Controller目录")) {
        return false;
      }
    }
    return true;
  }

  private boolean checkDirs(String folder, String tip) {
    File file = new File(folder);
    if (!file.exists()) {
      if (AlertUtil.showAndWaitInfoAlert("不存在，是否创建？", ButtonType.NO, ButtonType.OK)) {
        try {
          FilenameUtils.forceMkdir(new File(folder));
        } catch (Exception e) {
          AlertUtil.showErrorAlert("创建: " + tip + "失败: " + e.getMessage());
          return false;
        }
      } else {
        return false;
      }
    }
    return true;
  }

  @FXML
  public void openTargetFolder() {
    String projectFolder = this.projectFolderField.getText();
    if (StringUtils.isEmpty(projectFolder)) {
      AlertUtil.showErrorAlert("请先选择项目目录");
      return;
    }
    File file = new File(FilenameUtils.normalize(projectFolder));
    if (!file.exists()) {
      AlertUtil.showErrorAlert("项目目录不存在");
      return;
    }
    try {
      Desktop.getDesktop().browse(new File(FilenameUtils.normalize(projectFolder)).toURI());
    } catch (Exception e) {
      AlertUtil.showErrorAlert("打开目录失败: " + e.getMessage());
    }
  }
}
