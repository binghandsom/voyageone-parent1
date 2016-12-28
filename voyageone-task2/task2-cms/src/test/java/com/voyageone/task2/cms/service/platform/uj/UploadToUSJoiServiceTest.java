package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.category.match.*;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Autowired
    private Searcher searcher;

    @Test
    public void testUpload() throws Exception {

        // 清除缓存（这样在synship.com_mt_value_channel表中刚追加的brand，productType，sizeType等初始化mapping信息就能立刻取得了）
//        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString());
//        // 清除缓存（这样在synship.tm_order_channel表中刚追加的cartIds信息就能立刻取得了）
//        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_OrderChannelConfigs.toString());

        String usjoiChannelId = "928";   // Liking

//        // --------------------------------------------------------------------------------------------
//        // 品牌mapping表
//        Map<String, String> mapBrandMapping = new HashMap<>();
//        // 产品分类mapping表
//        Map<String, String> mapProductTypeMapping = new HashMap<>();
//        // 适用人群mapping表
//        Map<String, String> mapSizeTypeMapping = new HashMap<>();
//
//        // 品牌mapping作成
//        List<TypeChannelBean> brandTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.BRAND_41, usjoiChannelId);
//        if (ListUtils.notNull(brandTypeChannelBeanList)) {
//            for (TypeChannelBean typeChannelBean : brandTypeChannelBeanList) {
//                if (!StringUtils.isEmpty(typeChannelBean.getAdd_name1())
//                        && !StringUtils.isEmpty(typeChannelBean.getName())
//                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
//                        ) {
//                    // 品牌mapping表中key,value都设为小写(feed进来的brand不区分大小写)
//                    mapBrandMapping.put(typeChannelBean.getAdd_name1().toLowerCase().trim(), typeChannelBean.getName().toLowerCase().trim());
//                }
//            }
//        }
//
//        // 产品分类mapping作成
//        List<TypeChannelBean> productTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_57, usjoiChannelId);
//        if (ListUtils.notNull(productTypeChannelBeanList)) {
//            for (TypeChannelBean typeChannelBean : productTypeChannelBeanList) {
//                if (!StringUtils.isEmpty(typeChannelBean.getValue())
//                        && !StringUtils.isEmpty(typeChannelBean.getName())
//                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
//                        ) {
//                    // 产品分类mapping表(value是key,name和add_name1是值)
//                    mapProductTypeMapping.put(typeChannelBean.getValue().trim(), typeChannelBean.getName().trim());
//                }
//            }
//        }
//
//        // 适用人群mapping作成
//        List<TypeChannelBean> sizeTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_58, usjoiChannelId);
//        if (ListUtils.notNull(sizeTypeChannelBeanList)) {
//            for (TypeChannelBean typeChannelBean : sizeTypeChannelBeanList) {
//                if (!StringUtils.isEmpty(typeChannelBean.getValue())
//                        && !StringUtils.isEmpty(typeChannelBean.getName())
//                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
//                        ) {
//                    // 适用人群mapping作成(value是key,name和add_name1是值)
//                    mapSizeTypeMapping.put(typeChannelBean.getValue().trim(), typeChannelBean.getName().trim());
//                }
//            }
//        }

        // 获取当前usjoi channel, 有多少个platform
        List<TypeChannelBean> usjoiTypeChannelBeanList = TypeChannels.getTypeListSkuCarts(usjoiChannelId, "D", "en"); // 取得展示用数据
        if (ListUtils.isNull(usjoiTypeChannelBeanList)) {
            String errMsg = "com_mt_value_channel表中没有usJoiChannel(" + usjoiChannelId + ")对应的展示用(53 D en)mapping" +
                    "信息,不能插入usJoiGroup信息，终止UploadToUSJoiServie处理，后面的子店产品都不往USJOI本店导入了，请修改好共通数据后再导入";
            // channel级的共通配置异常，本USJOI channel后面的产品都不导入了
            return;
        }

        // 自动同步对象平台列表(ALL:所有平台，也可具体指定需要同步的平台id,用逗号分隔(如:"28,29"))
        String ccAutoSyncCarts = "";
        List<String> ccAutoSyncCartList = null;
        // 自动同步对象平台列表(ALL:所有平台，也可具体指定需要同步的平台id,用逗号分隔(如:"28,29"))
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(usjoiChannelId,
                CmsConstants.ChannelConfig.AUTO_SYNC_CARTS);
        if (cmsChannelConfigBean != null && !StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())) {
            String strAutoSyncCarts = cmsChannelConfigBean.getConfigValue1().trim();
            // 如果配置的值为ALL,则同步所有平台
            if ("ALL".equalsIgnoreCase(strAutoSyncCarts)) {
                ccAutoSyncCarts = "ALL";
            } else {
                // 取得自动同步指定平台列表
                ccAutoSyncCartList = Arrays.asList(strAutoSyncCarts.split(","));
            }
        }
        // --------------------------------------------------------------------------------------------

        // 从synship.tm_order_channel表中取得USJOI店铺channel对应的cartId列表（一般只有一条cartId.如928对应28, 929对应29）
        // 用于product.PXX追加平台信息(group表里面用到的用于展示的cartId不是从这里取得的)
        final List<Integer> cartIds = new ArrayList<>();
        // 从synship.com_mt_value_channel表中取得USJOI店铺channel对应的可售卖的cartId列表（如928对应28,29,27等）
        List<TypeChannelBean> approveCartList = TypeChannels.getTypeListSkuCarts(usjoiChannelId, "A", "en"); // 取得可售卖平台数据
        if (ListUtils.notNull(approveCartList)) {
            // 取得配置表中可售卖的非空cartId列表
            approveCartList.forEach(p -> {
                if(!StringUtils.isEmpty(p.getValue()))
                    cartIds.add(NumberUtils.toInt(p.getValue()));
            });
        }
        if (ListUtils.isNull(approveCartList) || ListUtils.isNull(cartIds)) {
            return;
        }

        CmsBtSxWorkloadModel sxWorkLoadBean = new CmsBtSxWorkloadModel();
        sxWorkLoadBean.setChannelId("017");
        sxWorkLoadBean.setGroupId(662793L);
        sxWorkLoadBean.setModifier("james");
        sxWorkLoadBean.setCartId(Integer.parseInt(usjoiChannelId)); // "928"

        int totalCnt = 1;
        int currentIndex = 1;
        long startTime = System.currentTimeMillis();

        // 从cms_mt_channel_config配置表中取得渠道级别配置项的值集合,2016/10/24以后在这个表里面新加的配置项都可以在这个里面取得
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
        uploadToUSJoiService.doChannelConfigInit(usjoiChannelId, channelConfigValueMap);

        uploadToUSJoiService.upload(sxWorkLoadBean, usjoiTypeChannelBeanList,
                cartIds, ccAutoSyncCarts, ccAutoSyncCartList, currentIndex, totalCnt, startTime, channelConfigValueMap);
    }

    @Test
    public void testProduct() {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("010", "16189");
        CmsBtProductModel_Platform_Cart cmsBtProductModel_platform_cart = new CmsBtProductModel_Platform_Cart();
        cmsBtProductModel_platform_cart.setpNumIId("1111");
        cmsBtProductModel_platform_cart.setpCatId("2222");

        cmsBtProductModel_platform_cart.setFields(new BaseMongoMap<>());
        cmsBtProductModel_platform_cart.getFields().put("8652", "aaa");

        cmsBtProductModel.setPlatform(26, cmsBtProductModel_platform_cart);
        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(cmsBtProductModel);
        productUpdateBean.setIsCheckModifed(false);

//        CmsMtPlatformCategorySchemaModel platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel("1349", 26);
//        List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());
//
//        CmsBtProductModel_Platform_Cart platformCart = cmsBtProductModel.getPlatform(26);
//        if (platformCart != null) {
//            BaseMongoMap<String, Object> fieldsValue = platformCart.getFields();
//            FieldUtil.setFieldsValueFromMap(fields, fieldsValue);
//            FieldUtil.getFieldsValueToMap(fields);
//        }
//        productService.updateProduct("010", productUpdateBean);
    }

    @Test
    public void testOnStartup() throws Exception {
        uploadToUSJoiService.onStartup(new ArrayList<>());
    }

    @Test
    public void testUploadByChannel() throws Exception {
        // 保存每个channel最终导入结果(成功失败件数信息)
        Map<String, String> resultMap = new ConcurrentHashMap<>();

        for (OrderChannelBean channelBean : Channels.getUsJoiChannelList()) {
            // 只测试928渠道时
            if ("928".equals(channelBean.getOrder_channel_id())) {
                uploadToUSJoiService.uploadByChannel(channelBean, resultMap);
            }
        }
    }

    @Test
    public void testDoSetSkuCnt() throws Exception {
        String usjoiChannelId = "928";

        List<String> productCodes = new ArrayList<>();
        productCodes.add("NY-23923-PG-PINK");
        productCodes.add("NY-26153");

        uploadToUSJoiService.doSetSkuCnt(usjoiChannelId, productCodes);
    }

    @Test
    public void testMtCategoryApi() throws Exception {
        // 测试主类目匹配接口有没有问题
        String feedCategoryPath = "Accessories-Womens-Slgs-Passport holder";

        StopWordCleaner cleaner = new StopWordCleaner();
        Tokenizer tokenizer = new Tokenizer(new ArrayList(){{add("-");}});
        FeedQuery query = new FeedQuery(feedCategoryPath, cleaner, tokenizer);
        query.setSizeType("feedSizeType1");
        query.setProductName("productNameEn");
        query.setProductType("feedProductType");

        List<SearchResult> result = searcher.search(query, 1);
        System.out.println("ok");
    }

    @Test
    public void testDoSetMainCategory() throws Exception {
        // 测试设置匹配主类目方法

        String likingChannelid = "928";
        String code = "P1086157-GREEN";
        String feedCategoryPath = "Accessories-Womens-Slgs-Passport holder";

        // 取得mongoDB中对象产品
        CmsBtProductModel prodObj = productService.getProductByCode(likingChannelid, code);
        if (prodObj == null) {
            System.out.println(String.format("在product表中没有查到该产品code(%s)!", code));
        }

        uploadToUSJoiService.doSetMainCategory(prodObj.getCommon(), feedCategoryPath);
        System.out.println("ok");
    }
}