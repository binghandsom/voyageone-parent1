package com.voyageone.synship.controller;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.util.StringUtils;
import com.voyageone.synship.SynshipConstants.TrackingUrls;
import com.voyageone.synship.formbean.OrderTrackInfoBean;
import com.voyageone.synship.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(method = RequestMethod.POST)
public class TrackingController {

    @Autowired
    TrackingService trackingService;

    @RequestMapping(method = RequestMethod.GET, value = TrackingUrls.GET_TRACKING_JM_INFO, produces = {"text/html;charset=UTF-8"})
    public String getJMTrackingInfo(String cwb) throws UnsupportedEncodingException {

        String trackingInfo= trackingService.getTrackingInfo(cwb, PlatFormEnums.PlatForm.JM);
        if (StringUtils.isNullOrBlank2(trackingInfo)) {
            return null;
        } else {
            return  trackingInfo;
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = TrackingUrls.GET_TRACKING_CN_INFO, produces = {"text/html;charset=UTF-8"})
    public String getCNTrackingInfo(String cwb) throws UnsupportedEncodingException {

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
}
