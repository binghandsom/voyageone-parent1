package com.voyageone.common.masterdate.schema.enums;


public enum TopSchemaErrorCodeEnum {
    ERROR_CODE_10001("10001", "Cannot find File!Please check your file path!"),
    ERROR_CODE_10002("10002", "Create Document Exception!ParserConfigurationException!"),
    ERROR_CODE_20010("20010", "Illegal Value,MaxLengthRule check failed!"),
    ERROR_CODE_20020("20020", "Illegal Value,MinLengthRule check failed!"),
    ERROR_CODE_20030("20030", "Illegal Value,ValueTypeRule check failed!Field\'s value is not a decimal!"),
    ERROR_CODE_20031("20031", "Illegal Value,ValueTypeRule check failed!Field\'s value is not a integer!"),
    ERROR_CODE_20032("20032", "Illegal Value,ValueTypeRule check failed!Field\'s value is not a date!"),
    ERROR_CODE_20033("20033", "Illegal Value,ValueTypeRule check failed!Field\'s value is not a time!"),
    ERROR_CODE_20034("20034", "Illegal Value,ValueTypeRule check failed!Field\'s value is not a url!"),
    ERROR_CODE_20040("20040", "RequiredRule check failed!Field\'s value should not be null!"),
    ERROR_CODE_20050("20050", "MaxValueRule check failed!Field\'s value is not a Number!"),
    ERROR_CODE_20051("20051", "MaxValueRule check failed!"),
    ERROR_CODE_20060("20060", "MinValueRule check failed!Field\'s value is not a Number!"),
    ERROR_CODE_20061("20061", "MinValueRule check failed!"),
    ERROR_CODE_20070("20070", "MaxCheckNumRule check failed!"),
    ERROR_CODE_20080("20080", "MinCheckNumRule check failed!"),
    ERROR_CODE_20090("20090", "InputRule check failed!FieldValue is not from fieldOptions!"),
    ERROR_CODE_20100("20090", "RegexRule check failed!FieldValue not matches regex!"),
    ERROR_CODE_30001("31001", "Field format error!Field\'s id should not be null!"),
    ERROR_CODE_30002("31002", "Field format error!Field\'s type should not be null!"),
    ERROR_CODE_30003("30003", "Field format error!Field\'s type is illegal!"),
    ERROR_CODE_31001("31001", "Rule format error!Rule\'s name should not be null!"),
    ERROR_CODE_31002("31002", "Rule format error!Rule\'s value should not be null!"),
    ERROR_CODE_31003("31003", "Rule format error!Rule\'s value is illegal!"),
    ERROR_CODE_32001("32001", "Depend format error!Depend\'s fieldId should not be null!"),
    ERROR_CODE_32002("32002", "Depend format error!Depend\'s value should not be null!"),
    ERROR_CODE_33001("33001", "Option format error!Option\'s displayName should not be null!"),
    ERROR_CODE_33002("33002", "Option format error!Option\'s value should not be null!"),
    ERROR_CODE_30010("30010", "MaxLengthRule format error!MaxLengthRule is not an Integer!"),
    ERROR_CODE_30020("30020", "MinLengthRule format error!MinLengthRule is not an Integer!"),
    ERROR_CODE_30030("30030", "ValueTypeRule format error!ValueType is illegal!"),
    ERROR_CODE_30050("30050", "MaxValueRule format error!MaxValueRule is not an Integer!"),
    ERROR_CODE_30060("30060", "MinValueRule format error!MinValueRule is not an Integer!"),
    ERROR_CODE_30070("30070", "MaxCheckNumRule format error!MaxCheckNumRule is not an Integer!"),
    ERROR_CODE_30080("30080", "MinCheckNumRule format error!MinCheckNumRule is not an Integer!"),
    ERROR_CODE_40000("40000", "util Error!");

    private final String errorCode;
    private final String errorMsg;

    private TopSchemaErrorCodeEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}
