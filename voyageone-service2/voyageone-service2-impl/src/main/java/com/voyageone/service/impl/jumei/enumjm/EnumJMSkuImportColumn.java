package com.voyageone.service.impl.jumei.enumjm;
import com.voyageone.service.impl.Excel.EnumExcelColumnType;
import com.voyageone.service.impl.Excel.ExcelColumn;
import com.voyageone.service.impl.Excel.FunctionFormatter;
import com.voyageone.service.model.jumei.JmBtProductModel;
import com.voyageone.service.model.jumei.JmBtSkuModel;
import java.util.Map;

public enum EnumJMSkuImportColumn {
    ProductCode("product_code", 1, "jm_bt_sku", "商品Code"),//商品Code                    jm_bt_sku
    sku("sku", 2, "jm_bt_sku", "品牌方SKU(聚美商家商品编码)"),//品牌方SKU(聚美商家商品编码)
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
}
