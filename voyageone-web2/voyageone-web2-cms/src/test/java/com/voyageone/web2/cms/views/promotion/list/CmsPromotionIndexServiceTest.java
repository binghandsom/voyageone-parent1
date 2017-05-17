package com.voyageone.web2.cms.views.promotion.list;

import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/2/24.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPromotionIndexServiceTest {

    @Autowired
    CmsPromotionIndexService cmsPromotionIndexService;
    @Test
    public void testGetCodeExcelFile() throws Exception {
        byte[] bytes = cmsPromotionIndexService.getCodeExcelFile(1793,"001");

        File f = new File("h:/promotion.xls");
        FileUtils.writeByteArrayToFile(f, bytes);
    }
}