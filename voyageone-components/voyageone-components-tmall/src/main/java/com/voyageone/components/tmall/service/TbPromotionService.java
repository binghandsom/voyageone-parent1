package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.TipItemPromDTO;
import com.taobao.api.request.TmallPromotionTipItemAddRequest;
import com.taobao.api.request.TmallPromotionTipItemModifyRequest;
import com.taobao.api.request.TmallPromotionTipItemRemoveRequest;
import com.taobao.api.response.TmallPromotionTipItemAddResponse;
import com.taobao.api.response.TmallPromotionTipItemModifyResponse;
import com.taobao.api.response.TmallPromotionTipItemRemoveResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/10/29.
 */
@Component
public class TbPromotionService extends TbBase {

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

    public TmallPromotionTipItemRemoveResponse removePromotion(ShopBean shopBean, Long num_iid, Long campaign_id) throws ApiException {
        logger.info("天猫特价宝删除活动商品 " + num_iid);
        TmallPromotionTipItemRemoveRequest req = new TmallPromotionTipItemRemoveRequest();
        req.setItemId(num_iid);
        req.setCampaignId(campaign_id);
        TmallPromotionTipItemRemoveResponse response = reqTaobaoApi(shopBean, req);
        if (response.getErrorCode() != null)
        {
            logger.error(response.getSubMsg());
        }
        return response;
    }
}
