package com.voyageone.service.impl.jumei.enumjm;
import com.voyageone.service.impl.Excel.EnumExcelColumnType;
import com.voyageone.service.impl.Excel.FunctionFormatter;
import com.voyageone.service.impl.jumei.JmBtProductService;
import com.voyageone.service.model.jumei.JmBtProductModel;
public enum EnumJMProductImportColumn implements EnumImport<JmBtProductModel> {
    ProductCode("product_code",0,"jm_bt_product","产品编号_ProductCode"),//Ajm_bt_product
    AppId("app_id",1,"jm_bt_product","APP端模块ID"),//APP端模块ID
    PcId("pc_id",1,"jm_bt_product","PC端模块ID"),//Ajm_bt_product
    ForeignLanguageName("foreign_language_name",1,"jm_bt_product","商品英文名称Foreign Language Product Name"),//B
    ProductNameCn("product_name_cn",2,"jm_bt_product","商品中文名称\\n\\rChinese Product Name"),//C
    ProductLongName("product_long_name",3,"jm_bt_product","长标题\r\nLong Product Name"),//D
    ProductMediumName("product_medium_name",4,"jm_bt_product","中标题\nMedium Product Name"),//E
    ProductShortName("product_short_name",4,"jm_bt_product","短标题\nShort Product Name"),//E
    VoCategoryName("vo_category_name",5,"jm_bt_product","主数据类目\nVO Category Name"),//G
    CategoryLv1Name("category_lv1_name",6,"jm_bt_product","聚美一级类目\nJM Category Lv1 Name"),//H
    CategoryLv2Name("category_lv2_name",7,"jm_bt_product","聚美一级类目\nJM Category Lv2 Name"),//I
    CategoryLv3Name("category_lv3_name",8,"jm_bt_product","聚美一级类目\nJM Category Lv3 Name"),//J
    CategoryLv4Name("category_lv4_name",9,"jm_bt_product","聚美一级类目\nJM Category Lv4 Name"),//K
    CategoryLv4Id("category_lv4_id",10,"jm_bt_product","聚美四级类目ID\nJM Category Lv4 ID"),//L
    BrandName("brand_name",11,"jm_bt_product","聚美品牌名称\nJM Brand Name"),//N
    BrandId("brand_id",12,"jm_bt_product","聚美品牌ID\nJM Brand ID"),//O
    ProductType("product_type",13,"jm_bt_product","商品类别\nProduct Type"),//P
    SizeType("size_type",14,"jm_bt_product","尺码类别\nSize Type"),//Q
    ColorEn("color_en",15,"jm_bt_product","英文颜色\nColor"),//R
    Attribute("attribute",16,"jm_bt_product","中文颜色\nAttribute"),//S
    Origin("origin",17,"jm_bt_product","英文产地\norigin"),//T
    AddressOfProduce("address_of_produce",18,"jm_bt_product","中文产地\nAddress Of Produce"),//U
    MaterialEn("material_en",19,"jm_bt_product","英文材质\nMaterial En"),//V
    MaterialCn("material_cn",20,"jm_bt_product","使用方法_产品介绍\nProduct Description Cn"),//W
    ProductDesEn("product_des_en",21,"jm_bt_product","使用方法_产品介绍\nProduct Description En"),//X
    ProductDesCn("product_des_cn",22,"jm_bt_product","使用方法_产品介绍\nProduct Description Cn"),//Y
    AvailablePeriod("available_period",23,"jm_bt_product","保质期\nExpiration Date"),//Z
    ApplicableCrowd("applicable_crowd",24,"jm_bt_product","适合人群ApplicableCrowd"),//AA
    SearchMetaTextCustom("search_meta_text_custom",25,"jm_bt_product","自定义搜索词\nSearch Meta Text Custom"),//AB
    SpecialNote("special_note",26,"jm_bt_product","特别说明 用于聚美上新"),//AC
    Msrp("msrp",27,"jm_bt_product","海外官网价格_MSRP(msrp)"),//AD                                                           jm_bt_product

    Limit("limit",28,"jm_bt_promotion_product","Deal\n每人限购_Limit"),//jm_bt_promotion_product
    PropertyImage("property_image",28,"jm_bt_images","使用方法_商品参数图(property_image)"),     //jm_bt_images  image url 进入jm_bt_images（产品图片）   image type是 3
    ProductImageUrlKey1("product_image_url_key1",28,"jm_bt_images","商品图片1Product Image Url Key1"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey2("product_image_url_key2",28,"jm_bt_images","商品图片2Product Image Url Key2"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey3("product_image_url_key3",28,"jm_bt_images","商品图片3Product Image Url Key3"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey4("product_image_url_key4",28,"jm_bt_images","商品图片4Product Image Url Key4"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey5("product_image_url_key5",28,"jm_bt_images","商品图片5Product Image Url Key5"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey6("product_image_url_key6",28,"jm_bt_images","商品图片6Product Image Url Key6");//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板


    private String columnName;
    private String camelColumnName;
    private String tableName;
    private int orderIndex;
    private String text;
    public EnumExcelColumnType columnType;
    public double columnWidth;
    public FunctionFormatter<Object, JmBtProductModel, Integer, Object> formatter;
    private EnumJMProductImportColumn(String columnName, int orderIndex, String tableName, String text)
    {
        this.columnName=columnName;
        this.camelColumnName=CamelUtil.underlineToCamel(this.columnName);
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

    public String getCamelColumnName() {
        return camelColumnName;
    }

    public void setCamelColumnName(String camelColumnName) {
       this.camelColumnName = camelColumnName;
    }
}
