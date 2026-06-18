package org.mybatis.generator.internal;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 自定义CommentGenerator
 *
 * @author xionghui
 */
public class CustomizationCommentGenerator extends DefaultCommentGenerator {

    public void addExampleClassComment(TopLevelClass topLevelClass,
                                       IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");

        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String remarkLine = String.join(" ", remarks);
            topLevelClass.addJavaDocLine(" * " + remarkLine + " 查询条件类");
        }

        addJavadocTag(topLevelClass);

        topLevelClass.addJavaDocLine(" */");
    }

    public void addRepositoryInterfaceComment(Interface interfaze,
                                              IntrospectedTable introspectedTable) {
        interfaze.addJavaDocLine("/**");

        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String remarkLine = String.join(" ", remarks);
            interfaze.addJavaDocLine(" * " + remarkLine + " 数据访问类");
        }

        addJavadocTag(interfaze);

        interfaze.addJavaDocLine(" */");
    }

    public void addServiceInterfaceComment(Interface interfaze, IntrospectedTable introspectedTable) {
        interfaze.addJavaDocLine("/**");

        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String remarkLine = String.join(" ", remarks);
            interfaze.addJavaDocLine(" * " + remarkLine + " 服务接口");
        }

        addJavadocTag(interfaze);

        interfaze.addJavaDocLine(" */");
    }

    public void addServiceImplClassComment(TopLevelClass topLevelClass,
                                           IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");

        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String remarkLine = String.join(" ", remarks);
            topLevelClass.addJavaDocLine(" * " + remarkLine + " 服务实现");
        }

        addJavadocTag(topLevelClass);

        topLevelClass.addJavaDocLine(" */");
    }

    public void addControllerClassComment(TopLevelClass topLevelClass,
                                          IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");

        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String remarkLine = String.join(" ", remarks);
            topLevelClass.addJavaDocLine(" * " + remarkLine + " Admin Controller");
        }

        addJavadocTag(topLevelClass);

        topLevelClass.addJavaDocLine(" */");
    }

    public void addInsertComment(XmlElement rootElement) {
        rootElement.addElement(new TextElement("<!-- insert -->"));
    }

    public void addDeleteComment(XmlElement rootElement) {
        rootElement.addElement(new TextElement("<!-- end insert -->"));
        rootElement.addElement(new TextElement("<!-- delete -->"));
    }

    public void addUpdateComment(XmlElement rootElement) {
        rootElement.addElement(new TextElement("<!-- end delete -->"));
        rootElement.addElement(new TextElement("<!-- update -->"));
    }

    public void addSelectComment(XmlElement rootElement) {
        rootElement.addElement(new TextElement("<!-- end update -->"));
        rootElement.addElement(new TextElement("<!-- select -->"));
    }

    public void addFinishComment(XmlElement rootElement) {
        rootElement.addElement(new TextElement("<!-- end select -->"));
        rootElement.addElement(new TextElement("<!-- My Custom Interfaces -->"));
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass,
                                     IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");

        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String remarkLine = String.join(" ", remarks);
            topLevelClass.addJavaDocLine(" * " + remarkLine);
        }

        addJavadocTag(topLevelClass);

        topLevelClass.addJavaDocLine(" */");
    }

    private void addJavadocTag(JavaElement javaElement) {
        javaElement.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append("@author lilaizhen");
        javaElement.addJavaDocLine(sb.toString());
        String s = getDateString();
        if (s != null) {
            sb.setLength(0);
            sb.append(" * ");
            sb.append("@date ");
            sb.append(s);
            javaElement.addJavaDocLine(sb.toString());
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        field.addJavaDocLine("/**");

        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator")); //$NON-NLS-1$
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" * " + remarkLine);
            }
        }
        field.addJavaDocLine(" */");
    }
}
