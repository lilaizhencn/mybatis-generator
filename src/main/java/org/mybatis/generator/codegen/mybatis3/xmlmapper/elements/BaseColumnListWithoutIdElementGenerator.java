package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * column without id
 *
 * @author xionghui
 */
public class BaseColumnListWithoutIdElementGenerator extends AbstractXmlElementGenerator {

  public BaseColumnListWithoutIdElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", //$NON-NLS-1$
        this.introspectedTable.getBaseColumnListId() + "_Without_Id"));

    this.context.getCommentGenerator().addComment(answer);

    Set<String> idSet = new HashSet<>();
    for (IntrospectedColumn primaryKeyColumns : this.introspectedTable.getPrimaryKeyColumns()) {
      idSet.add(MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumns).toLowerCase());
    }

    StringBuilder sb = new StringBuilder();
    Iterator<IntrospectedColumn> iter = this.introspectedTable.getNonBLOBColumns().iterator();
    while (iter.hasNext()) {
      String column = MyBatis3FormattingUtilities.getSelectListPhrase(iter.next());
      // without id
      if (idSet.contains(column.toLowerCase())) {
        continue;
      }
      sb.append(column);

      if (iter.hasNext()) {
        sb.append(", "); //$NON-NLS-1$
      }

      if (sb.length() > 80) {
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
      }
    }

    if (sb.length() > 0) {
      answer.addElement(new TextElement(sb.toString()));
    }

    if (this.context.getPlugins().sqlMapBaseColumnListElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
