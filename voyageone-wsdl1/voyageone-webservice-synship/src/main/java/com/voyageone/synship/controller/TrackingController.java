package com.voyageone.synship.controller;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.util.StringUtils;
import com.voyageone.synship.SynshipConstants.TrackingType;
import com.voyageone.synship.SynshipConstants.TrackingUrls;
import com.voyageone.synship.formbean.OrderTrackInfoBean;
import com.voyageone.synship.service.TrackingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(method = RequestMethod.POST)
public class TrackingController {

    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    TrackingService trackingService;

    @RequestMapping(method = RequestMethod.GET, value = TrackingUrls.GET_TRACKING_JM_INFO, produces = {"text/html;charset=UTF-8"})
    public String getJMTrackingInfo(String cwb) throws UnsupportedEncodingException {

        logger.info("物流查询，cwb：" + cwb);

        String trackingInfo= trackingService.getTrackingInfo(cwb, PlatFormEnums.PlatForm.JM);
        if (StringUtils.isNullOrBlank2(trackingInfo)) {
            return null;
        } else {
            return  trackingInfo;
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = TrackingUrls.GET_TRACKING_CN_INFO, produces = {"text/html;charset=UTF-8"})
    public String getCNTrackingInfo(String cwb) throws UnsupportedEncodingException {

        logger.info("物流查询，cwb：" + cwb);

        // 根据传入的参数取得订单相关情报
        OrderTrackInfoBean orderTrackInfoBean = trackingService.getOrderTrackInfo(cwb);
        if (orderTrackInfoBean == null || StringUtils.isNullOrBlank2(orderTrackInfoBean.getSyn_ship_no())) {
            return null;
        }

        // 按照官网方式查询物流
        String trackingInfo= trackingService.getTrackingInfo(orderTrackInfoBean.getSyn_ship_no(), PlatFormEnums.PlatForm.CN);
        if (StringUtils.isNullOrBlank2(trackingInfo)) {
            return null;
        } else {
            return  trackingInfo;
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = TrackingUrls.GET_TRACKING_INFO, produces = {"text/html;charset=UTF-8"})
    public String getCNTrackingInfo(String cwb,String type) throws UnsupportedEncodingException {

        logger.info("物流查询，cwb：" + cwb + "，type："+ type);

        OrderTrackInfoBean orderTrackInfoBean = new OrderTrackInfoBean();

        switch (type) {
            // 根据Source_order_id查询
            case TrackingType.WEBID:
                orderTrackInfoBean = trackingService.getSynshipNoByWebid(cwb);
                break;

            // 根据电话号码查询
            case TrackingType.PHONE:
                orderTrackInfoBean = trackingService.getSynshipNoByPhone(cwb);
                break;

            // 根据syn_ship_no查询
            case TrackingType.SYNSHIPNO:
                orderTrackInfoBean.setSyn_ship_no(cwb);
                break;

            // 根据快递单号查询
            case TrackingType.TRACKINGNO:
                orderTrackInfoBean = trackingService.getSynshipNoByTrackingNo(cwb);
                break;

            // 根据订单号查询
            case TrackingType.ORDERNUM:
                orderTrackInfoBean = trackingService.getSynshipNoByOrderNum(cwb);
                break;
        }

        String syn_ship_no = "";
        if (orderTrackInfoBean == null || StringUtils.isNullOrBlank2(orderTrackInfoBean.getSyn_ship_no())) {
            return null;
        }else {
            syn_ship_no = orderTrackInfoBean.getSyn_ship_no();
        }

        // 按照共通方式查询物流
        String trackingInfo= trackingService.getTrackingInfo(syn_ship_no, PlatFormEnums.PlatForm.COM);
        if (StringUtils.isNullOrBlank2(trackingInfo)) {
            return null;
        } else {
            return  trackingInfo;
        }

    }
}
