package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Ethan Shi on 2016/5/4.
 */
public class SelectOneMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private boolean isSimple;

    public SelectOneMethodGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
//        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance().getNewListInstance();
//        returnType.addTypeArgument(parameterType);

        FullyQualifiedJavaType mapParameterType = new FullyQualifiedJavaType("java.util.Map<String, Object>");

        method.setReturnType(FullyQualifiedJavaType.getNewListInstance());

        method.setReturnType(parameterType);

//        importedTypes.add(returnType);
        importedTypes.add(parameterType);
        importedTypes.add(mapParameterType);

        method.setName(introspectedTable.getSelectOneStatementId());


        method.addParameter(new Parameter(mapParameterType, "map")); //$NON-NLS-1$

        addMapperAnnotations(interfaze, method);

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(
                method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
        return;
    }
}
