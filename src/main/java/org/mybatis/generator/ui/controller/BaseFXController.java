package org.mybatis.generator.ui.controller;

import java.io.IOException;
import java.net.URL;

import org.mybatis.generator.ui.view.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public abstract class BaseFXController implements Initializable {
  private static final Logger LOG = LoggerFactory.getLogger(BaseFXController.class);

  private Stage primaryStage;
  private Stage dialogStage;

  public BaseFXController loadFXMLPage(String title, FXMLPage fxmlPage) {
    URL skeletonResource =
        Thread.currentThread().getContextClassLoader().getResource(fxmlPage.getFxml());
    FXMLLoader loader = new FXMLLoader(skeletonResource);
    try {
      Parent loginNode = loader.load();
      BaseFXController controller = loader.getController();
      this.dialogStage = new Stage();
      this.dialogStage.setTitle(title);
      this.dialogStage.initModality(Modality.APPLICATION_MODAL);
      this.dialogStage.initOwner(this.getPrimaryStage());
      this.dialogStage.setScene(new Scene(loginNode));
      this.dialogStage.setMaximized(false);
      this.dialogStage.setResizable(false);
      this.dialogStage.show();
      controller.setDialogStage(this.dialogStage);
      return controller;
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
      AlertUtil.showErrorAlert(e.getMessage());
    }
    return null;
  }

  public Stage getPrimaryStage() {
    return this.primaryStage;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public Stage getDialogStage() {
    return this.dialogStage;
  }

  public void setDialogStage(Stage dialogStage) {
    this.dialogStage = dialogStage;
  }

  public void showDialogStage() {
    if (this.dialogStage != null) {
      this.dialogStage.show();
    }
  }

  public void closeDialogStage() {
    if (this.dialogStage != null) {
      this.dialogStage.close();
    }
  }
}
