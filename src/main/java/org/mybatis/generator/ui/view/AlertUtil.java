package org.mybatis.generator.ui.view;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * 弹框
 */
@SuppressWarnings("restriction")
public class AlertUtil {

  public static void showInfoAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText(message);
    alert.show();
  }

  public static void showWarnAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setContentText(message);
    alert.show();
  }

  public static void showErrorAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(message);
    alert.show();
  }

  public static boolean showAndWaitInfoAlert(String message, ButtonType... buttonTypes) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, message, buttonTypes);
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      return true;
    }
    return false;
  }

  public static boolean showAndWaitWarnAlert(String message, ButtonType... buttonTypes) {
    Alert alert = new Alert(Alert.AlertType.WARNING, message, buttonTypes);
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      return true;
    }
    return false;
  }

  public static boolean showAndWaitErrorAlert(String message, ButtonType... buttonTypes) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message, buttonTypes);
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      return true;
    }
    return false;
  }
}
