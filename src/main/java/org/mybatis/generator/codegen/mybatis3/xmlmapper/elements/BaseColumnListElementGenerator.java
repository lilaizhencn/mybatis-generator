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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 *
 * @author Jeff Butler
 *
 */
public class BaseColumnListElementGenerator extends AbstractXmlElementGenerator {

  public BaseColumnListElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", //$NON-NLS-1$
        this.introspectedTable.getBaseColumnListId()));

    this.context.getCommentGenerator().addComment(answer);

    Set<String> idSet = new HashSet<>();
    for (IntrospectedColumn primaryKeyColumns : this.introspectedTable.getPrimaryKeyColumns()) {
      idSet.add(MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumns).toLowerCase());
    }

    Iterator<IntrospectedColumn> iter = this.introspectedTable.getNonBLOBColumns().iterator();
    while (iter.hasNext()) {
      String column = MyBatis3FormattingUtilities.getSelectListPhrase(iter.next());
      // with id
      if (idSet.contains(column.toLowerCase())) {
        StringBuilder sb = new StringBuilder();
        sb.append(column);
        sb.append(", "); //$NON-NLS-1$
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
      }
    }

    XmlElement includeElement = new XmlElement("include");
    includeElement.addAttribute(
        new Attribute("refid", this.introspectedTable.getBaseColumnListId() + "_Without_Id"));
    answer.addElement(includeElement);

    if (this.context.getPlugins().sqlMapBaseColumnListElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
