package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.jumei.ProductSaveInfo;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionTagProductModel;
import com.voyageone.service.model.cms.CmsBtPromotionSkusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/7/1.
 */
@Service
public class CmsBtJmPromotionImportSave3Service {
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionTagProductDao daoCmsBtJmPromotionTagProduct;
    @Autowired
    CmsBtJmPromotionSkuDao daoCmsBtJmPromotionSku;
    @Autowired
    CmsBtPromotionCodesDao daoCmsBtPromotionCodes;
    @Autowired
    private CmsBtPromotionGroupsDao daoCmsBtPromotionGroups;
    @Autowired
    private CmsBtPromotionSkusDao daoCmsBtPromotionSkus;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;

    @VOTransactional
    public void saveProductSaveInfo(ProductSaveInfo info) {
        //CmsBtJmPromotionProduct
        if (info.jmProductModel.getId() == null || info.jmProductModel.getId() == 0) {
            daoCmsBtJmPromotionProduct.insert(info.jmProductModel);
        } else {
            daoCmsBtJmPromotionProduct.update(info.jmProductModel);
        }
        daoExtCmsBtJmPromotionProduct.updateAvgPriceByPromotionProductId(info.jmProductModel.getId());//求价格 折扣 平均值
        //CmsBtJmPromotionSku
        for (CmsBtJmPromotionSkuModel sku : info.jmSkuList) {
            sku.setCmsBtJmPromotionProductId(info.jmProductModel.getId());
            if (sku.getId() == null || sku.getId() == 0) {
                daoCmsBtJmPromotionSku.insert(sku);
            } else {
                daoCmsBtJmPromotionSku.update(sku);
            }
        }
        //CmsBtJmPromotionTagProduct
        for (CmsBtJmPromotionTagProductModel tag : info.tagList) {
            tag.setCmsBtJmPromotionProductId(info.jmProductModel.getId());
            if (tag.getId() == null || tag.getId() == 0) {
                daoCmsBtJmPromotionTagProduct.insert(tag);
            }
        }

        if(info.codesModel!=null)
        {
            daoCmsBtPromotionCodes.insert(info.codesModel);
        }
        if(info.groupsModel!=null)
        {
            daoCmsBtPromotionGroups.insert(info.groupsModel);
        }
        if(info.skusModels!=null) {
            for (CmsBtPromotionSkusModel skusModel : info.skusModels) {
                daoCmsBtPromotionSkus.insert(skusModel);
            }
        }
    }
}
