package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.OrderChannelDao;
import com.voyageone.common.redis.CacheHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author aooer 2016/3/17.
 * @version 2.0.0
 * @since 2.0.0
 */
public class Channels {

    private static final Class selfClass = Channels.class;

    private static final Log logger = LogFactory.getLog(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_OrderChannelConfigs.toString();

    public static void reload() {
        OrderChannelDao orderChannelDao = ConfigDaoFactory.getOrderChannelDao();
        Map<String, OrderChannelBean> orderChannelBeanMap = new HashMap<>();
        orderChannelDao.getAll().forEach(
                bean ->
                    orderChannelBeanMap.put(
                            bean.getOrder_channel_id(),
                            bean
                    )
        );
        CacheHelper.reFreshSSB(KEY, orderChannelBeanMap);
        logger.info("orderChannel 读取数量: " + CacheHelper.getSize(KEY));
    }
    /**
     * 获取指定渠道的基本信息
     *
     * @param id 渠道ID
     * @return OrderChannel
     */
    public static OrderChannelBean getChannel(String id) {
        return CacheHelper.getBean(KEY, id, selfClass);
    }

    /**
     * 获取所有 Channel Id
     *
     * @return Set
     */
    public static Set<String> getChannelIdSet() {
        return CacheHelper.getKeySet(KEY, selfClass);
    }

}
