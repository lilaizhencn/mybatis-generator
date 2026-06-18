/**
 * Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.codegen.mybatis3.xmlmapper;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BaseColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BaseColumnListWithoutIdElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BatchInsertElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BatchInsertOnDuplicateKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BatchInsertValueListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BatchInsertValuesOnDuplicateKeyListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BatchUpdateElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BlobColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.CountByExampleElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.CountByPagerElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteByExampleElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteInElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ExampleWhereClauseElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertSelectiveElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertValueListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByExampleWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPagerWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectInWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectOneByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.TableNameElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByExampleSelectiveElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByExampleWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeySelectiveElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateSetFromBeanListElementGenerator;

/**
 *
 * @author Jeff Butler
 *
 */
public class XMLMapperGenerator extends AbstractXmlGenerator {

  public XMLMapperGenerator() {
    super();
  }

  protected XmlElement getSqlMapElement() {
    FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
    this.progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
    XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
    String namespace = this.introspectedTable.getMyBatis3SqlMapNamespace();
    answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
        namespace));

    this.context.getCommentGenerator().addRootComment(answer);

    this.addResultMapWithoutBLOBsElement(answer);
    this.addResultMapWithBLOBsElement(answer);
    this.addExampleWhereClauseElement(answer);
    // this.addMyBatis3UpdateByExampleWhereClauseElement(answer);

    // add sql config
    this.addTableNameElement(answer);
    this.addBaseColumnListWithoutIdElementElement(answer);
    this.addBaseColumnListElement(answer);
    this.addBlobColumnListElement(answer);
    this.addInsertColumnListElement(answer);
    this.addInsertValueListElement(answer);
    this.addBatchInsertValueListElement(answer);
    this.addBatchInsertValuesOnDuplicateKeyListElement(answer);
    this.addUpdateSetFromBeanListElement(answer);

    this.addInsertElement(answer);
    this.addBatchInsertElement(answer);
    this.addInsertOnDuplicateKeyElement(answer);
    // this.addInsertSelectiveElement(answer);

    this.addDeleteByPrimaryKeyElement(answer);
    this.addDeleteByExampleElement(answer);
    this.addDeleteInElement(answer);

    this.addUpdateByPrimaryKeyWithoutBLOBsElement(answer);
    this.addUpdateByPrimaryKeyWithBLOBsElement(answer);
    // this.addUpdateByExampleSelectiveElement(answer);
    this.addUpdateByExampleWithoutBLOBsElement(answer);
    this.addUpdateByExampleWithBLOBsElement(answer);
    // this.addUpdateByPrimaryKeySelectiveElement(answer);
    this.addBatchUpdateElement(answer);

    this.addSelectByPrimaryKeyElement(answer);
    this.addSelectByExampleWithoutBLOBsElement(answer);
    this.addSelectByExampleWithBLOBsElement(answer);
    this.addSelectOneByExampleWithoutBLOBsElement(answer);
    this.addSelectInWithoutBLOBsElement(answer);

    this.addCountByExampleElement(answer);
    this.addCountByPagerElement(answer);

    this.addSelectByPagerWithoutBLOBsElement(answer);

    return answer;
  }

  protected void addResultMapWithoutBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseResultMap()) {
      AbstractXmlElementGenerator elementGenerator =
          new ResultMapWithoutBLOBsElementGenerator(false);
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
      AbstractXmlElementGenerator elementGenerator = new ResultMapWithBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addExampleWhereClauseElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateSQLExampleWhereClause()) {
      AbstractXmlElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator(false);
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addMyBatis3UpdateByExampleWhereClauseElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateMyBatis3UpdateByExampleWhereClause()) {
      AbstractXmlElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator(true);
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addTableNameElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseColumnList()) {
      TableNameElementGenerator elementGenerator = new TableNameElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addBaseColumnListWithoutIdElementElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseColumnList()) {
      AbstractXmlElementGenerator elementGenerator = new BaseColumnListWithoutIdElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addBaseColumnListElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseColumnList()) {
      AbstractXmlElementGenerator elementGenerator = new BaseColumnListElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addBlobColumnListElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBlobColumnList()) {
      AbstractXmlElementGenerator elementGenerator = new BlobColumnListElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addInsertColumnListElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseColumnList()) {
      AbstractXmlElementGenerator elementGenerator = new InsertColumnListElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addInsertValueListElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseColumnList()) {
      AbstractXmlElementGenerator elementGenerator = new InsertValueListElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addBatchInsertValueListElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseColumnList()) {
      AbstractXmlElementGenerator elementGenerator = new BatchInsertValueListElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addBatchInsertValuesOnDuplicateKeyListElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseColumnList()) {
      AbstractXmlElementGenerator elementGenerator =
          new BatchInsertValuesOnDuplicateKeyListElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateSetFromBeanListElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseColumnList()) {
      AbstractXmlElementGenerator elementGenerator = new UpdateSetFromBeanListElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addSelectByExampleWithoutBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
      AbstractXmlElementGenerator elementGenerator =
          new SelectByExampleWithoutBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addSelectByExampleWithBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
      AbstractXmlElementGenerator elementGenerator = new SelectByExampleWithBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addSelectOneByExampleWithoutBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
      AbstractXmlElementGenerator elementGenerator =
          new SelectOneByExampleWithoutBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addSelectInWithoutBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
      AbstractXmlElementGenerator elementGenerator = new SelectInWithoutBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
      AbstractXmlElementGenerator elementGenerator = new SelectByPrimaryKeyElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
      AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addDeleteByExampleElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateDeleteByExample()) {
      AbstractXmlElementGenerator elementGenerator = new DeleteByExampleElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addDeleteInElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
      AbstractXmlElementGenerator elementGenerator = new DeleteInElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addInsertElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateInsert()) {
      AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addBatchInsertElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateInsert()) {
      AbstractXmlElementGenerator elementGenerator = new BatchInsertElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addInsertOnDuplicateKeyElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateInsert()) {
      AbstractXmlElementGenerator elementGenerator =
          new BatchInsertOnDuplicateKeyElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addInsertSelectiveElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateInsertSelective()) {
      AbstractXmlElementGenerator elementGenerator = new InsertSelectiveElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addCountByExampleElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateCountByExample()) {
      AbstractXmlElementGenerator elementGenerator = new CountByExampleElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addCountByPagerElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateCountByExample()) {
      AbstractXmlElementGenerator elementGenerator = new CountByPagerElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addSelectByPagerWithoutBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
      AbstractXmlElementGenerator elementGenerator =
          new SelectByPagerWithoutBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateByExampleSelectiveElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
      AbstractXmlElementGenerator elementGenerator = new UpdateByExampleSelectiveElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateByExampleWithBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
      AbstractXmlElementGenerator elementGenerator = new UpdateByExampleWithBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addBatchUpdateElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
      AbstractXmlElementGenerator elementGenerator = new BatchUpdateElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateByExampleWithoutBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
      AbstractXmlElementGenerator elementGenerator =
          new UpdateByExampleWithoutBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
      AbstractXmlElementGenerator elementGenerator =
          new UpdateByPrimaryKeySelectiveElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateByPrimaryKeyWithBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
      AbstractXmlElementGenerator elementGenerator =
          new UpdateByPrimaryKeyWithBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
      AbstractXmlElementGenerator elementGenerator =
          new UpdateByPrimaryKeyWithoutBLOBsElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator,
      XmlElement parentElement) {
    elementGenerator.setContext(this.context);
    elementGenerator.setIntrospectedTable(this.introspectedTable);
    elementGenerator.setProgressCallback(this.progressCallback);
    elementGenerator.setWarnings(this.warnings);
    elementGenerator.addElements(parentElement);
  }

  @Override
  public Document getDocument() {
    Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
        XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
    document.setRootElement(this.getSqlMapElement());

    if (!this.context.getPlugins().sqlMapDocumentGenerated(document, this.introspectedTable)) {
      document = null;
    }

    return document;
  }
}
