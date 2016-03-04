package com.voyageone.task2.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 为数据通用处理提供最大灵活度的SQL调用
 * Created by Jonas on 10/16/15.
 */
@Repository
public class TransformSqlDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    public List<String> selectStrings(String sql) {
        return selectList("cms_superfeed_selectErrData", parameters("sql", sql));
    }

    public List<Map<String, Object>> selectMaps(String sql) {
        return selectList("TransformSqlDao_selectMaps", parameters("sql", sql));
    }

    public int insert(String sql) {
        return insert("TransformSqlDao_insert", parameters("sql", sql));
    }

    public int delete(String sql) {
        return delete("TransformSqlDao_delete", parameters("sql", sql));
    }

    public int update(String sql) {
        return update("TransformSqlDao_update", parameters("sql", sql));
    }
}
