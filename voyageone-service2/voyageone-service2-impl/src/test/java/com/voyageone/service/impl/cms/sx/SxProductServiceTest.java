package com.voyageone.service.impl.cms.sx;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsMtCategoryTreeAllBean;
import com.voyageone.service.impl.cms.CategoryTreeAllService;
import com.voyageone.service.impl.cms.CategoryTreeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author james.li on 2016/5/12.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SxProductServiceTest {

    @Autowired
    private SxProductService sxProductService;

    @Test
    public void insertSxWorkLoadTest() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        sxProductService.insertSxWorkLoad("018", codeList, null, "tester");
    }

    @Test
    public void insertSxWorkLoadTest_WithCart() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        sxProductService.insertSxWorkLoad("018", codeList, 23, "tester");
    }


}