package com.voyageone.batch.synship.service.clientShippingInfo;

import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.synship.dao.ClientTrackingDao;
import com.voyageone.batch.synship.dao.OrderDao;
import com.voyageone.batch.synship.dao.ReservationDao;
import com.voyageone.batch.synship.dao.TrackingDao;
import com.voyageone.batch.synship.modelbean.ReservationClientBean;
import com.voyageone.batch.synship.modelbean.TrackingBean;
import com.voyageone.common.components.channelAdvisor.service.OrderService;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbLogisticsService;
import com.voyageone.common.configs.Enums.CarrierEnums;
import com.voyageone.common.configs.Enums.PortConfigEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.magento.api.service.MagentoApiServiceImpl;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jack on 2016-01-07.
 */
@Service
public abstract class GetClientShippingBaseService extends BaseTaskService {

    @Autowired
    OrderService orderService;

    @Autowired
    TbLogisticsService tbLogisticsService;

    @Autowired
    MagentoApiServiceImpl magentoApiServiceImpl;

    @Autowired
    ClientTrackingDao clientTrackingDao;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    TrackingDao trackingDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SynshipGetClientShipping";
    }


    /**
     * 品牌方发货订单时更新状态并插入日志
     */
    protected void SetShipOrderList(OrderChannelBean channel, List<ReservationClientBean>  reservationClientlList, String port) throws Exception {

        for (ReservationClientBean reservationClientBean : reservationClientlList) {

            $info(channel.getFull_name()+ "-------Order_Number：" + reservationClientBean.getOrder_number());

            String tracking_no = "";
            String tracking_type = "";
            String tracking_time = "";
            String tracking_status = "";
            String status = "";
            String res_note = "";

            if (PortConfigEnums.Port.SYB.getId().equals(port)) {
                tracking_no = reservationClientBean.getTaobao_logistics_id();
                tracking_type = CarrierEnums.Name.SYB.toString();
                tracking_time = DateTimeUtil.getGMTTime();
                tracking_status = CodeConstants.TRACKING.INFO_030;
                status = CodeConstants.Reservation_Status.ShippedTP;
                res_note = channel.getFull_name() + " Items sent to the SYB" ;
            }
            else if (PortConfigEnums.Port.LA.getId().equals(port)) {
                tracking_time = DateTimeUtil.getGMTTime();
                tracking_status = CodeConstants.TRACKING.INFO_052;
                status = CodeConstants.Reservation_Status.ShippedTP;
                res_note = channel.getFull_name() + " Items sent to LA" ;
            }

            if (PortConfigEnums.Port.SYB.getId().equals(port)) {

                // 模拟Tracking
                TrackingBean trackingBean = SimTracking(reservationClientBean.getSyn_ship_no(), reservationClientBean.getOrder_channel_id(), tracking_no, tracking_type, port);

                // 插入Tracking
                trackingDao.insertTracking(trackingBean);

                // 插入ResTracking
                trackingDao.insertResTracking(reservationClientBean.getSyn_ship_no(), tracking_no, tracking_type, getTaskName());
            }

            // 插入TrackingInfo
            trackingDao.insertTrackingInfoBySim(reservationClientBean.getSyn_ship_no(), tracking_no, tracking_status, tracking_time, getTaskName());

            // 更新物品状态
            reservationDao.UpdateReservationStatus(reservationClientBean.getSyn_ship_no(), status, "2", getTaskName());

            // 插入物品日志
            reservationDao.insertReservationLog(reservationClientBean.getSyn_ship_no(), res_note, getTaskName());

            if (PortConfigEnums.Port.SYB.getId().equals(port)) {
                // 更新订单状态
                orderDao.updateOrderStatus(reservationClientBean.getSyn_ship_no(), status, getTaskName());
            }

            // 插入订单日志
            orderDao.insertOrderNotes(reservationClientBean.getOrder_number(), reservationClientBean.getSource_order_id(), res_note, getTaskName());

        }

    }

    /**
     * 设置Track内容
     */
    private  TrackingBean SimTracking( String syn_ship_no, String order_channel_id, String tracking_no, String tracking_type, String port) {

        TrackingBean trackingBean = new TrackingBean();

        trackingBean.setSyn_ship_no(syn_ship_no);
        trackingBean.setOrder_channel_id(order_channel_id);
        trackingBean.setTracking_no(tracking_no);
        trackingBean.setTracking_type(tracking_type);
        trackingBean.setSim_order_num("");
        trackingBean.setTracking_kind("0");
        trackingBean.setTracking_area("0");
        trackingBean.setStatus("01");
        trackingBean.setClear_port_flg("0");
        trackingBean.setUse_flg("1");
        trackingBean.setWeight_kg("0");
        trackingBean.setWeight_lb("0");
        trackingBean.setTracking_fee("0");
        trackingBean.setTracking_cost("0");
        trackingBean.setMain_flg("0");
        trackingBean.setSender_code("");
        trackingBean.setReceiver_code("");
        trackingBean.setSent_kd100_poll_flg("0");
        trackingBean.setSent_kd100_poll_time("");
        trackingBean.setSent_kd100_poll_count("0");
        trackingBean.setSent_kd100_flg("0");
        trackingBean.setPrint_type("0");
        trackingBean.setCreate_time(DateTimeUtil.getNow());
        trackingBean.setUpdate_time(DateTimeUtil.getNow());
        trackingBean.setCreate_person(getTaskName());
        trackingBean.setUpdate_person(getTaskName());

        return trackingBean;
    }

    /**
     * 品牌方取消订单时更新状态并插入日志
     */
    protected void SetBackOrderList(OrderChannelBean channel, List<ReservationClientBean>  backClientlList) throws Exception {

        for (ReservationClientBean reservationClientBean : backClientlList) {

            $info(channel.getFull_name()+ "-------Order_Number：" + reservationClientBean.getOrder_number());

            //res id 设定
            String[] arr = reservationClientBean.getRes_id().split(",");
            Long[] longResids = new Long[arr.length];
            int i = 0;
            for (String id : arr){
                longResids[i] = Long.valueOf(id);
                i = i + 1;
            }

            String notes = "Item cancelled by the " + channel.getFull_name();

            // 更新物品状态
            reservationDao.updateReservationBySynshipno(reservationClientBean.getSyn_ship_no(), CodeConstants.Reservation_Status.BackOrdered, getTaskName(),longResids);
            // 插入物品日志
            reservationDao.insertReservationLogByInResID(reservationClientBean.getSyn_ship_no(), notes, getTaskName(),longResids);
            // 更新订单的品牌方取消标志位
            orderDao.UpdateOrderCancelFlg(reservationClientBean.getOrder_number(), getTaskName());
            // 插入订单日志
            orderDao.insertOrderNotes(reservationClientBean.getOrder_number(), reservationClientBean.getSource_order_id(), notes, getTaskName());

        }

        String errorMail = sendErrorMail(backClientlList);

        if (!StringUtils.isNullOrBlank2(errorMail)) {
            $info(channel.getFull_name() + "品牌方取消邮件出力");
            String subject = String.format(SynshipConstants.EmailSynshipGetClientShipping.SUBJECT, channel.getFull_name());
            Mail.sendAlert(CodeConstants.EmailReceiver.NEED_SOLVE, subject, errorMail, true);
        }

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
