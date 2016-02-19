package com.voyageone.web2.cms.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.cms.model.CmsBtFeedCustomPropModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * for voyageone_ims.ims_mt_custom_word and voyageone_ims.ims_mt_custom_word_param
 *
 * Created by Jonas on 9/11/15.
 */
@Repository
public class CmsBtFeedCustomPropDao extends WebBaseDao {
    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CMS;
    }

    public List<CmsBtFeedCustomPropModel> selectWithCategory(Map<String, Object> params) {
        return selectList("cms_bt_feed_custom_prop_selectWithCatPath", params);
    }
}
