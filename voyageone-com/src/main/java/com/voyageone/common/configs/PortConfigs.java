package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.PortConfigEnums;
import com.voyageone.common.configs.beans.PortConfigBean;
import com.voyageone.common.configs.dao.PortConfigDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 访问 tm_port_configs 表配置
 * Created by Jonas on 4/16/2015.
 */
public class PortConfigs {
    private static Configs configs;

    private static Log logger = LogFactory.getLog(PortConfigs.class);

    /**
     * 初始化港口相关的基本信息和配置信息
     *
     * @param portConfigDao 用于获取基本信息
     */
    public static void init(PortConfigDao portConfigDao) {
        if (configs == null) {
            List<PortConfigBean> all = portConfigDao.getAll();

            logger.info("PortConfig 读取数量: " + all.size());

            configs = new Configs(all);
        }

    }

    /**
     * 获取指定港口的第一个配置参数
     *
     * @param id   港口ID
     * @param name 配置名称
     * @return String
     */
    public static String getVal1(String id, PortConfigEnums.Name name) {
        List<PortConfigBean> beans = getConfigs(id, name);
        return (beans == null || beans.size() < 1) ? "" : beans.get(0).getCfg_val1();
    }

    /**
     * 获取指定港口、配置名称、第一个参数的第二个参数
     *
     * @param id   港口ID
     * @param name 配置名称
     * @param val1 第一个参数
     * @return String
     */
    public static String getVal2(String id, PortConfigEnums.Name name, String val1) {
        List<PortConfigBean> beans = getConfigs(id, name);
        if (beans == null || beans.size() < 1) {
            return "";
        }
        String val2 = "";
        for (PortConfigBean PortConfig : beans) {
            if (val1.equals(PortConfig.getCfg_val1())) {
                val2 = PortConfig.getCfg_val2();
                break;
            }
        }
        return val2;
    }


    /**
     * 获取指定港口的一个配置的所有参数
     *
     * @param id   港口ID
     * @param name 配置名称
     * @return List<PortConfig>
     */
    public static List<PortConfigBean> getConfigs(String id, PortConfigEnums.Name name) {
        if (configs == null) return null;

        if (!configs.containsKey(id)) return null;

        ConfigMap map = configs.get(id);

        if (!map.containsKey(name)) return null;

        return map.get(name);
    }

    /**
     * 专用存储所有港口的配置信息
     *
     * @author Jonas
     */
    private static class Configs extends HashMap<String, ConfigMap> {
        private static final long serialVersionUID = 1L;

        public Configs(List<PortConfigBean> beans) {
            for (PortConfigBean bean : beans) {
                put(bean);
            }
        }

        public void put(PortConfigBean config) {
            String port = config.getPort();
            ConfigMap map = containsKey(port) ? get(port) : new ConfigMap();

            try {
                PortConfigEnums.Name name = PortConfigEnums.Name.valueOf(config.getCfg_name());
                map.put(name, config);
                put(port, map);
            } catch (IllegalArgumentException e) {
                logger.warn(String.format("PortConfig 枚举匹配警告: [%s] NO \"%s\"", config.getPort(), config.getCfg_name()));
            }
        }
    }

    /**
     * 专用存储单个港口的所有配置信息
     *
     * @author Jonas
     */
    private static class ConfigMap extends HashMap<PortConfigEnums.Name, List<PortConfigBean>> {
        private static final long serialVersionUID = 1L;

        public void put(PortConfigEnums.Name name, PortConfigBean config) {
            if (!containsKey(name)) {
                super.put(name, new ArrayList<>());
            }

            get(name).add(config);
        }
    }
}
