package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * cms_mt_channel_config Dao
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */

@Repository
public class CmsMtChannelConfigDao extends BaseDao {

    public List<CmsMtChannelConfigModel> selectALl(){
        return selectList("cms_mt_channel_config_getAll");
    }

}
