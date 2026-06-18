/**
 * Copyright 2006-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.mybatis.generator.codegen.ibatis2.model;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansField;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansGetter;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansSetter;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;

/**
 * Generates a Base class for iBatis.
 *
 * @author Jeff Butler
 *
 */
public class BaseRecordGenerator extends AbstractJavaGenerator {

  public BaseRecordGenerator() {
    super();
  }

  @Override
  public List<CompilationUnit> getCompilationUnits() {
    FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
    this.progressCallback.startTask(getString("Progress.8", table.toString())); //$NON-NLS-1$
    Plugin plugins = this.context.getPlugins();
    CommentGenerator commentGenerator = this.context.getCommentGenerator();

    TopLevelClass topLevelClass = new TopLevelClass(this.introspectedTable.getBaseRecordType());
    topLevelClass.setVisibility(JavaVisibility.PUBLIC);
    commentGenerator.addJavaFileComment(topLevelClass);

    FullyQualifiedJavaType superClass = this.getSuperClass();
    if (superClass != null) {
      topLevelClass.setSuperClass(superClass);
      topLevelClass.addImportedType(superClass);
    }

    List<IntrospectedColumn> introspectedColumns;
    if (this.includePrimaryKeyColumns()) {
      if (this.includeBLOBColumns()) {
        introspectedColumns = this.introspectedTable.getAllColumns();
      } else {
        introspectedColumns = this.introspectedTable.getNonBLOBColumns();
      }
    } else {
      if (this.includeBLOBColumns()) {
        introspectedColumns = this.introspectedTable.getNonPrimaryKeyColumns();
      } else {
        introspectedColumns = this.introspectedTable.getBaseColumns();
      }
    }

    String rootClass = this.getRootClass();
    for (IntrospectedColumn introspectedColumn : introspectedColumns) {
      if (RootClassInfo.getInstance(rootClass, this.warnings)
          .containsProperty(introspectedColumn)) {
        continue;
      }

      Field field = getJavaBeansField(introspectedColumn, this.context, this.introspectedTable);
      if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn,
          this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
        topLevelClass.addField(field);
        topLevelClass.addImportedType(field.getType());
      }

      Method method = getJavaBeansGetter(introspectedColumn, this.context, this.introspectedTable);
      if (plugins.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn,
          this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
        topLevelClass.addMethod(method);
      }

      method = getJavaBeansSetter(introspectedColumn, this.context, this.introspectedTable);
      if (plugins.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn,
          this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
        topLevelClass.addMethod(method);
      }
    }

    List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
    if (this.context.getPlugins().modelBaseRecordClassGenerated(topLevelClass,
        this.introspectedTable)) {
      answer.add(topLevelClass);
    }
    return answer;
  }

  private FullyQualifiedJavaType getSuperClass() {
    FullyQualifiedJavaType superClass;
    if (this.introspectedTable.getRules().generatePrimaryKeyClass()) {
      superClass = new FullyQualifiedJavaType(this.introspectedTable.getPrimaryKeyType());
    } else {
      String rootClass = this.getRootClass();
      if (rootClass != null) {
        superClass = new FullyQualifiedJavaType(rootClass);
      } else {
        superClass = null;
      }
    }

    return superClass;
  }

  private boolean includePrimaryKeyColumns() {
    return !this.introspectedTable.getRules().generatePrimaryKeyClass()
        && this.introspectedTable.hasPrimaryKeyColumns();
  }

  private boolean includeBLOBColumns() {
    return !this.introspectedTable.getRules().generateRecordWithBLOBsClass()
        && this.introspectedTable.hasBLOBColumns();
  }
}
