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
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.service.MqSenderService;
import com.voyageone.components.tmall.TbBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/10/29.
 */
@Component
public class TbPromotionService extends TbBase {

    @Value("${cms2.components.tmall.services.promotion.async:false}")
    private boolean async;

    private final MqSenderService mqSenderService;

    public TbPromotionService(MqSenderService mqSenderService) {
        this.mqSenderService = mqSenderService;
    }

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

        TmallPromotionTipItemModifyResponse response;

        if (!async) {
            response = reqTaobaoApi(shopBean, req);

            if (response.getErrorCode() != null) {
                String msg = (response.getSubMsg() == null ? "" : response.getSubMsg()) + (response.getMsg() == null ? "" : response.getMsg());
                logger.error(msg);
            }
        } else {

            @VOMQQueue("voyageone_cms_jushita_mq_tjb_promotion_tip_Item_modify_queue")
            class TmallPromotionTipItemModifyMessage extends SimplePromotionMessage {

                private TmallPromotionTipItemModifyRequest tmallPromotionTipItemModifyRequest = req;

                private TmallPromotionTipItemModifyMessage() {
                    super(shopBean);
                }

                public TmallPromotionTipItemModifyRequest getTmallPromotionTipItemModifyRequest() {
                    return tmallPromotionTipItemModifyRequest;
                }
            }

            mqSenderService.sendMessage(new TmallPromotionTipItemModifyMessage());

            response = new TmallPromotionTipItemModifyResponse();
            response.setModifyRst(true);
        }

        return response;
    }

    public TmallPromotionTipItemRemoveResponse removePromotion(ShopBean shopBean, Long num_iid, Long campaign_id) throws ApiException {
        logger.info("天猫特价宝删除活动商品 " + num_iid);
        TmallPromotionTipItemRemoveRequest req = new TmallPromotionTipItemRemoveRequest();
        req.setItemId(num_iid);
        req.setCampaignId(campaign_id);

        TmallPromotionTipItemRemoveResponse response;

        if (!async) {
            response = reqTaobaoApi(shopBean, req);
            if (response.getErrorCode() != null) {
                logger.error(response.getSubMsg());
            }
        } else {
            @VOMQQueue("voyageone_cms_jushita_mq_tjb_promotion_tip_Item_remove_queue")
            class TmallPromotionTipItemRemoveMessage extends SimplePromotionMessage {

                private TmallPromotionTipItemRemoveRequest tmallPromotionTipItemModifyRequest = req;

                private TmallPromotionTipItemRemoveMessage() {
                    super(shopBean);
                }

                public TmallPromotionTipItemRemoveRequest getTmallPromotionTipItemModifyRequest() {
                    return tmallPromotionTipItemModifyRequest;
                }
            }

            mqSenderService.sendMessage(new TmallPromotionTipItemRemoveMessage());

            response = new TmallPromotionTipItemRemoveResponse();
            response.setRemoveRst(true);
        }

        return response;
    }

    abstract class SimplePromotionMessage extends BaseMQMessageBody {
        private final int cartId;

        SimplePromotionMessage(ShopBean shopBean) {
            this.cartId = Integer.valueOf(shopBean.getCart_id());
            setChannelId(shopBean.getOrder_channel_id());
        }

        public int getCartId() {
            return cartId;
        }

        @Override
        public void check() throws MQMessageRuleException {
        }
    }
}
