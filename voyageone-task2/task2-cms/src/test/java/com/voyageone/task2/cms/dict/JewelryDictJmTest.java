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
 * Jewelry聚美平台详情页描述JSON生成工具
 *
 * @author tom on 2016/6/17.
 * @version 2.1.0
 * @since 2.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JewelryDictJmTest {
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
        SxData sxData = sxProductService.getSxProductDataByGroupId("010", 20893L);
        sxData.setCartId(27);
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
     * 1. PC端自定义图 (要上传)（原图）
     * 2. 商品参数图
     *   模板:http://s7d5.scene7.com/is/image/sneakerhead/JEWERLY_20151014_x760_438x?$760x438$&$JEWERLY-760-438$&$$&$product=%s&$title01=%s&$text01=%s&$title02=%s&$text02=%s&$title03=%s&$text03=%s&$title04=%s&$text04=%s&$title05=%s&$text05=%s&$title06=%s&$text06=%s&$title07=%s&$text07=%s&$title08=%s&$text08=%s
     *   模板例子:http://s7d5.scene7.com/is/image/sneakerhead/JEWERLY_20151014_x760_438x?$760x438$&$JEWERLY-760-438$&$$&$product=101-074_sz7_main_1&$title01=> 产品品牌:&$text01=Kabana&$title02=> 产品材质:&$text02=925 银&$title03=> 产品宝石:&$text03=无宝石&$title04=> 产品尺寸:&$text04=宽 3/8 英寸（约 0.95 cm）&$title05=&$text05=&$title06=&$text06=&$title07=&$text07=&$title08=&$text08=
     * 3. 品牌故事图
     */
    private RuleExpression doDict_聚美详情() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        {

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

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            // 商品参数图
            {
                // 前缀
                String html = "<div><img src=\"";
                ruleRoot.addRuleWord(new TextWord(html));
            }

            {
                // imageTemplate
                RuleExpression imageTemplate = new RuleExpression();
//                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/JEWERLY_20151014_x760_438x?$760x438$&$JEWERLY-760-438$&$$&$product=%s&$title01=%s&$text01=%s&$title02=%s&$text02=%s&$title03=%s&$text03=%s&$title04=%s&$text04=%s&$title05=%s&$text05=%s&$title06=%s&$text06=%s&$title07=%s&$text07=%s&$title08=%s&$text08=%s";
//                String htmlTemplate =
//                        "http://s7d5.scene7.com/is/image/sneakerhead/JEWERLY_20151014_x760_438x?$760x438$&$JEWERLY-760-438$" +
//                                "&$product=%s" + // 图片
//                                "&$text01=%s" +  // 标题1   文本1
//                                "&$text02=%s" +
//                                "&$text03=%s" +
//                                "&$text04=%s" +
//                                "&$text05=%s" +
//                                "&$text06=%s" +
//                                "&$text07=%s" +
//                                "&$text08=%s";

                String htmlTemplate =
                        "http://s7d5.scene7.com/is/image/sneakerhead/JEWERLY_20151014_x760_438x?$760x438$&$JEWERLY-760-438$" +
                                "&$product=%s" + // 图片
                                "&$title01=%s" +
                                "&$text01=%s" +
                                "&$title02=%s" +
                                "&$text02=%s" +
                                "&$title03=%s" +
                                "&$text03=%s" +
                                "&$title04=%s" +
                                "&$text04=%s" +
                                "&$title05=%s" +
                                "&$text05=%s" +
                                "&$title06=%s" +
                                "&$text06=%s" +
                                "&$title07=%s" +
                                "&$text07=%s" +
                                "&$title08=%s" +
                                "&$text08=%s";
                imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                // 参数imageParams
                List<RuleExpression> imageParams = new ArrayList<>();

                {
                    // 第一个参数是product_id(GetMainProductImages)
                    CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("0"));
                    userParam.setImageIndex(imageIndex);
                    RuleExpression img_imageType = new RuleExpression();
                    img_imageType.addRuleWord(new TextWord(C_商品图片));
                    userParam.setImageType(img_imageType);

                    CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
                    wordValueGetMainProductImages.setUserParam(userParam);

                    RuleExpression imgWord = new RuleExpression();
                    imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
                    imageParams.add(imgWord);
                }

//                {
//                    // 第二个开始，共八个属性
//                    for (int index = 0; index < 8; index++) {
//                        RuleExpression ruleExpression = new RuleExpression();
//                        ruleExpression.addRuleWord(new FeedCnWord(true, index));
//                        ruleExpression.addRuleWord(new TextWord("   "));
//                        ruleExpression.addRuleWord(new FeedCnWord(false, index));
//                        imageParams.add(ruleExpression);
//                    }
//                }

                {
                    // 第二个开始，共八个属性
                    for (int index = 0; index < 8; index++) {
                        {
                            RuleExpression ruleExpression = new RuleExpression();
                            ruleExpression.addRuleWord(new FeedCnWord(true, index));
                            imageParams.add(ruleExpression);
                        }
                        {
                            RuleExpression ruleExpression = new RuleExpression();
                            ruleExpression.addRuleWord(new FeedCnWord(false, index));
                            imageParams.add(ruleExpression);
                        }
                    }
                }

                CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                // 后缀
                String html = "\"></div>";
                ruleRoot.addRuleWord(new TextWord(html));
            }
        }

        {
            // 品牌故事图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("3"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        return ruleRoot;

    }

	/**
     * 聚美使用方法
     * 1. 尺码图
     */
    private RuleExpression doDict_聚美使用方法() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        {
            // 尺码图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("2"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        return ruleRoot;

    }

	/**
     * 聚美实拍
     * 1. 商品图（要上传）
     *   模板:http://s7d5.scene7.com/is/image/sneakerhead/JW%5F20160202%5Fx790%5F790x?$790x700$&$790%5F700$&$product=%s
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
                imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/JW%%5F20160202%%5Fx790%%5F790x?$790x700$&$790%%5F700$&$product=%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_商品图片));

                RuleExpression useOriUrl = null;

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl);
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

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl);
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
