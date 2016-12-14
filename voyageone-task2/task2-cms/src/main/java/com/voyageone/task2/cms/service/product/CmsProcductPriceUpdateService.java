package com.voyageone.task2.cms.service.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 当产品sku的价格变更时，同步至code和group的价格范围, 目前同步中国建议售价、中国指导价和中国最终售价
 * 参数 channelId, prodId, cartId，具体设值参照 CmsBtPriceLogModel
 * 实施方法，不比较输入值和现有值的大小，直接重新计算价格区间
 * @author jiangjusheng on 2016/07/11
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_ProdcutPriceUpdateJob)
public class CmsProcductPriceUpdateService extends BaseMQCmsService {

    @Autowired
    ProductService productService;
    @Autowired
    ProductGroupService productGroupService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        //$info("参数" + JacksonUtil.bean2Json(messageMap));
        String channelId = StringUtils.trimToNull((String) messageMap.get("channelId"));
        if (channelId == null || messageMap.get("productId") == null || messageMap.get("cartId") == null) {
            $error("CmsProcductPriceUpdateService 缺少参数");
            return;
        }

        int cartId = (Integer) messageMap.get("cartId");
        Long prodId = null;
        if(messageMap.get("productId") instanceof Integer){
            prodId = Long.valueOf((Integer)messageMap.get("productId"));
        }else if(messageMap.get("productId") instanceof Long){
            prodId = (long) messageMap.get("productId");
        }
        $info( String.format("CmsProcductPriceUpdateService start channelId = %s  cartId = %d  prodId = %d",channelId,cartId,prodId));
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'prodId':#,'platforms.P#.skus':{$exists:true}}");
        queryObj.setParameters(prodId, cartId);
        queryObj.setProjectionExt("platforms.P" + cartId + ".mainProductCode", "platforms.P" + cartId + ".skus.priceMsrp", "platforms.P" + cartId + ".skus.priceRetail", "platforms.P" + cartId + ".skus.priceSale");
        CmsBtProductModel prodObj = productService.getProductByCondition(channelId, queryObj);
        if (prodObj == null) {
            $error("CmsProcductPriceUpdateService 产品不存在 参数=" + messageMap.toString());
            return;
        }

        CmsBtProductModel_Platform_Cart platObj =  prodObj.getPlatform(cartId);
        if (platObj == null) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 参数=" + messageMap.toString());
            return;
        }
        // 主商品code
        String mProdCode = StringUtils.trimToNull(platObj.getMainProductCode());
        if (mProdCode == null) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 缺少主商品code 参数=" + messageMap.toString());
            return;
        }
        List<BaseMongoMap<String, Object>> skuList = platObj.getSkus();
        if (skuList == null || skuList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据不正确 缺少platforms.skus 参数=" + messageMap.toString());
            return;
        }
        CmsBtProductGroupModel grpObj = productGroupService.selectMainProductGroupByCode(channelId, mProdCode, cartId);
        if (grpObj == null) {
            $error("CmsProcductPriceUpdateService 产品对应的group不存在 参数=" + messageMap.toString());
            return;
        }

        // 先计算价格范围
        List<Double> priceMsrpList = skuList.stream().map(skuObj -> skuObj.getDoubleAttribute("priceMsrp")).sorted().collect(Collectors.toList());
        if (priceMsrpList == null || priceMsrpList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceMsrp不正确 参数=" + messageMap.toString());
            return;
        }
        List<Double> priceRetailList = skuList.stream().map(skuObj -> skuObj.getDoubleAttribute("priceRetail")).sorted().collect(Collectors.toList());
        if (priceRetailList == null || priceRetailList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceRetail不正确 参数=" + messageMap.toString());
            return;
        }
        List<Double> priceSaleList = skuList.stream().map(skuObj -> skuObj.getDoubleAttribute("priceSale")).sorted().collect(Collectors.toList());
        if (priceSaleList == null || priceSaleList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据sku priceSale不正确 参数=" + messageMap.toString());
            return;
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
        updObj.setUpdateParameters(cartId, newPriceMsrpSt, cartId, newPriceMsrpEd, cartId, newPriceRetailSt, cartId, newPriceRetailEd, cartId, newPriceSaleSt, cartId, newPriceSaleEd, DateTimeUtil.getNowTimeStamp(), MqRoutingKey.CMS_TASK_ProdcutPriceUpdateJob);
        WriteResult rs = productService.updateFirstProduct(updObj, channelId);
        $debug("CmsProcductPriceUpdateService 产品platforms价格范围更新结果 " + rs.toString());

        // 然后更新group中的价格范围
        // 先取得group中各code的价格范围
        queryObj = new JongoQuery();
        queryObj.setQuery("{'platforms.P#.mainProductCode':#}");
        queryObj.setParameters(cartId, mProdCode);
        queryObj.setProjectionExt("platforms.P" + cartId + ".pPriceMsrpSt", "platforms.P" + cartId + ".pPriceMsrpEd", "platforms.P" + cartId + ".pPriceRetailSt", "platforms.P" + cartId + ".pPriceRetailEd", "platforms.P" + cartId + ".pPriceSaleSt", "platforms.P" + cartId + ".pPriceSaleEd");
        List<CmsBtProductModel> prodObjList = productService.getList(channelId, queryObj);
        if (prodObj == null || prodObjList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品不存在 参数=" + queryObj.toString());
            return;
        }

        List<Double> priceMsrpStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceMsrpSt")).sorted().collect(Collectors.toList());
        if (priceMsrpStList == null || priceMsrpStList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据pPriceMsrpSt不正确 参数=" + queryObj.toString());
            return;
        }
        List<Double> priceMsrpEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceMsrpEd")).sorted().collect(Collectors.toList());
        if (priceMsrpEdList == null || priceMsrpEdList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据pPriceMsrpEd不正确 参数=" + queryObj.toString());
            return;
        }
        List<Double> priceRetailStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceRetailSt")).sorted().collect(Collectors.toList());
        if (priceRetailStList == null || priceRetailStList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据pPriceRetailSt不正确 参数=" + queryObj.toString());
            return;
        }
        List<Double> priceRetailEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceRetailEd")).sorted().collect(Collectors.toList());
        if (priceRetailEdList == null || priceRetailEdList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据pPriceRetailEd不正确 参数=" + queryObj.toString());
            return;
        }
        List<Double> priceSaleStList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceSaleSt")).sorted().collect(Collectors.toList());
        if (priceSaleStList == null || priceSaleStList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据pPriceSaleSt不正确 参数=" + queryObj.toString());
            return;
        }
        List<Double> priceSaleEdList = prodObjList.stream().map(prodObjItem -> prodObjItem.getPlatformNotNull(cartId).getDoubleAttribute("pPriceSaleEd")).sorted().collect(Collectors.toList());
        if (priceSaleEdList == null || priceSaleEdList.isEmpty()) {
            $error("CmsProcductPriceUpdateService 产品数据pPriceSaleEd不正确 参数=" + queryObj.toString());
            return;
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
        updObj.setUpdateParameters(newPriceMsrpSt, newPriceMsrpEd, newPriceRetailSt, newPriceRetailEd, newPriceSaleSt, newPriceSaleEd, DateTimeUtil.getNowTimeStamp(), MqRoutingKey.CMS_TASK_ProdcutPriceUpdateJob);

        rs = productGroupService.updateFirst(updObj, (String) messageMap.get("channelId"));
        $debug("CmsProcductPriceUpdateService 产品group价格范围更新结果 " + rs.toString());
    }

}
