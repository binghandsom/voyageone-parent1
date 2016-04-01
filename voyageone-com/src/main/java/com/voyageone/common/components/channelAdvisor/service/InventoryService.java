package com.voyageone.common.components.channelAdvisor.service;

import com.voyageone.common.Constants;
import com.voyageone.common.components.channelAdvisor.base.CaBase;

import com.voyageone.common.components.channelAdvisor.bean.inventory.GetInventoryParamBean;
import com.voyageone.common.components.channelAdvisor.soap.InventoryItemResSoapenv;
import com.voyageone.common.components.channelAdvisor.soap.InventoryItemSoapenv;
import com.voyageone.common.components.channelAdvisor.soap.InventoryResSoapenv;
import com.voyageone.common.components.channelAdvisor.soap.InventorySoapenv;
import com.voyageone.common.components.channelAdvisor.webservices.*;
import com.voyageone.common.util.JaxbUtil;

import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * update by sky on 2015-07-30.
 */
@Component
public class InventoryService extends CaBase {
    /**
     * 取得商品库存
     * @param paramBean 参数bean
     * @return String
     * @throws Exception
     */
    public InventoryResSoapenv getCaInventory(GetInventoryParamBean paramBean, String updateType) throws Exception {
        String responseXmlStr = reqCaApiOnTimeoutRepert(paramBean.getPostUrl(), paramBean.getNameSpace() + paramBean.getPostAction(), createParamXmlStr(paramBean, updateType));
        return xmlStr2Bean(responseXmlStr);
    }

    //创建参数xml
    private String createParamXmlStr(GetInventoryParamBean paramBean, String updateType) {
        try {
            APICredentials apiCredentials = new APICredentials();
            apiCredentials.setDeveloperKey(paramBean.getDeveloperKey());
            apiCredentials.setPassword(paramBean.getPassword());
            return beanToXml(apiCredentials, setParamBean(paramBean, updateType));
        }catch (Exception e){
            throw new RuntimeException("创建参数xml文件出错：" + e);
        }
    }

    //设置请求参数bean
    private GetFilteredInventoryItemList setParamBean(GetInventoryParamBean paramBean, String updateType) throws DatatypeConfigurationException {
        GetFilteredInventoryItemList inventoryReq = new GetFilteredInventoryItemList();
        InventoryItemCriteria inventoryItemCriteria = new InventoryItemCriteria();
        inventoryItemCriteria.setLabelName(paramBean.getLabelName());
        inventoryItemCriteria.setPageNumber(paramBean.getnPageIndex());
        inventoryItemCriteria.setPageSize(paramBean.getnPageSize());
        //增量更新时候，无需设置的值
        if(Constants.updateClientInventoryType.INCREACE.equals(updateType)) {
            inventoryItemCriteria.setDateRangeStartGMT(paramBean.getDateTimeStart());
            inventoryItemCriteria.setDateRangeEndGMT(paramBean.getDateTimeEnd());
            inventoryItemCriteria.setDateRangeField(paramBean.getDateRangeField());
        }
        InventoryItemDetailLevel inventoryItemDetailLevel = new InventoryItemDetailLevel();
        inventoryItemDetailLevel.setIncludeQuantityInfo(true);
        inventoryReq.setAccountID(paramBean.getAccountId());
        inventoryReq.setItemCriteria(inventoryItemCriteria);
        inventoryReq.setDetailLevel(inventoryItemDetailLevel);
        return inventoryReq;
    }

    //将参数bean转化成参数xml
    private String beanToXml(APICredentials aPICredentials,GetFilteredInventoryItemList getFilteredInventoryItemListRequest){
        try {
            InventorySoapenv soapenv = new InventorySoapenv(aPICredentials, getFilteredInventoryItemListRequest);
            String xmlStr = JaxbUtil.convertToXml(soapenv, "UTF-8");
            //logger.info("参数xml内容：" + xmlStr);
            return xmlStr;
        }catch (Exception e){
            throw new RuntimeException("将bean转化成Xml错误" + e);
        }
    }

    //将结果集合的xmlStr转化成bean
    private InventoryResSoapenv xmlStr2Bean(String xmlStr) {
        try {
            return JaxbUtil.converyToJavaBean(xmlStr, InventoryResSoapenv.class);
        }catch (Exception e){
            throw new RuntimeException("返回的xmlStr转化成InventoryResSoapenv时发生错误" + e);
        }
    }

    /**
     * 取得商品库存
     * @param paramBean 参数bean
     * @return String
     * @throws Exception
     */
    public InventoryItemResSoapenv GetInventoryItemList(GetInventoryParamBean paramBean) throws Exception {
        String responseXmlStr = reqCaApiOnTimeoutRepert(paramBean.getPostUrl(), paramBean.getNameSpace() + paramBean.getPostAction(), createParamXmlStr(paramBean));
        return xmlStr2BeanItem(responseXmlStr);
    }

    //创建参数xml
    private String createParamXmlStr(GetInventoryParamBean paramBean) {
        try {
            APICredentials apiCredentials = new APICredentials();
            apiCredentials.setDeveloperKey(paramBean.getDeveloperKey());
            apiCredentials.setPassword(paramBean.getPassword());
            return beanToXml(apiCredentials, setParamBean(paramBean));
        }catch (Exception e){
            throw new RuntimeException("创建参数xml文件出错：" + e);
        }
    }

    //设置请求参数bean
    private GetInventoryItemList setParamBean(GetInventoryParamBean paramBean) throws DatatypeConfigurationException {
        GetInventoryItemList inventoryReq = new GetInventoryItemList();
        inventoryReq.setAccountID(paramBean.getAccountId());
        GetInventorySkuList inventorySkuList = new GetInventorySkuList();
        inventorySkuList.setString(paramBean.getSkuList());
        inventoryReq.setSkuList(inventorySkuList);
        return inventoryReq;
    }

    //将参数bean转化成参数xml
    private String beanToXml(APICredentials aPICredentials,GetInventoryItemList getInventoryItemListRequest){
        try {
            InventoryItemSoapenv soapenv = new InventoryItemSoapenv(aPICredentials, getInventoryItemListRequest);
            String xmlStr = JaxbUtil.convertToXml(soapenv, "UTF-8");
            //logger.info("参数xml内容：" + xmlStr);
            return xmlStr;
        }catch (Exception e){
            throw new RuntimeException("将bean转化成Xml错误" + e);
        }
    }

    //将结果集合的xmlStr转化成bean
    private InventoryItemResSoapenv xmlStr2BeanItem(String xmlStr) {
        try {
            return JaxbUtil.converyToJavaBean(xmlStr, InventoryItemResSoapenv.class);
        }catch (Exception e){
            throw new RuntimeException("返回的xmlStr转化成InventoryResSoapenv时发生错误" + e);
        }
    }
}