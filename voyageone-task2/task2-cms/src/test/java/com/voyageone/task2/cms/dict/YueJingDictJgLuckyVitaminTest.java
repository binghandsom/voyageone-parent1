package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

/**
 * Jewelry京东国际平台详情页描述JSON生成工具
 *
 * @author desmond on 2016/6/12.
 * @version 2.1.0
 * @since 2.1.0
 */
public class YueJingDictJgLuckyVitaminTest {

    String C_TEXT_BR = "<br />";
    String C_TEMPLATE_IMG = "<img src=%s>";

    String C_商品图片 = "PRODUCT_IMAGE";
//    String C_包装图片 = "PACKAGE_IMAGE";
//    String C_带角度图片 = "ANGLE_IMAGE";
    String C_自定义图片 = "CUSTOM_IMAGE";

    // -------------------------------------------------
    // 切换平台时，下列详情页固定图片URL需要修改
//    // 精品描述图
//    String C_URL_精品描述图 = "http://img10.360buyimg.com/imgzone/jfs/t2638/118/2083934877/9983/a62bf2a/575662e5N7cf4444b.jpg";
//    // 手绘尺寸巴菲特图
//    String C_URL_手绘尺寸巴菲特图 = "http://img10.360buyimg.com/imgzone/jfs/t2713/171/2061624255/1769894/83c7a70b/575662dfN6ab0bb49.jpg";
//    // 珠宝知识图
//    String C_URL_珠宝知识图 = "http://img10.360buyimg.com/imgzone/jfs/t2812/149/2092431244/176164/74268add/575662d9Nd3094a10.jpg";
//    // 购买须知流程退换货图
//    String C_URL_购买须知流程退换货图 = "http://img10.360buyimg.com/imgzone/jfs/t2761/73/2089688779/628089/a84ed2ad/575662d3N2e791a1a.jpg";
//
//    // 珠宝知识html地址
//    String C_HTML_珠宝知识 = "http://mall.jd.hk/view_page-46139406.html";
    // -------------------------------------------------

    @Test
    public void startupTest() {

        doCreateJson("京东详情页描述", false, doDict_详情页描述());
//        doCreateJson("京东详情页描述", false, doDict_详情页描述_TEST());
//        doCreateJson("关联版式", false, doDict_关联版式_TEST());

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

    private RuleExpression doDict_详情页描述_TEST() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // div-000-start
            String html = "京东详情页描述";
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
        }

        return ruleRoot;

    }

    private RuleExpression doDict_关联版式_TEST() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // div-000-start
            String html = "1751383";
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
        }

        return ruleRoot;

    }

    private RuleExpression doDict_详情页描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 悦境店详情页全是由图片构成，没有背景什么的
        // -------------------------------------------
        // 1.固定图片（关联版式-头部）
        // 2.参数图(或商品属性图)(动态,使用模板，getAllImages)
        // 3.自定义图(使用原图)
        // 4.商品实拍图的提示文字固定图片
        // 5.商品实拍图(动态,使用模板，getAllImages)
        // 6.固定图片（关联版式-尾部） --购物须知,购物流程
        // -------------------------------------------

        // 生成内容
        {

//            {
//                // 参数图（商品属性图）
//                {
//                    // 参数图(商品属性图)的前缀
//                    String html = "<img src=\"";
//                    ruleRoot.addRuleWord(new TextWord(html));
//                }
//
//                {
//                    // 参数图（商品属性图）
//                    RuleExpression imageTemplate = new RuleExpression();
//                    String htmlTemplate =
//                            "http://s7d5.scene7.com/is/image/sneakerhead/VTM-JD-CST?$vtm_790x415$" +
//                                    "&$img=%s"    +    // 主产品图片
//                                    "&$t1=%s"     +    // 标题1
//                                    "&$text01=%s" +    // 文本1
//                                    "&$t2=%s"     +
//                                    "&$text02=%s" +
//                                    "&$t3=%s"     +
//                                    "&$text03=%s" +
//                                    "&$t4=%s"     +
//                                    "&$text04=%s" +
//                                    "&$t5=%s"     +
//                                    "&$text05=%s" +
//                                    "&$t6=%s"     +
//                                    "&$text06=%s" +
//                                    "&$t7=%s"     +
//                                    "&$text07=%s" +
//                                    "&$t8=%s"     +
//                                    "&$text08=%s";
//                    imageTemplate.addRuleWord(new TextWord(htmlTemplate));
//
//                    // 参数imageParams
//                    List<RuleExpression> imageParams = new ArrayList<>();
//                    // 参数
//                    {
//                        {
//                            // 第一张商品图片
//                            // 第一个参数是product_id(GetMainProductImages)
//                            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
//                            RuleExpression img_imageIndex = new RuleExpression();
//                            img_imageIndex.addRuleWord(new TextWord("0"));
//                            userParam.setImageIndex(img_imageIndex);
//                            RuleExpression img_imageType = new RuleExpression();
//                            img_imageType.addRuleWord(new TextWord(C_商品图片));
//                            userParam.setImageType(img_imageType);
//
//                            CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
//                            wordValueGetMainProductImages.setUserParam(userParam);
//
//                            RuleExpression imgWord = new RuleExpression();
//                            imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
//                            imageParams.add(imgWord);
//                        }
//
//                        {
//                            // 第二个参数开始，共八个属性的标题和文本（品牌名称,产品类别,适用年龄,使用体重,固定方式,外形尺寸,材质用料,产品重量）
//                            for (int index = 0; index < 8; index++) {
//                                // 第index个自定义字段
//                                // 的 标题
//                                RuleExpression ruleExpression_key = new RuleExpression();
//                                ruleExpression_key.addRuleWord(new FeedCnWord(true, index));
//                                imageParams.add(ruleExpression_key);
//                                // 的 值
//                                RuleExpression ruleExpression_val = new RuleExpression();
//                                ruleExpression_val.addRuleWord(new FeedCnWord(false, index));
//                                imageParams.add(ruleExpression_val);
//                            }
//                        }
//                    }
//
//                    CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams);
//                    ruleRoot.addRuleWord(new CustomWord(word));
//                }
//
//                {
//                    // 参数图(商品属性图)的后缀
//                    String html = "\" />";
//                    ruleRoot.addRuleWord(new TextWord(html));
//                }
//            }

            {
                // 所有自定义图(getAllImages（注意参数里要设置使用原图）)
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\" />"));

                RuleExpression imageTemplate = new RuleExpression();
                imageTemplate.addRuleWord(new TextWord(""));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_自定义图片));

                RuleExpression useOriUrl = new RuleExpression();
                useOriUrl.addRuleWord(new TextWord("1")); // 使用原图

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                // 所有商品图的提示文字固定图片
                TextWord textWord = new TextWord("<img src=\"http://img10.360buyimg.com/imgzone/jfs/t2746/300/2296315934/10785/df3e02ca/575f7935N54e565a9.jpg\" />");
                ruleRoot.addRuleWord(textWord);
            }

            {
                // 所有商品图(商品实拍图)
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\" />"));

                RuleExpression imageTemplate = new RuleExpression();
                // 模板url里面如果需要出力%，需要(% -> %%)，不然String.format()会报错
                // 错误信息(java.util.UnknownFormatConversionException: Conversion = 'i')
                imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/product%%20image?$790x790$&$img=%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_商品图片));

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

        }

        return ruleRoot;
    }

}
