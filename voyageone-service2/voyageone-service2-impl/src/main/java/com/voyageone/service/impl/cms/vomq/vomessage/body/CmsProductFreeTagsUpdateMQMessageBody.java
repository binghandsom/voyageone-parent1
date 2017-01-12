package com.voyageone.service.impl.cms.vomq.vomessage.body;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import java.util.List;


/**
 * CmsProductFreeTagsUpdateMQMessageBody    高级搜索-设置自由标签
 *
 * @author peitao 2017/01/12
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_PPRODUCT_FREE_TAGS_UPDATE)
public class CmsProductFreeTagsUpdateMQMessageBody extends BaseMQMessageBody {

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
            throw new MQMessageRuleException("channelId不能为空");
        }
        if (!isSelAll && ListUtils.isNull(this.prodCodeList)) {
            throw new MQMessageRuleException("缺少参数，未选择商品!");
        }
        if (isSelAll && this.searchValue == null) {
            throw new MQMessageRuleException("高级检索session中的查询条件为空!");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
