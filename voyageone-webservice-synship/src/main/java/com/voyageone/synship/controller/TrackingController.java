package com.voyageone.synship.controller;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.util.StringUtils;
import com.voyageone.synship.SynshipConstants.TrackingUrls;
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
    public String getTrackingInfo(String cwb) throws UnsupportedEncodingException {

        String trackingInfo= trackingService.getTrackingInfo(cwb, PlatFormEnums.PlatForm.JM);
        if (StringUtils.isNullOrBlank2(trackingInfo)) {
            return null;
        } else {
            return  trackingInfo;
        }

    }
}
