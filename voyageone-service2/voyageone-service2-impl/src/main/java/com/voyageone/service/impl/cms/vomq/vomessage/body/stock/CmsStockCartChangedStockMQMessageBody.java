package com.voyageone.service.impl.cms.vomq.vomessage.body.stock;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.bean.cms.stock.CartChangedStockBean;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @description 接收WMS推送过来的渠道库存
 * @author piao
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_STOCK_CART_CHANGED_STOCK)
public class CmsStockCartChangedStockMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {

    private List<CartChangedStockBean> cartChangedStocks;

    public List<CartChangedStockBean> getCartChangedStocks() {
        return cartChangedStocks;
    }

    public void setCartChangedStocks(List<CartChangedStockBean> cartChangedStocks) {
        this.cartChangedStocks = cartChangedStocks;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (CollectionUtils.isEmpty(cartChangedStocks)) {
            throw new MQMessageRuleException("接收WMS推送过来的渠道库存MQ发送异常, 参数stockList为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
