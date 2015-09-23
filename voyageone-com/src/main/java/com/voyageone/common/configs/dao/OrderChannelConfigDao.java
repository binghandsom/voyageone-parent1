package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.DiscountRateBean;
import com.voyageone.common.configs.beans.OrderChannelConfigBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jonas on 4/10/2015.
 */
@Repository
public class OrderChannelConfigDao extends BaseDao {
    /**
     * 根据消息类型获得类型消息Map
     */
    public List<OrderChannelConfigBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "tm_order_channel_config_getAll");
    }

    /**
     * 取得渠道的折扣信息
     */
    public List<DiscountRateBean> getDiscountRate() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "tm_discount_rate_getDiscountRate");
    }
}
