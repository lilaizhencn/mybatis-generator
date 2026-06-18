package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class SelectOneByExampleWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator {

  public SelectOneByExampleWithoutBLOBsElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", "selectOneByExample"));
    answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId())); //$NON-NLS-1$
    answer.addAttribute(new Attribute("parameterType", "java.util.Map")); //$NON-NLS-1$

    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(new TextElement("select")); //$NON-NLS-1$

    StringBuilder sb = new StringBuilder();
    if (stringHasValue(this.introspectedTable.getSelectByExampleQueryId())) {
      sb.append('\'');
      sb.append(this.introspectedTable.getSelectByExampleQueryId());
      sb.append("' as QUERYID,"); //$NON-NLS-1$
      answer.addElement(new TextElement(sb.toString()));
    }
    answer.addElement(this.getBaseColumnListElement());

    sb.setLength(0);
    sb.append("from "); //$NON-NLS-1$
    // sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));
    answer.addElement(this.getTableNameIncludeElement());
    answer.addElement(this.getExampleIncludeElement());

    XmlElement ifElement = new XmlElement("if");
    ifElement
        .addAttribute(new Attribute("test", "example != null and example.orderByClause != null"));
    ifElement.addElement(new TextElement("order by ${example.orderByClause}"));
    answer.addElement(ifElement);

    answer.addElement(new TextElement("limit 1"));

    if (this.context.getPlugins().sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
