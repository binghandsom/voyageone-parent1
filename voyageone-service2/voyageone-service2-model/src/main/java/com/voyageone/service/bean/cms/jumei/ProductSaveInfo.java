package com.voyageone.service.bean.cms.jumei;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/5/27.
 */
public class ProductSaveInfo {
    public CmsBtJmPromotionProductModel jmProductModel = new CmsBtJmPromotionProductModel();
    public List<CmsBtJmPromotionSkuModel> jmSkuList = new ArrayList<>();
    public List<CmsBtJmPromotionTagProductModel> tagList = new ArrayList<>();

    public CmsBtPromotionCodesModel codesModel = null;

    public CmsBtPromotionGroupsModel groupsModel = null;

    public  List<CmsBtPromotionSkusModel> skusModels=new ArrayList<>();

    //不做保存处理 保存异常时 导出用
    public ProductImportBean _importProduct;
    //不做保存处理 保存异常时 导出用
    public List<SkuImportBean> _listSkuImport=new ArrayList<>();
    //输入参数
    public  CmsBtProductModel p_ProductInfo;
    public CmsBtProductModel_Platform_Cart p_Platform_Cart;
}