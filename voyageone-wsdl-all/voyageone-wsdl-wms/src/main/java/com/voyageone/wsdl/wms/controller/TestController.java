package com.voyageone.wsdl.wms.controller;

import com.voyageone.common.configs.ChannelConfigs;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
public class TestController {

    @RequestMapping(method = RequestMethod.GET, value = "/test/getInfo")
    public String getTrackingInfo(String cwb) throws UnsupportedEncodingException {

        return "测试结果："+ ChannelConfigs.getChannel("001").getFull_name();

    }
}
