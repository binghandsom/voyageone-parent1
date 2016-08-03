package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtPriceLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPriceLogDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 不干别的就记录商品价格变动
 * Created by jonasvlag on 16/7/4.
 *
 * @version 2.2.0
 * @since 2.2.0
 */
@Service
public class CmsBtPriceLogService extends BaseService {

    private final CmsBtPriceLogDao priceLogDao;
    private final CmsBtPriceLogDaoExt priceLogDaoExt;
    private final CmsBtProductDao productDao;
    private final MqSender sender;

    @Autowired
    public CmsBtPriceLogService(CmsBtPriceLogDao priceLogDao, CmsBtPriceLogDaoExt priceLogDaoExt, CmsBtProductDao productDao, MqSender sender) {
        this.priceLogDao = priceLogDao;
        this.priceLogDaoExt = priceLogDaoExt;
        this.productDao = productDao;
        this.sender = sender;
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
        int rs = priceLogDaoExt.insertCmsBtPriceLogList(paramList);

        // 向Mq发送消息同步sku,code,group价格范围
        for (CmsBtPriceLogModel newLog : paramList) {
            sender.sendMessage(MqRoutingKey.CMS_TASK_ProdcutPriceUpdateJob, JacksonUtil.jsonToMap(JacksonUtil.bean2JsonNotNull(newLog)));
        }
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
    public void addLogForSkuListAndCallSyncPriceJob(List<String> skuList, String channelId, Integer cartId, String username, String comment) {
        for (String sku : skuList)
            addLogAndCallSyncPriceJob(sku, channelId, cartId, username, comment);
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

    private void addLogAndCallSyncPriceJob(String sku, CmsBtProductModel_Platform_Cart cartProduct, String channelId, CmsBtProductModel_Sku commonSku, CmsBtProductModel productModel, String username, String comment) {

        BaseMongoMap<String, Object> cartSku = cartProduct.getSkus().stream().filter(i -> i.getStringAttribute("skuCode").equals(sku)).findFirst().orElseGet(null);

        if (cartSku == null)
            return;

        Integer cartId = cartProduct.getCartId();

        CmsBtPriceLogModel logModel = priceLogDaoExt.selectLastOneBySkuOnCart(sku, cartId, channelId);

        if (logModel != null && compareAllPrice(commonSku, cartSku, logModel))
            return;

        CmsBtPriceLogModel newLog = makeLog(sku, cartId, channelId, productModel, commonSku, cartSku, username, comment);
        int rs = priceLogDao.insert(newLog);
        $debug(String.format("价格变更历史 结果=%d sku=%s, channelid=%s, cartid=%d", rs, sku, channelId, cartId));

        // 向Mq发送消息同步sku,code,group价格范围
        sender.sendMessage(MqRoutingKey.CMS_TASK_ProdcutPriceUpdateJob, JacksonUtil.jsonToMap(JacksonUtil.bean2Json(newLog)));
    }

    private CmsBtPriceLogModel makeLog(String sku, Integer cartId, String channelId, CmsBtProductModel productModel, CmsBtProductModel_Sku commonSku, BaseMongoMap<String, Object> platformSku, String username, String comment) {

        CmsBtPriceLogModel logModel = new CmsBtPriceLogModel();

        logModel.setCode(productModel.getCommon().getFields().getCode());
        logModel.setProductId(productModel.getProdId().intValue());
        logModel.setSku(sku);
        logModel.setCartId(cartId);
        logModel.setChannelId(channelId);
        logModel.setClientMsrpPrice(tryGetPrice(commonSku.getClientMsrpPrice()));
        logModel.setClientNetPrice(tryGetPrice(commonSku.getClientNetPrice()));
        logModel.setClientRetailPrice(tryGetPrice(commonSku.getClientRetailPrice()));
        logModel.setMsrpPrice(platformSku.getDoubleAttribute("priceMsrp"));
        logModel.setRetailPrice(platformSku.getDoubleAttribute("priceRetail"));
        logModel.setSalePrice(platformSku.getDoubleAttribute("priceSale"));
        logModel.setComment(comment);
        Date now = new Date();
        logModel.setCreated(now);
        logModel.setModified(now);
        logModel.setCreater(username);
        logModel.setModifier(username);

        return logModel;
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

        JomgoQuery query = new JomgoQuery();
        query.setQuery("{\"common.skus.skuCode\": #}");
        query.addParameters(sku);
        query.setProjectionExt("common", "platforms", "prodId");

        List<CmsBtProductModel> productModelList = productDao.select(query, channelId);

        if (productModelList.isEmpty())
            return null;

        return productModelList.get(0);
    }
}
