package com.voyageone.common.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.bean.ComBtTaobaoApiLogModel;
import org.springframework.stereotype.Repository;

/**
 * Created by jonasvlag on 16/3/14.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class ComBtTaobaoApiLogDao extends BaseDao {

    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.COM);
    }

    public int insert(ComBtTaobaoApiLogModel model) {
        return insert("com_bt_taobao_api_log_insert", model);
    }
}
