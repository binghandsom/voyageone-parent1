package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductVoRateUpdateMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR;
import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA;

/**
 * 当产品的vo扣点变更时的处理，计算指导价，记录价格变更履历/记录商品修改历史/同步价格范围/插入上新程序
 * 参数 channelId, codeList, voRate, creater
 *
 * @author jiangjusheng on 2016/08/01
 * @version 2.0.0
 */
@Service
public class CmsProductVoRateUpdateService extends BaseService {

    @Autowired
    private ProductService productService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private PlatformPriceService platformPriceService;

    public List<CmsBtOperationLogModel_Msg> updateProductVoRate(ProductVoRateUpdateMQMessageBody messageBody) throws Exception {

        $info("CmsProductVoRateUpdateService start");

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        String channelId = StringUtils.trimToNull(messageBody.getChannelId());
        List<String> codeList = messageBody.getCodeList();
        String userName = StringUtils.trimToEmpty(messageBody.getSender());

        String voRate = messageBody.getVoRate();
        String msg;
        if (voRate == null) {
            msg = "高价检索 批量更新VO扣点 清空";
        } else {
            msg = "高价检索 批量更新VO扣点 " + voRate;
        }

        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'common.fields.code': {$in: #}}");
        queryObj.setParameters(codeList);
//        queryObj.setProjectionExt("prodId", "channelId", "orgChannelId", "platforms", "common.fields", "common.skus");
        List<CmsBtProductModel> prodObj = productService.getList(channelId, queryObj);

        List<String> successList = new ArrayList<>();
        for (CmsBtProductModel productModel : prodObj) {

            String code = productModel.getCommon().getFields().getCode();

            productModel.getPlatforms().forEach((s, platform) -> {
                Integer cartId = platform.getCartId();

                if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                    return;

                // 如果该平台使用的FORMULA计算价格,则跳过通过voRate的价格计算处理
                CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanWithDefault(channelId, PRICE_CALCULATOR, cartId.toString());
                if (priceCalculatorConfig == null || PRICE_CALCULATOR_FORMULA.equals(priceCalculatorConfig.getConfigValue1()))
                    return;

                try {

                    // 重新计算价格
                    Integer chg = priceService.setPrice(productModel, cartId, false);

                    // 判断是否更新平台价格 如果要更新直接更新
                    platformPriceService.publishPlatFormPrice(channelId, chg, productModel, cartId, userName);

                    // 保存计算结果
                    JongoUpdate updObj = new JongoUpdate();
                    updObj.setQuery("{'common.fields.code':#}");
                    updObj.setQueryParameters(code);
                    updObj.setUpdate("{$set:{'platforms.P#.skus':#}}");
                    updObj.setUpdateParameters(cartId, productModel.getPlatform(cartId).getSkus());
                    WriteResult rs = productService.updateFirstProduct(updObj, channelId);
                    $debug("CmsProductVoRateUpdateService 保存计算结果 " + rs.toString());

                    // 记录价格变更履历/同步价格范围
                    List<CmsBtPriceLogModel> logModelList = new ArrayList<>(1);
                    for (BaseMongoMap skuObj : productModel.getPlatform(cartId).getSkus()) {
                        String skuCode = skuObj.getStringAttribute("skuCode");
                        CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
                        cmsBtPriceLogModel.setChannelId(channelId);
                        cmsBtPriceLogModel.setProductId(productModel.getProdId().intValue());
                        cmsBtPriceLogModel.setCode(code);
                        cmsBtPriceLogModel.setCartId(cartId);
                        cmsBtPriceLogModel.setSku(skuCode);
                        cmsBtPriceLogModel.setSalePrice(skuObj.getDoubleAttribute("priceSale"));
                        cmsBtPriceLogModel.setMsrpPrice(skuObj.getDoubleAttribute("priceMsrp"));
                        cmsBtPriceLogModel.setRetailPrice(skuObj.getDoubleAttribute("priceRetail"));
                        CmsBtProductModel_Sku comSku = productModel.getCommonNotNull().getSku(skuCode);
                        if (comSku == null) {
                            cmsBtPriceLogModel.setClientMsrpPrice(0d);
                            cmsBtPriceLogModel.setClientRetailPrice(0d);
                            cmsBtPriceLogModel.setClientNetPrice(0d);
                        } else {
                            cmsBtPriceLogModel.setClientMsrpPrice(comSku.getClientMsrpPrice());
                            cmsBtPriceLogModel.setClientRetailPrice(comSku.getClientRetailPrice());
                            cmsBtPriceLogModel.setClientNetPrice(comSku.getClientNetPrice());
                        }
                        cmsBtPriceLogModel.setComment(msg);
                        cmsBtPriceLogModel.setCreated(new Date());
                        cmsBtPriceLogModel.setCreater(userName);
                        cmsBtPriceLogModel.setModified(new Date());
                        cmsBtPriceLogModel.setModifier(userName);
                        logModelList.add(cmsBtPriceLogModel);
                    }

                    // 插入价格变更履历
                    int cnt = cmsBtPriceLogService.addLogListAndCallSyncPriceJob(logModelList);
                    $debug("CmsProductVoRateUpdateService修改商品价格 记入价格变更履历结束 结果=" + cnt);
                } catch (Exception exp) {

                    $error(String.format("CmsProductVoRateUpdateService 调用共通函数计算指导价时出错 channelId=%s, code=%s, cartId=%d, errmsg=%s", channelId, code, cartId, exp.getMessage()), exp);

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(code);
                    errorInfo.setMsg(String.format("调用共通函数计算指导价时出错 cartId=%d, errmsg=%s", cartId, exp.getMessage()));
                    return;
                }
            });

            successList.add(code);
        }

        // 记录商品修改历史
        $debug("CmsProductVoRateUpdateService 开始记入价格变更履历");
        long sta = System.currentTimeMillis();
        productStatusHistoryService.insertList(channelId, successList, -1, EnumProductOperationType.BatchUpdate, msg, userName);
        $debug("CmsProductVoRateUpdateService 记入价格变更履历结束 耗时" + (System.currentTimeMillis() - sta));

        return failList;
    }
}