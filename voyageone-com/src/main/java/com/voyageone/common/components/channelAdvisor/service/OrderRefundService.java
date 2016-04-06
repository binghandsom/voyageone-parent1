package com.voyageone.common.components.channelAdvisor.service;

import com.voyageone.common.components.channelAdvisor.CaConstants;
import com.voyageone.common.components.channelAdvisor.base.CaBase;
import com.voyageone.common.components.channelAdvisor.bean.orders.RefundOrderRequest;
import com.voyageone.common.components.channelAdvisor.soap.OrderRefundResSoapenv;
import com.voyageone.common.components.channelAdvisor.soap.OrderRefundSoapenv;
import com.voyageone.common.components.channelAdvisor.webservices.APICredentials;
import com.voyageone.common.components.channelAdvisor.webservices.APIResultOfRefundOrderResponse;
import com.voyageone.common.components.channelAdvisor.webservices.SubmitOrderRefund;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.JaxbUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * update by Jerry on 2015-08-18.
 */
@Component
public class OrderRefundService extends CaBase {

    private final static String SUBMIT_ORDER_REFUND = "SubmitOrderRefund";

    /**
     * 订单取消/退货
     * @param refundOrderRequest 参数bean
     * @param configs 渠道配置信息
     * @return String
     * @throws Exception
     */
    public APIResultOfRefundOrderResponse submitOrderRefund(RefundOrderRequest refundOrderRequest, Map<String, ThirdPartyConfigBean> configs) throws Exception {
        String requestXmlStr = createParamXmlStr(refundOrderRequest, configs);
        //  请求XML保存
        backupTheXmlFile(refundOrderRequest.getClientOrderIdentifier(), requestXmlStr, 0);

        // CA 请求
        String responseXmlStr = reqCaApiOnTimeoutRepert(configs.get(CaConstants.URL_ORDER).getProp_val1(),
                                                            configs.get(CaConstants.NAMESPACE).getProp_val1() + SUBMIT_ORDER_REFUND,
                                                            requestXmlStr);
        //  响应XML保存
        backupTheXmlFile(refundOrderRequest.getClientOrderIdentifier(), responseXmlStr, 1);
        //  相应Bean转化
        OrderRefundResSoapenv orderRefundResSoapenv =  xmlStr2Bean(responseXmlStr);
        return orderRefundResSoapenv.getBody().getSubmitOrderRefundResponse().getSubmitOrderRefundResult();
    }

    //设置请求参数，创建参数xml
    private String createParamXmlStr(RefundOrderRequest refundOrderRequest, Map<String, ThirdPartyConfigBean> configs) {
        try {
            //设置Hearder内容 apiCredentials 对象
            APICredentials apiCredentials = new APICredentials();
            apiCredentials.setDeveloperKey(configs.get(CaConstants.DEVELOPERKEY).getProp_val1());
            apiCredentials.setPassword(configs.get(CaConstants.PASSWORD).getProp_val1());
            //设置body内容 submitOrderRefund 对象
            SubmitOrderRefund submitOrderRefund = new SubmitOrderRefund();
            submitOrderRefund.setAccountID(configs.get(CaConstants.ACCOUNTID).getProp_val1());
            submitOrderRefund.setRequest(refundOrderRequest);
            return beanToXml(apiCredentials, submitOrderRefund);
        }catch (Exception e){
            throw new RuntimeException("创建参数xml文件出错：" + e);
        }
    }

    //将参数bean转化成参数xml
    private String beanToXml(APICredentials aPICredentials,  SubmitOrderRefund submitOrderRefund){
        try {
            OrderRefundSoapenv soapenv = new OrderRefundSoapenv(aPICredentials, submitOrderRefund);
            String xmlStr = JaxbUtil.convertToXml(soapenv, "UTF-8");
            logger.info("参数xml内容：" + xmlStr);
            return xmlStr;
        }catch (Exception e){
            throw new RuntimeException("将bean转化成Xml错误" + e);
        }
    }

    //将结果集合的xmlStr转化成bean
    private OrderRefundResSoapenv xmlStr2Bean(String xmlStr) {
        try {
            return JaxbUtil.converyToJavaBean(xmlStr, OrderRefundResSoapenv.class);
        }catch (Exception e){
            throw new RuntimeException("返回的xmlStr转化成OrderRefundResSoapenv时发生错误" + e);
        }
    }
}