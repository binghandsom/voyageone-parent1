package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Enums.ActionType;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.service.dao.cms.CmsMtCommonPropDao;
import com.voyageone.service.dao.cms.mongo.CmsMtCategorySchemaDao;
import com.voyageone.service.model.cms.CmsMtCommonPropActionDefModel;
import net.minidev.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lewis on 15-12-10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class MasterCatSchemaBuildFromTmallServiceTest {

    private static Log logger = LogFactory.getLog(MasterCatSchemaBuildFromTmallServiceTest.class);

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
        logger.info("");
        List<JSONObject> masterSchemaIds = cmsMtCategorySchemaDao.getAllSchemaIds();
        logger.info("总件数： "+masterSchemaIds.size());
        List<CmsMtCommonPropActionDefModel> commPropActionDefModels = cmsMtCommonPropDao.selectActionModelList();
        int schemaCount = 0;
        for (JSONObject schemaId:masterSchemaIds) {
            schemaCount++;
            String id = schemaId.get("_id").toString();

            JSONObject schemaModel = cmsMtCategorySchemaDao.selectByIdRetrunJson(id);

            Object skuObj = schemaModel.get("sku");
            List skuList = new ArrayList<>();
            skuList.add(skuObj);
            List<Field> skuFields = SchemaJsonReader.readJsonForList(skuList);
            Object fieldsObj = schemaModel.get("fields");
            List<Field> fields = SchemaJsonReader.readJsonForList((List)fieldsObj);
            fields.addAll(skuFields);

            logger.info("Schema 数:"+schemaCount+"件， Category Id: " + schemaModel.get("catId"));

            for (CmsMtCommonPropActionDefModel defModel:commPropActionDefModels){
                ActionType actionType = ActionType.valueOf(Integer.valueOf(defModel.getActionType()));
                Field verifyField = null;
                    switch (actionType){
                        case ADD:
                            logger.info("ADD: " + defModel.getPropId());
                            verifyField = FieldUtil.getFieldById(fields,defModel.getPropId());
                            Assert.assertNotNull(verifyField);
                            break;
                        case UPDATE:
                            logger.info("UPDATE: " + defModel.getPropId());
                            verifyField = FieldUtil.getFieldById(fields,defModel.getPropId());
                            Assert.assertNotNull(verifyField);
                            Field orgField = FieldUtil.getFieldById(fields,defModel.getPlatformPropRefId());
                            Assert.assertNull(orgField);
                            break;
                        case REMOVE:
                            logger.info("REMOVE: " + defModel.getPropId());
                            verifyField = FieldUtil.getFieldById(fields,defModel.getPropId());
                            Assert.assertNull(verifyField);
                            break;

                }


            }


        }

    }

    @Test
    public void testBuildMasterCatSchema() throws Exception {

        masterCatSchemaBuildFromTmallService.buildMasterCatSchema();
    }
}