package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsMtHsCodeUnitBean;
import com.voyageone.service.dao.cms.CmsBtTmScItemDao;
import com.voyageone.service.dao.cms.CmsBtTmTonggouFeedAttrDao;
import com.voyageone.service.dao.cms.CmsMtChannelConditionMappingConfigDao;
import com.voyageone.service.daoext.cms.CmsMtHsCodeUnitDaoExt;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsBtTmScItemModel;
import com.voyageone.service.model.cms.CmsBtTmTonggouFeedAttrModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by desmond on 2016/8/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadTmTongGouServiceTest {

    @Autowired
    CmsBuildPlatformProductUploadTmTongGouService uploadTmTongGouService;
    @Autowired
    private CmsBtTmTonggouFeedAttrDao cmsBtTmTonggouFeedAttrDao;
    @Autowired
    private CmsMtChannelConditionMappingConfigDao cmsMtChannelConditionMappingConfigDao;

    @Autowired
    private CmsMtHsCodeUnitDaoExt cmsMtHsCodeUnitDaoExt;

    @Autowired
    private CmsBtTmScItemDao cmsBtTmScItemDao;
    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean tcb = new TaskControlBean();
        tcb.setTask_id("CmsBuildPlatformProductUploadJdJob");
        tcb.setCfg_name("order_channel_id");
        tcb.setCfg_val1("929");
        tcb.setTask_comment("天猫官网同购上新允许运行的渠道");
        taskControlList.add(tcb);
        uploadTmTongGouService.onStartup(taskControlList);
    }

    @Test
    public void testUploadProduct() throws Exception {

        // 清除缓存（cms.channel_config表）
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString());
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_ShopConfigs.toString());
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_ShopConfigConfigs.toString());

//        String channelId = "024";
//        Integer cartId = 30;
        String channelId = "928";
        Integer cartId = 31;

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(762584);
        workload.setChannelId(channelId);
        workload.setCartId(cartId);
//        workload.setGroupId(Long.parseLong("110022"));  // code:SCL020400
//        workload.setGroupId(Long.parseLong("9920001"));  // code:SCL020400
        workload.setGroupId(Long.parseLong("3379530"));  // code:001001A07_GREY
        workload.setPublishStatus(0);
        workload.setModifier("SYSTEM");

        // for test only=============================================================
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            return;
        }
//        ShopBean shopProp = new ShopBean();
//        shopProp.setOrder_channel_id(channelId);
//        shopProp.setCart_id(String.valueOf(cartId));
//        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
//        shopProp.setAppKey("");
//        shopProp.setAppSecret("");
//        shopProp.setSessionKey("");
//        // platformid默认为天猫（1），expressionParser.parse里面会上传照片到天猫空间
//        shopProp.setPlatform_id("1");
        // for test only==============================================================

        // 从cms_bt_tm_tonggou_feed_attr表中取得该渠道，平台对应的天猫官网同购允许上传的feed attribute属性，如果为空则全部上传
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("channelId", channelId);
        paramMap.put("cartId", StringUtils.toString(cartId));
        List<CmsBtTmTonggouFeedAttrModel> tmTonggouFeedAttrModelList = cmsBtTmTonggouFeedAttrDao.selectList(paramMap);
        List<String> tmTonggouFeedAttrList = new ArrayList<>();
        if (ListUtils.notNull(tmTonggouFeedAttrModelList)) {
            // 如果表中有该渠道和平台对应的feed attribute属性，则将每个attribute加到列表中
            tmTonggouFeedAttrModelList.forEach(p -> tmTonggouFeedAttrList.add(p.getFeedAttr()));
        }

        // 从cms_mt_channel_condition_mapping_config表中取得当前渠道的取得产品主类目与天猫平台叶子类目(或者平台一级类目)，以及feed类目id和天猫平台类目之间的mapping关系数据
        Map<String, List<Map<String, String>>> categoryMappingMap = uploadTmTongGouService.getCategoryMapping(channelId, cartId);

        // 测试的时候要往uploadProduct()方面里面最前面加上下面这段初期化的代码，不然会报nullpoint错误 start
//        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
//        List<String> channelIdList = new ArrayList() {{
//            add("024");
//        }};
//        channelConditionConfig = new HashMap<>();
//        if (ListUtils.notNull(channelIdList)) {
//            for (final String orderChannelID : channelIdList) {
//                channelConditionConfig.put(orderChannelID, conditionPropValueRepo.getAllByChannelId(orderChannelID));
//            }
//        }
        // 测试的时候要往uploadProduct()方面里面最前面加上下面这段初期化的代码，不然会报nullpoint错误 start
        // 如果希望新增一个新的测试商品的话，在uploadProduct()的判断新增商品还是更新商品之前，追加sxData.getPlatform().setNumIId("");
        // 如果连接生产环境，不希望因为上传图片之后因为回写图片信息报错的话，暂时注释掉SxProductService.uploadImage()方法里面530,588行的2个回写动作就可以了

        uploadTmTongGouService.uploadProduct(workload, shopProp, tmTonggouFeedAttrList, categoryMappingMap);
        System.out.println("天猫官网同购 testUploadProduct 测试结束!");
    }


    @Test
    public void testGetSimpleItemPCatPath() throws Exception {

        String channelId = "010";
        Integer cartId = 30;
        String numIId = "537818716734";

        // for test only=============================================================
        ShopBean shopProp = new ShopBean();
        shopProp.setOrder_channel_id(channelId);
        shopProp.setCart_id(String.valueOf(cartId));
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("23239809");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");
        // platformid默认为天猫（1），expressionParser.parse里面会上传照片到天猫空间
        shopProp.setPlatform_id("1");
        // for test only==============================================================

        Map<String, String> resultMap = uploadTmTongGouService.getSimpleItemCatInfo(shopProp, numIId);
        System.out.println("");
        System.out.println("pCatId = " + resultMap.get("pCatId"));
        System.out.println("pCatPath = " + resultMap.get("pCatPath"));
    }

    @Test
    public void testGetTongGouCatFullPathByCatId() throws Exception {

        String channelId = "010";
        Integer cartId = 30;

        ShopBean shopProp = new ShopBean();
        shopProp.setOrder_channel_id(channelId);
        shopProp.setCart_id(String.valueOf(cartId));
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("23239809");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");
        // platformid默认为天猫（1），expressionParser.parse里面会上传照片到天猫空间
        shopProp.setPlatform_id("1");
        // for test only==============================================================

        String pCatFullPath = uploadTmTongGouService.getTongGouCatFullPathByCatId(shopProp, "50017233");
        System.out.println("");
        System.out.println("pCatPath = " + pCatFullPath);
    }

    @Test
    public void testGetCategoryMapping() {
//        Map<String, String> tempMap = new LinkedHashMap<>();
//        tempMap.put("t_key_category", "服饰内衣>服饰配件>围巾/手套/帽子套装");
//        tempMap.put("t_key_sizeType", "Women");
//
//        String result = JacksonUtil.bean2Json(tempMap);
//        System.out.println("result = " + result);

        String channelId = "928";
        int cartId = 31;

        Map<String, List<Map<String, String>>> result = uploadTmTongGouService.getCategoryMapping(channelId, cartId);
        System.out.println("ok");
    }

    @Test
    public void testGetMainCategoryMappingInfo() {
        String channelId = "928";
        int cartId = 31;

        String mainCatPath1 = "服饰>女装>休闲运动服饰>衬衫T恤>背心";   // 125024039

        Map<String, List<Map<String, String>>> categoryMappingListMap = uploadTmTongGouService.getCategoryMapping(channelId, cartId);
        // 1.主类目匹配天猫叶子类目
        String leafCategory = uploadTmTongGouService.getMainCategoryMappingInfo(mainCatPath1, null, null,
                CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category_leaf, categoryMappingListMap);

        System.out.println("leafCategory=" + leafCategory);

        // 2.主类目匹配天猫一级类目
        String mainCatPath2 = "手表";   // 手表/瑞士腕表
        String brand2 = "a_line";
        String sizeType2 = "";
        String mainCategory = uploadTmTongGouService.getMainCategoryMappingInfo(mainCatPath2, brand2, sizeType2,
                CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
        System.out.println("mainCategory=" + mainCategory);

        // 3.feed类目匹配天猫一级类目
        String mainCatPath3 = "Accessories-Womens-Bags-Non_exotic bags-Shoulder bag";  // 箱包皮具/热销女包/男包
        String feedCategory = uploadTmTongGouService.getMainCategoryMappingInfo(mainCatPath3, null, null,
                CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_category, categoryMappingListMap);
        System.out.println("feedCategory=" + feedCategory);

        System.out.println("ok");
    }

    @Test
    public void testUpdateDefaultValue() {
        String wirelessValue = "{\"item_picture\":{\"content\":[{\"img\":\"http://img.alicdn.com/imgextra/i3/3031513024/TB2eRi0chmI.eBjy0FlXXbgkVXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i3/3031513024/TB2XRPkgbVkpuFjSspcXXbSMVXa_!!3031513024.jpg\"},{\"img\":\"http://img.alicdn.com/imgextra/i3/3031513024/TB2DPmQa1NOdeFjSZFBXXctzXXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i1/3031513024/TB2hOhwrt0opuFjSZFxXXaDNVXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i1/3031513024/TB2Uk.5b3NlpuFjy0FfXXX3CpXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i3/3031513024/TB2Bd37b9BjpuFjSsplXXa5MVXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i3/3031513024/TB2cJx6cJXnpuFjSZFoXXXLcpXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i4/3031513024/TB2LWJocgxlpuFjy0FoXXa.lXXa_!!3031513024.jpg\"},{\"img\":\"http://img.alicdn.com/imgextra/i2/3031513024/TB2J9eUcmiJ.eBjSszfXXa4bVXa_!!3031513024.jpg\"},{\"img\":\"http://img.alicdn.com/imgextra/i1/3031513024/TB2jx_ucF5N.eBjSZFvXXbvMFXa_!!3031513024.jpg\"}],\"enable\":true,\"order\":9},\"shop_discount\":{\"enable\":false,\"order\":2}}";
        String defaultValue = "{\"item_picture\":{\"content\":[{\"img\":\"http://img.alicdn.com/imgextra/i3/3031513024/TB2eRi0chmI.eBjy0FlXXbgkVXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i3/3031513024/TB2XRPkgbVkpuFjSspcXXbSMVXa_!!3031513024.jpg\"},{\"img\":\"http://img.alicdn.com/imgextra/i3/3031513024/TB2DPmQa1NOdeFjSZFBXXctzXXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i1/3031513024/TB2hOhwrt0opuFjSZFxXXaDNVXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i1/3031513024/TB2Uk.5b3NlpuFjy0FfXXX3CpXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i3/3031513024/TB2Bd37b9BjpuFjSsplXXa5MVXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i3/3031513024/TB2cJx6cJXnpuFjSZFoXXXLcpXa_!!3031513024.jpg\"},{\"img\":\"https://img.alicdn.com/imgextra/i4/3031513024/TB2LWJocgxlpuFjy0FoXXa.lXXa_!!3031513024.jpg\"},{\"img\":\"http://img.alicdn.com/imgextra/i2/3031513024/TB2J9eUcmiJ.eBjSszfXXa4bVXa_!!3031513024.jpg\"},{\"img\":\"http://img.alicdn.com/imgextra/i1/3031513024/TB2jx_ucF5N.eBjSZFvXXbvMFXa_!!3031513024.jpg\"}],\"enable\":true,\"order\":1},\"shop_discount\":{\"enable\":true,\"order\":4,\"template_id\":\"333333\"}\n" +
                ",\"coupon\":{\"enable\":true,\"order\":2,\"template_id\":\"111111\"},\"hot_recommanded\":{\"enable\":true,\"order\":3,\"template_id\":\"222222\"}}";

        wirelessValue = uploadTmTongGouService.updateDefaultValue(wirelessValue, "shop_discount", defaultValue);
        wirelessValue = uploadTmTongGouService.updateDefaultValue(wirelessValue, "item_text", defaultValue);
        wirelessValue = uploadTmTongGouService.updateDefaultValue(wirelessValue, "coupon", defaultValue);
        wirelessValue = uploadTmTongGouService.updateDefaultValue(wirelessValue, "hot_recommanded", defaultValue);

        wirelessValue = uploadTmTongGouService.updateDefaultValue(wirelessValue, "item_picture", defaultValue);

        System.out.println(wirelessValue);

    }

    @Test
    public void testGetHscodeUnit() {
        String hscode = "9019101000";
//        CmsMtHsCodeUnitBean cmsMtHsCodeUnitBean = cmsMtHsCodeUnitDaoExt.getHscodeUnit(hscode);

//        System.out.println(cmsMtHsCodeUnitBean.getHscode());
        String unitName = "瓶";
        Map<String, String> unitMap = cmsMtHsCodeUnitDaoExt.getHscodeSaleUnit(unitName);
        String aaa = String.format("code##%s||cnName##%s", unitMap.get("unitCode"), unitName);
        System.out.println(aaa);
        if (unitMap == null) {
            System.out.println("a");
        }

    }

    @Test
    public void testsplitString() {

//        String a = "aa,vv,dd";
//        String hscodeSaleUnit = a.substring(a.lastIndexOf(",") + 1, a.length());

        String a = String.format("code##%s||cnName##%s", "111", "套");

        System.out.println(a);
    }

    @Test
    public void testSelectScCode() {

        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("channelId", "017");
        searchParam.put("cartId", "30");
        searchParam.put("code", "I_LIKING_IT");
        searchParam.put("sku", "017-100130");
        searchParam.put("orgChannelId", "017");
        String scCode = MD5.getMd5_16("017-100130");

        searchParam.put("scCode", scCode);
        CmsBtTmScItemModel scItemModel = cmsBtTmScItemDao.selectOne(searchParam);

        System.out.println(scItemModel.getScCode());
    }

}
