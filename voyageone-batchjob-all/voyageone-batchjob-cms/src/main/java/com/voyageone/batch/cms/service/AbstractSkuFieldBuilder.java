package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.bean.PlatformUploadRunState;
import com.voyageone.batch.cms.bean.SxProductBean;
import com.voyageone.batch.cms.bean.tcb.TaskSignal;
import com.voyageone.batch.cms.dao.CustomSizePropDao;
import com.voyageone.batch.cms.dao.PlatformSkuInfoDao;
import com.voyageone.batch.cms.service.rule_parser.ExpressionParser;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Leo on 15-7-14.
 */
public abstract class AbstractSkuFieldBuilder {

    protected PlatformSkuInfoDao platformSkuInfoDao;
    protected CustomSizePropDao customSizePropDao;

    protected String codeImageTemplate;
    protected ExpressionParser expressionParser;

    public abstract boolean isYourFood(List platformProps, int cartId);

    public abstract List buildSkuInfoField(int cartId, String categoryCode, List platformProps,
                                           List<SxProductBean> processSxProducts, Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap,
                                           CmsMtPlatformMappingModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap,
                                           PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields,
                                           Set<String> imageSet) throws TaskSignal;

    public abstract int updateInventoryField(String orderChannelId,
                                     PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields,
                                     List fields);

    public void setDao(PlatformSkuInfoDao platformSkuInfoDao, CustomSizePropDao customSizePropDao) {
        this.platformSkuInfoDao = platformSkuInfoDao;
        this.customSizePropDao = customSizePropDao;
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    public void setCodeImageTemplete(String codeImageTemplate) {
        this.codeImageTemplate = codeImageTemplate;
    }
}
