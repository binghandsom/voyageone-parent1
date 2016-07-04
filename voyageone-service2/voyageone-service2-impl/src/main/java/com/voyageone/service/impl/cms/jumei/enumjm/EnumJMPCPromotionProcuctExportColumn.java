package com.voyageone.service.impl.cms.jumei.enumjm;

import com.voyageone.common.util.excel.EnumExcelColumnType;
import com.voyageone.common.util.excel.ExcelColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumJMPCPromotionProcuctExportColumn {
    JmHashId("jm_hash_id", 1, "cms_bt_jm_promotion_product", " 聚美生成的deal 唯一值"),//商品Code                    jm_bt_sku
    ProductCode("product_code", 2, "cms_bt_jm_promotion_product", "ProductCode"),//品牌方SKU(聚美商家商品编码)
    productNameCn("product_code", 2, "cms_bt_jm_product", "产品名称"),
    DealPrice("deal_price", 6, "cms_bt_jm_promotion_product", "团购价格"),//团购价格
    MarketPrice("market_price", 7, "cms_bt_jm_promotion_product", "市场价格"),//市场价格
    Discount("discount", 7, "cms_bt_jm_promotion_product", "折扣"),//市场价格
    SkuCount("sku_count", 7, "cms_bt_jm_promotion_product", "SKU数"),//市场价格
    Quantity("quantity", 7, "cms_bt_jm_promotion_product", "库存");//市场价格

    //public static int orderIndex;
    private ExcelColumn excelColumn;

    public ExcelColumn getExcelColumn() {
        return excelColumn;
    }

    public void setExcelColumn(ExcelColumn excelColumn) {
        this.excelColumn = excelColumn;
    }

    private EnumJMPCPromotionProcuctExportColumn(String columnName, int orderIndex, String tableName, String text) {
        this.excelColumn = new ExcelColumn(columnName, orderIndex, tableName, text);
    }

    private EnumJMPCPromotionProcuctExportColumn(String columnName, int orderIndex, String tableName, String text, EnumExcelColumnType columnType, boolean isNull) {
        this.excelColumn = new ExcelColumn(columnName, orderIndex, tableName, text, columnType, isNull);
    }

    //    private EnumJMPCPromotionProcuctExportColumn(String columnName, String tableName, String text, EnumExcelColumnType columnType, boolean isNull)
//    {
//        this.excelColumn = new ExcelColumn(columnName, EnumCount.ProductColumnIndex++, tableName, text,columnType,isNull);
//    }
    public static List<EnumJMPCPromotionProcuctExportColumn> getList() {
        List<EnumJMPCPromotionProcuctExportColumn> listEnumImportColumn = Arrays.asList(EnumJMPCPromotionProcuctExportColumn.values());
        listEnumImportColumn.sort((a, b) -> {
            if (a.getExcelColumn().getOrderIndex() > b.getExcelColumn().getOrderIndex()) return 1;
            return -1;
        });
        return listEnumImportColumn;
    }

    public static List<ExcelColumn> getListExcelColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        EnumJMPCPromotionProcuctExportColumn[] array = EnumJMPCPromotionProcuctExportColumn.values();
        for (EnumJMPCPromotionProcuctExportColumn column : array) {
            list.add(column.getExcelColumn());
        }
        return list;
    }
}
