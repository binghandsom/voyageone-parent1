package com.voyageone.service.impl.cms.vomqjobservice;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.UpdateProductSalePriceMQMessageBody;
import com.voyageone.service.impl.com.cache.CommCacheService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 修改商品saleprice业务类
 *
 * @Author rex
 * @Create 2017-01-09 14:16
 */
@Service
public class CmsUpdateProductSalePriceService extends BaseService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    public void process(UpdateProductSalePriceMQMessageBody mqMessageBody){
        long threadNo =  Thread.currentThread().getId();
        String channelId = mqMessageBody.getChannelId();
        Integer cartId = mqMessageBody.getCartId();
        List<String> productCodes = mqMessageBody.getProductCodes();
        String userName = mqMessageBody.getSender();
        Integer userId = mqMessageBody.getUserId();
        CartBean cartObj = Carts.getCart(cartId);
        Map<String, Object> params = mqMessageBody.getParams();
        // 检查商品价格 notChkPrice=1时表示忽略价格超过阈值
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
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
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
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
        List<String> prodPriceUpList = new ArrayList<>();
        List<String> prodPriceDownList = new ArrayList<>();
        List<String> prodPriceDownExList = new ArrayList<>();

        List<ErrorInfo> errorInfos = new ArrayList<ErrorInfo>();
        List<CmsBtProductModel> prodObjList = productService.getList(channelId, qryObj);
        if(ListUtils.isNull(prodObjList)) return;
        $debug("批量修改商品价格 开始批量处理");
        int i=0;
        for (CmsBtProductModel prodObj : prodObjList) {
            i++;
            $info(String.format("threadNo=%d %d/%d",threadNo, i , prodObjList.size()));
            prodObj.setChannelId(channelId); // 为后面调用priceService.setPrice使用
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

            try {
                for (BaseMongoMap skuObj : skuList) {
                    skuCode = skuObj.getStringAttribute("skuCode");
                    if (StringUtils.isEmpty(skuCode)) {
                        $warn(String.format("setProductSalePrice: 缺少数据 code=%s, para=%s", prodCode, skuCode, params.toString()));
                        throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s]的数据错误，没有skuCode。", prodCode, cartId, channelId), null);
                    }

                    // 修改后的最终售价
                    Double rs = null;
                    if (StringUtils.isEmpty(priceType)) {
                        // 使用固定值
                        if (priceValue == null) {
                            $warn(String.format("setProductSalePrice: 没有填写金额 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                            throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s]的数据错误，没有填写价格。", prodCode, cartId, channelId), null);
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
                            throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的数据错误，没有priceType的数据。", prodCode, cartId, channelId, skuCode), null);
                        }
                    }

                    if (rs == null) {
                        $warn(String.format("setProductSalePrice: 数据错误 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的价格计算发生错误。请联系IT.", prodCode, cartId, channelId, skuCode), null);
                    }
                    if (rs < 0) {
                        $warn(String.format("setProductSalePrice: 数据错误 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的最终售价计算结果为负数，请重新输入。", prodCode, cartId, channelId, skuCode), null);
                    }
                    // 修改前的最终售价
                    double befPriceSale = skuObj.getDoubleAttribute("priceSale");
                    if (rs == befPriceSale) {
                        // 修改前后价格相同
//                        $info(String.format("setProductSalePrice: 修改前后价格相同 code=%s, sku=%s, para=%s", prodCode, skuCode, params.toString()));
                        continue;
                    }

                    Object priceRetail = skuObj.get("priceRetail");
                    if (priceRetail == null) {
                        $warn(String.format("setProductSalePrice: 缺少数据 priceRetail为空 code=%s, sku=%s", prodCode, skuCode));
                        throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的数据错误，没有priceRetail的数据。", prodCode, cartId, channelId, skuCode), null);
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
                            throw new BusinessException(prodCode, String.format("商品[code=%s, cartId=%s, channelId=%s, sku=%s]的数据错误，priceRetail格式错误。", prodCode, cartId, channelId, skuCode), null);
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
                    cmsBtPriceLogModel.setChannelId(channelId);
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
                    cmsBtPriceLogModel.setCreater(userName);
                    cmsBtPriceLogModel.setModified(new Date());
                    cmsBtPriceLogModel.setModifier(userName);
                    priceLogList.add(cmsBtPriceLogModel);
                }

                try {
                    priceService.setPrice(prodObj, cartId, false);
                }catch (IllegalPriceConfigException | PriceCalculateException e) {
                    $error(String.format("批量修改商品价格　调用PriceService.setPrice失败 channelId=%s, cartId=%s msg=%s", channelId, cartId.toString(), e.getMessage()), e);
                    throw new BusinessException(prodCode, String.format("批量修改商品价格　调用PriceService.setPrice失败 channelId=%s, cartId=%s", channelId, cartId), e);
                }
                // 更新产品的信息
                JongoUpdate updObj = new JongoUpdate();
                updObj.setQuery("{'common.fields.code':#}");
                updObj.setUpdate("{$set:{'platforms.P" + cartId + ".skus':#,'modified':#,'modifier':#}}");
                updObj.setQueryParameters(prodObj.getCommon().getFields().getCode());
                updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userName);
                BulkWriteResult rs = bulkList.addBulkJongo(updObj);
                if (rs != null) {
                    $debug(String.format("批量修改商品价格 channelId=%s 执行结果=%s", userName, rs.toString()));
                }

                // 是天猫平台时直接调用API更新sku价格(要求已上新)
                try {
                    priceService.updateSkuPrice(channelId, cartId, prodObj);
                } catch (Exception e) {
                    $error(String.format("批量修改商品价格　调用API失败 channelId=%s, cartId=%s msg=%s", channelId, cartId.toString(), e.getMessage()), e);
                    throw new BusinessException(prodCode, String.format("批量修改商品价格　调用API失败 channelId=%s, cartId=%s", channelId, cartId.toString()), e);
                }
            } catch (BusinessException be) {
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setProductCode(be.getCode());
                errorInfo.setMessage(be.getMessage());
                errorInfos.add(errorInfo);
            }
        }
        if (CollectionUtils.isNotEmpty(errorInfos)) {

            // 批量有错误，发邮件
            issueLog.log("中国最终售价设置错误", JacksonUtil.bean2Json(errorInfos), ErrorType.BatchJob, SubSystem.CMS);
        }
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("批量修改商品价格 channelId=%s 结果=%s", channelId, rs.toString()));
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
//            sxProductService.insertSxWorkLoad(channelId, productCodes, cartId, userName);
            $debug("批量修改商品价格 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));
        }

//        // 如果有未处理的商品，则放入缓存
//        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userId + "2");
//        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userId + "3");
//        commCacheService.deleteCache("CmsFieldEditService.setProductSalePrice", userId + "4");
//        if (prodPriceUpList.size() > 0) {
//            commCacheService.setCache("CmsFieldEditService.setProductSalePrice", userId + "2", prodPriceUpList);
//        }
//        if (prodPriceDownList.size() > 0) {
//            commCacheService.setCache("CmsFieldEditService.setProductSalePrice", userId + "3", prodPriceDownList);
//        }
//        if (prodPriceDownExList.size() > 0) {
//            commCacheService.setCache("CmsFieldEditService.setProductSalePrice", userId + "4", prodPriceDownExList);
//        }
        // rsMap.put("unProcList", prodPriceUpList.size() + prodPriceDownList.size() + prodPriceDownExList.size());
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


    class ErrorInfo {
        private String productCode;
        private String message;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
