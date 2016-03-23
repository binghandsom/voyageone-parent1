package com.voyageone.common.configscache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.FeedDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.springframework.data.redis.core.HashOperations;

import java.util.HashMap;
import java.util.Map;

/**
 * 访问 com_mt_feed_config 表配置
 * Created by Zero on 8/18/2015.
 */
public class FeedConfigs {

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_FeedConfigs.toString();

    private static HashOperations<String, String, FeedBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            FeedDao feedDao = ConfigDaoFactory.getFeedDao();
            Map<String, FeedBean> feedBeanMap = new HashMap<>();
            feedDao.getAll().forEach(bean -> {
                        feedBeanMap.put(
                                FeedConfigs.buildKey(bean.getOrder_channel_id(), FeedEnums.Name.valueOf(bean.getCfg_name())),
                                bean
                        );
                    }
            );
            CacheHelper.reFreshSSB(KEY, feedBeanMap);
        }
    }

    /**
     * build redis hash Key
     *
     * @param id   id
     * @param name name
     * @return key
     */
    private static String buildKey(String id, FeedEnums.Name name) {
        return id + CacheHelper.SKIP + name;
    }

    /**
     * 获取指定渠道的第一个配置参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @return String
     */
    public static String getVal1(String id, FeedEnums.Name name) {
        FeedBean bean = getConfigs(id, name);
        return (bean == null) ? null : bean.getCfg_val1();
    }

    /**
     * 获取指定渠道的第一个配置参数
     *
     * @param channel 渠道
     * @param name    配置名称
     * @return String
     */
    public static String getVal1(ChannelConfigEnums.Channel channel, FeedEnums.Name name) {
        return getVal1(channel.getId(), name);
    }

    /**
     * 获取指定渠道的一个配置的所有参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @return FeedBean
     */
    public static FeedBean getConfigs(String id, FeedEnums.Name name) {
        return hashOperations.get(KEY, buildKey(id, name));
    }
}
