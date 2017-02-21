package com.voyageone.web2.cms.views.product;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.product.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/6/6.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsProductPlatformDetailServiceTest {

    @Autowired
    CmsProductPlatformDetailService cmsProductPlatformDetailService;
    @Autowired
    ProductService productService;

    @Test
    public void testGetProductPlatform() throws Exception {
//        cmsProductPlatformDetailService.getProductPlatform("010", 5924L, 26);

        Map<String, Object> result = new HashMap<>();

        result.put("mastData", cmsProductPlatformDetailService.getProductMastData("010",5924L,27));
        result.put("platform", cmsProductPlatformDetailService.getProductPlatform("010", 5924L, 27, "cn"));

        System.out.println(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testGetProductMastData() throws Exception {

    }

//    @Test
//    public void testProduct() {
//        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("010", "51A0HC13E1-00LCNB0");
//        CmsBtProductModel_Platform_Cart cmsBtProductModel_platform_cart = new CmsBtProductModel_Platform_Cart();
//        cmsBtProductModel_platform_cart.setpNumIId("1111");
//        cmsBtProductModel_platform_cart.setpCatId("1349");
//        cmsBtProductModel_platform_cart.setpCatPath("服饰内衣>男装>T恤");
//
//        cmsBtProductModel_platform_cart.setFields(new BaseMongoMap<>());
//        cmsBtProductModel_platform_cart.getFields().put("8652", "aaa");
//
//        cmsBtProductModel.setPlatform(26, cmsBtProductModel_platform_cart);
//        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
//        productUpdateBean.setProductModel(cmsBtProductModel);
//        productUpdateBean.setIsCheckModifed(false);
//
//        CmsMtPlatformCategorySchemaModel platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel("11935", 26);
//        List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());
//
//        CmsBtProductModel_Platform_Cart platformCart = cmsBtProductModel.getPlatform(26);
//        if (platformCart != null) {
//            BaseMongoMap<String, Object> fieldsValue = platformCart.getFields();
//            FieldUtil.setFieldsValueFromMap(fields, fieldsValue);
//            FieldUtil.getFieldsValueToMap(fields);
//        }
//        productService.updateProduct("010", productUpdateBean);
//    }

    @Test
    public void testUpdateProductPlatform() throws Exception {
        Map<String, Object> platform = cmsProductPlatformDetailService.getProductPlatform("010", 5924L, 26, "cn");
        platform.put("schemaFields",JacksonUtil.jsonToMapList(JacksonUtil.bean2Json(platform.get("schemaFields"))));
        List<Map<String,Object>> a =(List<Map<String,Object>>)platform.get("schemaFields");
        a.get(0).put("8652","bbb");
        cmsProductPlatformDetailService.updateProductPlatform("010", 5924L, platform,"",false);
    }

    @Test
    public void testCopyPropertyFromMainProduct() throws Exception {
//        public Map<String, Object> copyPropertyFromMainProduct(String channelId, Long prodId, Integer cartId, String language)
        Map<String, Object> result = cmsProductPlatformDetailService.copyPropertyFromMainProduct("007", 3630165L, 23, "en");
        System.out.print(result);
    }
}