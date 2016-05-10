package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.StoreConfigDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 访问 wms_mt_store 表配置
 * Created by Jack on 6/4/2015.
 */
public class Stores {
    private static final Class selfClass = Stores.class;

    private final static Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    public static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_StoreConfigs.toString();
    public static void reload() {
        StoreConfigDao storeConfigDao = ConfigDaoFactory.getStoreConfigDao();
        Map<String, StoreBean> storeBeanMap = new HashMap<>();
        storeConfigDao.getAll().forEach(
                bean -> {
                    storeBeanMap.put(
                            buildKey(bean.getStore_id(), bean.getOrder_channel_id()),
                            bean
                    );
                }
        );
        CacheHelper.reFreshSSB(KEY, storeBeanMap);
        logger.info("store 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(long storeId, String channelId) {
        return storeId + CacheKeyEnums.SKIP + channelId;
    }

    /**
     * 获取指定渠道、仓库ID的基本信息
     *
     * @param order_channel_id 订单渠道
     * @param store_id         仓库ID
     * @return StoreBean
     */
    public static StoreBean getStore(String order_channel_id, long store_id) {
        return CacheHelper.getBean(KEY, buildKey(store_id, order_channel_id), selfClass);
    }

    /**
     * 获取指定仓库ID的基本信息
     *
     * @param store_id 仓库ID
     * @return StoreBean
     */
    public static StoreBean getStore(long store_id) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if(CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(buildKey(store_id,""))) keyList.add(k);
        });
        Collections.sort(keyList);
        List<StoreBean> beans= CacheHelper.getBeans(KEY, keyList, selfClass);
        return CollectionUtils.isEmpty(beans)?null:beans.get(0);
    }

    /**
     * 获取指定渠道的仓库信息
     *
     * @param order_channel_id 订单渠道
     * @return List<StoreBean>
     */
    public static List<StoreBean> getChannelStoreList(String order_channel_id) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if(CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.endsWith(CacheKeyEnums.SKIP+order_channel_id)) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY, keyList, selfClass);
    }

    /**
     * 获取指定渠道的仓库信息
     *
     * @param order_channel_id 订单渠道
     * @param defaultAll       是否包含All选项
     * @param includeVirtual   是否包含虚拟仓库
     * @return List<StoreBean>
     */
    public static List<StoreBean> getChannelStoreList(String order_channel_id, boolean defaultAll, boolean includeVirtual) {
        List<StoreBean> beans = getChannelStoreList(order_channel_id);
        if (CollectionUtils.isEmpty(beans)) return null;

        List<StoreBean> channelStoreList = new ArrayList<>();
        // 是否有ALL选项
        if (defaultAll) {
            StoreBean bean = new StoreBean();
            bean.setStore_id(0);
            bean.setStore_name("ALL");
            channelStoreList.add(bean);
        }

        beans.forEach(bean -> {
            if (includeVirtual || (StoreConfigEnums.Kind.REAL.getId().equals(bean.getStore_kind())))
                channelStoreList.add(bean);
        });
        return channelStoreList;
    }

    /**
     * 获取所有渠道的仓库信息
     *
     * @return List<StoreBean>
     */
    public static List<StoreBean> getStoreList() {
        return getAllStores();
    }

    /**
     * 获取指定渠道的仓库信息
     *
     * @param defaultAll     是否包含All选项
     * @param includeVirtual 是否包含虚拟仓库
     * @return List<StoreBean>
     */
    public static List<StoreBean> getStoreList(boolean defaultAll, boolean includeVirtual) {
        List<StoreBean> beans = getAllStores();
        if (CollectionUtils.isEmpty(beans)) return null;

        List<StoreBean> storeList = new ArrayList<>();
        // 是否有ALL选项
        if (defaultAll) {
            StoreBean bean = new StoreBean();
            bean.setStore_id(0);
            bean.setStore_name("ALL");
            storeList.add(bean);
        }

        beans.forEach(bean -> {
            if (includeVirtual || (StoreConfigEnums.Kind.REAL.getId().equals(bean.getStore_kind())))
                storeList.add(bean);
        });
        return storeList;
    }

    private static List<StoreBean> getAllStores() {
        List<StoreBean> beans = CacheHelper.getAllBeans(KEY, selfClass);
        return CollectionUtils.isEmpty(beans) ? new ArrayList<>() : beans
                .stream()
                .sorted((a,b)->{
                    if(a.getOrder_channel_id().equals(b.getOrder_channel_id())) return a.getStore_id()>b.getStore_id()?1:-1;
                    return a.getOrder_channel_id().compareTo(b.getOrder_channel_id());
                })
                .collect(Collectors.toList());
    }

}
