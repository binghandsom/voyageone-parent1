package com.voyageone.batch.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.bean.ProductBean;
import com.voyageone.common.components.sears.bean.ProductResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2015/10/26.
 */
@Repository
public class SearsFeedDao extends BaseDao {
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    public int delete() {
        return delete("cms_zz_worktable_sears_superfeed_delete", null);
    }

    public int insert(ProductResponse product) {
        return insert("cms_zz_worktable_sears_superfeed_insert", product);
    }

    public int insertAattribute(ProductResponse product) {
        return insert("cms_zz_worktable_sears_attribute_insert", product);
    }

    public int deleteAattribute() {
        return delete("cms_zz_worktable_sears_attribute_delete", null);
    }

    public int deleteList() {
        return delete("cms_zz_worktable_sears_list_delete", null);
    }

    public int insertFeedList(ProductResponse product) {
        return insert("cms_zz_worktable_sears_list_insert", product);
    }

    /**
     * 获取feedList数据
     * @param status
     * @return
     */
    public List<String> getFeedList(String status) {
        List<String> ret;
        ret = selectList("cms_zz_worktable_sears_list_select", status);
        if (ret == null) {
            ret = new ArrayList<>();
        }
        return ret;
    }

}
