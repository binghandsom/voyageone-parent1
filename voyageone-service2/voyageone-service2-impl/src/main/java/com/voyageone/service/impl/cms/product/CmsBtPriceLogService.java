package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtPriceLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPriceLogDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM.*;
import static java.util.stream.Collectors.toMap;

/**
 * 不干别的就记录商品价格变动
 * Created by jonas on 16/7/4.
 *
 * @author jonas
 * @version 2.5.0
 * @since 2.2.0
 */
@Service
public class CmsBtPriceLogService extends BaseService {

    private final CmsBtPriceLogDao priceLogDao;
    private final CmsBtPriceLogDaoExt priceLogDaoExt;
    private final CmsBtProductDao productDao;
    private final CmsMqSenderService cmsMqSenderService;
    private final CmsBtPriceConfirmLogService priceConfirmLogService;

    @Autowired
    public CmsBtPriceLogService(CmsBtPriceLogDao priceLogDao, CmsBtPriceLogDaoExt priceLogDaoExt,
                                CmsBtProductDao productDao, CmsMqSenderService cmsMqSenderService,
                                CmsBtPriceConfirmLogService priceConfirmLogService) {
        this.priceLogDao = priceLogDao;
        this.priceLogDaoExt = priceLogDaoExt;
        this.productDao = productDao;
        this.cmsMqSenderService = cmsMqSenderService;
        this.priceConfirmLogService = priceConfirmLogService;
    }

    public List<CmsBtPriceLogModel> getList(String sku, String code, String cartId, String channelId) {
        return priceLogDaoExt.selectListBySkuOnCart(sku, code, cartId, channelId);
    }

    public List<CmsBtPriceLogModel> getPage(String sku, String code, String cartId, String channelId, int offset, int limit) {
        return priceLogDaoExt.selectPageBySkuOnCart(sku, code, cartId, channelId, offset, limit);
    }

    public int getCount(String sku, String code, String cartId, String channelId) {
        return priceLogDaoExt.selectCountBySkuOnCart(sku, code, cartId, channelId);
    }

    /**
     * 高级搜索专用日志记录方法
     * <p>
     * 日志记录后, 会调用 MQ 发送价格同步请求
     * create by jiangjusheng
     */
    public int addLogListAndCallSyncPriceJob(List<CmsBtPriceLogModel> paramList) {
        if (paramList == null || paramList.isEmpty()) {
            $warn("CmsBtPriceLogService:addLogListAndCallSyncPriceJob 输入为空");
            return 0;
        }
        paramList = paramList.stream()
                .filter(newlog->{
                    CmsBtPriceLogModel lastLog = priceLogDaoExt.selectLastOneBySkuOnCart(newlog.getSku(), newlog.getCartId(), newlog.getChannelId());
                    if (lastLog != null && compareAllPrice(newlog, lastLog)){
                        return false;
                    }else{
                        return true;
                    }
                })
                .collect(Collectors.toList());
        if(ListUtils.isNull(paramList)) return 0;
        int rs = priceLogDaoExt.insertCmsBtPriceLogList(paramList);

        if(paramList.size()>0){
            Map<String,CmsBtPriceLogModel> paramMap = new HashMap<>();
            for(CmsBtPriceLogModel cmsBtPriceLogModel:paramList){
                String key = String.format("%s-%d-%d",cmsBtPriceLogModel.getChannelId(),cmsBtPriceLogModel.getProductId(),cmsBtPriceLogModel.getCartId());
                if(!paramMap.containsKey(key)){
                    paramMap.put(key, cmsBtPriceLogModel);
                }
            }
            if (paramMap.size() > 0) {
                for (CmsBtPriceLogModel value:paramMap.values()) {
                    ProductPriceUpdateMQMessageBody mqMessageBody = new ProductPriceUpdateMQMessageBody();
                    mqMessageBody.setChannelId(value.getChannelId());
                    mqMessageBody.setCartId(value.getCartId());
                    mqMessageBody.setProdId(Long.valueOf(String.valueOf(value.getProductId())));
                    mqMessageBody.setSender(this.getClass().getSimpleName());
                    cmsMqSenderService.sendMessage(mqMessageBody);
                }
            }
        }
        // 先做完所有价格范围同步的请求后，再开始处理是否记录未确认价格的操作
//        for (CmsBtPriceLogModel newLog : paramList) {
//            String channelId = newLog.getChannelId();
//            int cartId = newLog.getCartId();
//            BaseMongoMap<String, Object> skuModel = getSinglePlatformSku(newLog.getSku(), cartId, channelId);
//            Double confirmPrice = skuModel.getDoubleAttribute(confPriceRetail.name());
//            // 检查价格，是否需要记录未确认
//            if (newLog.getRetailPrice() >= 0 && !newLog.getRetailPrice().equals(confirmPrice))
//                priceConfirmLogService.addUnConfirmed(channelId, cartId, newLog.getCode(), skuModel, newLog.getCreater());
//        }

        return rs;
    }

    /**
     * 对指定的 sku 进行价格变动检查
     *
     * @param skuList   将要检查的 sku 列表
     * @param channelId 所属渠道
     * @param cartId    所属平台
     * @param username  变动人 / 检查人
     * @param comment   变动备注 / 检查备注
     */
    public void addLogForSkuListAndCallSyncPriceJob(List<String> skuList, String channelId, Long productId, Integer cartId, String username, String comment) {
        for (String sku : skuList) {
            addLogAndCallSyncPriceJob(sku, channelId, cartId, username, comment);
        }
        /*Map<String,Object> newLog = new HashMap<>();
        newLog.put("cartId",cartId);
        newLog.put("productId",productId);
        newLog.put("channelId",channelId);*/
        // 向Mq发送消息同步sku,code,group价格范围
        ProductPriceUpdateMQMessageBody mqMessageBody = new ProductPriceUpdateMQMessageBody();
        mqMessageBody.setChannelId(channelId);
        mqMessageBody.setCartId(cartId);
        mqMessageBody.setProdId(productId);
        mqMessageBody.setSender(username);
        cmsMqSenderService.sendMessage(mqMessageBody);

    }

    /**
     * 对商品中有效的 sku 价格数据与数据库中上次记录的价格数据进行比较，如果有差异则新建价格记录
     *
     * @param channelId    渠道
     * @param productModel 包含价格的 common 和 platform 商品模型
     * @param comment      价格变更备注
     * @param username     此次操作的操作人
     */
    public void addLogAndCallSyncPriceJob(final String channelId, CmsBtProductModel productModel, final String comment, final String username) {

        final String code = productModel.getCommon().getFields().getCode();
        final Integer productId = productModel.getProdId().intValue();

        Map<String, CmsBtProductModel_Sku> commonSkuMap = productModel
                .getCommon()
                .getSkus()
                .stream()
                .collect(toMap(CmsBtProductModel_Sku::getSkuCode, skuCommonModel -> skuCommonModel));

        productModel
                .getPlatforms()
                .values()
                .stream()
                .filter(platform -> platform != null)
                .filter(platform -> {
                    int cartId = platform.getCartId();
                    return cartId >= 20 && cartId < 900;
                })
                .filter(platform -> platform.getSkus().size() > 0)
                .forEach(platform -> {

                    final Integer boxedCartId = platform.getCartId();

                    long logCount = platform.getSkus()
                            .stream()
                            .map(skuModel -> {
                                final String skuCode = skuModel.getStringAttribute("skuCode");
                                if (StringUtils.isEmpty(skuCode))
                                    return null;
                                if (!commonSkuMap.containsKey(skuCode))
                                    return null;
                                CmsBtProductModel_Sku skuCommonModel = commonSkuMap.get(skuCode);

                                CmsBtPriceLogModel newLog = new CmsBtPriceLogModel();

                                newLog.setSku(skuCode);

                                newLog.setClientMsrpPrice(tryGetPrice(skuCommonModel.getClientMsrpPrice()));
                                newLog.setClientNetPrice(tryGetPrice(skuCommonModel.getClientNetPrice()));
                                newLog.setClientRetailPrice(tryGetPrice(skuCommonModel.getClientRetailPrice()));
                                newLog.setMsrpPrice(skuModel.getDoubleAttribute(priceMsrp.name()));
                                newLog.setRetailPrice(skuModel.getDoubleAttribute(priceRetail.name()));
                                newLog.setSalePrice(skuModel.getDoubleAttribute(priceSale.name()));

                                skuModel.put("newLog", newLog);

                                return skuModel;
                            })
                            .filter(skuModel -> skuModel != null)
                            .filter(skuModel -> {
                                CmsBtPriceLogModel newLog = (CmsBtPriceLogModel) skuModel.get("newLog");
                                CmsBtPriceLogModel lastLog = priceLogDaoExt.selectLastOneBySkuOnCart(newLog.getSku(), boxedCartId, channelId);
                                if (lastLog != null && compareAllPrice(newLog, lastLog))
                                    return false;
                                final Date now = new Date();
                                newLog.setChannelId(channelId);
                                newLog.setCartId(boxedCartId);
                                newLog.setCode(code);
                                newLog.setProductId(productId);
                                newLog.setComment(comment);
                                newLog.setCreated(now);
                                newLog.setModified(now);
                                newLog.setCreater(username);
                                newLog.setModifier(username);

                                priceLogDao.insert(newLog);

                                Double confirmPrice = skuModel.getDoubleAttribute(confPriceRetail.name());

                                if (newLog.getRetailPrice() >= 0 && !newLog.getRetailPrice().equals(confirmPrice))
                                    priceConfirmLogService.addUnConfirmed(channelId, boxedCartId, newLog.getCode(), skuModel, newLog.getCreater());

                                return true;
                            })
                            .count();

                    if (logCount > 0) {
                        /*CmsBtPriceLogModel newLog = new CmsBtPriceLogModel();
                        newLog.setCartId(boxedCartId);
                        newLog.setProductId(productId);
                        newLog.setChannelId(channelId);*/

                        // 向Mq发送消息同步sku,code,group价格范围
                        // sender.sendMessage(CmsMqRoutingKey.CMS_TASK_ProdcutPriceUpdateJob, JacksonUtil.jsonToMap(JacksonUtil.bean2Json(newLog)));
                        ProductPriceUpdateMQMessageBody mqMessageBody = new ProductPriceUpdateMQMessageBody();
                        mqMessageBody.setChannelId(channelId);
                        mqMessageBody.setCartId(boxedCartId);
                        mqMessageBody.setProdId(productModel.getProdId());
                        mqMessageBody.setSender(username);
                        cmsMqSenderService.sendMessage(mqMessageBody);
                    }
                });
    }

    private void addLogAndCallSyncPriceJob(String sku, String channelId, Integer cartId, String username, String comment) {

        CmsBtProductModel productModel = getProduct(sku, channelId);

        if (productModel == null) {
            $warn(String.format("价格变更历史 产品不存在 sku=%s, channelid=%s", sku, channelId));
            return;
        }
        CmsBtProductModel_Sku commonSku = productModel.getCommon().getSku(sku);
        if (commonSku == null) {
            $error(String.format("价格变更历史 产品common数据不存在 sku=%s, channelid=%s", sku, channelId));
            return;
        }
        if (cartId != null) {

            if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                return;

            CmsBtProductModel_Platform_Cart cartProduct = productModel.getPlatform(cartId);

            if (cartProduct == null) {
                $error(String.format("价格变更历史 产品platform数据不存在 sku=%s, channelid=%s, cartid=%d", sku, channelId, cartId));
                return;
            }
            addLogAndCallSyncPriceJob(sku, cartProduct, channelId, commonSku, productModel, username, comment);
            return;
        }

        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : productModel.getPlatforms().entrySet()) {

            String strCartId = entry.getKey();

            if (StringUtils.isEmpty(strCartId))
                continue;

            strCartId = strCartId.replace("P", "");

            int iCartId = Integer.valueOf(strCartId);

            if (iCartId < CmsConstants.ACTIVE_CARTID_MIN)
                continue;

            addLogAndCallSyncPriceJob(sku, entry.getValue(), channelId, commonSku, productModel, username, comment);
        }
    }

    private void addLogAndCallSyncPriceJob(String sku, CmsBtProductModel_Platform_Cart cartProduct, String channelId,
                                           CmsBtProductModel_Sku commonSku, CmsBtProductModel productModel, String username, String comment) {

        List<BaseMongoMap<String, Object>> skuList = cartProduct.getSkus();

        if (skuList == null || skuList.isEmpty())
            return;

        BaseMongoMap<String, Object> cartSku;

        if (skuList.size() == 1)
            cartSku = skuList.get(0);
        else
            cartSku = skuList.stream()
                    .filter(i -> sku.equals(i.getStringAttribute("skuCode")))
                    .findFirst()
                    .orElseGet(null);

        if (cartSku == null)
            return;

        Integer cartId = cartProduct.getCartId();

        CmsBtPriceLogModel logModel = priceLogDaoExt.selectLastOneBySkuOnCart(sku, cartId, channelId);

        if (logModel != null && compareAllPrice(commonSku, cartSku, logModel))
            return;

        CmsBtPriceLogModel newLog = new CmsBtPriceLogModel();
        newLog.setCode(productModel.getCommon().getFields().getCode());
        newLog.setProductId(productModel.getProdId().intValue());
        newLog.setSku(sku);
        newLog.setCartId(cartId);
        newLog.setChannelId(channelId);
        newLog.setClientMsrpPrice(tryGetPrice(commonSku.getClientMsrpPrice()));
        newLog.setClientNetPrice(tryGetPrice(commonSku.getClientNetPrice()));
        newLog.setClientRetailPrice(tryGetPrice(commonSku.getClientRetailPrice()));
        newLog.setMsrpPrice(cartSku.getDoubleAttribute("priceMsrp"));
        newLog.setRetailPrice(cartSku.getDoubleAttribute("priceRetail"));
        newLog.setSalePrice(cartSku.getDoubleAttribute("priceSale"));
        newLog.setComment(comment);
        Date now = new Date();
        newLog.setCreated(now);
        newLog.setModified(now);
        newLog.setCreater(username);
        newLog.setModifier(username);

        priceLogDao.insert(newLog);


        Double confirmPrice = cartSku.getDoubleAttribute(confPriceRetail.name());

        if (newLog.getRetailPrice() >= 0 && !newLog.getRetailPrice().equals(confirmPrice))
            priceConfirmLogService.addUnConfirmed(channelId, cartId, newLog.getCode(), cartSku, newLog.getCreater());
    }

    private boolean compareAllPrice(CmsBtPriceLogModel log1, CmsBtPriceLogModel log2) {
        return log1.getClientMsrpPrice().equals(log2.getClientMsrpPrice()) &&
                log1.getClientNetPrice().equals(log2.getClientNetPrice()) &&
                log1.getClientRetailPrice().equals(log2.getClientRetailPrice()) &&
                log1.getMsrpPrice().equals(log2.getMsrpPrice()) &&
                log1.getRetailPrice().equals(log2.getRetailPrice()) &&
                log1.getSalePrice().equals(log2.getSalePrice());
    }

    private boolean compareAllPrice(CmsBtProductModel_Sku commonSku, BaseMongoMap<String, Object> platformSku, CmsBtPriceLogModel logModel) {

        Double clientMsrpPrice = 0d, clientNetPrice = 0d, clientRetailPrice = 0d,
                msrpPrice = 0d, retailPrice = 0d, salePrice = 0d;

        Double logClientMsrpPrice, logClientNetPrice, logClientRetailPrice,
                logMsrpPrice, logRetailPrice, logSalePrice;

        if (commonSku != null) {
            clientMsrpPrice = tryGetPrice(commonSku.getClientMsrpPrice());
            clientNetPrice = tryGetPrice(commonSku.getClientNetPrice());
            clientRetailPrice = tryGetPrice(commonSku.getClientRetailPrice());
        }

        if (platformSku != null) {
            msrpPrice = platformSku.getDoubleAttribute("priceMsrp");
            retailPrice = platformSku.getDoubleAttribute("priceRetail");
            salePrice = platformSku.getDoubleAttribute("priceSale");
        }

        logClientMsrpPrice = tryGetPrice(logModel.getClientMsrpPrice());
        logClientNetPrice = tryGetPrice(logModel.getClientNetPrice());
        logClientRetailPrice = tryGetPrice(logModel.getClientRetailPrice());
        logMsrpPrice = tryGetPrice(logModel.getMsrpPrice());
        logRetailPrice = tryGetPrice(logModel.getRetailPrice());
        logSalePrice = tryGetPrice(logModel.getSalePrice());

        return clientMsrpPrice.equals(logClientMsrpPrice)
                && clientNetPrice.equals(logClientNetPrice)
                && clientRetailPrice.equals(logClientRetailPrice)
                && msrpPrice.equals(logMsrpPrice)
                && retailPrice.equals(logRetailPrice)
                && salePrice.equals(logSalePrice);
    }

    private Double tryGetPrice(Double fromPrice) {
        return fromPrice == null ? 0d : fromPrice;
    }

    private CmsBtProductModel getProduct(String sku, String channelId) {

        JongoQuery query = new JongoQuery();
        query.setQuery("{\"common.skus.skuCode\": #}");
        query.addParameters(sku);
        query.setProjectionExt("common", "platforms", "prodId");

        List<CmsBtProductModel> productModelList = productDao.select(query, channelId);

        if (productModelList.isEmpty())
            return null;

        return productModelList.get(0);
    }

    public int updateCmsBtPriceLogForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        return priceLogDaoExt.updateCmsBtPriceLogForMove(channelId, itemCodeOld, skuList, itemCodeNew, modifier);
    }
}
