package com.voyageone.service.daoext.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lewis on 15-12-8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtCommonPropDaoExtTest {

    @Autowired
    private CmsMtCommonPropDaoExt cmsMtCommonPropDaoExt;

    @Test
    public void testSelectActionModelList() throws Exception {
//        List<CmsMtCommonPropActionDefModel> delModelsById = cmsMtCommonPropDao.selectActionModelList();

//        Assert.assertTrue(delModelsById.size()>0);

//        for (MtCommPropActionDefModel model:delModelsById){
//            Assert.assertTrue(StringUtils.isEmpty(model.getPlatformPropRefId()));
//            Assert.assertTrue("1".equals(model.getActionType()));
//        }
//
//        List<MtCommPropActionDefModel> delModelsByIdAndName = cmsMtCommonPropDao.selectActionModelList("2",null);
//        for (MtCommPropActionDefModel model:delModelsByIdAndName){
//            Assert.assertTrue(StringUtils.isEmpty(model.getPlatformPropRefId()));
//            Assert.assertTrue("2".equals(model.getActionType()));
//        }
//
//        List<MtCommPropActionDefModel> updModels = cmsMtCommonPropDao.selectActionModelList("0","");
//        for (MtCommPropActionDefModel model:updModels){
//            Assert.assertFalse(StringUtils.isEmpty(model.getPlatformPropRefId()));
//            Assert.assertTrue("0".equals(model.getActionType()));
//        }
//
//        List<MtCommPropActionDefModel> addModels = cmsMtCommonPropDao.selectActionModelList("0",null);
//        for (MtCommPropActionDefModel model:addModels){
//            Assert.assertTrue(StringUtils.isEmpty(model.getPlatformPropRefId()));
//            Assert.assertTrue("0".equals(model.getActionType()));
//        }

    }
}