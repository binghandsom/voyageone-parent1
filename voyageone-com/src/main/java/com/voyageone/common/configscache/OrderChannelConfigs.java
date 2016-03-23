package com.voyageone.common.configscache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.OrderChannelDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author aooer 2016/3/17.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderChannelConfigs {

    private static final Logger log = LoggerFactory.getLogger(OrderChannelConfigs.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_OrderChannelConfigs.toString();

    private static HashOperations<String, String, OrderChannelBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            OrderChannelDao orderChannelDao = ConfigDaoFactory.getOrderChannelDao();
            Map<String, OrderChannelBean> orderChannelBeanMap = new HashMap<>();
            orderChannelDao.getAll().forEach(
                    bean -> {
                        orderChannelBeanMap
                                .put(
                                        OrderChannelConfigs.buildKey(bean.getOrder_channel_id()),
                                        bean
                                );
                    }
            );
            CacheHelper.reFreshSSB(KEY, orderChannelBeanMap);
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String id) {
        return id+CacheHelper.SKIP;
    }

    /**
     * 获取指定渠道的基本信息
     *
     * @param id 渠道ID
     * @return OrderChannel
     */
    public static OrderChannelBean getChannel(String id) {
        return hashOperations.get(KEY, buildKey(id));
    }

    /**
     * 获取所有 Channel Id
     *
     * @return Set
     */
    public static Set<String> getChannelIdSet() {
        return hashOperations.keys(KEY);
    }

}
