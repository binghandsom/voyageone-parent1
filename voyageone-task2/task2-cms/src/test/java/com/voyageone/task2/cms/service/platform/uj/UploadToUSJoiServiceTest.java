package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
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
    @Test
    public void testUpload() throws Exception {

        CmsBtSxWorkloadModel sxWorkLoadBean = new CmsBtSxWorkloadModel();
        sxWorkLoadBean.setChannelId("017");
        sxWorkLoadBean.setGroupId(138906);
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
        platform.put("p26", cmsBtProductModel_platform_cart);
        cmsBtProductModel.setPlatform(platform);
        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(cmsBtProductModel);
        productUpdateBean.setIsCheckModifed(false);
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