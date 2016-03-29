package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.ThirdPartConfigDao;
import com.voyageone.common.redis.CacheHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 访问 tm_order_channel 和 tm_order_channel_configs 表配置
 * Created by Jonas on 06/26/2015.
 */
public class ThirdPartyConfigs {
    private static final Class selfClass = ThirdPartyConfigs.class;

    private static final Log logger = LogFactory.getLog(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_ThirdPartyConfigs.toString();

    public static void reload() {
        ThirdPartConfigDao thirdPartConfigDao = ConfigDaoFactory.getThirdPartConfigDao();
        Map<String, ThirdPartyConfigBean> thirdPartConfigBeanMap = new HashMap<>();
        thirdPartConfigDao.getAll().forEach(
                bean ->
                    thirdPartConfigBeanMap.put(
                            buildKey(bean.getChannel_id(), bean.getProp_name(), bean.getSeq()),
                            bean
                    )
        );
        CacheHelper.reFreshSSB(KEY, thirdPartConfigBeanMap);
        logger.info("thirdPartConfig 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String channelId, String propName, String seq) {
        return channelId + CacheKeyEnums.SKIP + propName + CacheKeyEnums.SKIP + seq;
    }

    /**
     * 获取 对应渠道对应属性的配置
     *
     * @param channelId 渠道ID
     * @param propName  属性名
     * @return ThirdPartyConfigBean
     */
    public static ThirdPartyConfigBean getThirdPartyConfig(String channelId, String propName) {
        List<ThirdPartyConfigBean> beans = getThirdPartyConfigList(channelId,propName);
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
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if(CollectionUtils.isEmpty(keySet)) {
            return null;
        }

        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(buildKey(channelId, propName, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY,keyList, selfClass);
    }

    public static HashMap<String, ThirdPartyConfigBean> getThirdPartyConfigMap(String channelId) {
        HashMap<String, ThirdPartyConfigBean> thirdPartyConfigMap = new HashMap<>();
        List<ThirdPartyConfigBean> configList = getConfigList(channelId);
        if (configList != null) {
            for (ThirdPartyConfigBean bean: configList) {
                thirdPartyConfigMap.put(bean.getProp_name(), bean);
            }
        }
        return thirdPartyConfigMap;
    }


    /**
     * 返回这个渠道的第一个配置
     */
    public static ThirdPartyConfigBean getFirstConfig(String channelId) {
        List<ThirdPartyConfigBean> configList = getConfigList(channelId);
        if(CollectionUtils.isEmpty(configList)) return null;
        return configList.get(0);
    }

    /**
     * 返回对应渠道的所有配置
     */
    public static List<ThirdPartyConfigBean> getConfigList(String channelId) {
        Set<String> keySet= CacheHelper.getKeySet(KEY, selfClass);
        if(CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(channelId+ CacheKeyEnums.SKIP)) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY,keyList, selfClass);
    }

    /**
     * 返回对应渠道的所有配置
     */
    public static String getVal1(String channelId, String propName) {
        ThirdPartyConfigBean thirdPartyConfigBean = getThirdPartyConfig(channelId, propName);
        return thirdPartyConfigBean == null ? "" : thirdPartyConfigBean.getProp_val1();
    }
}
