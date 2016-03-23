package com.voyageone.service.impl.Excel;

import com.voyageone.service.impl.jumei.enumjm.CamelUtil;

/**
 * Created by admin on 2015/10/9.
 */
public class ExcelColumn<T> {
    String text;
    String tableName;
    String columnName;
    String camelColumnName;
    EnumExcelColumnType columnType;
    boolean isNull=true;
    int orderIndex;
    FunctionFormatter<Object, T, Integer, Object> formatter;
    double columnWidth;
    public ExcelColumn()
    {

    }
    public ExcelColumn(String columnName, int orderIndex, String tableName, String text)
    {
        this.columnName=columnName;
        this.camelColumnName= CamelUtil.underlineToCamel(this.columnName);
        this.orderIndex=orderIndex;
        this.tableName=tableName;
        this.text=text;
    }
    public ExcelColumn(String columnName, int orderIndex, String tableName, String text,EnumExcelColumnType columnType,boolean isNull)
    {
        this(columnName,orderIndex,tableName,text);
        this.columnType=columnType;
        this.isNull=isNull;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getCamelColumnName() {
        return camelColumnName;
    }

    public void setCamelColumnName(String camelColumnName) {
        this.camelColumnName = camelColumnName;
    }

    public EnumExcelColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(EnumExcelColumnType columnType) {
        this.columnType = columnType;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public FunctionFormatter<Object, T, Integer, Object> getFormatter() {
        return formatter;
    }

    public void setFormatter(FunctionFormatter<Object, T, Integer, Object> formatter) {
        this.formatter = formatter;
    }

    public double getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(double columnWidth) {
        this.columnWidth = columnWidth;
    }
}