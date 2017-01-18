package com.voyageone.components.cnn.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 新独立域名商品调用服务测试
 *
 * Created by desmond on 2017/01/04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:test-context.xml")
public class CnnWareServiceTest {
//
//    @Autowired
//    private CnnWareService cnnWareService;
//
//    private static String _URL = "http://127.0.0.1:8080/api/v1";
//
//    @Test
//    public void testAddProduct() throws Exception {
//        // 新独立域名平台新增商品测试
//
//        String channelId = "928";
//        String cartId = CartEnums.Cart.LIKING.getId();   // 32
//        String code = "022-EA3060501754";
//
//        ShopBean shop = new ShopBean();
//        shop.setApp_url(_URL);
//        shop.setAppKey("");
//        shop.setAppSecret("");
//        shop.setSessionKey("");     // 新独立域名测试店(SessionKey)
//        shop.setOrder_channel_id(channelId);
//        shop.setCart_id(cartId);
//        shop.setShop_name("新独立域名测试店");
//        shop.setPlatform_id(PlatFormEnums.PlatForm.CNN.getId());
//
//        Map<String, Object> commonFields = new HashMap<>();
//        commonFields.put("categoryId", "2001,2030");
//        commonFields.put("channelId", "017");
//        commonFields.put("model", "108818:Sizes");
//        commonFields.put("brand", "nordic naturals");
//        commonFields.put("title", "Nordic Naturals - 草莓味儿童DHA液体 - 16盎司 51281");
//        commonFields.put("shortDesc", "Nordic Naturals - 儿童DHA液体草莓 - 16盎司。本产品由100%北极鳕鱼肝制成，支持大脑和视觉功能的发展。深受孩子喜欢。Nordic Naturals - 儿童DHA液体草莓含有丰富的omega-3 DHA。DHA对于大脑、眼睛、神经和免疫系统的发展是必需的。儿童DHA也含有100%的天然维生素A和D。支持3岁以上儿童大脑和视力功能的发展。草莓口味液体和可咀嚼的软胶囊。");
//        commonFields.put("longDesc", "Nordic Naturals - 儿童DHA液体草莓 - 16盎司。本产品由100%北极鳕鱼肝制成，支持大脑和视觉功能的发展。深受孩子喜欢。Nordic Naturals - 儿童DHA液体草莓含有丰富的omega-3 DHA。DHA对于大脑、眼睛、神经和免疫系统的发展是必需的。儿童DHA也含有100%的天然维生素A和D。支持3岁以上儿童大脑和视力功能的发展。草莓口味液体和可咀嚼的软胶囊。");
//        commonFields.put("material", "");
//        commonFields.put("feature", "未定义");
//        commonFields.put("origin", "NO");
//        commonFields.put("productType", "DHA");
//        commonFields.put("sizeType", "oz.");
//        commonFields.put("usage", "");
//        commonFields.put("mainImage", "017-107377-1");
//        String[] images = { "017-107377-1", "017-107377-2", "017-107377-3", "017-107377-4", "017-51281-1", "017-51281-2", "017-51281-3", "017-54869-1", "017-54869-2", "017-54869-3", "017-54869-4" };
//        commonFields.put("images", images);
//        commonFields.put("pageDetailPC", "pageDetailPC");
//        commonFields.put("pageDetailM", "pageDetailM");
//        commonFields.put("searchKey", "儿童DHA液体草莓");
//
//        List<Map<String, Object>> skuList = new ArrayList<>();
//
//        Map<String, Object> sku1 = new HashMap<>();
//        sku1.put("name", "Nordic Naturals 草莓味儿童DHA液 16盎司");
//        sku1.put("skuCode", "017-107377");
//        sku1.put("prodCode", "017-107377");
//        sku1.put("inventory", 33);
//        sku1.put("retailPrice", 315.0D);
//        sku1.put("salePrice", 314.0D);
//        Map<String, String> sku1Opt = new HashMap<>();
//        sku1Opt.put("size", "16");
//        sku1Opt.put("color", "white");
//        sku1.put("skuOptions", sku1Opt);
//        skuList.add(sku1);
//
//        Map<String, Object> sku2 = new HashMap<>();
//        sku2.put("name", "Nordic Naturals 草莓味儿童DHA液体 8盎司");
//        sku2.put("skuCode", "017-51281");
//        sku2.put("prodCode", "017-107377");
//        sku2.put("inventory", 133);
//        sku2.put("retailPrice", 198.0D);
//        sku2.put("salePrice", 190.0D);
//        Map<String, String> sku2Opt = new HashMap<>();
//        sku2Opt.put("size", "8");
//        sku2Opt.put("color", "white");
//        sku2.put("skuOptions", sku2Opt);
//        skuList.add(sku2);
//
//        Map<String, Object> sku3 = new HashMap<>();
//        sku3.put("name", "Nordic Naturals 草莓味儿童DHA液 4盎司");
//        sku3.put("skuCode", "17-54869");
//        sku3.put("prodCode", "017-107377");
//        sku3.put("inventory", 12);
//        sku3.put("retailPrice", 144.0D);
//        sku3.put("salePrice", 143.0D);
//        Map<String, String> sku3Opt = new HashMap<>();
//        sku3Opt.put("size", "4");
//        sku3Opt.put("color", "white");
//        sku3.put("skuOptions", sku3Opt);
//        skuList.add(sku3);
//
//        Map<String, Object> sku11 = new HashMap<>();
//        sku11.put("name", "Nordic Naturals 草莓味儿童DHA液 16盎司");
//        sku11.put("skuCode", "017-1073772");
//        sku11.put("prodCode", "017-107377");
//        sku11.put("inventory", 33);
//        sku11.put("retailPrice", 315.0D);
//        sku11.put("salePrice", 314.0D);
//        Map<String, String> sku11Opt = new HashMap<>();
//        sku11Opt.put("size", "16");
//        sku11Opt.put("color", "blue");
//        sku11.put("skuOptions", sku11Opt);
//        skuList.add(sku11);
//
//        Map<String, Object> sku12 = new HashMap<>();
//        sku12.put("name", "Nordic Naturals 草莓味儿童DHA液体 8盎司");
//        sku12.put("skuCode", "017-512812");
//        sku12.put("prodCode", "017-107377");
//        sku12.put("inventory", 133);
//        sku12.put("retailPrice", 198.0D);
//        sku12.put("salePrice", 190.0D);
//        Map<String, String> sku12Opt = new HashMap<>();
//        sku12Opt.put("size", "8");
//        sku12Opt.put("color", "blue");
//        sku12.put("skuOptions", sku12Opt);
//        skuList.add(sku12);
//
//        Map<String, Object> sku13 = new HashMap<>();
//        sku13.put("name", "Nordic Naturals 草莓味儿童DHA液 4盎司");
//        sku13.put("skuCode", "17-548692");
//        sku13.put("prodCode", "017-107377");
//        sku13.put("inventory", 12);
//        sku13.put("retailPrice", 144.0D);
//        sku13.put("salePrice", 143.0D);
//        Map<String, String> sku13Opt = new HashMap<>();
//        sku13Opt.put("size", "4");
//        sku13Opt.put("color", "blue");
//        sku13.put("skuOptions", sku13Opt);
//        skuList.add(sku13);
//
//        List<Map<String, Object>> optionsList  = new ArrayList<>();
//        Map<String, Object> optionItem = new HashMap<>();
//        optionItem.put("key", "size");
//        optionItem.put("name", "容量");
//        optionItem.put("order", 2);
//        String[] valueList = { "4", "8", "16"};
//        optionItem.put("valueList", valueList);
//        optionsList.add(optionItem);
//
//        Map<String, Object> optionItem2 = new HashMap<>();
//        optionItem2.put("key", "color");
//        optionItem2.put("name", "瓶身颜色");
//        optionItem2.put("order", 1);
//        String[] valueList2 = { "white", "blue"};
//        optionItem2.put("valueList", valueList2);
//        optionsList.add(optionItem2);
//
//        String result = cnnWareService.addProduct(shop, commonFields, null, skuList, optionsList);
//        System.out.println("新独立域名平台新增商品测试 result = " + (result == null ? "返回值为空" : result));
//    }
//
//    @Test
//    public void testUpdateProduct() throws Exception {
//        // 新独立域名平台更新商品测试
//
//        String channelId = "928";
//        String cartId = CartEnums.Cart.LIKING.getId();   // 32
//        String code = "022-EA3060501754";
//
//        ShopBean shop = new ShopBean();
//        shop.setApp_url(_URL);
//        shop.setAppKey("");
//        shop.setAppSecret("");
//        shop.setSessionKey("");     // 新独立域名测试店(SessionKey)
//        shop.setOrder_channel_id(channelId);
//        shop.setCart_id(cartId);
//        shop.setShop_name("新独立域名测试店");
//        shop.setPlatform_id(PlatFormEnums.PlatForm.CNN.getId());
//
//        long numIId = 1932;
//
//        List<Map<String, Object>> skuList = new ArrayList<>();
//
//        Map<String, Object> sku1 = new HashMap<>();
//        sku1.put("skuCode", "ao2993-l-1");
//        sku1.put("retailPrice", 4880.0D);
//        sku1.put("salePrice", 4880.0D);
//        skuList.add(sku1);
//
//        Map<String, Object> sku2 = new HashMap<>();
//        sku2.put("skuCode", "ao2993-m-1");
//        sku2.put("retailPrice", 5880.0D);
//        sku2.put("salePrice", 5880.0D);
//        skuList.add(sku2);
//
//        Map<String, Object> sku3 = new HashMap<>();
//        sku3.put("skuCode", "ao2993-s-1");
//        sku3.put("retailPrice", 6880.0D);
//        sku3.put("salePrice", 6880.0D);
//        sku3.put("tax", 6880.0D);
//        skuList.add(sku3);
//
////        Map<String, Object> product = new HashMap<>();
////        product.put("skuList",skuList);
////        product.put("numIId",numIId);
//
////        List<Map<String, Object>> productList = new ArrayList<>();
////        productList.add(product);
////        params.put("productList", productList);
//
//        String result = cnnWareService.updateProduct(shop, numIId, null, null, skuList, null);
//        System.out.println("新独立域名平台更新商品测试 result = " + (result == null ? "返回值为空" : result));
//    }

}
