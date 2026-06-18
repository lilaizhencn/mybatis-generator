package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class BatchInsertValuesOnDuplicateKeyListElementGenerator
    extends AbstractXmlElementGenerator {

  public BatchInsertValuesOnDuplicateKeyListElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("sql");

    answer.addAttribute(new Attribute("id", "Batch_Insert_Values_On_DuplicateKey"));

    this.context.getCommentGenerator().addComment(answer);

    Set<String> idSet = new HashSet<>();
    List<IntrospectedColumn> primaryKeyColumns = this.introspectedTable.getPrimaryKeyColumns();
    for (IntrospectedColumn primaryKeyColumn : primaryKeyColumns) {
      idSet.add(MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumn).toLowerCase());
    }
    boolean excludeId = primaryKeyColumns != null && primaryKeyColumns.size() == 1
        && primaryKeyColumns.get(0).isAutoIncrement();

    List<IntrospectedColumn> columns = ListUtilities
        .removeIdentityAndGeneratedAlwaysColumns(this.introspectedTable.getAllColumns());
    for (int i = 0; i < columns.size(); i++) {
      IntrospectedColumn introspectedColumn = columns.get(i);
      String column = introspectedColumn.getActualColumnName();
      // with id
      if (excludeId && idSet.contains(column.toLowerCase())) {
        StringBuilder sb = new StringBuilder();
        sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));
        sb.append(",");
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
      }
    }

    XmlElement includeElement = new XmlElement("include");
    includeElement.addAttribute(new Attribute("refid", "Batch_Insert_Values"));
    answer.addElement(includeElement);

    if (this.context.getPlugins().sqlMapBaseColumnListElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
