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
        return Constants.getDaoNameSpace(SubSystem.CMS) + ".BcbgSuperFeedDao";
    }

    public int insertWorkTables(List<SuperFeedBcbgBean> bcbgBeans) {
        return insert("insertWorkTables", parameters("bcbgBeans", bcbgBeans));
    }

    public int delete() {
        return delete("delete", null);
    }

    public int insertStyles(List<BcbgStyleBean> bcbgStyles) {
        return insert("insertStyles", parameters("bcbgStyles", bcbgStyles));
    }

    public int deleteStyles() {
        return delete("deleteStyles", null);
    }

    public List<SuperFeedBcbgBean> selectUnsaved() {
        return selectList("selectUnsaved", parameters("unsaved", 0));
    }

    public int updateSucceed(List<CmsBtFeedInfoModel> succeed) {
        return update("updateSucceed", parameters("succeed", succeed));
    }
}
