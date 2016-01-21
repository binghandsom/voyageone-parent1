package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.web2.cms.model.CustomWord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * for voyageone_ims.ims_mt_custom_word and voyageone_ims.ims_mt_custom_word_param
 *
 * Created by Jonas on 9/11/15.
 */
@Repository
public class CustomWordDao extends BaseDao {

    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    public List<CustomWord> selectWithParam() {
        return selectList("cms_mt_custom_word_selectWithParam");
    }
}
