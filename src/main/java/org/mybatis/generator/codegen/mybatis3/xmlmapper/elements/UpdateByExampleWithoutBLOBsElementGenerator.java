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

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 *
 * @author Jeff Butler
 *
 */
public class UpdateByExampleWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator {

  public UpdateByExampleWithoutBLOBsElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

    answer.addAttribute(new Attribute("id", //$NON-NLS-1$
        this.introspectedTable.getUpdateByExampleStatementId()));

    answer.addAttribute(new Attribute("parameterType", "java.util.Map")); //$NON-NLS-1$ //$NON-NLS-2$

    this.context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    sb.append("update "); //$NON-NLS-1$
    // sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));
    // set up for first column
    sb.setLength(0);

    answer.addElement(this.getTableNameIncludeElement());

    XmlElement setElement = new XmlElement("set");
    XmlElement includeElement = new XmlElement("include");
    includeElement.addAttribute(new Attribute("refid", "Update_Set_From_Bean"));
    setElement.addElement(includeElement);

    answer.addElement(setElement);

    answer.addElement(this.getExampleIncludeElement());

    if (this.context.getPlugins().sqlMapUpdateByExampleWithoutBLOBsElementGenerated(answer,
        this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
