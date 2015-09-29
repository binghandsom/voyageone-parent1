package com.voyageone.batch.synship.service;

import com.voyageone.batch.synship.dao.SmsConfigDao;
import com.voyageone.batch.synship.modelbean.SmsConfigBean;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供对配置在 tm_sms_config 内设定的访问
 *
 * Created by Jonas on 9/23/15.
 */
@Service
public class SmsConfigService {

    @Autowired
    private SmsConfigDao smsConfigDao;

    /**
     * 读取所有渠道的 sms_type 的短信配置
     *
     * @param sms_type 短信类型
     * @return SmsConfig
     */
    public SmsConfig getSmsConfigs(String sms_type) {

        Map<String, List<SmsConfigBean>> mapSmsConfig = new HashMap<>();

        for (ChannelConfigEnums.Channel channel : ChannelConfigEnums.Channel.values()) {
            List<SmsConfigBean> list = smsConfigDao.getDataListFromSmsConfigByOrderChannelId(channel.getId(), sms_type);
            mapSmsConfig.put(channel.getId(), list);
        }

        return new SmsConfig(mapSmsConfig);
    }
}
