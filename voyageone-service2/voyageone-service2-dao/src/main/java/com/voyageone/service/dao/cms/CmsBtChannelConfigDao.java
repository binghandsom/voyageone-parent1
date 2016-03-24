package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtChannelConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsBtChannelConfigDao extends ServiceBaseDao {

    /**
     * 更具configKey返回制定的数据
     * @param channelId
     * @param configKey
     * @return
     */
    public List<CmsBtChannelConfigModel> selectByConfigKey(String channelId, String configKey) {
        CmsBtChannelConfigModel model = new CmsBtChannelConfigModel();
        model.setChannelId(channelId);
        model.setConfigKey(configKey);
        return selectList("cms_mt_channel_config_selectByConfigKey", model);
    }
}
