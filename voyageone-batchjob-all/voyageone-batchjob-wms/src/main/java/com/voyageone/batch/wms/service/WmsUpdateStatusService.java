package com.voyageone.batch.wms.service;

import com.google.gson.Gson;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.ReservationDao;
import com.voyageone.batch.wms.modelbean.OrderDetailBean;
import com.voyageone.batch.wms.modelbean.ReservationBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.SearsService;
import com.voyageone.common.components.sears.SerasConstants;
import com.voyageone.common.components.sears.bean.OrderResponse;
import com.voyageone.common.components.sears.bean.UpdateStatusBean;
import com.voyageone.common.components.sears.bean.UpdateStatusItem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WmsUpdateStatusService extends BaseTaskService {

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Autowired
    SearsService searsService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsUpdateStatusJob";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList,TaskControlEnums.Name.row_count);

        int intRowCount = 1;
        if (!StringUtils.isNullOrBlank2(row_count)) {
            intRowCount = Integer.valueOf(row_count);
        }

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            final String reservationStatus = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);

            final int finalIntRowCount = intRowCount;

            threads.add(new Runnable() {
                @Override
                public void run() {
                    new updateStatus(orderChannelID, reservationStatus, finalIntRowCount).doRun();
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 更新订单相关状态
     */
    public class updateStatus  {
        private OrderChannelBean channel;
        List<String> reservationStatusList = new ArrayList<>();
        private int rowCount;

        public updateStatus(String orderChannelId, String reservationStatus, int rowCount) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            if (!StringUtils.isNullOrBlank2(reservationStatus)) {
                this.reservationStatusList = Arrays.asList(reservationStatus.split(","));
            }
            this.rowCount = rowCount;

        }

        public void doRun() {
            $info(channel.getFull_name() + " 更新订单相关状态开始");

            // 取得状态有变化的记录
            List<ReservationBean> reservationList = reservationDao.getUpdateStatusInfo(channel.getOrder_channel_id(), CodeConstants.Reservation_Status.Open, reservationStatusList, rowCount);

            $info(channel.getFull_name() + "----------更新订单相关状态的物品件数：" + reservationList.size());

            // 按订单级别进行编辑
             List<OrderDetailBean>  orderDetailList = new ArrayList<>();

            long orderNumber = 0;
            OrderDetailBean orderDetailBean = null;

            // 按照订单来分组数据
            for (ReservationBean reservation : reservationList) {

                if (orderNumber == reservation.getOrder_number()) {
                    List<ReservationBean> lstReservation = orderDetailBean.getLstReservation();
                    if (lstReservation == null) {
                        lstReservation = new ArrayList<>();
                    }

                    lstReservation.add(reservation);

                    orderDetailBean.setLstReservation(lstReservation);
                } else {
                    orderDetailBean = new OrderDetailBean();

                    orderDetailList.add(orderDetailBean);

                    orderDetailBean.setOrder_number(reservation.getOrder_number());
                    orderDetailBean.setClient_order_id(reservation.getClient_order_id());
                    orderDetailBean.setSyn_ship_no(reservation.getSyn_ship_no());
                    orderDetailBean.setSource_order_id(reservation.getSource_order_id());

                    List<ReservationBean> lstReservation = orderDetailBean.getLstReservation();
                    if (lstReservation == null) {
                        lstReservation = new ArrayList<>();
                    }

                    lstReservation.add(reservation);

                    orderDetailBean.setLstReservation(lstReservation);

                    orderNumber = reservation.getOrder_number();

                }

            }

            $info(channel.getFull_name() + "----------更新订单相关状态的订单件数：" + orderDetailList.size());

            for  (OrderDetailBean orderDetail : orderDetailList) {

                transactionRunner.runWithTran(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            $info(channel.getFull_name() + "---------Order_Number：" + orderDetail.getOrder_number() + "，Client_order_id：" + orderDetail.getClient_order_id() + "，明细件数：" + orderDetail.getLstReservation().size());

                            UpdateStatusBean updateStatusBean = new UpdateStatusBean();

                            // B2B order ID from the place order response. Same as the B2B-ORDER-ID in the URL
                            updateStatusBean.setOrderId(String.valueOf(orderDetail.getClient_order_id()));

                            for (ReservationBean reservation : orderDetail.getLstReservation()) {

                                reservation.setClient_status_update(false);

                                UpdateStatusItem updateStatusItem = new UpdateStatusItem();

                                // Item id from the place order request
                                updateStatusItem.setItemId(reservation.getClient_sku());

                                // Timestamp YYYY-MM-DD HH:MM:SS in UTC
                                updateStatusItem.setStatusUpdatedAt(reservation.getUpdate_time());

                                // 推送状态设置
                                switch (reservation.getStatus()) {

                                    case CodeConstants.Reservation_Status.Reserved:
                                    case CodeConstants.Reservation_Status.Packaged:

                                        reservation.setClient_status(SerasConstants.Status.PickedUp);
                                        reservation.setClient_status_comment(SerasConstants.Notes.PickedUp);

                                        // 该状态已更新的场合，忽略此记录
                                        if (reservation.getReservation_status().equals(CodeConstants.Reservation_Status.Reserved)) {
                                            reservation.setClient_status_update(true);
                                        }
                                        break;

                                    case CodeConstants.Reservation_Status.ShippedUS:
                                    case CodeConstants.Reservation_Status.Arrived:
                                        reservation.setClient_status(SerasConstants.Status.Shipped);
                                        reservation.setClient_status_comment(SerasConstants.Notes.Shipped);

                                        // Mandatory if the status is SHIPPED or DELIVERED. VoyageOne tracking number for the order.
                                        updateStatusItem.setInternationalTrackingNumber(reservation.getSyn_ship_no());

                                        // 该状态已更新的场合，忽略此记录
                                        if (reservation.getReservation_status().equals(CodeConstants.Reservation_Status.ShippedUS)) {
                                            reservation.setClient_status_update(true);
                                        }
                                        break;

                                    case CodeConstants.Reservation_Status.Clearance:
                                        reservation.setClient_status(SerasConstants.Status.Delivered);
                                        reservation.setClient_status_comment(SerasConstants.Notes.Delivered);

                                        // Mandatory if the status is SHIPPED or DELIVERED. VoyageOne tracking number for the order.
                                        updateStatusItem.setInternationalTrackingNumber(reservation.getSyn_ship_no());
                                        break;

                                    case CodeConstants.Reservation_Status.Returned:
                                        reservation.setClient_status(SerasConstants.Status.Returned);
                                        reservation.setClient_status_comment(SerasConstants.Notes.Returned);

                                        // Mandatory for cancellation and return status updates
                                        updateStatusItem.setQuantity("1");

                                        break;

                                    case CodeConstants.Reservation_Status.Cancelled:
                                        if (StringUtils.isNullOrBlank2(reservation.getReservation_status())) {
                                            reservation.setClient_status(SerasConstants.Status.Cancelled);
                                            reservation.setClient_status_comment(SerasConstants.Notes.Cancelled);
                                        } else {
                                            reservation.setClient_status(SerasConstants.Status.Returned);
                                            reservation.setClient_status_comment(SerasConstants.Notes.Returned);
                                        }

                                        // Mandatory for cancellation and return status updates
                                        updateStatusItem.setQuantity("1");
                                        break;
                                }

                                // Status values: PickedUp, Shipped, Delivered, Cancelled, Returned
                                updateStatusItem.setStatus(reservation.getClient_status());
                                // A note or text message that indicates the reason for certain status change
                                updateStatusItem.setComment(reservation.getClient_status_comment());

                                // 更新状态回写
                                reservation.setReservation_status(reservation.getStatus());

                                if (reservation.isClient_status_update()) {
                                    continue;
                                }

                                List<UpdateStatusItem> lstItem = updateStatusBean.getItems();
                                if (lstItem == null) {
                                    lstItem = new ArrayList<>();
                                    lstItem.add(updateStatusItem);
                                }
                                else {
                                    boolean blnFound =false;
                                    for (UpdateStatusItem item : lstItem) {

                                        if (item.getItemId().equals(updateStatusItem.getItemId()) &&
                                                item.getStatus().equals(updateStatusItem.getStatus())) {
                                            blnFound = true;
                                           if  (item.getStatus().equals(SerasConstants.Status.Returned) || item.getStatus().equals(SerasConstants.Status.Cancelled)) {
                                                item.setQuantity(String.valueOf(Integer.valueOf(item.getQuantity()) + 1));
                                            }
                                            break;
                                        }

                                    }

                                    if (!blnFound == true){
                                        lstItem.add(updateStatusItem);
                                    }

                                }

                                updateStatusBean.setItems(lstItem);

                            }

                            //没有可更新的物品时，不调用API
                            if (updateStatusBean.getItems() == null) {
                                $info(channel.getFull_name() + "---------无需推送，Order_Number：" +orderDetail.getOrder_number());
                                for (ReservationBean reservation : orderDetail.getLstReservation()) {
                                    reservationDao.updateClientStatus(reservation.getOrder_number(), reservation.getItem_number(), reservation.getReservation_status(), getTaskName());
                                }

                            } else {
                                OrderResponse orderResponse = new OrderResponse();

                                String json = updateStatusBean == null ? "" : new Gson().toJson(updateStatusBean);

                                orderResponse = searsService.UpdateStatus(updateStatusBean);

//                                orderResponse.setMessage("Succeed");

                                if (orderResponse != null && StringUtils.null2Space2(orderResponse.getMessage()).equals("Succeed")) {
                                    $info(channel.getFull_name() + "---------更新订单相关状态成功："+json);
                                    for (ReservationBean reservation : orderDetail.getLstReservation()) {

                                        reservationDao.updateClientStatus(reservation.getOrder_number(), reservation.getItem_number(), reservation.getReservation_status(), getTaskName());

                                        if (!reservation.isClient_status_update()) {
                                            String note = "Push status to Sears(Reservation ID:" + reservation.getId() + "，Status:" + reservation.getClient_status() + ")";

                                            reservationDao.insertOrderNotes(reservation.getOrder_number(), reservation.getSource_order_id(), note, getTaskName());
                                        }
                                    }

                                } else {
                                    $info(channel.getFull_name() + "---------更新订单相关状态错误："+json + ",ErrorMessage：" + StringUtils.null2Space2(orderResponse.getMessage()));
                                    logIssue(channel.getFull_name() + "更新订单相关状态错误：", json + ",ErrorMessage：" + StringUtils.null2Space2(orderResponse.getMessage()));
                                }
                            }

                        } catch (Exception e) {
                            $info(channel.getFull_name() + "---------更新订单相关状态错误，Order_Number：" +orderDetail.getOrder_number()+ "，Client_order_id：" + orderDetail.getClient_order_id());
                            logIssue(e, channel.getFull_name() + " 更新订单相关状态错误，Order_Number：" +orderDetail.getOrder_number()+ "，Client_order_id：" + orderDetail.getClient_order_id() );

                            throw new RuntimeException(e);
                        }
                    }
                });

            }

            $info(channel.getFull_name() + " 更新订单相关状态结束");

        }
    }

}
