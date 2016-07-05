package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.cms.CmsBtPriceLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPriceLogDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public void logAll(List<String> skuList, String channelId, String username, String comment) {
        logAll(skuList, channelId, null, username, comment);
    }

    public void logAll(List<String> skuList, String channelId, String cartId, String username, String comment) {
        for (String sku : skuList)
            log(sku, channelId, cartId, username, comment);
    }

    private void log(String sku, String channelId, String cartId, String username, String comment) {

        CmsBtProductModel productModel = getProductOnlyWithPrice(sku, cartId, channelId);

        if (productModel == null)
            return;

        CmsBtProductModel_Sku commonSku = productModel.getCommon().getSku(sku);

        if (commonSku == null)
            throw new BusinessException("通用属性下没有指定的 SKU");

        List<BaseMongoMap<String, Object>> platformSkus = productModel.getPlatform(Integer.valueOf(cartId)).getSkus();

        if (platformSkus == null || platformSkus.isEmpty())
            throw new BusinessException("平台属性下没有 SKU 数据");

        BaseMongoMap<String, Object> platformSku = platformSkus.stream().filter(i -> i.getStringAttribute("skuCode").equals(sku)).findFirst().orElseGet(null);

        if (platformSku == null)
            throw new BusinessException("指定的平台属性下没有指定的 SKU");

        CmsBtPriceLogModel logModel = priceLogDaoExt.selectLastOneBySkuOnCart(sku, cartId, channelId);

        if (logModel != null && compareAllPrice(commonSku, platformSku, logModel))
            return;

        CmsBtPriceLogModel newLog = makeLog(sku, cartId, channelId, productModel, commonSku, platformSku, username);

        priceLogDao.insert(newLog);
    }

    private CmsBtPriceLogModel makeLog(String sku, String cartId, String channelId, CmsBtProductModel productModel, CmsBtProductModel_Sku commonSku, BaseMongoMap<String, Object> platformSku, String username) {

        CmsBtPriceLogModel logModel = new CmsBtPriceLogModel();

        logModel.setCode(productModel.getCommon().getFields().getCode());
        logModel.setProductId(productModel.getProdId().intValue());
        logModel.setSku(sku);
        logModel.setCartId(Integer.valueOf(cartId));
        logModel.setChannelId(channelId);
        logModel.setClientMsrpPrice(String.valueOf(commonSku.getClientMsrpPrice()));
        logModel.setClientNetPrice(String.valueOf(commonSku.getClientNetPrice()));
        logModel.setClientRetailPrice(String.valueOf(commonSku.getClientRetailPrice()));
        logModel.setMsrpPrice(platformSku.getStringAttribute("priceMsrp"));
        logModel.setRetailPrice(platformSku.getStringAttribute("priceRetail"));
        logModel.setSalePrice(platformSku.getStringAttribute("priceSale"));
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

    private CmsBtProductModel getProductOnlyWithPrice(String sku, String cartId, String channelId) {

        boolean hasCart = !StringUtils.isEmpty(cartId);
        JomgoQuery query = new JomgoQuery();

        query.addQuery("'skus.skuCode': #");
        if (hasCart)
            query.addQuery("'platforms.P" + cartId + "': {$exists:true}");
        query.addParameters(sku);

        List<CmsBtProductModel> productModelList = productDao.select(query, channelId);

        if (productModelList.isEmpty())
            return null;

        return productModelList.get(0);
    }
}
