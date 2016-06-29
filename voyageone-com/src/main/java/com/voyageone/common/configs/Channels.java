package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.OrderChannelDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author aooer 2016/3/17.
 * @version 2.0.0
 * @since 2.0.0
 */
public class Channels {

    private static final Class selfClass = Channels.class;

    private static final Logger logger = LoggerFactory.getLogger(selfClass);

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
     * 让缓存失效
     */
    public static void invalidate(){
        CacheHelper.delete(KEY);
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

    /**
     * 获取所有 Channel Id及Name
     *
     * @return Set
     */
    public static List<OrderChannelBean> getChannelList() {
        return CacheHelper.getAllBeans(KEY, selfClass);
    }


    public static boolean isUsJoi(String org_channel_id) {
        return getChannelList().stream()
                .filter(orderChannelBean -> orderChannelBean.getIs_usjoi() == 1 && orderChannelBean.getOrder_channel_id().equalsIgnoreCase(org_channel_id))
                .toArray().length > 0;
    }

    public static List<OrderChannelBean> getUsJoiChannelList() {
        return getChannelList().stream()
                .filter(orderChannelBean -> orderChannelBean.getIs_usjoi() == 1).collect(Collectors.toList());
    }
}
