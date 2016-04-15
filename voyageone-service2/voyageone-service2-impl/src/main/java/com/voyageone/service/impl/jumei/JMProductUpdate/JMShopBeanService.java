package com.voyageone.service.impl.jumei.JMProductUpdate;

import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Service;

@Service
public class JMShopBeanService {
    public ShopBean getShopBean() {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        return shopBean;
    }
}
