package com.voyageone.web2.cms.views.promotion;

import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtPromotionSkuBean;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/16.
 * @version 2.0.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPromotionDetailServiceTest {

    @Test
    public void addPromotionDetail() throws Exception {

    }

    @Test
    public void addPromotionDetail1() throws Exception {

    }

    @Test
    public void insertPromotionGroup() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void addTeJiaBaoInit() throws Exception {

    }

    @Test
    public void teJiaBaoPromotionInsert() throws Exception {

    }

    @Test
    public void teJiaBaoPromotionUpdate() throws Exception {

    }

    @Test
    public void delPromotionCode() throws Exception {

    }

    @Autowired
    CmsPromotionDetailService cmsPromotionDetailService;
    @Autowired
    PromotionDetailService promotionDetailService;
    @Test
    public void testInsertPromotionProduct() throws Exception {

//        List< CmsPromotionProductPriceBean > productPrices = new ArrayList<>();
//        CmsPromotionProductPriceBean cmsPromotionProductPriceBean = new CmsPromotionProductPriceBean();
//        cmsPromotionProductPriceBean.setCode("100004");
//        cmsPromotionProductPriceBean.setPrice(20.99);
//        cmsPromotionProductPriceBean.setTag("7折");
//        productPrices.add(cmsPromotionProductPriceBean);
//
//        cmsPromotionProductPriceBean = new CmsPromotionProductPriceBean();
//        cmsPromotionProductPriceBean.setCode("100003");
//        cmsPromotionProductPriceBean.setPrice(10.99);
//        cmsPromotionProductPriceBean.setTag("7折");
//        productPrices.add(cmsPromotionProductPriceBean);
//
//        productPrices.add(cmsPromotionProductPriceBean);
//        cmsPromotionDetailService.insertPromotionProduct(productPrices, 15, "james");
//        System.out.println("");
    }

    @Test
    public void testGetPromotionGroup() throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("promotionId", 1);
        param.put("start", 1);
        param.put("length", 10);
        cmsPromotionDetailService.getPromotionGroup(param, 0);
    }

    @Test
    public void testResolvePromotionXls() throws Exception {
        InputStream stream = new FileInputStream("d:\\promotion.xlsx");
//        cmsPromotionDetailService.resolvePromotionXls(stream);
    }

    @Test
    public void testUploadPromotion() throws Exception {
        InputStream stream = new FileInputStream("d:\\promotion.xlsx");
        cmsPromotionDetailService.uploadPromotion(stream, 15, "james");
        System.out.println("");
    }

    @Test
    public void testTeJiaBaoInit() throws Exception {
        promotionDetailService.teJiaBaoInit(15, "010","james");
    }


    @Test
    public void testTeJiaBaoPromotionInsert() throws Exception {
        CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
        cmsBtPromotionCodesBean.setProductId(3143569L);
        cmsBtPromotionCodesBean.setProductCode("17298882-White-White");
        cmsBtPromotionCodesBean.setPromotionPrice(100.0);
        cmsBtPromotionCodesBean.setPromotionId(0);
        cmsBtPromotionCodesBean.setNumIid("538261461689");
        cmsBtPromotionCodesBean.setChannelId("024");
        cmsBtPromotionCodesBean.setCartId(23);
        cmsBtPromotionCodesBean.setModifier("james");
        List<CmsBtPromotionSkuBean> skus = new ArrayList<>();
        CmsBtPromotionSkuBean sku = new CmsBtPromotionSkuBean();
        sku.setProductSku("024-17298882-000-000");
        sku.setPromotionPrice(new BigDecimal(3.0));
        skus.add(sku);
        sku = new CmsBtPromotionSkuBean();
        sku.setProductSku("024-17298882-000-001");
        sku.setPromotionPrice(new BigDecimal(4.0));
        skus.add(sku);
        cmsBtPromotionCodesBean.setSkus(skus);
        promotionDetailService.teJiaBaoPromotionInsert(cmsBtPromotionCodesBean);
    }

    @Test
    public void testTeJiaBaoPromotionUpdate() throws Exception {
        CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
        cmsBtPromotionCodesBean.setProductId(3143569L);
        cmsBtPromotionCodesBean.setProductCode("17298882-White-White");
        cmsBtPromotionCodesBean.setPromotionPrice(100.0);
        cmsBtPromotionCodesBean.setPromotionId(0);
        cmsBtPromotionCodesBean.setNumIid("538261461689");
        cmsBtPromotionCodesBean.setChannelId("024");
        cmsBtPromotionCodesBean.setCartId(23);
        cmsBtPromotionCodesBean.setModifier("james");
        List<CmsBtPromotionSkuBean> skus = new ArrayList<>();
        CmsBtPromotionSkuBean sku = new CmsBtPromotionSkuBean();
        sku.setProductSku("024-17298882-000-000");
        sku.setPromotionPrice(new BigDecimal(3.0));
        skus.add(sku);
        sku = new CmsBtPromotionSkuBean();
        sku.setProductSku("024-17298882-000-001");
        sku.setPromotionPrice(new BigDecimal(4.0));
        skus.add(sku);
        cmsBtPromotionCodesBean.setSkus(skus);
        promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodesBean);
    }

    @Test
    public void testGetTMallJuHuaSuanExport() throws Exception {
        cmsPromotionDetailService.getTMallJuHuaSuanExport(7,"010");
    }

//    public void testTejiaBaoInsert(){
//        PromotionCodeAddTejiaBaoRequest request=new PromotionCodeAddTejiaBaoRequest();
//        request.setModifier("lijun");
//        request.setChannelId("013");
//        request.setCartId(23);
//        request.setProductCode("ESJ9293LBS-SC ");
//        request.setPromotionId(45);
//        request.setPromotionPrice(100.0);
//
//        voApiClient.execute(request);
//    }
}