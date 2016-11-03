package com.voyageone.task2.cms.service.tools;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.tools.RefreshProductsBean;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * MQ 任务。用于在用户要求对，某类目的全部或单个属性，进行重新计算值之后，重新赋值并触发上新的任务
 * Created by jonas on 2016/11/2.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_REFRESH_PRODUCTS)
public class CmsRefreshProductsJobService extends BaseMQCmsService {
    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {

        Map map = MapUtils.getMap(messageMap, "refreshProductsBean");
        RefreshProductsBean refreshProductsBean = new RefreshProductsBean();

        BeanUtils.populate(refreshProductsBean, map);

        System.out.println("\n\n");
        System.out.println(JacksonUtil.bean2Json(refreshProductsBean));
        System.out.println("\n\n");
    }
}
