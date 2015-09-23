package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.synship.modelbean.ClientTrackingSimBean;
import com.voyageone.batch.synship.modelbean.SmsHistoryBean;
import com.voyageone.batch.synship.modelbean.TrackingSyncBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fred on 2015/8/31.
 */
@Repository
public class SmsHistoryDao  extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.batch.synship.sql";
    }


    /**
     * 获取需要发送短信的记录
     *
     * @param order_channel_id 渠道
     * @param row_count        抽出件数
     * @return ClientTrackingSimBean
     */
    public List<SmsHistoryBean> getSendSMSLst(String order_channel_id, int row_count) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("limit", row_count);
        params.put("sent_type", SynshipConstants.SMS_SNET_TYPE.CLIENT);
        params.put("sms_flg", SynshipConstants.SMS_STATUS.NOT_SENT);
        return selectList("synShip_getSendSMSLst", params);
    }


    /**
     * 短信信息更新
     *
     * @param smsHistoryBean 短信信息
     */
    public int UpdateSmsHistory(SmsHistoryBean smsHistoryBean) {

        return updateTemplate.update( "synShip_UpdateSmsHistory", smsHistoryBean);
    }

    /**
     * 短信状态更新
     *
     * @param smsHistoryBean 短信信息
     */
    public int UpdateSmsHistoryStatus(SmsHistoryBean smsHistoryBean) {

        return updateTemplate.update( "synShip_UpdateSmsHistoryStatus", smsHistoryBean);
    }
}
