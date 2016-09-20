package com.voyageone.task2.cms.service.product.batch;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 重新计算指导价
 *
 * @author jiangjusheng on 2016/09/20
 * @version 2.0.0
 */
@Service
public class CmsRefreshRetailPriceTask extends VOAbsLoggable {

    @Autowired
    private ProductService productService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    public void onStartup(Map<String, Object> messageMap) {
        $debug("高级检索 重新计算指导价 开始执行... param=" + messageMap.toString());
        String channleId = StringUtils.trimToNull((String) messageMap.get("_channleId"));
        String userName = StringUtils.trimToNull((String) messageMap.get("_userName"));
        List<String> codeList = (List<String>) messageMap.get("productIds");
        List<Integer> cartList = (List<Integer>) messageMap.get("cartIds");
        if (channleId == null || userName == null || codeList == null || codeList.isEmpty() || cartList == null || cartList.isEmpty()) {
            $error("高级检索 重新计算指导价 缺少参数");
            return;
        }

        // 是否自动最终售价同步指导价格
        CmsChannelConfigBean autoPriceCfg = CmsChannelConfigs.getConfigBeanNoCode(channleId, CmsConstants.ChannelConfig.AUTO_APPROVE_PRICE);
        String msg = "高级检索 重新计算指导售价";
        if (autoPriceCfg != null && "1".equals(autoPriceCfg.getConfigValue1())) {
            // 自动同步
            msg += "(自动同步到最终售价)";
        } else {
            // 不自动同步
            msg += "(未同步到最终售价)";
        }

        JongoQuery queryObj = new JongoQuery();
        JongoUpdate updObj = new JongoUpdate();

        for (Integer cartId : cartList) {
            for (String prodCode : codeList) {
                queryObj.setQuery("{'common.fields.code':#,'platforms.P#':{$exists:true}}");
                queryObj.setParameters(prodCode, cartId);
                queryObj.setProjectionExt("prodId", "channelId", "orgChannelId", "platforms.P" + cartId + ".skus", "common.fields", "common.skus");
                CmsBtProductModel prodObj = productService.getProductByCondition(channleId, queryObj);
                if (prodObj == null) {
                    $warn("CmsRefreshRetailPriceTask 产品不存在 channelId=%s, code=%s, cartId=%d", channleId, prodCode, cartId);
                    continue;
                }
                List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
                if (skuList == null || skuList.isEmpty()) {
                    $warn("CmsRefreshRetailPriceTask 产品sku数据不存在 channelId=%s, code=%s, cartId=%d", channleId, prodCode, cartId);
                    continue;
                }

                // 计算指导价
                try {
                    priceService.setPrice(prodObj, cartId, false);
                } catch (Exception exp) {
                    $error(String.format("CmsRefreshRetailPriceTask 调用共通函数计算指导价时出错 channelId=%s, code=%s, cartId=%d, errmsg=%s", channleId, prodCode, cartId, exp.getMessage()), exp);
                    continue;
                }

                // 保存计算结果
                updObj.setQuery("{'common.fields.code':#}");
                updObj.setQueryParameters(prodCode);
                updObj.setUpdate("{$set:{'platforms.P#.skus':#}}");
                updObj.setUpdateParameters(cartId, prodObj.getPlatform(cartId).getSkus());
                WriteResult rs = productService.updateFirstProduct(updObj, channleId);
                $debug("CmsRefreshRetailPriceTask 保存计算结果 " + rs.toString());

                // 记录价格变更履历/同步价格范围
                List<CmsBtPriceLogModel> logModelList = new ArrayList<>(1);
                for (BaseMongoMap skuObj : skuList) {
                    String skuCode = (String) skuObj.getStringAttribute("skuCode");
                    CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
                    cmsBtPriceLogModel.setChannelId(channleId);
                    cmsBtPriceLogModel.setProductId(prodObj.getProdId().intValue());
                    cmsBtPriceLogModel.setCode(prodCode);
                    cmsBtPriceLogModel.setCartId(cartId);
                    cmsBtPriceLogModel.setSku(skuCode);
                    cmsBtPriceLogModel.setSalePrice(skuObj.getDoubleAttribute("priceSale"));
                    cmsBtPriceLogModel.setMsrpPrice(skuObj.getDoubleAttribute("priceMsrp"));
                    cmsBtPriceLogModel.setRetailPrice(skuObj.getDoubleAttribute("priceRetail"));
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
                    cmsBtPriceLogModel.setComment("高级检索-重新计算指导售价");
                    cmsBtPriceLogModel.setCreated(new Date());
                    cmsBtPriceLogModel.setCreater(userName);
                    cmsBtPriceLogModel.setModified(new Date());
                    cmsBtPriceLogModel.setModifier(userName);
                    logModelList.add(cmsBtPriceLogModel);
                }
                int cnt = cmsBtPriceLogService.addLogListAndCallSyncPriceJob(logModelList);
                $debug("CmsRefreshRetailPriceTask修改商品价格 记入价格变更履历结束 结果=" + cnt);
            }

            // 记录商品修改历史
            $debug("CmsRefreshRetailPriceTask 开始记入商品修改历史");
            long sta = System.currentTimeMillis();
            productStatusHistoryService.insertList(channleId, codeList, cartId, EnumProductOperationType.BatchRefreshRetailPrice, msg, userName);
            $debug("CmsRefreshRetailPriceTask 记入商品修改历史结束 耗时" + (System.currentTimeMillis() - sta));

            // 只有最终售价变化了，才需要上新
            if (autoPriceCfg != null && "1".equals(autoPriceCfg.getConfigValue1())) {
                // 最终售价被自动同步
                CartBean cartObj = Carts.getCart(cartId);
//                if (PlatFormEnums.PlatForm.TM.getId().equals(cartObj.getPlatform_id())) {
//                    // TODO --天猫平台直接调用API, 暂时先使用原来代码
//
//                } else {
                    // 插入上新程序
                    $debug("CmsRefreshRetailPriceTask 开始记入SxWorkLoad表");
                    sta = System.currentTimeMillis();
                    sxProductService.insertSxWorkLoad(channleId, codeList, cartId, userName);
                    $debug("CmsRefreshRetailPriceTask 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));
//                }
            }
        }
    }

}
