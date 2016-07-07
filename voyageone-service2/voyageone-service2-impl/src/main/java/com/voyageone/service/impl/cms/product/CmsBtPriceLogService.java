package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtPriceLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPriceLogDaoExt;
import com.voyageone.service.impl.BaseService;
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

    @Autowired
    private CmsBtPriceLogDao priceLogDao;

    @Autowired
    private CmsBtPriceLogDaoExt priceLogDaoExt;

    @Autowired
    private CmsBtProductDao productDao;

    public List<CmsBtPriceLogModel> getList(String sku, String code, String cartId, String channelId) {
        return priceLogDaoExt.selectListBySkuOnCart(sku, code, cartId, channelId);
    }

    public List<CmsBtPriceLogModel> getPage(String sku, String code, String cartId, String channelId, int offset, int limit) {
        return priceLogDaoExt.selectPageBySkuOnCart(sku, code, cartId, channelId, offset, limit);
    }

    public int getCount(String sku, String code, String cartId, String channelId) {
        return priceLogDaoExt.selectCountBySkuOnCart(sku, code, cartId, channelId);
    }

    public void forceLog(CmsBtPriceLogModel logModel) {
        priceLogDao.insert(logModel);
    }

    /**
     * 对指定的 sku 进行价格变动检查
     *
     * @param skuList   将要检查的 sku 列表
     * @param channelId 所属渠道
     * @param username  变动人 / 检查人
     * @param comment   变动备注 / 检查备注
     */
    public void logAll(List<String> skuList, String channelId, String username, String comment) {
        logAll(skuList, channelId, null, username, comment);
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
    public void logAll(List<String> skuList, String channelId, Integer cartId, String username, String comment) {
        for (String sku : skuList)
            log(sku, channelId, cartId, username, comment);
    }

    private void log(String sku, String channelId, Integer cartId, String username, String comment) {

        CmsBtProductModel productModel = getProduct(sku, channelId);

        if (productModel == null)
            return;

        CmsBtProductModel_Sku commonSku = productModel.getCommon().getSku(sku);

        if (commonSku == null)
            return;

        if (cartId != null) {

            if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                return;

            CmsBtProductModel_Platform_Cart cartProduct = productModel.getPlatform(cartId);

            if (cartProduct == null)
                return;

            log(sku, cartProduct, channelId, commonSku, productModel, username, comment);

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

            log(sku, entry.getValue(), channelId, commonSku, productModel, username, comment);
        }
    }

    private void log(String sku, CmsBtProductModel_Platform_Cart cartProduct, String channelId, CmsBtProductModel_Sku commonSku, CmsBtProductModel productModel, String username, String comment) {

        BaseMongoMap<String, Object> cartSku = cartProduct.getSkus().stream().filter(i -> i.getStringAttribute("skuCode").equals(sku)).findFirst().orElseGet(null);

        if (cartSku == null)
            return;

        Integer cartId = cartProduct.getCartId();

        CmsBtPriceLogModel logModel = priceLogDaoExt.selectLastOneBySkuOnCart(sku, cartId, channelId);

        if (logModel != null && compareAllPrice(commonSku, cartSku, logModel))
            return;

        CmsBtPriceLogModel newLog = makeLog(sku, cartId, channelId, productModel, commonSku, cartSku, username, comment);

        priceLogDao.insert(newLog);
    }

    private CmsBtPriceLogModel makeLog(String sku, Integer cartId, String channelId, CmsBtProductModel productModel, CmsBtProductModel_Sku commonSku, BaseMongoMap<String, Object> platformSku, String username, String comment) {

        CmsBtPriceLogModel logModel = new CmsBtPriceLogModel();

        logModel.setCode(productModel.getCommon().getFields().getCode());
        logModel.setProductId(productModel.getProdId().intValue());
        logModel.setSku(sku);
        logModel.setCartId(cartId);
        logModel.setChannelId(channelId);
        logModel.setClientMsrpPrice(String.valueOf(commonSku.getClientMsrpPrice()));
        logModel.setClientNetPrice(String.valueOf(commonSku.getClientNetPrice()));
        logModel.setClientRetailPrice(String.valueOf(commonSku.getClientRetailPrice()));
        logModel.setMsrpPrice(platformSku.getStringAttribute("priceMsrp"));
        logModel.setRetailPrice(platformSku.getStringAttribute("priceRetail"));
        logModel.setSalePrice(platformSku.getStringAttribute("priceSale"));
        logModel.setComment(comment);
        Date now = new Date();
        logModel.setCreated(now);
        logModel.setModified(now);
        logModel.setCreater(username);
        logModel.setModifier(username);

        return logModel;
    }

    private boolean compareAllPrice(CmsBtProductModel_Sku commonSku, BaseMongoMap<String, Object> platformSku, CmsBtPriceLogModel logModel) {
        return commonSku.getClientMsrpPrice().equals(Double.valueOf(logModel.getClientMsrpPrice()))
                && commonSku.getClientNetPrice().equals(Double.valueOf(logModel.getClientNetPrice()))
                && commonSku.getClientRetailPrice().equals(Double.valueOf(logModel.getClientRetailPrice()))
                && new Double(platformSku.getDoubleAttribute("priceMsrp")).equals(Double.valueOf(logModel.getMsrpPrice()))
                && new Double(platformSku.getDoubleAttribute("priceRetail")).equals(Double.valueOf(logModel.getRetailPrice()))
                && new Double(platformSku.getDoubleAttribute("priceSale")).equals(Double.valueOf(logModel.getSalePrice()));
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
