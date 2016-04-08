package com.voyageone.components.channeladvisor.service;

import com.voyageone.components.channeladvisor.CaConstants;
import com.voyageone.components.channeladvisor.CaBase;
import com.voyageone.components.channeladvisor.bean.orders.OrderCriteria;
import com.voyageone.components.channeladvisor.soap.OrderListResSoapenv;
import com.voyageone.components.channeladvisor.soap.OrderListSoapenv;
import com.voyageone.components.channeladvisor.webservice.APICredentials;
import com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfOrderResponseItem;
import com.voyageone.components.channeladvisor.webservice.GetOrderList;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.JaxbUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by sn3 on 2015-08-14.
 * modify by sky on 2015-08-18
 */
@Service
public class OrderService extends CaBase {

    /**
     * 根据条件获取订单信息
     * @param orderCriteria 请求参数对象
     * @param configs 配置信息bean
     * @return OrderResponseItem bean对象
     */
    public APIResultOfArrayOfOrderResponseItem getOrderList(OrderCriteria orderCriteria, Map<String, ThirdPartyConfigBean> configs) throws Exception {
        String responseXmlStr = reqCaApiOnTimeoutRepert(configs.get(CaConstants.OrderList.URL).getProp_val1(),
                                                        configs.get(CaConstants.NAMESPACE).getProp_val1() + "GetOrderList",
                                                        createParamXmlStr(orderCriteria, configs));
        OrderListResSoapenv orderListResSoapenv =  xmlStr2Bean(responseXmlStr);
        return orderListResSoapenv.getBody().getOrderListResponse().getGetOrderListResult();
    }

    //设置请求参数，创建参数xml
    private String createParamXmlStr(OrderCriteria orderCriteria, Map<String, ThirdPartyConfigBean> configs) {
        try {
            //设置Hearder内容 apiCredentials 对象
            APICredentials apiCredentials = new APICredentials();
            apiCredentials.setDeveloperKey(configs.get(CaConstants.DEVELOPERKEY).getProp_val1());
            apiCredentials.setPassword(configs.get(CaConstants.PASSWORD).getProp_val1());
            //设置body内容 GetOrderList 对象
            GetOrderList getOrderList = new GetOrderList();
            getOrderList.setAccountID(configs.get(CaConstants.ACCOUNTID).getProp_val1());
            getOrderList.setOrderCriteria(orderCriteria);
            return beanToXml(apiCredentials, getOrderList);
        }catch (Exception e){
            throw new RuntimeException("创建参数xml文件出错：" + e);
        }
    }

    //将参数bean转化成参数xml
    private String beanToXml(APICredentials aPICredentials, GetOrderList getOrderList){
        try {
            OrderListSoapenv soapenv = new OrderListSoapenv(aPICredentials, getOrderList);
            return JaxbUtil.convertToXml(soapenv, "UTF-8");
            //logger.info("参数xml内容：" + xmlStr);
            //return xmlStr;
        }catch (Exception e){
            throw new RuntimeException("将bean转化成Xml错误" + e);
        }
    }

    //将结果集合的xmlStr转化成bean
    private OrderListResSoapenv xmlStr2Bean(String xmlStr) {
        try {
            return JaxbUtil.converyToJavaBean(xmlStr, OrderListResSoapenv.class);
        }catch (Exception e){
            throw new RuntimeException("返回的xmlStr转化成OrderListResSoapenv时发生错误" + e);
        }
    }
}
