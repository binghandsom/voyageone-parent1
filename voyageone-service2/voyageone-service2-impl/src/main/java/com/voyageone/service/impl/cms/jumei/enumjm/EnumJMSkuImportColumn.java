package com.voyageone.service.impl.cms.jumei.enumjm;
import com.voyageone.common.util.excel.EnumExcelColumnType;
import com.voyageone.common.util.excel.ExcelColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum EnumJMSkuImportColumn {
    ProductCode("product_code", 1, "jm_bt_sku", "商品Code",EnumExcelColumnType.ColumnType_String,false),//商品Code                    jm_bt_sku
    SkuCode("sku_code", 2, "jm_bt_sku", "品牌方SKU(聚美商家商品编码)"),//品牌方SKU(聚美商家商品编码)
    upc("upc", 3, "jm_bt_sku", "商品条形码"),//商品条形码
    cms_size("cms_size", 4, "jm_bt_sku", "尺码(VO系统)"),//尺码(VO系统)                     jm_bt_sku
    jm_size("jm_size", 5, "jm_bt_promotion_sku", "尺码（聚美系统"),//尺码（聚美系统）      jm_bt_promotion_sku
    deal_price("deal_price", 6, "jm_bt_promotion_sku", "团购价格"),//团购价格
    market_price("market_price", 7, "jm_bt_promotion_sku", "市场价格");//市场价格          jm_bt_promotion_sku
    public static int orderIndex;
    private ExcelColumn<Map> excelColumn;
    public ExcelColumn<Map> getExcelColumn() {
        return excelColumn;
    }
    public void setExcelColumn(ExcelColumn<Map> excelColumn) {
        this.excelColumn = excelColumn;
    }
    private EnumJMSkuImportColumn(String columnName, int orderIndex, String tableName, String text) {
        this.excelColumn = new ExcelColumn(columnName, orderIndex, tableName, text);
    }
    private EnumJMSkuImportColumn(String columnName, int orderIndex, String tableName, String text, EnumExcelColumnType columnType, boolean isNull) {
        this.excelColumn = new ExcelColumn(columnName, orderIndex, tableName, text, columnType, isNull);
    }
    private EnumJMSkuImportColumn(String columnName, String tableName, String text,EnumExcelColumnType columnType,boolean isNull)
    {
        this.excelColumn = new ExcelColumn(columnName, EnumCount.ProductColumnIndex++, tableName, text,columnType,isNull);
    }
    public  static List<EnumJMSkuImportColumn> getList()
    {
        List<EnumJMSkuImportColumn> listEnumImportColumn = Arrays.asList(EnumJMSkuImportColumn.values());
        listEnumImportColumn.sort((a, b) -> {
            if (a.getExcelColumn().getOrderIndex() > b.getExcelColumn().getOrderIndex()) return 1;
            return -1;
        });
        return listEnumImportColumn;
    }
    public static  List<ExcelColumn> getListExcelColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        EnumJMSkuImportColumn[] array = EnumJMSkuImportColumn.values();
        for (EnumJMSkuImportColumn column : array) {
            list.add(column.getExcelColumn());
        }
        return list;
    }
}
