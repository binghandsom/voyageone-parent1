package com.voyageone.task2.cms.service.tools;

import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.tools.PlatformMappingService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtRefreshProductTaskModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final PlatformMappingService platformMappingService;
    private final CmsBtProductDao cmsBtProductDao;

    @Autowired
    public CmsRefreshProductsJobService(PlatformMappingService platformMappingService, CmsBtProductDao cmsBtProductDao) {
        this.platformMappingService = platformMappingService;
        this.cmsBtProductDao = cmsBtProductDao;
    }

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {

        Map map = MapUtils.getMap(messageMap, "cmsBtRefreshProductTaskModel");
        CmsBtRefreshProductTaskModel refreshProductsBean = new CmsBtRefreshProductTaskModel();

        BeanUtils.populate(refreshProductsBean, map);
    }
}
