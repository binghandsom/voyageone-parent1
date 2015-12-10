package com.voyageone.batch.synship.service;

import com.ctc.wstx.util.StringUtil;
import com.google.gson.Gson;
import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.dao.ClientTrackingDao;
import com.voyageone.batch.synship.dao.ReservationDao;
import com.voyageone.batch.synship.modelbean.ClientTrackingBean;
import com.voyageone.batch.synship.modelbean.ReservationBean;
import com.voyageone.batch.synship.modelbean.ReservationClientBean;
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
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.mail.Mail;
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

    @Autowired
    ReservationDao reservationDao;

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
            //错误邮件初期化
            List<ReservationClientBean>  errorAllotInventoryDetailList = new ArrayList<>();

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
                    errorAllotInventoryDetailList = insertClientTracking(response, channel, errorAllotInventoryDetailList,reservationOpenMap);
                    if(response.getResultData().getOrderResponseItem().size() < Integer.parseInt(configs.get(CaConstants.OrderList.PAGE_SIZE).getProp_val1())){
                        break;
                    }else{
                        pageNum++;
                        param.setPageNumberFilter(pageNum);
                    }
                }

                String errorMail = sendErrorMail(errorAllotInventoryDetailList);

                if (!StringUtils.isNullOrBlank2(errorMail)) {
                    $info("品牌方取消邮件出力");
                    String subject = String.format(SynshipConstants.EmailSynshipGetClientShipping.SUBJECT, channel.getFull_name());
                    Mail.sendAlert(CodeConstants.EmailReceiver.NEED_SOLVE, subject, errorMail, true);
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
     * @param reservationOpenMap
     */
    private List<ReservationClientBean> insertClientTracking(APIResultOfArrayOfOrderResponseItem list, OrderChannelBean channel, List<ReservationClientBean> errorAllotInventoryDetailList,HashMap<String, ReservationClientBean> reservationOpenMap) throws Exception {

        List<ClientTrackingBean> trackingList = new ArrayList<>();
        List<String> errorTrackingLst= new ArrayList<>();
        if (list != null) {
            $info(channel.getFull_name() + "----------返回的订单件数：" + list.getResultData().getOrderResponseItem().size());
            for (OrderResponseDetailHigh order : list.getResultData().getOrderResponseItem()) {
                order.setOrderState("Cancelled");
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
                    //res id 设定
                    String[] arr = clientBean.getRes_id().split(",");
                    Long[] longResids = new Long[arr.length];
                    int i = 0;
                    for (String id : arr){
                        longResids[i] = Long.valueOf(id);
                        i = i + 1;
                    }

                    reservationDao.updateReservationBySynshipno(clientBean.getSyn_ship_no(), CodeConstants.Reservation_Status.BackOrdered, getTaskName(),longResids);
                    // 并且插入相关记录到wms_bt_reservation_log中
                    reservationDao.insertReservationLogByInResID(clientBean.getSyn_ship_no(), "GetClientTrackingJob BackOrdered", getTaskName(),longResids);
                    // 错误邮件设定
                    errorAllotInventoryDetailList.add(clientBean);
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
        return errorAllotInventoryDetailList;
    }

    /**
     * 错误邮件出力
     * @param errorList 错误SKU一览
     * @return 错误邮件内容
     */
    private String sendErrorMail(List<ReservationClientBean> errorList) {

        StringBuilder builderContent = new StringBuilder();

        if (errorList.size() > 0) {

            StringBuilder builderDetail = new StringBuilder();

            int index = 0;
            for (ReservationClientBean error : errorList) {
                index = index + 1;
                builderDetail.append(String.format(SynshipConstants.EmailSynshipGetClientShipping.ROW,
                        index,
                        StringUtils.null2Space(error.getShop_name()),
                        StringUtils.null2Space(String.valueOf(error.getOrder_number())),
                        StringUtils.null2Space(error.getSource_order_id()),
                        StringUtils.null2Space(error.getSyn_ship_no()),
                        StringUtils.null2Space(error.getClient_order_id()),
                        StringUtils.null2Space(error.getSeller_order_id()),
                        StringUtils.null2Space(error.getSku()),
                        StringUtils.null2Space(error.getRes_id()),
                        StringUtils.null2Space(error.getShip_name()),
                        StringUtils.null2Space(error.getShip_phone()),
                        StringUtils.null2Space(DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(error.getOrder_date_time(),8), DateTimeUtil.DEFAULT_DATETIME_FORMAT))));
            }

            String count = String.format(SynshipConstants.EmailSynshipGetClientShipping.COUNT, errorList.size());

            String detail = String.format(SynshipConstants.EmailSynshipGetClientShipping.TABLE, count, builderDetail.toString());

            builderContent
                    .append(Constants.EMAIL_STYLE_STRING)
                    .append(SynshipConstants.EmailSynshipGetClientShipping.HEAD)
                    .append(detail);


        }

        return builderContent.toString();
    }

}
