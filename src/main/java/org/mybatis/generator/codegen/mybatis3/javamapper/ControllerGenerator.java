package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.internal.CustomizationCommentGenerator;
import org.mybatis.generator.ui.util.TablePrimaryKeyUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getCamelCaseString;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class ControllerGenerator extends AbstractJavaClientGenerator {

    public ControllerGenerator() {
        super(true);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.51", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type =
                new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaControllerType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        // add annotations
        FullyQualifiedJavaType slf4jControllerAnnotation =
                new FullyQualifiedJavaType("lombok.extern.log4j.Log4j2");
        topLevelClass.addImportedType(slf4jControllerAnnotation);
        topLevelClass.addAnnotation("@Log4j2");
        FullyQualifiedJavaType restControllerAnnotation =
                new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RestController");
        topLevelClass.addImportedType(restControllerAnnotation);
        topLevelClass.addAnnotation("@RestController");
        FullyQualifiedJavaType requestMappingAnnotation =
                new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping");
        topLevelClass.addImportedType(requestMappingAnnotation);
        topLevelClass.addAnnotation("@RequestMapping(value = \"/admin/v1/xxx\")");

        // add comment
        if (commentGenerator instanceof CustomizationCommentGenerator) {
            ((CustomizationCommentGenerator) commentGenerator).addControllerClassComment(topLevelClass,
                    introspectedTable);
        }

        FullyQualifiedJavaType serviceType =
                new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaServiceType());
        topLevelClass.addImportedType(serviceType);
        String serviceTypeName = getCamelCaseString(
                introspectedTable.getFullyQualifiedTable().getIntrospectedTableName(), false)
                + "Service";

        // add field
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(serviceType);
        field.setName(serviceTypeName);
        FullyQualifiedJavaType resourceAnnotation =
                new FullyQualifiedJavaType("javax.annotation.Resource");
        topLevelClass.addImportedType(resourceAnnotation);
        field.addAnnotation("@Resource");
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        FullyQualifiedJavaType getMappingAnnotation =
                new FullyQualifiedJavaType("org.springframework.web.bind.annotation.GetMapping");
        topLevelClass.addImportedType(getMappingAnnotation);
        FullyQualifiedJavaType postMappingAnnotation =
                new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PostMapping");
        topLevelClass.addImportedType(postMappingAnnotation);
        FullyQualifiedJavaType deleteMappingAnnotation =
                new FullyQualifiedJavaType("org.springframework.web.bind.annotation.DeleteMapping");
        topLevelClass.addImportedType(deleteMappingAnnotation);
        FullyQualifiedJavaType putMappingAnnotation =
                new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PutMapping");
        topLevelClass.addImportedType(putMappingAnnotation);

        FullyQualifiedJavaType requestBodyAnnotation =
                new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestBody");
        topLevelClass.addImportedType(requestBodyAnnotation);
        FullyQualifiedJavaType pathVariableAnnotation =
                new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PathVariable");
        topLevelClass.addImportedType(pathVariableAnnotation);
        FullyQualifiedJavaType responseResultAnnotation =
                new FullyQualifiedJavaType("com.surprising.commons.support.model.ResponseResult");
        topLevelClass.addImportedType(responseResultAnnotation);
        FullyQualifiedJavaType objectCopyUtilsAnnotation =
                new FullyQualifiedJavaType("cc.newex.dax.users.common.util.ObjectCopyUtils");
        topLevelClass.addImportedType(objectCopyUtilsAnnotation);
        FullyQualifiedJavaType dataGridPagerAnnotation =
                new FullyQualifiedJavaType("cc.newex.commons.support.model.DataGridPager");
        topLevelClass.addImportedType(dataGridPagerAnnotation);

        FullyQualifiedJavaType dtoInstance = new FullyQualifiedJavaType(
                new FullyQualifiedJavaType(introspectedTable.getBaseRecordType() + "DTO")
                        .getShortName());

        String baseRecordShortName =
                new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()).getShortName();

        FullyQualifiedJavaType primaryKeyType =
                TablePrimaryKeyUtils.getPrimaryKeyType(introspectedTable);

        // add method
        addAddMethod(dtoInstance, baseRecordShortName, serviceTypeName, commentGenerator,
                topLevelClass);
        // delete method
        addRemoveMethod(serviceTypeName, primaryKeyType, commentGenerator, topLevelClass);
        // edit method
        addEditMethod(dtoInstance, baseRecordShortName, serviceTypeName, commentGenerator,
                topLevelClass);
        // getByPager method
        addGetByPagerMethod(dtoInstance, baseRecordShortName, serviceTypeName, commentGenerator,
                topLevelClass);
        // get method
        addGetByIdMethod(dtoInstance, baseRecordShortName, serviceTypeName, primaryKeyType,
                commentGenerator, topLevelClass);

        // add dto static class
        addDtoInnerClass(dtoInstance, baseRecordShortName, topLevelClass);

        List<CompilationUnit> answer = new ArrayList<>();
        answer.add(topLevelClass);

        return answer;
    }

    private void addAddMethod(FullyQualifiedJavaType dtoInstance, String baseRecordShortName,
                              String serviceTypeName, CommentGenerator commentGenerator, TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("ResponseResult<Integer>"));
        method.setName("add");
        method.addAnnotation("@PostMapping(value = \"\")");
        method.addParameter(new Parameter(dtoInstance, "dto", "@RequestBody final"));
        StringBuilder sb = new StringBuilder();
        sb.append("final ");
        sb.append(baseRecordShortName);
        sb.append(" po = ObjectCopyUtils.map(dto, ");
        sb.append(baseRecordShortName);
        sb.append(".class);");
        method.addBodyLine(sb.toString());
        method.addBodyLine("return ResultUtils.success(this." + serviceTypeName + ".add(po));");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    private void addRemoveMethod(String serviceTypeName, FullyQualifiedJavaType primaryKeyType,
                                 CommentGenerator commentGenerator, TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("ResponseResult<Integer>"));
        method.setName("remove");
        method.addAnnotation("@DeleteMapping(value = \"/{id}\")");
        method.addParameter(new Parameter(primaryKeyType, "id", "@PathVariable(\"id\") final"));
        method.addBodyLine("return ResultUtils.success(this." + serviceTypeName + ".removeById(id));");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    private void addEditMethod(FullyQualifiedJavaType dtoInstance, String baseRecordShortName,
                               String serviceTypeName, CommentGenerator commentGenerator, TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("ResponseResult<Integer>"));
        method.setName("edit");
        method.addAnnotation("@PutMapping(value = \"\")");
        method.addParameter(new Parameter(dtoInstance, "dto", "@RequestBody final"));
        StringBuilder sb = new StringBuilder();
        sb.append("final ");
        sb.append(baseRecordShortName);
        sb.append(" po = ObjectCopyUtils.map(dto, ");
        sb.append(baseRecordShortName);
        sb.append(".class);");
        method.addBodyLine(sb.toString());
        method.addBodyLine("return ResultUtils.success(this." + serviceTypeName + ".editById(po));");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    private void addGetByPagerMethod(FullyQualifiedJavaType dtoInstance, String baseRecordShortName,
                                     String serviceTypeName, CommentGenerator commentGenerator, TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        String dtoShortName = dtoInstance.getShortName();
        method.setReturnType(
                new FullyQualifiedJavaType("ResponseResult<DataGridPagerResult<" + dtoShortName + ">>"));
        method.setName("getPagerList");
        method.addAnnotation("@PostMapping(value = \"/pager\")");
        FullyQualifiedJavaType dataGridPagerInstance = new FullyQualifiedJavaType("DataGridPager");
        method.addParameter(new Parameter(dataGridPagerInstance, "pager", "@RequestBody final"));
        method.addBodyLine("final PageInfo pageInfo = pager.toPageInfo();");
        topLevelClass.addImportedType(introspectedTable.getExampleType());
        String exampleShortName =
                new FullyQualifiedJavaType(introspectedTable.getExampleType()).getShortName();
        method.addBodyLine("final " + exampleShortName + " example = new " + exampleShortName + "();");
        method.addBodyLine("final List<" + introspectedTable.getBaseRecordType()
                + "> domainList = this." + serviceTypeName + ".getByPage(pageInfo, example);");
        method.addBodyLine("final List<" + dtoShortName
                + "> dtoList = Lists.newArrayListWithCapacity(CollectionUtils.size(domainList));");
        method.addBodyLine("domainList.forEach(domain -> dtoList.add(ObjectCopyUtils.map(domain, "
                + dtoShortName + ".class)));");
        method.addBodyLine(
                "return ResultUtils.success(new DataGridPagerResult<>(pageInfo.getTotals(), dtoList));");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    private void addGetByIdMethod(FullyQualifiedJavaType dtoInstance, String baseRecordShortName,
                                  String serviceTypeName, FullyQualifiedJavaType primaryKeyType,
                                  CommentGenerator commentGenerator, TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        String dtoShortName = dtoInstance.getShortName();
        method.setReturnType(new FullyQualifiedJavaType("ResponseResult<" + dtoShortName + ">"));
        method.setName("get");
        method.addAnnotation("@GetMapping(value = \"/{id}\")");
        method.addParameter(new Parameter(primaryKeyType, "id", "@PathVariable(\"id\") final"));
        method.addBodyLine(
                "final " + baseRecordShortName + " po = this." + serviceTypeName + ".getById(id);");
        method.addBodyLine(
                "return ResultUtils.success(ObjectCopyUtils.map(po, " + dtoShortName + ".class));");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    private void addDtoInnerClass(FullyQualifiedJavaType dtoInstance, String baseRecordShortName,
                                  TopLevelClass topLevelClass) {
        InnerClass answer = new InnerClass(dtoInstance);

        answer.setVisibility(JavaVisibility.PUBLIC);
        answer.setStatic(true);
        answer.setSuperClass(new FullyQualifiedJavaType(baseRecordShortName));

        context.getCommentGenerator().addClassComment(answer, introspectedTable, true);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PROTECTED);
        method.setName(dtoInstance.getShortName());
        method.setConstructor(true);
        method.addBodyLine("super();");
        answer.addMethod(method);

        topLevelClass.addInnerClass(answer);
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new XMLMapperGenerator();
    }
}
