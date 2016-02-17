package com.voyageone.common.masterdate.schema.depend;


import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.MultiInputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.value.Value;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DependExpress {

//    public static final String SYMBOL_IS_NULL = "is null";
//    public static final String SYMBOL_EQUALS = "==";
//    public static final String SYMBOL_NOT_EQUALS = "!=";
//    public static final String SYMBOL_GREATER = ">";
//    public static final String SYMBOL_LESS = "<";
//    public static final String SYMBOL_GREATER_AND_EQUALS = ">=";
//    public static final String SYMBOL_LESS_AND_EQUALS = "<=";
//    public static final String SYMBOL_CONTAINS = "contains";
//    public static final String SYMBOL_NOT_CONTAINS = "not contains";
//    public static final String SYMBOL_IN_FIELDOPTIONS = "this field\'s value in fieldOptions";
//    public static final String SYMBOL_NOT_IN_FIELDOPTIONS = "this field\'s value not in fieldOptions";
    protected String fieldId;
    protected String value;
    protected String symbol = "==";

    public DependExpress() {
    }

    public boolean excuteExpress(Map<String, Field> fieldMap) {
        Field field = fieldMap.get(this.fieldId);
        if(field == null) {
            return false;
        } else {
            List values = this.getFieldValues(field);
            if(values != null && !values.isEmpty()) {
                Iterator contains;
                String i$;
                if("==".equals(this.symbol)) {
                    contains = values.iterator();

                    while(contains.hasNext()) {
                        i$ = (String)contains.next();
                        if(!i$.equals(this.value)) {
                            return false;
                        }
                    }
                } else if("!=".equals(this.symbol)) {
                    contains = values.iterator();

                    while(contains.hasNext()) {
                        i$ = (String)contains.next();
                        if(i$.equals(this.value)) {
                            return false;
                        }
                    }
                } else {
                    Iterator fieldValue;
                    String fieldValue1;
                    double fieldValueDouble;
                    double contains1;
                    if(">".equals(this.symbol)) {
                        try {
                            contains1 = Double.valueOf(this.value);
                            fieldValue = values.iterator();

                            while(fieldValue.hasNext()) {
                                fieldValue1 = (String)fieldValue.next();
                                fieldValueDouble = Double.valueOf(fieldValue1);
                                if(fieldValueDouble <= contains1) {
                                    return false;
                                }
                            }
                        } catch (Exception var13) {
                            return false;
                        }
                    } else if("<".equals(this.symbol)) {
                        try {
                            contains1 = Double.valueOf(this.value);
                            fieldValue = values.iterator();

                            while(fieldValue.hasNext()) {
                                fieldValue1 = (String)fieldValue.next();
                                fieldValueDouble = Double.valueOf(fieldValue1);
                                if(fieldValueDouble >= contains1) {
                                    return false;
                                }
                            }
                        } catch (Exception var12) {
                            return false;
                        }
                    } else if(">=".equals(this.symbol)) {
                        try {
                            contains1 = Double.valueOf(this.value);
                            fieldValue = values.iterator();

                            while(fieldValue.hasNext()) {
                                fieldValue1 = (String)fieldValue.next();
                                fieldValueDouble = Double.valueOf(fieldValue1);
                                if(fieldValueDouble < contains1) {
                                    return false;
                                }
                            }
                        } catch (Exception var11) {
                            return false;
                        }
                    } else if("<=".equals(this.symbol)) {
                        try {
                            contains1 = Double.valueOf(this.value);
                            fieldValue = values.iterator();

                            while(fieldValue.hasNext()) {
                                fieldValue1 = (String)fieldValue.next();
                                fieldValueDouble = Double.valueOf(fieldValue1);
                                if(fieldValueDouble > contains1) {
                                    return false;
                                }
                            }
                        } catch (Exception var10) {
                            return false;
                        }
                    } else {
                        Iterator i$1;
                        boolean contains2;
                        String fieldValue2;
                        if("contains".equals(this.symbol)) {
                            contains2 = false;
                            i$1 = values.iterator();

                            while(i$1.hasNext()) {
                                fieldValue2 = (String)i$1.next();
                                if(fieldValue2.equals(this.value)) {
                                    contains2 = true;
                                    break;
                                }
                            }

                            return contains2;
                        }

                        if("not contains".equals(this.symbol)) {
                            contains2 = true;
                            i$1 = values.iterator();

                            while(i$1.hasNext()) {
                                fieldValue2 = (String)i$1.next();
                                if(fieldValue2.equals(this.value)) {
                                    contains2 = false;
                                    break;
                                }
                            }

                            return contains2;
                        }
                    }
                }

                return true;
            } else {
                return "is null".equals(this.symbol);
            }
        }
    }

    protected List<String> getFieldValues(Field field) {
        List<String> values = new ArrayList<>();
        switch(field.getType()) {
            case INPUT:
                InputField inputField = (InputField)field;
                values.add(inputField.getValue());
                break;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField)field;
                values.add(singleCheckField.getValue().getValue());
                break;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField)field;
                List vList = multiCheckField.getValues();

                for (Object aVList : vList) {
                    Value viList1 = (Value) aVList;
                    values.add(viList1.getValue());
                }

                return values;
            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField)field;
                List viList = multiInputField.getValues();

                for (Object aViList : viList) {
                    Value v = (Value) aViList;
                    values.add(v.getValue());
                }
        }

        return values;
    }

    public String getFieldId() {
        return this.fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
