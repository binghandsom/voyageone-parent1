package com.voyageone.service.dao.cms.mongo.sh;

import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by yangjindong on 2017/5/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-sh.xml")
public class CmsBtProductGroupDaoTest {
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Test
    public void testSelectGroupInfoByMoreProductCodes() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.selectGroupInfoByMoreProductCodes("928", 27);
        long end = System.currentTimeMillis();
        System.out.println("selectProductByIds cost:" + (end - start));
//        Assert.assertEquals(grpList.size(), 4);
//        Assert.assertEquals(grpList.get(0).getCartId().intValue(), 27);
    }

}
