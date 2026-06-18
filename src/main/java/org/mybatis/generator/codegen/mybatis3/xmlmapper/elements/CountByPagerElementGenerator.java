package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 *
 * @author xionghui
 */
public class CountByPagerElementGenerator extends AbstractXmlElementGenerator {

  public CountByPagerElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", "countByPager")); //$NON-NLS-1$
    answer.addAttribute(new Attribute("parameterType", "java.util.Map")); //$NON-NLS-1$
    answer.addAttribute(new Attribute("resultType", "java.lang.Integer")); //$NON-NLS-1$ //$NON-NLS-2$

    this.context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    sb.append("select count(1) as total from "); //$NON-NLS-1$
    // sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));
    answer.addElement(this.getTableNameIncludeElement());
    answer.addElement(this.getExampleIncludeElement());

    if (this.context.getPlugins().sqlMapCountByExampleElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
