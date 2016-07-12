package com.voyageone.web2.vms.views.order;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.util.MapUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.vms.order.VmsOrderDetailService;
import com.voyageone.service.impl.vms.shipment.VmsShipmentService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants.ChannelConfig;
import com.voyageone.web2.vms.VmsConstants.STATUS_VALUE;
import com.voyageone.web2.vms.VmsConstants.TYPE_ID;
import com.voyageone.web2.vms.bean.VmsChannelSettings;
import com.voyageone.web2.vms.bean.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * order info service of controller
 * Created by vantis on 16-7-6.
 */
@Service
public class OrderInfoService extends BaseService {

    private VmsOrderDetailService vmsOrderDetailService;
    private VmsShipmentService vmsShipmentService;

    @Autowired
    public OrderInfoService(VmsOrderDetailService vmsOrderDetailService, VmsShipmentService vmsShipmentService) {
        this.vmsOrderDetailService = vmsOrderDetailService;
        this.vmsShipmentService = vmsShipmentService;
    }

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
    public VmsBtShipmentModel getCurrentShipment(UserSessionBean user) {

        Map<String, Object> shipmentSearchParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel());
        }};
        return vmsShipmentService.select(shipmentSearchParams);
    }

    /**
     * 读取channel相应配置
     *
     * @param user 当前用户
     * @return 当前用户所选择channel的配置
     */
    public VmsChannelSettings getChannelConfigs(UserSessionBean user) {

        VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                ChannelConfig.VENDOR_OPERATE_TYPE, ChannelConfig.COMMON_CONFIG_CODE);

        // Missing required configures for this channel, please contact with the system administrator for help.
        if (null == vmsChannelConfigBean) throw new BusinessException("8000019");

        VmsChannelSettings vmsChannelSettings = new VmsChannelSettings();
        vmsChannelSettings.setVendorOperateType(vmsChannelConfigBean.getConfigValue1());
        return vmsChannelSettings;
    }

    /**
     * 根据条件搜索订单
     *
     * @param user            当前用户
     * @param orderSearchInfo 搜索条件
     * @return 订单列表
     */
    public List<AbstractSubOrderInfoBean> getOrders(UserSessionBean user, OrderSearchInfo orderSearchInfo) {

        List<AbstractSubOrderInfoBean> orderList = new ArrayList<>();
        Map<String, Object> orderSearchParamsWithLimitAndSort = organizeOrderSearchParams(user, orderSearchInfo);
        /**
         * 根据渠道配置设置
         */
        switch (this.getChannelConfigs(user).getVendorOperateType()) {

            // sku级的订单获取
            case STATUS_VALUE.VENDOR_OPERATE_TYPE.SKU: {
                List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = vmsOrderDetailService.selectOrderList
                        (orderSearchParamsWithLimitAndSort);
                return vmsBtOrderDetailModelList.stream()
                        .map(vmsBtOrderDetailModel -> {
                            SubOrderInfoBean orderInfoBean = new SubOrderInfoBean();
                            orderInfoBean.setOrderId(vmsBtOrderDetailModel.getOrderId());
                            orderInfoBean.setSku(vmsBtOrderDetailModel.getClientSku());
                            orderInfoBean.setDesc(vmsBtOrderDetailModel.getDecription());
                            orderInfoBean.setOrderDateTime(vmsBtOrderDetailModel.getOrderTime());
                            orderInfoBean.setPrice(vmsBtOrderDetailModel.getClientRetailPrice());
                            orderInfoBean.setStatus(vmsBtOrderDetailModel.getStatus());
                            return orderInfoBean;
                        })
                        .collect(Collectors.toList());
            }

            // 平台订单级获取
            case STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER: {

                return vmsOrderDetailService.selectPlatformOrderIdList(orderSearchParamsWithLimitAndSort).stream()
                        .map(orderId -> {

                            // 获取平台订单id下的所有的sku
                            List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = vmsOrderDetailService
                                    .selectOrderList(new HashMap<String, Object>() {{
                                        put("orderId", orderId);
                                    }});

                            // 按照第一个sku初始化平台订单id内容
                            PlatformSubOrderInfoBean platformOrderInfoBean = new PlatformSubOrderInfoBean();
                            platformOrderInfoBean.setOrderId(vmsBtOrderDetailModelList.get(0).getOrderId());
                            platformOrderInfoBean.setOrderDateTime(vmsBtOrderDetailModelList.get(0).getOrderTime());
                            platformOrderInfoBean.setStatus(vmsBtOrderDetailModelList.get(0).getStatus());

                            // 将订单下的sku信息压入
                            vmsBtOrderDetailModelList.stream()
                                    .map(vmsBtOrderDetailModel -> new SubOrderInfoBean() {{

                                        // 整理格式
                                        setOrderId(vmsBtOrderDetailModel.getOrderId());
                                        setOrderDateTime(vmsBtOrderDetailModel.getOrderTime());
                                        setDesc(vmsBtOrderDetailModel.getDecription());
                                        setPrice(vmsBtOrderDetailModel.getClientRetailPrice());
                                        setSku(vmsBtOrderDetailModel.getClientSku());
                                        setStatus(vmsBtOrderDetailModel.getStatus());
                                    }})

                                    // 压入
                                    .forEach(platformOrderInfoBean::pushOrderInfoBean);

                            return platformOrderInfoBean;
                        })
                        .collect(Collectors.toList());
            }
        }
        return orderList;
    }

    /**
     * 根据输入条件组出对应的搜索Map
     *
     * @param user            当前用户
     * @param orderSearchInfo 搜索条件
     * @return 搜索条件Map
     */
    private Map<String, Object> organizeOrderSearchParams(UserSessionBean user, OrderSearchInfo orderSearchInfo) {

        Map<String, Object> orderSearchParams;
        try {
            orderSearchParams = MapUtil.toMap(orderSearchInfo);
        } catch (IllegalAccessException e) {
            throw new BusinessException("WRONG SEARCH PARAMETERS.", e);
        }

        orderSearchParams.put("channelId", user.getSelChannel().getId());

        // limit sort条件
        Map<String, Object> orderSearchParamsWithLimitAndSort = MySqlPageHelper.build(orderSearchParams)
                .addSort("order_time", ((null != orderSearchInfo.getStatus() && orderSearchInfo.getStatus() == 1) ?
                        Order.Direction.DESC : Order.Direction.ASC))
                .limit(orderSearchInfo.getSize())
                .page(orderSearchInfo.getCurr())
                .toMap();

        $debug(orderSearchParamsWithLimitAndSort.toString());
        return orderSearchParamsWithLimitAndSort;
    }

    /**
     * 默认条件获取订单信息(Open)
     *
     * @param user 当前用户
     * @return Order信息内容
     */
    @Deprecated
    public OrderInfoBean getOrderInfo(UserSessionBean user) {

        OrderInfoBean orderInfoBean = new OrderInfoBean();
        OrderSearchInfo defaultOrderSearchInfo = new OrderSearchInfo();
        defaultOrderSearchInfo.setStatus(STATUS_VALUE.SHIPMENT_STATUS.OPEN);
        return this.getOrderInfo(user, defaultOrderSearchInfo);
    }

    /**
     * 默认条件获取订单信息(Open)
     *
     * @param user 当前用户
     * @return Order信息内容
     */
    public OrderInfoBean getOrderInfo(UserSessionBean user, OrderSearchInfo orderSearchInfo) {

        OrderInfoBean orderInfoBean = new OrderInfoBean();
        orderInfoBean.setTotal(this.getTotalOrderNum(user, orderSearchInfo));
        orderInfoBean.setOrderList(this.getOrders(user, orderSearchInfo));
        return orderInfoBean;
    }

    /**
     * 获取条件下的订单总数
     *
     * @param user            当前用户
     * @param orderSearchInfo 搜索条件
     * @return 订单总数
     */
    private long getTotalOrderNum(UserSessionBean user, OrderSearchInfo orderSearchInfo) {

        if (STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER.equals(this.getChannelConfigs(user).getVendorOperateType())) {
            Map<String, Object> orderSearchParamsWithLimitAndSort = organizeOrderSearchParams(user, orderSearchInfo);
            return vmsOrderDetailService.getTotalOrderNum(orderSearchParamsWithLimitAndSort);
        } else if (STATUS_VALUE.VENDOR_OPERATE_TYPE.SKU.equals(this.getChannelConfigs(user).getVendorOperateType())) {
            Map<String, Object> skuSearchParamsWithLimitAndSort = organizeOrderSearchParams(user, orderSearchInfo);
            return vmsOrderDetailService.getTotalSkuNum(skuSearchParamsWithLimitAndSort);
        } else return 0;
    }

    /**
     * 取消订单
     *
     * @param user 当前用户
     * @param item 被取消订单
     * @return 取消条目数
     */
    public int cancelOrder(UserSessionBean user, PlatformSubOrderInfoBean item) {

        Map<String, Object> cancelOrderParam = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel());
            put("orderId", item.getOrderId());
            put("status", STATUS_VALUE.PRODUCT_STATUS.CANCEL);
        }};
        return vmsOrderDetailService.updateOrderStatus(cancelOrderParam);
    }
}
