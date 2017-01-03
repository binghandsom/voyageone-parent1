package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
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
 * 天猫同购共通标题描述翻译测试
 *
 * Created by desmond on 2016/12/08.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsTranslateByTonggouMqServiceTest {

    @Autowired
    private CmsTranslateByTonggouMqService cmsTranslateByTonggouMqService;

    @Test
    public void testOnStartup1() throws Exception {
        // 指定code的翻译

        String channelId = "010";
        String code = "SCL020400";
        String code2 = "15344:SZ7";
        boolean blnNeedTransTitle = true;
        boolean blnRunType = true;    // false:不强制翻译标题以外项目  true:强制翻译标题以外项目(即使对应的中文项目已经有值了也要重新翻译)

        List<String> codeList = new ArrayList<>();
        codeList.add(code);
        codeList.add(code2);

        BaseMongoMap<String, String> transSrcDesMap = new BaseMongoMap() {{
            put("longDesEn", "longDesCn");   // 已经有中文信息了
            put("shortDesEn", "shortDesCn");
            put("usageEn", "usageCn");
        }};

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("channelId", channelId);
        messageMap.put("codeList", codeList);
        messageMap.put("blnNeedTransTitle", blnNeedTransTitle);
        messageMap.put("transSrcDesMap", transSrcDesMap);
        messageMap.put("blnRunType", blnRunType);
        cmsTranslateByTonggouMqService.onStartup(messageMap);
    }

    @Test
    public void testOnStartup2() throws Exception {
        // 不指定code的翻译，翻译整个channel的所有code

        String channelId = "010";
//        String code = "SCL020400";
        boolean blnNeedTransTitle = true;
        boolean blnRunType = false;    // false:不强制翻译标题以外项目  true:强制翻译标题以外项目(即使对应的中文项目已经有值了也要重新翻译)

        List<String> codeList = new ArrayList<>();
//        codeList.add(code);

        BaseMongoMap<String, String> transSrcDesMap = new BaseMongoMap() {{
            put("longDesEn", "longDesCn");   // 已经有中文信息了
            put("shortDesEn", "shortDesCn");
            put("usageEn", "usageCn");
        }};

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("channelId", channelId);
        messageMap.put("codeList", codeList);
        messageMap.put("blnNeedTransTitle", blnNeedTransTitle);
        messageMap.put("transSrcDesMap", transSrcDesMap);
        messageMap.put("blnRunType", blnRunType);
        cmsTranslateByTonggouMqService.onStartup(messageMap);
    }

    @Test
    public void testDoMain() throws Exception {
        // 多个产品code的同购翻译,并批量回写数据库

        String channelId = "010";
        String code = "SCL020400";
        boolean blnNeedTransTitle = true;
        boolean blnRunType = true;    // false:不强制翻译标题以外项目  true:强制翻译标题以外项目(即使对应的中文项目已经有值了也要重新翻译)

        BaseMongoMap<String, String> transSrcDesMap = new BaseMongoMap() {{
            put("longDesEn", "longDesCn");   // 已经有中文信息了
            put("shortDesEn", "shortDesCn");
            put("usageEn", "usageCn");
        }};

        List<String> codeList = new ArrayList<>();
        codeList.add(code);

        // 调用批量翻译
        cmsTranslateByTonggouMqService.doMain(channelId, codeList, blnNeedTransTitle, transSrcDesMap, blnRunType);

        System.out.println("testDoMain 测试正常结束!");
    }

    @Test
    public void testDoTranslateByTonggouSingle() throws Exception {
        // 单个产品code的同购翻译,不回写数据库

        String channelId = "010";
//        String cartId = "28";
        String code = "SCL020400";
        boolean blnNeedTransTitle = true;
        boolean blnRunType = false;    // false:不强制翻译标题以外项目  true:强制翻译标题以外项目(即使对应的中文项目已经有值了也要重新翻译)
        String numIIdForTransOnly = "542998554105";   // 翻译专用测试商品id

        ShopBean transShop = new ShopBean();
        transShop.setApp_url("http://gw.api.taobao.com/router/rest");
        transShop.setOrder_channel_id("000");
        transShop.setAppKey("23239809");
        transShop.setAppSecret("34fb2f57498bc6b00384da175021e587");
        transShop.setSessionKey("6100330f76a107e76570295d6a3f2d7295f98415d0d2b1e2640015666");
        transShop.setShop_name("天猫同购共通标题描述翻译专用店");
        transShop.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());

        BaseMongoMap<String, String> transSrcDesMap = new BaseMongoMap() {{
            put("longDesEn", "longDesCn");   // 已经有中文信息了
            put("shortDesEn", "shortDesCn");
            put("usageEn", "usageCn");
        }};

        // 取得synship.com_mt_third_party_config表中配置的同购APPKEY，翻译专用商品id等信息
        String configChannelId = "000";
        String tm_tt_trans = "tm_tt_sx";
        ThirdPartyConfigBean config = ThirdPartyConfigs.getThirdPartyConfig(configChannelId, tm_tt_trans);
        if (config == null) {
            String warnMsg = String.format("取得synship.com_mt_third_party_config表中配置的同购APPKEY，翻译专用商品id等信息失败! " +
                    "[channel_id:%s] [prop_name:%s]", configChannelId, tm_tt_trans);
            System.out.println(warnMsg);
            return;
        }
        // 官网同购更新商品时，其他必填项目和值的json串,必填的项目有主图地址，物流模板id，skus的json串等
        String otherItemValueJson = config.getProp_val6();
        Map<String, Object> otherItemMap = null;
        if (!StringUtils.isEmpty(otherItemValueJson)) {
            otherItemMap = JacksonUtil.jsonToMap(otherItemValueJson);
        }

        // 设置商品更新共通属性
        BaseMongoMap<String, String> productInfoMap = cmsTranslateByTonggouMqService.getProductCommonInfo();

        List<JongoUpdate> updateSql = cmsTranslateByTonggouMqService.doTranslateByTonggouSingle(transShop, channelId, code,
                blnNeedTransTitle, transSrcDesMap, blnRunType, numIIdForTransOnly, otherItemMap, productInfoMap);

//        System.out.println("testDoTranslateByTonggouSingle 测试正常结束！updateSql = " + updateSql);
    }
}