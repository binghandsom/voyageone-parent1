package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;

/**
 * ims 的 Activity 和 CartActivity 的数据库操作类
 *
 * Created by Jonas on 7/16/15.
 */
public class ActivityDao extends BaseDao {
    @Override
    protected String namespace() {
        return "com.voyageone.ims.sql";
    }
}
