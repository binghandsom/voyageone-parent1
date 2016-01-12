package com.voyageone.batch.synship.service.clientShippingInfo;

import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.synship.dao.ClientTrackingDao;
import com.voyageone.batch.synship.dao.OrderDao;
import com.voyageone.batch.synship.dao.ReservationDao;
import com.voyageone.batch.synship.modelbean.ReservationClientBean;
import com.voyageone.common.components.channelAdvisor.service.OrderService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbLogisticsService;
import com.voyageone.common.configs.beans.OrderChannelBean;
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
    protected IssueLog issueLog;

    @Autowired
    ClientTrackingDao clientTrackingDao;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    OrderDao orderDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SynshipGetClientShipping";
    }


    /**
     * 品牌方取消订单时更新状态并插入日志
     */
    protected void SetBackOrderList(OrderChannelBean channel, List<ReservationClientBean>  reservationClientlList) throws Exception {

        for (ReservationClientBean reservationClientBean : reservationClientlList) {

            //res id 设定
            String[] arr = reservationClientBean.getRes_id().split(",");
            Long[] longResids = new Long[arr.length];
            int i = 0;
            for (String id : arr){
                longResids[i] = Long.valueOf(id);
                i = i + 1;
            }

            reservationDao.updateReservationBySynshipno(reservationClientBean.getSyn_ship_no(), CodeConstants.Reservation_Status.BackOrdered, getTaskName(),longResids);
            // 插入相关记录到wms_bt_reservation_log中
            reservationDao.insertReservationLogByInResID(reservationClientBean.getSyn_ship_no(), "Item cancelled by the " + channel.getFull_name(), getTaskName(),longResids);

        }

        String errorMail = sendErrorMail(reservationClientlList);

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
