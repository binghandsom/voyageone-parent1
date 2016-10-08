package com.voyageone.task2.cms.service.product.batch;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 高级检索业务的批量更新
 *
 * @author jiangjusheng on 2016/08/24
 * @version 2.0.0
 */
@Service
public class CmsBacthUpdateTask extends VOAbsLoggable {

    @Autowired
    private ProductService productService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;

    public void onStartup(Map<String, Object> messageMap) {
        $debug("高级检索 批量更新 开始执行... param=" + messageMap.toString());
        String channleId = StringUtils.trimToNull((String) messageMap.get("_channleId"));
        String userName = StringUtils.trimToNull((String) messageMap.get("_userName"));
        List<String> codeList = (List<String>) messageMap.get("productIds");
        if (channleId == null || userName == null || codeList == null || codeList.isEmpty()) {
            $error("高级检索 批量更新 缺少参数");
            return;
        }

        Map<String, Object> prop = (Map<String, Object>) messageMap.get("property");
        if (prop == null || prop.isEmpty()) {
            $error("高级检索 批量更新 缺少property参数");
            return;
        }
        String prop_id = StringUtils.trimToEmpty((String) prop.get("id"));
        if ("hsCodePrivate".equals(prop_id) || "hsCodeCrop".equals(prop_id)) {
            // 税号更新
            String hsCode = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                hsCode = StringUtils.trimToEmpty((String) valObj.get("value"));
            }
            // 是否自动同步指导价到最终售价
            Boolean synPriceFlg = (Boolean) messageMap.get("synPrice");
            if (synPriceFlg == null) {
                synPriceFlg = false;
            }
            updateHsCode(prop_id, hsCode, codeList, channleId, userName, synPriceFlg);
        } else if ("translateStatus".equals(prop_id)) {
            // 翻译状态更新
            String stsCode = null;
            String priorDate = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                stsCode = StringUtils.trimToEmpty((String) valObj.get("value"));
                priorDate = StringUtils.trimToEmpty((String) valObj.get("priorTranslateDate"));
            }
            updateTranslateStatus(stsCode, codeList, channleId, userName, priorDate);
        }
    }

    /*
     * 税号变更
     */
    private void updateHsCode(String propId, String propValue, List<String> codeList, String channelId, String userName, Boolean synPriceFlg) {
        String msg = "税号变更 " + propId + "=> " + propValue;
        // 未配置自动同步的店铺，显示同步状况
        if (synPriceFlg) {
            msg += " (同步价格)";
        } else {
            CmsChannelConfigBean autoApprovePrice = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_APPROVE_PRICE);
            if (autoApprovePrice == null || !"1".equals(autoApprovePrice.getConfigValue1())) {
                msg += " (未同步最终售价)";
            }
        }

        String msgTxt = msg;
        boolean isUpdFlg = false;
        JongoUpdate updObj = new JongoUpdate();
        WriteResult rs = null;

        for (String prodCode : codeList) {
            try {
                CmsBtProductModel newProduct = productService.getProductByCode(channelId, prodCode);
                String oldHsCode = StringUtils.trimToNull((String) newProduct.getCommonNotNull().getFieldsNotNull().get(propId));

                updObj.setQuery("{'common.fields.code':#}");
                updObj.setQueryParameters(prodCode);
                updObj.setUpdate("{$set:{'common.fields." + propId + "':#,'common.fields.hsCodeStatus':'1','common.fields.hsCodeSetter':#,'common.fields.hsCodeSetTime':#}}");
                updObj.setUpdateParameters(propValue, userName, DateTimeUtil.getNow());
                rs = productService.updateFirstProduct(updObj, channelId);
                $debug("高级检索 批量更新 更新税号结果 " + rs.toString());

                // 重新计算并保存价格
                priceService.setPrice(newProduct, synPriceFlg || oldHsCode == null);
                newProduct.getPlatforms().forEach((s, platform) -> {
                    if (platform.getCartId() != 0) {
                        productService.updateProductPlatform(channelId, newProduct.getProdId(), platform, userName, false, EnumProductOperationType.BatchUpdate, msgTxt);
                    }
                });

                // 确认指导价变更
                List<Integer> cartList = newProduct.getCartIdList();
                for (Integer cartVal : cartList) {
                    isUpdFlg = false;
                    List<BaseMongoMap<String, Object>> skuList = newProduct.getPlatform(cartVal).getSkus();
                    for (BaseMongoMap skuObj : skuList) {
                        Boolean isSaleFlg = (Boolean) skuObj.get("isSale");
                        String chgFlg = StringUtils.trimToEmpty(skuObj.getStringAttribute("priceChgFlg"));
                        if ((chgFlg.startsWith("U") || chgFlg.startsWith("D")) && isSaleFlg) {
                            // 指导价有变更
                            skuObj.put("priceChgFlg", "0");
                            skuObj.put("confPriceRetail", skuObj.getDoubleAttribute("priceRetail"));
                            isUpdFlg = true;
                        }
                    }

                    // 更新产品的信息
                    if (isUpdFlg) {
                        updObj.setQuery("{'common.fields.code':#}");
                        updObj.setQueryParameters(prodCode);
                        updObj.setUpdate("{$set:{'platforms.P" + cartVal + ".skus':#,'modified':#,'modifier':#}}");
                        updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userName);

                        rs = productService.updateFirstProduct(updObj, channelId);
                        if (rs != null) {
                            $debug("高级检索 批量更新 指导价变更批量确认 code=%s, channelId=%s 执行结果=%s", prodCode, channelId, rs.toString());
                        }
                    }
                }
            } catch (PriceCalculateException e) {
                $error(String.format("高级检索 批量更新 价格计算错误 channleid=%s, prodcode=%s", channelId, prodCode), e);
                continue;
            } catch (IllegalPriceConfigException e) {
                $error(String.format("高级检索 批量更新 配置错误 channleid=%s, prodcode=%s", channelId, prodCode), e);
                continue;
            } catch (Throwable e) {
                $error(String.format("高级检索 批量更新 未知错误 channleid=%s, prodcode=%s", channelId, prodCode), e);
                continue;
            }
        }
    }

    /**
     * 翻译状态更新
     */
    private void updateTranslateStatus(String propValue, List<String> codeList, String channelId, String userName, String priorDate) {
        // 先找出所选商品的主商品code
        JongoQuery qryObj = new JongoQuery();
        qryObj.setQuery("{'productCodes':{$in:#},'cartId':0}");
        qryObj.setParameters(codeList);
        qryObj.setProjection("{'mainProductCode':1,'_id':0}");
        List<CmsBtProductGroupModel> grpList = productGroupService.getList(channelId, qryObj);
        if (grpList == null || grpList.isEmpty()) {
            $error("高级检索 批量更新 翻译状态更新 没有找到主商品");
            return;
        }
        List<String> mnCodeList = grpList.stream().map(grpObj -> grpObj.getMainProductCode()).filter(mnCode -> mnCode != null && mnCode.length() > 0).collect(Collectors.toList());

        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'platforms.P0.mainProductCode':{$in:#}}");
        updObj.setQueryParameters(mnCodeList);
        if ("0".equals(propValue)) {
            updObj.setUpdate("{$set:{'common.fields.translateStatus':'0','common.fields.translator':'','common.fields.translateTime':''}}");
        } else if ("1".equals(propValue)) {
            updObj.setUpdate("{$set:{'common.fields.translateStatus':'1','common.fields.translator':#,'common.fields.translateTime':#}}");
            updObj.setUpdateParameters(userName, DateTimeUtil.getNow());
        } else if ("2".equals(propValue)) {
            updObj.setUpdate("{$set:{'common.fields.translateStatus':'2','common.fields.translator':'','common.fields.translateTime':'','common.fields.priorTranslateDate':#}}");
            updObj.setUpdateParameters(priorDate);
        } else {
            $warn("高级检索 批量更新 翻译状态更新 未知设置");
            return;
        }
        WriteResult rs = productService.updateMulti(updObj, channelId);
        $debug("高级检索 批量更新 翻译状态批量更新结果 " + rs.toString());

        // 记录商品修改历史
        propValue = Types.getTypeName(TypeConfigEnums.MastType.translationStatus.getId(), "cn", propValue);
        productStatusHistoryService.insertList(channelId, codeList, -1, EnumProductOperationType.BatchUpdate, "高级检索 批量更新：翻译状态--" + propValue, userName);
    }

}
