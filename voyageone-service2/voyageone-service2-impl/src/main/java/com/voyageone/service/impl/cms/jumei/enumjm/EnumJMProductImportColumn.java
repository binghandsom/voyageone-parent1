package com.voyageone.service.impl.cms.jumei.enumjm;
import com.voyageone.common.util.excel.EnumExcelColumnType;
import com.voyageone.common.util.excel.ExcelColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum EnumJMProductImportColumn {
    ProductCode("product_code","cms_bt_jm_product","产品编号",EnumExcelColumnType.ColumnType_String,false),//Acms_bt_jm_product
    AppId("app_id","cms_bt_jm_promotion_product","APP端模块ID"),//APP端模块ID
    PcId("pc_id","cms_bt_jm_promotion_product","PC端模块ID"),//Acms_bt_jm_product
    Limit("limit","cms_bt_jm_promotion_product","Deal每人限购"),//jm_bt_promotion_product
    PromotionTag("promotion_tag","cms_bt_jm_product","专场标签（以|分隔");
    private ExcelColumn<Map> excelColumn;
    public ExcelColumn<Map> getExcelColumn() {
        return excelColumn;
    }
    public void setExcelColumn(ExcelColumn<Map> excelColumn) {
        this.excelColumn = excelColumn;
    }
    private EnumJMProductImportColumn(String columnName, String tableName, String text) {
        this.excelColumn = new ExcelColumn(columnName, EnumCount.ProductColumnIndex++, tableName, text);
    }
    private EnumJMProductImportColumn(String columnName, String tableName, String text, EnumExcelColumnType columnType, boolean isNull)
    {
        this.excelColumn = new ExcelColumn(columnName, EnumCount.ProductColumnIndex++, tableName, text,columnType,isNull);
    }
    public  static  List<EnumJMProductImportColumn> getList()
    {
        List<EnumJMProductImportColumn> listEnumJMProductImportColumn = Arrays.asList(EnumJMProductImportColumn.values());
        listEnumJMProductImportColumn.sort((a, b) -> {
            if (a.getExcelColumn().getOrderIndex() > b.getExcelColumn().getOrderIndex()) return 1;
            return -1;
        });
        return listEnumJMProductImportColumn;
    }
    public static  List<ExcelColumn> getListExcelColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        EnumJMProductImportColumn[] array = EnumJMProductImportColumn.values();
        for (EnumJMProductImportColumn column : array) {
                list.add(column.getExcelColumn());
        }
        return list;
    }
}
