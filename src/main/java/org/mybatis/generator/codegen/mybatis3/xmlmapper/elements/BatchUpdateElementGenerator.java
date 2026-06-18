package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 *
 * @author xionghui
 */
public class BatchUpdateElementGenerator extends AbstractXmlElementGenerator {

  public BatchUpdateElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("update");

    answer.addAttribute(new Attribute("id", "batchUpdate"));
    answer.addAttribute(new Attribute("parameterType", "java.util.Map"));

    this.context.getCommentGenerator().addComment(answer);


    XmlElement foreachElement = new XmlElement("foreach");
    foreachElement.addAttribute(new Attribute("collection", "records"));
    foreachElement.addAttribute(new Attribute("item", "record"));
    foreachElement.addAttribute(new Attribute("index", "index"));
    foreachElement.addAttribute(new Attribute("open", ""));
    foreachElement.addAttribute(new Attribute("close", ""));
    foreachElement.addAttribute(new Attribute("separator", ";"));

    foreachElement.addElement(new TextElement("update "));

    foreachElement.addElement(this.getTableNameIncludeElement());

    XmlElement setElement = new XmlElement("set");
    XmlElement includeElement = new XmlElement("include");
    includeElement.addAttribute(new Attribute("refid", "Update_Set_From_Bean"));
    setElement.addElement(includeElement);

    foreachElement.addElement(setElement);

    boolean and = false;
    StringBuilder sb = new StringBuilder();
    for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
      if (and) {
        sb.append("  and "); //$NON-NLS-1$
      } else {
        sb.append("where "); //$NON-NLS-1$
        and = true;
      }

      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = "); //$NON-NLS-1$
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));
      foreachElement.addElement(new TextElement(sb.toString()));
      sb.setLength(0);
    }

    answer.addElement(foreachElement);

    if (this.context.getPlugins().sqlMapUpdateByExampleWithoutBLOBsElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
