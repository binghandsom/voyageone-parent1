package com.voyageone.service.impl.jumei.enumjm;
import com.voyageone.service.impl.Excel.EnumExcelColumnType;
import com.voyageone.service.impl.Excel.FunctionFormatter;
import com.voyageone.service.impl.jumei.JmBtProductService;
import com.voyageone.service.model.jumei.JmBtProductModel;
public enum EnumJMProductImportColumn implements EnumImport<JmBtProductModel> {
    ProductCode("product_code",0,"jm_bt_product","商品code 唯一标识一个商品"),//Ajm_bt_product
    ForeignLanguageName("foreign_language_name",1,"jm_bt_product","商品英文名称"),//B
    ProductNameCn("product_name_cn",2,"jm_bt_product","商品中文名称"),//C
    ProductLongName("product_long_name",3,"jm_bt_product","商品长标题（聚美系统）"),//D
    ProductMediumName("product_medium_name",4,"jm_bt_product","中标题（聚美系统）"),//E
    VoCategoryName("vo_category_name",5,"jm_bt_product","vo系统自用类目（Feed类目或CMS类目）"),//G
    CategoryLv1Name("category_lv1_name",6,"jm_bt_product","聚美一级类目"),//H
    CategoryLv2Name("category_lv2_name",7,"jm_bt_product","聚美二级类目"),//I
    CategoryLv3Name("category_lv3_name",8,"jm_bt_product","聚美三级类目"),//J
    CategoryLv4Name("category_lv4_name",9,"jm_bt_product","聚美四级类目"),//K
    CategoryLv4Id("category_lv4_id",10,"jm_bt_product","聚美第四级类目id"),//L
    BrandName("brand_name",11,"jm_bt_product","品牌名"),//N
    BrandId("brand_id",12,"jm_bt_product","聚美品牌id"),//O
    ProductType("product_type",13,"jm_bt_product",""),//P
    SizeType("size_type",14,"jm_bt_product","商品类型"),//Q
    ColorEn("color_en",15,"jm_bt_product",""),//R
    Attribute("attribute",16,"jm_bt_product","现在仅仅设置颜色"),//S
    Origin("origin",17,"jm_bt_product","英文产地"),//T
    AddressOfProduce("address_of_produce",18,"jm_bt_product","中文产地"),//U
    MaterialEn("material_en",19,"jm_bt_product","英文材质（确定报关税号用）"),//V
    MaterialCn("material_cn",20,"jm_bt_product","中文材质（确定报关税号用）"),//W
    ProductDesEn("product_des_en",21,"jm_bt_product","商品英文描述"),//X
    ProductDesCn("product_des_cn",22,"jm_bt_product","商品中文描述"),//Y
    AvailablePeriod("available_period",23,"jm_bt_product","保质期"),//Z
    ApplicableCrowd("applicable_crowd",24,"jm_bt_product","适用人群"),//AA
    SearchMetaTextCustom("search_meta_text_custom",25,"jm_bt_product","自定义搜索词（聚美系统）"),//AB
    SpecialNote("special_note",26,"jm_bt_product","特别说明 用于聚美上新"),//AC
    Msrp("msrp",27,"jm_bt_product","海外官网价格"),//AD                                                           jm_bt_product
    limit("limit",28,"jm_bt_promotion_product","每人限购（聚美系统）");//jm_bt_promotion_product
    // 成员变量
    private String columnName;
    private String tableName;
    private int orderIndex;
    private String text;
    public EnumExcelColumnType columnType;
    public double columnWidth;
    public FunctionFormatter<Object, JmBtProductModel, Integer, Object> formatter;
    private EnumJMProductImportColumn(String columnName, int orderIndex, String tableName, String text)
    {
        this.columnName=columnName;
        this.orderIndex=orderIndex;
        this.tableName=tableName;
        this.text=text;
    }
    private EnumJMProductImportColumn(String columnName, int orderIndex, String tableName, String text,EnumExcelColumnType columnType) {
        this.columnName = columnName;
        this.orderIndex = orderIndex;
        this.tableName = tableName;
        this.text = text;
        this.columnType = columnType;
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
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public EnumExcelColumnType getColumnType() {
        return columnType;
    }
    public void setColumnType(EnumExcelColumnType columnType) {
        this.columnType = columnType;
    }
    @Override
    public FunctionFormatter<Object, JmBtProductModel, Integer, Object> getFormatter() {
        return formatter;
    }
    public void setFormatter(FunctionFormatter<Object, JmBtProductModel, Integer, Object> formatter) {
        this.formatter = formatter;
    }
    public double getColumnWidth() {
        return columnWidth;
    }
    public void setColumnWidth(double columnWidth) {
        this.columnWidth = columnWidth;
    }
}
