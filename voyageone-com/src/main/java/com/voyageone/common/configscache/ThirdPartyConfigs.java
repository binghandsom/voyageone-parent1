package com.voyageone.common.configscache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.ThirdPartConfigDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 访问 tm_order_channel 和 tm_order_channel_configs 表配置
 * Created by Jonas on 06/26/2015.
 */
public class ThirdPartyConfigs {

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_ThirdPartyConfigs.toString();

    private static HashOperations<String, String, ThirdPartyConfigBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            ThirdPartConfigDao thirdPartConfigDao = ConfigDaoFactory.getThirdPartConfigDao();
            Map<String, ThirdPartyConfigBean> thirdPartConfigBeanMap = new HashMap<>();
            thirdPartConfigDao.getAll().forEach(
                    bean -> {
                        thirdPartConfigBeanMap.put(
                                ThirdPartyConfigs.buildKey(bean.getChannel_id(), bean.getProp_name(), bean.getSeq()),
                                bean
                        );
                    }
            );
            CacheHelper.reFreshSSB(KEY, thirdPartConfigBeanMap);
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String channelId, String propName, String seq) {
        return channelId + CacheHelper.SKIP + propName + CacheHelper.SKIP + seq;
    }

    /**
     * 获取 对应渠道对应属性的配置
     *
     * @param channelId 渠道ID
     * @param propName  属性名
     * @return ThirdPartyConfigBean
     */
    public static ThirdPartyConfigBean getThirdPartyConfig(String channelId, String propName) {
        List<ThirdPartyConfigBean> beans=getThirdPartyConfigList(channelId,propName);
        return CollectionUtils.isEmpty(beans)?null:beans.get(0);
    }

    /**
     * 获取 对应渠道对应属性的配置集合
     *
     * @param channelId 渠道ID
     * @param propName  属性名
     * @return ThirdPartyConfigBean
     */
    public static List<ThirdPartyConfigBean> getThirdPartyConfigList(String channelId, String propName) {
        Set<String> keySet=hashOperations.keys(KEY);
        if(CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(buildKey(channelId,propName,""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY,keyList);
        //return getConfigList(channelId).stream().filter(bean -> bean.getProp_name().equals(propName)).collect(Collectors.toList());
    }

    public static HashMap<String, ThirdPartyConfigBean> getThirdPartyConfigMap(String channelId) {
        HashMap<String, ThirdPartyConfigBean> thirdPartyConfigMap = new HashMap<String, ThirdPartyConfigBean>();
        getConfigList(channelId).forEach(bean -> thirdPartyConfigMap.put(bean.getProp_name(), bean));
        return thirdPartyConfigMap;
    }


    /**
     * 返回这个渠道的第一个配置
     *
     * @param channelId
     * @return
     */
    public static ThirdPartyConfigBean getFirstConfig(String channelId) {
        List<ThirdPartyConfigBean> configList = getConfigList(channelId);
        if(CollectionUtils.isEmpty(configList)) return null;
        return configList.get(0);
    }

    /**
     * 返回对应渠道的所有配置
     *
     * @param channelId
     * @return
     */
    public static List<ThirdPartyConfigBean> getConfigList(String channelId) {
        Set<String> keySet=hashOperations.keys(KEY);
        if(CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(channelId+CacheHelper.SKIP)) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY,keyList);
        /*
        List<ThirdPartyConfigBean> beans = hashOperations.values(KEY);
        return CollectionUtils.isEmpty(beans) ? new ArrayList<>() : beans.stream().filter(bean -> bean.getChannel_id().equals(channelId)).collect(Collectors.toList());
        */
    }

    /**
     * 返回对应渠道的所有配置
     *
     * @param channelId
     * @return
     */
    public static String getVal1(String channelId, String propName) {
        ThirdPartyConfigBean thirdPartyConfigBean = getThirdPartyConfig(channelId, propName);
        return thirdPartyConfigBean == null ? "" : thirdPartyConfigBean.getProp_val1();
    }
}
