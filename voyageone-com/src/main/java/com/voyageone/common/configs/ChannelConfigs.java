package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.DiscountRateBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.OrderChannelConfigBean;
import com.voyageone.common.configs.dao.OrderChannelConfigDao;
import com.voyageone.common.configs.dao.OrderChannelDao;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 访问 tm_order_channel 和 tm_order_channel_configs 表配置
 * Created by Jonas on 4/16/2015.
 */
public class ChannelConfigs {
    private static Configs configs;
    private static Channels channels;
    private static  List<DiscountRateBean> discountRates;

    private static Log logger = LogFactory.getLog(ChannelConfigs.class);

    /**
     * 初始化渠道相关的基本信息和配置信息
     *
     * @param orderChannelDao       用于获取基本信息
     * @param orderChannelConfigDao 用于获取配置信息
     */
    public static void init(OrderChannelDao orderChannelDao, OrderChannelConfigDao orderChannelConfigDao) {
        if (configs == null) {
            List<OrderChannelConfigBean> all = orderChannelConfigDao.getAll();

            logger.info("OrderChannelConfig 读取数量: " + all.size());

            configs = new Configs(all);
        }

        if (channels == null) {
            List<OrderChannelBean> all = orderChannelDao.getAll();

            logger.info("OrderChannel 读取数量: " + all.size());

            channels = new Channels(all);
        }

        if (discountRates == null) {
            discountRates = orderChannelConfigDao.getDiscountRate();

            logger.info("DiscountRate 读取数量: " + discountRates.size());

        }
    }

    /**
     * 获取指定渠道的基本信息
     *
     * @param id 渠道ID
     * @return OrderChannel
     */
    public static OrderChannelBean getChannel(String id) {
        if (channels == null) {
            logger.error("====== ChannelConfig 没有初始化，必须先调用 init");
            return null;
        }

        return channels.get(id);
    }

    /**
     * 获取所有 Channel Id
     * @return Set
     */
    public static Set<String> getChannelIdSet() {
        return channels.keySet();
    }

    /**
     * 获取指定渠道的第一个配置参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @return String
     */
    public static String getVal1(String id, ChannelConfigEnums.Name name) {
        List<OrderChannelConfigBean> beans = getConfigs(id, name);
        return (beans == null || beans.size() < 1) ? "" : beans.get(0).getCfg_val1();
    }

    /**
     * 获取指定渠道、配置名称、第一个参数的第二个参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @param val1 第一个参数
     * @return String
     */
    public static String getVal2(String id, ChannelConfigEnums.Name name, String val1) {
        List<OrderChannelConfigBean> beans = getConfigs(id, name);
        if (beans == null || beans.size() < 1) {
            return "";
        }
        String val2 = "";
        for (OrderChannelConfigBean orderChannelConfig : beans) {
            if (val1.equals(orderChannelConfig.getCfg_val1())) {
                val2 = orderChannelConfig.getCfg_val2();
                break;
            }
        }
        return val2;
    }

    /**
     * 获取指定渠道的一个配置的所有参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @return List<OrderChannelConfig>
     */
    public static List<OrderChannelConfigBean> getConfigs(String id, ChannelConfigEnums.Name name) {
        if (configs == null) {
            logger.error("====== ChannelConfig 没有初始化，必须先调用 init");
            return null;
        }

        if (!configs.containsKey(id)) return null;

        ConfigMap map = configs.get(id);

        if (!map.containsKey(name)) return null;

        return map.get(name);
    }

    /**
     * 专用存储渠道基础信息
     *
     * @author Jonas
     */
    private static class Channels extends HashMap<String, OrderChannelBean> {
        private static final long serialVersionUID = 1L;

        public Channels(List<OrderChannelBean> beans) {
            for (OrderChannelBean bean : beans) {
                put(bean.getOrder_channel_id(), bean);
            }
        }
    }

    /**
     * 专用存储所有渠道的配置信息
     *
     * @author Jonas
     */
    private static class Configs extends HashMap<String, ConfigMap> {
        private static final long serialVersionUID = 1L;

        public Configs(List<OrderChannelConfigBean> beans) {
            for (OrderChannelConfigBean bean : beans) {
                put(bean);
            }
        }

        public void put(OrderChannelConfigBean config) {
            String id = config.getOrder_channel_id();
            ConfigMap map = containsKey(id) ? get(id) : new ConfigMap();

            try {
                ChannelConfigEnums.Name name = ChannelConfigEnums.Name.valueOf(config.getCfg_name());
                map.put(name, config);
                put(id, map);
            } catch (IllegalArgumentException e) {
                logger.warn("=== ChannelConfig.Configs.put ===");
                logger.warn(e);
                logger.warn(e.getMessage());
                logger.warn(String.format("Channel: %s ; Name: %s", config.getOrder_channel_id(), config.getCfg_name()));
            }
        }
    }

    /**
     * 专用存储单个渠道的所有配置信息
     *
     * @author Jonas
     */
    private static class ConfigMap extends HashMap<ChannelConfigEnums.Name, List<OrderChannelConfigBean>> {
        private static final long serialVersionUID = 1L;

        public void put(ChannelConfigEnums.Name name, OrderChannelConfigBean config) {
            if (!containsKey(name)) {
                super.put(name, new ArrayList<OrderChannelConfigBean>());
            }

            get(name).add(config);
        }
    }

    /**
     * 获取指定订单渠道、发货渠道的折扣信息
     *
     * @param orderChannelId 订单渠道
     * @param shipChannel 发货渠道
     * @return OrderChannel
     */
    public static BigDecimal getDiscountRate(String orderChannelId, String shipChannel) {
        if (discountRates == null) {
            logger.error("====== ChannelConfig 没有初始化，必须先调用 init");
            return null;
        }

        String rate = "";
        for (DiscountRateBean discountRate : discountRates) {
            if (discountRate.getOrder_channel_id().equals(orderChannelId) &&
                    discountRate.getShip_channel().equals(shipChannel)) {
                rate = discountRate.getDiscount_rate();
                break;
            }
        }

        if (StringUtils.isNullOrBlank2(rate)) {
            return null;
        } else {
            return new BigDecimal(rate);
        }

    }
}
