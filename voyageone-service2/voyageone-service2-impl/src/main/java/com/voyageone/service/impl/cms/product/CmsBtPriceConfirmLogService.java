package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.CmsBtPriceConfirmLogDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPriceConfirmLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 查询和记录价格确认历史
 * <p>
 * 当指导价变更时, 不会立即更新确认价格, 只有在确认时才会使用新的指导价覆盖确认价
 * <p>
 * 确认历史就是用来记录相关变更的信息。便于查询和追责
 * <p>
 * Created by jonas on 16/8/30.
 *
 * @author jonas
 * @version 2.5.0
 * @since 2.5.0
 */
@Service
public class CmsBtPriceConfirmLogService extends BaseService {

    private final static int STATUS_CONFIRMED = 1;

    private final static int STATUS_UNCONFIRMED = 0;

    private final CmsBtPriceConfirmLogDao priceConfirmLogDao;

    @Autowired
    public CmsBtPriceConfirmLogService(CmsBtPriceConfirmLogDao priceConfirmLogDao) {
        this.priceConfirmLogDao = priceConfirmLogDao;
    }

    public void addConfirmed(String channelId, String code, CmsBtProductModel_Platform_Cart platformCart, String username) {
        int cartId = platformCart.getCartId();
        for (BaseMongoMap<String, Object> skuModel: platformCart.getSkus())
            addSkuWithStatus(channelId, cartId, code, skuModel, STATUS_CONFIRMED, username);
    }

    public void addUnConfirmed(String channelId, int cartId, String code, BaseMongoMap<String, Object> skuModel, String username) {
        addSkuWithStatus(channelId, cartId, code, skuModel, STATUS_UNCONFIRMED, username);
    }

    private void addSkuWithStatus(String channelId, int cartId, String code, BaseMongoMap<String, Object> skuModel, int status, String username) {

        CmsBtPriceConfirmLogModel priceConfirmLogModel = new CmsBtPriceConfirmLogModel();

        priceConfirmLogModel.setChannelId(channelId);
        priceConfirmLogModel.setCartId(cartId);
        priceConfirmLogModel.setCode(code);

        setSkuInfo(priceConfirmLogModel, skuModel);

        priceConfirmLogModel.setStatus(status);

        addByUser(priceConfirmLogModel, username);
    }

    private boolean addByUser(CmsBtPriceConfirmLogModel priceConfirmLogModel, String username) {
        priceConfirmLogModel.setCreater(username);
        priceConfirmLogModel.setModifier(username);
        return add(priceConfirmLogModel);
    }

    private boolean add(CmsBtPriceConfirmLogModel priceConfirmLogModel) {
        Date now = DateTimeUtil.getDate();
        priceConfirmLogModel.setCreated(now);
        priceConfirmLogModel.setModified(now);
        return priceConfirmLogDao.insert(priceConfirmLogModel) > 0;
    }

    private void setSkuInfo(CmsBtPriceConfirmLogModel priceConfirmLogModel, BaseMongoMap<String, Object> sku) {
        priceConfirmLogModel.setSkuCode(sku.getStringAttribute(Platform_SKU_COM.skuCode.name()));
        priceConfirmLogModel.setFloatingRate(sku.getStringAttribute(Platform_SKU_COM.priceChgFlg.name()));
        priceConfirmLogModel.setCurrentRetailPrice(sku.getDoubleAttribute(Platform_SKU_COM.priceRetail.name()));
        priceConfirmLogModel.setCurrentConfirmPrice(sku.getDoubleAttribute(Platform_SKU_COM.confPriceRetail.name()));
    }
}
