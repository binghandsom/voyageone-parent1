package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.overstock.mp.mpc.externalclient.PaymentResourceClientImpl;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 高级检索批量修改商品originalTitleCn消息实体
 *
 * @Author rex.wu
 * @Create 2017-05-23 14:27
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_BATCH_UPDATE_PRODUCT_TITLE)
public class CmsBatchUpdateProductTitleMQMessageBody extends BaseMQMessageBody {

    /**
     * 待批量修改Title的产品code
     */
    private List<String> productCodes;
    /**
     * 要修改的Title值
     */
    private String title;
    /**
     * cover:覆盖 prefix:添加前缀 suffix：添加后缀
     */
    private String titlePlace;


    @Override
    public void check() throws MQMessageRuleException {

        if (StringUtils.isBlank(getSender())) {
            throw new BusinessException("批量修改商品标题消息发送人为空");
        }
        if (StringUtils.isBlank(getChannelId())) {
            throw new BusinessException("批量修改商品标题参数(channelId)为空");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new BusinessException("批量修改商品标题参数(产品Code)为空");
        }
        if (StringUtils.isBlank(title)) {
            throw new BusinessException("批量修改商品标题参数(title)为空");
        }
        if (StringUtils.isBlank(titlePlace)) {
            throw new BusinessException("批量修改商品标题参数(titlePlace)为空");
        }
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePlace() {
        return titlePlace;
    }

    public void setTitlePlace(String titlePlace) {
        this.titlePlace = titlePlace;
    }
}
