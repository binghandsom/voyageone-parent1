package com.voyageone.common.masterdate.schema.Util;

import com.voyageone.common.masterdate.schema.depend.DependExpress;
import com.voyageone.common.masterdate.schema.depend.DependGroup;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.rule.Rule;

import java.util.List;

public class FieldUtil {

    /**
     * 把field中的【.】替换成【->】
     */
    public static void replaceFieldIdDot(List<Field> inputFields) {
        if (inputFields != null) {
            for (Field inputField : inputFields) {
                replaceFieldIdDot(inputField);
            }
        }
    }

    /**
     * 把field中的【.】替换成【->】
     */
    public static void replaceFieldIdDot(Field inputField) {
        if (inputField != null && inputField.getId() != null) {
            inputField.setId(StringUtil.replaceDot(inputField.getId()));
            List<Field> fieldList = null;
            switch(inputField.getType()) {
                case COMPLEX:
                    fieldList = ((ComplexField)inputField).getFieldList();
                    break;
                case MULTICOMPLEX:
                    fieldList = ((MultiComplexField)inputField).getFieldList();
                    break;
            }

            if (fieldList != null) {
                for (Field field : fieldList) {
                    replaceFieldIdDot(field);
                }
            }
        }
    }

    /**
     * 把field中的【->】替换成【.】
     */
    public static void convertFieldIdToDot(List<Field> inputFields) {
        if (inputFields != null) {
            for (Field inputField : inputFields) {
                convertFieldIdToDot(inputField);
            }
        }
    }

    /**
     * 把field中的【->】替换成【.】
     */
    public static void convertFieldIdToDot(Field inputField) {
        if (inputField != null && inputField.getId() != null) {
            inputField.setId(StringUtil.replaceToDot(inputField.getId()));
            List<Field> fieldList = null;
            switch(inputField.getType()) {
                case COMPLEX:
                    fieldList = ((ComplexField)inputField).getFieldList();
                    break;
                case MULTICOMPLEX:
                    fieldList = ((MultiComplexField)inputField).getFieldList();
                    break;
            }

            if (fieldList != null) {
                for (Field field : fieldList) {
                    convertFieldIdToDot(field);
                }
            }
        }
    }

    /**
     * field Id rename And replace Depend fieldId
     */
    public static void fieldRenameId(Field inputField, String newId, List<Field> inputFields) {
        String oldId = inputField.getId();
        inputField.setId(newId);

        for (Field field : inputFields) {
            fieldRenameDepend(field, oldId, newId);
        }
    }

    private static void fieldRenameDepend(Field inputField, String oldId, String newId) {
        if (inputField != null && inputField.getRules() != null && inputField.getRules().size() > 0) {
            List<Rule>  rules = inputField.getRules();
            for (Rule rule : rules) {
                DependGroup dependGroup = rule.getDependGroup();
                fieldRenameDepend(dependGroup, oldId, newId);
            }

            List<Field> fieldList = null;
            switch(inputField.getType()) {
                case COMPLEX:
                    fieldList = ((ComplexField)inputField).getFieldList();
                    break;
                case MULTICOMPLEX:
                    fieldList = ((MultiComplexField)inputField).getFieldList();
                    break;
            }

            if (fieldList != null) {
                for (Field field : fieldList) {
                    fieldRenameDepend(field, oldId, newId);
                }
            }
        }
    }

    private static void fieldRenameDepend(DependGroup inputDependGroup, String oldId, String newId) {
        if (inputDependGroup != null) {
            List<DependExpress> dependExpressList = inputDependGroup.getDependExpressList();
            if (dependExpressList != null) {
                for (DependExpress dependExpress : dependExpressList) {
                    if (oldId.equals(dependExpress.getFieldId())) {
                        dependExpress.setFieldId(newId);
                    }
                }
            }

            List<DependGroup> dependGroupList = inputDependGroup.getDependGroupList();
            if (dependGroupList != null) {
                for (DependGroup dependGroup : dependGroupList) {
                    fieldRenameDepend(dependGroup, oldId, newId);
                }
            }

        }
    }

}
