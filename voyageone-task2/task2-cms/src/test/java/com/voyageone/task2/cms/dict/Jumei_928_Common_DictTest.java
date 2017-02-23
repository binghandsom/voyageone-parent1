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

import java.util.ArrayList;
import java.util.List;

/**
 * 聚美平台详情页描述JSON生成工具
 *  (928)总店聚美优品店
 *
 * @author tom on 2016/12/22.
 * @version 2.1.0
 * @since 2.1.0
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:context-cms-test.xml")
public class Jumei_928_Common_DictTest {
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
        doCreateJson("聚美详情", false, doDict_聚美详情(1));
        doCreateJson("聚美详情-重点商品", false, doDict_聚美详情(2));
        doCreateJson("聚美详情-无属性图", false, doDict_聚美详情(3));
        doCreateJson("聚美详情-非重点之英文长描述", false, doDict_聚美详情(4));
        doCreateJson("聚美详情-非重点之中文长描述", false, doDict_聚美详情(5));
        doCreateJson("聚美详情-非重点之中文使用说明", false, doDict_聚美详情(6));

        // 聚美使用方法
        doCreateJson("聚美使用方法", false, doDict_聚美使用方法());

        // 聚美实拍
        doCreateJson("聚美实拍", false, doDict_聚美实拍());

        // 聚美白底方图
        doCreateJson("聚美白底方图", false, doDict_聚美白底方图());

    }

    @Test
    public void dictTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("010", 20893L);
        sxData.setCartId(27);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = Shops.getShop("010", 27);
//        shopProp.setCart_id("27");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.JM.getId());

        // 最终测试结果是这样的 ======================================== START
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
     * 2. pc端自定义图
     */
    private RuleExpression doDict_聚美详情(int propType) {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        {
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

            if (propType == 1 // 参数图 - 普通商品（非重点之英文使用说明）
                    || propType == 4 // 参数图 - 非重点之英文长描述
                    || propType == 5 // 参数图 - 非重点之中文长描述
                    || propType == 6 // 参数图 - 非重点之中文使用说明
                    ) {

                {
                    // 前缀
                    String html = "<img src=\"";
                    ruleRoot.addRuleWord(new TextWord(html));
                }

                {
                    // imageTemplate
                    RuleExpression imageTemplate = new RuleExpression();
                    String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/jumei790X300wenzi?$790X300$&$wenzi=%s";
                    imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                    // 参数imageParams
                    List<RuleExpression> imageParams = new ArrayList<>();

                    {
                        // 第一个参数是描述
                        RuleExpression ruleExpression = new RuleExpression();
                        switch (propType) {
                            case 1:
                                ruleExpression.addRuleWord(new MasterClrHtmlWord("usageEn")); // 英文使用方法
                                break;
                            case 4:
                                ruleExpression.addRuleWord(new MasterClrHtmlWord("longDesEn")); // 英文长描述
                                break;
                            case 5:
                                ruleExpression.addRuleWord(new MasterClrHtmlWord("longDesCn")); // 中文长描述
                                break;
                            case 6:
                                ruleExpression.addRuleWord(new MasterClrHtmlWord("usageCn")); // 非重点之中文使用说明
                                break;
                        }
                        imageParams.add(ruleExpression);
                    }

                    CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }

                {
                    // 后缀
                    String html = "\">";
                    ruleRoot.addRuleWord(new TextWord(html));
                }
            }

            if (propType == 2) {   // 参数图 - 重点商品

                {
                    // 前缀
                    String html = "<img src=\"";
                    ruleRoot.addRuleWord(new TextWord(html));
                }

                {
                    // imageTemplate
                    RuleExpression imageTemplate = new RuleExpression();
                    String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/likingjumei790X373?$790X373$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s&$6=%s&$7=%s&$8=%s&$9=%s&$10=%s&$11=%s&$12=%s&$13=%s&$14=%s&$15=%s&$16=%s";
                    imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                    // 参数imageParams
                    List<RuleExpression> imageParams = new ArrayList<>();

                    {
                        // 共7个属性
                        for (int index = 0; index < 16; index++) {
                            RuleExpression ruleExpression = new RuleExpression();
                            ruleExpression.addRuleWord(new FeedCnWord(true, index));
                            ruleExpression.addRuleWord(new TextWord("   "));
                            ruleExpression.addRuleWord(new FeedCnWord(false, index));
                            imageParams.add(ruleExpression);
                        }
                    }

                    CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }

                {
                    // 后缀
                    String html = "\">";
                    ruleRoot.addRuleWord(new TextWord(html));
                }
            }

            {
                // 商品自定义图
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

                RuleExpression imageTemplate = new RuleExpression();
                imageTemplate.addRuleWord(new TextWord(""));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_自定义图片));

                RuleExpression useOriUrl = new RuleExpression();
                useOriUrl.addRuleWord(new TextWord("1"));

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

        }

        return ruleRoot;

    }

	/**
     * 聚美使用方法
     * 1. 长描述中文
     * 2. 材质中文
     * 3. 尺码图
     */
    private RuleExpression doDict_聚美使用方法() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
//        {
//            // 详情描述 - 中文
//            // 注意：<br> 替换成 <br />，并删除所有*号。
//            MasterHtmlWord word = new MasterHtmlWord("longDesCn");
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // 回车一个
//            TextWord word = new TextWord(C_TEXT_BR);
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // 材质 - 中文
//            MasterHtmlWord masterWordmaterialCn = new MasterHtmlWord("materialCn");
//            ruleRoot.addRuleWord(masterWordmaterialCn);
//        }
//
//        {
//            // 回车
//            TextWord textWord = new TextWord("<br />");
//            ruleRoot.addRuleWord(textWord);
//        }

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

        {
            // 测量方式图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("7"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            // 回车
            TextWord textWord = new TextWord("<br />");
            ruleRoot.addRuleWord(textWord);
        }

        return ruleRoot;

    }

	/**
     * 聚美实拍
     * 1. 商品图
     * 2. 购物流程图
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
                imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/BHFO%%2D20150730%%2Dx790%%2D600x?$790x600$&$790%%5F600$&$product=%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_商品图片));

                RuleExpression useOriUrl = null;

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }
        }

        {
            // 使用说明图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("6"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));
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
