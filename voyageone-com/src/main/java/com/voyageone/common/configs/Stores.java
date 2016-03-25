package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.StoreConfigDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 访问 wms_mt_store 表配置
 * Created by Jack on 6/4/2015.
 */
public class Stores {
    private static final Log logger = LogFactory.getLog(Stores.class);

    /* redis key */
    public static final String KEY = CacheKeyEnums.ConfigData_StoreConfigs.toString();

    private static HashOperations<String, String, StoreBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
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
            logger.info("store 读取数量: " + hashOperations.size(KEY));
        }

    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(long storeId, String channelId) {
        return storeId + CacheHelper.SKIP + channelId;
    }

    /**
     * 获取指定渠道、仓库ID的基本信息
     *
     * @param order_channel_id 订单渠道
     * @param store_id         仓库ID
     * @return StoreBean
     */
    public static StoreBean getStore(String order_channel_id, long store_id) {
        return hashOperations.get(KEY, buildKey(store_id, order_channel_id));
    }

    /**
     * 获取指定仓库ID的基本信息
     *
     * @param store_id 仓库ID
     * @return StoreBean
     */
    public static StoreBean getStore(long store_id) {
        Set<String> keySet = hashOperations.keys(KEY);
        if(CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(buildKey(store_id,""))) keyList.add(k);
        });
        Collections.sort(keyList);
        List<StoreBean> beans=hashOperations.multiGet(KEY,keyList);
        return CollectionUtils.isEmpty(beans)?null:beans.get(0);
    }

    /**
     * 获取指定渠道的仓库信息
     *
     * @param order_channel_id 订单渠道
     * @return List<StoreBean>
     */
    public static List<StoreBean> getChannelStoreList(String order_channel_id) {
        Set<String> keySet = hashOperations.keys(KEY);
        if(CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.endsWith(CacheHelper.SKIP+order_channel_id)) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY,keyList);
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
        if (defaultAll) channelStoreList.add(new StoreBean() {{
            setStore_id(0);
            setStore_name("ALL");
        }});

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
        if (defaultAll) storeList.add(new StoreBean() {{
            setStore_id(0);
            setStore_name("ALL");
        }});

        beans.forEach(bean -> {
            if (includeVirtual || (StoreConfigEnums.Kind.REAL.getId().equals(bean.getStore_kind())))
                storeList.add(bean);
        });
        return storeList;
    }

    private static List<StoreBean> getAllStores() {
        List<StoreBean> beans = hashOperations.values(KEY);
        return CollectionUtils.isEmpty(beans) ? new ArrayList<StoreBean>() : beans
                .stream()
                .sorted((a,b)->{
                    if(a.getOrder_channel_id().equals(b.getOrder_channel_id())) return a.getStore_id()>b.getStore_id()?1:-1;
                    return a.getOrder_channel_id().compareTo(b.getOrder_channel_id());
                })
                .collect(Collectors.toList());
    }

}
