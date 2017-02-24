package com.voyageone.service.impl.cms.vomqjobservice;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.BatchUpdateProductMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR;
import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA;

/**
 * 高级检索业务的批量更新
 *
 * @author jiangjusheng on 2016/08/24
 * @version 2.0.0
 */
@Service
public class CmsBatchUpdateService extends VOAbsLoggable {

    @Autowired
    private ProductService productService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private PlatformPriceService platformPriceService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    public List<CmsBtOperationLogModel_Msg> updateProductComField(BatchUpdateProductMQMessageBody messageBody) {
        // 错误map，key-value分别对于产品code和错误信息
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        String channelId = StringUtils.trimToNull(messageBody.getChannelId());
        String userName = StringUtils.trimToNull(messageBody.getSender());
        List<String> codeList = messageBody.getProductCodes();

        Map<String, Object> prop = (Map<String, Object>) messageBody.getParams().get("property");
        if (prop == null || prop.isEmpty()) {
            throw new BusinessException("高级检索 批量更新 缺少property参数");
        }
        String prop_id = StringUtils.trimToEmpty((String) prop.get("id"));
        String prop_name = StringUtils.trimToEmpty((String) prop.get("name"));
        if ("hsCodePrivate".equals(prop_id) || "hsCodeCross".equals(prop_id)) {
            // 税号更新
            String hsCode = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                hsCode = StringUtils.trimToEmpty((String) valObj.get("value"));
            }
            // 是否自动同步指导价到最终售价
            Boolean synPriceFlg = (Boolean) messageBody.getParams().get("synPrice");
            if (synPriceFlg == null) {
                synPriceFlg = false;
            }
            failList = updateHsCode(prop_id, prop_name, hsCode, codeList, channelId, userName, synPriceFlg);
        } else if ("translateStatus".equals(prop_id)) {
            // 翻译状态更新
            String stsCode = null;
            String priorDate = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                stsCode = StringUtils.trimToEmpty((String) valObj.get("value"));
                priorDate = StringUtils.trimToEmpty((String) valObj.get("priorTranslateDate"));
            }
            updateTranslateStatus(prop_name, stsCode, codeList, channelId, userName, priorDate);
        }
        return failList;
    }

    /**
     * 税号变更处理逻辑
     * @param propId 属性Id
     * @param propName 属性名称
     * @param propValue 属性值
     * @param codeList 产品Code列表
     * @param channelId 店铺Id
     * @param userName 操作者
     * @param synPriceFlg 价格是否同步Flg
     * @return 未处理错误数据列表
     */
    private List<CmsBtOperationLogModel_Msg> updateHsCode(String propId, String propName, String propValue, List<String> codeList, String channelId, String userName, Boolean synPriceFlg) {
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        final String msg = "高级检索批量 税号变更 " + propName + "=> " + propValue + " (根据配置判断同步最终售价)";
        List<String> successList = new ArrayList<>();

        // 获取所有的产品信息
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'common.fields.code': {$in: #}}");
        queryObj.setParameters(codeList);
        List<CmsBtProductModel> prodObj = productService.getList(channelId, queryObj);

        for (CmsBtProductModel newProduct : prodObj) {

            String prodCode = newProduct.getCommonNotNull().getFieldsNotNull().getCode();
            // 获取原始税号
            String oldHsCode = StringUtils.trimToNull((String) newProduct.getCommonNotNull().getFieldsNotNull().get(propId));

            if (productService.compareHsCode(oldHsCode, propValue))
                continue;

            // 设置新税号
            newProduct.getCommon().getFields().setAttribute(propId, propValue);

            // 处理各平台价格
            newProduct.getPlatforms().forEach((s, platform) -> {
                Integer cartId = platform.getCartId();

                if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                    return;

                // 如果该平台使用的FORMULA计算价格,则跳过通过voRate的价格计算处理
                CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanWithDefault(channelId, PRICE_CALCULATOR, cartId.toString());
                if (priceCalculatorConfig == null || PRICE_CALCULATOR_FORMULA.equals(priceCalculatorConfig.getConfigValue1()))
                    return;

                // 计算指导价
                try {

                    // 保存计算结果
                    JongoUpdate updObj = new JongoUpdate();
                    updObj.setQuery("{'common.fields.code':#}");
                    updObj.setQueryParameters(prodCode);
                    updObj.setUpdate("{$set:{'platforms.P#.skus':#, 'common.catConf':'1','common.fields.\" + propId + \"':#,'common.fields.hsCodeStatus':'1','common.fields.hsCodeSetter':#,'common.fields.hsCodeSetTime':#}}}");
                    updObj.setUpdateParameters(cartId, newProduct.getPlatform(cartId).getSkus(), propValue, userName, DateTimeUtil.getNow());
                    WriteResult rs = productService.updateFirstProduct(updObj, channelId);
                    $debug("CmsProductVoRateUpdateService 保存计算结果 " + rs.toString());

                    platformPriceService.updateProductPlatformPrice(newProduct, cartId, false, userName, msg);

                } catch (PriceCalculateException e) {

                    $error(String.format("高级检索 批量更新 价格计算错误 channleid=%s, prodcode=%s", channelId, prodCode), e);

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(prodCode);
                    errorInfo.setMsg("高级检索 批量更新 价格计算错误");
                    failList.add(errorInfo);
                    return;
                } catch (IllegalPriceConfigException e) {

                    $error(String.format("高级检索 批量更新 配置错误 channleid=%s, prodcode=%s", channelId, prodCode), e);

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(prodCode);
                    errorInfo.setMsg("高级检索 批量更新 配置错误");
                    failList.add(errorInfo);
                    return;
                } catch (Throwable e) {
                    $error(String.format("高级检索 批量更新 未知错误 channleid=%s, prodcode=%s", channelId, prodCode), e);

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(prodCode);
                    errorInfo.setMsg("高级检索 批量更新 未知错误");
                    failList.add(errorInfo);
                    return;
                }
            });

            successList.add(prodCode);
        }
        productStatusHistoryService.insertList(channelId, successList, -1, EnumProductOperationType.BatchUpdate, msg, userName);
        return failList;
    }

    /**
     * 翻译状态更新
     * @param propName 属性名称
     * @param propValue 属性值
     * @param codeList 产品Code列表
     * @param channelId 店铺Id
     * @param userName 操作者
     * @param priorDate 操作日期
     */
    private void updateTranslateStatus(String propName, String propValue, List<String> codeList, String channelId, String userName, String priorDate) {

        // 先找出所选商品的主商品code
        JongoQuery qryObj = new JongoQuery();
        qryObj.setQuery("{\"common.fields.code\":{$in:#}}");
        qryObj.setParameters(codeList);
        qryObj.setProjection("{'platforms.P0.mainProductCode':1,'_id':0}");
        List<CmsBtProductModel> prodList = productService.getList(channelId, qryObj);
        if (prodList == null || prodList.isEmpty()) {
            /*$error("高级检索 批量更新 翻译状态更新 没有找到主商品");
            return;*/
            throw new BusinessException(String.format("高级检索 批量更新 翻译状态更新 没有找到对应的商品, channelId=%s, codeList=%s", channelId, JacksonUtil.bean2Json(codeList)));
        }

        List<String> mnCodeList = prodList.stream()
                .map(prodObj -> prodObj.getPlatform(0).getMainProductCode())
                .filter(mnCode -> mnCode != null && mnCode.length() > 0)
                .collect(Collectors.toList());

        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'platforms.P0.mainProductCode':{$in:#}}");
        updObj.setQueryParameters(mnCodeList);
        if ("0".equals(propValue)) {
            updObj.setUpdate("{$set:{'common.fields.translateStatus':'0','common.fields.translator':'','common.fields.translateTime':'','common.fields.priorTranslateDate':''}}");
        } else if ("1".equals(propValue)) {
            updObj.setUpdate("{$set:{'common.fields.translateStatus':'1','common.fields.translator':#,'common.fields.translateTime':#,'common.fields.priorTranslateDate':''}}");
            updObj.setUpdateParameters(userName, DateTimeUtil.getNow());
        } else if ("2".equals(propValue)) {
            updObj.setUpdate("{$set:{'common.fields.translateStatus':'2','common.fields.translator':'','common.fields.translateTime':'','common.fields.priorTranslateDate':#}}");
            updObj.setUpdateParameters(priorDate);
        } else {
            throw new BusinessException(String.format("高级检索 批量更新 翻译状态更新 未知设置, translateStatus=%s", propValue));
        }
        WriteResult rs = productService.updateMulti(updObj, channelId);
        $debug("高级检索 批量更新 翻译状态批量更新结果 " + rs.toString());

        // 记录商品修改历史
        propValue = Types.getTypeName(TypeConfigEnums.MastType.translationStatus.getId(), "cn", propValue);
        productStatusHistoryService.insertList(channelId, codeList, -1, EnumProductOperationType.BatchUpdate, "高级检索 批量更新：" + propName +"--" + propValue, userName);
    }

}
