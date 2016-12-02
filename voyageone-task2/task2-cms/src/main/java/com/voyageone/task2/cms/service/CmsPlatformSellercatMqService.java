package com.voyageone.task2.cms.service;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 取得店铺内分类，回写数据库
 * 注意：直接insert，所以只有第一次开店，在平台上创建好店铺内分类的情况下，才适用此MQ
 *
 * @author morse on 2016/12/01
 * @version 2.6.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformSellercatJob)
public class CmsPlatformSellercatMqService  extends BaseMQCmsService {

    @Autowired
    private SellerCatService sellerCatService;

    /**
     * 入口
     *    输入参数:
     *        channelId: channel id (目前只支持京东系和天猫系)
     *        cartId: cart id
     *
     * @param messageMap Mq消息Map
     * @throws Exception
     */
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        String channelId = (String) messageMap.get("channelId");
        String cartId = (String) messageMap.get("cartId");

        try {
            doMain(channelId, Integer.valueOf(cartId));
        } catch (Exception e) {
            if (StringUtils.isEmpty(e.getMessage())) {
                $error("异常发生!");
                e.printStackTrace();
            } else {
                $error("异常发生!" + e.getMessage());
            }
        }
        $info("success end");
    }

    private void doMain(String channelId, int cartId) throws Exception {
        $info("取得店铺内开始!");
        List<CmsBtSellerCatModel> root = sellerCatService.refreshSellerCat(channelId, cartId, getTaskName());
        $info("开始插入mongoDB!根目录数为" + root.size());
        sellerCatService.save(root);
        $info("成功插入mongoDB!");
    }

}
