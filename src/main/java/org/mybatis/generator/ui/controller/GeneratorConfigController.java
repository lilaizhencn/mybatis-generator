package org.mybatis.generator.ui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.mybatis.generator.ui.model.GeneratorConfig;
import org.mybatis.generator.ui.util.ConfigHelper;
import org.mybatis.generator.ui.view.AlertUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

@SuppressWarnings("restriction")
public class GeneratorConfigController extends BaseFXController {
  @FXML
  private TableView<GeneratorConfig> configTable;
  @FXML
  private TableColumn<Object, Object> nameColumn;
  @FXML
  private TableColumn<Object, Object> opsColumn;

  private MainUIController mainUIController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    // 自定义操作列
    this.opsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    this.opsColumn.setCellFactory(cell -> {
      return new TableCell<Object, Object>() {

        @Override
        public void updateItem(Object item, boolean empty) {
          super.updateItem(item, empty);
          if (item == null || empty) {
            this.setText(null);
            this.setGraphic(null);
          } else {
            Button btn1 = new Button("应用");
            Button btn2 = new Button("删除");
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.getChildren().add(btn1);
            hBox.getChildren().add(btn2);
            btn1.setOnAction(event -> {
              try {
                // 应用配置
                GeneratorConfig generatorConfig = ConfigHelper.loadGeneratorConfig(item.toString());
                GeneratorConfigController.this.mainUIController
                    .setGeneratorConfigIntoUI(generatorConfig);
                GeneratorConfigController.this.closeDialogStage();
              } catch (Exception e) {
                AlertUtil.showErrorAlert(e.getMessage());
              }
            });
            btn2.setOnAction(event -> {
              if (AlertUtil.showAndWaitWarnAlert("确认删除？", ButtonType.NO, ButtonType.OK)) {
                try {
                  // 删除配置
                  ConfigHelper.deleteGeneratorConfig(item.toString());
                  GeneratorConfigController.this.refreshTableView();
                } catch (Exception e) {
                  AlertUtil.showErrorAlert(e.getMessage());
                }
              }
            });
            this.setGraphic(hBox);
          }
        }
      };
    });
    this.refreshTableView();
  }

  private void refreshTableView() {
    try {
      List<GeneratorConfig> configs = ConfigHelper.loadGeneratorConfigs();
      this.configTable.setItems(FXCollections.observableList(configs));
    } catch (Exception e) {
      AlertUtil.showErrorAlert(e.getMessage());
    }
  }

  void setMainUIController(MainUIController mainUIController) {
    this.mainUIController = mainUIController;
  }
}
