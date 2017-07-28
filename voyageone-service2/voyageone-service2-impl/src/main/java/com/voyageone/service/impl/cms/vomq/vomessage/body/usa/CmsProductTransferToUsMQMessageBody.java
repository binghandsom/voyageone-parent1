package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.TransferUsProductModel;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;


/**
 * Created by Charis on 2017/7/19.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_PRODUCT_TRANSFER)
public class CmsProductTransferToUsMQMessageBody extends BaseMQMessageBody {

    private List<TransferUsProductModel> productModels;

    public List<TransferUsProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<TransferUsProductModel> productModels) {
        this.productModels = productModels;
    }


    @Override
    public void check() throws MQMessageRuleException {
        if (CollectionUtils.isEmpty(productModels)) {
            throw new MQMessageRuleException("VOUsSynchronizeProductQueue MQ发送异常, 参数productModels为空.");
        }
    }

}
