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
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SimpleSelectAllElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SimpleSelectByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;

/**
 *
 * @author Jeff Butler
 *
 */
public class SimpleXMLMapperGenerator extends AbstractXmlGenerator {

  public SimpleXMLMapperGenerator() {
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

    this.addResultMapElement(answer);
    this.addDeleteByPrimaryKeyElement(answer);
    this.addInsertElement(answer);
    this.addUpdateByPrimaryKeyElement(answer);
    this.addSelectByPrimaryKeyElement(answer);
    this.addSelectAllElement(answer);

    return answer;
  }

  protected void addResultMapElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateBaseResultMap()) {
      AbstractXmlElementGenerator elementGenerator =
          new ResultMapWithoutBLOBsElementGenerator(true);
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
      AbstractXmlElementGenerator elementGenerator = new SimpleSelectByPrimaryKeyElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addSelectAllElement(XmlElement parentElement) {
    AbstractXmlElementGenerator elementGenerator = new SimpleSelectAllElementGenerator();
    this.initializeAndExecuteGenerator(elementGenerator, parentElement);
  }

  protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
      AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addInsertElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateInsert()) {
      AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator();
      this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateByPrimaryKeyElement(XmlElement parentElement) {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
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
