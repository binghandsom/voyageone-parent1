package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.PlatformUploadRunState;
import com.voyageone.batch.ims.dao.*;
import com.voyageone.batch.ims.modelbean.CmsModelPropBean;
import com.voyageone.batch.ims.modelbean.PlatformPropBean;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Leo on 15-7-14.
 */
public abstract class AbstractSkuFieldBuilder{
    protected PlatformPropDao platformPropDao;
    protected PlatformSkuInfoDao platformSkuInfoDao;
    protected SkuInfoDao skuInfoDao;
    protected SkuPropValueDao skuPropValueDao;
    protected CustomSizePropDao customSizePropDao;

    protected String codeImageTemplate;

    public abstract boolean isYourFood(List<PlatformPropBean> platformProps);

    public abstract List buildSkuInfoField(int cartId, String categoryCode, List<PlatformPropBean> platformProps,
                           CmsModelPropBean cmsModelProp,
                           PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields,
                           Set<String> imageSet);

    public abstract int updateInventoryField(String orderChannelId,
                                     PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields,
                                     List fields);

    public void updateSkuPropImage(Map<String, String> urlMap,
                            PlatformUploadRunState.PlatformContextBuildCustomFields contextBuildCustomFields){}

    public void setDao(PlatformPropDao platformPropDao, SkuPropValueDao skuPropValueDao, PlatformSkuInfoDao platformSkuInfoDao, SkuInfoDao skuInfoDao, CustomSizePropDao customSizePropDao) {
        this.platformPropDao = platformPropDao;
        this.platformSkuInfoDao = platformSkuInfoDao;
        this.skuInfoDao = skuInfoDao;
        this.skuPropValueDao = skuPropValueDao;
        this.customSizePropDao = customSizePropDao;
    }

    public void setCodeImageTemplete(String codeImageTemplate) {
        this.codeImageTemplate = codeImageTemplate;
    }
}
