/**
 * Copyright 2006-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.internal.CustomizationCommentGenerator;

/**
 *
 * @author Jeff Butler
 * @author xionghui
 *
 */
public class InsertElementGenerator extends AbstractXmlElementGenerator {

  public InsertElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", this.introspectedTable.getInsertStatementId()));
    answer.addAttribute(new Attribute("parameterType", "java.util.Map"));

    this.context.getCommentGenerator().addComment(answer);

    List<IntrospectedColumn> primaryKeyColumns = this.introspectedTable.getPrimaryKeyColumns();
    if (primaryKeyColumns != null && primaryKeyColumns.size() == 1
        && primaryKeyColumns.get(0).isAutoIncrement()) {
      XmlElement selectKeyElement = new XmlElement("selectKey");
      selectKeyElement.addAttribute(new Attribute("resultType", primaryKeyColumns.get(0)
          .getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters()));
      selectKeyElement.addAttribute(
          new Attribute("keyProperty", "record." + primaryKeyColumns.get(0).getJavaProperty()));
      selectKeyElement.addAttribute(new Attribute("order", "AFTER"));
      selectKeyElement.addElement(new TextElement("SELECT LAST_INSERT_ID()"));
      answer.addElement(selectKeyElement);
    }

    GeneratedKey gk = this.introspectedTable.getGeneratedKey();
    if (gk != null) {
      IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(gk.getColumn());
      // if the column is null, then it's a configuration error. The
      // warning has already been reported
      if (introspectedColumn != null) {
        if (gk.isJdbcStandard()) {
          answer.addAttribute(new Attribute("useGeneratedKeys", "true")); //$NON-NLS-1$ //$NON-NLS-2$
          answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
          answer.addAttribute(new Attribute("keyColumn", introspectedColumn.getActualColumnName())); //$NON-NLS-1$
        } else {
          answer.addElement(this.getSelectKey(introspectedColumn, gk));
        }
      }
    }

    answer.addElement(new TextElement("insert into "));

    answer.addElement(this.getTableNameIncludeElement());

    {
      XmlElement trimElement = new XmlElement("trim");
      trimElement.addAttribute(new Attribute("prefix", "("));
      trimElement.addAttribute(new Attribute("suffix", ")"));
      trimElement.addAttribute(new Attribute("suffixOverrides", ","));

      XmlElement trimIncludeElement = new XmlElement("include");
      trimIncludeElement.addAttribute(new Attribute("refid", "Insert_Columns"));
      trimElement.addElement(trimIncludeElement);

      answer.addElement(trimElement);
    }

    {
      XmlElement trimElement = new XmlElement("trim");
      trimElement.addAttribute(new Attribute("prefix", "values ("));
      trimElement.addAttribute(new Attribute("suffix", ")"));
      trimElement.addAttribute(new Attribute("suffixOverrides", ","));

      XmlElement trimIncludeElement = new XmlElement("include");
      trimIncludeElement.addAttribute(new Attribute("refid", "Insert_Values"));
      trimElement.addElement(trimIncludeElement);

      answer.addElement(trimElement);
    }

    if (this.context.getPlugins().sqlMapInsertElementGenerated(answer, this.introspectedTable)) {
      // add comment
      CommentGenerator commentGenerator = this.context.getCommentGenerator();
      if (commentGenerator instanceof CustomizationCommentGenerator) {
        ((CustomizationCommentGenerator) commentGenerator).addInsertComment(parentElement);
      }

      parentElement.addElement(answer);
    }
  }
}
