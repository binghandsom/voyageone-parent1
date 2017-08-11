package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.service.bean.cms.product.ProductForOmsBean;
import com.voyageone.service.bean.cms.product.ProductForWmsBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsProductServiceTest {
    @Autowired
    private ProductService cmsProductService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

//    @Test
//    public void testInsertCmsBtProduct() throws Exception {
//        CmsBtProductModel productModel = create("001", 1, new Random());
//        WriteResult result = cmsProductService.insert(productModel);
//        System.out.println(result);
//    }

    private CmsBtProductModel create(String channelId, int index, Random random) {
        CmsBtProductModel product = new CmsBtProductModel(channelId);
//        product.setProdId(Long.parseLong("" + index));
//        //String catId = String.valueOf(random.nextInt(1000));
//        product.setCatPath("女装>休闲服>上衣>");
//        product.setCatId(StringUtils.generCatId(product.getCatPath()));
//        String code = String.valueOf(100000 + index);
//        CmsBtProductModel_Field fields = product.getFields();
//        fields.setCode(code);
//        fields.setBrand("Jewelry" + random.nextInt(10));
//        fields.setAttribute("productName", "Stud Earrings with Cubic Zirconia in Sterling Silver " + code);
//
//        fields.setLongTitle("Stud Earrings with Cubic Zirconia in Sterling Silver- 长标题" + random.nextInt(100));
//        fields.setMiddleTitle("Stud Earrings with Cubic Zirconia in Sterling Silver- 中标题" + random.nextInt(100));
//        fields.setShortTitle("Stud Earrings with Cubic Zirconia in Sterling Silver- 短标题" + random.nextInt(100));
//
//        fields.setModel("model-aa-" + random.nextInt(100));
//        fields.setColor("Color" + random.nextInt(100));
//        fields.setOrigin("china" + random.nextInt(10));
//
//        fields.setShortDesCn("Stud Earrings with Cubic Zirconia in Sterling Silver- 简短描述中文" + random.nextInt(100));
//        fields.setLongDesCn("Stud Earrings with Cubic Zirconia in Sterling Silver- 详情描述中文" + random.nextInt(100));
//        fields.setShortDesEn("Stud Earrings with Cubic Zirconia in Sterling Silver- 简短描述英语" + random.nextInt(100));
//        fields.setLongDesEn("Stud Earrings with Cubic Zirconia in Sterling Silver- 详情描述英语" + random.nextInt(100));
//
//        fields.setHsCodeCrop("Stud Ear" + random.nextInt(10));
//        fields.setHsCodePrivate("Stud Ear" + random.nextInt(10));
//
//        fields.setPriceChange(random.nextInt(1));
//
//        List<CmsBtProductModel_Field_Image> images = fields.getImages1();
//        images.add(new CmsBtProductModel_Field_Image("image1", "xxxxx-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "xxxxx-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "xxxxx-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "xxxxx-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "xxxxx-" + random.nextInt(10) + ".jpg"));
//
//        images = fields.getImages2();
//        images.add(new CmsBtProductModel_Field_Image("image1", "yyyyy-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "yyyyy-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "yyyyy-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "yyyyy-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "yyyyy-" + random.nextInt(10) + ".jpg"));
//
//        images = fields.getImages3();
//        images.add(new CmsBtProductModel_Field_Image("image1", "uuuuu-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "uuuuu-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "uuuuu-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "uuuuu-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "uuuuu-" + random.nextInt(10) + ".jpg"));
//
//        images = fields.getImages3();
//        images.add(new CmsBtProductModel_Field_Image("image1", "zzzzz-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "zzzzz-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "zzzzz-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "zzzzz-" + random.nextInt(10) + ".jpg"));
//        images.add(new CmsBtProductModel_Field_Image("image1", "zzzzz-" + random.nextInt(10) + ".jpg"));
//
//      //  fields.setLock(index % 2 == 0);
//
//
//        fields.setPriceMsrpSt(100.00 + random.nextInt(100));
//        fields.setPriceMsrpEd(200.00 + random.nextInt(100));
//        fields.setPriceRetailSt(300.00 + random.nextInt(100));
//        fields.setPriceRetailEd(400.00 + random.nextInt(100));
//        fields.setPriceSaleSt(500.00 + random.nextInt(100));
//        fields.setPriceSaleEd(600.00 + random.nextInt(100));
//        fields.setCurPriceSt(700.00 + random.nextInt(100));
//        fields.setCurPriceEd(800.00 + random.nextInt(100));
//        fields.setStatus(CmsConstants.ProductStatus.New);
//        fields.setTranslateStatus("0");
//        fields.setEditStatus("0");
//        fields.setSizeType("Men" + random.nextInt(5));
////        fields.setInventory(100 + random.nextInt(10));
//        fields.setPriceChange(random.nextInt(1));
//
//
////        CmsBtProductModel_Group groups = product.getGroups();
////        groups.setMsrpStart(100.00 + random.nextInt(100));
////        groups.setMsrpEnd(200.00 + random.nextInt(100));
////        groups.setRetailPriceStart(300.00 + random.nextInt(100));
////        groups.setRetailPriceEnd(400.00 + random.nextInt(100));
////        groups.setSalePriceStart(500.00 + random.nextInt(100));
////        groups.setSalePriceEnd(600.00 + random.nextInt(100));
////        groups.setCurrentPriceStart(700.00 + random.nextInt(100));
////        groups.setCurrentPriceEnd(800.00 + random.nextInt(100));
////
////        List<CmsBtProductModel_Group_Platform> platforms = groups.getPlatforms();
////        CmsBtProductModel_Group_Platform platform = new CmsBtProductModel_Group_Platform();
////        platform.setGroupId(Long.parseLong("" + random.nextInt(1000)));
////        platform.setCartId(21);
////        platform.setNumIId(String.valueOf(2000000 + random.nextInt(1000)));
////        platform.setIsMain(false);
////        platform.setDisplayOrder(random.nextInt(100));
////        platform.setPublishTime("2015-11-12 16:19:00");
////        platform.setInStockTime("2015-11-18 16:19:00");
//////        platform.setStatus("InStock");
//////        platform.setPublishStatus("等待上新");
//////        platform.setComment("");
//////        platform.setInventory(random.nextInt(100));
////        platforms.add(platform);
////
////        platform = new CmsBtProductModel_Group_Platform(platform);
////        platform.setGroupId(Long.parseLong("" + random.nextInt(1000)));
////        platform.setCartId(23);
////        platforms.add(platform);
//
//        List<CmsBtProductModel_Sku> skus = product.getSkus();
//        for (int i=1; i<3+random.nextInt(5); i++) {
//            CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();
//            sku.setSkuCode(code + "-" + i);
//            sku.setBarcode("1234567890" + (100 + random.nextInt(100)));
//            sku.setPriceMsrp(100.00 + random.nextInt(100));
//            sku.setPriceRetail(300.00 + random.nextInt(100));
//            sku.setPriceSale(800.00 + random.nextInt(100));
//            List<Integer> skuCarts = new ArrayList<>();
//            skuCarts.add(21);
//            skuCarts.add(23);
//            sku.setSkuCarts(skuCarts);
//            skus.add(sku);
//        }
//
//        BaseMongoMap feedOrgAtts = product.getFeed().getOrgAtts();
//        feedOrgAtts.setAttribute("washingtype", "dry cleaning");
//        feedOrgAtts.setAttribute("collar", "mandarin collar");
//        feedOrgAtts.setAttribute("style", "campus");
//        feedOrgAtts.setAttribute("waspe", "dleaning");
//
//        BaseMongoMap feedCnAtts = product.getFeed().getCnAtts();
//        feedCnAtts.setAttribute("washingtype", "dry cleaning");
//        feedCnAtts.setAttribute("collar", "mandarin collar");
//        feedCnAtts.setAttribute("style", "campus");
//        feedCnAtts.setAttribute("waspe", "dleaning");
//
        return product;
    }

    @Test
    public void testSelectCmsBtProductById() throws Exception {
        CmsBtProductModel ret = cmsProductService.getProductById("010", 22139);

        ret = cmsProductService.getProductById("010", 22139);
        System.out.println(ret.getFeed().getCnAtts());
//        System.out.println(ret.getSkus().get(0).isIncludeCart(CartEnums.Cart.getValueByID("21")));
//        System.out.println(ret.getSkus().get(0).isIncludeCart(CartEnums.Cart.getValueByID("20")));
//        System.out.println(ret.getGroups().getCurrentPriceEnd());
    }

//    @Test
//    public void testSelectCmsBtProductByCode() throws Exception {
//        CmsBtProductModel ret = cmsProductService.getProductByCode("200", "100001");
//        System.out.println(ret.toString());
//        System.out.println(ret.getGroups().getCurrentPriceEnd());
//    }

//    @Test
//    public void testSelectCmsBtProductByGroupId() throws Exception {
//        List<CmsBtProductModel> listRet = cmsProductService.getProductByGroupId("001", 470);
//        for (CmsBtProductModel ret : listRet) {
//            System.out.println(ret.toString());
//            System.out.println(ret.getGroups().getPlatformByGroupId(470L));
//        }
//    }

//    @Test
//    public void testSelectSKUById() throws Exception {
//        List<CmsBtProductModel_Sku> listRet = cmsProductService.selectSKUById("001", 1);
//        for (CmsBtProductModel_Sku ret : listRet) {
//            System.out.println(ret.toString());
//        }
//    }

//    @Test
//    public void testInsert10W() {
//        long start = System.currentTimeMillis();
//        List<CmsBtProductModel> lst = new ArrayList<>();
//        int index = 0;
//        for(int i=1; i<=100000; i++) {
//            CmsBtProductModel productModel = create("010", i, new Random());
//            lst.add(productModel);
//            index++;
//            if (i%1000 == 0) {
//                System.out.println("current count:=" + index);
//                cmsProductService.insert(lst);
//                lst.clear();
//            }
//        }
//        if (lst.size()>0) {
//            cmsProductService.insert(lst);
//        }
//        long total = System.currentTimeMillis()-start;
//        System.out.println("total count:=" + index + "; totalTime:="+total);
//    }

//    @Test
//    public void remove10W(){
//        long start = System.currentTimeMillis();
//        cmsProductService.removeAll("101");
//        long total = System.currentTimeMillis()-start;
//        System.out.println("total totalTime:=" + total);
//    }

//    @Test
//    public void testSelectCmsBtProductByIdOnlyProdId() throws Exception {
//        JSONObject ret = cmsProductService.getProductByIdWithJson("001", 1);
//        System.out.println(ret.toString());
//    }

    @Test
    public void bulkUpdate(){
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        System.out.println(new Date());
        for (int i = 1; i < 100000 ; i++) {
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("tags","tag-1-" + String.valueOf(i));
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("prodId",i);
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }
        System.out.println(new Date());
        cmsBtProductDao.bulkUpdateWithMap("100",bulkList,"init","$addToSet");
        System.out.println(new Date());
    }

    @Test
    public void testBathUpdateWithSXResult() {
        List<String> codeList = new ArrayList<>();
        codeList.add("100001");
        codeList.add("100002");
//        cmsProductService.bathUpdateWithSXResult("001", 21, codeList, "123123123", "product_id1", "2015-11-12 16:19:00", "2015-11-12 16:19:00", CmsConstants.PlatformStatus.Onsale);
    }

//    @Test
//    public void testGetProductCodesByCart() {
//        long start = System.currentTimeMillis();
//        List<String> productCodeList = cmsProductService.getProductCodesByCart("300", 21);
//        long end = System.currentTimeMillis();
//
//        int index = 1;
//        for (String productCode : productCodeList) {
//            System.out.println(productCode);
//            index++;
//            if (index > 10) {
//                break;
//            }
//        }
//        System.out.println(productCodeList.size());
//        System.out.println("total time:=" + (end - start));
//    }
//
//    @Test
//    public void testGetProductGroupIdCodesMapByCart() {
//        long start = System.currentTimeMillis();
//        Map<String, List<String>> productCodeMap = cmsProductService.getProductGroupIdCodesMapByCart("300", 21);
//        long end = System.currentTimeMillis();
//
//        int index = 1;
//
//        for (Map.Entry<String, List<String>> entry : productCodeMap.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//            index++;
//            if (index > 10) {
//                break;
//            }
//        }
//        System.out.println(productCodeMap.size());
//        System.out.println("total time:=" + (end-start));
//    }

    @Test
    public void getWmsProductsInfo() {

        String[] projection = "prodId;channelId;fields;skus;groups".split(";");

        ProductForWmsBean result = cmsProductService.getWmsProductsInfo("928", "016-33835-NTMT-0524603-065", projection);
        System.out.println(result);
    }

    @Test
    public void getOmsProductsInfo () {
        List<String> skuList = new ArrayList<>();

        List<ProductForOmsBean> result = cmsProductService.getOmsProductsInfo("928", "016-33835-NTMT-0524603-065", skuList, null, null, "1", null);
        System.out.println(result);
    }

}