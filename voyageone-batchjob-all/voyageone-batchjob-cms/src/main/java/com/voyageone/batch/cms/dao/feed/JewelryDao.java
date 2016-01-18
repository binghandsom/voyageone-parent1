package com.voyageone.batch.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.CmsBtFeedInfoJewelryModel;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/1/15.
 * @version 2.0.0
 */
@Repository
public class JewelryDao extends BaseDao {

    public List<CmsBtFeedInfoJewelryModel> selectSuperfeedModel(String keyword, Map params, String tableName) {
        params.put("keyword", keyword);
        params.put("tableName", tableName);

        return selectList("cms_jewelryfeed_select", params);
    }

    // 把读入成功的FEED数据保存起来
    public int insertFull(List<String> itemIds){
        return insert("cms_zz_worktable_je_superfeed_full_insert",itemIds);
    }

    //把导入成功的FEED数据 从保存数据中删除
    public int delFull(List<String> itemIds){
        return delete("cms_zz_worktable_je_superfeed_full_del", itemIds);
    }

    // 导入成功的FEED数据的状态变更
    public int updateFeetStatus(List<String> itemIds){
        return delete("cms_zz_worktable_je_superfeed_update",itemIds);
    }
}
