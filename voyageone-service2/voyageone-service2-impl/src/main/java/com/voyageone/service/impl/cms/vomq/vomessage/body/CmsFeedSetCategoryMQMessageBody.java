package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.bean.cms.CmsMtCategoryTreeAllBean;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.List;

/**
 * Created by james on 2017/5/17.
 *
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_FEED_SET_CATEGORY_MQ_JOB)
public class CmsFeedSetCategoryMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {

    private List<String> codeList;
    private CmsMtCategoryTreeAllBean mainCategoryInfo;

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public CmsMtCategoryTreeAllBean getMainCategoryInfo() {
        return mainCategoryInfo;
    }

    public void setMainCategoryInfo(CmsMtCategoryTreeAllBean mainCategoryInfo) {
        this.mainCategoryInfo = mainCategoryInfo;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(mainCategoryInfo == null || ListUtils.isNull(codeList)) throw new MQMessageRuleException("参数不正确");
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
