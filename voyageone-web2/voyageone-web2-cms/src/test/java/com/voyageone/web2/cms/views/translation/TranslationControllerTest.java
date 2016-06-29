package com.voyageone.web2.cms.views.translation;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.views.search.CmsFeedSearchController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.util.ajax.AjaxFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/6/29.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})

public class TranslationControllerTest {

    @Autowired
    TranslationController translationControllerTest;

    @Test
    public void testDoInit() throws Exception {
        AjaxResponse result = translationControllerTest.doInit();
        System.out.println(JsonUtil.bean2Json(result));
    }
}