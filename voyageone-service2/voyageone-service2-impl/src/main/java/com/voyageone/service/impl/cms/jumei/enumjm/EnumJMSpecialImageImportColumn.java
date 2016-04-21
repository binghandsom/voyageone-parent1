package com.voyageone.service.impl.cms.jumei.enumjm;
import com.voyageone.common.util.excel.EnumExcelColumnType;
import com.voyageone.common.util.excel.ExcelColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum EnumJMSpecialImageImportColumn {//productImageUrlKey1
    ProductCode("product_code", 1, "jm_bt_sku", "商品Code",EnumExcelColumnType.ColumnType_String,false),//商品Code                    jm_bt_sku
    ProductImageUrlKey1("product_image_url_key1",2,"jm_bt_images","商品图片1",EnumExcelColumnType.ColumnType_String,false),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey2("product_image_url_key2",3,"jm_bt_images","商品图片2",EnumExcelColumnType.ColumnType_String,false),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey3("product_image_url_key3",4,"jm_bt_images","商品图片3"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey4("product_image_url_key4",5,"jm_bt_images","商品图片4"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey5("product_image_url_key5",6,"jm_bt_images","商品图片5"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey6("product_image_url_key6",7,"jm_bt_images","商品图片6"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey7("product_image_url_key7",8,"jm_bt_images","商品图片7"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey8("product_image_url_key8",9,"jm_bt_images","商品图片8"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey9("product_image_url_key9",10,"jm_bt_images","商品图片9"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey10("product_image_url_key10",11,"jm_bt_images","商品图片10"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板

    SpecialImage1("special_image1",12, "cms_bt_jm_product_images", "商品定制图1"),
    SpecialImage2("special_image2", 13, "cms_bt_jm_product_images", "商品定制图2"),
    SpecialImage3("special_image3", 14, "cms_bt_jm_product_images", "商品定制图3"),
    SpecialImage4("special_image4", 15, "cms_bt_jm_product_images", "商品定制图4"),
    SpecialImage5("special_image5",16, "cms_bt_jm_product_images", "商品定制图5"),
    SpecialImage6("special_image6", 17, "cms_bt_jm_product_images", "商品定制图6"),
    SpecialImage7("special_image7", 18, "cms_bt_jm_product_images", "商品定制图7"),
    SpecialImage8("special_image8", 19, "cms_bt_jm_product_images", "商品定制图8"),
    SpecialImage9("special_image9", 20, "cms_bt_jm_product_images", "商品定制图9"),
    SpecialImage10("special_image10", 21, "cms_bt_jm_product_images", "商品定制图10"),

    PropertyImage1("property_Image1", 22, "cms_bt_jm_product_images", "商品参数图1"),
    PropertyImage2("property_Image2", 23, "cms_bt_jm_product_images", "商品参数图2"),
    PropertyImage3("property_Image3", 24, "cms_bt_jm_product_images", "商品参数图3"),
    PropertyImage4("property_Image4", 25, "cms_bt_jm_product_images", "商品参数图4"),
    PropertyImage5("property_Image5", 26, "cms_bt_jm_product_images", "商品参数图5"),
    PropertyImage6("property_Image6", 27, "cms_bt_jm_product_images", "商品参数图6"),
    PropertyImage7("property_Image7", 28, "cms_bt_jm_product_images", "商品参数图6"),
    PropertyImage8("property_Image8", 29, "cms_bt_jm_product_images", "商品参数图6"),
    PropertyImage9("property_Image9", 30, "cms_bt_jm_product_images", "商品参数图6"),
    PropertyImage10("property_Image10",31, "cms_bt_jm_product_images", "商品参数图6");
    public static int orderIndex;
    private ExcelColumn<Map> excelColumn;
    public ExcelColumn<Map> getExcelColumn() {
        return excelColumn;
    }
    public void setExcelColumn(ExcelColumn<Map> excelColumn) {
        this.excelColumn = excelColumn;
    }
    private EnumJMSpecialImageImportColumn(String columnName, int orderIndex, String tableName, String text) {
        this.excelColumn = new ExcelColumn(columnName, orderIndex, tableName, text);
    }
    private EnumJMSpecialImageImportColumn(String columnName, int orderIndex, String tableName, String text, EnumExcelColumnType columnType, boolean isNull) {
        this.excelColumn = new ExcelColumn(columnName, orderIndex, tableName, text, columnType, isNull);
    }
    private EnumJMSpecialImageImportColumn(String columnName, String tableName, String text, EnumExcelColumnType columnType, boolean isNull)
    {
        this.excelColumn = new ExcelColumn(columnName, EnumCount.ProductColumnIndex++, tableName, text,columnType,isNull);
    }
    public  static List<EnumJMSpecialImageImportColumn> getList()
    {
        List<EnumJMSpecialImageImportColumn> listEnumImportColumn = Arrays.asList(EnumJMSpecialImageImportColumn.values());
        listEnumImportColumn.sort((a, b) -> {
            if (a.getExcelColumn().getOrderIndex() > b.getExcelColumn().getOrderIndex()) return 1;
            return -1;
        });
        return listEnumImportColumn;
    }
    public static  List<ExcelColumn> getListExcelColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        EnumJMSpecialImageImportColumn[] array = EnumJMSpecialImageImportColumn.values();
        for (EnumJMSpecialImageImportColumn column : array) {
            list.add(column.getExcelColumn());
        }
        return list;
    }
}
