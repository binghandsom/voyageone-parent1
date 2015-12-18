package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.bean.*;
import com.voyageone.batch.cms.dao.DarwinPropValueDao;
import com.voyageone.batch.cms.dao.PlatformPropCustomMappingDao;
import com.voyageone.batch.cms.model.*;
import com.voyageone.batch.cms.bean.tcb.TaskSignal;
import com.voyageone.batch.cms.bean.tcb.UploadProductTcb;
import com.voyageone.batch.cms.service.rule_parser.ExpressionParser;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformMappingDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Leo on 15-6-29.
 */
@Repository
public class MasterDataMappingService {
    /*
    @Autowired
    private PlatformPropDao platformPropDao;
    */
    @Autowired
    private PlatformPropCustomMappingDao platformPropCustomMappingDao;
    /*
    @Autowired
    private PropValueDao propValueDao;
    @Autowired
    private PropDao propDao;
    @Autowired
    private DarwinPropValueDao darwinPropValueDao;
    */
    @Autowired
    private CmsMtPlatformMappingDao cmsMtPlatformMappingDao;

    private static Log logger = LogFactory.getLog(MasterDataMappingService.class);

    /*
    public static String getCodePrice(WorkLoadBean workLoadBean, CmsCodePropBean cmsCodeProp) {
        String priceChoice = ShopConfigs.getVal1(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()), ShopConfigEnums.Name.upload_price_choice);
        UploadPriceChoiceEnum uploadPriceChoiceEnum = UploadPriceChoiceEnum.valueOf(priceChoice);

        String price;
        switch (uploadPriceChoiceEnum) {
            case price:
                logger.info("使用price作为sku价格");
                price = cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.price);
                break;
            case msrp:
                logger.info("使用msrp作为sku价格");
                price = cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.msrp);
                break;
            default:
                logger.warn("没有设定价格选择，默认使用price作为sku价格");
                price = cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.price);
                break;
        }
        return price;
    }
    */

    public void constructPlatformProps(PlatformServiceInterface platformService, UploadProductTcb tcb, List<PlatformPropBean> platformProps, Set<String> imageSet) throws TaskSignal {
        if (platformProps == null)
            return;

        List<PlatformPropBean> platformPropsWillBeFilted = new ArrayList<>();

        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        String channelId = workLoadBean.getOrder_channel_id();
        int cartId = workLoadBean.getCart_id();
        SxProductBean mainSxProduct = workLoadBean.getMainProduct();
        CmsBtProductModel mainProduct = mainSxProduct.getCmsBtProductModel();
        String mainCategoryId = mainProduct.getCatId();

        ExpressionParser expressionParser = tcb.getExpressionParser();

        //第一步，先从ims_mt_platform_prop_mapping从查找，该属性是否在范围，如果在，那么采用特殊处理,
        // 并把这些字段加入将要删除子孙属性的链表
        List<CustomPlatformPropMapping> customPlatformPropMappings = platformPropCustomMappingDao.getCustomMappingPlatformProps(workLoadBean.getCart_id());

        Map<CustomMappingType, List<PlatformPropBean>> mappingTypePropsMap = new HashMap<>();

        for (CustomPlatformPropMapping customPlatformPropMapping : customPlatformPropMappings)
        {
            for (PlatformPropBean platformProp : platformProps)
            {
                if (customPlatformPropMapping.getPlatformPropId().equals(platformProp.getPlatformPropId()))
                {
                    List<PlatformPropBean> mappingPlatformPropBeans = mappingTypePropsMap.get(customPlatformPropMapping.getCustomMappingType());
                    if (mappingPlatformPropBeans == null) {
                        mappingPlatformPropBeans = new ArrayList<>();
                        mappingTypePropsMap.put(customPlatformPropMapping.getCustomMappingType(), mappingPlatformPropBeans);
                    }
                    mappingPlatformPropBeans.add(platformProp);
                    platformPropsWillBeFilted.add(platformProp);
                }
            }
        }

        /*
        platformService.constructCustomPlatformPropsBeforeUploadImage(tcb, mappingTypePropsMap, expressionParser, imageSet, platformPropsWillBeFilted);

        //过滤掉platformProp的子孙子段
        filtDescendantPlatformProps(platformProps, platformPropsWillBeFilted);

        //第二步，处理达尔文字段
        platformPropsWillBeFilted.clear();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        if (tmallUploadRunState.is_darwin()) {
            String styleCode = tmallUploadRunState.getStyle_code();
            for (PlatformPropBean platformProp : platformProps) {
                DarwinPropValue darwinPropValue = darwinPropValueDao.selectDarwinPropValue(cartId, styleCode, platformProp.getPlatformPropId());
                if (darwinPropValue != null) {
                    platformPropsWillBeFilted.add(platformProp);
                    platformService.constructDarwinPlatformProps(tcb, platformProp, darwinPropValue.getPlatform_prop_value());
                }
            }
        }
        //过滤掉platformProp的子孙子段
        filtDescendantPlatformProps(platformProps, platformPropsWillBeFilted);
        */

        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = cmsMtPlatformMappingDao.getMapping(channelId, cartId, mainCategoryId);
        //platformService.constructMappingPlatformPropsBeforeUploadImage(tcb, platformProps, cmsMtPlatformMappingModel, expressionParser, imageSet);
    }

    /*
    public List<Object> resolvePlatformProps(PlatformServiceInterface platformServiceInterface, PlatformUploadRunState platformUploadRunState,
                                             Map<String, String> urlMap) {
        List<Object> platformFields = new ArrayList<>();
        List customFields = (List)platformServiceInterface.resolveCustomMappingProps(platformUploadRunState, urlMap);
        List platformMasterProps = (List)platformServiceInterface.resolveMappingProps(platformUploadRunState, urlMap);

        platformFields.addAll(customFields);
        platformFields.addAll(platformMasterProps);
        return platformFields;
    }


    public void filtDescendantPlatformProps(List<PlatformPropBean> platformProps, List<PlatformPropBean> platformPropsWillBeFilted) {
        for (Iterator<PlatformPropBean> it$=platformPropsWillBeFilted.iterator(); it$.hasNext();)
        {
            filtDescendantPlatformProp(platformProps, it$.next());
            it$.remove();
        }
    }


    public void filtDescendantPlatformProp(List<PlatformPropBean> platformProps, PlatformPropBean platformPropFilted) {
        List<PlatformPropBean> platformPropsWillBeFilted = new ArrayList<>();
        for (PlatformPropBean platformProp : platformProps)
        {
            if (platformPropFilted.getPlatformPropHash().equals(platformProp.getParentPropHash()))
                platformPropsWillBeFilted.add(platformProp);
        }

        //如果是叶子结点，那么直接删除该结点
        if (platformPropsWillBeFilted.size() == 0)
            platformProps.remove(platformPropFilted);
        else {
            filtDescendantPlatformProps(platformProps, platformPropsWillBeFilted);
            platformProps.remove(platformPropFilted);
        }
    }
    */
}
