package com.voyageone.wsdl.oms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.voyageone.wsdl.core.Constants;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
public class OrderApplication {
    
    /**
     * 订单服务类
     */
    @Autowired
    private OrderController orderController;
}