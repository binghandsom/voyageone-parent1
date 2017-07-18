package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by dell on 2017/7/18.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_PRODUCT_UPDATE_TAGS)
public class CmsBtProductUpdateTagsMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {

    List<String> prodCodeList;

    List<String> tagPathList;

    //List<String> orgDispTagList;

    public List<String> getProdCodeList() {
        return prodCodeList;
    }

    public void setProdCodeList(List<String> prodCodeList) {
        this.prodCodeList = prodCodeList;
    }

    public List<String> getTagPathList() {
        return tagPathList;
    }

    public void setTagPathList(List<String> tagPathList) {
        this.tagPathList = tagPathList;
    }


    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isEmpty(super.getChannelId())) {
            throw new MQMessageRuleException("高级搜索-批量设置自由标签MQ发送异常, 参数channelId为空.");
        }
        if ( CollectionUtils.isEmpty(prodCodeList)) {
            throw new MQMessageRuleException("高级搜索-批量设置自由标签MQ发送异常, 参数isSelAll为false并且prodCodeList为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("高级检索-批量设置自由标签MQ发送异常, 发送者为空.");
        }
    }

    @Override
    public String getSubBeanName() {
            return getChannelId();
    }
}
