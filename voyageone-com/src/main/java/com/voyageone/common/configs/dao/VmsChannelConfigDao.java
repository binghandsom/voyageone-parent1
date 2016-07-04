package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * vms_mt_channel_config Dao
 * @author chuanyu.liang, 16/06/25
 * @version 2.0.0
 * @since 2.0.0
 */

@Repository
public class VmsChannelConfigDao extends BaseDao {

    public List<VmsChannelConfigBean> selectALl(){
        return selectList("vms_mt_channel_config_getAll");
    }

}
