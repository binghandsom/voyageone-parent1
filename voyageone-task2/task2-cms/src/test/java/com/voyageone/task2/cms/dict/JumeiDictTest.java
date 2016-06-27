package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

/**
 * 聚美平台详情页描述JSON生成工具
 * 大部分店铺都用的是这个, 比如:
 *  (016)ShoeCity聚美优品店
 *  (022)DFO聚美优品店
 *  (023)ShoeZoo聚美优品店
 *
 * @author tom on 2016/6/17.
 * @version 2.1.0
 * @since 2.1.0
 */
public class JumeiDictTest {

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
     *   模板:http://s7d5.scene7.com/is/image/sneakerhead/BHFO%2D20150730%2Dx790%2D600x?$790x600$&$790%5F600$&$product=%s
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
