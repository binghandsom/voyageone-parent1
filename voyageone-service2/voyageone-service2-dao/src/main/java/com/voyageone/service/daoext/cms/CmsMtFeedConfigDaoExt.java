package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/7.
 */
@Repository
public class CmsMtFeedConfigDaoExt extends ServiceBaseDao {

    public List<CmsMtFeedConfigBean> selectFeedConFigKey() {
        return selectList("cms_mt_feed_config_key");
    }

    public List<CmsMtFeedConfigBean> selectFeedConFigByChannelId(String channelId) {
        return selectList("cms_mt_feed_config_selectByChannelId", channelId);
    }
    public int deleteFeedConFigByChannelId(String channelId) {
        return delete("cms_mt_feed_config_deleteByChannelId", channelId);
    }

    public int deleteFeedConFigInfoByChannelId(String channelId) {
        return delete("cms_mt_feed_config_info_deleteByChannelId", channelId);
    }
}
