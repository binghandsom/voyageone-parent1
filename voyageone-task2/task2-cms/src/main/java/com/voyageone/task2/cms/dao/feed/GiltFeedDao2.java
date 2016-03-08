package com.voyageone.task2.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.bean.SuperFeedGiltBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class GiltFeedDao2 extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    public int clearTemp() {
        return delete("cms2_zz_worktable_gilt_superfeed_clear", null);
    }

    public int insertListTemp(List<SuperFeedGiltBean> feedGiltBeanList) {
        return insert("cms2_zz_worktable_gilt_superfeed_insertList", parameters("items", feedGiltBeanList));
    }

    public boolean selectBySku(String id) {
        List<Map<String, Object>> rslt = selectList("cms2_zz_worktable_gilt_superfeed_full_select4exist", parameters("id", id));
        if (rslt == null || rslt.isEmpty()) {
            return false;
        } else{
            return true;
        }
    }

    public int insertGiltList(List<SuperFeedGiltBean> feedGiltBeanList) {
        return insert("cms2_zz_worktable_gilt_superfeed_full_insert", parameters("items", feedGiltBeanList));
    }

    public int updateGiltList(List<SuperFeedGiltBean> feedGiltBeanList) {
        return insert("cms2_zz_worktable_gilt_superfeed_full_update", parameters("items", feedGiltBeanList));
    }
}
