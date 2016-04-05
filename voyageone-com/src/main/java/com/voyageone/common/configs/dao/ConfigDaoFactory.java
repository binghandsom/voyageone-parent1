package com.voyageone.common.configs.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author aooer 2016/3/21.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class ConfigDaoFactory {

    private static CarrierDao carrierDao;
    private static OrderChannelDao orderChannelDao;
    private static OrderChannelConfigDao orderChannelConfigDao;
    private static CmsChannelConfigDao cmsChannelConfigDao;
    private static CodeDao codeDao;
    private static FeedDao feedDao;
    private static ImsCategoryDao imsCategoryDao;
    private static PortConfigDao portConfigDao;
    private static ShopDao shopDao;
    private static ShopConfigDao shopConfigDao;
    private static StoreConfigDao storeConfigDao;
    private static ThirdPartConfigDao thirdPartConfigDao;
    private static TypeDao typeDao;
    private static TypeChannelDao channelValueDao;
    private static UsJoiDao usJoiDao;


    @Autowired
    private void setCarrierDao(CarrierDao carrierDao) {
        ConfigDaoFactory.carrierDao = carrierDao;
    }
    @Autowired
    private void setOrderChannelDao(OrderChannelDao orderChannelDao) {
        ConfigDaoFactory.orderChannelDao = orderChannelDao;
    }
    @Autowired
    private void setOrderChannelConfigDao(OrderChannelConfigDao orderChannelConfigDao) {
        ConfigDaoFactory.orderChannelConfigDao = orderChannelConfigDao;
    }
    @Autowired
    private void setCmsChannelConfigDao(CmsChannelConfigDao cmsChannelConfigDao) {
        ConfigDaoFactory.cmsChannelConfigDao = cmsChannelConfigDao;
    }
    @Autowired
    private void setCodeDao(CodeDao codeDao) {
        ConfigDaoFactory.codeDao = codeDao;
    }
    @Autowired
    private void setFeedDao(FeedDao feedDao) {
        ConfigDaoFactory.feedDao = feedDao;
    }
    @Autowired
    private void setImsCategoryDao(ImsCategoryDao imsCategoryDao) {
        ConfigDaoFactory.imsCategoryDao = imsCategoryDao;
    }
    @Autowired
    private void setPortConfigDao(PortConfigDao portConfigDao) {
        ConfigDaoFactory.portConfigDao = portConfigDao;
    }
    @Autowired
    private void setShopDao(ShopDao shopDao) {
        ConfigDaoFactory.shopDao = shopDao;
    }
    @Autowired
    private void setShopConfigDao(ShopConfigDao shopConfigDao) {
        ConfigDaoFactory.shopConfigDao = shopConfigDao;
    }
    @Autowired
    private void setStoreConfigDao(StoreConfigDao storeConfigDao) {
        ConfigDaoFactory.storeConfigDao = storeConfigDao;
    }
    @Autowired
    private void setThirdPartConfigDao(ThirdPartConfigDao thirdPartConfigDao) {
        ConfigDaoFactory.thirdPartConfigDao = thirdPartConfigDao;
    }
    @Autowired
    private void setTypeDao(TypeDao typeDao) {
        ConfigDaoFactory.typeDao = typeDao;
    }
    @Autowired
    private void setChannelValueDao(TypeChannelDao channelValueDao) {
        ConfigDaoFactory.channelValueDao = channelValueDao;
    }
    @Autowired
    private void setUsJoiDao(UsJoiDao usJoiDao) {
        ConfigDaoFactory.usJoiDao = usJoiDao;
    }

    public static UsJoiDao getUsJoiDao() {
        return usJoiDao;
    }

    public static CarrierDao getCarrierDao() {
        return carrierDao;
    }

    public static OrderChannelDao getOrderChannelDao() {
        return orderChannelDao;
    }

    public static OrderChannelConfigDao getOrderChannelConfigDao() {
        return orderChannelConfigDao;
    }

    public static CmsChannelConfigDao getCmsChannelConfigDao() {
        return cmsChannelConfigDao;
    }

    public static CodeDao getCodeDao() {
        return codeDao;
    }

    public static FeedDao getFeedDao() {
        return feedDao;
    }

    public static ImsCategoryDao getImsCategoryDao() {
        return imsCategoryDao;
    }

    public static PortConfigDao getPortConfigDao() {
        return portConfigDao;
    }

    public static ShopDao getShopDao() {
        return shopDao;
    }

    public static ShopConfigDao getShopConfigDao() {
        return shopConfigDao;
    }

    public static StoreConfigDao getStoreConfigDao() {
        return storeConfigDao;
    }

    public static ThirdPartConfigDao getThirdPartConfigDao() {
        return thirdPartConfigDao;
    }

    public static TypeDao getTypeDao() {
        return typeDao;
    }

    public static TypeChannelDao getChannelValueDao() {
        return channelValueDao;
    }
}
