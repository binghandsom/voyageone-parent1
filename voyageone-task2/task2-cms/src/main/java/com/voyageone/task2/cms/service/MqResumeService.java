package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.mq.MqSender;
import com.voyageone.common.mq.dao.MqMsgBackDao;
import com.voyageone.common.mq.enums.MqRoutingKey;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * mq恢复Service
 * @author aooer 2016/3/1.
 * @version 2.0.0
 * @since 2.0.0
 */

@Service
public class MqResumeService extends BaseTaskService {

    @Autowired
    private MqSender sender;

    @Autowired
    private MqMsgBackDao msgBackDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.COM;
    }

    @Override
    public String getTaskName() {
        return "mqConsumJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // get data from db
        msgBackDao.selectBatchMessage().forEach(message->{
            MqRoutingKey routingKey = MqRoutingKey.valueOf((String)message.get("routingKey"));
            String messageMapStr = (String)message.get("messageMap");

            $debug("MqResumeService Resume:=" + routingKey.getValue() + " ; " + messageMapStr);

            // send mq to mqserver
            sender.sendMessage(
                    routingKey,
                    JsonUtil.jsonToMap(messageMapStr));

            // update db data flag
            msgBackDao.updateBatchMessageStatus((int)message.get("id"));
        });
    }
}
