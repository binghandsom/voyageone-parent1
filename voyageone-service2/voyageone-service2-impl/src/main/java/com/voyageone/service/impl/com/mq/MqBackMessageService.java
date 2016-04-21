package com.voyageone.service.impl.com.mq;

import com.voyageone.service.dao.com.ComMtMqMessageBackDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class MqBackMessageService extends BaseService {

    @Autowired
    private ComMtMqMessageBackDao comMtMqMessageBackDao;
    /**
     * 查询未处理的数据(Top 100)
     */
    public List<Map<String, Object>> getBackMessageTop100() {
        return comMtMqMessageBackDao.selectBackMessageTop100();
    }

    /**
     * 插入备份数据
     * @param routingKey rk
     * @param messageMap mm
     */
    public void addBackMessage(String routingKey, Map<String, Object> messageMap) {
        comMtMqMessageBackDao.insertBackMessage(routingKey, messageMap);
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
