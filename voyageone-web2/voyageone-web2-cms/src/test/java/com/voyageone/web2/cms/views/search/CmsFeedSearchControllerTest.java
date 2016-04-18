package com.voyageone.web2.cms.views.search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author jiang, 2016/2/26
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsFeedSearchControllerTest {

    @Autowired
    CmsFeedSearchController targetController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(targetController).build();
    }

    // 测试检索出feed数据
    @Test
    public void testSearch1() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");
        userInfo.setUserName("j");
        userInfo.setUserId(26);

        Map<String, Object> rsMap = new HashMap<String, Object>();
        rsMap.put("pageNum", 0);
        rsMap.put("pageSize", 20);
        String paraStr = JacksonUtil.bean2Json(rsMap);

        MockHttpServletRequestBuilder mb = MockMvcRequestBuilders.post("/cms/search/feed/search").sessionAttr(BaseConstants.SESSION_USER, userInfo);
        mb.accept("application/json").contentType("application/json").content(paraStr);
        output(mb);
    }

    private void output(MockHttpServletRequestBuilder mb) throws Exception {
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