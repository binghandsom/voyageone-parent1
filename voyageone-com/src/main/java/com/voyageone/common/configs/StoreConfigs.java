package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.beans.StoreConfigBean;
import com.voyageone.common.configs.dao.StoreConfigDao;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 访问 wms_mt_store 表配置
 * Created by Jack on 6/4/2015.
 */
public class StoreConfigs {

    private static Configs configs;
    private static List<StoreBean> allStores;
    private static final Log logger = LogFactory.getLog(StoreConfigs.class);

    /**
     * 初始化仓库相关的基本信息和配置信息
     *
     * @param storeConfigDao       用于获取基本信息
     */
    public static void init(StoreConfigDao storeConfigDao) {

        if (configs == null) {
            List<StoreConfigBean> all = storeConfigDao.getAllConfigs();

            logger.info("StoreConfig 读取数量: " + all.size());

            configs = new Configs(all);
        }

        if (allStores == null) {
            allStores = storeConfigDao.getAll();

            logger.info("Store 读取数量: " + allStores.size());

        }
    }

    /**
     * 获取指定渠道、仓库ID的基本信息
     * @param order_channel_id 订单渠道
     * @param store_id 仓库ID
     * @return StoreBean
     */
    public static StoreBean getStore(String order_channel_id, long store_id) {
        if (allStores == null) {
            logger.error("====== StoreConfig 没有初始化，必须先调用 init");
            return null;
        }

        StoreBean resultStore = null;
        for (StoreBean storeBean : allStores){
            if (storeBean.getOrder_channel_id().equals(order_channel_id) &&
                    storeBean.getStore_id() == store_id) {
                resultStore = storeBean;
                break;
            }

        }

        return resultStore;
    }

    /**
     * 获取指定仓库ID的基本信息
     * @param store_id 仓库ID
     * @return StoreBean
     */
    public static StoreBean getStore(long store_id) {
        if (allStores == null) {
            logger.error("====== StoreConfig 没有初始化，必须先调用 init");
            return null;
        }

        StoreBean resultStore = null;
        for (StoreBean storeBean : allStores){
            if (storeBean.getStore_id() == store_id) {
                resultStore = storeBean;
                break;
            }

        }

        return resultStore;
    }

    /**
     * 获取指定渠道的仓库信息
     * @param order_channel_id 订单渠道
     * @return List<StoreBean>
     */
    public static List<StoreBean> getChannelStoreList(String order_channel_id) {
        if (allStores == null) {
            logger.error("====== StoreConfig 没有初始化，必须先调用 init");
            return null;
        }
        List<StoreBean> channelStoreList = new ArrayList<StoreBean>();
        for (StoreBean bean : allStores) {
            if (order_channel_id.equals(bean.getOrder_channel_id())) {
                channelStoreList.add(bean);
            }
        }
        return channelStoreList;
    }

    /**
     * 获取指定渠道的仓库信息
     * @param order_channel_id 订单渠道
     * @param defaultAll 是否包含All选项
     * @param includeVirtual 是否包含虚拟仓库
     * @return List<StoreBean>
     */
    public static List<StoreBean> getChannelStoreList(String order_channel_id, boolean defaultAll, boolean includeVirtual) {
        if (allStores == null) {
            logger.error("====== StoreConfig 没有初始化，必须先调用 init");
            return null;
        }

        List<StoreBean> channelStoreList = new ArrayList<>();

        // 是否有ALL选项
        if (defaultAll) {
            StoreBean channelStoreBean = new StoreBean();
            channelStoreBean.setStore_id(0);
            channelStoreBean.setStore_name("ALL");

            channelStoreList.add(channelStoreBean);
        }

        for (StoreBean bean : allStores) {
            if (order_channel_id.equals(bean.getOrder_channel_id())) {
                // 是否包含虚拟仓库
                if (includeVirtual) {
                    channelStoreList.add(bean);
                }
                else if (StoreConfigEnums.Kind.REAL.getId().equals(bean.getStore_kind())) {
                    channelStoreList.add(bean);
                }
            }
        }

        return channelStoreList;
    }

    /**
     * 获取所有渠道的仓库信息
     *
     * @return List<StoreBean>
     */
    public static List<StoreBean> getStoreList() {
        if (allStores == null) {
            logger.error("====== StoreConfig 没有初始化，必须先调用 init");
            return null;
        }
        return allStores;
    }

    /**
     * 获取指定渠道的仓库信息
     * @param defaultAll 是否包含All选项
     * @param includeVirtual 是否包含虚拟仓库
     * @return List<StoreBean>
     */
    public static List<StoreBean> getStoreList(boolean defaultAll, boolean includeVirtual) {
        if (allStores == null) {
            logger.error("====== StoreConfig 没有初始化，必须先调用 init");
            return null;
        }

        List<StoreBean> storeList = new ArrayList<>();

        // 是否有ALL选项
        if (defaultAll) {
            StoreBean storeBean = new StoreBean();
            storeBean.setStore_id(0);
            storeBean.setStore_name("ALL");

            storeList.add(storeBean);
        }

        for (StoreBean bean : allStores) {
            // 是否包含虚拟仓库
            if (includeVirtual) {
                storeList.add(bean);
            }
            else if (StoreConfigEnums.Kind.REAL.getId().equals(bean.getStore_kind())) {
                storeList.add(bean);
            }
        }
        return storeList;
    }

    /**
     * 专用存储所有仓库的配置信息
     *
     * @author Jack
     */
    private static class Configs extends HashMap<Long, ConfigMap> {
        private static final long serialVersionUID = 1L;

        public Configs(List<StoreConfigBean> beans) {
            for (StoreConfigBean bean : beans) {
                put(bean);
            }
        }

        public void put(StoreConfigBean config) {
            long id = config.getStore_id();
            ConfigMap map = containsKey(id) ? get(id) : new ConfigMap();

            try {
                StoreConfigEnums.Name name = StoreConfigEnums.Name.valueOf(config.getCfg_name());
                map.put(name, config);
                put(id, map);
            } catch (IllegalArgumentException e) {
                logger.warn("=== StoreConfig.Configs.put ===");
                logger.warn(e);
                logger.warn(e.getMessage());
                logger.warn(String.format("Store: %s ; Name: %s", config.getStore_id(), config.getCfg_name()));
            }
        }
    }

    /**
     * 专用存储单仓库的所有配置信息
     *
     * @author Jonas
     */
    private static class ConfigMap extends HashMap<StoreConfigEnums.Name, List<StoreConfigBean>> {
        private static final long serialVersionUID = 1L;

        public void put(StoreConfigEnums.Name name, StoreConfigBean config) {
            if (!containsKey(name)) {
                super.put(name, new ArrayList<StoreConfigBean>());
            }

            get(name).add(config);
        }
    }

    /**
     * 获取指定仓库的第一个配置参数
     *
     * @param id   仓库ID
     * @param name 配置名称
     * @return String
     */
    public static String getVal1(long id, StoreConfigEnums.Name name) {
        List<StoreConfigBean> beans = getConfigs(id, name);
        return (beans == null || beans.size() < 1) ? "" : beans.get(0).getCfg_val1();
    }

    /**
     * 获取指定仓库、配置名称、第一个参数的第二个参数
     *
     * @param id   仓库ID
     * @param name 配置名称
     * @param val1 第一个参数
     * @return String
     */
    public static String getVal2(long id, StoreConfigEnums.Name name, String val1) {
        List<StoreConfigBean> beans = getConfigs(id, name);
        if (beans == null || beans.size() < 1) {
            return "";
        }
        String val2 = "";
        for (StoreConfigBean StoreConfig : beans) {
            if (val1.equals(StoreConfig.getCfg_val1())) {
                val2 = StoreConfig.getCfg_val2();
                break;
            }
        }
        return val2;
    }

    /**
     * 获取指定仓库的一个配置的所有参数
     *
     * @param id   仓库ID
     * @param name 配置名称
     * @return List<StoreConfig>
     */
    public static List<StoreConfigBean> getConfigs(long id, StoreConfigEnums.Name name) {
        if (configs == null) {
            logger.error("====== StoreConfig 没有初始化，必须先调用 init");
            return null;
        }

        if (!configs.containsKey(id)) return null;

        ConfigMap map = configs.get(id);

        if (!map.containsKey(name)) return null;

        return map.get(name);
    }
}
