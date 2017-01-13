package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.*;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.service.MqSenderService;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.*;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.tools.CmsMtPlatformCommonSchemaService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.*;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.com.cache.CommCacheService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCommonSchemaModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
@Service
public class CmsFieldEditService extends BaseViewService {

    @Autowired
    private CategorySchemaService categorySchemaService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private SizeChartService sizeChartService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private MqSender sender;
    @Autowired
    private MqSenderService mqSenderService;
    @Autowired
    private CommCacheService commCacheService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private CmsMtPlatformCommonSchemaService cmsMtPlatformCommonSchemaService;
    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    private static final String FIELD_SKU_CARTS = "skuCarts";

    /**
     * 获取pop画面options.
     */
    public List<CmsMtCommonPropDefModel> getPopOptions(String language, String channel_id) {
        List<CmsMtCommonPropDefModel> modelList = categorySchemaService.getALLCommonPropDef();
        List<CmsMtCommonPropDefModel> resultList = new ArrayList<>();

        for (CmsMtCommonPropDefModel model : modelList) {
            Field field = model.getField();
            if (field.getIsDisplay() != 1) {
                continue;
            }

            // 对于VO扣点进行特殊判断
            if ("voRate".equals(field.getId())) {
                CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanNoCode(channel_id, CmsConstants.ChannelConfig.PRICE_CALCULATOR);
                if (priceCalculatorConfig != null) {
                    if (CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA.equals(priceCalculatorConfig.getConfigValue1())) {
                        // 不是价格管理体系
                        continue;
                    }
                }
            }

            CmsMtCommonPropDefModel resModel = new CmsMtCommonPropDefModel();
            if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())
                    || CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
                OptionsField optionsField = getOptions(field, language, channel_id);
                resModel.setField(optionsField);
            } else {
                resModel.setField(field);
            }
            resultList.add(resModel);
        }
        return resultList;
    }

    public List<CmsMtCommonPropDefModel> getPlatfromPopOptions(Integer cartId) {
        CmsMtPlatformCommonSchemaModel commonSchemaModel = cmsMtPlatformCommonSchemaService.get(cartId);
        List<Field> items = new ArrayList<>();
        if (commonSchemaModel == null)
            return null;

        List<Map<String, Object>> itemFieldMapList = commonSchemaModel.getPropsItem();
        if (itemFieldMapList != null && !itemFieldMapList.isEmpty())
            items.addAll(SchemaJsonReader.readJsonForList(itemFieldMapList));

        List<Map<String, Object>> productFieldMapList = commonSchemaModel.getPropsProduct();
        if (productFieldMapList != null && !productFieldMapList.isEmpty())
            items.addAll(SchemaJsonReader.readJsonForList(productFieldMapList));

        List<CmsMtCommonPropDefModel> resultList = new ArrayList<>(items.size());
        items.forEach(field -> {
            CmsMtCommonPropDefModel resModel = new CmsMtCommonPropDefModel();
            resModel.setField(field);
            resultList.add(resModel);
        });
        return resultList;
    }

    /**
     * 商品的智能上新
     */
    @SuppressWarnings("unchecked")
    public void intelligentPublish(int cartId, Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        List<String> productCodes = null;
        // 判断是否为全选
        boolean isSelectAll = ((Integer) params.get("isSelectAll") == 1);
        if (isSelectAll) {
            // 从高级检索重新取得查询结果（根据Session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        } else {
            productCodes = (List<String>) params.get("productIds");
        }

        // 按产品的Code检索出所有的商品
        JongoQuery queryObj = new JongoQuery();
        StringBuilder queryStr = new StringBuilder("{")
                .append(" 'common.fields.code':{$in:#}")
                .append(",'common.fields.hsCodeStatus': '1'")
//        		.append(",'platforms.P").append(cartId).append(".pBrandId': {$nin:[null, '']}")
                .append(",'platforms.P").append(cartId).append(".pCatId': {$nin:[null, '']}")
                .append("}");
        queryObj.setQuery(queryStr.toString());
        queryObj.setParameters(productCodes);
        queryObj.setProjection("{'common.fields.code':1,'_id':0}");
        List<CmsBtProductModel> products = productService.getList(userInfo.getSelChannelId(), queryObj);

        // 开始智能上新
        if (CollectionUtils.isNotEmpty(products)) {
            productCodes.clear();
            // 智能上新批处理
            BulkJongoUpdateList productBulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, userInfo.getSelChannelId());

            String productCode = null;
            for (CmsBtProductModel product : products) {
                productCode = product.getCommon().getFields().getCode();
                StringBuffer updateStr = new StringBuffer("{$set:{")
                        .append(" 'platforms.P").append(cartId).append(".status':'Approved'")
                        .append(",'modified':#")
                        .append(",'modifier':#")
                        .append("}}");
                // 更新商品状态
                JongoUpdate updateObj = new JongoUpdate();
                updateObj.setQuery("{'common.fields.code':#}");
                updateObj.setQueryParameters(productCode);
                updateObj.setUpdate(updateStr.toString());
                updateObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());

                // 保存商品Code
                productCodes.add(productCode);
                // 添加秕处理执行语句
                BulkWriteResult rs = productBulkList.addBulkJongo(updateObj);
                if (rs != null) {
                    $debug(String.format("智能上新(product表) channelId=%s 执行结果=%s", userInfo.getSelChannelId(), rs.toString()));
                }
            }

            // 执行智能上新批处理
            BulkWriteResult rs = productBulkList.execute();
            if (rs != null) {
                $debug(String.format("智能上新(product表) channelId=%s 结果=%s", userInfo.getSelChannelId(), rs.toString()));
            }

            // 插入上新程序
            $debug("批量修改属性 (智能上新) 开始记入SxWorkLoad表");
            long start = System.currentTimeMillis();
            sxProductService.insertSxWorkLoad(userInfo.getSelChannelId(), productCodes, cartId, userInfo.getUserName(), true);
            $debug("批量修改属性 (智能上新) 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - start));

            // 记录商品修改历史
            productStatusHistoryService.insertList(userInfo.getSelChannelId(), productCodes, cartId, EnumProductOperationType.IntelligentPublish,
                    "高级检索 智能上新", userInfo.getUserName());
        }
    }

    /**
     * 批量修改属性.
     */
    public Map<String, Object> setProductFields(Map<String, Object> params, UserSessionBean userInfo, int cartId, CmsSessionBean cmsSession) {
        Map<String, Object> rsMap = new HashMap<>();
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }
        List<String> productCodes = null;
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        } else {
            productCodes = (ArrayList<String>) params.get("productIds");
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        Map<String, Object> prop = (Map<String, Object>) params.get("property");
        String prop_id = StringUtils.trimToEmpty((String) prop.get("id"));
        if ("productType".equals(prop_id) || "sizeType".equals(prop_id)) {
            // 产品分类/适用人群
            String stsCode = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                stsCode = (String) valObj.get("value");
            }
            if (stsCode == null || stsCode.isEmpty()) {
                $warn("没有设置变更项目 params=" + params.toString());
                rsMap.put("ecd", 2);
                return rsMap;
            }

            JongoUpdate updObj = new JongoUpdate();
            updObj.setQuery("{'common.fields.code':{$in:#}}");
            updObj.setQueryParameters(productCodes);
            updObj.setUpdate("{$set:{'common.fields." + prop_id + "':#}}");
            updObj.setUpdateParameters(stsCode);

            WriteResult rs = productService.updateMulti(updObj, userInfo.getSelChannelId());
            $debug("批量更新结果 " + rs.toString());
            rsMap.put("ecd", 0);

            // 记录商品修改历史
            String msg = "";
            if ("productType".equals(prop_id)) {
                msg = "高级检索 批量更新：产品分类--" + stsCode;
            } else if ("sizeType".equals(prop_id)) {
                msg = "高级检索 批量更新：适用人群--" + stsCode;
            }
            productStatusHistoryService.insertList(userInfo.getSelChannelId(), productCodes, -1, EnumProductOperationType.BatchUpdate, msg, userInfo.getUserName());
            return rsMap;

        } else if ("voRate".equals(prop_id)) {
            // 修改VO扣点值
            Number voRate = (Number) prop.get("value");

            JongoUpdate updObj = new JongoUpdate();
            updObj.setQuery("{'common.fields.code':{$in:#}}");
            updObj.setQueryParameters(productCodes);
            String voRateVal = null;
            if (voRate == null) {
                updObj.setUpdate("{$set:{'common.fields.commissionRate':null}}");
            } else {
                updObj.setUpdate("{$set:{'common.fields.commissionRate':#}}");
                if (voRate instanceof Integer) {
                    voRateVal = ((Integer) voRate).toString();
                    updObj.setUpdateParameters(voRate.doubleValue());
                } else {
                    BigDecimal val = new BigDecimal(voRate.toString());
                    double f1 = val.setScale(2, RoundingMode.HALF_UP).doubleValue();
                    voRateVal = Double.toString(f1);
                    updObj.setUpdateParameters(f1);
                }
            }
            WriteResult rs = productService.updateMulti(updObj, userInfo.getSelChannelId());
            $debug("VO扣点值批量更新结果 " + rs.toString());

            // 调用批处理程序 记录价格变更履历/记录商品修改历史/同步价格范围/插入上新程序
            /*Map<String, Object> logParams = new HashMap<>(3);
            logParams.put("channelId", userInfo.getSelChannelId());
            logParams.put("creater", userInfo.getUserName());
            logParams.put("codeList", productCodes);
            logParams.put("voRate", voRateVal);*/

            ProductVoRateUpdateMQMessageBody mqMessageBody = new ProductVoRateUpdateMQMessageBody();
            mqMessageBody.setChannelId(userInfo.getSelChannelId());
            mqMessageBody.setCreater(userInfo.getUserName());
            mqMessageBody.setCodeList(productCodes);
            mqMessageBody.setVoRate(voRateVal);
            mqMessageBody.setSender(userInfo.getUserName());
            try {
                mqSenderService.sendMessage(mqMessageBody);
            } catch (MQMessageRuleException e) {
                $error(String.format("VO扣点值批量更新MQ发送异常,channelId=%s,userName=%s", userInfo.getSelChannelId(), userInfo.getUserName()), e);
                throw new BusinessException("MQ发送异常:" + e.getMessage());
            }

        } else if ("hsCodePrivate".equals(prop_id) || "hsCodeCrop".equals(prop_id) || "translateStatus".equals(prop_id)) {
            // 税号更新 /翻译状态更新
            String hsCode = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                hsCode = (String) valObj.get("value");
            }
            if (hsCode == null || hsCode.isEmpty()) {
                $warn("没有设置变更项目 params=" + params.toString());
                rsMap.put("ecd", 2);
                return rsMap;
            }

            /*params.put("productIds", productCodes);
            params.put("_channleId", userInfo.getSelChannelId());
            params.put("_userName", userInfo.getUserName());
            params.put("_taskName", "batchupdate");
            sender.sendMessage(CmsMqRoutingKey.CMS_TASK_AdvSearch_AsynProcessJob, params);*/

            BatchUpdateProductMQMessageBody mqMessageBody = new BatchUpdateProductMQMessageBody();
            mqMessageBody.setChannelId(userInfo.getSelChannelId());
            mqMessageBody.setUserNmme(userInfo.getUserName());
            mqMessageBody.setProductCodes(productCodes);
            mqMessageBody.setParams(params);
            mqMessageBody.setSender(userInfo.getUserName());
            try {
                mqSenderService.sendMessage(mqMessageBody);
            } catch (MQMessageRuleException e) {
                $error(String.format("批量更新商品发送MQ异常,channleId=%s,userName=%s", userInfo.getSelChannelId(), userInfo.getUserName()), e);
                throw new BusinessException("MQ发送异常:" + e.getMessage());
            }

            rsMap.put("ecd", 0);
            return rsMap;

        } else {
            $warn("CmsFieldEditService.setProductFields 错误的选择项 params=" + params.toString());
        }

        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 批量修改属性.(商品上下架)
     */
    public Map<String, Object> setProductOnOff(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        Map<String, Object> rsMap = new HashMap<>();
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        String prop_id = (String) params.get("putFlg");
        if (prop_id == null || prop_id.isEmpty()) {
            $warn("没有设置上下架操作");
            rsMap.put("ecd", 2);
            return rsMap;
        }
        if (!"1".equals(prop_id) && !"0".equals(prop_id)) {
            $warn("没有设置上下架操作");
            rsMap.put("ecd", 2);
            return rsMap;
        }

        Integer cartId = (Integer) params.get("cartId");
        // 聚美及minimall店铺没有上下架业务
        List<Integer> cartList = null;
        if (cartId == null || cartId == 0) {
            // 表示全平台更新
            // 店铺(cart/平台)列表
            List<TypeChannelBean> cartTypeList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            cartList = new ArrayList<>();
            for (TypeChannelBean cartObj : cartTypeList) {
//                if (!CartEnums.Cart.JM.getId().equals(cartObj.getValue()) && !"3".equals(cartObj.getCartType())) {
                if (!"3".equals(cartObj.getCartType())) {
                    cartList.add(NumberUtils.toInt(cartObj.getValue()));
                }
            }
            cartList = cartTypeList.stream().map((cartType) -> NumberUtils.toInt(cartType.getValue())).collect(Collectors.toList());
        } else {
            TypeChannelBean cartObj = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, userInfo.getSelChannelId(), cartId.toString(), "en");
//            if (CartEnums.Cart.JM.getValue() != cartId && !"3".equals(cartObj.getCartType())) {
            if (!"3".equals(cartObj.getCartType())) {
                cartList = new ArrayList<>(1);
                cartList.add(cartId);
            }
        }

        if (cartList == null || cartList.isEmpty()) {
            $warn("批量修改属性 所选平台不需要更新");
            rsMap.put("ecd", 0);
            return rsMap;
        }

        // 更新产品的信息
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'productCodes':{$in:#},'channelId':#,'cartId':{$in:#}}");
        updObj.setUpdate("{$set:{'platformActive':#,'modified':#,'modifier':#}}");

        // 设置platformActive的状态
        CmsConstants.PlatformActive statusVal = null;
        if ("1".equals(prop_id)) {
            statusVal = com.voyageone.common.CmsConstants.PlatformActive.ToOnSale;
        } else if ("0".equals(prop_id)) {
            statusVal = com.voyageone.common.CmsConstants.PlatformActive.ToInStock;
        }

        updObj.setQueryParameters(productCodes, userInfo.getSelChannelId(), cartList);
        updObj.setUpdateParameters(statusVal, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
        WriteResult rs = productGroupService.updateMulti(updObj, userInfo.getSelChannelId());
        $debug("批量修改属性.(商品上下架) 结果1=：" + rs.toString());

        // 发送请求到MQ,插入操作历史记录
        /*Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", userInfo.getSelChannelId());
        logParams.put("cartIdList", cartList);
        logParams.put("activeStatus", statusVal.name());
        logParams.put("creater", userInfo.getUserName());
        if (cartId == null || cartId == 0) {
            logParams.put("comment", "高级检索 批量上下架(全店铺操作)");
        } else {
            logParams.put("comment", "高级检索 批量上下架");
        }

        logParams.put("codeList", productCodes);
        sender.sendMessage(CmsMqRoutingKey.CMS_TASK_PlatformActiveLogJob, logParams);*/

        PlatformActiveLogMQMessageBody mqMessageBody = new PlatformActiveLogMQMessageBody();
        mqMessageBody.setChannelId(userInfo.getSelChannelId());
        mqMessageBody.setCartList(cartList);
        mqMessageBody.setActiveStatus(statusVal.name());
        mqMessageBody.setUserName(userInfo.getUserName());
        mqMessageBody.setSender(userInfo.getUserName());
        if (cartId == null || cartId == 0) {
            mqMessageBody.setComment("高级检索 批量上下架(全店铺操作)");
        } else {
            mqMessageBody.setComment("高级检索 批量上下架");
        }
        mqMessageBody.setProductCodes(productCodes);
        try {
            mqSenderService.sendMessage(mqMessageBody);
        } catch (MQMessageRuleException e) {
            $error(String.format("商品上下架MQ发送异常,channelId=%s,userName=%s", userInfo.getSelChannelId(), userInfo.getUserName()), e);
            throw new BusinessException("商品上下架MQ发送异常: " + e.getMessage());
        }

        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 批量修改属性(商品审批)
     */
    public Map<String, Object> setProductApproval(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");

        if (isSelAll == null) {
            isSelAll = 0;
        }

        Map<String, Object> rsMap = new HashMap<>();

        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }

        /**
         * 未选择商品
         * 结果代码 ecd ：1
         */
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        // #############################################################################################################

        Integer cartId = (Integer) params.get("cartId");
        List<Integer> cartList = null;

        if (cartId == null || cartId == 0) {
            // 表示全平台更新
            // 店铺(cart/平台)列表
            List<TypeChannelBean> cartTypesList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            cartList = cartTypesList.stream().map((cartType) -> NumberUtils.toInt(cartType.getValue())).collect(Collectors.toList());
        } else {
            cartList = new ArrayList<>(1);
            cartList.add(cartId);
        }

        // 先判断是否是ready状态（minimall店铺不验证）
        List<Integer> newcartList = new ArrayList<>();
        for (Integer cartIdVal : cartList) {
            TypeChannelBean cartType = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, userInfo.getSelChannelId(), cartIdVal.toString(), "en");
            if (!"3".equals(cartType.getCartType())) {
                newcartList.add(cartIdVal);
            }
        }
        if (newcartList.size() > 0) {
            JongoQuery queryObject = new JongoQuery();
            StringBuilder qryStr = new StringBuilder();
            qryStr.append("{'common.fields.code':{$in:#},$or:[");
            for (Integer cartIdVal : newcartList) {
                if (!CartEnums.Cart.TT.getId().equals(String.valueOf(cartIdVal))
                        && !CartEnums.Cart.USTT.getId().equals(String.valueOf(cartIdVal)))
                    qryStr.append("{'platforms.P" + cartIdVal + ".status':{$nin:['Ready','Approved']}},");
                else
                    qryStr.append("{'common.fields.hsCodeStatus': '0'},");
            }
            qryStr.deleteCharAt(qryStr.length() - 1);
            qryStr.append("]}");
            queryObject.setQuery(qryStr.toString());
            queryObject.setParameters(productCodes);
            queryObject.setProjection("{'common.fields.code':1,'common.fields.hsCodeStatus':1,'_id':0}");

            List<CmsBtProductModel> prodList = productService.getList(userInfo.getSelChannelId(), queryObject);
            if (prodList != null && prodList.size() > 0) {
                // 存在未ready状态
                List<String> codeList = new ArrayList<>(prodList.size());
                List<String> hsCodeList = new ArrayList<>();

                for (CmsBtProductModel prodObj : prodList) {
                    if (prodObj.getCommon() == null) {
                        continue;
                    }
                    CmsBtProductModel_Field field = prodObj.getCommon().getFields();
                    if (field != null && field.getCode() != null) {
                        codeList.add(field.getCode());
                    }

                    if(field != null && "0".equals(field.getHsCodeStatus())){
                        hsCodeList.add(field.getCode());
                    }
                }
                rsMap.put("ecd", 2);

                if (hsCodeList.size() > 0 && (newcartList.contains(Integer.parseInt(CartEnums.Cart.TT.getId()))
                        || newcartList.contains(Integer.parseInt(CartEnums.Cart.TT.getId())))) {
                    rsMap.put("ts", true);
                    rsMap.put("codeList", hsCodeList);
                }else{
                    rsMap.put("codeList", codeList);
                }

                return rsMap;
            }
        }

        //###############################################################################################################
        // 检查商品价格 notChkPrice=1时表示忽略价格问题
        Integer notChkPriceFlg = (Integer) params.get("notChkPrice");
        if (notChkPriceFlg == null) {
            notChkPriceFlg = 0;
        }
        if (notChkPriceFlg == 0) {
            Integer startIdx = (Integer) params.get("startIdx");
            if (startIdx == null) {
                startIdx = 0;
            }

            // 阀值
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
            Double breakThreshold = null;
            if (cmsChannelConfigBean != null) {
                breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D + 1.0;
            }

            int idx = 0;
            for (String code : productCodes) {
                if (idx < startIdx) {
                    idx++;
                    continue;
                }
                idx++;
                // 获取产品的信息
                CmsBtProductModel productModel = productService.getProductByCode(userInfo.getSelChannelId(), code);
                List<Map<String, Object>> prodInfoList = new ArrayList<>();
                double priceLimit = 0;

                for (Integer cartIdVal : cartList) {
                    CmsBtProductModel_Platform_Cart ptmObj = productModel.getPlatform(cartIdVal);
                    if (ptmObj == null) {
                        continue;
                    }
                    String cartName = Carts.getCart(cartIdVal).getName();
                    List<BaseMongoMap<String, Object>> skuObjList = ptmObj.getSkus();
                    if (skuObjList == null) {
                        continue;
                    }
                    for (BaseMongoMap<String, Object> skuObj : skuObjList) {
                        double priceSale = skuObj.getDoubleAttribute("priceSale");
                        double priceRetail = skuObj.getDoubleAttribute("priceRetail");
                        Map<String, Object> priceInfo = new HashMap<>();
                        if (priceSale < priceRetail) {
                            priceInfo.put("priceRetail", priceRetail);
                        }
                        if (breakThreshold != null) {
                            priceLimit = priceRetail * breakThreshold;
                        }
                        if (breakThreshold != null && priceSale > priceLimit) {
                            priceInfo.put("priceLimit", priceLimit);
                        }
                        if (priceSale < priceRetail || (breakThreshold != null && priceSale > priceLimit)) {
                            priceInfo.put("priceSale", priceSale);
                            priceInfo.put("skuCode", skuObj.get("skuCode"));
                            priceInfo.put("cartName", cartName);
                            prodInfoList.add(priceInfo);
                        }
                    }
                }
                if (prodInfoList.size() > 0) {
                    rsMap.put("startIdx", idx);
                    rsMap.put("code", code);
                    rsMap.put("infoList", prodInfoList);
                    break;
                }
            }
            if (rsMap.size() > 0) {
                rsMap.put("ecd", 3);
                return rsMap;
            }
        }

        // ############################################################################################################

        BulkJongoUpdateList prodBulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, userInfo.getSelChannelId());
        BulkJongoUpdateList grpBulkList = new BulkJongoUpdateList(1000, cmsBtProductGroupDao, userInfo.getSelChannelId());
        List<String> newProdCodeList = new ArrayList<>();
        for (String code : productCodes) {
            // 获取产品的信息
            CmsBtProductModel productModel = productService.getProductByCode(userInfo.getSelChannelId(), code);
            if (productModel.getCommon() == null) {
                continue;
            }
            CmsBtProductModel_Field field = productModel.getCommon().getFields();
            if (field == null) {
                continue;
            }

            List<String> strList = new ArrayList<>();
            for (Integer cartIdVal : cartList) {
                // 如果该产品以前就是approved,则不更新pStatus
                String prodStatus = productModel.getPlatformNotNull(cartIdVal).getStatus();
                TypeChannelBean cartType = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, userInfo.getSelChannelId(), cartIdVal.toString(), "en");
                if ("3".equals(cartType.getCartType())) {
                    strList.add("'platforms.P" + cartIdVal + ".status':'Approved','platforms.P" + cartIdVal + ".pStatus':'WaitingPublish'");
                } else {
                    if (CmsConstants.ProductStatus.Ready.name().equals(prodStatus)) {
                        strList.add("'platforms.P" + cartIdVal + ".status':'Approved','platforms.P" + cartIdVal + ".pStatus':'WaitingPublish'");
                    } else if (CmsConstants.ProductStatus.Approved.name().equals(prodStatus)) {
                        strList.add("'platforms.P" + cartIdVal + ".status':'Approved'");
                    } else if (newcartList.contains(Integer.parseInt(CartEnums.Cart.TT.getId()))
                            || newcartList.contains(Integer.parseInt(CartEnums.Cart.USTT.getId()))) {
                        strList.add("'platforms.P" + cartIdVal + ".status':'Approved'");
                    }
                }
            }

            if (strList.isEmpty()) {
                $debug("产品未更新 code=" + code);
                continue;
            }
            newProdCodeList.add(code);
            String updStr = "{$set:{";
            updStr += StringUtils.join(strList, ',');
            updStr += ",'modified':#,'modifier':#}}";
            // 更新product表的status及pStatus
            JongoUpdate updObj = new JongoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setQueryParameters(code);
            updObj.setUpdate(updStr);
            updObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());

            BulkWriteResult rs = prodBulkList.addBulkJongo(updObj);
            if (rs != null) {
                $debug(String.format("商品审批(product表) channelId=%s 执行结果=%s", userInfo.getSelChannelId(), rs.toString()));
            }

            // 更新group表的platformStatus
            JongoUpdate grpUpdObj = new JongoUpdate();
            grpUpdObj.setQuery("{'productCodes':#,'channelId':#,'cartId':{$in:#},'platformStatus':{$in:[null,'']}}");
            grpUpdObj.setQueryParameters(code, userInfo.getSelChannelId(), cartList);
            grpUpdObj.setUpdate("{$set:{'platformStatus':'WaitingPublish','modified':#,'modifier':#}}");
            grpUpdObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());

            rs = grpBulkList.addBulkJongo(grpUpdObj);
            if (rs != null) {
                $debug(String.format("商品审批(group表) channelId=%s 执行结果=%s", userInfo.getSelChannelId(), rs.toString()));
            }
        }

        BulkWriteResult rs = prodBulkList.execute();
        if (rs != null) {
            $debug(String.format("商品审批(product表) channelId=%s 结果=%s", userInfo.getSelChannelId(), rs.toString()));
        }
        rs = grpBulkList.execute();
        if (rs != null) {
            $debug(String.format("商品审批(group表) channelId=%s 结果=%s", userInfo.getSelChannelId(), rs.toString()));
        }

        String msg;
        if (cartId == null || cartId == 0) {
            msg = "高级检索 商品审批(全平台)";
        } else {
            msg = "高级检索 商品审批";
        }
        for (Integer cartIdVal : cartList) {
            // 插入上新程序
            $debug("批量修改属性 (商品审批) 开始记入SxWorkLoad表");
            long sta = System.currentTimeMillis();
            sxProductService.insertSxWorkLoad(userInfo.getSelChannelId(), newProdCodeList, cartIdVal, userInfo.getUserName(), false);
            $debug("批量修改属性 (商品审批) 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));

            // 记录商品修改历史
            productStatusHistoryService.insertList(userInfo.getSelChannelId(), newProdCodeList, cartIdVal, EnumProductOperationType.ProductApproved, msg, userInfo.getUserName());
        }

        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 返回OptionField数据.
     */
    private OptionsField getOptions(Field field, String language, String channelId) {
        OptionsField optionsField = (OptionsField) field;
        if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())) {
            List<TypeBean> typeBeanList = Types.getTypeList(field.getId(), language);

            // 替换成field需要的样式
            List<Option> options = new ArrayList<>();
            for (TypeBean typeBean : typeBeanList) {
                Option opt = new Option();
                opt.setDisplayName(typeBean.getName());
                opt.setValue(typeBean.getValue());
                options.add(opt);
            }
            optionsField.setOptions(options);
        } else if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
            // 获取type channel bean
            List<TypeChannelBean> typeChannelBeanList;
            if (FIELD_SKU_CARTS.equals(field.getId())) {
                typeChannelBeanList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
            } else {
                typeChannelBeanList = TypeChannels.getTypeWithLang(field.getId(), channelId, language);
            }

            // 替换成field需要的样式
            List<Option> options = new ArrayList<>();
            if (typeChannelBeanList != null) {
                for (TypeChannelBean typeChannelBean : typeChannelBeanList) {
                    Option opt = new Option();
                    opt.setDisplayName(typeChannelBean.getName());
                    opt.setValue(typeChannelBean.getValue());
                    options.add(opt);
                }
            }
            optionsField.setOptions(options);
        } else if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_SIZE_CHART.equals(field.getDataSource())) {
            JongoQuery queryObject = new JongoQuery();
            //取得收索的条件
            queryObject.setQuery("{\"channelId\": #, \"finish\": \"1\"}");
            queryObject.setParameters(channelId);
            queryObject.setSort("{sizeChartId:-1}");
            //返回数据的类型
            List<CmsBtSizeChartModel> sizeCharList = sizeChartService.getSizeCharts(queryObject);
            List<Option> options = new ArrayList<>();
            for (CmsBtSizeChartModel sizeChart : sizeCharList) {
                Option opt = new Option();
                opt.setDisplayName(sizeChart.getSizeChartName());
                opt.setValue(String.valueOf(sizeChart.getSizeChartId()));
                options.add(opt);
            }
            optionsField.setOptions(options);
        }
        return optionsField;
    }

    /**
     * 批量修改属性.(修改商品最终售价)
     */
    public Map<String, Object> setProductSalePrice(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        $debug("批量修改商品价格 开始处理");
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        Map<String, Object> rsMap = new HashMap<>();
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("批量修改商品价格 没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        Integer cartId = (Integer) params.get("cartId");
        if (cartId == null || cartId == 0) {
            $warn("批量修改商品价格 没有cartId条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }
        ShopBean shopObj = Shops.getShop(userInfo.getSelChannelId(), cartId.toString());
        CartBean cartObj = Carts.getCart(cartId);
        if (shopObj == null) {
            $error("批量修改商品价格 未配置平台 channelId=%s, cartId=%s", userInfo.getSelChannelId(), cartId.toString());
            throw new BusinessException("本店铺未配置所销售平台");
        }

        List<List<String>> productCodesList = CommonUtil.splitList(productCodes, 100);
        UpdateProductSalePriceMQMessageBody mqMessageBody = new UpdateProductSalePriceMQMessageBody();
        mqMessageBody.setCartId(cartId);
        mqMessageBody.setChannelId(userInfo.getSelChannelId());
        mqMessageBody.setSender(userInfo.getUserName());
        mqMessageBody.setUserId(userInfo.getUserId());
        mqMessageBody.setParams(params);
        for (List<String> codes:productCodesList) {
            mqMessageBody.setProductCodes(codes);
            try {
                mqSenderService.sendMessage(mqMessageBody);
            } catch (MQMessageRuleException e) {
                $error(String.format("修改商品中国最终上架MQ发送异常,channelId=%s,cartId=%s,userName=%s", userInfo.getSelChannelId(), cartId, userInfo.getUserName()), e);
                throw new BusinessException(e.getMessage());
            }
        }


/*        // 检查商品价格 notChkPrice=1时表示忽略价格超过阈值
        Integer notChkPriceFlg = (Integer) params.get("notChkPrice");
        if (notChkPriceFlg == null) {
            notChkPriceFlg = 0;
        }

        String priceType = StringUtils.trimToNull((String) params.get("priceType"));
        String optionType = StringUtils.trimToNull((String) params.get("optionType"));
        String priceValue = StringUtils.trimToNull((String) params.get("priceValue"));
        // 小数点向上取整:1    个位向下取整:2    个位向上取整:3    无特殊处理:4
        Integer roundType = (Integer) params.get("roundType");
        if (roundType == null) {
            roundType = 0;
        }
        // 商品内，SKU统一最高价:1 商品内，SKU统一最低价:2  商品内，SKU价格不统一:3
        Integer skuUpdType = (Integer) params.get("skuUpdType");
        if (skuUpdType == null) {
            skuUpdType = 0;
        }

        // 阀值
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(userInfo.getSelChannelId(), CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
        double breakThreshold = 0;
        if (cmsChannelConfigBean != null) {
            breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D;
        }

        // 获取产品的信息
        JongoQuery qryObj = new JongoQuery();
        qryObj.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartId + ".skus.0':{$exists:true}}");
        qryObj.setParameters(productCodes);
//        qryObj.setProjection("{'common.fields.code':1,'prodId':1,'common.skus.skuCode':1,'common.skus.clientMsrpPrice':1,'common.skus.clientRetailPrice':1,'common.skus.clientNetPrice':1,'platforms.P" + cartId + ".pNumIId':1,'platforms.P" + cartId + ".status':1,'platforms.P" + cartId + ".skus':1,'_id':0}");

        List<CmsBtPriceLogModel> priceLogList = new ArrayList<CmsBtPriceLogModel>();
        String skuCode = null;
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, userInfo.getSelChannelId());
        List<String> prodPriceUpList = new ArrayList<>();
        List<String> prodPriceDownList = new ArrayList<>();
        List<String> prodPriceDownExList = new ArrayList<>();

        List<CmsBtProductModel> prodObjList = productService.getList(userInfo.getSelChannelId(), qryObj);
        $debug("批量修改商品价格 开始批量处理");
        for (CmsBtProductModel prodObj : prodObjList) {
            prodObj.setChannelId(userInfo.getSelChannelId()); // 为后面调用priceService.setPrice使用
            List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
            String prodCode = prodObj.getCommonNotNull().getFieldsNotNull().getCode();

            // 先取出最高价/最低价
            Double maxPriceSale = null;
            if (priceType != null) {
                if (skuUpdType == 1) {
                    // 统一最高价
                    for (BaseMongoMap skuObj : skuList) {
                        double befPriceSale = skuObj.getDoubleAttribute(priceType);
                        if (maxPriceSale == null) {
                            maxPriceSale = befPriceSale;
                        } else if (maxPriceSale < befPriceSale) {
                            maxPriceSale = befPriceSale;
                        }
                    }
                } else if (skuUpdType == 2) {
                    // 统一最低价
                    for (BaseMongoMap skuObj : skuList) {
                        double befPriceSale = skuObj.getDoubleAttribute(priceType);
                        if (maxPriceSale == null) {
                            maxPriceSale = befPriceSale;
                        } else if (maxPriceSale > befPriceSale) {
                            maxPriceSale = befPriceSale;
                        }
                    }
                }
            }

            for (BaseMongoMap skuObj : skuList) {
                skuCode = skuObj.getStringAttribute("skuCode");
                if (StringUtils.isEmpty(skuCode)) {
                    $warn(String.format("setProductSalePrice: 缺少数据 code=%s, para=%s", prodCode, skuCode, params.toString()));
                    rsMap.put("ecd", 6);
                    rsMap.put("prodCode", prodCode);
                    return rsMap;
                }

                // 修改后的最终售价
                Double rs = null;
                if (StringUtils.isEmpty(priceType)) {
                    // 使用固定值
                    if (priceValue == null) {
                        $warn(String.format("setProductSalePrice: 没有填写金额 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        rsMap.put("ecd", 7);
                        return rsMap;
                    }
                    rs = getFinalSalePrice(null, optionType, priceValue, roundType);
                } else {
                    Object basePrice = null;
                    if (maxPriceSale == null) {
                        basePrice = skuObj.getAttribute(priceType);
                    } else {
                        basePrice = maxPriceSale;
                    }
                    if (basePrice != null) {
                        BigDecimal baseVal = new BigDecimal(basePrice.toString());
                        rs = getFinalSalePrice(baseVal, optionType, priceValue, roundType);
                    } else {
                        $warn(String.format("setProductSalePrice: 缺少数据 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        rsMap.put("ecd", 9);
                        rsMap.put("prodCode", prodCode);
                        rsMap.put("skuCode", skuCode);
                        rsMap.put("priceType", priceType);
                        return rsMap;
                    }
                }

                if (rs == null) {
                    $warn(String.format("setProductSalePrice: 数据错误 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                    rsMap.put("ecd", 8);
                    rsMap.put("prodCode", prodCode);
                    rsMap.put("skuCode", skuCode);
                    return rsMap;
                }
                if (rs < 0) {
                    $warn(String.format("setProductSalePrice: 数据错误 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                    rsMap.put("ecd", 10);
                    rsMap.put("prodCode", prodCode);
                    rsMap.put("skuCode", skuCode);
                    return rsMap;
                }
                // 修改前的最终售价
                double befPriceSale = skuObj.getDoubleAttribute("priceSale");
                if (rs == befPriceSale) {
                    // 修改前后价格相同
                    $info(String.format("setProductSalePrice: 修改前后价格相同 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                    continue;
                }

                Object priceRetail = skuObj.get("priceRetail");
                if (priceRetail == null) {
                    $warn(String.format("setProductSalePrice: 缺少数据 priceRetail为空 code=%s, sku=%s", prodCode, skuCode));
                    rsMap.put("ecd", 9);
                    rsMap.put("prodCode", prodCode);
                    rsMap.put("skuCode", skuCode);
                    rsMap.put("priceType", "priceRetail");
                    return rsMap;
                }
                // 指导价
                Double result = 0D;
                if (priceRetail instanceof Double) {
                    result = (Double) priceRetail;
                } else {
                    if (!StringUtil.isEmpty(priceRetail.toString())) {
                        result = new Double(priceRetail.toString());
                    } else {
                        $warn(String.format("setProductSalePrice: 数据错误 priceRetail格式错误 code=%s, sku=%s", prodCode, skuCode));
                        rsMap.put("ecd", 9);
                        rsMap.put("prodCode", prodCode);
                        rsMap.put("skuCode", skuCode);
                        rsMap.put("priceType", "priceRetail");
                        return rsMap;
                    }
                }
                // 要更新最终售价变化状态
                String diffFlg = productSkuService.getPriceDiffFlg(breakThreshold, rs, result);
                if ("2".equals(diffFlg)) {
                    $info(String.format("setProductSalePrice: 输入的最终售价低于指导价，不更新此sku的价格 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                    prodPriceDownList.add(prodCode + "\t " + skuCode + "\t " + befPriceSale + "\t " + result + "\t\t " + rs);
                    continue;
                } else if ("5".equals(diffFlg)) {
                    $info(String.format("setProductSalePrice: 输入的最终售价低于下限阈值，不更新此sku的价格 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                    prodPriceDownExList.add(prodCode + "\t " + skuCode + "\t " + befPriceSale + "\t " + result + "\t " + (result * (1 - breakThreshold)) + "\t " + rs);
                    continue;
                }
                // DOC-161 价格向上击穿的阀值检查 取消
//                else if ("4".equals(diffFlg)) {
//                    $info(String.format("setProductSalePrice: 输入的最终售价大于阈值，不更新此sku的价格 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
//                    prodPriceUpList.add(prodCode + "\t " + skuCode + "\t " + befPriceSale + "\t " + result + "\t " + (result * (breakThreshold + 1)) + "\t " + rs);
//                    continue;
//                    // 超过阈值时不更新，(下面注释掉的代码暂时保留，将来可能会有用)
////                    if (notChkPriceFlg == 1) {
////                        // 忽略检查
////                        $info(String.format("setProductSalePrice: 输入的最终售价大于阈值，强制更新此sku的价格 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
////                    } else {
////                        $warn(String.format("setProductSalePrice: 输入数据错误 大于阈值 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
////                        rsMap.put("ecd", 3);
////                        rsMap.put("prodCode", prodCode);
////                        rsMap.put("skuCode", skuCode);
////                        rsMap.put("priceSale", rs);
////                        rsMap.put("priceLimit", result * (breakThreshold + 1));
////                        return rsMap;
////                    }
//                }
                skuObj.setAttribute("priceSale", rs);
                skuObj.setAttribute("priceDiffFlg", diffFlg);

                CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
                cmsBtPriceLogModel.setChannelId(userInfo.getSelChannelId());
                cmsBtPriceLogModel.setProductId(prodObj.getProdId().intValue());
                cmsBtPriceLogModel.setCode(prodCode);
                cmsBtPriceLogModel.setCartId(cartId);
                cmsBtPriceLogModel.setSku(skuCode);
                cmsBtPriceLogModel.setSalePrice(rs);
                cmsBtPriceLogModel.setMsrpPrice(skuObj.getDoubleAttribute("priceMsrp"));
                cmsBtPriceLogModel.setRetailPrice(result);
                CmsBtProductModel_Sku comSku = prodObj.getCommonNotNull().getSku(skuCode);
                if (comSku == null) {
                    cmsBtPriceLogModel.setClientMsrpPrice(0d);
                    cmsBtPriceLogModel.setClientRetailPrice(0d);
                    cmsBtPriceLogModel.setClientNetPrice(0d);
                } else {
                    cmsBtPriceLogModel.setClientMsrpPrice(comSku.getClientMsrpPrice());
                    cmsBtPriceLogModel.setClientRetailPrice(comSku.getClientRetailPrice());
                    cmsBtPriceLogModel.setClientNetPrice(comSku.getClientNetPrice());
                }
                cmsBtPriceLogModel.setComment("高级检索 设置最终售价");
                cmsBtPriceLogModel.setCreated(new Date());
                cmsBtPriceLogModel.setCreater(userInfo.getUserName());
                cmsBtPriceLogModel.setModified(new Date());
                cmsBtPriceLogModel.setModifier(userInfo.getUserName());
                priceLogList.add(cmsBtPriceLogModel);
            }

            try {
                priceService.setPrice(prodObj, cartId, false);
            }catch (IllegalPriceConfigException | PriceCalculateException e) {
                $error(String.format("批量修改商品价格　调用PriceService.setPrice失败 channelId=%s, cartId=%s msg=%s", userInfo.getSelChannelId(), cartId.toString(), e.getMessage()), e);
                throw new BusinessException(e.getMessage());
            }
            // 是天猫平台时直接调用API更新sku价格(要求已上新)
            try {
                priceService.updateSkuPrice(userInfo.getSelChannelId(), cartId, prodObj);
            } catch (Exception e) {
                $error(String.format("批量修改商品价格　调用天猫API失败 channelId=%s, cartId=%s msg=%s", userInfo.getSelChannelId(), cartId.toString(), e.getMessage()), e);
            }

            // 更新产品的信息
            JongoUpdate updObj = new JongoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setUpdate("{$set:{'platforms.P" + cartId + ".skus':#,'modified':#,'modifier':#}}");
            updObj.setQueryParameters(prodObj.getCommon().getFields().getCode());
            updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userInfo.getUserName());
            BulkWriteResult rs = bulkList.addBulkJongo(updObj);
            if (rs != null) {
                $debug(String.format("批量修改商品价格 channelId=%s 执行结果=%s", userInfo.getSelChannelId(), rs.toString()));
            }
        }
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("批量修改商品价格 channelId=%s 结果=%s", userInfo.getSelChannelId(), rs.toString()));
        }

        // 需要记录价格变更履历
        $debug("批量修改商品价格 开始记入价格变更履历");
        long sta = System.currentTimeMillis();
        int cnt = cmsBtPriceLogService.addLogListAndCallSyncPriceJob(priceLogList);
        $debug("批量修改商品价格 记入价格变更履历结束 结果=" + cnt + " 耗时" + (System.currentTimeMillis() - sta));

        if (!PlatFormEnums.PlatForm.TM.getId().equals(cartObj.getPlatform_id())
                && !PlatFormEnums.PlatForm.JD.getId().equals(cartObj.getPlatform_id())
                && !PlatFormEnums.PlatForm.JM.getId().equals(cartObj.getPlatform_id())) {
            // 不是天猫平台时插入上新程序 votodo
            $debug("批量修改商品价格 开始记入SxWorkLoad表");
            sta = System.currentTimeMillis();
            sxProductService.insertSxWorkLoad(userInfo.getSelChannelId(), productCodes, cartId, userInfo.getUserName());
            $debug("批量修改商品价格 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));
        }

        // 如果有未处理的商品，则放入缓存
        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "2");
        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "3");
        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "4");
        if (prodPriceUpList.size() > 0) {
            commCacheService.setCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "2", prodPriceUpList);
        }
        if (prodPriceDownList.size() > 0) {
            commCacheService.setCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "3", prodPriceDownList);
        }
        if (prodPriceDownExList.size() > 0) {
            commCacheService.setCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "4", prodPriceDownExList);
        }
        rsMap.put("unProcList", prodPriceUpList.size() + prodPriceDownList.size() + prodPriceDownExList.size());*/
        rsMap.put("ecd", 0);
        return rsMap;
    }

    private Double getFinalSalePrice(BigDecimal baseVal, String optionType, String priceValueStr, int roundType) {
        BigDecimal priceValue = null;
        if (priceValueStr != null) {
            priceValue = new BigDecimal(priceValueStr);
        }
        BigDecimal rs = null;
        if ("=".equals(optionType)) {
            if (baseVal == null) {
                rs = priceValue;
            } else {
                rs = baseVal;
            }
        } else if ("+".equals(optionType)) {
            rs = baseVal.add(priceValue);
        } else if ("-".equals(optionType)) {
            rs = baseVal.subtract(priceValue);
        } else if ("*".equals(optionType)) {
            rs = baseVal.multiply(priceValue);
        } else if ("/".equals(optionType)) {
            rs = baseVal.divide(priceValue, 3, BigDecimal.ROUND_CEILING);
        }
        if (rs == null) {
            return null;
        } else {
            if (roundType == 1) {
                // 小数点向上取整
                return rs.setScale(0, BigDecimal.ROUND_CEILING).doubleValue();
            } else if (roundType == 2) {
                // 个位向下取整
                BigDecimal multyValue = new BigDecimal("10");
                if (rs.compareTo(multyValue) <= 0) {
                    // 少于10的直接返回
                    return rs.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
                }

                rs = rs.divide(multyValue);
                rs = rs.setScale(0, BigDecimal.ROUND_DOWN);
                rs = rs.multiply(multyValue);
                return rs.doubleValue();
            } else if (roundType == 3) {
                // 个位向上取整
                BigDecimal multyValue = new BigDecimal("10");
                rs = rs.divide(multyValue);
                rs = rs.setScale(1, BigDecimal.ROUND_UP);
                rs = rs.setScale(0, BigDecimal.ROUND_CEILING);
                rs = rs.multiply(multyValue);
                return rs.doubleValue();
            } else {
                return rs.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
            }
        }
    }

    /**
     * 获取数据文件内容
     */
    public String getCodeFile(UserSessionBean userInfo) {
        // 取回缓存
        List<String> prodPriceUpList = commCacheService.getCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "2");
        List<String> prodPriceDownList = commCacheService.getCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "3");
        List<String> prodPriceDownExList = commCacheService.getCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "4");
        // 删除缓存
        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "2");
        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "3");
        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userInfo.getUserId() + "4");

        StringBuilder rs = new StringBuilder();
        if (prodPriceUpList != null && prodPriceUpList.size() > 0) {
            prodPriceUpList.forEach(item -> {
                rs.append(item + "\t 修改后最终售价高于指导价向上阈值");
                rs.append(com.voyageone.common.util.StringUtils.LineSeparator);
            });
        }
        if (prodPriceDownList != null && prodPriceDownList.size() > 0) {
            prodPriceDownList.forEach(item -> {
                rs.append(item + "\t 修改后最终售价低于指导价");
                rs.append(com.voyageone.common.util.StringUtils.LineSeparator);
            });
        }
        if (prodPriceDownExList != null && prodPriceDownExList.size() > 0) {
            prodPriceDownExList.forEach(item -> {
                rs.append(item + "\t 修改后最终售价低于指导价向下阈值");
                rs.append(com.voyageone.common.util.StringUtils.LineSeparator);
            });
        }

        if (rs.length() == 0) {
            $warn("缓存中没有数据啊！");
            return null;
        } else {
            rs.insert(0, "  商品code\t  sku code\t 修改前售价\t 指导价\t 阈值\t 修改后售价\t 未处理原因" + com.voyageone.common.util.StringUtils.LineSeparator);
        }
        return rs.toString();
    }

    /**
     * 指导价变更批量确认 （与"重新计算指导售价"共用代码）
     */
    public Map<String, Object> confirmRetailPrice(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        Map<String, Object> rsMap = new HashMap<>();
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            rsMap.put("ecd", 1);
            return rsMap;
        }

        Integer cartId = (Integer) params.get("cartId");
        List<Integer> cartList = null;
        if (cartId == null || cartId == 0) {
            // 表示全平台更新
            // 店铺(cart/平台)列表
            List<TypeChannelBean> cartTypeList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            cartList = cartTypeList.stream().map((cartType) -> NumberUtils.toInt(cartType.getValue())).collect(Collectors.toList());
            if (cartList == null || cartList.isEmpty()) {
                $error("confirmRatailPrice 该店铺未设置平台 param=" + params.toString());
                rsMap.put("ecd", 0);
                return rsMap;
            }
        } else {
            cartList = new ArrayList<>(1);
            cartList.add(cartId);
        }

        $debug("指导价变更批量确认 开始批量处理");
        /*params.put("productIds", productCodes);
        params.put("cartIds", cartList);
        params.put("_taskName", params.get("_option"));
        params.put("_channleId", userInfo.getSelChannelId());
        params.put("_userName", userInfo.getUserName());*/

        try {
            if ("refreshRetailPrice".equalsIgnoreCase((String) params.get("_option"))) {
                // sender.sendMessage(CmsMqRoutingKey.CMS_TASK_AdvSearch_RefreshRetailPriceServiceJob, params);
                AdvSearchRefreshRetailPriceMQMessageBody mqMessageBody = new AdvSearchRefreshRetailPriceMQMessageBody();
                mqMessageBody.setCodeList(productCodes);
                mqMessageBody.setCartList(cartList);
                mqMessageBody.setUserName(userInfo.getUserName());
                mqMessageBody.setChannelId(userInfo.getSelChannelId());
                mqMessageBody.setSender(userInfo.getUserName());
                mqSenderService.sendMessage(mqMessageBody);
            } else {
                // sender.sendMessage(CmsMqRoutingKey.CMS_TASK_AdvSearch_AsynProcessJob, params);
                AdvSearchConfirmRetailPriceMQMessageBody mqMessageBody = new AdvSearchConfirmRetailPriceMQMessageBody();
                mqMessageBody.setChannelId(userInfo.getSelChannelId());
                mqMessageBody.setCartList(cartList);
                mqMessageBody.setCodeList(productCodes);
                mqMessageBody.setUserName(userInfo.getUserName());
                mqMessageBody.setSender(userInfo.getUserName());
                mqSenderService.sendMessage(mqMessageBody);
            }
        } catch (MQMessageRuleException e) {
            throw new BusinessException("MQ发送异常:" + e.getMessage());
        }
        rsMap.put("ecd", 0);
        return rsMap;
    }

    /**
     * 批量设置类目
     */
    public void bulkSetCategory(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        $debug("批量修改商品价格 开始处理");
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {

            new BusinessException("批量修改商品价格 没有code条件 params=" + params.toString());
        }

        Integer cartId = (Integer) params.get("cartId");
        if (cartId == null || cartId == 0) {
            new BusinessException("批量修改商品价格 没有cartId条件 params=" + params.toString());
        }
        String pCatPath = (String) params.get("pCatPath");
        String pCatId = (String) params.get("pCatId");
        if (StringUtil.isEmpty(pCatPath) || StringUtil.isEmpty(pCatId)) {
            new BusinessException("类目不能为空");
        }

        CmsPlatformCategoryUpdateMQMessageBody cmsPlatformCategoryUpdateMQMessageBody = new CmsPlatformCategoryUpdateMQMessageBody();
        cmsPlatformCategoryUpdateMQMessageBody.setpCatId(pCatId);
        cmsPlatformCategoryUpdateMQMessageBody.setpCatPath(pCatPath);
        cmsPlatformCategoryUpdateMQMessageBody.setSender(userInfo.getUserName());
        cmsPlatformCategoryUpdateMQMessageBody.setChannelId(userInfo.getSelChannelId());
        cmsPlatformCategoryUpdateMQMessageBody.setCartId(cartId);
        List<List<String>> productCodesList = CommonUtil.splitList(productCodes, 500);
        productCodesList.forEach(codes -> {
            cmsPlatformCategoryUpdateMQMessageBody.setProductCodes(codes);
            mqSenderService.sendMessage(cmsPlatformCategoryUpdateMQMessageBody);
        });

//        List<BulkUpdateModel> bulkList = new ArrayList<>(productCodes.size());
//        for (String productCode : productCodes) {
//            HashMap<String, Object> updateMap = new HashMap<>();
//            updateMap.put("platforms.P" + cartId + ".pCatPath", pCatPath);
//            updateMap.put("platforms.P" + cartId + ".pCatId", pCatId);
//            updateMap.put("platforms.P" + cartId + ".pCatStatus", 1);
//            HashMap<String, Object> queryMap = new HashMap<>();
//            queryMap.put("common.fields.code", productCode);
//            HashMap<String, Object> queryMap2 = new HashMap<>();
//            queryMap2.put("$in", new String[]{null, ""});
//            queryMap.put("platforms.P" + cartId + ".pCatPath", queryMap2);
//            BulkUpdateModel model = new BulkUpdateModel();
//            model.setUpdateMap(updateMap);
//            model.setQueryMap(queryMap);
//            bulkList.add(model);
//        }
//
//        cmsBtProductDao.bulkUpdateWithMap(userInfo.getSelChannelId(), bulkList, userInfo.getUserName(), "$set");
    }

    public void bulkSetPlatformFields(Map<String, Object> params, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        List<String> productCodes = (ArrayList<String>) params.get("productIds");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            productCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), cmsSession);
        }
        if (productCodes == null || productCodes.isEmpty()) {

            new BusinessException("批量修改商品属性 没有code条件 params=" + params.toString());
        }

        Integer cartId = (Integer) params.get("cartId");
        if (cartId == null || cartId == 0) {
            new BusinessException("批量修改商品属性 没有cartId条件 params=" + params.toString());
        }
        Map<String, Object> prop = (Map<String, Object>) params.get("property");
        String prop_id = StringUtils.trimToEmpty((String) prop.get("id"));
        List<BulkUpdateModel> bulkList = new ArrayList<>(productCodes.size());

        Field fields = SchemaJsonReader.readJsonForObject(prop);

        if (fields instanceof ComplexField) {
            ComplexField complexField = (ComplexField) fields;
            List<Field> complexFields = complexField.getFields();
            ComplexValue complexValue = complexField.getComplexValue();
            setComplexValue(complexFields, complexValue);
        }


        Map<String, Object> result = new LinkedHashMap<>();
        fields.getFieldValueToMap(result);

//        Map<String, Object> mqMessage = new HashedMap();
//        mqMessage.put("cartId", cartId);
//        mqMessage.put("channelId", userInfo.getSelChannelId());
//        mqMessage.put("productCodes", productCodes);
//        mqMessage.put("userName", userInfo.getUserName());
//        mqMessage.put("fieldsId", prop_id);
//        mqMessage.put("fieldsValue", result.get(prop_id));

        CmsBatchPlatformFieldsMQMessageBody mqMessageBody = new CmsBatchPlatformFieldsMQMessageBody();
        mqMessageBody.setCartId(cartId);
        mqMessageBody.setChannelId(userInfo.getSelChannelId());
        mqMessageBody.setProductCodes(productCodes);
        mqMessageBody.setUserName(userInfo.getUserName());
        mqMessageBody.setFieldsId(prop_id);
        mqMessageBody.setFieldsValue(result.get(prop_id));
        mqMessageBody.setSender(userInfo.getUserName());
        try {
            cmsMqSenderService.sendMessage(mqMessageBody);
        } catch (MQMessageRuleException e) {
            $error(e);
            e.printStackTrace();
        }
    }

    /**
     * set complex value.
     */
    private void setComplexValue(List<Field> fields, ComplexValue complexValue) {

        for (Field fieldItem : fields) {

            complexValue.put(fieldItem);

            FieldTypeEnum fieldType = fieldItem.getType();

            switch (fieldType) {
                case INPUT:
                    InputField inputField = (InputField) fieldItem;
                    String inputValue = inputField.getValue();
                    complexValue.setInputFieldValue(inputField.getId(), inputValue);
                    break;
                case SINGLECHECK:
                    SingleCheckField singleCheckField = (SingleCheckField) fieldItem;
                    Value checkValue = singleCheckField.getValue();
                    complexValue.setSingleCheckFieldValue(singleCheckField.getId(), checkValue);
                    break;
                case MULTICHECK:
                    MultiCheckField multiCheckField = (MultiCheckField) fieldItem;
                    List<Value> checkValues = multiCheckField.getValues();
                    complexValue.setMultiCheckFieldValues(multiCheckField.getId(), checkValues);
                    break;
                case MULTIINPUT:
                    MultiInputField multiInputField = (MultiInputField) fieldItem;
                    List<String> inputValues = multiInputField.getStringValues();
                    complexValue.setMultiInputFieldValues(multiInputField.getId(), inputValues);
                    break;
                case COMPLEX:
                    ComplexField complexField = (ComplexField) fieldItem;
                    List<Field> subFields = complexField.getFields();
                    ComplexValue subComplexValue = complexField.getComplexValue();
                    setComplexValue(subFields, subComplexValue);
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField) fieldItem;
                    List<ComplexValue> complexValueList = multiComplexField.getComplexValues();
                    complexValue.setMultiComplexFieldValues(multiComplexField.getId(), complexValueList);
                    break;

                default:
                    break;
            }

        }
    }
}
