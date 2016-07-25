package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.com.mq.MqBackMessageService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * mq恢复Service
 *
 * @author aooer 2016/3/1.
 * @version 2.0.0
 * @since 2.0.0
 */

@Service
public class MqResumeService extends BaseTaskService {

    @Autowired
    private MqSender sender;

    @Autowired
    private MqBackMessageService mqBackMessageService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.COM;
    }

    @Override
    public String getTaskName() {
        return "mqResumeJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        while(true) {
            // get data from db
            List<Map<String, Object>> rowList = mqBackMessageService.getBackMessageTop100();
            if (rowList == null || rowList.isEmpty()) {
                break;
            }
            for (Map<String, Object> row : rowList) {
                String messageMapStr = (String) row.get("messageMap");
                // send mq to mqserver
                sender.sendMessage((String) row.get("routingKey"), JacksonUtil.jsonToMap(messageMapStr));
                // update db data flag
                mqBackMessageService.updateBackMessageFlag((int) row.get("id"));
            }
        }
    }
}
