package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.sx.ConditionPropValueService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsMtChannelConditionConfigModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 平台标题翻译
 *
 * @author morse.lu 2016/10/24
 * @version 2.6.0
 * @since 2.6.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_PlatformTitleTranslateJob)
public class CmsPlatformTitleTranslateMqService extends BaseMQCmsService {

    private static final String[][] TITLE_IDS = new String[][]{
            {"0", "common.fields.originalTitleCn", "originalTitleCn"}, // common
            {CartEnums.Cart.TM.getId(), "platforms.P" + CartEnums.Cart.TM.getId() + ".fields.title", "title"},
            {CartEnums.Cart.TG.getId(), "platforms.P" + CartEnums.Cart.TG.getId() + ".fields.title", "title"},
            {CartEnums.Cart.JD.getId(), "platforms.P" + CartEnums.Cart.JD.getId() + ".fields.productTitle", "productTitle"},
            {CartEnums.Cart.JG.getId(), "platforms.P" + CartEnums.Cart.JG.getId() + ".fields.productTitle", "productTitle"},
            {CartEnums.Cart.JM.getId(), "platforms.P" + CartEnums.Cart.JM.getId() + ".fields.productLongName", "productLongName"},
            {CartEnums.Cart.JGJ.getId(), "platforms.P" + CartEnums.Cart.JGJ.getId() + ".fields.productTitle", "productTitle"},
            {CartEnums.Cart.JGY.getId(), "platforms.P" + CartEnums.Cart.JGY.getId() + ".fields.productTitle", "productTitle"}
    }; // {cartId , title_id 全路径, title_id}
    @Autowired
    private ConditionPropValueService conditionPropValueService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    public String getTaskName() {
        return "CmsPlatformTitleTranslateMqService";
    }

    /**
     * 入口
     *    输入参数:
     *        runType: 0或1（0的场合， 如果未翻译的场合才会进行翻译， 1的场合， 不管是否已翻译， 都会进行翻译）
     *        channelId：xxx
     *        cartId：（0是common， 20天猫， 23天猫国际， 24京东， 26京东国际）(预定1是 全平台设值, 这版先不做)
     *        code：（不设置的场合就是所有code）
     *
     * @param messageMap Mq消息Map
     * @throws Exception
     */
    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        // 分析输入参数
        // 参数: 执行方式
        String runType;
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        } else {
            $error("平台标题翻译(MQ): 输入参数不存在: runType");
            return;
        }

        // channelId
        String channelId;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        } else {
            $error("平台标题翻译(MQ): 输入参数不存在: channelId");
            return;
        }

        // cartId
        String cartId;
        if (messageMap.containsKey("cartId")) {
            cartId = String.valueOf(messageMap.get("cartId"));
            String[] titleId = getTitleId(cartId);
            if (titleId == null) {
                $error("未作成此平台的逻辑!");
                return;
            }
        } else {
            $error("平台标题翻译(MQ): 输入参数不存在: cartId");
            return;
        }

        // code
        String code = null;
        if (messageMap.containsKey("code")) {
            code = String.valueOf(messageMap.get("code"));
        }

        // 初始化cms_mt_channel_condition_config表的条件表达式
        conditionPropValueService.init();

        doMain(channelId, cartId, code, runType);

    }

    private void doMain(String channelId, String cartId, String code, String runType) {
        if (!StringUtils.isEmpty(code)) {
            // 单个code
            try {
                executeSingleCode(channelId, Integer.valueOf(cartId), code, runType);
            } catch (Exception ex) {
                // 错误都在executeSingleCode里面打印了,这边只是catch一下，让程序正常结束
            }
        } else {
            // 不设置的场合就是所有code
            List<String> listCode = cmsBtProductDao.selectAll(channelId).stream().map(model -> model.getCommon().getFields().getCode()).collect(Collectors.toList());
            for (String eachCode : listCode) {
                try {
                    executeSingleCode(channelId, Integer.valueOf(cartId), eachCode, runType);
                } catch (Exception ex) {
                    // 错误都在executeSingleCode里面打印了,这边只是catch一下，可以继续循环下一个code
                }
            }
        }
    }

    /**
     * 单个code
     */
    public void executeSingleCode(String channelId, int cartId, String code, String runType) {
        // 上新数据
        SxData sxData;

        try {
            sxData = sxProductService.getSxDataByCodeWithoutCheck(channelId, cartId, code, false);
            if (sxData == null) {
                throw new BusinessException("SxData取得失败!");
            }
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                // 有错误的时候，直接报错
                throw new BusinessException(sxData.getErrorMessage());
            }

            if (!judgeNeedUpdate(sxData ,runType)) {
                // 不需要翻译
//                $info("不需要翻译!code[%s]", code);
                return;
            }

            String searchKey = "title_trans_" + String.valueOf(cartId);
            List<CmsMtChannelConditionConfigModel> conditionPropValueModels = conditionPropValueService.get(channelId, searchKey);
            if (ListUtils.isNull(conditionPropValueModels)) {
                // 看看共通有没有
                conditionPropValueModels = conditionPropValueService.get(channelId, "title_trans_0");
            }

            if (ListUtils.isNull(conditionPropValueModels)) {
                // Condition表未设定,直接翻译
                List<String> transBaiduOrg = new ArrayList<>();
                // 20161109 tom 品牌不翻译
                String brandEn = sxData.getMainProduct().getCommon().getFields().getBrand();
                String nameEn = sxData.getMainProduct().getCommon().getFields().getProductNameEn();
                nameEn = nameEn.replaceAll("(?i)" + brandEn, ""); // 删掉英文标题里的品牌（忽略大小写）
                transBaiduOrg.add(nameEn);
                List<String> transBaiduCn = sxProductService.transBaidu(transBaiduOrg);
                if (ListUtils.isNull(transBaiduCn)) {
                    throw new BusinessException("百度翻译失败!");
                }
                // 回写product表
                updateTitle(channelId, String.valueOf(cartId), code, brandEn + " " + transBaiduCn.get(0)); // 在翻译之后的内容前， 加上品牌
            } else {
                // Condition表设定了
                ShopBean shopBean = Shops.getShop(channelId, cartId);

                // 表达式解析子
                ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

                RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                boolean isFind = false;
                for (CmsMtChannelConditionConfigModel conditionPropValueModel : conditionPropValueModels) {
                    // 应该只有一条,总之取第一条就好
                    String conditionExpressionStr = conditionPropValueModel.getConditionExpression();
                    RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                    String propValue = expressionParser.parse(conditionExpression, shopBean, getTaskName(), null);
                    if (!StringUtils.isEmpty(propValue)) {
                        isFind = true;
                        $info("翻译后标题为:" + propValue);
                        // 回写product表
                        updateTitle(channelId, String.valueOf(cartId), code, propValue);
                        break;
                    }
                }
                if (!isFind) {
                    throw new BusinessException("未找到符合条件的设定!");
                }

            }
        } catch (Exception e) {
            String errMsg = String.format("商品标题设值失败[ChannelId:%s] [cartId:%s] [code:%s]!", channelId, String.valueOf(cartId), code);
            if (StringUtils.isEmpty(e.getMessage())) {
                e.printStackTrace();
            } else {
                errMsg += e.getMessage();
            }
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
    }

    /**
     * 单个code
     */
    public void executeSingleCode(SxData sxData, String runType) {
        String channelId = "";
        int cartId = 0;
        String code = "";
        String newTitle = "";
        String titleName = "";
        try {
            channelId = sxData.getChannelId();
            cartId = sxData.getCartId();
            code = sxData.getMainProduct().getCommon().getFields().getCode();
            titleName = getTitleId(String.valueOf(cartId))[2];

            if (!judgeNeedUpdate(sxData ,runType)) {
                // 不需要翻译
//                $info("不需要翻译!code[%s]", code);
                return;
            }

            String searchKey = "title_trans_" + String.valueOf(cartId);
            List<CmsMtChannelConditionConfigModel> conditionPropValueModels = conditionPropValueService.get(channelId, searchKey);
            if (ListUtils.isNull(conditionPropValueModels)) {
                // 看看共通有没有
                conditionPropValueModels = conditionPropValueService.get(channelId, "title_trans_0");
            }

            if (ListUtils.isNull(conditionPropValueModels)) {
                // Condition表未设定,直接翻译
                List<String> transBaiduOrg = new ArrayList<>();
                // 20161109 tom 品牌不翻译
                String brandEn = sxData.getMainProduct().getCommon().getFields().getBrand();
                String nameEn = sxData.getMainProduct().getCommon().getFields().getProductNameEn();
                nameEn = nameEn.replaceAll("(?i)" + brandEn, ""); // 删掉英文标题里的品牌（忽略大小写）
                transBaiduOrg.add(nameEn);
                List<String> transBaiduCn = sxProductService.transBaidu(transBaiduOrg);
                if (ListUtils.isNull(transBaiduCn)) {
                    throw new BusinessException("百度翻译失败!");
                }
                // 回写product表
                newTitle = brandEn + " " + transBaiduCn.get(0);
                updateTitle(channelId, String.valueOf(cartId), code, newTitle); // 在翻译之后的内容前， 加上品牌

            } else {
                // Condition表设定了
                ShopBean shopBean = Shops.getShop(channelId, cartId);

                // 表达式解析子
                ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

                RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                boolean isFind = false;
                for (CmsMtChannelConditionConfigModel conditionPropValueModel : conditionPropValueModels) {
                    // 应该只有一条,总之取第一条就好
                    String conditionExpressionStr = conditionPropValueModel.getConditionExpression();
                    RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                    String propValue = expressionParser.parse(conditionExpression, shopBean, getTaskName(), null);
                    if (!StringUtils.isEmpty(propValue)) {
                        isFind = true;
                        $info("翻译后标题为:" + propValue);
                        newTitle = propValue;
                        // 回写product表
                        updateTitle(channelId, String.valueOf(cartId), code, newTitle);
                        break;
                    }
                }
                if (!isFind) {
                    throw new BusinessException("未找到符合条件的设定!");
                }

            }
            sxData.getMainProduct().getPlatform(cartId).getFields().setStringAttribute(titleName, newTitle);

        } catch (Exception e) {
            String errMsg = String.format("商品标题设值失败[ChannelId:%s] [cartId:%s] [code:%s]!", channelId, String.valueOf(cartId), code);
            if (StringUtils.isEmpty(e.getMessage())) {
                e.printStackTrace();
            } else {
                errMsg += e.getMessage();
            }
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
    }

    /**
     * 回写title
     */
    private void updateTitle(String channelId, String cartId, String code, String title) {
        JongoUpdate updateQuery = new JongoUpdate();
        updateQuery.setQuery("{\"common.fields.code\": #}");
        updateQuery.setQueryParameters(code);

        String titleId = getTitleId(cartId)[1];
        updateQuery.setUpdate("{$set:{\"" + titleId + "\": #}}");
        updateQuery.setUpdateParameters(title);
        cmsBtProductDao.updateFirst(updateQuery, channelId);
    }

    /**
     * 判断是否需要翻译
     */
    private boolean judgeNeedUpdate(SxData sxData, String runType) {
        boolean needUpdate = false;

        if ("1".equals(runType)) {
            // 1的场合， 不管是否已翻译， 都会进行翻译
            needUpdate = true;
        } else {
            // 未翻译的场合才会进行翻译
            int cartId = sxData.getCartId();
            String[] titleId = getTitleId(String.valueOf(cartId));
            String oriVal = null;
            if (cartId == 0) {
                // common
                oriVal = sxData.getMainProduct().getCommon().getFields().getStringAttribute(titleId[2]);
            } else {
                // 各个平台
                if (sxData.getMainProduct().getPlatform(cartId).getFields() != null) {
                    oriVal = sxData.getMainProduct().getPlatform(cartId).getFields().getStringAttribute(titleId[2]);
                }
            }

            if (StringUtils.isEmpty(oriVal)) {
                needUpdate = true;
            }
        }

        return needUpdate;
    }

    /**
     * 取得title_id
     */
    private String[] getTitleId(String cartId) {
        for (String[] title : TITLE_IDS) {
            if (title[0].equals(cartId)) {
                return title;
            }
        }

        return null;
    }
}
