package com.voyageone.batch.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.BcbgStyleBean;
import com.voyageone.batch.cms.bean.SuperFeedBcbgBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 所有 bcbg 数据解析的操作
 *
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

    public int insertFullWithoutFail(List<String> modelFailList, List<String> productFailList) {
        return insert("cms_zz_worktable_bcbg_superfeed_full_insertWithoutFail",
                parameters("update_flg", 1, "modelFailList", modelFailList,
                        "productFailList", productFailList));
    }
}
