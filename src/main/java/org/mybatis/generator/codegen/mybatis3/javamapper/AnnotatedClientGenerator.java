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
package org.mybatis.generator.codegen.mybatis3.javamapper;

import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedCountByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedInsertSelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleSelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeySelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

public class AnnotatedClientGenerator extends JavaMapperGenerator {

  public AnnotatedClientGenerator() {
    super(false);
  }

  @Override
  protected void addCountByExampleMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateCountByExample()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedCountByExampleMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addDeleteByExampleMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateDeleteByExample()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedDeleteByExampleMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedDeleteByPrimaryKeyMethodGenerator(false);
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addInsertMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateInsert()) {
      AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedInsertMethodGenerator(false);
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addInsertSelectiveMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateInsertSelective()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedInsertSelectiveMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addSelectByExampleWithBLOBsMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedSelectByExampleWithBLOBsMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addSelectByExampleWithoutBLOBsMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedSelectByExampleWithoutBLOBsMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedSelectByPrimaryKeyMethodGenerator(false, false);
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addUpdateByExampleSelectiveMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedUpdateByExampleSelectiveMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addUpdateByExampleWithBLOBsMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedUpdateByExampleWithBLOBsMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addUpdateByExampleWithoutBLOBsMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedUpdateByPrimaryKeySelectiveMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
      AbstractJavaMapperMethodGenerator methodGenerator =
          new AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator(false);
      this.initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
  }

  @Override
  public List<CompilationUnit> getExtraCompilationUnits() {
    boolean useLegacyBuilder = false;

    String prop = this.context.getJavaClientGeneratorConfiguration()
        .getProperty(PropertyRegistry.CLIENT_USE_LEGACY_BUILDER);
    if (StringUtility.stringHasValue(prop)) {
      useLegacyBuilder = Boolean.valueOf(prop);
    }
    SqlProviderGenerator sqlProviderGenerator = new SqlProviderGenerator(useLegacyBuilder);
    sqlProviderGenerator.setContext(this.context);
    sqlProviderGenerator.setIntrospectedTable(this.introspectedTable);
    sqlProviderGenerator.setProgressCallback(this.progressCallback);
    sqlProviderGenerator.setWarnings(this.warnings);
    return sqlProviderGenerator.getCompilationUnits();
  }

  @Override
  public AbstractXmlGenerator getMatchedXMLGenerator() {
    // No XML required by the annotated client
    return null;
  }
}
