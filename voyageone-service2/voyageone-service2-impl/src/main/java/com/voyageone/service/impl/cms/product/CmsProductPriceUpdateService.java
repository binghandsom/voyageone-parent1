package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 当产品sku的价格变更时，同步至code和group的价格范围, 目前同步中国建议售价、中国指导价和中国最终售价
 * 参数 channelId, prodId, cartId，具体设值参照 CmsBtPriceLogModel
 * 实施方法，不比较输入值和现有值的大小，直接重新计算价格区间
 * @author jiangjusheng on 2016/07/11
 * @version 2.0.0
 */
@Service
public class CmsProductPriceUpdateService extends BaseService {

    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;

    /**
     * 更新product及group的价格
     * @param messageBody messageBody
     * @throws Exception Exception
     */
    public void updateProductAndGroupPrice(ProductPriceUpdateMQMessageBody messageBody) throws Exception {

        //$info("参数" + JacksonUtil.bean2Json(messageMap));
        String channelId = StringUtils.trimToNull(messageBody.getChannelId());

        int cartId = messageBody.getCartId();
        Long prodId = messageBody.getProdId();

        $info( String.format("CmsProcductPriceUpdateService start channelId = %s  cartId = %d  prodId = %d",channelId,cartId,prodId));
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'prodId':#,'platforms.P#.skus':{$exists:true}}");
        queryObj.setParameters(prodId, cartId);
        queryObj.setProjectionExt("platforms.P" + cartId + ".mainProductCode", "platforms.P" + cartId + ".skus.priceMsrp", "platforms.P" + cartId + ".skus.priceRetail", "platforms.P" + cartId + ".skus.priceSale", "platforms.P" + cartId + ".skus.isSale");
        CmsBtProductModel prodObj = productService.getProductByCondition(channelId, queryObj);
        if (prodObj == null) {
            $error("CmsProcductPriceUpdateService 产品不存在 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品不存在, params=%s", JacksonUtil.bean2Json(messageBody)));
        }

        CmsBtProductModel_Platform_Cart platObj =  prodObj.getPlatform(cartId);
        if (platObj == null) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品Platform不存在, prodId=%d, channelId=%s, cartId=%d", prodId, channelId, cartId));
        }
        // 主商品code
        String mProdCode = StringUtils.trimToNull(platObj.getMainProductCode());
        if (mProdCode == null) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 缺少主商品code 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品缺少主商品code prodId=%d, channelId=%s", prodId, channelId));
        }
        List<BaseMongoMap<String, Object>> skuList = platObj.getSkus();
        if (skuList == null || skuList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 缺少platforms.skus 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品参数不正确, 缺少platforms.skus prodId=%d, channelId=%s, cartId=%d", prodId, channelId, cartId));
        }
        CmsBtProductGroupModel grpObj = productGroupService.selectMainProductGroupByCode(channelId, mProdCode, cartId);
        if (grpObj == null) {
            $error("CmsProcductPriceUpdateService 产品对应的group不存在 参数=" + JacksonUtil.bean2Json(messageBody));
//            throw new BusinessException(String.format("产品对于的group不存在, mainProductCode=%s, channelId=%s, cartId=%d", mProdCode, channelId, cartId));
        }

        // 先计算价格范围
        List<Double> priceMsrpList = skuList.stream()
                .filter(skuObj -> skuObj.getAttribute("isSale") != null && (boolean)skuObj.getAttribute("isSale"))
                .map(skuObj -> skuObj.getDoubleAttribute("priceMsrp")).sorted().collect(Collectors.toList());
        if (priceMsrpList == null || priceMsrpList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceMsrp不正确 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品数据sku priceMsrp不正确, params=%s", JacksonUtil.bean2Json(messageBody)));
        }
        List<Double> priceRetailList = skuList.stream()
                .filter(skuObj -> skuObj.getAttribute("isSale") != null && (boolean)skuObj.getAttribute("isSale"))
                .map(skuObj -> skuObj.getDoubleAttribute("priceRetail")).sorted().collect(Collectors.toList());
        if (priceRetailList == null || priceRetailList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceRetail不正确 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品数据sku priceRetail不正确, params=%s", JacksonUtil.bean2Json(messageBody)));
        }
        List<Double> priceSaleList = skuList.stream()
                .filter(skuObj -> skuObj.getAttribute("isSale") != null && (boolean)skuObj.getAttribute("isSale"))
                .map(skuObj -> skuObj.getDoubleAttribute("priceSale")).sorted().collect(Collectors.toList());
        if (priceSaleList == null || priceSaleList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceSale不正确 参数=" + JacksonUtil.bean2Json(messageBody));
            throw new BusinessException(String.format("产品数据sku priceSale不正确, params=%s", JacksonUtil.bean2Json(messageBody)));
        }
        double newPriceMsrpSt = priceMsrpList.get(0);
        double newPriceMsrpEd = priceMsrpList.get(priceMsrpList.size() - 1);
        double newPriceRetailSt = priceRetailList.get(0);
        double newPriceRetailEd = priceRetailList.get(priceRetailList.size() - 1);
        double newPriceSaleSt = priceSaleList.get(0);
        double newPriceSaleEd = priceSaleList.get(priceSaleList.size() - 1);

        // 先更新产品platforms价格范围（不更新common中的价格范围）
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'prodId':#,'platforms.P#.skus':{$exists:true}}");
        updObj.setQueryParameters(prodId, cartId);
        updObj.setUpdate("{$set:{'platforms.P#.pPriceMsrpSt':#,'platforms.P#.pPriceMsrpEd':#, 'platforms.P#.pPriceRetailSt':#,'platforms.P#.pPriceRetailEd':#, 'platforms.P#.pPriceSaleSt':#,'platforms.P#.pPriceSaleEd':#, 'modified':#,'modifier':#}}");
        updObj.setUpdateParameters(cartId, newPriceMsrpSt, cartId, newPriceMsrpEd, cartId, newPriceRetailSt, cartId, newPriceRetailEd, cartId, newPriceSaleSt, cartId, newPriceSaleEd, DateTimeUtil.getNowTimeStamp(), CmsMqRoutingKey.CMS_BATCH_COUNT_PRODUCT_PRICE);
        WriteResult rs = productService.updateFirstProduct(updObj, channelId);
        $debug("CmsProcductPriceUpdateService 产品platforms价格范围更新结果 " + rs.toString());

        if(grpObj != null) {
            // 然后更新group中的价格范围
            // 先取得group中各code的价格范围
            queryObj = new JongoQuery();
            queryObj.setQuery("{'platforms.P#.mainProductCode':#}");
            queryObj.setParameters(cartId, mProdCode);
            queryObj.setProjectionExt("platforms.P" + cartId + ".pPriceMsrpSt", "platforms.P" + cartId + ".pPriceMsrpEd", "platforms.P" + cartId + ".pPriceRetailSt", "platforms.P" + cartId + ".pPriceRetailEd", "platforms.P" + cartId + ".pPriceSaleSt", "platforms.P" + cartId + ".pPriceSaleEd");
            List<CmsBtProductModel> prodObjList = productService.getList(channelId, queryObj);
            if (prodObj == null || prodObjList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品不存在 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品不存在, params=%s", queryObj.toString()));
            }

            List<Double> priceMsrpStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceMsrpSt")).sorted().collect(Collectors.toList());
            if (priceMsrpStList == null || priceMsrpStList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceMsrpSt不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceMsrpSt不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceMsrpEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceMsrpEd")).sorted().collect(Collectors.toList());
            if (priceMsrpEdList == null || priceMsrpEdList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceMsrpEd不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceMsrpEd不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceRetailStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceRetailSt")).sorted().collect(Collectors.toList());
            if (priceRetailStList == null || priceRetailStList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceRetailSt不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceRetailSt不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceRetailEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceRetailEd")).sorted().collect(Collectors.toList());
            if (priceRetailEdList == null || priceRetailEdList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceRetailEd不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceRetailEd不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceSaleStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceSaleSt")).sorted().collect(Collectors.toList());
            if (priceSaleStList == null || priceSaleStList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceSaleSt不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceSaleSt不正确, params=%s", queryObj.toString()));
            }
            List<Double> priceSaleEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceSaleEd")).sorted().collect(Collectors.toList());
            if (priceSaleEdList == null || priceSaleEdList.isEmpty()) {
                $error("CmsProcductPriceUpdateService 产品数据pPriceSaleEd不正确 参数=" + queryObj.toString());
                throw new BusinessException(String.format("产品数据pPriceSaleEd不正确, params=%s", queryObj.toString()));
            }
            newPriceMsrpSt = priceMsrpStList.get(0);
            newPriceMsrpEd = priceMsrpEdList.get(priceMsrpEdList.size() - 1);
            newPriceRetailSt = priceRetailStList.get(0);
            newPriceRetailEd = priceRetailEdList.get(priceRetailEdList.size() - 1);
            newPriceSaleSt = priceSaleStList.get(0);
            newPriceSaleEd = priceSaleEdList.get(priceSaleEdList.size() - 1);

            // 更新group中的价格范围
            updObj = new JongoUpdate();
            updObj.setQuery("{'mainProductCode':#,'cartId':#}");
            updObj.setQueryParameters(mProdCode, cartId);
            updObj.setUpdate("{$set:{'priceMsrpSt':#,'priceMsrpEd':#, 'priceRetailSt':#,'priceRetailEd':#, 'priceSaleSt':#,'priceSaleEd':#, 'modified':#,'modifier':#}}");
            updObj.setUpdateParameters(newPriceMsrpSt, newPriceMsrpEd, newPriceRetailSt, newPriceRetailEd, newPriceSaleSt, newPriceSaleEd, DateTimeUtil.getNowTimeStamp(), CmsMqRoutingKey.CMS_BATCH_COUNT_PRODUCT_PRICE);

            rs = productGroupService.updateFirst(updObj, messageBody.getChannelId());
            $debug("CmsProcductPriceUpdateService 产品group价格范围更新结果 " + rs.toString());
        }
    }

}
