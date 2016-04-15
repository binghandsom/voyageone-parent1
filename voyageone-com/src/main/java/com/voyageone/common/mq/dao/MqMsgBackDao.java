package com.voyageone.common.mq.dao;

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
public class MqMsgBackDao extends BaseDao {

    /**
     * 插入备份数据
     * @param routingKey rk
     * @param messageMap mm
     */
    public void insertBatchMessage(String routingKey,Map<String,Object> messageMap){
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("routingKey",routingKey);
        param.put("messageMap", JacksonUtil.bean2Json(messageMap));
        updateTemplate.insert("mq_message_back_insert",param);
    }

    /**
     * 更新状态
     * @param id 主键
     */
    public void updateBatchMessageStatus(int id){
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("id",id);
        updateTemplate.update("mq_message_back_update",param);
    }

    /**
     * 查询未处理的数据
     */
    public List<Map<String,Object>> selectBatchMessage(){
        return selectList("mq_message_back_select");
    }
}
