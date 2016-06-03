package com.voyageone.task2.cms.mongoDao;

import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lewis on 15-12-2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsMtPlatformCategorySchemaDaoTest {

    @Autowired
    private CmsMtPlatformCategorySchemaDao schemaDao;

    @Test
    public void testDeletePlatformCategorySchemaByCartId() throws Exception {
        schemaDao.deletePlatformCategorySchemaByCartId(20);
        schemaDao.deletePlatformCategorySchemaByCartId(23);

    }

    @Test
    public void testGetAllSchemaKeys() throws Exception {
        List<JSONObject> schemaKeyModels = schemaDao.selectAllSchemaKeys(23);
        int count =0;
        for (JSONObject keyMap:schemaKeyModels){
          String id =  keyMap.get("_id").toString();
            CmsMtPlatformCategorySchemaModel schemaModel = schemaDao.selectPlatformCatSchemaModelById(id, 23);
            String itemSchema = schemaModel.getPropsItem();
            String productSchema = schemaModel.getPropsProduct();
            List<Field> itemFields =null;
            List<Field> productFields =null;
            if (!StringUtils.isEmpty(itemSchema))
                itemFields = SchemaReader.readXmlForList(itemSchema);
            if (!StringUtils.isEmpty(productSchema))
                productFields = SchemaReader.readXmlForList(productSchema);
            if(itemFields!=null && productFields!=null){
                itemFields.addAll(productFields);
            }
            System.out.println(count++);
        }
    }

    @Test
    public void testGetPlatformCatSchemaModel() throws Exception {

        List<String> str1 = new ArrayList<>();
        str1.add("aaaa");
        str1.add("bbbb");
        str1.add("1111");
        List<String> str2 = new ArrayList<>();
        str2.add("aaaa");
        str2.add("bbbb");
        str2.add("1111");

        str2.addAll(str1);
        System.out.println();
        for (String s:str2){
            System.out.println(s);
        }



    }
}