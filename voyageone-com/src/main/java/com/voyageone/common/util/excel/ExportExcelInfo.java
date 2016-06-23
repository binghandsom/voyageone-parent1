package com.voyageone.common.util.excel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/10/10.
 */
public class ExportExcelInfo<TRow> {

    public ExportExcelInfo(List<TRow> dataSource) {
        this.setDataSource(dataSource);
    }

    List<ExcelColumn<TRow>> listColumn = new ArrayList<ExcelColumn<TRow>>();
    String fileName;
    List<TRow> dataSource;
    String Sheet;
    boolean IsDisplayColumnName=false;

    public boolean isDisplayColumnName() {
        return IsDisplayColumnName;
    }

    public void setDisplayColumnName(boolean displayColumnName) {
        IsDisplayColumnName = displayColumnName;
    }

    public String getSheet() {
        return Sheet;
    }

    public void setSheet(String sheet) {
        Sheet = sheet;
    }

    public List<TRow> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<TRow> dataSource) {
        this.dataSource = dataSource;
    }

    public List<ExcelColumn<TRow>> getListColumn() {
        return listColumn;
    }

    public void setListColumn(List<ExcelColumn<TRow>> listColumn) {
        this.listColumn = listColumn;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void addExcelColumn(ExcelColumn column) {
        listColumn.add(column);
    }

    public ExcelColumn<TRow> addExcelColumn(String text, String columnName) {
        ExcelColumn<TRow> column = new ExcelColumn<TRow>();
        column.setText(text);
        column.setColumnName(columnName);
        column.setColumnType(EnumExcelColumnType.ColumnType_String);
        this.getListColumn().add(column);
        return column;
    }
    public ExcelColumn<TRow> addExcelColumn(String text, String columnName,String tableName,short colorIndex) {
        ExcelColumn<TRow> column = new ExcelColumn<TRow>();
        column.setText(text);
        column.setColumnName(columnName);
        column.setColumnType(EnumExcelColumnType.ColumnType_String);
        column.setTableName(tableName);
        column.setColorIndex(colorIndex);
        this.getListColumn().add(column);
        return column;
    }
    public ExcelColumn addExcelColumn(String text, String columnName, EnumExcelColumnType columnType) {
        ExcelColumn<TRow> column = addExcelColumn(text, columnName);
        column.setColumnType(columnType);
        return column;
    }
    public ExcelColumn<TRow> addExcelColumn(String text, String columnName,String tableName) {
        ExcelColumn<TRow> column = new ExcelColumn<TRow>();
        column.setText(text);
        column.setColumnName(columnName);
        column.setColumnType(EnumExcelColumnType.ColumnType_String);
        column.setTableName(tableName);
        this.getListColumn().add(column);
        return column;
    }
    public ExcelColumn<TRow> addExcelColumn(String text, String columnName, FunctionFormatter<Object, TRow, Integer, Object> formatter) {
        ExcelColumn<TRow> column = addExcelColumn(text, columnName);
        column.setFormatter(formatter);
        return column;
    }
    public ExcelColumn<TRow> addExcelColumn(String text, String columnName, FunctionFormatter<Object, TRow, Integer, Object> formatter, EnumExcelColumnType columnType) {
        ExcelColumn column = addExcelColumn(text, columnName, formatter);
        column.setColumnType(columnType);
        return column;
    }
    public ExcelColumn<TRow> getErrorColumn() {
        ExcelColumn<TRow> column = new ExcelColumn<TRow>();
        column.setText("异常信息");
        column.setColumnName("errorMsg");//errorMsg
        column.setColumnType(EnumExcelColumnType.ColumnType_String);
        column.setCamelColumnName("errorMsg");
        return column;
    }
}
