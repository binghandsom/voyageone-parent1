package com.voyageone.service.dao.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.CmsMtCommonPropModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016/4/25.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtCommonPropDaoTest {

    @Autowired
    private CmsMtCommonPropDao cmsMtCommonPropDao;

    @Test
    public void testSelect() throws Exception {

        System.out.println(JacksonUtil.bean2Json(cmsMtCommonPropDao.select(1)));
    }

    @Test
    public void testSelectList() throws Exception {
        Map map = new HashMap<String, Object>();
//        map.put("propId", "code");
        String jsonStr = JacksonUtil.bean2Json(cmsMtCommonPropDao.selectList(map));
        System.out.println(jsonStr);


    }

    @Test
    public void testSelectOne() throws Exception {
        Map map = new HashMap<String, Object>();
        map.put("propId", "code");

        String jsonStr = JacksonUtil.bean2Json(cmsMtCommonPropDao.selectOne(map));
        System.out.println(jsonStr);
        CmsMtCommonPropModel cmsMtCommonPropModel = JacksonUtil.json2Bean(jsonStr, CmsMtCommonPropModel.class);

        System.out.println(JacksonUtil.bean2Json(cmsMtCommonPropModel));

    }

    @Test
    public void testInsert() throws Exception {
        CmsMtCommonPropModel cmsMtCommonPropModel = new CmsMtCommonPropModel();
        cmsMtCommonPropModel.setPropId("test");
        cmsMtCommonPropModel.setPropParentId("test");
        cmsMtCommonPropModel.setPropName("test");
        cmsMtCommonPropModel.setPropType("test");
        cmsMtCommonPropModel.setActionType(1);
        cmsMtCommonPropModel.setPlatformPropRefId("1");
        cmsMtCommonPropModel.setRules("1");
        cmsMtCommonPropModel.setDefult("1");
        cmsMtCommonPropModel.setIsComm(1);
        cmsMtCommonPropModel.setIsCode(1);
        cmsMtCommonPropModel.setModified(new Date());
        cmsMtCommonPropModel.setModifier("test");
        cmsMtCommonPropModel.setCreater("test");
        cmsMtCommonPropModel.setCreated(new Date());
        System.out.println(cmsMtCommonPropDao.insert(cmsMtCommonPropModel));


    }

    @Test
    public void testUpdate() throws Exception {
        CmsMtCommonPropModel cmsMtCommonPropModel = new CmsMtCommonPropModel();
        cmsMtCommonPropModel.setId(65);
        cmsMtCommonPropModel.setCreater("test-----");
        cmsMtCommonPropModel.setCreated(new Date());
        System.out.println(cmsMtCommonPropDao.update(cmsMtCommonPropModel));

    }

    @Test
    public void testDelete() throws Exception {
        cmsMtCommonPropDao.delete(65);

    }
}