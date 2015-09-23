package com.voyageone.batch.bi.configs;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kylin on 2015/7/17.
 */
public class DriverConfigs {
    private static final Log logger = LogFactory.getLog(DriverConfigs.class);

    private static Drivers drivers;

    /**
     * 初始化店铺相关的Drive信息
     *
     * @param fireFoxDriverService 用于获取配置信息
     */
    public static void init(FireFoxDriverService fireFoxDriverService) {
        try {
            if (drivers == null) {
                List<DriverConfigBean> all = fireFoxDriverService.initialLoginFireFoxDriverAll();

                logger.info("DriverConfig 读取数量: " + all.size());

                drivers = new Drivers(all);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 专用存储店铺Drive信息
     *
     * @author Jack
     */
    private static class Drivers extends HashMap<Integer, DriverConfigBean> {
        private static final long serialVersionUID = 1L;

        public Drivers(List<DriverConfigBean> beans) {
            for (DriverConfigBean bean : beans) {
                put(bean.getShopBean().getShop_id(), bean);
            }
        }
    }


    /**
     * 获取指定店铺的Driver
     *
     * @return ShopBean
     */
    public static DriverConfigBean getDriver(int ShopID) {
        if (drivers == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }

        return drivers.get(ShopID);
    }
}
