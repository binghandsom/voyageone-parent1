package com.voyageone.task2.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.bean.ProductBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.sears.bean.ProductResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<Map> getFeedAttribute(ProductBean productBean) {
        List<Map> ret;
        ret = selectList("cms_attribute_select", productBean);
        if (ret == null) {
            ret = new ArrayList<>();
        }
        return ret;
    }

    // 把读入成功的FEED数据保存起来
    public int insertFull(List<String> itemIds){
        return insert("cms_zz_worktable_sears_superfeed_full_insert",itemIds);
    }

    //把导入成功的FEED数据 从保存数据中删除
    public int delFull(List<String> itemIds){
        return delete("cms_zz_worktable_sears_superfeed_full_del", itemIds);
    }

    // 导入成功的FEED数据的状态变更
    public int updateFeetStatus(List<String> itemIds){
        return delete("cms_zz_worktable_sears_superfeed_update",itemIds);
    }

    // 把读入成功的FEED数据保存起来
    public int insertFullByCode(Map code){
        return insert("cms_zz_worktable_sears_superfeed_full_code_insert",code);
    }

    //把导入成功的FEED数据 从保存数据中删除
    public int delFullByCode(Map code){
        return delete("cms_zz_worktable_sears_superfeed_full_code_del", code);
    }

    // 导入成功的FEED数据的状态变更
    public int updateFeetStatusByCode(Map code){
        return delete("cms_zz_worktable_sears_superfeed_code_update",code);
    }
}
