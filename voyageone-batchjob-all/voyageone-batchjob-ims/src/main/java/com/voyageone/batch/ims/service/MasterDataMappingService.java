package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.ImsConstants;
import com.voyageone.batch.ims.bean.CustomMappingType;
import com.voyageone.batch.ims.bean.PlatformUploadRunState;
import com.voyageone.batch.ims.bean.PropValueTreeNode;
import com.voyageone.batch.ims.bean.TmallUploadRunState;
import com.voyageone.batch.ims.bean.tcb.TaskSignal;
import com.voyageone.batch.ims.bean.tcb.UploadProductTcb;
import com.voyageone.batch.ims.dao.*;
import com.voyageone.batch.ims.enums.UploadPriceChoiceEnum;
import com.voyageone.batch.ims.modelbean.*;
import com.voyageone.batch.ims.service.rule_parser.ExpressionParser;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
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
    @Autowired
    private PlatformPropDao platformPropDao;
    @Autowired
    private PlatformPropCustomMappingDao platformPropCustomMappingDao;
    @Autowired
    private PropValueDao propValueDao;
    @Autowired
    private PropDao propDao;
    @Autowired
    private DarwinPropValueDao darwinPropValueDao;

    private static Log logger = LogFactory.getLog(MasterDataMappingService.class);

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

    public void constructPlatformProps(PlatformServiceInterface platformService, UploadProductTcb tcb, List<PlatformPropBean> platformProps, Set<String> imageSet) throws TaskSignal {
        if (platformProps == null)
            return;

        List<PlatformPropBean> platformPropsWillBeFilted = new ArrayList<>();

        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        CmsModelPropBean cmsModelProp = workLoadBean.getCmsModelProp();
        CmsCodePropBean mainCmsCodeProp = workLoadBean.getMainProductProp();

        ExpressionParser expressionParser = new ExpressionParser(cmsModelProp, mainCmsCodeProp, workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id(), workLoadBean.getLevel(), workLoadBean.getLevelValue());


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

        platformService.constructCustomPlatformPropsBeforeUploadImage(tcb, mappingTypePropsMap, expressionParser, imageSet);

        //过滤掉platformProp的子孙子段
        filtDescendantPlatformProps(platformProps, platformPropsWillBeFilted);

        //第二步，处理达尔文字段
        platformPropsWillBeFilted.clear();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        if (tmallUploadRunState.is_darwin()) {
            int cartId = workLoadBean.getCart_id();
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

        //第三步，从ims_mt_prop_mapping中查找这条记录的主数据匹配
        List<String> platformPropHashList = new ArrayList<>();
        for (PlatformPropBean platformProp : platformProps) {
            platformPropHashList.add(platformProp.getPlatformPropHash());
        }
        List<PropMappingBean> masterPropMappingList = propDao.selectPropMappingByPlatformPropHashList(platformPropHashList, false);
        List<PropMappingBean> shopPropMappingList = propDao.selectPropMappingByPlatformPropHashList(platformPropHashList, true);
        //没有找到主数据匹配的属性列表，它们要通过匹配类型才能解决
        Map<PlatformPropBean, PropMappingBean> platformPropsMapping = new HashMap<>();
        //Map<PlatformPropBean, List<PropValueBean>> platformPropBeanValueMap = new HashMap<>();

        for (PlatformPropBean platformProp : platformProps) {
            PropMappingBean masterPropMapping = null;

            logger.debug(String.format("try find master mapping for platform prop:[%s, %s]", platformProp.getPlatformPropId(), platformProp.getPlatformPropName()));
            for (PropMappingBean propMappingBean : masterPropMappingList)
            {
                if (platformProp.getPlatformPropHash().equals(propMappingBean.getPlatformPropHash()))
                {
                    //TODO: 按照之前约定，在mapping表中不包含父的mapping关系，但是现在表中有，所以临时加上这个判断
                    if (platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_COMPLEX ||
                            platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_MULTI_COMPLEX) {
                        break;
                    }
                    masterPropMapping = propMappingBean;
                    logger.debug("find master mapping!");
                    break;
                }
            }
            if (masterPropMapping == null) {
                logger.debug("can not find master mapping!");
            }
            if ("seller_cids".equals(platformProp.getPlatformPropId())) {
                logger.debug("find shangpin leimu");
            }

            //如果主数据中有他的映射关系，第三步
            //有映射关系的只能是简单类型的属性，这是规定，但对于multicheckbox，或者multicomplex的子属性select的结果仍然
            // 可能有多条记录
            if (masterPropMapping != null && masterPropMapping.getPropId() != -1)
            {
                platformService.constructFieldMappingPlatformPropsBeforeUploadImage(tcb, platformProp, platformProps,
                        masterPropMapping, expressionParser, imageSet);
            }
            else if (masterPropMapping != null)
            {
                platformPropsMapping.put(platformProp, masterPropMapping);
            }
        }
        //List platformMasterProps = platformService.resolveMasterMappingProps(platformProps, platformPropBeanValueMap);

        //第四步，如果在上一步没能为该字段找到合适的值，那么使用shop级别的mapping关系
        for (Map.Entry<PlatformPropBean, PropMappingBean> entry : platformPropsMapping.entrySet()) {
            String plainPropValue = null;
            PlatformPropBean platformProp = entry.getKey();
            PropMappingBean propMapping = null;
            //先找到属性对应的shop级别的mapping关系
            PropMappingBean shopPropMapping = null;
            for (PropMappingBean propMappingBean : shopPropMappingList) {
                if (platformProp.getPlatformPropHash().equals(propMappingBean.getPlatformPropHash())) {
                    propMapping = propMappingBean;
                    break;
                }
            }

            if (propMapping == null) {
                propMapping = entry.getValue();
            }

            if (propMapping != null) {
                plainPropValue = parsePropValueByMapping(expressionParser, shopPropMapping, imageSet);
            }

        }
    }

    public List<Object> resolvePlatformProps(PlatformServiceInterface platformServiceInterface, PlatformUploadRunState platformUploadRunState,
                                             Map<String, String> urlMap) {
        List<Object> platformFields = new ArrayList<>();
        List customFields = (List)platformServiceInterface.resolveCustomMappingProps(platformUploadRunState, urlMap);
        List platformMasterProps = (List)platformServiceInterface.resolveMasterMappingProps(platformUploadRunState, urlMap);

        platformFields.addAll(customFields);
        platformFields.addAll(platformMasterProps);
        return platformFields;
    }

    /**
     * 根据主属性的选项值，找到对应的平台选项值
     * @param masterPropId
     * @param masterOptionValue
     * @return
     */
    public String getPlatformOptionValueByMasterOptionValue(int masterPropId, String masterOptionValue)
    {
        PropOptionBean propOptionBean = propDao.selectPropOptionByPropIdAndPropOptionValue(masterPropId, masterOptionValue);
        if (propOptionBean == null)
        {
            return null;
        }
        //根据主属性的选项ID找到平台属性的选项hash值
        String propOptionHash = propDao.selectPlatformPropOptionHashByPropOptionId(propOptionBean.getPropOptionId());

        if (propOptionHash == null)
            return null;

        return propDao.selectPlatformPropOptionValueByOptionHash(propOptionHash);
    }

    /**
     *
     */
    public Map<PropValueBean, PropValueTreeNode> buildPropValueTree(Map<PlatformPropBean, List<PropValueBean>> propValueMapping)
    {
        Map<PropValueBean, PropValueTreeNode> valueTreeMap = new HashMap<>();
        Map<String, PropValueBean> uuidPropValueMap = new HashMap<>();

        for (Map.Entry<PlatformPropBean, List<PropValueBean>> entry : propValueMapping.entrySet())
        {
            List<PropValueBean> propValueBeans = entry.getValue();
            PlatformPropBean platformPropBean = entry.getKey();

            for (PropValueBean propValueBean : propValueBeans) {
                uuidPropValueMap.put(propValueBean.getUuid(), propValueBean);
            }

            for (PropValueBean propValueBean : propValueBeans) {
                PropValueTreeNode propValueTreeNode = valueTreeMap.get(propValueBean);
                if (propValueTreeNode == null) {
                    propValueTreeNode = new PropValueTreeNode();
                    valueTreeMap.put(propValueBean, propValueTreeNode);
                }
                else {
                    continue;
                }
                propValueTreeNode.setPropValue(propValueBean);
                propValueTreeNode.setPlatformProp(platformPropBean);

                if (propValueBean.getParent() != null && !"".equals(propValueBean.getParent())) {
                    PropValueBean parentPropValue;
                    PlatformPropBean parentPlatformProp = null;
                    PropValueBean currentPropValue = propValueBean;
                    PlatformPropBean currentPlatformProp = platformPropBean;
                    PropValueTreeNode currentPropValueTreeNode = propValueTreeNode;
                    PropValueTreeNode parentPropValueTreeNode = null;
                    do {
                        parentPropValue = uuidPropValueMap.get(currentPropValue.getParent());
                        if (parentPropValue == null) {
                            parentPropValue = propValueDao.selectPropValueByUUID(currentPropValue.getParent());
                            uuidPropValueMap.put(currentPropValue.getParent(), parentPropValue);
                        }
                        if (parentPropValue != null) {
                            parentPropValueTreeNode = valueTreeMap.get(parentPropValue);
                            if (parentPropValueTreeNode == null) {
                                parentPropValueTreeNode = new PropValueTreeNode();
                                parentPropValueTreeNode.setPropValue(parentPropValue);
                                //此时，说明父子关系是multicheck与option或者multicomplex与单个complexvalue
                                if (currentPropValue.getProp_id() == parentPropValue.getProp_id()) {
                                    parentPlatformProp = currentPlatformProp;
                                    parentPropValueTreeNode.setPlatformProp(parentPlatformProp);
                                } else {
                                    parentPlatformProp = platformPropDao.selectPlatformPropByPropHash(currentPlatformProp.getParentPropHash());
                                    parentPropValueTreeNode.setPlatformProp(parentPlatformProp);
                                }
                                valueTreeMap.put(parentPropValue, parentPropValueTreeNode);
                            }
                            if (currentPropValueTreeNode.getParentNode() == null) {
                                currentPropValueTreeNode.setParentNode(parentPropValueTreeNode);
                                parentPropValueTreeNode.addChildNode(currentPropValueTreeNode);
                            }
                        }
                        currentPropValue = parentPropValue;
                        currentPlatformProp = parentPlatformProp;
                        currentPropValueTreeNode = parentPropValueTreeNode;
                    } while (currentPropValue != null) ;
                }
            }
        }
        return valueTreeMap;
    }

    public List<PropValueTreeNode> getLeafPropValueTreeNode(List<PropValueTreeNode> propValueTreeNodes)
    {
        List leafPropValueTreeNodes = new ArrayList();
        for (PropValueTreeNode propValueTreeNode : propValueTreeNodes)
        {
            if (propValueTreeNode.getChildNodes() == null || propValueTreeNode.getChildNodes().isEmpty())
            {
                leafPropValueTreeNodes.add(propValueTreeNode);
            }
        }
        return leafPropValueTreeNodes;
    }

    public String parsePropValueByMapping(ExpressionParser expressionParser, PropMappingBean shopPropMapping, Set<String> imageSet) {
        switch (shopPropMapping.getMappingType())
        {
            case PropMappingBean.EXPRESSION: {
                RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                RuleExpression expression = ruleJsonMapper.deserializeRuleExpression(shopPropMapping.getMappingValue());
                return expressionParser.parse(expression, imageSet);
            }
            case PropMappingBean.RANDOM:
            case PropMappingBean.TAG:
            case PropMappingBean.UNKNOWN:
        }
        return null;
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
}
