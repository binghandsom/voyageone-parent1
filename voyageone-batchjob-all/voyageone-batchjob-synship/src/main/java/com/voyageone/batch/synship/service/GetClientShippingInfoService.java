package com.voyageone.batch.synship.service;

import com.google.gson.Gson;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.dao.ClientTrackingDao;
import com.voyageone.batch.synship.modelbean.ClientTrackingBean;
import com.voyageone.common.components.channelAdvisor.CaConstants;
import com.voyageone.common.components.channelAdvisor.bean.orders.OrderCriteria;
import com.voyageone.common.components.channelAdvisor.bean.orders.OrderResponseDetailHigh;
import com.voyageone.common.components.channelAdvisor.service.OrderService;
import com.voyageone.common.components.channelAdvisor.webservices.APIResultOfArrayOfOrderResponseItem;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sn3 on 2015-08-17.
 * modify by sky on 2015-08-19
 */
@Service
public class GetClientShippingInfoService extends BaseTaskService {

    @Autowired
    OrderService orderService;

    @Autowired
    protected IssueLog issueLog;

    @Autowired
    ClientTrackingDao clientTrackingDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SynshipGetClientShipping";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        //允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        //线程
        List<Runnable> threads = new ArrayList<>();
        for (String channelId : orderChannelIdList) {
            threads.add(() -> new GetShippingInfo(channelId).doRun());
        }
        runWithThreadPool(threads, taskControlList);
        $info(getTaskName() + "----------结束");

    }

    /**
     * 按渠道进行逻辑库存计算
     */
    public class GetShippingInfo {
        private OrderChannelBean channel;

        public GetShippingInfo(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        /**
         * 线程运行函数
         */
        public void doRun() {

            $info(channel.getFull_name() + "获取物流信息Start");

            //获取当前销售渠道的第三方配置信息
            HashMap<String, ThirdPartyConfigBean> configs = ThirdPartyConfigs.getThirdPartyConfigMap(channel.getOrder_channel_id());

            //设置调用参数
            OrderCriteria param = setParam(configs);

            //调用CA服务的API 获取订单信息
            APIResultOfArrayOfOrderResponseItem response;
            try {
                int pageNum = 1;
                while (true) {
                    response = orderService.getOrderList(param, configs);
                    //插入tt_client_tracking 主KEY重复时 跳过
                    insertClientTracking(response, channel);
                    if(response.getResultData().getOrderResponseItem().size() < Integer.parseInt(configs.get(CaConstants.OrderList.PAGE_SIZE).getProp_val1())){
                        break;
                    }else{
                        pageNum++;
                        param.setPageNumberFilter(pageNum);
                    }
                }
            } catch (Exception e) {
                $info("调用CA-orderService错误：" + e.getMessage());
                issueLog.log(e, ErrorType.BatchJob, SubSystem.SYNSHIP);
            }
            $info(channel.getFull_name() + "获取物流信息End");
        }
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
     */
    private void insertClientTracking(APIResultOfArrayOfOrderResponseItem list, OrderChannelBean channel) throws Exception {

        List<ClientTrackingBean> trackingList = new ArrayList<>();
        List<String> errorTrackingLst= new ArrayList<>();
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
    }

}
