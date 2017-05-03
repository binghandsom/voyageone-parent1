package com.voyageone.service.impl.cms.feed;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.wms.WmsBtItemDetailsDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 2016/9/21.
 */
@Service
public class FeedSaleService extends BaseService {

    @Autowired
    CmsBtProductDao cmsBtProductDao;
    @Autowired
    private WmsBtItemDetailsDao wmsBtItemDetailsDao;
    @Autowired
    private ProductService  productService;

    /**
     * 设定product.sku不再售卖
     *
     * @param channelId 店铺Id
     * @param clientSku 品牌方sku
     * @param skuCode   voSku
     * @param isSale    1:售卖, 0:不卖
     */
    public void setSaleOrNotSale(String channelId, String clientSku, String skuCode, Integer isSale) {

        CmsBtProductModel cmsBtProductModel = productService.getProductBySku(channelId, skuCode);
        if(cmsBtProductModel!=null) {
            CmsBtProductModel_Sku cmsBtProductModel_sku = cmsBtProductModel.getCommon().getSku(skuCode);
            cmsBtProductModel_sku.setIsSale(isSale);
        }

        if(cmsBtProductModel!=null) {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.skus.skuCode", skuCode);
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("common.skus.$.isSale", isSale);
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, "FeedSaleService", "$set");
            wmsBtItemDetailsDao.update(channelId, clientSku, isSale);
        }
    }
}
