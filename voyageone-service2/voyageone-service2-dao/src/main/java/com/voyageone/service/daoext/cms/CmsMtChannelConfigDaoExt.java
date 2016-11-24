package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/7.
 */
@Repository
public class CmsMtChannelConfigDaoExt extends ServiceBaseDao {

    public List<CmsMtChannelConfigModel> selectByChannelId(String channelId) {
        return selectList("cms_mt_channel_config_selectByChannleId", channelId);
    }
}
