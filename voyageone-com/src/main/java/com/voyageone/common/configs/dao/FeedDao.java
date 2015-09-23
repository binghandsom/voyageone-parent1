package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.FeedBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zero on 8/18/2015.
 */
@Repository
public class FeedDao extends BaseDao {
    /**
     * 根据消息类型获得类型消息Map
     */
    public List<FeedBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "cms_mt_feed_config_getAll");
    }
}
