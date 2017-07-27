package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.TransferUsCategoryModel;

import java.util.List;

/**
 * Created by Charis on 2017/7/25.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_CATEGORY_TRANSFER)
public class CmsCategoryTransferToUsMQMessageBody extends BaseMQMessageBody {

    private TransferUsCategoryModel categoryModel;

    public TransferUsCategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(TransferUsCategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    @Override
    public void check() throws MQMessageRuleException {

    }
}
