package com.voyageone.common.masterdate.schema.util;

import com.voyageone.common.masterdate.schema.depend.DependExpress;
import com.voyageone.common.masterdate.schema.depend.DependGroup;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.rule.Rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public static void renameDependFieldId(Field inputField, String oldId, String newId, List<Field> rootFields) {
        for (Field field : rootFields) {
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

    /**
     * 根据Name 取得Field list
     */
    public static List<Field> getFieldByName(List<Field> rootFields, String name) {
        List<Field> result = new ArrayList<>();
        if (rootFields != null && name != null) {
            for (Field field : rootFields) {
                if(name.equals(field.getName())) {
                    result.add(field);
                } else {
                    Field outField = getChildFieldByName(field, name);
                    if (outField != null) {
                        result.add(outField);
                    }
                }
            }
        }
        return result;
    }

    private static Field getChildFieldByName(Field inputField, String name) {
        if (inputField != null) {
            List<Field> fieldList = null;
            switch(inputField.getType()) {
                case COMPLEX:
                    fieldList = ((ComplexField)inputField).getFieldList();
                    break;
                case MULTICOMPLEX:
                    fieldList = ((MultiComplexField)inputField).getFieldList();
                    break;
            }

            Field result = null;
            if (fieldList != null) {
                for (Field field : fieldList) {
                    if(field.getName() != null && field.getName().equals(name)) {
                        return field;
                    } else {
                        result = getChildFieldByName(field, name);
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * 根据ID 取得Field
     */
    public static Field getFieldById(List<Field> rootFields, String id) {
        if (rootFields != null && id != null) {
            for (Field field : rootFields) {
                if (id.equals(field.getId())) {
                    return field;
                }
                Field outField = getChildFieldById(field, id);
                if (outField != null) {
                    return outField;
                }
            }
        }
        return null;
    }

    private static Field getChildFieldById(Field inputField, String id) {
        List<Field> fieldList = null;
        switch(inputField.getType()) {
            case COMPLEX:
                fieldList = ((ComplexField)inputField).getFieldList();
                break;
            case MULTICOMPLEX:
                fieldList = ((MultiComplexField)inputField).getFieldList();
                break;
        }

        Field result = null;
        if (fieldList != null) {
            for (Field field : fieldList) {
                if(field.getId() != null && field.getId().equals(id)) {
                    return field;
                } else {
                    result = getChildFieldById(field, id);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 删除指定ID的Field
     */
    public static void removeFieldById(List<Field> rootFields, String id) {
        if (rootFields != null && id != null) {
            Field removeField = null;
            for (Field rootField : rootFields) {
                if (id.equals(rootField.getId())) {
                    removeField = rootField;
                    break;
                } else {
                    List<Field> fieldList = null;
                    switch(rootField.getType()) {
                        case COMPLEX:
                            fieldList = ((ComplexField)rootField).getFieldList();
                            break;
                        case MULTICOMPLEX:
                            fieldList = ((MultiComplexField)rootField).getFieldList();
                            break;
                    }
                    if (fieldList != null) {
                        removeFieldById(fieldList, id);
                    }
                }
            }
            if (removeField != null) {
                rootFields.remove(removeField);
            }
        }
    }


    public static void setFieldsValueFromMap(List<Field> rootFields, Map<String, Object> valueMap){
        if (rootFields != null && valueMap != null) {
            for (Field rootField : rootFields) {
                rootField.setFieldValueFromMap(valueMap);
            }
        }
    }

    public static Map<String, Object> getFieldsValueToMap(List<Field> rootFields){
        Map<String, Object> result = new LinkedHashMap<>();
        if (rootFields != null) {
            for (Field rootField : rootFields) {
                rootField.getFieldValueToMap(result);
            }
        }
        return result;
    }

}
