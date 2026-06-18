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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.internal.CustomizationCommentGenerator;

/**
 *
 * @author Jeff Butler
 *
 */
public class DeleteByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator {

  public DeleteByPrimaryKeyElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

    // answer.addAttribute(new Attribute("id",
    // introspectedTable.getDeleteByPrimaryKeyStatementId()));
    answer.addAttribute(new Attribute("id", "deleteById"));
    answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
        "java.util.Map"));

    this.context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    sb.append("delete from "); //$NON-NLS-1$
    // sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));

    answer.addElement(this.getTableNameIncludeElement());

    boolean and = false;
    for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
      sb.setLength(0);
      if (and) {
        sb.append("  and "); //$NON-NLS-1$
      } else {
        sb.append("where "); //$NON-NLS-1$
        and = true;
      }

      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = "); //$NON-NLS-1$
      // sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
      sb.append(this.getParameterClause(introspectedColumn, null));
      answer.addElement(new TextElement(sb.toString()));
    }

    if (this.context.getPlugins().sqlMapDeleteByPrimaryKeyElementGenerated(answer,
        this.introspectedTable)) {
      // add comment
      CommentGenerator commentGenerator = this.context.getCommentGenerator();
      if (commentGenerator instanceof CustomizationCommentGenerator) {
        ((CustomizationCommentGenerator) commentGenerator).addDeleteComment(parentElement);
      }

      parentElement.addElement(answer);
    }
  }

  private String getParameterClause(IntrospectedColumn introspectedColumn, String prefix) {
    StringBuilder sb = new StringBuilder();

    sb.append("#{"); //$NON-NLS-1$
    sb.append("id");
    sb.append(",jdbcType="); //$NON-NLS-1$
    sb.append(introspectedColumn.getJdbcTypeName());

    if (stringHasValue(introspectedColumn.getTypeHandler())) {
      sb.append(",typeHandler="); //$NON-NLS-1$
      sb.append(introspectedColumn.getTypeHandler());
    }

    sb.append('}');

    return sb.toString();
  }
}
