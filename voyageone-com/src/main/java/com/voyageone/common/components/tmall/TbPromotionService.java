package com.voyageone.common.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.domain.TipItemPromDTO;
import com.taobao.api.request.TmallProductMatchSchemaGetRequest;
import com.taobao.api.request.TmallPromotionTipItemAddRequest;
import com.taobao.api.request.TmallPromotionTipItemModifyRequest;
import com.taobao.api.request.TmallPromotionTipItemRemoveRequest;
import com.taobao.api.response.TmallProductMatchSchemaGetResponse;
import com.taobao.api.response.TmallPromotionTipItemAddResponse;
import com.taobao.api.response.TmallPromotionTipItemModifyResponse;
import com.taobao.api.response.TmallPromotionTipItemRemoveResponse;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/10/29.
 */
@Component
public class TbPromotionService extends TbBase {
    private static Log logger = LogFactory.getLog(TbProductService.class);

    public TmallPromotionTipItemAddResponse addPromotion(ShopBean shopBean,TipItemPromDTO ItemProm) throws ApiException {

        logger.info("天猫特价宝添加活动商品 " + ItemProm.getItemId());

        TmallPromotionTipItemAddRequest req = new TmallPromotionTipItemAddRequest();

        req.setItemProm(ItemProm);

        TmallPromotionTipItemAddResponse response = reqTaobaoApi(shopBean, req);
        if (response.getErrorCode() != null)
        {
            logger.error(response.getSubMsg());
        }

        return response;
    }

    public TmallPromotionTipItemModifyResponse updatePromotion(ShopBean shopBean,TipItemPromDTO ItemProm) throws ApiException {
        logger.info("天猫特价宝更新活动商品 " + ItemProm.getItemId());

        TmallPromotionTipItemModifyRequest req = new TmallPromotionTipItemModifyRequest();

        req.setItemProm(ItemProm);

        TmallPromotionTipItemModifyResponse response = reqTaobaoApi(shopBean, req);
        if (response.getErrorCode() != null)
        {
            logger.error(response.getSubMsg());
        }
        return response;
    }

    public TmallPromotionTipItemRemoveResponse removePromotion(ShopBean shopBean, int num_iid, Long campaign_id) throws ApiException {
        logger.info("天猫特价宝删除活动商品 " + num_iid);
        TmallPromotionTipItemRemoveRequest req = new TmallPromotionTipItemRemoveRequest();
        req.setItemId((long) num_iid);
        req.setCampaignId(campaign_id);
        TmallPromotionTipItemRemoveResponse response = reqTaobaoApi(shopBean, req);
        if (response.getErrorCode() != null)
        {
            logger.error(response.getSubMsg());
        }
        return response;
    }
}
