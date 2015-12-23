package com.voyageone.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.synship.formbean.TrackInfoBean;
import com.voyageone.synship.modelbean.WaybillRouteBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2015/7/23.
 */
@Repository
public class TaskDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.synship.sql";
    }

    /**
     * 获得该渠道的CMS、IMS的取得源
     * @param task_id
     * @param cfg_name
     * @param cfg_val1
     * @return String
     */
    public String getCfg_val2(String task_id,String cfg_name,String cfg_val1) {

        Map<String, Object> params = new HashMap<>();

        params.put("task_id", task_id);
        params.put("cfg_name", cfg_name);
        params.put("cfg_val1", cfg_val1);

        return selectOne("taskControl_getCfg_val2", params);
    }

}
