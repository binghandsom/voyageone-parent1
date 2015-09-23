package com.voyageone.common.configs;

import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.configs.dao.ThirdPartConfigDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 访问 tm_order_channel 和 tm_order_channel_configs 表配置
 * Created by Jonas on 06/26/2015.
 */
public class ThirdPartyConfigs {

    private static HashMap<String, List<ThirdPartyConfigBean>> thirdPartyMap;

    private static Log logger = LogFactory.getLog(ThirdPartyConfigs.class);

    /**
     * 初始化渠道相关的基本信息和配置信息
     *
     */
    public static void init(ThirdPartConfigDao thirdPartConfigDao) {
        if (thirdPartyMap == null) {
            List<ThirdPartyConfigBean> beans = thirdPartConfigDao.getAll();

            logger.info("ShopConfig 读取数量: " + beans.size());

            thirdPartyMap = new HashMap<>();
            List<ThirdPartyConfigBean> childList;

            for (ThirdPartyConfigBean bean : beans) {
                childList = thirdPartyMap.get(bean.getChannel_id());

                if (childList == null) thirdPartyMap.put(bean.getChannel_id(), childList = new ArrayList<>());

                childList.add(bean);
            }
        }


    }

    /**
     * 获取 对应渠道对应属性的配置
     *
     * @param channelId 渠道ID
     * @param propName 属性名
     * @return ThirdPartyConfigBean
     */
    public static ThirdPartyConfigBean getThirdPartyConfig(String channelId,String propName) {
        List<ThirdPartyConfigBean> configList = thirdPartyMap.get(channelId);

        for (ThirdPartyConfigBean bean : configList) {
            if (propName.equals(bean.getProp_name())) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 获取 对应渠道对应属性的配置集合
     *
     * @param channelId 渠道ID
     * @param propName 属性名
     * @return ThirdPartyConfigBean
     */
    public static List<ThirdPartyConfigBean> getThirdPartyConfigList(String channelId,String propName) {
        List<ThirdPartyConfigBean> configList = thirdPartyMap.get(channelId);
        List<ThirdPartyConfigBean> thirdPartyConfigList = new ArrayList<>();
        for (ThirdPartyConfigBean bean : configList) {
            if (propName.equals(bean.getProp_name())) {
                thirdPartyConfigList.add(bean);
            }
        }
        return thirdPartyConfigList;
    }

    public static HashMap<String,ThirdPartyConfigBean> getThirdPartyConfigMap(String channelId) {
        List<ThirdPartyConfigBean> configList = thirdPartyMap.get(channelId);
        HashMap<String,ThirdPartyConfigBean> thirdPartyConfigMap = new HashMap<String,ThirdPartyConfigBean>();

        for (ThirdPartyConfigBean bean : configList) {
            if (channelId.equals(bean.getChannel_id())) {
                thirdPartyConfigMap.put(bean.getProp_name(),bean);
            }
        }
        return thirdPartyConfigMap;
    }


    /**
     * 返回这个渠道的第一个配置
     * @param channelId
     * @return
     */
    public static ThirdPartyConfigBean getFirstConfig(String channelId) {
        List<ThirdPartyConfigBean> configList = thirdPartyMap.get(channelId);

        return configList != null && configList.size() > 0
                ? configList.get(0)
                : null;
    }

    /**
     * 返回对应渠道的所有配置
     * @param channelId
     * @return
     */
    public static List<ThirdPartyConfigBean> getConfigList(String channelId) {
        List<ThirdPartyConfigBean> configList = thirdPartyMap.get(channelId);

        return configList;
    }

    /**
     * 返回对应渠道的所有配置
     * @param channelId
     * @return
     */
    public static String getVal1(String channelId,String propName) {
        List<ThirdPartyConfigBean> configList = thirdPartyMap.get(channelId);

        for (ThirdPartyConfigBean bean : configList) {
            if (propName.equals(bean.getProp_name())) {
                return bean.getProp_val1();
            }
        }
        return null;
    }
}
