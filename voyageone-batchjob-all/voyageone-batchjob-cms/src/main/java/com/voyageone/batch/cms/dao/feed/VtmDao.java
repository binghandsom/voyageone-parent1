package com.voyageone.batch.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.CmsBtFeedInfoVtmModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/9
 */
@Repository
public class VtmDao extends BaseDao {

    public List<CmsBtFeedInfoVtmModel> selectSuperfeedModel(String keyword, Map params, String tableName) {
        params.put("keyword", keyword);
        params.put("tableName", tableName);

        return selectList("cms_vtmfeed_select", params);
    }

    // 原数据UpdateFlag更新成0
    public int updateFull(){
        return delete("cms_zz_worktable_vtm_superfeed_full_update", null);
    }

    // 把读入成功的FEED数据保存起来
    public int insertFull(List<String> itemIds){
        return insert("cms_zz_worktable_vtm_superfeed_full_insert",itemIds);
    }

    //把导入成功的FEED数据 从保存数据中删除
    public int delFull(List<String> itemIds){
        return delete("cms_zz_worktable_vtm_superfeed_full_del", itemIds);
    }

    // 导入成功的FEED数据的状态变更
    public int updateFeetStatus(List<String> itemIds){
        return delete("cms_zz_worktable_vtm_superfeed_update",itemIds);
    }
}
