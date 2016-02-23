package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * cms_mt_channel_config Dao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */

@Repository
public class CmsChannelConfigDao extends BaseDao {

    public List<CmsChannelConfigBean> selectALl(){
        return selectList("cms_mt_channel_config_getAll");
    }

}
