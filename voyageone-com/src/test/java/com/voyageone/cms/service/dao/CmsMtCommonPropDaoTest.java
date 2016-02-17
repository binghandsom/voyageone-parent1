package com.voyageone.cms.service.dao;

import com.voyageone.cms.service.model.MtCommPropActionDefModel;
import com.voyageone.common.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lewis on 15-12-8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtCommonPropDaoTest {

    @Autowired
    private CmsMtCommonPropDao cmsMtCommonPropDao;

    @Test
    public void testGetActionModelList() throws Exception {
        List<MtCommPropActionDefModel> delModelsById = cmsMtCommonPropDao.getActionModelList();

        Assert.assertTrue(delModelsById.size()>0);

//        for (MtCommPropActionDefModel model:delModelsById){
//            Assert.assertTrue(StringUtils.isEmpty(model.getPlatformPropRefId()));
//            Assert.assertTrue("1".equals(model.getActionType()));
//        }
//
//        List<MtCommPropActionDefModel> delModelsByIdAndName = cmsMtCommonPropDao.getActionModelList("2",null);
//        for (MtCommPropActionDefModel model:delModelsByIdAndName){
//            Assert.assertTrue(StringUtils.isEmpty(model.getPlatformPropRefId()));
//            Assert.assertTrue("2".equals(model.getActionType()));
//        }
//
//        List<MtCommPropActionDefModel> updModels = cmsMtCommonPropDao.getActionModelList("0","");
//        for (MtCommPropActionDefModel model:updModels){
//            Assert.assertFalse(StringUtils.isEmpty(model.getPlatformPropRefId()));
//            Assert.assertTrue("0".equals(model.getActionType()));
//        }
//
//        List<MtCommPropActionDefModel> addModels = cmsMtCommonPropDao.getActionModelList("0",null);
//        for (MtCommPropActionDefModel model:addModels){
//            Assert.assertTrue(StringUtils.isEmpty(model.getPlatformPropRefId()));
//            Assert.assertTrue("0".equals(model.getActionType()));
//        }

    }
}