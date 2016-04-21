package com.voyageone.service.impl.com.mq;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.com.ComMtMqMessageBackDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.mq.enums.MqRoutingKey;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class MqSender extends BaseService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private ComMtMqMessageBackDao comMtMqMessageBackDao;

    public void sendMessage(MqRoutingKey routingKey, Map<String, Object> messageMap) {
        try {
            amqpAdmin.declareQueue(new Queue(routingKey.getValue()));
            if (messageMap == null) {
                messageMap = new HashMap<>();
            }
            amqpTemplate.convertAndSend(routingKey.getValue(), JacksonUtil.bean2Json(messageMap));
        } catch (Exception e) {
            $error(e.getMessage(), e);
            comMtMqMessageBackDao.insertBackMessage(routingKey.toString(), messageMap);
        }
    }

    /**
     * 查询未处理的数据(Top 100)
     */
    public List<Map<String, Object>> getBackMessageTop100() {
        return comMtMqMessageBackDao.selectBackMessageTop100();
    }

    /**
     * 更新状态
     *
     * @param id 主键
     */
    public void updateBackMessageFlag(int id) {
        comMtMqMessageBackDao.updateBackMessageFlag(id);
    }

}
