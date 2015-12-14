package com.voyageone.batch.cms.service;

import com.voyageone.base.dao.mongodb.model.CmsMtCategorySchemaModel;
import com.voyageone.batch.cms.dao.mongo.CmsMtPlatformFieldsRemoveHistoryDao;
import com.voyageone.cms.service.dao.CmsMtCommonPropDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.MtCommPropActionDefModel;
import com.voyageone.common.configs.Enums.ActionType;
import com.voyageone.common.masterdate.schema.Util.FieldUtil;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.StringUtils;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by lewis on 15-12-10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class MasterCatSchemaBuildFromTmallServiceTest {

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    @Autowired
    MasterCatSchemaBuildFromTmallService masterCatSchemaBuildFromTmallService;

    @Autowired
    private CmsMtCommonPropDao cmsMtCommonPropDao;

    @Test
    public void testOnStartup() throws Exception {
        System.out.println("MasterCatSchemaBuildFromTmallService start..." );
        masterCatSchemaBuildFromTmallService.startup();
        System.out.println("MasterCatSchemaBuildFromTmallService end..." );

    }

    @Test
    public void verify() throws TopSchemaException {
        List<JSONObject> maseterSchemaIds = cmsMtCategorySchemaDao.getAllSchemaIds();

        List<MtCommPropActionDefModel> commPropActionDefModels = cmsMtCommonPropDao.getActionModelList();

        for (JSONObject schemaId:maseterSchemaIds) {
            String id = schemaId.get("_id").toString();
            JSONObject schemaModel = cmsMtCategorySchemaDao.selectByIdRetrunJson(id);
            List<Field> fields = SchemaJsonReader.readJsonForList((List)schemaModel.get("fields"));

            for (MtCommPropActionDefModel defModel:commPropActionDefModels){
                ActionType actionType = ActionType.valueOf(Integer.valueOf(defModel.getActionType()));
                Field verifyField = null;
                switch (actionType){
                    case Add:
                         verifyField = FieldUtil.getFieldById(fields,defModel.getPropId());
                        Assert.assertNotNull(verifyField);
                        break;
                    case Update:
                        verifyField = FieldUtil.getFieldById(fields,defModel.getPropId());
                        if (StringUtils.isEmpty(defModel.getParentPropId())){
                            // 如果是最
                            Assert.assertNotNull(verifyField);
                        }
                        Field orgField = FieldUtil.getFieldById(fields,defModel.getPlatformPropRefId());
                        Assert.assertNull(orgField);
                        break;
                    case RemoveById:
                        verifyField = FieldUtil.getFieldById(fields,defModel.getPropId());
                        Assert.assertNull(verifyField);
                        break;
                    case RemoveByIdAndName:
                        List<Field> fieldList = FieldUtil.getFieldByName(fields,defModel.getPropName());
                        if (fieldList.size()>0){
                            for (Field field:fieldList){
                                if ("品牌".equals(defModel.getPropName())){
                                    Assert.assertEquals(field.getId(),"brand");
                                }
                            }
                        }else {
                            Assert.assertEquals(fieldList.size(),0);
                        }


                }

            }


        }

    }

}