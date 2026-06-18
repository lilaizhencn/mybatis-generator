package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.CustomizationCommentGenerator;
import org.mybatis.generator.ui.util.TablePrimaryKeyUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getCamelCaseString;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class ServiceImplGenerator extends AbstractJavaClientGenerator {

    public ServiceImplGenerator() {
        super(true);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.51", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type =
                new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaServiceImplType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        // add annotations
        FullyQualifiedJavaType slf4jAnnotation =
                new FullyQualifiedJavaType("lombok.extern.log4j.Log4j2");
        topLevelClass.addImportedType(slf4jAnnotation);
        topLevelClass.addAnnotation("@Log4j2");
        FullyQualifiedJavaType serviceAnnotation =
                new FullyQualifiedJavaType("org.springframework.stereotype.Service");
        topLevelClass.addImportedType(serviceAnnotation);
        topLevelClass.addAnnotation("@Service");

        // add comment
        if (commentGenerator instanceof CustomizationCommentGenerator) {
            ((CustomizationCommentGenerator) commentGenerator).addServiceImplClassComment(topLevelClass,
                    introspectedTable);
        }

        String superClass = introspectedTable
                .getTableConfigurationProperty(PropertyRegistry.SERVICE_IMPL_ROOT_CLASS);
        if (!stringHasValue(superClass)) {
            superClass = context.getJavaClientGeneratorConfiguration()
                    .getProperty(PropertyRegistry.SERVICE_IMPL_ROOT_CLASS);
        }

        if (stringHasValue(superClass)) {
            FullyQualifiedJavaType originFqjt = new FullyQualifiedJavaType(superClass);
            topLevelClass.addImportedType(originFqjt);
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(superClass);
            FullyQualifiedJavaType myBatis3JavaMapperType =
                    new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
            topLevelClass.addImportedType(myBatis3JavaMapperType);
            fqjt.addTypeArgument(myBatis3JavaMapperType);
            FullyQualifiedJavaType baseRecordType =
                    new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            topLevelClass.addImportedType(baseRecordType);
            fqjt.addTypeArgument(baseRecordType);
            FullyQualifiedJavaType exampleType =
                    new FullyQualifiedJavaType(introspectedTable.getExampleType());
            topLevelClass.addImportedType(exampleType);
            fqjt.addTypeArgument(exampleType);
            fqjt.addTypeArgument(TablePrimaryKeyUtils.getPrimaryKeyType(introspectedTable));
            topLevelClass.setSuperClass(fqjt);
        }

        String rootInterface = introspectedTable.getMyBatis3JavaServiceType();
        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            topLevelClass.addImportedType(fqjt);
            topLevelClass.addSuperInterface(fqjt);
        }

        // add field
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        FullyQualifiedJavaType repositoryType =
                new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        field.setType(repositoryType);
        field.setName(getCamelCaseString(
                introspectedTable.getFullyQualifiedTable().getIntrospectedTableName(), false)
                + "Repository");
        FullyQualifiedJavaType autowiredAnnotation =
                new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
        topLevelClass.addImportedType(autowiredAnnotation);
        field.addAnnotation("@Autowired");
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        // add method
        Method method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setReturnType(new FullyQualifiedJavaType(introspectedTable.getExampleType()));
        method.setName("getPageExample");
        method.addAnnotation("@Override");
        method.addParameter(
                new Parameter(FullyQualifiedJavaType.getStringInstance(), "fieldName", "final"));
        method.addParameter(
                new Parameter(FullyQualifiedJavaType.getStringInstance(), "keyword", "final"));
        StringBuilder sb = new StringBuilder();
        sb.append("final ");
        String exampleTypeShortName =
                new FullyQualifiedJavaType(introspectedTable.getExampleType()).getShortName();
        sb.append(exampleTypeShortName);
        sb.append(" example = new ");
        sb.append(exampleTypeShortName);
        sb.append("();");
        method.addBodyLine(sb.toString());
        method.addBodyLine("example.createCriteria().andFieldLike(fieldName, keyword);");
        method.addBodyLine("return example;");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        List<CompilationUnit> answer = new ArrayList<>();
        answer.add(topLevelClass);

        return answer;
    }

    protected void addCountByExampleMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateCountByExample()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new CountByExampleMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addDeleteByExampleMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByExample()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByExampleMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new DeleteByPrimaryKeyMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new InsertMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addInsertSelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new InsertSelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addSelectByExampleWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new SelectByExampleWithBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addSelectByExampleWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new SelectByExampleWithoutBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new SelectByPrimaryKeyMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addUpdateByExampleSelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new UpdateByExampleSelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addUpdateByExampleWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new UpdateByExampleWithBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addUpdateByExampleWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new UpdateByExampleWithoutBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new UpdateByPrimaryKeySelectiveMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new UpdateByPrimaryKeyWithBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void initializeAndExecuteGenerator(AbstractJavaMapperMethodGenerator methodGenerator,
                                                 Interface interfaze) {
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.addInterfaceElements(interfaze);
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new XMLMapperGenerator();
    }
}
