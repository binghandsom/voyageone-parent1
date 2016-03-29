package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.CarrierEnums;
import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.redis.CacheHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 访问 tm_carrier、tm_carrier_channel 表配置
 * Created by Jack on 9/27/2015.
 */
public class Carriers {

    private static final Class selfClass = Carriers.class;

    private static final Log logger = LogFactory.getLog(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_CarrierConfigs.toString();
    /**
     * 初始化相关的基本信息和配置信息
     */
    public static void reload() {
        List<CarrierBean> allCarriers = ConfigDaoFactory.getCarrierDao().getAll();
        Map<String, CarrierBean> carrierBeanMap = new HashMap<>();
        allCarriers.forEach(bean->carrierBeanMap.put(buildKey(bean.getOrder_channel_id(), bean.getCarrier()), bean));
        CacheHelper.reFreshSSB(KEY, carrierBeanMap);
        logger.info("Carrier 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String order_channel_id, String name) {
        return order_channel_id + CacheKeyEnums.SKIP + name;
    }

    /**
     * 获取指定订单渠道、快递的配置参数
     *
     * @param order_channel_id 订单渠道
     * @param name             快递名称
     * @return CarrierBean
     */
    public static CarrierBean getCarrier(String order_channel_id, CarrierEnums.Name name) {
        return CacheHelper.getBean(KEY, buildKey(order_channel_id, name.toString()), selfClass);
    }

    /**
     * 获取订单渠道的所有快递参数
     *
     * @param order_channel_id 订单渠道
     * @return CarrierBean
     */
    public static List<CarrierBean> getCarrier(String order_channel_id) {
        List<CarrierBean> carriersList = new ArrayList<>();
        Set<String> keys = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keys)) {
            logger.warn("未初始化CarrierBean");
            return carriersList;
        }
        List<String> filterKeys = new ArrayList<>();
        for (String key : keys) {
            if (key.startsWith(order_channel_id+ CacheKeyEnums.SKIP)) filterKeys.add(key);
        }
        if (!filterKeys.isEmpty()) {
            Collections.sort(filterKeys);
            carriersList = CacheHelper.getBeans(KEY, filterKeys, selfClass);
        }
        return carriersList;
    }

}
