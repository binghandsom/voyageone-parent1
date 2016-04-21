package com.voyageone.common.util.excel;

/**
 * Created by yuweiquan on 2015-05-05.
 */
public enum EnumExcelColumnType {
    ColumnType_Double(0),
    ColumnType_Date(1),
    ColumnType_Calendar(2),
    ColumnType_String(3),
    ColumnType__BOOLEAN(4),
    ColumnType_ERROR(5);
    private int RowId;
    private EnumExcelColumnType(int id) {
        RowId = id;
    }
    public int GetValue() {
        return RowId;
    }
}
