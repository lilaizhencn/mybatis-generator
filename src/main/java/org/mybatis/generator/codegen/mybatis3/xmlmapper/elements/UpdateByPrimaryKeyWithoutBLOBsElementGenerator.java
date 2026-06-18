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
public class UpdateByPrimaryKeyWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator {

  public UpdateByPrimaryKeyWithoutBLOBsElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

    // answer.addAttribute(new Attribute("id",
    // this.introspectedTable.getUpdateByPrimaryKeyStatementId()));
    answer.addAttribute(new Attribute("id", "updateById"));
    answer.addAttribute(new Attribute("parameterType", "java.util.Map"));

    this.context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    sb.append("update "); //$NON-NLS-1$
    // sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));
    // set up for first column
    sb.setLength(0);

    answer.addElement(this.getTableNameIncludeElement());

    XmlElement setElement = new XmlElement("set");
    XmlElement includeElement = new XmlElement("include");
    includeElement.addAttribute(new Attribute("refid", "Update_Set_From_Bean"));
    setElement.addElement(includeElement);

    answer.addElement(setElement);

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
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));
      answer.addElement(new TextElement(sb.toString()));
    }

    if (this.context.getPlugins().sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(answer,
        this.introspectedTable)) {
      // add comment
      CommentGenerator commentGenerator = this.context.getCommentGenerator();
      if (commentGenerator instanceof CustomizationCommentGenerator) {
        ((CustomizationCommentGenerator) commentGenerator).addUpdateComment(parentElement);
      }

      parentElement.addElement(answer);
    }
  }
}
