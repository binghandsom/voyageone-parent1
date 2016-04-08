package com.voyageone.service.impl.jumei.enumjm;
import com.voyageone.service.impl.Excel.EnumExcelColumnType;
import com.voyageone.service.impl.Excel.ExcelColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum EnumJMSpecialImageImportColumn {
    ProductCode("product_code", 1, "jm_bt_sku", "商品Code",EnumExcelColumnType.ColumnType_String,false),//商品Code                    jm_bt_sku
    ProductImageUrlKey1("product_image_url_key1",2,"jm_bt_images","商品图片1"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey2("product_image_url_key2",3,"jm_bt_images","商品图片2"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey3("product_image_url_key3",4,"jm_bt_images","商品图片3"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey4("product_image_url_key4",5,"jm_bt_images","商品图片4"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey5("product_image_url_key5",6,"jm_bt_images","商品图片5"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    ProductImageUrlKey6("product_image_url_key6",7,"jm_bt_images","商品图片6"),//jm_bt_images imageURLKey   进入jm_bt_images（产品图片） image type是 1、2   需要套模板
    specialImage1("special_image1", 8, "cms_bt_jm_product_images", "商品定制图1"),
    specialImage2("special_image2", 9, "cms_bt_jm_product_images", "商品定制图2"),
    specialImage3("special_image3", 10, "cms_bt_jm_product_images", "商品定制图3"),
    specialImage4("special_image4", 11, "cms_bt_jm_product_images", "商品定制图4"),
    specialImage5("special_image5",12, "cms_bt_jm_product_images", "商品定制图5"),
    specialImage6("special_image6", 13, "cms_bt_jm_product_images", "商品定制图6"),
    propertyImage1("propertyImage1", 14, "cms_bt_jm_product_images", "商品参数图1"),
    propertyImage2("propertyImage2", 15, "cms_bt_jm_product_images", "商品参数图2"),
    propertyImage3("propertyImage3", 16, "cms_bt_jm_product_images", "商品参数图3"),
    propertyImage4("propertyImage4", 17, "cms_bt_jm_product_images", "商品参数图4"),
    propertyImage5("propertyImage5", 18, "cms_bt_jm_product_images", "商品参数图5"),
    propertyImage6("propertyImage6", 19, "cms_bt_jm_product_images", "商品参数图6");
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
