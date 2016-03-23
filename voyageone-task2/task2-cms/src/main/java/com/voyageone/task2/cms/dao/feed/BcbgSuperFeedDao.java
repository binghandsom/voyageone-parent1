package com.voyageone.task2.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.cms.bean.BcbgStyleBean;
import com.voyageone.task2.cms.bean.SuperFeedBcbgBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 所有 bcbg 数据解析的操作
 * <p>
 * Created by Jonas on 10/13/15.
 */
@Repository
public class BcbgSuperFeedDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    public int insertWorkTables(List<SuperFeedBcbgBean> bcbgBeans) {
        return insert("cms_zz_worktable_bcbg_superfeed_insertWorkTables", parameters("bcbgBeans", bcbgBeans));
    }

    public int delete() {
        return delete("cms_zz_worktable_bcbg_superfeed_delete", null);
    }

    public int insertStyles(List<BcbgStyleBean> bcbgStyles) {
        return insert("cms_zz_worktable_bcbg_styles_insertStyles", parameters("bcbgStyles", bcbgStyles));
    }

    public int deleteStyles() {
        return delete("cms_zz_worktable_bcbg_styles_deleteStyles", null);
    }

    public List<SuperFeedBcbgBean> selectUnsaved() {
        return selectList("cms_zz_worktable_bcbg_superfeed_selectUnsaved", parameters("unsaved", 0));
    }

    public int updateSucceed(List<CmsBtFeedInfoModel> succeed) {
        return update("cms_zz_worktable_bcbg_superfeed_full_updateSucceed", parameters("succeed", succeed));
    }
}
