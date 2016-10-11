package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by jonas on 2016/9/20.
 *
 * @version 2.6.0
 * @since 2.6.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TbSimpleItemServiceTest {
    @Autowired
    private TbSimpleItemService tbSimpleItemService;

    @Test
    public void testGetSimpleItem() throws GetUpdateSchemaFailException, ApiException, TopSchemaException {

        String channelId = "010";
        Integer cartId = 30;
        Long numIId = 537818716734L;

        ShopBean shopProp = new ShopBean();
        shopProp.setOrder_channel_id(channelId);
        shopProp.setCart_id(String.valueOf(cartId));
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("23239809");
        shopProp.setAppSecret("34fb2f57498bc6b00384da175021e587");
        shopProp.setSessionKey("6100330f76a107e76570295d6a3f2d7295f98415d0d2b1e2640015666");
        shopProp.setPlatform_id("1");

        TbItemSchema tbItemSchema = tbSimpleItemService.getSimpleItem(shopProp, numIId);

        tbItemSchema.setFieldValueWithDefault();

        tbItemSchema.setMainImage(new HashMap<Integer, String>(){{
            put(1, "http://img.alicdn.com/imgextra/i3/2640015666/TB2RySHaGnyQeBjy1zkXXXmyXXa_!!2640015666.jpg");
            put(2, "http://img.alicdn.com/imgextra/i3/2640015666/TB2RySHaGnyQeBjy1zkXXXmyXXa_!!2640015666.jpg");
            put(3, "http://img.alicdn.com/imgextra/i3/2640015666/TB2RySHaGnyQeBjy1zkXXXmyXXa_!!2640015666.jpg");
        }});

        String result = tbSimpleItemService.updateSimpleItem(shopProp, tbItemSchema);

        System.out.println("\n\n" + result + "\n\n");
    }
}