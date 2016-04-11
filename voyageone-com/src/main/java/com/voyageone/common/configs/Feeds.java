package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.FeedDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 访问 com_mt_feed_config 表配置
 * Created by Zero on 8/18/2015.
 */
public class Feeds {

    private static final Class selfClass = Feeds.class;

    private final static Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_FeedConfigs.toString();

    public static void reload() {
        FeedDao feedDao = ConfigDaoFactory.getFeedDao();
        Map<String, FeedBean> feedBeanMap = new HashMap<>();
        feedDao.getAll().forEach(bean ->
                    feedBeanMap.put(
                        buildKey(bean.getOrder_channel_id(), bean.getCfg_name(), bean.getId()),
                        bean
                    )
        );
        CacheHelper.reFreshSSB(KEY, feedBeanMap);
        logger.info("feed_config 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @param channelId channelId
     * @param name name
     * @param id id
     * @return key
     */
    private static String buildKey(String channelId, String name, int id) {
        return channelId + CacheKeyEnums.SKIP + name + CacheKeyEnums.SKIP + id;
    }

    /**
     * 获取指定渠道的第一个配置参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @return String
     */
    public static String getVal1(String id, FeedEnums.Name name) {
        List<FeedBean> beans = getConfigs(id, name);
        return (beans == null || beans.isEmpty()) ? "" : beans.get(0).getCfg_val1();
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
     * @param channelId   渠道ID
     * @param name 配置名称
     * @return List<FeedBean>
     */
    public static List<FeedBean> getConfigs(String channelId, FeedEnums.Name name) {
        List<FeedBean> feedBeanList = new ArrayList<>();
        Set<String> keys = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keys)) {
            logger.warn("未初始化CarrierBean");
            return feedBeanList;
        }

        List<String> filterKeys = new ArrayList<>();
        for (String key : keys) {
            if (key.startsWith(channelId + CacheKeyEnums.SKIP + name)) filterKeys.add(key);
        }
        if (!filterKeys.isEmpty()) {
            Collections.sort(filterKeys);
            feedBeanList = CacheHelper.getBeans(KEY, filterKeys, selfClass);
        }
        return feedBeanList;
    }
}
