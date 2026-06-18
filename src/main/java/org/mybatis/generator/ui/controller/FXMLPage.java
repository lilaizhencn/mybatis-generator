package org.mybatis.generator.ui.controller;

public enum FXMLPage {
  NEW_CONNECTION("fxml/newConnection.fxml"), //
  GENERATOR_CONFIG("fxml/generatorConfigs.fxml"),//
  ;

  private String fxml;

  FXMLPage(String fxml) {
    this.fxml = fxml;
  }

  public String getFxml() {
    return this.fxml;
  }
}
