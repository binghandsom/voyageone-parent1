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

    public void addConfirmed(String channelId, CmsBtProductModel_Platform_Cart platformCart, String username) {
        for (BaseMongoMap<String, Object> sku: platformCart.getSkus()) {
            String skuCode = sku.getStringAttribute(Platform_SKU_COM.skuCode.name());
            addConfirmed(skuCode, sku.getDoubleAttribute(Platform_SKU_COM.priceRetail.name()), username);
        }
    }

    public boolean addConfirmed(String skuCode, Double currentRetailPrice, String username) {
        return add(skuCode, STATUS_CONFIRMED, "0%", currentRetailPrice, currentRetailPrice, username);
    }

    public boolean addUnConfirmed(String skuCode, String floatingRate, Double currentRetailPrice, Double currentConfirmPrice, String username) {
        return add(skuCode, STATUS_UNCONFIRMED, floatingRate, currentRetailPrice, currentConfirmPrice, username);
    }

    private boolean add(String skuCode, int status, String floatingRate, Double currentRetailPrice, Double currentConfirmPrice, String username) {
        Date now = DateTimeUtil.getDate();
        CmsBtPriceConfirmLogModel newRecord = new CmsBtPriceConfirmLogModel();
        newRecord.setSkuCode(skuCode);
        newRecord.setStatus(status);
        newRecord.setFloatingRate(floatingRate);
        newRecord.setCurrentRetailPrice(currentRetailPrice);
        newRecord.setCurrentConfirmPrice(currentConfirmPrice);
        newRecord.setCreater(username);
        newRecord.setCreated(now);
        newRecord.setModifier(username);
        newRecord.setModified(now);
        return priceConfirmLogDao.insert(newRecord) > 0;
    }
}
