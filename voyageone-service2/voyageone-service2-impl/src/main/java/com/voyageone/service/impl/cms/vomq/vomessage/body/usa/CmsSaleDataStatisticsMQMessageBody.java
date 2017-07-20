package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Created by james on 2017/7/20.
 *
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_SALE_DATA_STATISTICS)
public class CmsSaleDataStatisticsMQMessageBody  extends BaseMQMessageBody {

    private Integer cartId;
    private String startDate;
    private String endDate;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(cartId == null || StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)){
            throw new MQMessageRuleException("参数不正确");
        }
    }
}
