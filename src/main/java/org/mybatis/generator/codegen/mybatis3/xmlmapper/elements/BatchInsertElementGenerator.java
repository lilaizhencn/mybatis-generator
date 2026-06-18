package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 *
 * @author xionghui
 */
public class BatchInsertElementGenerator extends AbstractXmlElementGenerator {

  public BatchInsertElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", "batchInsert"));
    answer.addAttribute(new Attribute("parameterType", "java.util.Map"));

    this.context.getCommentGenerator().addComment(answer);

    answer.addElement(new TextElement("insert into "));

    answer.addElement(this.getTableNameIncludeElement());

    List<IntrospectedColumn> primaryKeyColumns = this.introspectedTable.getPrimaryKeyColumns();
    boolean excludeId = primaryKeyColumns != null && primaryKeyColumns.size() == 1
        && primaryKeyColumns.get(0).isAutoIncrement();

    {
      XmlElement trimElement = new XmlElement("trim");
      trimElement.addAttribute(new Attribute("prefix", "("));
      trimElement.addAttribute(new Attribute("suffix", ")"));
      trimElement.addAttribute(new Attribute("suffixOverrides", ","));

      XmlElement trimIncludeElement = new XmlElement("include");
      trimIncludeElement.addAttribute(
          new Attribute("refid", excludeId ? "Base_Column_List_Without_Id" : "Base_Column_List"));
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
      trimIncludeElement.addAttribute(new Attribute("refid",
          excludeId ? "Batch_Insert_Values" : "Batch_Insert_Values_On_DuplicateKey"));
      trimElement.addElement(trimIncludeElement);

      foreachElement.addElement(trimElement);

      answer.addElement(foreachElement);
    }

    if (this.context.getPlugins().sqlMapInsertElementGenerated(answer, this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
