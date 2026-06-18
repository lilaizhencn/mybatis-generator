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
package org.mybatis.generator.codegen.ibatis2.dao;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.AbstractDAOElementGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.CountByExampleMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.DeleteByExampleMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.DeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.InsertMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.InsertSelectiveMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.SelectByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.SelectByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.SelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.UpdateByExampleParmsInnerclassGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.UpdateByExampleSelectiveMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.UpdateByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.UpdateByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.UpdateByPrimaryKeySelectiveMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.UpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.elements.UpdateByPrimaryKeyWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.templates.AbstractDAOTemplate;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.rules.Rules;

/**
 * DAO generator for iBatis.
 *
 * @author Jeff Butler
 *
 */
public class DAOGenerator extends AbstractJavaClientGenerator {

  private AbstractDAOTemplate daoTemplate;
  private boolean generateForJava5;

  public DAOGenerator(AbstractDAOTemplate daoTemplate, boolean generateForJava5) {
    super(true);
    this.daoTemplate = daoTemplate;
    this.generateForJava5 = generateForJava5;
  }

  @Override
  public List<CompilationUnit> getCompilationUnits() {
    FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
    this.progressCallback.startTask(getString("Progress.14", table.toString())); //$NON-NLS-1$
    TopLevelClass topLevelClass = this.getTopLevelClassShell();
    Interface interfaze = this.getInterfaceShell();

    this.addCountByExampleMethod(topLevelClass, interfaze);
    this.addDeleteByExampleMethod(topLevelClass, interfaze);
    this.addDeleteByPrimaryKeyMethod(topLevelClass, interfaze);
    this.addInsertMethod(topLevelClass, interfaze);
    this.addInsertSelectiveMethod(topLevelClass, interfaze);
    this.addSelectByExampleWithBLOBsMethod(topLevelClass, interfaze);
    this.addSelectByExampleWithoutBLOBsMethod(topLevelClass, interfaze);
    this.addSelectByPrimaryKeyMethod(topLevelClass, interfaze);
    this.addUpdateByExampleParmsInnerclass(topLevelClass, interfaze);
    this.addUpdateByExampleSelectiveMethod(topLevelClass, interfaze);
    this.addUpdateByExampleWithBLOBsMethod(topLevelClass, interfaze);
    this.addUpdateByExampleWithoutBLOBsMethod(topLevelClass, interfaze);
    this.addUpdateByPrimaryKeySelectiveMethod(topLevelClass, interfaze);
    this.addUpdateByPrimaryKeyWithBLOBsMethod(topLevelClass, interfaze);
    this.addUpdateByPrimaryKeyWithoutBLOBsMethod(topLevelClass, interfaze);

    List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
    if (this.context.getPlugins().clientGenerated(interfaze, topLevelClass,
        this.introspectedTable)) {
      answer.add(topLevelClass);
      answer.add(interfaze);
    }

    return answer;
  }

  protected TopLevelClass getTopLevelClassShell() {
    FullyQualifiedJavaType interfaceType =
        new FullyQualifiedJavaType(this.introspectedTable.getDAOInterfaceType());
    FullyQualifiedJavaType implementationType =
        new FullyQualifiedJavaType(this.introspectedTable.getDAOImplementationType());


    TopLevelClass answer = new TopLevelClass(implementationType);
    answer.setVisibility(JavaVisibility.PUBLIC);
    answer.setSuperClass(this.daoTemplate.getSuperClass());
    answer.addImportedType(this.daoTemplate.getSuperClass());
    answer.addSuperInterface(interfaceType);
    answer.addImportedType(interfaceType);

    for (FullyQualifiedJavaType fqjt : this.daoTemplate.getImplementationImports()) {
      answer.addImportedType(fqjt);
    }

    CommentGenerator commentGenerator = this.context.getCommentGenerator();
    commentGenerator.addJavaFileComment(answer);

    // add constructor from the template
    answer.addMethod(this.daoTemplate.getConstructorClone(commentGenerator, implementationType,
        this.introspectedTable));

    // add any fields from the template
    for (Field field : this.daoTemplate.getFieldClones(commentGenerator, this.introspectedTable)) {
      answer.addField(field);
    }

    // add any methods from the template
    for (Method method : this.daoTemplate.getMethodClones(commentGenerator,
        this.introspectedTable)) {
      answer.addMethod(method);
    }

    return answer;
  }

  protected Interface getInterfaceShell() {
    Interface answer =
        new Interface(new FullyQualifiedJavaType(this.introspectedTable.getDAOInterfaceType()));
    answer.setVisibility(JavaVisibility.PUBLIC);

    String rootInterface =
        this.introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
    if (!stringHasValue(rootInterface)) {
      rootInterface = this.context.getJavaClientGeneratorConfiguration()
          .getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
    }

    if (stringHasValue(rootInterface)) {
      FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
      answer.addSuperInterface(fqjt);
      answer.addImportedType(fqjt);
    }

    for (FullyQualifiedJavaType fqjt : this.daoTemplate.getInterfaceImports()) {
      answer.addImportedType(fqjt);
    }

    this.context.getCommentGenerator().addJavaFileComment(answer);

    return answer;
  }

  protected void addCountByExampleMethod(TopLevelClass topLevelClass, Interface interfaze) {
    if (this.introspectedTable.getRules().generateCountByExample()) {
      AbstractDAOElementGenerator methodGenerator =
          new CountByExampleMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addDeleteByExampleMethod(TopLevelClass topLevelClass, Interface interfaze) {
    if (this.introspectedTable.getRules().generateDeleteByExample()) {
      AbstractDAOElementGenerator methodGenerator =
          new DeleteByExampleMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addDeleteByPrimaryKeyMethod(TopLevelClass topLevelClass, Interface interfaze) {
    if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
      AbstractDAOElementGenerator methodGenerator =
          new DeleteByPrimaryKeyMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addInsertMethod(TopLevelClass topLevelClass, Interface interfaze) {
    if (this.introspectedTable.getRules().generateInsert()) {
      AbstractDAOElementGenerator methodGenerator =
          new InsertMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addInsertSelectiveMethod(TopLevelClass topLevelClass, Interface interfaze) {
    if (this.introspectedTable.getRules().generateInsertSelective()) {
      AbstractDAOElementGenerator methodGenerator =
          new InsertSelectiveMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addSelectByExampleWithBLOBsMethod(TopLevelClass topLevelClass,
      Interface interfaze) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
      AbstractDAOElementGenerator methodGenerator =
          new SelectByExampleWithBLOBsMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addSelectByExampleWithoutBLOBsMethod(TopLevelClass topLevelClass,
      Interface interfaze) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
      AbstractDAOElementGenerator methodGenerator =
          new SelectByExampleWithoutBLOBsMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addSelectByPrimaryKeyMethod(TopLevelClass topLevelClass, Interface interfaze) {
    if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
      AbstractDAOElementGenerator methodGenerator =
          new SelectByPrimaryKeyMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addUpdateByExampleParmsInnerclass(TopLevelClass topLevelClass,
      Interface interfaze) {
    Rules rules = this.introspectedTable.getRules();
    if (rules.generateUpdateByExampleSelective() || rules.generateUpdateByExampleWithBLOBs()
        || rules.generateUpdateByExampleWithoutBLOBs()) {
      AbstractDAOElementGenerator methodGenerator = new UpdateByExampleParmsInnerclassGenerator();
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addUpdateByExampleSelectiveMethod(TopLevelClass topLevelClass,
      Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
      AbstractDAOElementGenerator methodGenerator =
          new UpdateByExampleSelectiveMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addUpdateByExampleWithBLOBsMethod(TopLevelClass topLevelClass,
      Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
      AbstractDAOElementGenerator methodGenerator =
          new UpdateByExampleWithBLOBsMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addUpdateByExampleWithoutBLOBsMethod(TopLevelClass topLevelClass,
      Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
      AbstractDAOElementGenerator methodGenerator =
          new UpdateByExampleWithoutBLOBsMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addUpdateByPrimaryKeySelectiveMethod(TopLevelClass topLevelClass,
      Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
      AbstractDAOElementGenerator methodGenerator =
          new UpdateByPrimaryKeySelectiveMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addUpdateByPrimaryKeyWithBLOBsMethod(TopLevelClass topLevelClass,
      Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
      AbstractDAOElementGenerator methodGenerator =
          new UpdateByPrimaryKeyWithBLOBsMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(TopLevelClass topLevelClass,
      Interface interfaze) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
      AbstractDAOElementGenerator methodGenerator =
          new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator(this.generateForJava5);
      this.initializeAndExecuteGenerator(methodGenerator, topLevelClass, interfaze);
    }
  }

  protected void initializeAndExecuteGenerator(AbstractDAOElementGenerator methodGenerator,
      TopLevelClass topLevelClass, Interface interfaze) {
    methodGenerator.setDAOTemplate(this.daoTemplate);
    methodGenerator.setContext(this.context);
    methodGenerator.setIntrospectedTable(this.introspectedTable);
    methodGenerator.setProgressCallback(this.progressCallback);
    methodGenerator.setWarnings(this.warnings);
    methodGenerator.addImplementationElements(topLevelClass);
    methodGenerator.addInterfaceElements(interfaze);
  }

  @Override
  public AbstractXmlGenerator getMatchedXMLGenerator() {
    // this method is not called for iBATIS2
    return null;
  }
}
