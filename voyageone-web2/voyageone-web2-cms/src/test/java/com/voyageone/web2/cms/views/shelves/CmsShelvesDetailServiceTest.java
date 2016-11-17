package com.voyageone.web2.cms.views.shelves;

import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsShelvesDetailServiceTest {

    @Autowired
    CmsShelvesDetailService cmsShelvesDetailService;
    @Test
    public void getShelvesInfo() throws Exception {

//        CacheHelper.getValueOperation().set("ShelvesMonitor_" + 1, 1, 10, TimeUnit.SECONDS);

        List<CmsBtShelvesInfoBean> cmsBtShelvesInfoBeen = cmsShelvesDetailService.getShelvesInfo(Arrays.asList(1),false);
        System.out.print(JacksonUtil.bean2Json(cmsBtShelvesInfoBeen));
    }

}