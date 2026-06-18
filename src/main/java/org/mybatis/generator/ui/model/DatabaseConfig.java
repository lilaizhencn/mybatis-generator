package org.mybatis.generator.ui.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author lilaizhen
 */
@Data
@ToString
public class DatabaseConfig {
    /**
     * The primary key in the sqlite db
     */
    private Integer id;

    private String name;

    private String dbType;

    private String host;

    private String port;

    private String schema;

    private String username;

    private String password;
}
