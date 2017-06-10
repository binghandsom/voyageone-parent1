package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author piao
 * @description 高级检索批量lock产品
 */

@VOMQQueue(value = CmsMqRoutingKey.EWMS_MQ_STOCK_SYNC_PLATFORM_STOCK)
public class EwmsStockSyncPlatformMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName {

    List<PlatformStock> platformStocks;

    public List<PlatformStock> getPlatformStocks() {
        return platformStocks;
    }

    public void setPlatformStocks(List<PlatformStock> platformStocks) {
        this.platformStocks = platformStocks;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数channelId为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }

    public static class PlatformStock{
        private String  channelId;
        private Integer cartId;
        private String sku;
        private String userName;             //备注

        public PlatformStock(String  channelId, Integer cartId, String sku, String userName){
            this.channelId = channelId;
            this.cartId = cartId;
            this.sku = sku;
            this.userName = userName;
        }
        public PlatformStock(){

        }
        public Integer getCartId() {
            return cartId;
        }

        public void setCartId(Integer cartId) {
            this.cartId = cartId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }
    }
}
