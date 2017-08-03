package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by dell on 2017/7/31.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_PRODUCT_SALES_UPDATE)
public class CmsUsaProductSalesMQMessageBody extends BaseMQMessageBody {

    private List<Param> items;

    public List<Param> getItems() {
        return items;
    }

    public void setItems(List<Param> items) {
        this.items = items;
    }

    public static class Param {
        private Integer cartId;
        private Long orderDate;
        private String sku;
        private Integer qty;
        //1:下单 / 0:取消
        private Integer status;

        public Integer getCartId() {
            return cartId;
        }

        public void setCartId(Integer cartId) {
            this.cartId = cartId;
        }

        public Long getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(Long orderDate) {
            this.orderDate = orderDate;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("参数channelId为空.");
        }
        if (ListUtils.notNull(items)) {
            throw new MQMessageRuleException(" items为空.");
        }
    }
}
