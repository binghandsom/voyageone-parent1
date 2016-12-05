package com.voyageone.task2.cms.service.product.batch;

import com.voyageone.common.util.JacksonUtil;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/11/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchAsynProcessServiceTest {

    @Autowired
    CmsAdvSearchAsynProcessService cmsAdvSearchAsynProcessService;
    @Test
    public void onStartup() throws Exception {

        Map<String,Object> map = new HashedMap();
//        param={cartId=27, _option=refreshRetailPrice, productIds=[49896], isSelAll=0, cartIds=[27], _taskName=refreshRetailPrice, _channleId=017, _userName=james}
        map.put("_taskName","refreshRetailPrice");
        map.put("cartId",27);
        map.put("_option","refreshRetailPrice");
        map.put("productIds", Arrays.asList("53356"));
        map.put("isSelAll",0);
        map.put("cartIds",Arrays.asList(27));
        map.put("_channleId","017");
        map.put("_userName","james");

        cmsAdvSearchAsynProcessService.onStartup(map);
    }
    @Test
    public  void  testSaveChannelCategory() throws Exception {
        String str="{\"sellerCats\":[{\"cId\":\"31\",\"cIds\":[\"10\",\"31\"],\"cName\":\"鞋履>靴子\",\"cNames\":[\"鞋履\",\"靴子\"]},{\"cId\":\"32\",\"cIds\":[\"10\",\"32\"],\"cName\":\"鞋履>运动鞋\",\"cNames\":[\"鞋履\",\"运动鞋\"]},{\"cId\":\"34\",\"cIds\":[\"10\",\"34\"],\"cName\":\"鞋履>皮鞋\",\"cNames\":[\"鞋履\",\"皮鞋\"]},{\"cId\":\"35\",\"cIds\":[\"10\",\"35\"],\"cName\":\"鞋履>休闲鞋\",\"cNames\":[\"鞋履\",\"休闲鞋\"]},{\"cId\":\"37\",\"cIds\":[\"10\",\"37\"],\"cName\":\"鞋履>凉鞋\",\"cNames\":[\"鞋履\",\"凉鞋\"]},{\"cId\":\"38\",\"cIds\":[\"10\",\"38\"],\"cName\":\"鞋履>长靴\",\"cNames\":[\"鞋履\",\"长靴\"]},{\"cId\":\"39\",\"cIds\":[\"10\",\"39\"],\"cName\":\"鞋履>短靴\",\"cNames\":[\"鞋履\",\"短靴\"]},{\"cId\":\"40\",\"cIds\":[\"10\",\"40\"],\"cName\":\"鞋履>平底鞋\",\"cNames\":[\"鞋履\",\"平底鞋\"]}],\"cartId\":32,\"_orgDispList\":[],\"productIds\":[\"144196\",\"122287\",\"146509\",\"55008\",\"53241\",\"134892\",\"105992\",\"54330\",\"134206\",\"53391\",\"149736\",\"56247\",\"68499\",\"103931\",\"103244\",\"127285\",\"105948\",\"108292\",\"53390\",\"105205\",\"69894\",\"59531\",\"103236\",\"104095\",\"150190\",\"127150\",\"108230\",\"117861\",\"54253\",\"105990\",\"53547\",\"139670\",\"75584\",\"109331\",\"107377\",\"54863\",\"65941\",\"138967\",\"106610\",\"66886\",\"65942\",\"100407\",\"79250\",\"100400\",\"67171\",\"53188\",\"143991\",\"59530\",\"122418\",\"65398\",\"104476\",\"137408\",\"100463\",\"58070\",\"70013\",\"122402\",\"100759\",\"107372\",\"54090\",\"150187\",\"58384\",\"56248\",\"54578\",\"48928\",\"57297\",\"105206\",\"66588\",\"63882\",\"118183\",\"51957\",\"56856\",\"71607\",\"49896\",\"54413\",\"72287\",\"66590\",\"118232\",\"100533\",\"107351\",\"71580\",\"136170\",\"101010\",\"56590\",\"66560\",\"127663\",\"54709\",\"101565\",\"120158\",\"65823\",\"115682\",\"114189\",\"54254\",\"57305\",\"118035\",\"83118\",\"77874\",\"106179\",\"72588\",\"52943\",\"125559\"],\"isSelAll\":0,\"channelId\":\"928\",\"userName\":\"will\",\"_taskName\":\"saveChannelCategory\"}";

        Map<String,Object> map =  JacksonUtil.jsonToMap(str);
        cmsAdvSearchAsynProcessService.onStartup(map);
    }

}