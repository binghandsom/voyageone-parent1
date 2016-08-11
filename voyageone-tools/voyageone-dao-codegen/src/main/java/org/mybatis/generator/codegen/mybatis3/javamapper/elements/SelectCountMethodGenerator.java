package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

public class SelectCountMethodGenerator extends AbstractJavaMapperMethodGenerator {

    public SelectCountMethodGenerator(boolean isSimple) {
        super();
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        method.setName(introspectedTable.getSelectCountStatementId());

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getIntInstance();
        method.setReturnType(returnType);

        FullyQualifiedJavaType mapParameterType = new FullyQualifiedJavaType("Object");
        method.addParameter(new Parameter(mapParameterType, "map")); //$NON-NLS-1$

        addMapperAnnotations(interfaze, method);

        importedTypes.add(returnType);
        importedTypes.add(mapParameterType);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }
}
