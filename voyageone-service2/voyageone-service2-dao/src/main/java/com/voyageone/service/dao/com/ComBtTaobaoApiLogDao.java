package com.voyageone.service.dao.com;

import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.com.ComBtTaobaoApiLogModel;
import org.springframework.stereotype.Repository;

/**
 * Created by jonasvlag on 16/3/14.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class ComBtTaobaoApiLogDao  extends ServiceBaseDao {

    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.COM);
    }

    public int insert(ComBtTaobaoApiLogModel model) {
        return insert("com_bt_taobao_api_log_insert", model);
    }
}
