package com.voyageone.task2.cms.service.product.batch;

import com.voyageone.service.impl.cms.vomessage.CmsMqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 高级检索业务的批量处理相关业务将来都要移到这里，都采用异步处理
 *
 * @author jiangjusheng on 2016/08/24
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_TASK_AdvSearch_AsynProcessJob)
public class CmsAdvSearchAsynProcessService extends BaseMQCmsService {

    @Autowired
    private CmsBacthUpdateTask bacthUpdateService;
    @Autowired
    private CmsAddChannelCategoryTask saveChannelCategoryService;
    @Autowired
    private CmsConfirmRetailPriceTask confirmRetailPriceService;

    @Override
    public String getTaskName() {
        return "CmsAdvSearchAsynProcessService";
    }

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        String serviceName = (String) messageMap.get("_taskName");
        if ("batchupdate".equals(serviceName)) {
            bacthUpdateService.onStartup(messageMap);
        } else if ("saveChannelCategory".equals(serviceName)) {
            saveChannelCategoryService.onStartup(messageMap);
        } else if ("retailprice".equals(serviceName)) {
            confirmRetailPriceService.onStartup(messageMap);
        } else if ("refreshRetailPrice".equals(serviceName)) {
//            refreshRetailPriceService.onStartup(messageMap);
        } else {
            $error("高级检索异步批量处理 未知操作 " + messageMap.toString());
        }
    }

}
