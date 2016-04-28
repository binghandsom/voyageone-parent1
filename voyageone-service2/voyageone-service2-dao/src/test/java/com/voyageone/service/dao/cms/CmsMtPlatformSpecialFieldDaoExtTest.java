package com.voyageone.service.dao.cms;

import com.voyageone.service.daoext.cms.CmsMtPlatformSpecialFieldDaoExt;
import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2016/1/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtPlatformSpecialFieldDaoExtTest {

    @Autowired
    private CmsMtPlatformSpecialFieldDaoExt cmsMtPlatformSpecialFieldDaoExt;

    @Test
    public void testInsert() throws Exception {
        CmsMtPlatformSpecialFieldModel model = new CmsMtPlatformSpecialFieldModel();
        model.setCartId(1);
        model.setCatId("11");
        model.setFieldId("testFieldId22");
        model.setType("1");
        model.setCreater("liang");
        model.setModifier("liang");
        cmsMtPlatformSpecialFieldDaoExt.insert(model);
    }

    @Test
    public void testSelect() throws Exception {
        List<CmsMtPlatformSpecialFieldModel> list = cmsMtPlatformSpecialFieldDaoExt.select(1, null, null, null);
        for (CmsMtPlatformSpecialFieldModel model : list) {
            System.out.println(model.getCatId() + "," + model.getCartId() + "," + model.getFieldId() + "," + model.getType());
        }

        list = cmsMtPlatformSpecialFieldDaoExt.select(1, "11", null, null);
        for (CmsMtPlatformSpecialFieldModel model : list) {
            System.out.println(model.getCatId() + "," + model.getCartId() + "," + model.getFieldId() + "," + model.getType());
        }

        list = cmsMtPlatformSpecialFieldDaoExt.select(1, "11", "testFieldId11", null);
        for (CmsMtPlatformSpecialFieldModel model : list) {
            System.out.println(model.getCatId() + "," + model.getCartId() + "," + model.getFieldId() + "," + model.getType());
        }

        list = cmsMtPlatformSpecialFieldDaoExt.select(1, "11", "testFieldId11", "2");
        for (CmsMtPlatformSpecialFieldModel model : list) {
            System.out.println(model.getCatId() + "," + model.getCartId() + "," + model.getFieldId() + "," + model.getType());
        }
    }

    @Test
    public void testDelete() throws Exception {
        CmsMtPlatformSpecialFieldModel model = new CmsMtPlatformSpecialFieldModel();
        model.setCartId(1);
        model.setCatId("22");
        model.setFieldId("testFieldId22");
        model.setType("2");
        cmsMtPlatformSpecialFieldDaoExt.delete(model);
    }
}
