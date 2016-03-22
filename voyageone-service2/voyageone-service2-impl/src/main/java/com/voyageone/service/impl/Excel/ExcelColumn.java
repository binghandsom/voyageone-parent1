package com.voyageone.service.impl.Excel;

/**
 * Created by admin on 2015/10/9.
 */
public class ExcelColumn<T> {
    String text;
    String columnName;
    EnumExcelColumnType columnType;
    FunctionFormatter<Object, T, Integer, Object> formatter;
    double columnWidth;
    public double getColumnWidth() {
        return columnWidth;
    }
    public void setColumnWidth(double columnWidth) {
        this.columnWidth = columnWidth;
    }
    public FunctionFormatter<Object, T, Integer, Object> getFormatter() {
        return formatter;
    }

    public void setFormatter(FunctionFormatter<Object, T, Integer, Object> formatter) {
        this.formatter = formatter;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public EnumExcelColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(EnumExcelColumnType columnType) {
        this.columnType = columnType;
    }

}