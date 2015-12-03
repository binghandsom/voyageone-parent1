package com.voyageone.common.masterdate.schema.Util;

import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.MultiInputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.value.Value;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ISPUtil {
    public ISPUtil() {
    }

    public static Value getFieldValue(Field field) {
        Value value = new Value();
        switch(field.getType()) {
            case INPUT:
                InputField inputField = (InputField)field;
                value.setValue(inputField.getValue());
                break;
            case MULTIINPUT:
                value = null;
                break;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField)field;
                value = singleCheckField.getValue();
                break;
            case MULTICHECK:
                value = null;
                break;
            case COMPLEX:
                value = null;
                break;
            case MULTICOMPLEX:
                value = null;
                break;
            case LABEL:
                value = null;
                break;
            default:
                value = null;
        }

        return value;
    }

    public static String getFieldStringValue(Field field) {
        Value value = getFieldValue(field);
        return value == null?null:value.getValue();
    }

    public static List<Value> getFieldValues(Field field) {
        new ArrayList();
        List values;
        switch(field.getType()) {
            case INPUT:
                values = null;
                break;
            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField)field;
                values = multiInputField.getValues();
                break;
            case SINGLECHECK:
                values = null;
                break;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField)field;
                values = multiCheckField.getValues();
                break;
            case COMPLEX:
                values = null;
                break;
            case MULTICOMPLEX:
                values = null;
                break;
            case LABEL:
                values = null;
                break;
            default:
                values = null;
        }

        return values;
    }

    public static List<String> getFieldStringValues(Field field) {
        List vList = getFieldValues(field);
        ArrayList list = new ArrayList();
        Iterator i$ = vList.iterator();

        while(i$.hasNext()) {
            Value v = (Value)i$.next();
            list.add(v.getValue());
        }

        return list;
    }

    public static Date parseDate(String fieldValue) throws TopSchemaException {
        try {
            String e = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(e);
            return sdf.parse(fieldValue);
        } catch (ParseException var3) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_20032, (String)null);
        }
    }

    public static Date parseTime(String fieldValue) throws TopSchemaException {
        try {
            String e = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(e);
            return sdf.parse(fieldValue);
        } catch (ParseException var3) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_20033, (String)null);
        }
    }
}
