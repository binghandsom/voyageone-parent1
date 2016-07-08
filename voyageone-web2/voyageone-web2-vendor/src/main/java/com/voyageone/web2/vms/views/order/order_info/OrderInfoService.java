package com.voyageone.web2.vms.views.order.order_info;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.MapUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.vms.order.VmsOrderDetailService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants.*;
import com.voyageone.web2.vms.bean.VmsChannelSettings;
import com.voyageone.web2.vms.bean.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

        List<TypeBean> skuStatusList = Types.getTypeList(TYPE_ID.PRODUCT_STATUS);

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

    /**
     * 读取channel相应配置
     *
     * @param user 当前用户
     * @return 当前用户所选择channel的配置
     */
    public VmsChannelSettings getChannelConfigs(UserSessionBean user) {
        VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(user.getSelChannelId(), ChannelConfig.VENDER_OPERATE_TYPE, ChannelConfig.COMMON_CONFIG_CODE);
        VmsChannelSettings vmsChannelSettings = new VmsChannelSettings();
        vmsChannelSettings.setVenderOperateType(vmsChannelConfigBean.getConfigValue1());
        return vmsChannelSettings;
    }

    /**
     * 根据条件搜索订单
     *
     * @param user            当前用户
     * @param orderSearchInfo 搜索条件
     * @return 订单列表
     */
    public List<AbstractOrderInfoBean> getOrders(UserSessionBean user, OrderSearchInfo orderSearchInfo) {

        List<AbstractOrderInfoBean> orderList = new ArrayList<>();
        try {
            Map<String, Object> orderSearchParams = MapUtil.toMap(orderSearchInfo);
            orderSearchParams.put("channelId", user.getSelChannel().getId());
            $debug(JsonUtil.bean2Json(orderSearchParams));


            /**
             * 根据渠道配置设置
             */
            switch (this.getChannelConfigs(user).getVenderOperateType()) {

                // sku级的订单获取
                case STATUS_VALUE.VENDER_OPERATE_TYPE.SKU: {
                    List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = vmsOrderDetailService.selectOrderList(orderSearchParams);
                    return vmsBtOrderDetailModelList.stream()
                            .map(vmsBtOrderDetailModel -> {
                                OrderInfoBean orderInfoBean = new OrderInfoBean();
                                orderInfoBean.setOrderId(vmsBtOrderDetailModel.getOrderId());
                                orderInfoBean.setDesc(vmsBtOrderDetailModel.getDecription());
//                                orderInfoBean.setOrderDateTimestamp(DateTimeUtil.parse(vmsBtOrderDetailModel.getOrderTime()).getTime());
                                orderInfoBean.setPrice(vmsBtOrderDetailModel.getClientRetailPrice());
                                orderInfoBean.setStatus(vmsBtOrderDetailModel.getStatus());
                                return orderInfoBean;
                            })
                            .collect(Collectors.toList());
                }
                case STATUS_VALUE.VENDER_OPERATE_TYPE.ORDER: {

                    return vmsOrderDetailService.selectPlatformOrderIdList(orderSearchParams).stream()
                            .map(orderId -> {

                                List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = vmsOrderDetailService.selectOrderList(new HashMap<String, Object>() {{
                                    put("orderId", orderId);
                                }});
                                PlatformOrderInfoBean platformOrderInfoBean = new PlatformOrderInfoBean();
                                platformOrderInfoBean.setOrderId(vmsBtOrderDetailModelList.get(0).getOrderId());
//                                platformOrderInfoBean.setOrderDateTimestamp(DateTimeUtil.parse(vmsBtOrderDetailModelList.get(0).getOrderTime()).getTime());
                                platformOrderInfoBean.setStatus(vmsBtOrderDetailModelList.get(0).getStatus());
                                vmsBtOrderDetailModelList.stream()
                                        .map(vmsBtOrderDetailModel -> new OrderInfoBean() {{
                                            setOrderId(vmsBtOrderDetailModel.getOrderId());
//                                            setOrderDateTimestamp(DateTimeUtil.parse(vmsBtOrderDetailModel.getOrderTime()).getTime());
                                            setDesc(vmsBtOrderDetailModel.getDecription());
                                            setPrice(vmsBtOrderDetailModel.getClientRetailPrice());
                                            setSku(vmsBtOrderDetailModel.getClientSku());
                                            setStatus(vmsBtOrderDetailModel.getStatus());
                                        }})
                                        .forEach(platformOrderInfoBean::pushOrderInfoBean);

                                return platformOrderInfoBean;
                            })
                            .collect(Collectors.toList());

                }
            }
        } catch (IllegalAccessException e) {
            $error("搜索条件转换Map失败:" + JsonUtil.bean2Json(orderSearchInfo));
        }
        return orderList;
    }

    /**
     * 默认条件搜索订单(Open)
     *
     * @param user 当前用户
     * @return 订单列表
     */
    public List<AbstractOrderInfoBean> getOrders(UserSessionBean user) {

        OrderSearchInfo defaultOrderSearchInfo = new OrderSearchInfo();
        defaultOrderSearchInfo.setStatus(STATUS_VALUE.SHIPMENT_STATUS.OPEN);
        return this.getOrders(user, defaultOrderSearchInfo);
    }
}
