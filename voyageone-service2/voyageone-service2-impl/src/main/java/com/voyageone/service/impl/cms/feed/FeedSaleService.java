package com.voyageone.service.impl.cms.feed;
import com.voyageone.service.dao.wms.WmsBtItemDetailsDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/9/21.
 */
@Service
public class FeedSaleService extends BaseService {
    @Autowired
    private FeedInfoService feedInfoService;
@Autowired
    private WmsBtItemDetailsDao  wmsBtItemDetailsDao;
    @Autowired
    private ProductService  productService;
    public void notSale( String channelId, String clientSku) {
      //  boolean isSale=false;
//        clientSku
//                feed的sku库存变成0
//        的sku.isSale=0
//        product库存扣减sku库存
//
//                通过feed的sku找
//        product的common.skus.isSale = 0
//
//        clientSku
//        wms_bt_item_details 对应的channel和sku,is_sale =0
        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByClientSku(channelId, clientSku);
        CmsBtFeedInfoModel_Sku cmsBtFeedInfoModel_sku = cmsBtFeedInfoModel.getSkus().stream().filter(w -> clientSku.equals(w.getClientSku())).findFirst().get();
        if (cmsBtFeedInfoModel_sku != null) {
            cmsBtFeedInfoModel.setQty(cmsBtFeedInfoModel.getQty() - cmsBtFeedInfoModel_sku.getQty());
            cmsBtFeedInfoModel_sku.setQty(0);
            cmsBtFeedInfoModel_sku.setIsSale(0);
        }

        CmsBtProductModel cmsBtProductModel = productService.getProductBySku(channelId, cmsBtFeedInfoModel_sku.getSku());
        if(cmsBtProductModel!=null) {
            CmsBtProductModel_Sku cmsBtProductModel_sku = cmsBtProductModel.getCommon().getSku(cmsBtFeedInfoModel_sku.getSku());
            cmsBtProductModel_sku.setIsSale(0);
        }
        feedInfoService.updateFeedInfo(cmsBtFeedInfoModel);
        if(cmsBtProductModel!=null) {
            productService.updateProductCommon(channelId, cmsBtProductModel.getProdId(), cmsBtProductModel.getCommon(), "", false);
            wmsBtItemDetailsDao.update(channelId, clientSku, 0);
        }
    }

    public void sale(String channelId, String clientSku, int stockQty) {
        //boolean isSale=true;
        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByClientSku(channelId, clientSku);
        CmsBtFeedInfoModel_Sku cmsBtFeedInfoModel_sku = cmsBtFeedInfoModel.getSkus().stream().filter(w -> clientSku.equals(w.getClientSku())).findFirst().get();
        if (cmsBtFeedInfoModel_sku != null) {
            cmsBtFeedInfoModel.setQty(cmsBtFeedInfoModel.getQty()+stockQty);
            cmsBtFeedInfoModel_sku.setQty(stockQty);
            cmsBtFeedInfoModel_sku.setIsSale(1);
        }
        CmsBtProductModel cmsBtProductModel = productService.getProductBySku(channelId, cmsBtFeedInfoModel_sku.getSku());
        if(cmsBtProductModel!=null) {
            CmsBtProductModel_Sku cmsBtProductModel_sku = cmsBtProductModel.getCommon().getSku(cmsBtFeedInfoModel_sku.getSku());
            cmsBtProductModel_sku.setIsSale(1);
        }

        feedInfoService.updateFeedInfo(cmsBtFeedInfoModel);
        if(cmsBtProductModel!=null) {
            productService.updateProductCommon(channelId, cmsBtProductModel.getProdId(), cmsBtProductModel.getCommon(), "", false);
            wmsBtItemDetailsDao.update(channelId,clientSku,1);
        }

    }
}
