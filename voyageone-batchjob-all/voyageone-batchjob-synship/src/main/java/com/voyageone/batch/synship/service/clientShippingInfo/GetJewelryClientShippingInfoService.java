package com.voyageone.batch.synship.service.clientShippingInfo;

import com.google.gson.Gson;
import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.modelbean.ClientTrackingBean;
import com.voyageone.batch.synship.modelbean.ReservationClientBean;
import com.voyageone.common.components.channelAdvisor.CaConstants;
import com.voyageone.common.components.channelAdvisor.bean.orders.OrderCriteria;
import com.voyageone.common.components.channelAdvisor.bean.orders.OrderResponseDetailHigh;
import com.voyageone.common.components.channelAdvisor.webservices.APIResultOfArrayOfOrderResponseItem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jack on 2016-01-07.
 */
@Service
@Transactional
public class GetJewelryClientShippingInfoService extends GetClientShippingBaseService {

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 JD 的内容。但暂时搁置
    }

    /**
     * 获取Jewelry的物流以及超卖信息
     */
    public void  getJewelryShippingInfo(String channelId,  List<Runnable> threads) {

        threads.add(() -> {

            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);

            $info(channel.getFull_name() + "获取物流信息Start");

            transactionRunner.runWithTran(() -> {

                //错误邮件初期化
                List<ReservationClientBean> errorReservationClientList = new ArrayList<>();

                //获取当前销售渠道的第三方配置信息
                HashMap<String, ThirdPartyConfigBean> configs = ThirdPartyConfigs.getThirdPartyConfigMap(channel.getOrder_channel_id());

                //设置调用参数
                OrderCriteria param = setParam(configs);

                //调用CA服务的API 获取订单信息
                APIResultOfArrayOfOrderResponseItem response;
                try {
                    // 从tt_reservation表中取得status=11(open)的记录,同时需要再抽出oms_bt_orders中的client_order_id
                    List<ReservationClientBean> ReservationList = reservationDao.getReservationDatas(channel.getOrder_channel_id(), CodeConstants.Reservation_Status.Open);
                    HashMap<String, ReservationClientBean> reservationOpenMap = new HashMap<String, ReservationClientBean>();
                    for (ReservationClientBean reservationBean : ReservationList) {
                        if (!StringUtils.isNullOrBlank2(reservationBean.getClient_order_id()))
                            reservationOpenMap.put(reservationBean.getClient_order_id(), reservationBean);
                    }
                    int pageNum = 1;
                    while (true) {
                        response = orderService.getOrderList(param, configs);
                        //插入tt_client_tracking 主KEY重复时 跳过
                        List<ReservationClientBean> CancelReservationClientList = insertClientTracking(response, channel, reservationOpenMap);
                        errorReservationClientList.addAll(CancelReservationClientList);
                        if (response.getResultData().getOrderResponseItem().size() < Integer.parseInt(configs.get(CaConstants.OrderList.PAGE_SIZE).getProp_val1())) {
                            break;
                        } else {
                            pageNum++;
                            param.setPageNumberFilter(pageNum);
                        }
                    }

                    // 更新超卖订单状态
                    $info(channel.getFull_name() + "更新超卖订单件数：" + errorReservationClientList.size());
                    SetBackOrderList(channel, errorReservationClientList);

                } catch (Exception e) {
                    $info(channel.getFull_name() + "获取物流信息失败：" + e.getMessage());
                    logIssue(channel.getFull_name() + "获取物流信息失败：" + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            $info(channel.getFull_name() + "获取物流信息End");
        });
    }

    /**
     * 设置调用API的参数
     *
     * @return OrderCriteria
     */
    private OrderCriteria setParam(HashMap<String, ThirdPartyConfigBean> configs) {
        OrderCriteria param = new OrderCriteria();

        // 默认时间间隔24小时
        int timeInterval = 24;
        if (!StringUtils.isNullOrBlank2(configs.get(CaConstants.OrderList.TIME_INTERVAL).getProp_val1())) {
            timeInterval = Integer.parseInt(configs.get(CaConstants.OrderList.TIME_INTERVAL).getProp_val1());
        }

        //当前时间
        String endTime = DateTimeUtil.getLocalTime(0);
        //当前时间-1
        String startTime = DateTimeUtil.getLocalTime(timeInterval * -1);

        //搜索条件是 系统时间~系统时间-N天
        XMLGregorianCalendar startTimeXml = DateTimeUtil.strDateTimeTOXMLGregorianCalendar(startTime);
        XMLGregorianCalendar endTimeXml = DateTimeUtil.strDateTimeTOXMLGregorianCalendar(endTime);

        param.setStatusUpdateFilterBeginTimeGMT(startTimeXml);
        param.setStatusUpdateFilterEndTimeGMT(endTimeXml);
        param.setShippingStatusFilter(configs.get(CaConstants.OrderList.SHIPPING_STATUS_FILTER).getProp_val1());
        param.setDetailLevel(configs.get(CaConstants.OrderList.DETAILLEVEL).getProp_val1());
        param.setPageNumberFilter(1);
        param.setPageSize(Integer.parseInt(configs.get(CaConstants.OrderList.PAGE_SIZE).getProp_val1()));

        $info("startTimeXml:" + startTimeXml);
        $info("endTimeXml:" + endTimeXml);

        return param;
    }

    /**
     * 插入tt_client_tracking 主KEY重复时 跳过
     *
     * @param list    APIResultOfArrayOfOrderResponseItem
     * @param channel 渠道
     * @param reservationOpenMap
     */
    private List<ReservationClientBean> insertClientTracking(APIResultOfArrayOfOrderResponseItem list, OrderChannelBean channel, HashMap<String, ReservationClientBean> reservationOpenMap) throws Exception {

        List<ClientTrackingBean> trackingList = new ArrayList<>();
        List<String> errorTrackingLst= new ArrayList<>();
        List<ReservationClientBean> CancelReservationClientList =new ArrayList<>();
        if (list != null) {
            $info(channel.getFull_name() + "----------返回的订单件数：" + list.getResultData().getOrderResponseItem().size());
            for (OrderResponseDetailHigh order : list.getResultData().getOrderResponseItem()) {
                ClientTrackingBean bean = new ClientTrackingBean();
                bean.setOrder_channel_id(channel.getOrder_channel_id());
                //品牌方订单编号
                bean.setClient_order_id(String.valueOf(order.getOrderID()));
                //平台订单编号
                bean.setSource_order_id(order.getClientOrderIdentifier());
                //默认获取第一条物流信息
                if (order.getShippingInfo().getShipmentList().getShipment().get(0) != null) {
                    bean.setTracking_no(order.getShippingInfo().getShipmentList().getShipment().get(0).getTrackingNumber());
                    bean.setTracking_type(order.getShippingInfo().getShipmentList().getShipment().get(0).getShippingCarrier());
                }
                //发货时间
                bean.setTracking_time(DateTimeUtil.XMLGregorianCalendarToDate(order.getOrderStatus().getCheckoutDateGMT()));

                bean.setCreater(getTaskName());
                bean.setModifier(getTaskName());
                String json = bean == null ? "" : new Gson().toJson(bean);
                $info(channel.getFull_name() + "----------返回的订单信息：" + json);
                if (StringUtils.isNullOrBlank2(bean.getTracking_type())|| StringUtils.isNullOrBlank2(bean.getTracking_no())) {
                    //String json = order == null ? "" : new Gson().toJson(order);
                    //$info(channel.getFull_name() + "----------返回的订单具体信息：" + json);
                    errorTrackingLst.add(bean.getSource_order_id());
                }
                else {
                    // GMT时间转换
                    String channel_time_zone = ChannelConfigs.getVal1(channel.getOrder_channel_id(), ChannelConfigEnums.Name.channel_time_zone);
                    bean.setTracking_time(DateTimeUtil.getGMTTime(bean.getTracking_time(), StringUtils.isNullOrBlank2(channel_time_zone)?0:Integer.valueOf(channel_time_zone)));
                    trackingList.add(bean);
                }

                // 保存JE订单号
                clientTrackingDao.updateSellerOrderID(channel.getOrder_channel_id(),String.valueOf(order.getOrderID()), order.getSellerOrderID(), getTaskName());

                //如果orderState是Cancelled，并且orderid等于client_order_id时
                if (order.getOrderState().equals("Cancelled") && !reservationOpenMap.equals(null) && reservationOpenMap.size() > 0 && reservationOpenMap.containsKey(String.valueOf(order.getOrderID()))){
                    $info(channel.getFull_name() + "--------------Client_order_id：" + String.valueOf(order.getOrderID()) + "  is Cancelled");
                    // 此订单超卖，需要更新tt_reservation的状态为98

                    ReservationClientBean clientBean = new ReservationClientBean();
                    clientBean = reservationOpenMap.get(String.valueOf(order.getOrderID()));

                    // 超卖订单设定
                    CancelReservationClientList.add(clientBean);
                }
            }

            $info(channel.getFull_name() + "----------订单的快递信息不全：" + errorTrackingLst.toString());
//            if (errorTrackingLst.size() > 0 ) {
//                logIssue(channel.getFull_name() + "订单的快递信息不全，无法进行LA港口模拟发货", errorTrackingLst);
//            }
        }

        //插入tt_client_tracking 主KEY重复时 跳过
        if (trackingList.size() > 0) {
            clientTrackingDao.insert(trackingList);
        }
        return CancelReservationClientList;
    }

}
