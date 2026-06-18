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
package org.mybatis.generator.codegen.mybatis3;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.ControllerGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.MixedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.ServiceGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.ServiceImplGenerator;
import org.mybatis.generator.codegen.mybatis3.model.BaseRecordGenerator;
import org.mybatis.generator.codegen.mybatis3.model.ExampleGenerator;
import org.mybatis.generator.codegen.mybatis3.model.PrimaryKeyGenerator;
import org.mybatis.generator.codegen.mybatis3.model.RecordWithBLOBsGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.internal.ObjectFactory;

/**
 * @author Jeff Butler
 */
public class IntrospectedTableMyBatis3Impl extends IntrospectedTable {
  private static String ENABLE_REPOSITORY = "enableRepository";
  private static String ENABLE_SERVICE = "enableService";
  private static String ENABLE_SERVICE_IMPL = "enableServiceImpl";
  private static String ENABLE_CONTROLLER = "enableController";

  protected List<AbstractJavaGenerator> javaModelGenerators;

  protected List<AbstractJavaGenerator> clientGenerators;

  protected AbstractXmlGenerator xmlMapperGenerator;

  public IntrospectedTableMyBatis3Impl() {
    super(TargetRuntime.MYBATIS3);
    this.javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
    this.clientGenerators = new ArrayList<AbstractJavaGenerator>();
  }

  @Override
  public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
    this.calculateJavaModelGenerators(warnings, progressCallback);

    AbstractJavaClientGenerator javaClientGenerator =
        this.calculateClientGenerators(warnings, progressCallback);

    this.calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
  }

  protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator,
      List<String> warnings, ProgressCallback progressCallback) {
    SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration =
        this.context.getSqlMapGeneratorConfiguration();
    if (!Boolean.parseBoolean(sqlMapGeneratorConfiguration.getProperty("enable"))) {
      return;
    }
    if (javaClientGenerator == null) {
      if (this.context.getSqlMapGeneratorConfiguration() != null) {
        this.xmlMapperGenerator = new XMLMapperGenerator();
      }
    } else {
      this.xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
    }

    this.initializeAbstractGenerator(this.xmlMapperGenerator, warnings, progressCallback);
  }

  protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings,
      ProgressCallback progressCallback) {
    if (!this.rules.generateJavaClient()) {
      return null;
    }

    AbstractJavaClientGenerator javaGenerator = this.createJavaClientGenerator();
    if (javaGenerator == null) {
      return null;
    }

    JavaClientGeneratorConfiguration javaClientGeneratorConfiguration =
        this.context.getJavaClientGeneratorConfiguration();
    String enableRepository = javaClientGeneratorConfiguration.getProperty(ENABLE_REPOSITORY);
    if (Boolean.parseBoolean(enableRepository)) {
      this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
      this.clientGenerators.add(javaGenerator);
    }

    String enableService = javaClientGeneratorConfiguration.getProperty(ENABLE_SERVICE);
    if (Boolean.parseBoolean(enableService)) {
      boolean overrideService =
          Boolean.parseBoolean(javaClientGeneratorConfiguration.getProperty("overrideService"));
      String serviceProject = javaClientGeneratorConfiguration.getProperty("serviceProject");
      AbstractJavaClientGenerator serviceGenerator = new ServiceGenerator();
      serviceGenerator.setOverride(overrideService);
      serviceGenerator.setProject(serviceProject);
      this.initializeAbstractGenerator(serviceGenerator, warnings, progressCallback);
      this.clientGenerators.add(serviceGenerator);
    }
    String enableServiceImpl = javaClientGeneratorConfiguration.getProperty(ENABLE_SERVICE_IMPL);
    if (Boolean.parseBoolean(enableServiceImpl)) {
      boolean overrideServiceImpl =
          Boolean.parseBoolean(javaClientGeneratorConfiguration.getProperty("overrideServiceImpl"));
      String serviceImplProject =
          javaClientGeneratorConfiguration.getProperty("serviceImplProject");
      AbstractJavaClientGenerator serviceImplGenerator = new ServiceImplGenerator();
      serviceImplGenerator.setOverride(overrideServiceImpl);
      serviceImplGenerator.setProject(serviceImplProject);
      this.initializeAbstractGenerator(serviceImplGenerator, warnings, progressCallback);
      this.clientGenerators.add(serviceImplGenerator);
    }
    String enableController = javaClientGeneratorConfiguration.getProperty(ENABLE_CONTROLLER);
    if (Boolean.parseBoolean(enableController)) {
      boolean overrideController =
          Boolean.parseBoolean(javaClientGeneratorConfiguration.getProperty("overrideController"));
      String controllerProject = javaClientGeneratorConfiguration.getProperty("controllerProject");
      AbstractJavaClientGenerator controllerGenerator = new ControllerGenerator();
      controllerGenerator.setOverride(overrideController);
      controllerGenerator.setProject(controllerProject);
      this.initializeAbstractGenerator(controllerGenerator, warnings, progressCallback);
      this.clientGenerators.add(controllerGenerator);
    }

    return javaGenerator;
  }

  protected AbstractJavaClientGenerator createJavaClientGenerator() {
    if (this.context.getJavaClientGeneratorConfiguration() == null) {
      return null;
    }

    String type = this.context.getJavaClientGeneratorConfiguration().getConfigurationType();

    boolean overrideMapper = Boolean.parseBoolean(
        this.context.getJavaClientGeneratorConfiguration().getProperty("overrideMapper"));
    String mapperProject =
        this.context.getJavaClientGeneratorConfiguration().getProperty("mapperProject");

    AbstractJavaClientGenerator javaGenerator;
    if ("XMLMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
      javaGenerator = new JavaMapperGenerator();
      javaGenerator.setOverride(overrideMapper);
      javaGenerator.setProject(mapperProject);
    } else if ("MIXEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
      javaGenerator = new MixedClientGenerator();
    } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
      javaGenerator = new AnnotatedClientGenerator();
    } else if ("MAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
      javaGenerator = new JavaMapperGenerator();
    } else {
      javaGenerator = (AbstractJavaClientGenerator) ObjectFactory.createInternalObject(type);
    }

    return javaGenerator;
  }

  protected void calculateJavaModelGenerators(List<String> warnings,
      ProgressCallback progressCallback) {
    JavaModelGeneratorConfiguration javaModelGeneratorConfiguration =
        this.context.getJavaModelGeneratorConfiguration();
    if (this.getRules().generateExampleClass()) {
      boolean overrideExample =
          Boolean.parseBoolean(javaModelGeneratorConfiguration.getProperty("overrideExample"));
      String exampleProject = javaModelGeneratorConfiguration.getProperty("exampleProject");
      AbstractJavaGenerator javaGenerator = new ExampleGenerator();
      javaGenerator.setOverride(overrideExample);
      javaGenerator.setProject(exampleProject);
      this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
      this.javaModelGenerators.add(javaGenerator);
    }

    if (this.getRules().generatePrimaryKeyClass()) {
      AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
      this.initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
      this.javaModelGenerators.add(javaGenerator);
    }

    if (this.getRules().generateBaseRecordClass()) {
      boolean overrideRecord =
          Boolean.parseBoolean(javaModelGeneratorConfiguration.getProperty("overrideRecord"));
      String recordProject = javaModelGeneratorConfiguration.getProperty("recordProject");
      AbstractJavaGenerator javaGenerator = new BaseRecordGenerator();
      javaGenerator.setOverride(overrideRecord);
      javaGenerator.setProject(recordProject);
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
    if (abstractGenerator == null) {
      return;
    }

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
        String project = javaGenerator.getProject();
        GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
            project == null ? this.context.getJavaModelGeneratorConfiguration().getTargetProject()
                : project,
            this.context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
            this.context.getJavaFormatter(), javaGenerator.isOverride());
        answer.add(gjf);
      }
    }

    for (AbstractJavaGenerator javaGenerator : this.clientGenerators) {
      List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
      for (CompilationUnit compilationUnit : compilationUnits) {
        String project = javaGenerator.getProject();
        GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
            project == null ? this.context.getJavaClientGeneratorConfiguration().getTargetProject()
                : project,
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

    if (this.xmlMapperGenerator != null) {
      Document document = this.xmlMapperGenerator.getDocument();
      GeneratedXmlFile gxf = new GeneratedXmlFile(document, this.getMyBatis3XmlMapperFileName(),
          this.getMyBatis3XmlMapperPackage(),
          this.context.getSqlMapGeneratorConfiguration().getTargetProject(), false,
          this.context.getXmlFormatter());
      if (this.context.getPlugins().sqlMapGenerated(gxf, this)) {
        answer.add(gxf);
      }
    }

    return answer;
  }

  @Override
  public int getGenerationSteps() {
    return this.javaModelGenerators.size() + this.clientGenerators.size()
        + (this.xmlMapperGenerator == null ? 0 : 1);
  }

  @Override
  public boolean isJava5Targeted() {
    return true;
  }

  @Override
  public boolean requiresXMLGenerator() {
    AbstractJavaClientGenerator javaClientGenerator = this.createJavaClientGenerator();

    if (javaClientGenerator == null) {
      return false;
    } else {
      return javaClientGenerator.requiresXMLGenerator();
    }
  }
}
