package com.voyageone.service.impl.cms.jumei.platefrom;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Service;

@Service
public class JMShopBeanService {
    public ShopBean getShopBean(String channelId) {
        ShopBean shopBean = new ShopBean();
//        shopBean.setAppKey("72");
//        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
//        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
//        shopBean.setApp_url("http://182.138.102.82:8868/");

        //登陆账号aimee2      123456abc@
//        商家名称:
//        aimee2
//        商家ID(Client_id):
//        110
//        商家键值(Client_key):
//        5b533889ddab5eb7ef096ca2a28bddfa
//        接口签名(Sign):
//        5b8a445b9d1b3d0f3d2313067ac93931ce8cae59
//        商家名称:
//        aimee2
//        商家ID(Client_id):
//        110
//        商家键值(Client_key):
//        b50ef86b6acc34eb2249fe57f2ee963e
//        接口签名(Sign):
//        c5ddadb0633516c3bbb38041081458b3a4c65d6a
//                重置
        //  shopBean.setAppKey("110");
        //  shopBean.setSessionKey("b50ef86b6acc34eb2249fe57f2ee963e");
        //   shopBean.setAppSecret("c5ddadb0633516c3bbb38041081458b3a4c65d6a");
        // shopBean.setApp_url("http://182.138.102.82:8868/");
        shopBean = Shops.getShop(channelId, CartEnums.Cart.JM.getId());
        return shopBean;
    }
}
