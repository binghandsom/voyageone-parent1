package com.voyageone.service.model.jumei.jmenum;
public enum EnumJMSkuImportColumn {
    ProductCode("product_code",0,"jm_bt_sku","商品Code"),//商品Code                    jm_bt_sku
    sku("sku",0,"jm_bt_sku","品牌方SKU(聚美商家商品编码)"),//品牌方SKU(聚美商家商品编码)
    upc("upc",0,"jm_bt_sku","商品条形码"),//商品条形码
    cms_size("cms_size",0,"jm_bt_sku","尺码(VO系统)"),//尺码(VO系统)                     jm_bt_sku

    jm_size("jm_size",0,"jm_bt_promotion_sku","尺码（聚美系统"),//尺码（聚美系统）      jm_bt_promotion_sku
    deal_price("deal_price",0,"jm_bt_promotion_sku","团购价格"),//团购价格
    market_price("market_price",0,"jm_bt_promotion_sku","市场价格");//市场价格          jm_bt_promotion_sku


    // 成员变量
    private String columnName;
    private String tableName;
    private int orderIndex;
    private String description;
    private EnumJMSkuImportColumn(String columnName, int orderIndex, String tableName, String description)
    {
        this.columnName=columnName;
        this.orderIndex=orderIndex;
        this.tableName=tableName;
        this.description=description;
    }
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
