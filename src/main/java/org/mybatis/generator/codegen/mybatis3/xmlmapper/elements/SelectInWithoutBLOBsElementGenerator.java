package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 *
 * @author xionghui
 */
public class SelectInWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator {

  public SelectInWithoutBLOBsElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    List<IntrospectedColumn> primaryKeyColumns = this.introspectedTable.getPrimaryKeyColumns();
    if (primaryKeyColumns == null || primaryKeyColumns.size() != 1) {
      return;
    }

    IntrospectedColumn introspectedColumn = primaryKeyColumns.get(0);

    XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", "selectIn"));
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
    // sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement("from "));
    answer.addElement(this.getTableNameIncludeElement());

    sb.append("where ");
    sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
    sb.append(" in ");
    answer.addElement(new TextElement(sb.toString()));
    sb.setLength(0);

    XmlElement foreachElement = new XmlElement("foreach");
    foreachElement.addAttribute(new Attribute("collection", "records"));
    foreachElement.addAttribute(new Attribute("item", "record"));
    foreachElement.addAttribute(new Attribute("index", "index"));
    foreachElement.addAttribute(new Attribute("open", "("));
    foreachElement.addAttribute(new Attribute("separator", ","));
    foreachElement.addAttribute(new Attribute("close", ")"));

    foreachElement.addElement(new TextElement(
        MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record.")));
    answer.addElement(foreachElement);

    if (this.context.getPlugins().sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
