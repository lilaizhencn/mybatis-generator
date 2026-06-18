/**
 * Copyright 2006-2016 the original author or authors.
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
public class SelectByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator {

  public SelectByPrimaryKeyElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", "selectById"));

    String parameterType;
    if (this.introspectedTable.getRules().generatePrimaryKeyClass()) {
      parameterType = this.introspectedTable.getPrimaryKeyType();
    } else {
      // PK fields are in the base class. If more than on PK
      // field, then they are coming in a map.
      if (this.introspectedTable.getPrimaryKeyColumns().size() > 1) {
        parameterType = "map"; //$NON-NLS-1$
      } else {
        parameterType = "java.util.Map";
      }
    }

    answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
        parameterType));

    if (this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
      answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
          this.introspectedTable.getResultMapWithBLOBsId()));
    } else {
      answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
          this.introspectedTable.getBaseResultMapId()));
    }

    this.context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    sb.append("select "); //$NON-NLS-1$

    if (stringHasValue(this.introspectedTable.getSelectByPrimaryKeyQueryId())) {
      sb.append('\'');
      sb.append(this.introspectedTable.getSelectByPrimaryKeyQueryId());
      sb.append("' as QUERYID,"); //$NON-NLS-1$
    }
    answer.addElement(new TextElement(sb.toString()));
    answer.addElement(this.getBaseColumnListElement());
    if (this.introspectedTable.hasBLOBColumns()) {
      answer.addElement(new TextElement(",")); //$NON-NLS-1$
      answer.addElement(this.getBlobColumnListElement());
    }

    sb.setLength(0);
    sb.append("from "); //$NON-NLS-1$
    // sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
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

      sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
      sb.append(" = "); //$NON-NLS-1$
      // sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
      sb.append(this.getParameterClause(introspectedColumn, null));
      answer.addElement(new TextElement(sb.toString()));
    }

    if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer,
        this.introspectedTable)) {
      // add comment
      CommentGenerator commentGenerator = this.context.getCommentGenerator();
      if (commentGenerator instanceof CustomizationCommentGenerator) {
        ((CustomizationCommentGenerator) commentGenerator).addSelectComment(parentElement);
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
