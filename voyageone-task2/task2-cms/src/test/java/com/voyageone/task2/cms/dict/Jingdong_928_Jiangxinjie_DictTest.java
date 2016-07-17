package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

/**
 * 京东国际平台详情页描述JSON生成工具
 * (928) 匠心界
 *
 * @author tom on 2016/7/6.
 * @version 2.1.0
 * @since 2.1.0
 */
public class Jingdong_928_Jiangxinjie_DictTest {

    String C_TEXT_BR = "<br />";
    String C_TEMPLATE_IMG = "<img src=%s>";

    String C_商品图片 = "PRODUCT_IMAGE";
//    String C_包装图片 = "PACKAGE_IMAGE";
//    String C_带角度图片 = "ANGLE_IMAGE";
    String C_自定义图片 = "CUSTOM_IMAGE";

    // -------------------------------------------------

    @Test
    public void startupTest() {

        doCreateJson("京东详情页描述", false, doDict_详情页描述());

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

    private RuleExpression doDict_详情页描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 匠心界详情页
        // -------------------------------------------
        // 1. 共通图片 - 店铺介绍图
        // 2. 共通图片 - 品牌故事图
        // 3. 产品展示标题图(固定图片)
        // 4. 产品展示图 - (商品图片) - 模板()
        // 5. 尺码指南标题(固定)
        // 6. 共通图片 - 尺码图
        // 7. 共通图片 - 购物流程图
        // -------------------------------------------

        // 生成内容
        {

//            {
//                // 店铺介绍图
//                RuleExpression htmlTemplate = new RuleExpression();
//                htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));
//
//                RuleExpression imageType = new RuleExpression();
//                imageType.addRuleWord(new TextWord("5"));
//
//                RuleExpression viewType = new RuleExpression();
//                viewType.addRuleWord(new TextWord("1"));
//
//                RuleExpression useOriUrl = null;
//
//                CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl);
//                ruleRoot.addRuleWord(new CustomWord(word));
//            }
            {
                // 店铺介绍图 (临时写死两张图)
                {
                    String html = "<img src=\"https://img10.360buyimg.com/imgzone/jfs/t2770/279/3106099073/166614/6b027351/5783655dN31d8f51f.jpg\">";
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }
                {
                    String html = "<img src=\"https://img10.360buyimg.com/imgzone/jfs/t2839/256/3185848904/115544/e2eaca8e/5783655dN7befefb7.jpg\">";
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
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

                CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                {
                    // 产品展示标题图
                    String html = "<img src=\"https://img10.360buyimg.com/imgzone/jfs/t2986/244/1446991714/7866/d2e26f50/5783655fN10ab7f3e.jpg\">";
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }
                {
                    // 所有商品图(商品实拍图)
                    RuleExpression htmlTemplate = new RuleExpression();
                    htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\" />"));

                    RuleExpression imageTemplate = new RuleExpression();
                    // 模板url里面如果需要出力%，需要(% -> %%)，不然String.format()会报错
                    // 错误信息(java.util.UnknownFormatConversionException: Conversion = 'i')
                    imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/790X500%%2Dimg%%2DJXJ?$790%%5F500$&$img=%s"));

                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord(C_商品图片));

                    CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }
            }

            {
                {
                    // 尺码图的标题图
                    String html = "<img src=\"https://img10.360buyimg.com/imgzone/jfs/t2707/89/3170742214/8310/78cabe18/57836560N708b07e4.jpg\">";
                    TextWord word = new TextWord(html);
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
            }

//            {
//                // 购物流程图
//                RuleExpression htmlTemplate = new RuleExpression();
//                htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));
//
//                RuleExpression imageType = new RuleExpression();
//                imageType.addRuleWord(new TextWord("4"));
//
//                RuleExpression viewType = new RuleExpression();
//                viewType.addRuleWord(new TextWord("1"));
//
//                RuleExpression useOriUrl = null;
//
//                CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl);
//                ruleRoot.addRuleWord(new CustomWord(word));
//            }

            {
                // 购物流程图 (临时写死四张图)
                {
                    String html = "<img src=\"https://img10.360buyimg.com/imgzone/jfs/t2797/201/3143502543/55794/84ba32a3/57836561N0a46dd4c.jpg\">";  // 购物流程
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }
                {
                    String html = "<img src=\"https://img10.360buyimg.com/imgzone/jfs/t2746/359/3200059128/86041/d0a17c52/57836561Neb557734.jpg\">";  // 温馨提示
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }
                {
                    String html = "<img src=\"https://img10.360buyimg.com/imgzone/jfs/t2947/115/1468462332/115720/455a7c74/57836562N875ad314.jpg\">";  // 售后须知
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }
                {
                    String html = "<img src=\"https://img10.360buyimg.com/imgzone/jfs/t2752/246/3115358048/201357/1cc619ee/57836562Ne0618ed2.jpg\">";  // 售后与质保范围
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }
            }

        }

        return ruleRoot;
    }

}
