package com.voyageone.common.configs.dao;

import com.voyageone.common.spring.SpringContext;
import org.springframework.stereotype.Component;

/**
 * @author aooer 2016/3/21.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class ConfigDaoFactory {

    public static CarrierDao getCarrierDao() {
        return SpringContext.getBean(CarrierDao.class);
    }

    public static OrderChannelDao getOrderChannelDao() {
        return SpringContext.getBean(OrderChannelDao.class);
    }

    public static OrderChannelConfigDao getOrderChannelConfigDao() {
        return SpringContext.getBean(OrderChannelConfigDao.class);
    }

    public static CmsChannelConfigDao getCmsChannelConfigDao() {
        return SpringContext.getBean(CmsChannelConfigDao.class);
    }

    public static CodeDao getCodeDao() {
        return SpringContext.getBean(CodeDao.class);
    }

    public static FeedDao getFeedDao() {
        return SpringContext.getBean(FeedDao.class);
    }

    public static ImsCategoryDao getImsCategoryDao() {
        return SpringContext.getBean(ImsCategoryDao.class);
    }

    public static PortConfigDao getPortConfigDao() {
        return SpringContext.getBean(PortConfigDao.class);
    }

    public static ShopDao getShopDao() {
        return SpringContext.getBean(ShopDao.class);
    }

    public static ShopConfigDao getShopConfigDao() {
        return SpringContext.getBean(ShopConfigDao.class);
    }

    public static StoreConfigDao getStoreConfigDao() {
        return SpringContext.getBean(StoreConfigDao.class);
    }

    public static ThirdPartConfigDao getThirdPartConfigDao() {
        return SpringContext.getBean(ThirdPartConfigDao.class);
    }

    public static TypeDao getTypeDao() {
        return SpringContext.getBean(TypeDao.class);
    }

    public static TypeChannelDao getChannelValueDao() {
        return SpringContext.getBean(TypeChannelDao.class);
    }
}
