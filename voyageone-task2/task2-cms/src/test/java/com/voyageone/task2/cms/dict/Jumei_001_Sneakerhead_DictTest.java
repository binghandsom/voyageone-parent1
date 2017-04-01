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
 * 聚美平台详情页描述JSON生成工具
 *  (001)sneakerhead聚美优品店
 *
 * @author tom on 2016/6/28.
 * @version 2.1.0
 * @since 2.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Jumei_001_Sneakerhead_DictTest {
    @Autowired
    private SxProductService sxProductService;

    String C_TEXT_BR = "<br />";
    String C_TEMPLATE_IMG = "<img src=%s>";

    String C_商品图片 = "PRODUCT_IMAGE";
    String C_包装图片 = "PACKAGE_IMAGE";
    String C_带角度图片 = "ANGLE_IMAGE";
    String C_自定义图片 = "CUSTOM_IMAGE";

    // -------------------------------------------------
    // 切换平台时，下列详情页固定图片URL需要修改
    // -------------------------------------------------

    @Test
    public void startupTest() {

        // 聚美详情
        doCreateJson("聚美详情", false, doDict_聚美详情());

        // 聚美使用方法
        doCreateJson("聚美使用方法", false, doDict_聚美使用方法());

        // 聚美实拍
        doCreateJson("聚美实拍", false, doDict_聚美实拍());

        // 聚美白底方图
        doCreateJson("聚美白底方图", false, doDict_聚美白底方图());

    }

    @Test
    public void dictTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("015", 479584L);
        sxData.setCartId(27);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = Shops.getShop("015", 27);
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
            // 聚美详情
            System.out.println("=====================================");
            System.out.println("字典: 聚美详情");
            String result = sxProductService.resolveDict("聚美详情", expressionParser, shopProp, getTaskName(), null);
            System.out.println(result);

            // 聚美使用方法
            System.out.println("=====================================");
            System.out.println("字典: 聚美使用方法");
            result = sxProductService.resolveDict("聚美使用方法", expressionParser, shopProp, getTaskName(), null);
            System.out.println(result);

            // 聚美实拍
            System.out.println("=====================================");
            System.out.println("字典: 聚美实拍");
            result = sxProductService.resolveDict("聚美实拍", expressionParser, shopProp, getTaskName(), null);
            System.out.println(result);

            // 聚美白底方图
            System.out.println("=====================================");
            System.out.println("字典: 聚美白底方图");
            result = sxProductService.resolveDict("聚美白底方图", expressionParser, shopProp, getTaskName(), null);
            System.out.println(result);
            for (String picUrl : result.split(",")) {
//                String jmPicUrl = sxProductService.uploadImageByUrl_JM(picUrl, shopProp);
//                System.out.println(jmPicUrl);
                System.out.println(picUrl);
            }


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
     * 聚美详情
     * 1. 品牌故事图
     */
    private RuleExpression doDict_聚美详情() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        {
            // 品牌故事图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("3"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        return ruleRoot;

    }

    /**
     * 聚美使用方法
     * 1. 详情描述 - 中文
     * 2. 尺码图
     */
    private RuleExpression doDict_聚美使用方法() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        {
            // 详情描述 - 中文
            // 注意：<br> 替换成 <br />，并删除所有*号。
            MasterHtmlWord word = new MasterHtmlWord("longDesEn");
            ruleRoot.addRuleWord(word);
        }

        {
            // 回车一个
            TextWord word = new TextWord(C_TEXT_BR);
            ruleRoot.addRuleWord(word);
        }

        {
            // 尺码图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("2"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        return ruleRoot;

    }

    /**
     * 聚美实拍
     * 1. 商品图（要上传）
     *   模板:http://s7d5.scene7.com/is/image/sneakerhead/BHFO%2D20150730%2Dx790%2D600x?$790x600$&$790%5F600$&$product=%s
     * 2. PC端自定义图
     * 3. 购物流程图
     */
    private RuleExpression doDict_聚美实拍() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        {
            // 商品图片
            {
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

                RuleExpression imageTemplate = new RuleExpression();
                imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/jm-20170331-790-604?$790-604$&$img1=%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_商品图片));

                RuleExpression useOriUrl = null;

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }
        }

        {
            // 购物流程图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("4"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        return ruleRoot;

    }

    /**
     * 聚美白底方图
     * 1. (要上传)(非html)(逗号分隔的商品主图)
     *   模板:http://s7d5.scene7.com/is/image/sneakerhead/BHFO%5F2015%5Fx1000%5F1000x?$jc1000_1000$&$product=%s
     */
    private RuleExpression doDict_聚美白底方图() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        for (int i = 0; i < 5; i++) {

            // 第一个参数是product_id(GetMainProductImages)
            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();

            RuleExpression imageTemplate = new RuleExpression();
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
//            RuleExpression paddingTemplate = new RuleExpression();
//            paddingTemplate.addRuleWord(new TextWord(","));
//            userParam.setPaddingExpression(paddingTemplate);

            CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
            wordValueGetMainProductImages.setUserParam(userParam);

            ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));

        }

        return ruleRoot;

    }

}
