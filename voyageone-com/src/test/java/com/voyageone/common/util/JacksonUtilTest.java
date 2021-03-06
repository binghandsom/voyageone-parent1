package com.voyageone.common.util;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.support.VOBsonQueryFactory;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.beanutils.BeanUtils;
import org.jongo.query.Query;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2016/1/28.
 */
public class JacksonUtilTest {

    @Test
    public void testBean2Json() throws IOException {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");

        String str = JacksonUtil.bean2Json(shopBean);
        System.out.println(str);
    }

    @Test
    public void testJson2Bean() {
        String str = "{\"cart_id\":null,\"cart_type\":null,\"platform_id\":null,\"order_channel_id\":null,\"appKey\":\"72\",\"appSecret\":\"62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac\",\"sessionKey\":\"e5f9d143815a520726576040460bd67f\",\"app_url\":\"http://182.138.102.82:8868/\",\"shop_name\":null,\"platform\":null,\"comment\":null,\"cart_name\":null}";
        ShopBean shopBean = JacksonUtil.json2Bean(str, ShopBean.class);
        System.out.println(shopBean.getAppKey()+";"+shopBean.getSessionKey());
    }

    @Test
    public void testJsonToMap() {
        String str = "{\"cart_id\":null,\"cart_type\":null,\"platform_id\":null,\"order_channel_id\":null,\"appKey\":\"72\",\"appSecret\":\"62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac\",\"sessionKey\":\"e5f9d143815a520726576040460bd67f\",\"app_url\":\"http://182.138.102.82:8868/\",\"shop_name\":null,\"platform\":null,\"comment\":null,\"cart_name\":null}";
        Map<String, Object> map = JacksonUtil.jsonToMap(str);
        System.out.println(map.get("appKey")+";"+map.get("sessionKey"));
    }

    @Test
    public void testJsonToBeanList() {
        String str = "[{\"cart_id\":null,\"cart_type\":null,\"platform_id\":null,\"order_channel_id\":null,\"appKey\":\"72\",\"appSecret\":\"62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac\",\"sessionKey\":\"e5f9d143815a520726576040460bd67f\",\"app_url\":\"http://182.138.102.82:8868/\",\"shop_name\":null,\"platform\":null,\"comment\":null,\"cart_name\":null}]";
        List<ShopBean> list = JacksonUtil.jsonToBeanList(str, ShopBean.class);
        System.out.println(list.get(0).getAppKey() + ";" + list.get(0).getSessionKey());
    }

    @Test
    public void testJsonToMapList() {
        String str = "[{\"cart_id\":null,\"cart_type\":null,\"platform_id\":null,\"order_channel_id\":null,\"appKey\":\"72\",\"appSecret\":\"62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac\",\"sessionKey\":\"e5f9d143815a520726576040460bd67f\",\"app_url\":\"http://182.138.102.82:8868/\",\"shop_name\":null,\"platform\":null,\"comment\":null,\"cart_name\":null}]";
        List<Map<String, Object>> list = JacksonUtil.jsonToMapList(str);
        System.out.println(list.get(0).get("appKey")+";"+list.get(0).get("sessionKey"));
    }

    @Test
    public void test111() {
        String[] aa = new String[0];
        System.out.println(JacksonUtil.bean2Json(aa));
    }

    @Test
    public void test222() {
        String str = "{$and:[{$or:[{\"brand\":\"American West\"}]},{\"updFlg\":#}]}";
        Object[] params = new Object[1];
        params[0] = "aaa\"afasfd";
        VOBsonQueryFactory queryFactory = new VOBsonQueryFactory();
        Query query = queryFactory.createQuery(str, params);
        System.out.println(query.toDBObject().toMap());
    }

    @Test
    public void test223() {
        String str = "{$and:[{$or:[{\"brand\":\"American West\"}]},{\"updFlg\":#}]}";
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(str);
        jongoQuery.addParameters("aaa\"afasfd");
        System.out.println(jongoQuery.getQueryMap());
    }

    @Test
    public void testBean2JsonNotNull() {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url(null);

        String startMap = JacksonUtil.bean2JsonNotNull(shopBean);
        System.out.println(startMap);

        Map mapp = null;
        try {
            mapp = BeanUtils.describe(shopBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mapp);
    }


    @Test
    public void bean2Map() {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url(null);

        Map mapp = JacksonUtil.bean2Map(shopBean);
        System.out.println(mapp);
    }
}
