package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/4/7.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class UploadToUSJoiServiceTest {

    @Autowired
    UploadToUSJoiService uploadToUSJoiService;

    @Autowired
    ProductService productService;

    @Autowired
    CmsMtPlatformCategorySchemaDao  cmsMtPlatformCategorySchemaDao;
    @Test
    public void testUpload() throws Exception {

        CmsBtSxWorkloadModel sxWorkLoadBean = new CmsBtSxWorkloadModel();
        sxWorkLoadBean.setChannelId("017");
        sxWorkLoadBean.setGroupId(138906L);
        sxWorkLoadBean.setModifier("james");

        uploadToUSJoiService.upload(sxWorkLoadBean);
    }

    @Test
    public void testProduct(){
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("010","1E35-2529");
        CmsBtProductModel_Platform platform = new CmsBtProductModel_Platform();
        CmsBtProductModel_Platform_Cart cmsBtProductModel_platform_cart = new CmsBtProductModel_Platform_Cart();
        cmsBtProductModel_platform_cart.setNumIid("1111");
        cmsBtProductModel_platform_cart.setpCatId("2222");

        cmsBtProductModel_platform_cart.setFields(new BaseMongoMap<>());
        cmsBtProductModel_platform_cart.getFields().put("8652","aaa");
        platform.put("p26", cmsBtProductModel_platform_cart);
        cmsBtProductModel.setPlatform(platform);
        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(cmsBtProductModel);
        productUpdateBean.setIsCheckModifed(false);

        CmsMtPlatformCategorySchemaModel platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.getPlatformCatSchemaModel("1349", 26);
        List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());

        CmsBtProductModel_Platform_Cart platformCart = cmsBtProductModel.getPlatform().get("p26");
        if(platformCart != null){
            Map<String, Object> fieldsValue =  platformCart.getFields();
            FieldUtil.setFieldsValueFromMap(fields, fieldsValue);
            FieldUtil.getFieldsValueToMap(fields);
        }
        productService.updateProduct("010", productUpdateBean);
    }
    @Test
    public void testOnStartup() throws Exception {

        uploadToUSJoiService.onStartup(new ArrayList<TaskControlBean>());
    }

    @Test
    public void testOnStartup1() throws Exception {

    }
}