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
package org.mybatis.generator.internal.rules;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * This class centralizes all the rules related to code generation - including the methods and
 * objects to create, and certain attributes related to those objects.
 *
 * @author Jeff Butler
 */
public abstract class BaseRules implements Rules {

  protected TableConfiguration tableConfiguration;

  protected IntrospectedTable introspectedTable;

  protected final boolean isModelOnly;

  public BaseRules(IntrospectedTable introspectedTable) {
    super();
    this.introspectedTable = introspectedTable;
    this.tableConfiguration = introspectedTable.getTableConfiguration();
    String modelOnly = this.tableConfiguration.getProperty(PropertyRegistry.TABLE_MODEL_ONLY);
    this.isModelOnly = StringUtility.isTrue(modelOnly);
  }

  /**
   * Implements the rule for generating the insert SQL Map element and DAO method. If the insert
   * statement is allowed, then generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateInsert() {
    if (this.isModelOnly) {
      return false;
    }

    return this.tableConfiguration.isInsertStatementEnabled();
  }

  /**
   * Implements the rule for generating the insert selective SQL Map element and DAO method. If the
   * insert statement is allowed, then generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateInsertSelective() {
    if (this.isModelOnly) {
      return false;
    }

    return this.tableConfiguration.isInsertStatementEnabled();
  }

  /**
   * Calculates the class that contains all fields. This class is used as the insert statement
   * parameter, as well as the returned value from the select by primary key method. The actual
   * class depends on how the domain model is generated.
   *
   * @return the type of the class that holds all fields
   */
  @Override
  public FullyQualifiedJavaType calculateAllFieldsClass() {

    String answer;

    if (this.generateRecordWithBLOBsClass()) {
      answer = this.introspectedTable.getRecordWithBLOBsType();
    } else if (this.generateBaseRecordClass()) {
      answer = this.introspectedTable.getBaseRecordType();
    } else {
      answer = this.introspectedTable.getPrimaryKeyType();
    }

    return new FullyQualifiedJavaType(answer);
  }

  /**
   * Implements the rule for generating the update by primary key without BLOBs SQL Map element and
   * DAO method. If the table has a primary key as well as other non-BLOB fields, and the
   * updateByPrimaryKey statement is allowed, then generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateUpdateByPrimaryKeyWithoutBLOBs() {
    if (this.isModelOnly) {
      return false;
    }

    if (ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getBaseColumns())
        .isEmpty()) {
      return false;
    }

    boolean rc = this.tableConfiguration.isUpdateByPrimaryKeyStatementEnabled()
        && this.introspectedTable.hasPrimaryKeyColumns() && this.introspectedTable.hasBaseColumns();

    return rc;
  }

  /**
   * Implements the rule for generating the update by primary key with BLOBs SQL Map element and DAO
   * method. If the table has a primary key as well as other BLOB fields, and the updateByPrimaryKey
   * statement is allowed, then generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateUpdateByPrimaryKeyWithBLOBs() {
    if (this.isModelOnly) {
      return false;
    }

    if (ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns())
        .isEmpty()) {
      return false;
    }
    boolean rc = this.tableConfiguration.isUpdateByPrimaryKeyStatementEnabled()
        && this.introspectedTable.hasPrimaryKeyColumns() && this.introspectedTable.hasBLOBColumns();

    return rc;
  }

  /**
   * Implements the rule for generating the update by primary key selective SQL Map element and DAO
   * method. If the table has a primary key as well as other fields, and the updateByPrimaryKey
   * statement is allowed, then generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateUpdateByPrimaryKeySelective() {
    if (this.isModelOnly) {
      return false;
    }

    if (ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns())
        .isEmpty()) {
      return false;
    }

    boolean rc = this.tableConfiguration.isUpdateByPrimaryKeyStatementEnabled()
        && this.introspectedTable.hasPrimaryKeyColumns()
        && (this.introspectedTable.hasBLOBColumns() || this.introspectedTable.hasBaseColumns());

    return rc;
  }

  /**
   * Implements the rule for generating the delete by primary key SQL Map element and DAO method. If
   * the table has a primary key, and the deleteByPrimaryKey statement is allowed, then generate the
   * element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateDeleteByPrimaryKey() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isDeleteByPrimaryKeyStatementEnabled()
        && this.introspectedTable.hasPrimaryKeyColumns();

    return rc;
  }

  /**
   * Implements the rule for generating the delete by example SQL Map element and DAO method. If the
   * deleteByExample statement is allowed, then generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateDeleteByExample() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isDeleteByExampleStatementEnabled();

    return rc;
  }

  /**
   * Implements the rule for generating the result map without BLOBs. If either select method is
   * allowed, then generate the result map.
   *
   * @return true if the result map should be generated
   */
  @Override
  public boolean generateBaseResultMap() {
    if (this.isModelOnly) {
      return true;
    }

    boolean rc = this.tableConfiguration.isSelectByExampleStatementEnabled()
        || this.tableConfiguration.isSelectByPrimaryKeyStatementEnabled();

    return rc;
  }

  /**
   * Implements the rule for generating the result map with BLOBs. If the table has BLOB columns,
   * and either select method is allowed, then generate the result map.
   *
   * @return true if the result map should be generated
   */
  @Override
  public boolean generateResultMapWithBLOBs() {
    boolean rc;

    if (this.introspectedTable.hasBLOBColumns()) {
      if (this.isModelOnly) {
        rc = true;
      } else {
        rc = this.tableConfiguration.isSelectByExampleStatementEnabled()
            || this.tableConfiguration.isSelectByPrimaryKeyStatementEnabled();
      }
    } else {
      rc = false;
    }

    return rc;
  }

  /**
   * Implements the rule for generating the SQL example where clause element.
   *
   * <p>
   * In iBATIS2, generate the element if the selectByExample, deleteByExample, updateByExample, or
   * countByExample statements are allowed.
   *
   * <p>
   * In MyBatis3, generate the element if the selectByExample, deleteByExample, or countByExample
   * statements are allowed.
   *
   * @return true if the SQL where clause element should be generated
   */
  @Override
  public boolean generateSQLExampleWhereClause() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isSelectByExampleStatementEnabled()
        || this.tableConfiguration.isDeleteByExampleStatementEnabled()
        || this.tableConfiguration.isCountByExampleStatementEnabled();

    if (this.introspectedTable.getTargetRuntime() == TargetRuntime.IBATIS2) {
      rc |= this.tableConfiguration.isUpdateByExampleStatementEnabled();
    }

    return rc;
  }

  /**
   * Implements the rule for generating the SQL example where clause element specifically for use in
   * the update by example methods.
   *
   * <p>
   * In iBATIS2, do not generate the element.
   *
   * <p>
   * In MyBatis3, generate the element if the updateByExample statements are allowed.
   *
   * @return true if the SQL where clause element should be generated
   */
  @Override
  public boolean generateMyBatis3UpdateByExampleWhereClause() {
    if (this.isModelOnly) {
      return false;
    }

    return this.introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3
        && this.tableConfiguration.isUpdateByExampleStatementEnabled();
  }

  /**
   * Implements the rule for generating the select by primary key SQL Map element and DAO method. If
   * the table has a primary key as well as other fields, and the selectByPrimaryKey statement is
   * allowed, then generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateSelectByPrimaryKey() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isSelectByPrimaryKeyStatementEnabled()
        && this.introspectedTable.hasPrimaryKeyColumns()
        && (this.introspectedTable.hasBaseColumns() || this.introspectedTable.hasBLOBColumns());

    return rc;
  }

  /**
   * Implements the rule for generating the select by example without BLOBs SQL Map element and DAO
   * method. If the selectByExample statement is allowed, then generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateSelectByExampleWithoutBLOBs() {
    if (this.isModelOnly) {
      return false;
    }

    return this.tableConfiguration.isSelectByExampleStatementEnabled();
  }

  /**
   * Implements the rule for generating the select by example with BLOBs SQL Map element and DAO
   * method. If the table has BLOB fields and the selectByExample statement is allowed, then
   * generate the element and method.
   *
   * @return true if the element and method should be generated
   */
  @Override
  public boolean generateSelectByExampleWithBLOBs() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isSelectByExampleStatementEnabled()
        && this.introspectedTable.hasBLOBColumns();

    return rc;
  }

  /**
   * Implements the rule for generating an example class. The class should be generated if the
   * selectByExample or deleteByExample or countByExample methods are allowed.
   *
   * @return true if the example class should be generated
   */
  @Override
  public boolean generateExampleClass() {
    if (this.introspectedTable.getContext().getSqlMapGeneratorConfiguration() == null
        && this.introspectedTable.getContext().getJavaClientGeneratorConfiguration() == null) {
      // this is a model only context - don't generate the example class
      return false;
    }

    if (this.isModelOnly) {
      return false;
    }

    JavaModelGeneratorConfiguration javaModelGeneratorConfiguration =
        this.introspectedTable.getContext().getJavaModelGeneratorConfiguration();
    boolean exampleEnable =
        Boolean.parseBoolean(javaModelGeneratorConfiguration.getProperty("exampleEnable"));
    if (!exampleEnable) {
      return false;
    }

    boolean rc = this.tableConfiguration.isSelectByExampleStatementEnabled()
        || this.tableConfiguration.isDeleteByExampleStatementEnabled()
        || this.tableConfiguration.isCountByExampleStatementEnabled()
        || this.tableConfiguration.isUpdateByExampleStatementEnabled();

    return rc;
  }

  @Override
  public boolean generateCountByExample() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isCountByExampleStatementEnabled();

    return rc;
  }

  @Override
  public boolean generateUpdateByExampleSelective() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isUpdateByExampleStatementEnabled();

    return rc;
  }

  @Override
  public boolean generateUpdateByExampleWithoutBLOBs() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isUpdateByExampleStatementEnabled()
        && (this.introspectedTable.hasPrimaryKeyColumns()
            || this.introspectedTable.hasBaseColumns());

    return rc;
  }

  @Override
  public boolean generateUpdateByExampleWithBLOBs() {
    if (this.isModelOnly) {
      return false;
    }

    boolean rc = this.tableConfiguration.isUpdateByExampleStatementEnabled()
        && this.introspectedTable.hasBLOBColumns();

    return rc;
  }

  @Override
  public IntrospectedTable getIntrospectedTable() {
    return this.introspectedTable;
  }

  @Override
  public boolean generateBaseColumnList() {
    if (this.isModelOnly) {
      return false;
    }

    return this.generateSelectByPrimaryKey() || this.generateSelectByExampleWithoutBLOBs();
  }

  @Override
  public boolean generateBlobColumnList() {
    if (this.isModelOnly) {
      return false;
    }

    return this.introspectedTable.hasBLOBColumns()
        && (this.tableConfiguration.isSelectByExampleStatementEnabled()
            || this.tableConfiguration.isSelectByPrimaryKeyStatementEnabled());
  }

  @Override
  public boolean generateJavaClient() {
    return !this.isModelOnly;
  }
}
