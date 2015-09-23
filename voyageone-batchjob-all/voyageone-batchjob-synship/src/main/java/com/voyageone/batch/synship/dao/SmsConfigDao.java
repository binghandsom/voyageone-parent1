package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.SmsConfigBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jonas on 9/23/15.
 */
@Repository
public class SmsConfigDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.SYNSHIP);
    }

    public List<SmsConfigBean> getDataListFromSmsConfigByOrderChannelId(String id, String smsTypeCloudClient) {
        return selectList("tm_sms_config_getDataListFromSmsConfigByOrderChannelId", parameters("orderChannelId", id, "smsType", smsTypeCloudClient));
    }
}
