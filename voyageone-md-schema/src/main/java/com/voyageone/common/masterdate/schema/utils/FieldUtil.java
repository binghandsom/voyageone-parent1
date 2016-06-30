package com.voyageone.common.masterdate.schema.utils;

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
                    fieldList = ((ComplexField)inputField).getFields();
                    // added by morse.lu 2016/06/23 start
                    // map清一下，方便之后取得map时是转换后的field_id
                    ((ComplexField)inputField).clearMapField();
                    // added by morse.lu 2016/06/23 end
                    break;
                case MULTICOMPLEX:
                    fieldList = ((MultiComplexField)inputField).getFields();
                    // added by morse.lu 2016/06/23 start
                    // map清一下，方便之后取得map时是转换后的field_id
                    ((MultiComplexField)inputField).clearMapField();
                    // added by morse.lu 2016/06/23 end
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
                    fieldList = ((ComplexField)inputField).getFields();
                    // added by morse.lu 2016/06/23 start
                    // map清一下，方便之后取得map时是转换后的field_id
                    ((ComplexField)inputField).clearMapField();
                    // added by morse.lu 2016/06/23 end
                    break;
                case MULTICOMPLEX:
                    fieldList = ((MultiComplexField)inputField).getFields();
                    // added by morse.lu 2016/06/23 start
                    // map清一下，方便之后取得map时是转换后的field_id
                    ((MultiComplexField)inputField).clearMapField();
                    // added by morse.lu 2016/06/23 end
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
                    fieldList = ((ComplexField)inputField).getFields();
                    break;
                case MULTICOMPLEX:
                    fieldList = ((MultiComplexField)inputField).getFields();
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
                    fieldList = ((ComplexField)inputField).getFields();
                    break;
                case MULTICOMPLEX:
                    fieldList = ((MultiComplexField)inputField).getFields();
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
                fieldList = ((ComplexField)inputField).getFields();
                break;
            case MULTICOMPLEX:
                fieldList = ((MultiComplexField)inputField).getFields();
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
                            fieldList = ((ComplexField)rootField).getFields();
                            break;
                        case MULTICOMPLEX:
                            fieldList = ((MultiComplexField)rootField).getFields();
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

    /**
     * 设置Fields的值
     * @param rootFields fields
     * @param valueMap value map
     */
    public static void setFieldsValueFromMap(List<Field> rootFields, Map<String, Object> valueMap){
        if (rootFields != null && valueMap != null) {
            List<Field> delResult = new ArrayList<>();
            for (Field rootField : rootFields) {
                if (rootField.getIsDisplay() == 1) {
                    rootField.setFieldValueFromMap(valueMap);
                } else {
                    delResult.add(rootField);
                }
            }
            rootFields.removeAll(delResult);
        }
    }

    /**
     * 取得Field值到Map中
     * @param rootFields fields
     * @return Value Map
     */
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
