package com.voyageone.service.impl.cms.jumei.enumjm;
import com.voyageone.common.util.excel.EnumExcelColumnType;
import com.voyageone.common.util.excel.ExcelColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum EnumJMSkuPriceExportColumn {
    JmHashId("jm_hash_id", 1, "cms_bt_jm_promotion_product", " 聚美生成的deal 唯一值"),//商品Code                    jm_bt_sku
    SkuCode("sku_code", 2, "jm_bt_sku", "品牌方SKU(聚美商家商品编码)"),//品牌方SKU(聚美商家商品编码)
    DealPrice("deal_price", 6, "jm_bt_promotion_sku", "团购价格"),//团购价格
    MarketPrice("market_price", 7, "jm_bt_promotion_sku", "市场价格");//市场价格          jm_bt_promotion_sku
    public static int orderIndex;
    private ExcelColumn<Map> excelColumn;
    public ExcelColumn<Map> getExcelColumn() {
        return excelColumn;
    }
    public void setExcelColumn(ExcelColumn<Map> excelColumn) {
        this.excelColumn = excelColumn;
    }
    private EnumJMSkuPriceExportColumn(String columnName, int orderIndex, String tableName, String text) {
        this.excelColumn = new ExcelColumn(columnName, orderIndex, tableName, text);
    }
    private EnumJMSkuPriceExportColumn(String columnName, int orderIndex, String tableName, String text, EnumExcelColumnType columnType, boolean isNull) {
        this.excelColumn = new ExcelColumn(columnName, orderIndex, tableName, text, columnType, isNull);
    }
    private EnumJMSkuPriceExportColumn(String columnName, String tableName, String text, EnumExcelColumnType columnType, boolean isNull)
    {
        this.excelColumn = new ExcelColumn(columnName, EnumCount.ProductColumnIndex++, tableName, text,columnType,isNull);
    }
    public  static List<EnumJMSkuPriceExportColumn> getList()
    {
        List<EnumJMSkuPriceExportColumn> listEnumImportColumn = Arrays.asList(EnumJMSkuPriceExportColumn.values());
        listEnumImportColumn.sort((a, b) -> {
            if (a.getExcelColumn().getOrderIndex() > b.getExcelColumn().getOrderIndex()) return 1;
            return -1;
        });
        return listEnumImportColumn;
    }
    public static  List<ExcelColumn> getListExcelColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        EnumJMSkuPriceExportColumn[] array = EnumJMSkuPriceExportColumn.values();
        for (EnumJMSkuPriceExportColumn column : array) {
            list.add(column.getExcelColumn());
        }
        return list;
    }
}
