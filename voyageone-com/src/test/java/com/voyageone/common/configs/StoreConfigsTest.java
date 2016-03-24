package com.voyageone.common.configs;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/3/23.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class StoreConfigsTest {

    @Test
    public void testGetStore() throws Exception {
        System.out.println(JsonUtil.getJsonString(Stores.getStore(10)));
    }

    @Test
    public void testGetStore1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Stores.getStore(1)));
    }

    @Test
    public void testGetChannelStoreList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Stores.getChannelStoreList("001")));
    }

    @Test
    public void testGetChannelStoreList1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Stores.getChannelStoreList("001",true,true)));
    }

    @Test
    public void testGetStoreList() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Stores.getStoreList()));
    }

    @Test
    public void testGetStoreList1() throws Exception {
        System.out.println(JacksonUtil.bean2Json(Stores.getStoreList(true,true)));
        System.out.println(JacksonUtil.bean2Json(Stores.getStoreList(true,false)));
        System.out.println(JacksonUtil.bean2Json(Stores.getStoreList(false,true)));
        System.out.println(JacksonUtil.bean2Json(Stores.getStoreList(false,false)));
    }
}