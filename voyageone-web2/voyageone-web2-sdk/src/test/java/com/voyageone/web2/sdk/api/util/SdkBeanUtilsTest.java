package com.voyageone.web2.sdk.api.util;

import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 2016/1/19.
 */
public class SdkBeanUtilsTest {

    @Test
    public void copyPropertiesTest1() {

    }

    @Test
    public void copyPropertiesTest2() throws InvocationTargetException, IllegalAccessException {
        PromotionsGetRequest request = new PromotionsGetRequest();
        request.setChannelId("aaa");
        request.setPromotionName("ff");
        Map target = new HashMap();
        SdkBeanUtils.copyProperties(request, target);
        System.out.println(target.get("channelId"));
        System.out.println(target.get("promotionName"));
    }
}
