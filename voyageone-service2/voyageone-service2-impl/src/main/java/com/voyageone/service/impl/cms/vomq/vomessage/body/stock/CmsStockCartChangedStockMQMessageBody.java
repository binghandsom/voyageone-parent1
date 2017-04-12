package com.voyageone.service.impl.cms.vomq.vomessage.body.stock;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.bean.cms.stock.CartChangedStockBean;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @description 接收WMS推送过来的渠道库存
 * @author piao
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_STOCK_CART_CHANGED_STOCK)
public class CmsStockCartChangedStockMQMessageBody extends BaseMQMessageBody {

    private List<CartChangedStockBean> stockList;

    public List<CartChangedStockBean> getStockList() {
        return stockList;
    }

    public void setStockList(List<CartChangedStockBean> stockList) {
        this.stockList = stockList;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (CollectionUtils.isEmpty(stockList)) {
            throw new MQMessageRuleException("接收WMS推送过来的渠道库存MQ发送异常, 参数stockList为空.");
        }
    }
}
