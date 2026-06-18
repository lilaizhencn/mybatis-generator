package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 生成tableName
 */
public class TableNameElementGenerator extends AbstractXmlElementGenerator {

  public TableNameElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", "Table_Name"));

    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(
        new TextElement(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

    if (this.context.getPlugins().sqlMapBaseColumnListElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
