package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class UpdateSetFromBeanListElementGenerator extends AbstractXmlElementGenerator {

  public UpdateSetFromBeanListElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", "Update_Set_From_Bean"));

    this.context.getCommentGenerator().addComment(answer);

    List<IntrospectedColumn> columns = ListUtilities
        .removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < columns.size(); i++) {
      IntrospectedColumn introspectedColumn = columns.get(i);
      String column = introspectedColumn.getActualColumnName();
      // without id
      if ("id".equalsIgnoreCase(column)) {
        continue;
      }
      XmlElement ifElement = new XmlElement("if");
      sb.append("record.");
      sb.append(introspectedColumn.getJavaProperty());
      sb.append(" != null");
      ifElement.addAttribute(new Attribute("test", sb.toString()));
      sb.setLength(0);
      sb.append(column);
      sb.append(" = ");
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));
      sb.append(",");
      ifElement.addElement(new TextElement(sb.toString()));
      sb.setLength(0);
      answer.addElement(ifElement);
    }

    if (this.context.getPlugins().sqlMapBaseColumnListElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
