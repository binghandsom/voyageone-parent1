package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsBtTmTonggouFeedAttrDao;
import com.voyageone.service.dao.cms.CmsMtChannelConditionMappingConfigDao;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsBtTmTonggouFeedAttrModel;
import com.voyageone.service.model.cms.CmsMtChannelConditionMappingConfigModel;
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

        String channelId = "010";
        Integer cartId = 30;

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(762584);
        workload.setChannelId(channelId);
        workload.setCartId(cartId);
        workload.setGroupId(Long.parseLong("110022"));  // code:SCL020400
        workload.setPublishStatus(0);
        workload.setModifier("SYSTEM");

        // for test only=============================================================
//        ShopBean shopProp = Shops.getShop("010", "30");
//        if (shopProp == null) {
//            return;
//        }
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

        // 从cms_mt_channel_condition_mapping_config表中取得该渠道，平台对应的客户过来的类目id和天猫平台一级类目之间的mapping关系数据
        Map<String, String> conditionMappingParamMap = new HashMap<>();
        conditionMappingParamMap.put("channelId", channelId);
        conditionMappingParamMap.put("cartId", StringUtils.toString(cartId));
        conditionMappingParamMap.put("propName", "tt_category");   // 天猫同购一级类目匹配
        List<CmsMtChannelConditionMappingConfigModel> conditionMappingConfigModels =
                cmsMtChannelConditionMappingConfigDao.selectList(conditionMappingParamMap);
        if (ListUtils.isNull(conditionMappingConfigModels)) {
            return;
        }
        Map<String, String> conditionMappingMap = new HashMap<>();
        conditionMappingConfigModels.forEach(p -> conditionMappingMap.put(p.getMapKey(), p.getMapValue()));

        uploadTmTongGouService.uploadProduct(workload, shopProp, tmTonggouFeedAttrList, conditionMappingMap);
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

}
