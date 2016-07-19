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
public class Jingdong_929_YueJing_LuckyVitamin_DictTest {

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
        // 1.固定图片（关联版式-头部）-> 改为固定图片, 不使用关联版式
        //   <center>
        //   1.1 寻遍全球发现好货
        //   1.2 消费者告知书
        //   </center>
        // 2.自定义图(使用原图, getAllImages（参数使用原图设为1）)
        // 3.商品实拍图(动态,使用模板，getAllImages)
        // 4.固定图片（关联版式-尾部） --购物须知,购物流程-> 改为固定图片, 不使用关联版式
        //   <center>
        //   4.1 购物流程
        //   4.2 温馨提示
        //   4.3 售后须知
        //   </center>
        // -------------------------------------------

        // 生成内容
        {

            {
                {
                    TextWord textWord = new TextWord("<center><br />");
                    ruleRoot.addRuleWord(textWord);
                }

                {
                    //   1.1 寻遍全球发现好货
                    TextWord textWord = new TextWord("<img src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2614/193/2518100957/183329/4d2ed181/57691885Naec223d0.jpg\"><br />");
                    ruleRoot.addRuleWord(textWord);
                }

                {
                    //   1.2 消费者告知书
                    TextWord textWord = new TextWord("<img src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2938/149/794673756/131726/6d62c641/57691885N60b9803f.jpg\"><br />");
                    ruleRoot.addRuleWord(textWord);
                }

                {
                    TextWord textWord = new TextWord("</center><br />");
                    ruleRoot.addRuleWord(textWord);
                }
            }

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

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null);
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

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                {
                    TextWord textWord = new TextWord("<center><br />");
                    ruleRoot.addRuleWord(textWord);
                }

                {
                    //   4.1 购物流程
                    TextWord textWord = new TextWord("<img src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2932/112/792034825/74390/cc1ee443/576918daN74acf582.jpg\"><br />");
                    ruleRoot.addRuleWord(textWord);
                }

                {
                    //   4.2 温馨提示
                    TextWord textWord = new TextWord("<img src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2599/362/2585900445/111062/fd1de4de/576918e9N079e484c.jpg\"><br />");
                    ruleRoot.addRuleWord(textWord);
                }

                {
                    //   4.3 售后须知
                    TextWord textWord = new TextWord("<img src=\"http://img30.360buyimg.com/popWaterMark/jfs/t2896/79/2495768949/140387/224f1a88/576918f8N9f40000d.jpg\"><br />");
                    ruleRoot.addRuleWord(textWord);
                }

                {
                    TextWord textWord = new TextWord("</center><br />");
                    ruleRoot.addRuleWord(textWord);
                }
            }

        }

        return ruleRoot;
    }

}
