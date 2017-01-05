package com.voyageone.service.daoext.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.CmsMtFeedConfigInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/12/27.
 */
@Repository
public class CmsMtFeedConfigInfoDaoExt extends BaseDao {

    public List<CmsMtFeedConfigInfoModel> selectFeedConFigInfo(String channelId) {
        return selectList("cms_mt_feed_config_info", channelId);
    }

    public void createdTable(Map<Object, Object> params){
        updateTemplate.update("createdTable",params);
    }

    public  int selectFeedConFigInfoCnt(int id){
        return selectOne("cms_mt_feed_config_info_cnt", id);
    }
}
