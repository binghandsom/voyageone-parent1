package com.voyageone.web2.vms.views.order.order_info;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.vms.order.VmsOrderDetailService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.VendorChannelConfig;
import com.voyageone.web2.vms.bean.order.ShipmentBean;
import com.voyageone.web2.vms.bean.order.SkuStatusBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * order info service of controller
 * Created by vantis on 16-7-6.
 */
@Service
public class OrderInfoService extends BaseService {

    @Autowired
    VmsOrderDetailService vmsOrderDetailService;

    /**
     * 获得所有SKU相关的状态 用于页面选项显示
     *
     * @return skuStatusBeanList
     */
    public List<SkuStatusBean> getAllOrderStatusesList() {

        List<TypeBean> skuStatusList = Types.getTypeList(VmsConstants.TYPE_ID.PRODUCT_STATUS);

        if (null == skuStatusList) return new ArrayList<>();

        return skuStatusList.stream()
                .map(typeBean -> {
                    SkuStatusBean skuStatusBean = new SkuStatusBean();
                    skuStatusBean.setName(typeBean.getName());
                    skuStatusBean.setValue(typeBean.getValue());
                    return skuStatusBean;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取当前用户/Channel已建立的shipment
     *
     * @param user 当前用户
     * @return shipmentBean
     */
    public ShipmentBean getCurrentShipment(UserSessionBean user) {
        ChannelConfigEnums.Channel channel = user.getSelChannel();

        return null;
    }

    public VendorChannelConfig getChannelConfigs(UserSessionBean user) {

        return null;
    }
}
