package com.voyageone.service.impl.jumei.enumjm;
import com.voyageone.service.impl.Excel.EnumExcelColumnType;
import com.voyageone.service.impl.Excel.ExcelColumn;
import org.apache.avro.generic.GenericData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum EnumJMProductImportColumn   {
    ProductCode("product_code","cms_bt_jm_product","产品编号",EnumExcelColumnType.ColumnType_String,false),//Acms_bt_jm_product
    AppId("app_id","cms_bt_jm_promotion_product","APP端模块ID"),//APP端模块ID
    PcId("pc_id","cms_bt_jm_promotion_product","PC端模块ID"),//Acms_bt_jm_product
    ForeignLanguageName("foreign_language_name","cms_bt_jm_product","商品英文名称"),//B
    ProductNameCn("product_name_cn","cms_bt_jm_product","商品中文名称"),//C
    ProductLongName("product_long_name","cms_bt_jm_product","长标题"),//D
    ProductMediumName("product_medium_name","cms_bt_jm_product","中标题"),//E
    ProductShortName("product_short_name","cms_bt_jm_product","短标题"),//E
    VoCategoryName("vo_category_name","cms_bt_jm_product","主数据类目"),//G
    CategoryLv1Name("category_lv1_name","cms_bt_jm_product","聚美一级类目"),//H
    CategoryLv2Name("category_lv2_name","cms_bt_jm_product","聚美一级类目"),//I
    CategoryLv3Name("category_lv3_name","cms_bt_jm_product","聚美一级类目"),//J
    CategoryLv4Name("category_lv4_name","cms_bt_jm_product","聚美一级类目"),//K
    CategoryLv4Id("category_lv4_id","cms_bt_jm_product","聚美四级类目ID"),//L
    VOBrandName("brand_name","cms_bt_jm_product","主数据品牌子"),//N
    BrandName("brand_name","cms_bt_jm_product","聚美品牌名称"),//N
    BrandId("brand_id","cms_bt_jm_product","聚美品牌"),//O
    ProductType("product_type","cms_bt_jm_product","商品类别"),//P
    SizeType("size_type","cms_bt_jm_product","尺码类别"),//Q
    ColorEn("color_en","cms_bt_jm_product","英文颜色"),//R
    Attribute("attribute","cms_bt_jm_product","中文颜色"),//S
    Origin("origin","cms_bt_jm_product","英文产地"),//T
    AddressOfProduce("address_of_produce","cms_bt_jm_product","中文产地"),//U
    MaterialEn("material_en","cms_bt_jm_product","英文材质"),//V
    MaterialCn("material_cn","cms_bt_jm_product","使用方法_产品介绍"),//W
    ProductDesEn("product_des_en","cms_bt_jm_product","使用方法_产品介绍"),//X
    ProductDesCn("product_des_cn","cms_bt_jm_product","使用方法_产品介绍"),//Y
    AvailablePeriod("available_period","cms_bt_jm_product","保质期"),//Z
    ApplicableCrowd("applicable_crowd","cms_bt_jm_product","适合人群"),//AA
    SearchMetaTextCustom("search_meta_text_custom","cms_bt_jm_product","自定义搜索词"),//AB
    SpecialNote("special_note","cms_mt_master_info","特别说明 用于聚美上新"),//AC
    Msrp("msrp","cms_bt_jm_product","海外官网价格"),//AD                                                           cms_bt_jm_product
    Limit("limit","cms_bt_jm_promotion_product","Deal每人限购"),//jm_bt_promotion_product
    PropertyImage("property_image","jm_bt_images","使用方法_商品参数图"),     //jm_bt_images  image url 进入jm_bt_images（产品图片）   image type是 3
    ProductImageUrlKey1("product_image_url_key1","jm_bt_images","商品图片1"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey2("product_image_url_key2","jm_bt_images","商品图片2"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey3("product_image_url_key3","jm_bt_images","商品图片3"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey4("product_image_url_key4","jm_bt_images","商品图片4"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey5("product_image_url_key5","jm_bt_images","商品图片5"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey6("product_image_url_key6","jm_bt_images","商品图片6");//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板


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
    private EnumJMProductImportColumn(String columnName, String tableName, String text,EnumExcelColumnType columnType,boolean isNull)
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
