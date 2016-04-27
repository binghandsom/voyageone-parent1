package com.voyageone.service.dao.com;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/3/1.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class ComMtMqMessageBackDao extends BaseDao {

    /**
     * 查询未处理的数据
     */
    public List<Map<String, Object>> selectBackMessageTop100() {
        return selectList("com_mt_mq_message_back_select100");
    }

    /**
     * 插入备份数据
     * @param routingKey rk
     * @param messageMap mm
     */
    public void insertBackMessage(String routingKey, Map<String, Object> messageMap) {
        Map<String, Object> param = new HashMap<>();
        param.put("routingKey", routingKey);
        param.put("messageMap", JacksonUtil.bean2Json(messageMap));
        updateTemplate.insert("com_mt_mq_message_back_insert", param);
    }

    /**
     * 更新状态
     * @param id 主键
     */
    public void updateBackMessageFlag(int id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        updateTemplate.update("com_mt_mq_message_back_update_flag", param);
    }


}
