package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * cms_mt_channel_config Dao
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */

@Repository
public class CmsMtChannelConfigDaoExt extends ServiceBaseDao {

    // TODO: 16/4/20 没有被用到,edward
//    public List<CmsMtChannelConfigModel> selectALl(){
//        return selectList("cms_mt_channel_config_getAll");
//    }


    /**
     * 更具configKey返回制定的数据
     */
    public List<CmsMtChannelConfigModel> selectByConfigKey(String channelId, String configKey) {
        CmsMtChannelConfigModel model = new CmsMtChannelConfigModel();
        model.setChannelId(channelId);
        model.setConfigKey(configKey);
        return selectList("cms_mt_channel_config_selectByConfigKey", model);
    }
}
