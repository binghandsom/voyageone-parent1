package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsMtPlatformCategoryExtendInfoModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 天猫平台类目schema信息取得(指定参数)
 *
 * @author tom on 2016/8/9.
 * @version 2.2.0
 * @since 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformCategorySchemaTmJob)
public class CmsBuildPlatformCategorySchemaTmMqService extends BaseMQCmsService {

    @Autowired
    private GetPlatformCategorySchemaService getPlatformCategorySchemaService;
    @Autowired
    private PlatformCategoryService platformCategoryService;

    /**
     * 入口
     *    输入参数:
     *        runType: 执行方式 ("1": 指定channel和cart, "2": 指定channel和cart和category和categoryPath)
     *        channelId: channel id
     *        cartId: cart id
     *        categoryId: category id
     *        categoryPath: category path
     *
     * @param messageMap Mq消息Map
     * @throws Exception
     */
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        // 分析输入参数
        // 参数: 执行方式
        String runType;
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        } else {
            $error("天猫平台类目schema信息取得(MQ): 输入参数不存在: runType");
            return;
        }

        // 其他参数
        String channelId = null;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        }
        String cartId = null;
        if (messageMap.containsKey("cartId")) {
            cartId = String.valueOf(messageMap.get("cartId"));
        }
        String categoryId = null;
        if (messageMap.containsKey("categoryId")) {
            categoryId = String.valueOf(messageMap.get("categoryId"));
        }
        String categoryPath = null;
        if (messageMap.containsKey("categoryPath")) {
            categoryPath = String.valueOf(messageMap.get("categoryPath"));
        }

        // 关联检查
        switch (runType) {
            case "1":
                if (StringUtils.isEmpty(channelId) || StringUtils.isEmpty(cartId)) {
                    // 出错了
                    $error("天猫平台类目schema信息取得(MQ): 输入参数错误: 需要字符串形式的channelId和cartId");
                    return;
                } else {
                    ShopBean shopBean = Shops.getShop(channelId, cartId);
                    doLogic_type1_channel_cart(shopBean);
                }
                break;
            case "2":
                if (StringUtils.isEmpty(channelId) || StringUtils.isEmpty(cartId) || (StringUtils.isEmpty(categoryId) && StringUtils.isEmpty(categoryPath))) {
                    // 出错了
                    $error("天猫平台类目schema信息取得(MQ): 输入参数错误: 需要字符串形式的channelId和cartId和categoryId和categoryPath");
                    return;
                } else {
                    ShopBean shopBean = Shops.getShop(channelId, cartId);
                    // modified by morse.lu 2016/10/13 start
                    // 参数categoryId和categoryPath，有一个有值就可以了，另一个去检索得到
//                    doLogic_type2_channel_cart_category(shopBean, categoryId, categoryPath);
                    CmsMtPlatformCategorySchemaModel schemaModel;
                    if (!StringUtils.isEmpty(categoryId)) {
//                        schemaModel = platformCategoryService.getPlatformCatSchema(categoryId, Integer.valueOf(cartId));
                        schemaModel = platformCategoryService.getPlatformCatSchemaTm(categoryId, channelId, Integer.valueOf(cartId));
                    } else {
//                        schemaModel = platformCategoryService.getPlatformSchemaByCategoryPath(categoryPath, Integer.valueOf(cartId));
                        schemaModel = platformCategoryService.getTmallSchemaByCategoryPath(categoryPath, channelId, Integer.valueOf(cartId));
                    }

                    if (schemaModel == null) {
                        if (StringUtils.isEmpty(categoryId) || StringUtils.isEmpty(categoryPath)) {
                            $error("环境里没有此类目,新增加的话,参数categoryId和categoryPath必须都输入!不是的话,请检查参数是否正确!");
                            return;
                        } else {
                            doLogic_type2_channel_cart_category(shopBean, categoryId, categoryPath);
                        }
                    } else {
                        doLogic_type2_channel_cart_category(shopBean, schemaModel.getCatId(), schemaModel.getCatFullPath());
                    }
                    // modified by morse.lu 2016/10/13 end
                }
                break;
            default:
                $error("天猫平台类目schema信息取得(MQ): 输入参数错误: runType: 执行方式 (\"1\": 指定channel和cart, \"2\": 指定channel和cart和category和categoryPath): 当前输入值为" + runType);
                return;
        }

    }

    /**
     * 执行方式1: 根据channel和cart
     * @param shopBean
     */
    private void doLogic_type1_channel_cart(ShopBean shopBean) {
        Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap = getPlatformCategorySchemaService.doInit();

        String logInfo = String.format("获取天猫类目schema[MQ指定方式]-> channel:[%s], cart:[%s]", shopBean.getOrder_channel_id(), shopBean.getCart_id());
        getPlatformCategorySchemaService.doLogic(shopBean, platformCategoryExtendInfoMap, logInfo);
    }

    /**
     * 执行方式2: 根据channel和cart和category和categoryPath
     * @param shopBean
     * @param categoryId
     * @param categoryPath
     */
    private void doLogic_type2_channel_cart_category(ShopBean shopBean, String categoryId, String categoryPath) {
        Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap = getPlatformCategorySchemaService.doInit();

        String logInfo = String.format("获取天猫类目schema[MQ指定方式]-> channel:[%s], cart:[%s]", shopBean.getOrder_channel_id(), shopBean.getCart_id());
        getPlatformCategorySchemaService.doLogicSimple(shopBean, platformCategoryExtendInfoMap, categoryId, categoryPath, logInfo);
    }

}
