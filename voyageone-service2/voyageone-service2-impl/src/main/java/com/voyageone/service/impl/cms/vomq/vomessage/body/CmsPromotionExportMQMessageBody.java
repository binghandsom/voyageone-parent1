package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.lang3.StringUtils;

/**
 * 活动(非聚美)商品导出Job消息实体
 *
 * @Author rex.wu
 * @Create 2017-05-17 15:38
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_PROMOTION_EXPORT)
public class CmsPromotionExportMQMessageBody extends BaseMQMessageBody {

    private Integer cmsPromotionExportTaskId;

    @Override
    public void check() throws MQMessageRuleException {

        String channelId = getChannelId();
        String username = getSender();

        if (StringUtils.isBlank(channelId)) {
            throw new BusinessException("活动商品导出MQ参数channel为空");
        }

        if (cmsPromotionExportTaskId == null) {
            throw new BusinessException("活动商品导出MQ参数cmsPromotionExportTaskId为空");
        }

        if (StringUtils.isBlank(username)) {
            throw new BusinessException("活动商品导出MQ发送人为空");
        }
    }

    public Integer getCmsPromotionExportTaskId() {
        return cmsPromotionExportTaskId;
    }

    public void setCmsPromotionExportTaskId(Integer cmsPromotionExportTaskId) {
        this.cmsPromotionExportTaskId = cmsPromotionExportTaskId;
    }

}
