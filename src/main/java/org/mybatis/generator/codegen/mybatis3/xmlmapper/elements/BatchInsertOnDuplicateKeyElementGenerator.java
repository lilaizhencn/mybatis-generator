package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;

/**
 *
 * @author xionghui
 */
public class BatchInsertOnDuplicateKeyElementGenerator extends AbstractXmlElementGenerator {

  public BatchInsertOnDuplicateKeyElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", "batchInsertOnDuplicateKey"));
    answer.addAttribute(new Attribute("parameterType", "java.util.Map"));

    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(new TextElement("insert into "));

    answer.addElement(this.getTableNameIncludeElement());

    {
      XmlElement trimElement = new XmlElement("trim");
      trimElement.addAttribute(new Attribute("prefix", "("));
      trimElement.addAttribute(new Attribute("suffix", ")"));
      trimElement.addAttribute(new Attribute("suffixOverrides", ","));

      XmlElement trimIncludeElement = new XmlElement("include");
      trimIncludeElement.addAttribute(new Attribute("refid", "Base_Column_List"));
      trimElement.addElement(trimIncludeElement);

      answer.addElement(trimElement);
    }

    answer.addElement(new TextElement("values "));

    {
      XmlElement foreachElement = new XmlElement("foreach");
      foreachElement.addAttribute(new Attribute("collection", "records"));
      foreachElement.addAttribute(new Attribute("item", "record"));
      foreachElement.addAttribute(new Attribute("index", "index"));
      foreachElement.addAttribute(new Attribute("separator", ","));

      XmlElement trimElement = new XmlElement("trim");
      trimElement.addAttribute(new Attribute("prefix", "("));
      trimElement.addAttribute(new Attribute("suffix", ")"));
      trimElement.addAttribute(new Attribute("suffixOverrides", ","));

      XmlElement trimIncludeElement = new XmlElement("include");
      trimIncludeElement
          .addAttribute(new Attribute("refid", "Batch_Insert_Values_On_DuplicateKey"));
      trimElement.addElement(trimIncludeElement);

      foreachElement.addElement(trimElement);

      answer.addElement(foreachElement);
    }

    answer.addElement(new TextElement("ON DUPLICATE KEY UPDATE"));

    List<IntrospectedColumn> columns = ListUtilities
        .removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
    StringBuilder sb = new StringBuilder();
    for (int i = 0;; i++) {
      String column = columns.get(i).getActualColumnName();
      // without id
      if ("id".equalsIgnoreCase(column)) {
        continue;
      }
      sb.append(column);
      sb.append(" = VALUES(");
      sb.append(column);
      sb.append(")");
      if (i == columns.size() - 1) {
        break;
      }
      sb.append(", ");
    }
    answer.addElement(new TextElement(sb.toString()));

    if (this.context.getPlugins().sqlMapInsertElementGenerated(answer, this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
