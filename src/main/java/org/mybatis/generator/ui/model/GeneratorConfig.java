package org.mybatis.generator.ui.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lilaizhen
 */
@Data
@ToString
public class GeneratorConfig {
    /**
     * 配置名称
     */
    private String name;

    private String tableName;
    private boolean sharding;
    private String deleteTablePre;
    private String projectFolder;

    private boolean buildModel;
    private boolean overrideModel;
    private String modelPackage;
    private String modelFolder;

    private boolean buildExample;
    private boolean overrideExample;
    private String examplePackage;
    private String exampleFolder;

    private boolean buildMapper;
    private boolean overrideMapper;
    private String mapperPackage;
    private String mapperFolder;

    private boolean buildXML;
    private boolean overrideXML;
    private String xmlPackage;
    private String xmlFolder;

    private boolean buildService;
    private boolean overrideService;
    private String servicePackage;
    private String serviceFolder;

    private boolean buildServiceImpl;
    private boolean overrideServiceImpl;
    private String serviceImplPackage;
    private String serviceImplFolder;

    private boolean buildController;
    private boolean overrideController;
    private String controllerPackage;
    private String controllerFolder;
}
