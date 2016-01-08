package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Field_Image;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductUpdateResponse;
import com.voyageone.web2.sdk.api.response.ProductsAddResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2015/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductsAddRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testAdd() {

        ProductsAddRequest requestModel = new ProductsAddRequest("001");
        requestModel.addProduct(createProduct(11, "a12" + 11));
        requestModel.addProduct(createProduct(12, "a12" + 12));

        //modifier
        requestModel.setCreater("liang");

        //SDK取得Product 数据
        ProductsAddResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }

    private CmsBtProductModel createProduct(long prodId, String productCode) {
        CmsBtProductModel model = new CmsBtProductModel();
        model.setProdId(prodId);
        model.setCatPath("女装>休闲服1>上衣>");
        model.setCatId(StringUtils.generCatId(model.getCatPath()));
        //field
        model.getFields().setCode(productCode);
        model.getFields().setColor(model.getFields().getColor() + "1");
        List<CmsBtProductModel_Field_Image> images1 = new ArrayList<>();
        images1.add(new CmsBtProductModel_Field_Image("aa1.jsp"));
        images1.add(new CmsBtProductModel_Field_Image("aa2.jsp"));
        images1.add(new CmsBtProductModel_Field_Image("aa3.jsp"));
        model.getFields().setImages1(images1);

        //feed
        model.getFeed().getOrgAtts().put("aa1", "b1");
        model.getFeed().getOrgAtts().put("aa2", "b2");
        model.getFeed().getCnAtts().put("aa1", "cnb1");
        model.getFeed().getCnAtts().put("aa2", "cnb2");
        model.getFeed().getCustomIds().add("aa1");
        model.getFeed().getCustomIds().add("aa2");
        return model;
    }
}
