package com.voyageone.service.dao.com;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.com.ComBtJingdongApiLogModel;
import org.springframework.stereotype.Repository;

/**
 * Created by jonasvlag on 16/3/14.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class ComBtJingdongApiLogDao extends ServiceBaseDao {

    public int insert(ComBtJingdongApiLogModel model) {
        return insert("com_bt_jingdong_api_log_insert", model);
    }
}
