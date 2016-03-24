package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.model.CmsBusinessLogModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

/**
 * Created by Leo on 15-12-21.
 */
@Repository
public class CmsBusinessLogDao extends BaseDao {

    /**
     * 插入主数据（类目匹配）
     */
    public void insertBusinessLog(CmsBusinessLogModel cmsBusinessLogModel) {
       updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insertCmsBusinessLog", cmsBusinessLogModel);
    }
}
