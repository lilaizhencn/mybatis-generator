package org.mybatis.generator.ui.bridge;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.ui.model.DatabaseConfig;
import org.mybatis.generator.ui.model.GeneratorConfig;
import org.mybatis.generator.ui.util.DbUtils;
import org.mybatis.generator.ui.util.FilenameUtils;
import org.mybatis.generator.ui.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lilaizhen
 */
public class MybatisGeneratorBridge {
    private GeneratorConfig generatorConfig;

    private DatabaseConfig selectedDatabaseConfig;

    private ProgressCallback progressCallback;

    public void setGeneratorConfig(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        selectedDatabaseConfig = databaseConfig;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public void generate() throws Exception {
        Configuration configuration = new Configuration();
        Context context = new Context(ModelType.FLAT);
        configuration.addContext(context);

        String dbType = getDatabaseId(selectedDatabaseConfig);
        context.setId(dbType);
        context.setTargetRuntime("MyBatis3");
        context.addProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING, "UTF-8");
        context.addProperty("sharding", String.valueOf(generatorConfig.isSharding()));

        TableConfiguration tableConfig = new TableConfiguration(context);
        tableConfig.setTableName(generatorConfig.getTableName());
        if (generatorConfig.getDeleteTablePre() != null
                && generatorConfig.getDeleteTablePre().length() > 0) {
            DomainObjectRenamingRule domainObjectRenamingRule = new DomainObjectRenamingRule();
            domainObjectRenamingRule.setSearchString(generatorConfig.getDeleteTablePre());
            domainObjectRenamingRule.setReplaceString("");
            tableConfig.setDomainObjectRenamingRule(domainObjectRenamingRule);
        }
        context.addTableConfiguration(tableConfig);

        CommentGeneratorConfiguration commentConfig = new CommentGeneratorConfiguration();
        commentConfig.addProperty("suppressAllComments", "true");
        commentConfig.addProperty("dateFormat", "yyyy-MM-dd HH:mm:ss");
        context.setCommentGeneratorConfiguration(commentConfig);

        JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
        jdbcConfig.setDriverClass(DbUtils.getDriverClass(selectedDatabaseConfig.getDbType()));
        jdbcConfig.setConnectionURL(DbUtils.getConnectionUrlWithSchema(selectedDatabaseConfig));
        jdbcConfig.setUserId(selectedDatabaseConfig.getUsername());
        jdbcConfig.setPassword(selectedDatabaseConfig.getPassword());
        jdbcConfig.addProperty("remarks", "true");
        jdbcConfig.addProperty("useInformationSchema", "true");
        context.setJdbcConnectionConfiguration(jdbcConfig);

        JavaTypeResolverConfiguration javaTypeConfig = new JavaTypeResolverConfiguration();
        javaTypeConfig.addProperty("forceBigDecimals", "true");
        javaTypeConfig.addProperty("useJSR310Types", "true");
        context.setJavaTypeResolverConfiguration(javaTypeConfig);

        JavaModelGeneratorConfiguration modelConfig = new JavaModelGeneratorConfiguration();
        modelConfig.setTargetPackage("package");
        modelConfig.setTargetProject("project");
        modelConfig.addProperty("exampleEnable", String.valueOf(generatorConfig.isBuildExample()));
        modelConfig.addProperty("exampleProject",
                FilenameUtils.normalize(generatorConfig.getProjectFolder()
                        + (StringUtils.isEmpty(generatorConfig.getExampleFolder()) ? ""
                        : File.separator + generatorConfig.getExampleFolder())));
        modelConfig.addProperty("overrideExample",
                String.valueOf(generatorConfig.isOverrideExample()));
        modelConfig.addProperty("recordEnable", String.valueOf(generatorConfig.isBuildModel()));
        modelConfig.addProperty("recordProject",
                FilenameUtils.normalize(generatorConfig.getProjectFolder()
                        + (StringUtils.isEmpty(generatorConfig.getModelFolder()) ? ""
                        : File.separator + generatorConfig.getModelFolder())));
        modelConfig.addProperty("overrideRecord",
                String.valueOf(generatorConfig.isOverrideModel()));
        modelConfig.addProperty("enableSubPackages", "false");
        modelConfig.addProperty("trimStrings", "false");
        if (generatorConfig.getExamplePackage() != null) {
            modelConfig.addProperty("targetExamplePackage", generatorConfig.getExamplePackage());
        }
        if (generatorConfig.getModelPackage() != null) {
            modelConfig.addProperty("targetRecordPackage", generatorConfig.getModelPackage());
        }
        context.setJavaModelGeneratorConfiguration(modelConfig);

        SqlMapGeneratorConfiguration xmlConfig = new SqlMapGeneratorConfiguration();
        xmlConfig.setTargetPackage(!generatorConfig.isBuildXML()
                && StringUtils.isEmpty(generatorConfig.getXmlPackage()) ? "package"
                : generatorConfig.getXmlPackage());
        xmlConfig.setTargetProject(FilenameUtils.normalize(generatorConfig.getProjectFolder()
                + (StringUtils.isEmpty(generatorConfig.getXmlFolder()) ? ""
                : File.separator + generatorConfig.getXmlFolder())));
        xmlConfig.addProperty("enable", String.valueOf(generatorConfig.isBuildXML()));
        xmlConfig.addProperty("enableSubPackages", "false");
        context.setSqlMapGeneratorConfiguration(xmlConfig);

        JavaClientGeneratorConfiguration javaClientGeneratorConfig =
                new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfig.setConfigurationType("XMLMAPPER");
        javaClientGeneratorConfig.setTargetPackage("package");
        javaClientGeneratorConfig.setTargetProject("project");
        if (generatorConfig.isSharding()) {
            javaClientGeneratorConfig.addProperty("rootInterface",
                    "com.surprising.common.mybatis.sharding.data.CrudRepository");
            javaClientGeneratorConfig.addProperty("serviceRootInterface",
                    "com.surprising.common.mybatis.sharding.service.CrudService");
            javaClientGeneratorConfig.addProperty("serviceImplRootClass",
                    "com.surprising.common.mybatis.sharding.service.AbstractCrudService");
        } else {
            javaClientGeneratorConfig.addProperty("rootInterface",
                    "com.surprising.common.mybatis.data.CrudRepository");
            javaClientGeneratorConfig.addProperty("serviceRootInterface",
                    "com.surprising.common.mybatis.service.CrudService");
            javaClientGeneratorConfig.addProperty("serviceImplRootClass",
                    "com.surprising.common.mybatis.service.AbstractCrudService");
        }
        javaClientGeneratorConfig.addProperty("enableRepository",
                String.valueOf(generatorConfig.isBuildMapper()));
        javaClientGeneratorConfig.addProperty("mapperProject",
                FilenameUtils.normalize(generatorConfig.getProjectFolder()
                        + (StringUtils.isEmpty(generatorConfig.getMapperFolder()) ? ""
                        : File.separator + generatorConfig.getMapperFolder())));
        javaClientGeneratorConfig.addProperty("enableService",
                String.valueOf(generatorConfig.isBuildService()));
        javaClientGeneratorConfig.addProperty("serviceProject",
                FilenameUtils.normalize(generatorConfig.getProjectFolder()
                        + (StringUtils.isEmpty(generatorConfig.getServiceFolder()) ? ""
                        : File.separator + generatorConfig.getServiceFolder())));
        javaClientGeneratorConfig.addProperty("enableServiceImpl",
                String.valueOf(generatorConfig.isBuildServiceImpl()));
        javaClientGeneratorConfig.addProperty("serviceImplProject",
                FilenameUtils.normalize(generatorConfig.getProjectFolder()
                        + (StringUtils.isEmpty(generatorConfig.getServiceImplFolder()) ? ""
                        : File.separator + generatorConfig.getServiceImplFolder())));
        javaClientGeneratorConfig.addProperty("enableController",
                String.valueOf(generatorConfig.isBuildController()));
        javaClientGeneratorConfig.addProperty("controllerProject",
                FilenameUtils.normalize(generatorConfig.getProjectFolder()
                        + (StringUtils.isEmpty(generatorConfig.getControllerFolder()) ? ""
                        : File.separator + generatorConfig.getControllerFolder())));
        javaClientGeneratorConfig.addProperty("overrideMapper",
                String.valueOf(generatorConfig.isOverrideMapper()));
        javaClientGeneratorConfig.addProperty("overrideService",
                String.valueOf(generatorConfig.isOverrideService()));
        javaClientGeneratorConfig.addProperty("overrideServiceImpl",
                String.valueOf(generatorConfig.isOverrideServiceImpl()));
        javaClientGeneratorConfig.addProperty("overrideController",
                String.valueOf(generatorConfig.isOverrideController()));
        if (generatorConfig.getMapperPackage() != null) {
            javaClientGeneratorConfig.addProperty("targetRepositoryPackage",
                    generatorConfig.getMapperPackage());
        }
        if (generatorConfig.getServicePackage() != null) {
            javaClientGeneratorConfig.addProperty("targetServicePackage",
                    generatorConfig.getServicePackage());
        }
        if (generatorConfig.getServiceImplPackage() != null) {
            javaClientGeneratorConfig.addProperty("targetServiceImplPackage",
                    generatorConfig.getServiceImplPackage());
        }
        if (generatorConfig.getControllerPackage() != null) {
            javaClientGeneratorConfig.addProperty("targetControllerPackage",
                    generatorConfig.getControllerPackage());
        }
        javaClientGeneratorConfig.addProperty("enableSubPackages", "false");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfig);

        List<String> warnings = new ArrayList<>();
        Set<String> fullyqualifiedTables = new HashSet<>();
        Set<String> contexts = new HashSet<>();
        ShellCallback shellCallback = new DefaultShellCallback(false);
        MyBatisGenerator myBatisGenerator =
                new MyBatisGenerator(configuration, shellCallback, warnings);
        // if overrideXML selected, delete oldXML file and generate a new one
        if (generatorConfig.isBuildXML() && generatorConfig.isOverrideXML()) {
            String mappingXMLFilePath = getMappingXMLFilePath(generatorConfig);
            File mappingXMLFile = new File(mappingXMLFilePath);
            if (mappingXMLFile.exists()) {
                mappingXMLFile.delete();
            }
        }
        myBatisGenerator.generate(progressCallback, contexts, fullyqualifiedTables);
    }

    private String getDatabaseId(DatabaseConfig config) {
        String dbType = config.getDbType();
        if ("PostgreSQL".equalsIgnoreCase(dbType)) {
            return "postgresql";
        }
        return "mysql";
    }

    private String getMappingXMLFilePath(GeneratorConfig generatorConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append(generatorConfig.getProjectFolder()).append(File.separator);
        if (!StringUtils.isEmpty(generatorConfig.getXmlFolder())) {
            sb.append(generatorConfig.getXmlFolder()).append(File.separator);
        }
        sb.append(generatorConfig.getXmlPackage().replace(".", File.separator)).append(File.separator);
        sb.append(JavaBeansUtil.getCamelCaseString(generatorConfig.getTableName(), true));
        sb.append("Mapper.xml");
        return FilenameUtils.normalize(sb.toString());
    }
}
