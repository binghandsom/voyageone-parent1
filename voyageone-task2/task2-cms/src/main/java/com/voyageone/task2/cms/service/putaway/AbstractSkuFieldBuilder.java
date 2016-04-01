package com.voyageone.task2.cms.service.putaway;

import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.bean.PlatformUploadRunState;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.tcb.TaskSignal;
import com.voyageone.task2.cms.dao.CustomSizePropDao;
import com.voyageone.task2.cms.dao.PlatformSkuInfoDao;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Leo on 15-7-14.
 */
public abstract class AbstractSkuFieldBuilder {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

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
