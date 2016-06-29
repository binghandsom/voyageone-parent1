package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.PortConfigEnums;
import com.voyageone.common.configs.beans.PortConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.PortConfigDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 访问 tm_port_configs 表配置
 * Created by Jonas on 4/16/2015.
 */
public class PortConfigs {

    private static final Class selfClass = PortConfigs.class;

    private static final Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_PortConfigs.toString();

    public static void reload() {
        PortConfigDao portConfigDao = ConfigDaoFactory.getPortConfigDao();
        Map<String, PortConfigBean> portConfigBeanMap = new HashMap<>();
        portConfigDao.getAll().forEach(
                bean ->
                    portConfigBeanMap.put(
                            buildKey(bean.getPort(), bean.getCfg_name(), bean.getSeq() + ""),
                            bean
                    )
        );
        CacheHelper.reFreshSSB(KEY, portConfigBeanMap);
        logger.info("portConfig 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String id, String name, String seq) {
        return id + CacheKeyEnums.SKIP + name + CacheKeyEnums.SKIP + seq;
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
        if (CollectionUtils.isEmpty(beans)) return "";
        return beans.get(0).getCfg_val1();
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
        if (beans != null) {
            for (PortConfigBean bean : beans)
                if (val1.equals(bean.getCfg_val1())) return bean.getCfg_val2();
        }
        return "";
    }


    /**
     * 获取指定港口的一个配置的所有参数
     *
     * @param id   港口ID
     * @param name 配置名称
     * @return List<PortConfig>
     */
    public static List<PortConfigBean> getConfigs(String id, PortConfigEnums.Name name) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(id, name.toString(), ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY, keyList, selfClass);
    }
}
