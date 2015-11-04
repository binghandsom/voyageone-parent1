package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.configs.dao.FeedDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 访问 com_mt_feed_config 表配置
 * Created by Zero on 8/18/2015.
 */
public class Feed {
    private static Configs configs;
    private static Log logger = LogFactory.getLog(Feed.class);

    /**
     * 初始化渠道相关的基本信息和配置信息
     *
     * @param feedDao 用于获取配置信息
     */
    public static void init(FeedDao feedDao) {
        if (configs == null) {
            List<FeedBean> all = feedDao.getAll();

            logger.info("Feed 读取数量: " + all.size());

            configs = new Configs(all);
        }
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
        return (beans == null || beans.size() < 1) ? "" : beans.get(0).getCfg_val1();
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
     * @return List<Feed>
     */
    public static List<FeedBean> getConfigs(String id, FeedEnums.Name name) {
        if (configs == null) {
            logger.error("====== Feed 没有初始化，必须先调用 init");
            return null;
        }

        if (!configs.containsKey(id)) return null;

        ConfigMap map = configs.get(id);

        if (!map.containsKey(name)) return null;

        return map.get(name);
    }

    /**
     * 专用存储所有渠道的配置信息
     *
     * @author Jonas
     */
    private static class Configs extends HashMap<String, ConfigMap> {
        private static final long serialVersionUID = 1L;

        public Configs(List<FeedBean> beans) {
            beans.forEach(this::put);
        }

        public void put(FeedBean config) {
            String id = config.getOrder_channel_id();
            ConfigMap map = containsKey(id) ? get(id) : new ConfigMap();

            try {
                FeedEnums.Name name = FeedEnums.Name.valueOf(config.getCfg_name());
                map.put(name, config);
                put(id, map);
            } catch (IllegalArgumentException e) {
                logger.warn(String.format("Feed 枚举匹配警告: [%s] NO \"%s\"", config.getOrder_channel_id(), config.getCfg_name()));
            }
        }
    }

    /**
     * 专用存储单个渠道的所有配置信息
     *
     * @author Jonas
     */
    private static class ConfigMap extends HashMap<FeedEnums.Name, List<FeedBean>> {
        private static final long serialVersionUID = 1L;

        public void put(FeedEnums.Name name, FeedBean config) {
            if (!containsKey(name)) {
                super.put(name, new ArrayList<>());
            }

            get(name).add(config);
        }
    }
}
