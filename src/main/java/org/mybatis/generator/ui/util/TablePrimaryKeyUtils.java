package org.mybatis.generator.ui.util;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

public class TablePrimaryKeyUtils {

  public static FullyQualifiedJavaType getPrimaryKeyType(IntrospectedTable introspectedTable) {
    List<IntrospectedColumn> introspectedColumnList = introspectedTable.getPrimaryKeyColumns();
    if (introspectedColumnList == null || introspectedColumnList.size() != 1) {
      throw new RuntimeException("introspectedTable.getPrimaryKeyColumns()'s size is "
          + (introspectedColumnList == null ? "null" : introspectedColumnList.size()));
    }
    IntrospectedColumn introspectedColumn = introspectedColumnList.get(0);
    return introspectedColumn.getFullyQualifiedJavaType();
  }
}
