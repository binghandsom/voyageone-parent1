package com.voyageone.common.configscache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.beans.StoreConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.StoreConfigDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aooer 2016/3/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class StoreConfigConfigs {

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_StoreConfigConfigs.toString();

    private static HashOperations<String, String, StoreConfigBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            StoreConfigDao storeConfigDao = ConfigDaoFactory.getStoreConfigDao();
            Map<String, StoreConfigBean> storeConfigBeanMap = new HashMap<>();
            storeConfigDao.getAllConfigs().forEach(
                    bean -> {
                        storeConfigBeanMap.put(
                                StoreConfigConfigs.buildKey(
                                        bean.getStore_id(), StoreConfigEnums.Name.valueOf(bean.getCfg_name()), bean.getCfg_val1()),
                                bean
                        );
                    }
            );
            CacheHelper.reFreshSSB(KEY, storeConfigBeanMap);
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(long storeId, StoreConfigEnums.Name name, String val1) {
        return storeId + CacheHelper.SKIP + name + CacheHelper.SKIP + val1;
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
        beans.sort((a, b) -> a.getCfg_val1().compareTo(b.getCfg_val1()));
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
        StoreConfigBean bean = hashOperations.get(KEY, buildKey(id, name, val1));
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
        Set<String> keySet=hashOperations.keys(KEY);
        if(CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(buildKey(id,name,""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY,keyList);
    }

}
