package org.mybatis.generator.ui.view;

import org.mybatis.generator.api.ProgressCallback;

import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;

@SuppressWarnings("restriction")
public class UIProgressCallback implements ProgressCallback {
  private ProgressBar progressBar;
  private Alert alert;

  public UIProgressCallback(ProgressBar progressBar) {
    this.progressBar = progressBar;
    this.alert = new Alert(Alert.AlertType.INFORMATION);
    this.alert.setOnCloseRequest(event -> {
      this.progressBar.setProgress(0.0);
    });
  }

  @Override
  public void introspectionStarted(int totalTasks) {
    this.progressBar.setProgress(0.2);
  }

  @Override
  public void generationStarted(int totalTasks) {
    this.progressBar.setProgress(0.4);
  }

  @Override
  public void saveStarted(int totalTasks) {
    this.progressBar.setProgress(0.6);
  }

  @Override
  public void startTask(String taskName) {
    if (taskName.startsWith("Saving file")) {
      this.progressBar.setProgress(0.8);
    }
  }

  @Override
  public void done() {
    this.progressBar.setProgress(1);
    this.alert.setContentText("生成完成");
    this.alert.show();
  }

  @Override
  public void checkCancel() throws InterruptedException {}
}
