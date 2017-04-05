package com.voyageone.service.impl.cms.vomq.vomessage.body;
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
 * CmsProductFreeTagsUpdateMQMessageBody    高级搜索-设置自由标签
 *
 * @author peitao 2017/01/12
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_FREE_TAGS)
public class CmsProductFreeTagsUpdateMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName {

    //channelId
    String channelId;
    //是否全量
    boolean isSelAll;
    //product code List
    List<String> prodCodeList;
    //高级搜索查询条件
    CmsSearchInfoBean2 searchValue;

    List<String> tagPathList;

    List<String> orgDispTagList;

    public List<String> getOrgDispTagList() {
        return orgDispTagList;
    }

    public void setOrgDispTagList(List<String> orgDispTagList) {
        this.orgDispTagList = orgDispTagList;
    }

    public List<String> getTagPathList() {
        return tagPathList;
    }

    public void setTagPathList(List<String> tagPathList) {
        this.tagPathList = tagPathList;
    }

    public CmsSearchInfoBean2 getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(CmsSearchInfoBean2 searchValue) {
        this.searchValue = searchValue;
    }

    public List<String> getProdCodeList() {
        return prodCodeList;
    }

    public void setProdCodeList(List<String> prodCodeList) {
        this.prodCodeList = prodCodeList;
    }

    public boolean getIsSelAll() {
        return isSelAll;
    }

    public void setIsSelAll(boolean isSelAll) {
        this.isSelAll = isSelAll;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isEmpty(channelId)) {
            throw new MQMessageRuleException("高级搜索-批量设置自由标签MQ发送异常, 参数channelId为空.");
        }
        if (!isSelAll && CollectionUtils.isEmpty(prodCodeList)) {
            throw new MQMessageRuleException("高级搜索-批量设置自由标签MQ发送异常, 参数isSelAll为false并且prodCodeList为空.");
        }
        if (isSelAll && this.searchValue == null) {
            throw new MQMessageRuleException("高级搜索-批量设置自由标签MQ发送异常, 参数isSelAll为true并且searchValue为空.");
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
