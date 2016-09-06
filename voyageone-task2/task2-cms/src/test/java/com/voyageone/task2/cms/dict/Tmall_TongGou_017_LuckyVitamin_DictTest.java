package com.voyageone.task2.cms.dict;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.*;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 天猫同购平台商品5张主图JSON生成工具
 *  (017)LuckyVitamin天猫同购店
 *
 * @author desmond on 2016/8/23.
 * @version 2.5.0
 * @since 2.5.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_TongGou_017_LuckyVitamin_DictTest {
    @Autowired
    private SxProductService sxProductService;

    String C_TEXT_BR = "<br />";
    String C_TEMPLATE_IMG = "<img src=%s>";

    String C_商品图片 = "PRODUCT_IMAGE";
    String C_包装图片 = "PACKAGE_IMAGE";
    String C_带角度图片 = "ANGLE_IMAGE";
    String C_自定义图片 = "CUSTOM_IMAGE";

    // -------------------------------------------------
    // 切换平台时，下列商品5张主图的固定图片URL需要修改
    // -------------------------------------------------

    @Test
    public void startupTest() {

        // 天猫同购商品主图5张
//        doCreateJson("天猫同购商品主图5张", false, doDict_天猫同购商品主图5张());

        // 天猫同购描述(详情页描述)
//        doCreateJson("天猫同购描述", false, doDict_天猫同购描述());

        doCreateJson("天猫同购无线描述", false, doDict_天猫同购无线描述());

        // 属性图片模板

        //
//        // 聚美使用方法
//        doCreateJson("聚美使用方法", false, doDict_聚美使用方法());
//
//        // 聚美实拍
//        doCreateJson("聚美实拍", false, doDict_聚美实拍());

    }

    @Test
    public void dictTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("010", 20893L);
        sxData.setCartId(31);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = Shops.getShop("010", 27);
//        shopProp.setCart_id("27");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.JM.getId());

        // 最终测试结果是这样的 ======================================== START
// =====================================
// 字典: 聚美详情
// <div><img src="http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/3b17329e-1af1-4e1f-adc1-e4451cfa1477.jpeg"></div>
// =====================================
// 字典: 聚美使用方法
// <div><img src="http://p12.jmstatic.com/open_api/gPop_131/010/2/20160617165939.jpeg" /></div>
// =====================================
// 字典: 聚美实拍
// <div><img src="http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/5eca6152-21fb-4352-a647-9d122b63c7a1.jpeg" /></div><div><img src="http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/f439cf4b-9791-45fe-9b3d-6d3960261612.jpeg" /></div>
// =====================================
// 字典: 聚美白底方图
// http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/56dbb1c0-ef09-48f6-9530-80d734653155.jpeg,http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/2ed210b5-10da-49b9-a7cf-49b1e89b9fe9.jpeg,
        // 最终测试结果是这样的 ======================================== END

        try {
            String result = "";

//
//            // 聚美使用方法
//            System.out.println("=====================================");
//            System.out.println("字典: 聚美使用方法");
//            result = sxProductService.resolveDict("聚美使用方法", expressionParser, shopProp, getTaskName(), null);
//            System.out.println(result);
//
//            // 聚美实拍
//            System.out.println("=====================================");
//            System.out.println("字典: 聚美实拍");
//            result = sxProductService.resolveDict("聚美实拍", expressionParser, shopProp, getTaskName(), null);
//            System.out.println(result);

            // 天猫同购商品主图5张
            System.out.println("=====================================");
            System.out.println("字典: 天猫同购商品主图5张");
            result = sxProductService.resolveDict("天猫同购商品主图5张", expressionParser, shopProp, getTaskName(), null);
            System.out.println(result);
            for (String picUrl : result.split(",")) {
//                String jmPicUrl = sxProductService.uploadImageByUrl_JM(picUrl, shopProp);
//                System.out.println(jmPicUrl);
                System.out.println(picUrl);
            }

            // 天猫同购描述
            System.out.println("=====================================");
            System.out.println("字典: 天猫同购描述");
            result = sxProductService.resolveDict("天猫同购描述", expressionParser, shopProp, getTaskName(), null);
            System.out.println(result);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getTaskName() {
        return getClass().getName();
    }


    /**
     * 生成json
     *
     * @param title    字典名字
     * @param isUrl    生成出来的内容是否是url(一般是图片的话就是true, 其他有文字的都是false)
     * @param ruleRoot rule
     */
    private void doCreateJson(String title, boolean isUrl, RuleExpression ruleRoot) {

        DictWord dictRoot = new DictWord();
        dictRoot.setName(title);
        dictRoot.setExpression(ruleRoot);
        dictRoot.setIsUrl(isUrl);

        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
        String json = ruleJsonMapper.serializeRuleWord(dictRoot);

        System.out.println("=====================================");
        System.out.println("字典: " + title);
        System.out.println(json);

    }



    /**
     * 天猫同购商品主图5张
     * 1. (要上传)(非html)(逗号分隔的商品主图)
     *   模板:http://s7d5.scene7.com/is/image/sneakerhead/BHFO%5F2015%5Fx1000%5F1000x?$jc1000_1000$&$product=%s
     */
    private RuleExpression doDict_天猫同购商品主图5张() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        for (int i = 0; i < 5; i++) {

            // 第一个参数是product_id(GetMainProductImages)
            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();

            RuleExpression imageTemplate = new RuleExpression();
            // 带logo商品图片（不是产品图片）的源图片模板
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/BHFO%%5F2015%%5Fx1000%%5F1000x?$jc1000_1000$&$product=%s"));
            userParam.setImageTemplate(imageTemplate);
            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord(String.valueOf(i)));
            userParam.setImageIndex(imageIndex);
            RuleExpression img_imageType = new RuleExpression();
            img_imageType.addRuleWord(new TextWord(C_商品图片));
            userParam.setImageType(img_imageType);
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("%s,"));
            userParam.setHtmlTemplate(htmlTemplate);

            CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
            wordValueGetMainProductImages.setUserParam(userParam);

            ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));

        }

        return ruleRoot;

    }

//    /**
//     * 天猫同购描述（参考聚美详情）
//     */
//    private RuleExpression doDict_天猫同购描述() {
//
//        // 根字典
//        RuleExpression ruleRoot = new RuleExpression();
//
//        // 生成内容
//        {
//            // 天猫同购描述
//            RuleExpression htmlTemplate = new RuleExpression();
//            htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));
//
//            RuleExpression imageType = new RuleExpression();
//            imageType.addRuleWord(new TextWord("3"));
//
//            RuleExpression viewType = new RuleExpression();
//            viewType.addRuleWord(new TextWord("1"));
//
//            RuleExpression useOriUrl = null;
//
//            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
//            ruleRoot.addRuleWord(new CustomWord(word));
//        }
//
//        return ruleRoot;
//
//    }

    private RuleExpression doDict_天猫同购无线描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // start
            String kv = "{";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

//        {
//            // K-V 模板
//            String kv = "\"k1\":{\"k1-1\":\"v1\",\"k1-2\":\"v2\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//
//            <field id="xxx_enable" name="是否启用" type="singleCheck">
//                <options>
//                <option displayName="启用" value="true"/>
//                <option displayName="不启用" value="false"/>
//                </options>
//            </field>
//        }

        {
            // coupon 优惠
//            String kv = "\"coupon\":{\"enable\":\"true\",\"coupon_id\":\"342115\"},";
            String kv = "\"coupon\":{\"enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // hot_recommanded 同店推荐
//            <field id="hot_recommanded_id" name="选择模板" type="singleCheck">
//                <options>
//                <option displayName="商品推荐" value="520277"/>
//                </options>
//            </field>
//            String kv = "\"hot_recommanded\":{\"enable\":\"true\",\"hot_recommanded_id\":\"341911\"},";
            String kv = "\"hot_recommanded\":{\"enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // shop_discount 店铺活动
//            String kv = "\"shop_discount\":{\"enable\":\"true\",\"shop_discount_id\":\"342160\"},";
            String kv = "\"shop_discount\":{\"enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

//        {
//            // user_define 自定义
//            String kv = "\"user_define\":{\"user_define_enable\":\"true\",\"user_define_name\":\"xxx\",\"user_define_image_0\":\"xxx\",\"user_define_image_1\":\"xxx\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }

        // 图片全是DictWord(总共20张图片,但最后5张不用设置图片)
        {
            // item_picture 商品图片
            {
                String kv = "\"item_picture\":{\"order\": \"6\",\"enable\":\"true\",";
                TextWord word = new TextWord(kv);
                ruleRoot.addRuleWord(word);
            }

            {
                TextWord word = new TextWord("\"content\":[");
                ruleRoot.addRuleWord(word);
            }

            {
                // 5张无线商品图片
                for (int i = 0; i < 5; i++) {
                    int j = i + 1;
                    String imageStr = ", {\"img\":\"";
                    if (i == 0) {
                        imageStr = "{\"img\":\"";
                    }

                    TextWord imageWord = new TextWord(imageStr);
                    ruleRoot.addRuleWord(imageWord);

                    DictWord dictRoot = new DictWord();
                    dictRoot.setName("无线商品图片-" + j);
                    ruleRoot.addRuleWord(dictRoot);

                    imageStr = "\"}";
                    imageWord = new TextWord(imageStr);
                    ruleRoot.addRuleWord(imageWord);
                }

                // 5张无线自定义图片
                for (int i = 5; i < 10; i++) {

                    int j = i - 4;
                    String imageStr = ", {\"img\":\"";
                    TextWord imageWord = new TextWord(imageStr);
                    ruleRoot.addRuleWord(imageWord);

                    DictWord dictRoot = new DictWord();
                    dictRoot.setName("无线自定义图片-" + j);
                    ruleRoot.addRuleWord(dictRoot);

                    imageStr = "\"}";
                    imageWord = new TextWord(imageStr);
                    ruleRoot.addRuleWord(imageWord);
                }
            }

            {
                TextWord word = new TextWord("]");
                ruleRoot.addRuleWord(word);
            }



//            // 5张无线固定图
//            for (int i = 10; i < 15; i++) {
//                // 5张无线商品图片
//                int j = i - 9;
//                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
//                TextWord imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//
//                DictWord dictRoot = new DictWord();
//                dictRoot.setName("无线固定图-" + j);
//                ruleRoot.addRuleWord(dictRoot);
//
//                imageStr = "\"}";
//                imageWord = new TextWord(imageStr);
//                ruleRoot.addRuleWord(imageWord);
//            }

            // item_picture_image_15 ~ item_picture_image_19不用设置

            // end
            String endStr = "}";
            TextWord endWord = new TextWord(endStr);
            ruleRoot.addRuleWord(endWord);
        }

        {
            // end
            String kv = "}";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        return ruleRoot;
    }

}
