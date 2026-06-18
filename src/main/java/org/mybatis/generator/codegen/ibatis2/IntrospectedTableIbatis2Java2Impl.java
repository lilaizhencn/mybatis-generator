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
package org.mybatis.generator.codegen.ibatis2;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.DAOGenerator;
import org.mybatis.generator.codegen.ibatis2.dao.templates.GenericCIDAOTemplate;
import org.mybatis.generator.codegen.ibatis2.dao.templates.GenericSIDAOTemplate;
import org.mybatis.generator.codegen.ibatis2.dao.templates.IbatisDAOTemplate;
import org.mybatis.generator.codegen.ibatis2.dao.templates.SpringDAOTemplate;
import org.mybatis.generator.codegen.ibatis2.model.BaseRecordGenerator;
import org.mybatis.generator.codegen.ibatis2.model.ExampleGenerator;
import org.mybatis.generator.codegen.ibatis2.model.PrimaryKeyGenerator;
import org.mybatis.generator.codegen.ibatis2.model.RecordWithBLOBsGenerator;
import org.mybatis.generator.codegen.ibatis2.sqlmap.SqlMapGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.ObjectFactory;

/**
 * Introspected table implementation for iBatis targeting Java versions before 1.5.
 *
 * @author Jeff Butler
 *
 */
public class IntrospectedTableIbatis2Java2Impl extends IntrospectedTable {
  protected List<AbstractJavaGenerator> javaModelGenerators;
  protected List<AbstractJavaGenerator> daoGenerators;
  protected AbstractXmlGenerator sqlMapGenerator;

  public IntrospectedTableIbatis2Java2Impl() {
    super(TargetRuntime.IBATIS2);
    this.javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
    this.daoGenerators = new ArrayList<AbstractJavaGenerator>();
  }

  @Override
  public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
    this.calculateJavaModelGenerators(warnings, progressCallback);
    this.calculateDAOGenerators(warnings, progressCallback);
    this.calculateSqlMapGenerator(warnings, progressCallback);
  }

  protected void calculateSqlMapGenerator(List<String> warnings,
      ProgressCallback progressCallback) {
    this.sqlMapGenerator = new SqlMapGenerator();
    this.initializeAbstractGenerator(this.sqlMapGenerator, warnings, progressCallback);
  }

  protected void calculateDAOGenerators(List<String> warnings, ProgressCallback progressCallback) {
    if (this.context.getJavaClientGeneratorConfiguration() == null) {
      return;
    }

    String type = this.context.getJavaClientGeneratorConfiguration().getConfigurationType();

    AbstractJavaGenerator javaGenerator;
    if ("IBATIS".equalsIgnoreCase(type)) { //$NON-NLS-1$
      javaGenerator = new DAOGenerator(new IbatisDAOTemplate(), this.isJava5Targeted());
    } else if ("SPRING".equalsIgnoreCase(type)) { //$NON-NLS-1$
      javaGenerator = new DAOGenerator(new SpringDAOTemplate(), this.isJava5Targeted());
    } else if ("GENERIC-CI".equalsIgnoreCase(type)) { //$NON-NLS-1$
      javaGenerator = new DAOGenerator(new GenericCIDAOTemplate(), this.isJava5Targeted());
    } else if ("GENERIC-SI".equalsIgnoreCase(type)) { //$NON-NLS-1$
      javaGenerator = new DAOGenerator(new GenericSIDAOTemplate(), this.isJava5Targeted());
    } else {
      javaGenerator = (AbstractJavaGenerator) ObjectFactory.createInternalObject(type);
    }

    this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
    this.daoGenerators.add(javaGenerator);
  }

  protected void calculateJavaModelGenerators(List<String> warnings,
      ProgressCallback progressCallback) {
    if (this.getRules().generateExampleClass()) {
      AbstractJavaGenerator javaGenerator = new ExampleGenerator(this.isJava5Targeted());
      this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
      this.javaModelGenerators.add(javaGenerator);
    }

    if (this.getRules().generatePrimaryKeyClass()) {
      AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
      this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
      this.javaModelGenerators.add(javaGenerator);
    }

    if (this.getRules().generateBaseRecordClass()) {
      AbstractJavaGenerator javaGenerator = new BaseRecordGenerator();
      this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
      this.javaModelGenerators.add(javaGenerator);
    }

    if (this.getRules().generateRecordWithBLOBsClass()) {
      AbstractJavaGenerator javaGenerator = new RecordWithBLOBsGenerator();
      this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
      this.javaModelGenerators.add(javaGenerator);
    }
  }

  protected void initializeAbstractGenerator(AbstractGenerator abstractGenerator,
      List<String> warnings, ProgressCallback progressCallback) {
    abstractGenerator.setContext(this.context);
    abstractGenerator.setIntrospectedTable(this);
    abstractGenerator.setProgressCallback(progressCallback);
    abstractGenerator.setWarnings(warnings);
  }

  @Override
  public List<GeneratedJavaFile> getGeneratedJavaFiles() {
    List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

    for (AbstractJavaGenerator javaGenerator : this.javaModelGenerators) {
      List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
      for (CompilationUnit compilationUnit : compilationUnits) {
        GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
            this.context.getJavaModelGeneratorConfiguration().getTargetProject(),
            this.context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
            this.context.getJavaFormatter(), javaGenerator.isOverride());
        answer.add(gjf);
      }
    }

    for (AbstractJavaGenerator javaGenerator : this.daoGenerators) {
      List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
      for (CompilationUnit compilationUnit : compilationUnits) {
        GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
            this.context.getJavaClientGeneratorConfiguration().getTargetProject(),
            this.context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
            this.context.getJavaFormatter(), javaGenerator.isOverride());
        answer.add(gjf);
      }
    }

    return answer;
  }

  @Override
  public List<GeneratedXmlFile> getGeneratedXmlFiles() {
    List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

    Document document = this.sqlMapGenerator.getDocument();
    GeneratedXmlFile gxf = new GeneratedXmlFile(document, this.getIbatis2SqlMapFileName(),
        this.getIbatis2SqlMapPackage(),
        this.context.getSqlMapGeneratorConfiguration().getTargetProject(), true,
        this.context.getXmlFormatter());
    if (this.context.getPlugins().sqlMapGenerated(gxf, this)) {
      answer.add(gxf);
    }

    return answer;
  }

  @Override
  public boolean isJava5Targeted() {
    return false;
  }

  @Override
  public int getGenerationSteps() {
    // +1 for the sqlMapGenerator
    return this.javaModelGenerators.size() + this.daoGenerators.size() + 1;
  }

  @Override
  public boolean requiresXMLGenerator() {
    return true;
  }
}
