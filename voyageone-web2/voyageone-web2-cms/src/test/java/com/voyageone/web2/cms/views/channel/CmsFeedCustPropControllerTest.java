package com.voyageone.web2.cms.views.channel;

import com.google.gson.*;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.cms.views.channel.CmsFeedCustPropController;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;

/**
 * @author jiang, 2016/2/26
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsFeedCustPropControllerTest {

    @Autowired
    CmsFeedCustPropController cmsFeedCustPropController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cmsFeedCustPropController).build();
    }

    // 测试类目属性查询(两个list)
    @Test
    public void testGetFeedCustProp1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/prop/get").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        mb.param("cat_path", "NOT_APPLICABLE - NOT_APPLICABLE - No Stone");
        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试取得类目属性一览
    @Test
    public void testGetFeedCustProp2() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/prop/get").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        mb.param("cat_path", "NOT_APPLICABLE - NOT_APPLICABLE - No Stone");
        mb.param("unsplitFlg", "1");
        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试取得类目树
    @Test
    public void testGetCatTree1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("015");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/prop/getCatTree").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试取得类目一览
    @Test
    public void testGetCatList1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("015");

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.get("/cms/channel/custom/prop/getCatList").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试保存类目属性
    @Test
    public void testSaveFeedCustProp1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");
        userInfo.setUserName("jason");

        String paraStr = "{\"cat_path\":\"NOT_APPLICABLE - NOT_APPLICABLE - No Stone\", " +
                "\"valList\":[{\"prop_id\":\"\", \"prop_original\":\"size\", \"prop_translation\":\"尺寸\"}]，" +
                "\"unvalList\":[{\"prop_id\":\"\", \"prop_original\":\"size\", \"prop_translation\":\"尺寸\"}] }";

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.post("/cms/channel/custom/prop/update").sessionAttr(BaseConstants.SESSION_USER, userInfo)
                .accept("application/json").contentType("application/json").content(paraStr);

        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }

    // 测试新增类目属性
    @Test
    public void testSaveFeedCustProp2() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");
        userInfo.setUserName("jason");

        String paraStr = "{\"cat_path\":\"NOT_APPLICABLE - NOT_APPLICABLE - No Stone\"," +
                "\"valList\":[{\"prop_id\":\"\", \"prop_original\":\"size\", \"prop_translation\":\"尺寸\"}] }";

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.post("/cms/channel/custom/prop/update").sessionAttr(BaseConstants.SESSION_USER, userInfo)
                .accept("application/json").contentType("application/json").content(paraStr);

        MockHttpServletResponse msr = mockMvc.perform(mb).andReturn().getResponse();
        String json = msr.getContentAsString();
        int sts = msr.getStatus();
        Gson gson3 = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String prettyJsonStr2 = gson3.toJson(je);
        System.out.println("prettyJsonStr: " + prettyJsonStr2);
        assertEquals(sts, 200);
    }
}