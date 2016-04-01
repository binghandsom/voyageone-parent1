package com.voyageone.task2.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.cms.bean.ShoeCityFeedBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jonasvlag on 16/3/30.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class ShoeCityDao extends BaseDao {

    public int insertList(List<ShoeCityFeedBean> beanList) {
        return insert("cms_zz_worktable_se_superfeed_insertList", parameters("beanList", beanList));
    }

    public List<ShoeCityFeedBean> selectListUnsaved() {
        return selectList("cms_zz_worktable_se_superfeed_full_selectUnsaved");
    }

    public int updateSucceed(List<CmsBtFeedInfoModel> succeed) {
        return update("cms_zz_worktable_se_superfeed_full_updateSucceed", parameters("succeed", succeed));
    }
}
