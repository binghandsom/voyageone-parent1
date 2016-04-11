package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.CarrierEnums;
import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.configs.beans.UsJoiBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/4/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class UsJois {

    private static final Class selfClass = UsJois.class;

    private static final Logger log = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_UsJois.toString();

    /**
     * 初始化相关的基本信息和配置信息
     */
    public static void reload() {
        List<UsJoiBean> beans = ConfigDaoFactory.getUsJoiDao().getAll();
        Map<String, UsJoiBean> beanmap = new HashMap<>();
        beans.forEach(bean -> beanmap.put(buildKey(bean.getSub_channel_id()), bean));
        CacheHelper.reFreshSSB(KEY, beanmap);
        log.info("UsJoiBean 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String sub_channel_id) {
        return sub_channel_id;
    }

    public static boolean isExists(String sub_channel_id){
        return CacheHelper.isExists(KEY,buildKey(sub_channel_id),selfClass);
    }

    public static UsJoiBean getUsJoiByOrgChannelId(String org_channel_id){
        return CacheHelper.getBean(KEY,buildKey(org_channel_id),selfClass);
    }

}
