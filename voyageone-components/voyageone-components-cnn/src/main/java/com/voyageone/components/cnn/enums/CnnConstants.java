package com.voyageone.components.cnn.enums;

public class CnnConstants {

    // C_CNN_RETURN_SUCCESS_0
    public static final int C_CNN_RETURN_SUCCESS_0 = 0;

    // 调用新独立域名平台的API Action
    public interface CnnApiAction {
        // 1 PRODUCT
        String PRODUCT_ADD         = "/product/add";             // 添加商品(商品上新)
        String PRODUCT_UPDATE      = "/product/update";          // 更新商品信息

        // 2 WARE
        String PRODUCT_ADD2WARE    = "/product/addToWare";       // 商品上架
        String PRODUCT_DELFROMWARE = "/product/delFromWare";     // 商品下架

        // 3 CATALOG
        String CATALOG_ADD         = "/catalog/add";             // 添加店铺内分类
        String CATALOG_UPDATE      = "/catalog/update";          // 修改店铺内分类名称
        String CATALOG_DELETE      = "/catalog/delete/";         // 删除店铺内分类
        String CATALOG_PRODUCT_SET = "/catalog/product/set";     // 设置商品的店铺内分类
        String CATALOG_GET         = "/catalog/get";             // 查询店铺内分类信息
//        String CATALOG_ADD         = "/catalog/add";           // 重置所有店铺内分类
        String CATALOG_PRODUCT_RESET = "/catalog/product/reset";   // 重置所有商品的店铺内分类

        // 4 ORDER

        // 5 INVENTORY

        // 6 PAYMENT

    }
    
}
