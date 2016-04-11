package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.beans.StoreConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.StoreConfigDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author aooer 2016/3/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class StoreConfigs {
    private static final Class selfClass = StoreConfigs.class;

    private final static Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_StoreConfigConfigs.toString();

    public static void reload() {
        StoreConfigDao storeConfigDao = ConfigDaoFactory.getStoreConfigDao();
        Map<String, StoreConfigBean> storeConfigBeanMap = new HashMap<>();
        storeConfigDao.getAllConfigs().forEach(
                bean ->
                    storeConfigBeanMap.put(
                            buildKey(bean.getStore_id(), bean.getCfg_name(), bean.getCfg_val1()),
                            bean
                    )
        );
        CacheHelper.reFreshSSB(KEY, storeConfigBeanMap);
        logger.info("storeConfig 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(long storeId, String name, String val1) {
        return storeId + CacheKeyEnums.SKIP + name + CacheKeyEnums.SKIP + val1;
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
        if (CollectionUtils.isEmpty(beans)) return "";

        return beans.get(0).getCfg_val1();
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
        StoreConfigBean bean = CacheHelper.getBean(KEY, buildKey(id, name.toString(), val1), selfClass);
        return bean == null ? null : bean.getCfg_val2();
    }

    /**
     * 获取指定仓库的一个配置的所有参数
     *
     * @param id   仓库ID
     * @param name 配置名称
     * @return List<StoreConfig>
     */
    public static List<StoreConfigBean> getConfigs(long id, StoreConfigEnums.Name name) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if(CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(buildKey(id,name.toString(),""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY, keyList, selfClass);
    }

}
