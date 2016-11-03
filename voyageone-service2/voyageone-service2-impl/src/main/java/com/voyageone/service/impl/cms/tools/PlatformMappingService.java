package com.voyageone.service.impl.cms.tools;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.bean.cms.CustomPropBean;
import com.voyageone.service.dao.cms.CmsBtRefreshProductTaskDao;
import com.voyageone.service.dao.cms.CmsBtRefreshProductTaskItemDao;
import com.voyageone.service.dao.cms.mongo.CmsBtPlatformMappingDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtRefreshProductTaskItemModel;
import com.voyageone.service.model.cms.CmsBtRefreshProductTaskModel;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 查询, 获取和编辑平台类目的属性匹配。根据匹配计算商品属性值
 * <p>
 * Created by jonas on 8/13/16.
 *
 * @author jonas
 * @version 2.9.0
 * @since 2.4.0
 */
@Service
public class PlatformMappingService extends BaseService {
    public final static int CATEGORY_TYPE_COMMON = 1;
    public final static int CATEGORY_TYPE_SPECIFIC = 2;

    private final static String EXPRESSION_TYPE_FEED_CN = "FEED_CN";
    private final static String EXPRESSION_TYPE_FEED_ORG = "FEED_ORG";
    private final static String EXPRESSION_TYPE_MASTER = "MASTER";

    private final ProductService productService;
    private final CmsBtPlatformMappingDao platformMappingDao;
    private final MqSender mqSender;
    private final CmsBtRefreshProductTaskDao cmsBtRefreshProductTaskDao;
    private final CmsBtRefreshProductTaskItemDao cmsBtRefreshProductTaskItemDao;

    @Autowired
    public PlatformMappingService(ProductService productService, CmsBtPlatformMappingDao platformMappingDao,
                                  MqSender mqSender, CmsBtRefreshProductTaskDao cmsBtRefreshProductTaskDao,
                                  CmsBtRefreshProductTaskItemDao cmsBtRefreshProductTaskItemDao) {
        this.productService = productService;
        this.platformMappingDao = platformMappingDao;
        this.mqSender = mqSender;
        this.cmsBtRefreshProductTaskDao = cmsBtRefreshProductTaskDao;
        this.cmsBtRefreshProductTaskItemDao = cmsBtRefreshProductTaskItemDao;
    }

    public CmsBtPlatformMappingModel get(CmsBtPlatformMappingModel platformMappingModel, String channelId) {

        if (platformMappingModel.getCategoryType() == CATEGORY_TYPE_COMMON)
            return platformMappingDao.selectCommon(platformMappingModel.getCartId(), channelId);

        return platformMappingDao.selectOne(platformMappingModel.getCartId(), platformMappingModel.getCategoryType(),
                platformMappingModel.getCategoryPath(), channelId);
    }

    public boolean saveMap(CmsBtPlatformMappingModel fieldMapsModel, String modified) {

        if (platformMappingDao.exists(fieldMapsModel)) {

            String lastModified = platformMappingDao.selectModified(fieldMapsModel);

            if (!lastModified.equals(modified))
                return false;

            platformMappingDao.update(fieldMapsModel);
        } else {
            platformMappingDao.insert(fieldMapsModel);
        }

        return true;
    }

    public Map<String, Object> getValueMap(String channelId, Long productId, int cartId, String categoryPath) {

        // 查询需要用到的平台类目也在商品中获取

        CmsBtProductModel product = productService.getProductById(channelId, productId);

        if (StringUtils.isEmpty(categoryPath)) {
            CmsBtProductModel_Platform_Cart cart = product.getPlatform(cartId);
            categoryPath = cart.getpCatPath();
        }

        // 计算多级类目
        Stack<String> categoryPathStack = new Stack<>();
        categoryPathStack.push(categoryPath);
        String separator = ">";
        int index = categoryPath.lastIndexOf(separator);
        while (index > -1) {
            String part = categoryPath.substring(0, index);
            categoryPathStack.push(part);
            index = part.lastIndexOf(separator);
        }

        // 创建配置计算基础数据
        List<CustomPropBean> customPropBeanList = productService.getCustomProp(product);
        Map<String, Object> valueMap = new HashMap<>();
        ValueMapFiller filler = new ValueMapFiller(product, customPropBeanList);

        // 执行通用配置
        CmsBtPlatformMappingModel commonFieldMapsModel = platformMappingDao.selectCommon(cartId, channelId);
        if (commonFieldMapsModel != null)
            fillValueMap(valueMap, filler, commonFieldMapsModel);

        // 执行多级目录配置
        while (!categoryPathStack.isEmpty()) {
            String targetCategoryPath = categoryPathStack.pop();
            CmsBtPlatformMappingModel fieldMapsModel = platformMappingDao.selectOne(cartId, CATEGORY_TYPE_SPECIFIC, targetCategoryPath, channelId);
            if (fieldMapsModel != null)
                fillValueMap(valueMap, filler, fieldMapsModel);
        }

        return valueMap;
    }

    public List<CmsBtPlatformMappingModel> getPage(ChannelConfigEnums.Channel channel, Integer categoryType, Integer cartId, String categoryPath, int page, int size) {
        return platformMappingDao.selectPage(channel.getId(), categoryType, cartId, categoryPath, page * size, size);
    }

    public long getCount(ChannelConfigEnums.Channel channel, Integer categoryType, Integer cartId, String categoryPath) {
        return platformMappingDao.count(channel.getId(), categoryType, cartId, categoryPath);
    }

    public boolean delete(CmsBtPlatformMappingModel platformMappingModel) {

        WriteResult writeResult = platformMappingDao.delete(platformMappingModel);

        return writeResult.getN() > 0;
    }

    private void fillValueMap(Map<String, Object> valueMap, ValueMapFiller filler, CmsBtPlatformMappingModel fieldMapsModel) {

        Map<String, CmsBtPlatformMappingModel.FieldMapping> mappingMap = fieldMapsModel.getMappings();

        if (mappingMap == null || mappingMap.isEmpty())
            return;

        filler.fillValueMap(valueMap, mappingMap);
    }

    public boolean refreshProductsByMapping(CmsBtRefreshProductTaskModel cmsBtRefreshProductTaskModel, String userName) {

        List<Long> productIdList = getProductIdList(cmsBtRefreshProductTaskModel);

        if (productIdList == null || productIdList.isEmpty())
            return false;

        // 创建任务记录
        cmsBtRefreshProductTaskModel.setCreater(userName);
        cmsBtRefreshProductTaskModel.setModifier(userName);
        cmsBtRefreshProductTaskModel.setStatus(0);
        cmsBtRefreshProductTaskDao.insert(cmsBtRefreshProductTaskModel);

        // 创建商品任务记录
        productIdList.stream().map(prodId -> new CmsBtRefreshProductTaskItemModel() {{
            setTaskId(cmsBtRefreshProductTaskModel.getId());
            setProductId(prodId.intValue());
            setStatus(0);
            setCreater(userName);
            setModifier(userName);
        }}).forEach(cmsBtRefreshProductTaskItemDao::insert);

        Map<String, Object> map = new HashMap<>();
        map.put("cmsBtRefreshProductTaskModel", cmsBtRefreshProductTaskModel);
        mqSender.sendMessage(MqRoutingKey.CMS_TASK_REFRESH_PRODUCTS, map);

        return true;
    }

    private List<Long> getProductIdList(CmsBtRefreshProductTaskModel cmsBtRefreshProductTaskModel) {

        String query;

        switch (cmsBtRefreshProductTaskModel.getCategoryType()) {
            case PlatformMappingService.CATEGORY_TYPE_COMMON:
                // 全类目（通用类目）按平台查询
                query = String.format("{\"platforms.P%s.pCatPath\": {$exists: true}}", cmsBtRefreshProductTaskModel.getCartId());
                break;
            case PlatformMappingService.CATEGORY_TYPE_SPECIFIC:
                // 具体类目则按类目查询
                query = String.format("{\"platforms.P%s.pCatPath\": \"%s\"}", cmsBtRefreshProductTaskModel.getCartId(), cmsBtRefreshProductTaskModel.getCategoryPath());
                break;
            default:
                return null;
        }

        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(query);
        jongoQuery.setProjection("{\"prodId\":1}");

        List<CmsBtProductModel> productModelList = productService.getList(cmsBtRefreshProductTaskModel.getChannelId(), jongoQuery);

        return productModelList.stream().map(CmsBtProductModel::getProdId).collect(toList());
    }

    private class ValueMapFiller {

        private final Map<String, String> translatedFeedAttrs;

        private final BaseMongoMap<String, Object> cnAtts;

        private final BaseMongoMap<String, Object> orgAtts;

        private final CmsBtProductModel_Field master;

        ValueMapFiller(CmsBtProductModel product, List<CustomPropBean> customPropBeanList) {
            CmsBtProductModel_Feed feed = product.getFeed();
            CmsBtProductModel_Common common = product.getCommon();
            Map<String, String> translatedFeedAttrs = customPropBeanList.stream().collect(toMap(CustomPropBean::getFeedAttrEn, CustomPropBean::getFeedAttrValueCn));
            this.cnAtts = feed.getCnAtts();
            this.orgAtts = feed.getOrgAtts();
            this.translatedFeedAttrs = translatedFeedAttrs;
            this.master = common.getFields();
        }

        @SuppressWarnings("unchecked")
        private void fillValueMap(Map<String, Object> valueMap, Map<String, CmsBtPlatformMappingModel.FieldMapping> mappingMap) {

            // 循环所有配置
            // 如果没有表达式配置, 就简单的使用 value 作为值
            // 如果有表达式配置, 需要循环表达式, 根据配置类型去商品获取值进行拼接

            for (CmsBtPlatformMappingModel.FieldMapping mapping : mappingMap.values()) {

                Map<String, CmsBtPlatformMappingModel.FieldMapping> childrenMapping = mapping.getChildren();

                String fieldId = mapping.getFieldId();

                // 先看当前这个 mapping 有没有子字段匹配
                // 有的话, 说明是 complex
                // 那么整一个嵌套的 map 即可
                if (childrenMapping != null && !childrenMapping.isEmpty()) {

                    Object childrenValueObject = valueMap.get(fieldId);

                    Map<String, Object> childrenValue;

                    if (childrenValueObject == null || !(childrenValueObject instanceof Map))
                        childrenValue = new HashMap<>();
                    else
                        childrenValue = (Map<String, Object>) childrenValueObject;

                    fillValueMap(childrenValue, childrenMapping);

                    valueMap.put(mapping.getFieldId(), childrenValue);
                    continue;
                }

                List<CmsBtPlatformMappingModel.FieldMappingExpression> expressionList = mapping.getExpressions();

                if (expressionList == null) {
                    valueMap.put(mapping.getFieldId(), mapping.getValue());
                    continue;
                }

                StringBuilder valueBuilder = new StringBuilder();

                for (CmsBtPlatformMappingModel.FieldMappingExpression expression : expressionList) {

                    String key = expression.getValue();

                    switch (expression.getType()) {

                        case EXPRESSION_TYPE_FEED_CN:
                            Object cnFeedValue = cnAtts.get(key);
                            if (cnFeedValue == null)
                                cnFeedValue = translatedFeedAttrs.get(key);
                            if (cnFeedValue != null)
                                valueBuilder.append(String.valueOf(cnFeedValue));
                            break;

                        case EXPRESSION_TYPE_FEED_ORG:
                            Object orgFeedValue = orgAtts.get(key);
                            if (orgFeedValue != null)
                                valueBuilder.append(String.valueOf(orgFeedValue));
                            break;

                        case EXPRESSION_TYPE_MASTER:
                            String masterValue = master.getStringAttribute(key);
                            if (!StringUtils.isEmpty(masterValue))
                                valueBuilder.append(masterValue);
                            break;

                        default:
                            valueBuilder.append(expression.getValue());
                            break;
                    }

                    valueBuilder.append(expression.getAppend());
                }

                valueMap.put(mapping.getFieldId(), valueBuilder.toString());
            }
        }
    }
}
