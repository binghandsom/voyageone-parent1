package com.voyageone.service.impl.jumei.enumjm;

import com.voyageone.service.impl.Excel.EnumExcelColumnType;
import com.voyageone.service.impl.Excel.FunctionFormatter;
import com.voyageone.service.model.jumei.JmBtProductModel;
import com.voyageone.service.model.jumei.JmBtSkuModel;

public enum EnumJMSkuImportColumn implements EnumImport<JmBtSkuModel> {
    ProductCode("product_code",0,"jm_bt_sku","商品Code"),//商品Code                    jm_bt_sku
    sku("sku",0,"jm_bt_sku","品牌方SKU(聚美商家商品编码)"),//品牌方SKU(聚美商家商品编码)
    upc("upc",0,"jm_bt_sku","商品条形码"),//商品条形码
    cms_size("cms_size",0,"jm_bt_sku","尺码(VO系统)"),//尺码(VO系统)                     jm_bt_sku

    jm_size("jm_size",0,"jm_bt_promotion_sku","尺码（聚美系统"),//尺码（聚美系统）      jm_bt_promotion_sku
    deal_price("deal_price",0,"jm_bt_promotion_sku","团购价格"),//团购价格
    market_price("market_price",0,"jm_bt_promotion_sku","市场价格");//市场价格          jm_bt_promotion_sku

    private String columnName;
    private String tableName;
    private int orderIndex;
    private String text;
    public EnumExcelColumnType columnType;
    public double columnWidth;
    public FunctionFormatter<Object, JmBtSkuModel, Integer, Object> formatter;
    private EnumJMSkuImportColumn(String _columnName, int _orderIndex, String _tableName, String _description)
    {
        this.columnName=_columnName;
        this.orderIndex=_orderIndex;
        this.tableName=_tableName;
        this.text=_description;
    }
    @Override
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public EnumExcelColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(EnumExcelColumnType columnType) {
        this.columnType = columnType;
    }

    @Override
    public double getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(double columnWidth) {
        this.columnWidth = columnWidth;
    }

    @Override
    public FunctionFormatter<Object, JmBtSkuModel, Integer, Object> getFormatter() {
        return formatter;
    }

    public void setFormatter(FunctionFormatter<Object, JmBtSkuModel, Integer, Object> formatter) {
        this.formatter = formatter;
    }
}
