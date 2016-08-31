package com.voyageone.service.impl.cms.sx;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.product.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/5/12.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SxProductServiceTest {

    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ProductService productService;

    @Test
    public void insertSxWorkLoadTest_WithProductModel() throws Exception {
        sxProductService.insertSxWorkLoad(productService.getProductById("010", 5944), "tester");
    }

    @Test
    public void insertSxWorkLoadTest() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        sxProductService.insertSxWorkLoad("018", codeList, null, "tester");
    }

    @Test
    public void insertSxWorkLoadTest_WithCart() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        sxProductService.insertSxWorkLoad("018", codeList, 23, "tester");
    }

    @Test
    public void getSxProductDataByGroupIdTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("010", 3L);
        System.out.println(sxData == null);
    }

    @Test
    public void testSortSkuInfo() {

        List<BaseMongoMap<String, Object>> skuList = new ArrayList<>();
        BaseMongoMap<String, Object> sku1 = new BaseMongoMap<>();
        sku1.put("skuCode", "A001");
        sku1.put("sizeSx", "43");  // 尺码排序只支持SkuSort枚举变量中定义的"XXL"等排序，不支持英文，汉字等排序
        skuList.add(sku1);
        BaseMongoMap<String, Object> sku2 = new BaseMongoMap<>();
        sku2.put("skuCode", "A002");
        sku2.put("sizeSx", "42");
        skuList.add(sku2);
        BaseMongoMap<String, Object> sku3 = new BaseMongoMap<>();
        sku3.put("skuCode", "A003");
        sku3.put("sizeSx", "41");
        skuList.add(sku3);
        sxProductService.sortSkuInfo(skuList);

//        for (BaseMongoMap<String, Object> sku:skuList) {
//            System.out.println(sku.get("skuCode")+":"+sku.get("sizeSx"));
//        }
        skuList.forEach(s -> System.out.println(s.get("skuCode")+":"+s.get("sizeSx")));
    }

}