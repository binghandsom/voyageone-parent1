package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.IdCardHistory;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Repository;

/**
 * Created by Jonas on 9/22/15.
 */
@Repository
public class IdCardHistoryDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.SYNSHIP);
    }

    public int insert(IdCardHistory idCardHistory) {
        return insert("tt_idcard_history_insert", idCardHistory);
    }
}
