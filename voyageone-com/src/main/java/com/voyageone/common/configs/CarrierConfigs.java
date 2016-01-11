package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CarrierEnums;
import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.configs.dao.CarrierDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 访问 tm_carrier、tm_carrier_channel 表配置
 * Created by Jack on 9/27/2015.
 */
public class CarrierConfigs {

    private static Log logger = LogFactory.getLog(Carriers.class);

    private static Carriers carriers;
    private static List<CarrierBean> allCarriers;

    /**
     * 初始化相关的基本信息和配置信息
     *
     * @param carrierDao 用于获取基本信息
     */
    public static void init(CarrierDao carrierDao) {
        if (carriers == null) {
            allCarriers = carrierDao.getAll();

            logger.info("Carrier 读取数量: " + allCarriers.size());

            carriers = new Carriers(allCarriers);
        }

    }

    /**
     * 专用存储渠道基础信息
     *
     * @author Jonas
     */
    private static class Carriers extends HashMap<String,CarrierBean> {
        private static final long serialVersionUID = 1L;

        public Carriers(List<CarrierBean> beans) {
            for (CarrierBean bean : beans) {
                put(bean.getOrder_channel_id()+"_"+bean.getCarrier(), bean);
            }
        }
    }

    /**
     * 获取指定订单渠道、快递的配置参数
     *
     * @param order_channel_id   订单渠道
     * @param name 快递名称
     * @return CarrierBean
     */
    public static CarrierBean getCarrier(String order_channel_id, CarrierEnums.Name name) {
        if (carriers == null) {
            logger.error("====== CarrierConfig 没有初始化，必须先调用 init");
            return null;
        }

        return carriers.get(order_channel_id + "_" + name.toString());
    }

    /**
     * 获取订单渠道的所有快递参数
     *
     * @param order_channel_id   订单渠道
      * @return CarrierBean
     */
    public static List<CarrierBean> getCarrier(String order_channel_id) {
        if (allCarriers == null) {
            logger.error("====== CarrierConfig 没有初始化，必须先调用 init");
            return null;
        }
        List<CarrierBean> carriersList = new ArrayList<>();
        for (CarrierBean bean : allCarriers) {
            if (order_channel_id.equals(bean.getOrder_channel_id())) {
                carriersList.add(bean);
            }
        }
        return carriersList;
    }

}
