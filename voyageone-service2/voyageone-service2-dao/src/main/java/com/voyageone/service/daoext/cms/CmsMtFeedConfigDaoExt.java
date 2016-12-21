package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import com.voyageone.service.model.cms.CmsMtFeedConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/7.
 */
@Repository
public class CmsMtFeedConfigDaoExt extends ServiceBaseDao {
    public List<CmsMtFeedConfigBean> selectFeedConFigByChannelId(String channelId) {
        return selectList("cms_mt_feed_config_selectByChannelId", channelId);
    }
    public List<CmsMtFeedConfigBean> deleteFeedConFigByChannelId(String channelId) {
        return selectList("cms_mt_feed_config_deleteByChannelId", channelId);
    }
}
