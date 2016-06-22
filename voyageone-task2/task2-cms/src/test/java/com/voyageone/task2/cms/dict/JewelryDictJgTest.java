package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Jewelry京东国际平台详情页描述JSON生成工具
 *
 * @author desmond on 2016/6/7.
 * @version 2.1.0
 * @since 2.1.0
 */
public class JewelryDictJgTest {

    String C_TEXT_BR = "<br />";
    String C_TEMPLATE_IMG = "<img src=%s>";

    String C_商品图片 = "PRODUCT_IMAGE";
    String C_包装图片 = "PACKAGE_IMAGE";
    String C_带角度图片 = "ANGLE_IMAGE";
    String C_自定义图片 = "CUSTOM_IMAGE";

    // -------------------------------------------------
    // 切换平台时，下列详情页固定图片URL需要修改
    // 精品描述图
    String C_URL_精品描述图 = "http://img10.360buyimg.com/imgzone/jfs/t2638/118/2083934877/9983/a62bf2a/575662e5N7cf4444b.jpg";
    // 手绘尺寸巴菲特图
    String C_URL_手绘尺寸巴菲特图 = "http://img10.360buyimg.com/imgzone/jfs/t2713/171/2061624255/1769894/83c7a70b/575662dfN6ab0bb49.jpg";
    // 珠宝知识图
    String C_URL_珠宝知识图 = "http://img10.360buyimg.com/imgzone/jfs/t2812/149/2092431244/176164/74268add/575662d9Nd3094a10.jpg";
    // 购买须知流程退换货图
    String C_URL_购买须知流程退换货图 = "http://img10.360buyimg.com/imgzone/jfs/t2761/73/2089688779/628089/a84ed2ad/575662d3N2e791a1a.jpg";

    // 珠宝知识html地址
    String C_HTML_珠宝知识 = "http://mall.jd.hk/view_page-46139406.html";
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

        // 生成内容

        {
            // div-000-start
            String html = "<div style=\"margin-left: auto;margin-right: auto;width: 790.0px;\">";
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
        }

        {
            {
                // div-001-start
                String html = "<div style=\"background-color: #f6f1ed;margin-bottom: 15.0px;\">";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }

            {

                {
                    // 精品描述图片
                    TextWord word = new TextWord(String.format(C_TEMPLATE_IMG, C_URL_精品描述图)); // jewelry JG
                    ruleRoot.addRuleWord(word);
                }

                {
                    {
                        // div-002-start
                        String html = "<div style=\"text-align: center;margin-left: 52.0px;margin-right: 52.0px;line-height: 200.0%;\">";
                        TextWord word = new TextWord(html);
                        ruleRoot.addRuleWord(word);
                    }

                    {
                        // 英文长描述
                        MasterWord word = new MasterWord("longDesEn");
                        ruleRoot.addRuleWord(word);
                    }

                    {
                        // 回车一个
                        TextWord word = new TextWord(C_TEXT_BR);
                        ruleRoot.addRuleWord(word);
                    }

                    {
                        // 中文长描述
                        MasterWord word = new MasterWord("longDesCn");
                        ruleRoot.addRuleWord(word);
                    }

                    {
                        // div-002-end
                        String html = "</div>";
                        TextWord word = new TextWord(html);
                        ruleRoot.addRuleWord(word);
                    }
                }

                {
                    {
                        // 商品属性图的前缀
                        String html =
                                "<div style=\"margin: 15.0px;\">" +
                                        "<img src=";
                        ruleRoot.addRuleWord(new TextWord(html));
                    }

                    // 商品属性图
                    RuleExpression imageTemplate = new RuleExpression();
                    String htmlTemplate =
                            "http://s7d5.scene7.com/is/image/sneakerhead/JEWERLY_20151014_x760_438x?$760x438$&$JEWERLY-760-438$" +
                                    "&$product=%s" + // 图片
                                    "&$text01=%s" +  // 标题1   文本1
                                    "&$text02=%s" +
                                    "&$text03=%s" +
                                    "&$text04=%s" +
                                    "&$text05=%s" +
                                    "&$text06=%s" +
                                    "&$text07=%s" +
                                    "&$text08=%s";
//                    String htmlTemplate = "42";
                    imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                    // 参数imageParams
                    List<RuleExpression> imageParams = new ArrayList<>();
                    // 参数
                    {
                        {
                            // 第一张商品图片
                            // 第一个参数是product_id(GetMainProductImages)
                            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
                            RuleExpression img_imageIndex = new RuleExpression();
                            img_imageIndex.addRuleWord(new TextWord("0"));
                            userParam.setImageIndex(img_imageIndex);
                            RuleExpression img_imageType = new RuleExpression();
                            img_imageType.addRuleWord(new TextWord(C_商品图片));
                            userParam.setImageType(img_imageType);

                            CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
                            wordValueGetMainProductImages.setUserParam(userParam);

                            RuleExpression imgWord = new RuleExpression();
                            imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
                            imageParams.add(imgWord);
                        }

                        {
                            // 第二个开始，共八个属性（品牌名称,产品类别,适用年龄,使用体重,固定方式,外形尺寸,材质用料,产品重量）
                            for (int index = 0; index < 8; index++) {
                                RuleExpression ruleExpression = new RuleExpression();
                                ruleExpression.addRuleWord(new FeedCnWord(true, index));
                                ruleExpression.addRuleWord(new TextWord("   "));
                                ruleExpression.addRuleWord(new FeedCnWord(false, index));
                                imageParams.add(ruleExpression);
                            }
                        }
//                        {
//                            // 第1个自定义字段
////                            // 的 标题
////                            RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 0)); imageParams.add(ruleExpression_key);
////                            // 的 值
////                            RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 0)); imageParams.add(ruleExpression_val);
//
//                            // 合并
//                            RuleExpression ruleExpression = new RuleExpression();
//                            ruleExpression.addRuleWord(new FeedCnWord(true, 0));
//                            ruleExpression.addRuleWord(new TextWord("   "));
//                            ruleExpression.addRuleWord(new FeedCnWord(false, 0));
//                            imageParams.add(ruleExpression);
//
//                        }
//                        {
//                            // 第2个自定义字段
////                            // 的 标题
////                            RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 1)); imageParams.add(ruleExpression_key);
////                            // 的 值
////                            RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 1)); imageParams.add(ruleExpression_val);
//
//                            // 合并
//                            RuleExpression ruleExpression = new RuleExpression();
//                            ruleExpression.addRuleWord(new FeedCnWord(true, 1));
//                            ruleExpression.addRuleWord(new TextWord("   "));
//                            ruleExpression.addRuleWord(new FeedCnWord(false, 1));
//                            imageParams.add(ruleExpression);
//
//                        }
//                        {
//                            // 第3个自定义字
////                            // 的 标题
////                            RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 2)); imageParams.add(ruleExpression_key);
////                            // 的 值
////                            RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 2)); imageParams.add(ruleExpression_val);
//
//                            // 合并
//                            RuleExpression ruleExpression = new RuleExpression();
//                            ruleExpression.addRuleWord(new FeedCnWord(true, 2));
//                            ruleExpression.addRuleWord(new TextWord("   "));
//                            ruleExpression.addRuleWord(new FeedCnWord(false, 2));
//                            imageParams.add(ruleExpression);
//
//                        }
//                        {
//                            // 第4个自定义字段
////                            // 的 标题
////                            RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 3)); imageParams.add(ruleExpression_key);
////                            // 的 值
////                            RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 3)); imageParams.add(ruleExpression_val);
//
//                            // 合并
//                            RuleExpression ruleExpression = new RuleExpression();
//                            ruleExpression.addRuleWord(new FeedCnWord(true, 3));
//                            ruleExpression.addRuleWord(new TextWord("   "));
//                            ruleExpression.addRuleWord(new FeedCnWord(false, 3));
//                            imageParams.add(ruleExpression);
//
//                        }
//                        {
//                            // 第5个自定义字段
////                            // 的 标题
////                            RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 4)); imageParams.add(ruleExpression_key);
////                            // 的 值
////                            RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 4)); imageParams.add(ruleExpression_val);
//
//                            // 合并
//                            RuleExpression ruleExpression = new RuleExpression();
//                            ruleExpression.addRuleWord(new FeedCnWord(true, 4));
//                            ruleExpression.addRuleWord(new TextWord("   "));
//                            ruleExpression.addRuleWord(new FeedCnWord(false, 4));
//                            imageParams.add(ruleExpression);
//
//                        }
//                        {
//                            // 第6个自定义字段
////                            // 的 标题
////                            RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 5)); imageParams.add(ruleExpression_key);
////                            // 的 值
////                            RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 5)); imageParams.add(ruleExpression_val);
//
//                            // 合并
//                            RuleExpression ruleExpression = new RuleExpression();
//                            ruleExpression.addRuleWord(new FeedCnWord(true, 5));
//                            ruleExpression.addRuleWord(new TextWord("   "));
//                            ruleExpression.addRuleWord(new FeedCnWord(false, 5));
//                            imageParams.add(ruleExpression);
//
//                        }
//                        {
//                            // 第7个自定义字段
////                            // 的 标题
////                            RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 6)); imageParams.add(ruleExpression_key);
////                            // 的 值
////                            RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 6)); imageParams.add(ruleExpression_val);
//
//                            // 合并
//                            RuleExpression ruleExpression = new RuleExpression();
//                            ruleExpression.addRuleWord(new FeedCnWord(true, 6));
//                            ruleExpression.addRuleWord(new TextWord("   "));
//                            ruleExpression.addRuleWord(new FeedCnWord(false, 6));
//                            imageParams.add(ruleExpression);
//
//                        }
//                        {
//                            // 第8个自定义字段
////                            // 的 标题
////                            RuleExpression ruleExpression_key = new RuleExpression(); ruleExpression_key.addRuleWord(new FeedCnWord(true, 7)); imageParams.add(ruleExpression_key);
////                            // 的 值
////                            RuleExpression ruleExpression_val = new RuleExpression(); ruleExpression_val.addRuleWord(new FeedCnWord(false, 7)); imageParams.add(ruleExpression_val);
//
//                            // 合并
//                            RuleExpression ruleExpression = new RuleExpression();
//                            ruleExpression.addRuleWord(new FeedCnWord(true, 7));
//                            ruleExpression.addRuleWord(new TextWord("   "));
//                            ruleExpression.addRuleWord(new FeedCnWord(false, 7));
//                            imageParams.add(ruleExpression);
//
//                        }

//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("标题1")); imageParams.add(ruleExpression);
//                        }
//                        {
//                            RuleExpression ruleExpression = new RuleExpression(); ruleExpression.addRuleWord(new TextWord("文本1")); imageParams.add(ruleExpression);
//                        }
                    }

                    CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams);
                    ruleRoot.addRuleWord(new CustomWord(word));

                    {
                        // 商品属性图的后缀
                        String html = ">" +
                                "</div>";
                        ruleRoot.addRuleWord(new TextWord(html));
                    }
                }

                {
                    // 所有商品图
                    RuleExpression htmlTemplate = new RuleExpression();
                    htmlTemplate.addRuleWord(new TextWord("<div style=\"margin:15px\" ><img src=\"%s\" style=\"display:block; width:760px;\" /></div>"));

                    RuleExpression imageTemplate = new RuleExpression();
                    imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%%5F20150819%%5Fx760%%5F760x?$760x760$&$JEWERLY%%2D760%%2D760$&$proudct=%s"));
//                    imageTemplate.addRuleWord(new TextWord("47"));

                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord(C_商品图片));

                    CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }

                {
                    // 所有自定义图
                    RuleExpression htmlTemplate = new RuleExpression();
                    htmlTemplate.addRuleWord(new TextWord("<div style=\"margin:15px\" ><img src=\"%s\" style=\"display:block; width:760px;\" /></div>"));

                    RuleExpression imageTemplate = new RuleExpression();
                    imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/JW_20160506_x790_526x?$790x526$&$product=%s"));
//                    imageTemplate.addRuleWord(new TextWord("49"));

                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord(C_自定义图片));

                    CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }

//                {
//                    // 四张手绘图
//
//                    // html
//                    String html_01 =
//                            "<div style=\"font-size: 0;padding: 0.0px 15.0px 15.0px 15.0px;\">" +
//                                    "    <div style=\"display: inline-block;width: 380.0px;border: solid 0.0px;\">" +
//                                    "        <img style=\"display: block;width: 380.0px;height: 251.0px;\" src=\"";
//                    ruleRoot.addRuleWord(new TextWord(html_01));
//
//                    // 左上角图片
//                    // TODO:添加自定义图片 CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams);
//                    ruleRoot.addRuleWord(new TextWord("https://img.alicdn.com/imgextra/i2/2183719539/TB2x8C5gpXXXXaNXpXXXXXXXXXX_!!2183719539.jpg"));
//
//                    // html
//                    String html_02 =
//                            "\">";
//                    ruleRoot.addRuleWord(new TextWord(html_02));
//
//                    // 左下角图片 - 手绘图 - 1
//                    {
//                        RuleExpression htmlTemplate = new RuleExpression();
//                        htmlTemplate.addRuleWord(new TextWord("<img style=\"display: block;width: 380.0px;height: 509.0px;\" src=\"%s\">"));
//                        RuleExpression imageTemplate = new RuleExpression();
//                        imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Jewerly_20150907_x380_509x?$308x509$&$Jewerly_380-509$&$product=%s"));
//                        RuleExpression imageIndex = new RuleExpression();
//                        imageIndex.addRuleWord(new TextWord("1")); // 手绘图中的第一张
//                        RuleExpression imageType = new RuleExpression();
//                        imageType.addRuleWord(new TextWord(C_自定义图片)); // 手绘图
//
//                        RuleExpression paddingExpression = get获取手绘图的默认图("1"); // 第一张替换图
//
//                        CustomWordValueGetMainProductImages word = new CustomWordValueGetMainProductImages(htmlTemplate, imageTemplate, imageIndex, imageType, paddingExpression);
//                        ruleRoot.addRuleWord(new CustomWord(word));
//                    }
//
//                    // html
//                    String html_03 =
//                            "    </div>" +
//                                    "    <div style=\"display: inline-block;width: 380.0px;border: solid 0.0px;\">";
//                    ruleRoot.addRuleWord(new TextWord(html_03));
//
//                    // 右上角图片 - 手绘图 - 2
//                    {
//                        RuleExpression htmlTemplate = new RuleExpression();
//                        htmlTemplate.addRuleWord(new TextWord("<img style=\"display: block;width: 380.0px;height: 509.0px;\" src=\"%s\">"));
//                        RuleExpression imageTemplate = new RuleExpression();
//                        imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Jewerly_20150907_x380_509x?$308x509$&$Jewerly_380-509$&$product=%s"));
//                        RuleExpression imageIndex = new RuleExpression();
//                        imageIndex.addRuleWord(new TextWord("2")); // 手绘图中的第二张
//                        RuleExpression imageType = new RuleExpression();
//                        imageType.addRuleWord(new TextWord(C_自定义图片)); // 手绘图
//
//                        RuleExpression paddingExpression = get获取手绘图的默认图("2"); // 第二张替换图
//
//                        CustomWordValueGetMainProductImages word = new CustomWordValueGetMainProductImages(htmlTemplate, imageTemplate, imageIndex, imageType, paddingExpression);
//                        ruleRoot.addRuleWord(new CustomWord(word));
//                    }
//
//                    // html
//                    String html_04 =
//                            "        <img style=\"display: block;width: 380.0px;height: 251.0px;\" src=\"";
//                    ruleRoot.addRuleWord(new TextWord(html_04));
//
//                    // 右下角图片 - 固定图片
//                    String html_05 = "https://img.alicdn.com/imgextra/i2/2183719539/TB2x8C5gpXXXXaNXpXXXXXXXXXX_!!2183719539.jpg";
//                    ruleRoot.addRuleWord(new TextWord(html_05));
//
//                    // html
//                    String html_06 =
//                            "\">" +
//                                    "    </div>" +
//                                    "</div>";
//                    ruleRoot.addRuleWord(new TextWord(html_06));
//
//                }

            }

            {
                // div-001-end
                String html = "</div>";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }

        }

        {
            // 手绘，尺寸参考, 关于我们, 巴菲特图
            String html = "<img src=\"" + C_URL_手绘尺寸巴菲特图 + "\">"; // jewelry JG
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
        }

        {
            // 珠宝知识图
            String html = "<a href=\"" + C_HTML_珠宝知识 + "\"><img src=\"" + C_URL_珠宝知识图 + "\"></a>"; // jewelry JG
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
        }

        {
            // 购买须知, 购买流程, 商品包装, 退货政策图
            String html = "<img src=\"" + C_URL_购买须知流程退换货图 + "\">"; // jewelry JG
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
        }

        {
            // div-000-end
            String html = "</div>";
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
        }


        return ruleRoot;
    }

    /**
     * 获取手绘图的默认图
     * 当手绘图不存在的场合, 用这张共通的图片替换掉
     *
     * @param paddingImageIndex 使用第几张替换的图
     * @return 默认图
     */
//    private RuleExpression get获取手绘图的默认图(String paddingImageIndex) {
//        RuleExpression paddingExpression = new RuleExpression();
//
//        RuleExpression paddingPropName = new RuleExpression();
//        paddingPropName.addRuleWord(new TextWord("Jewwery_handMade_image"));
//        RuleExpression imageIndex = new RuleExpression();
//        imageIndex.addRuleWord(new TextWord(paddingImageIndex));
//        CustomWordValueGetPaddingImageKey paddingImage = new CustomWordValueGetPaddingImageKey(paddingPropName, imageIndex);
//
//        paddingExpression.addRuleWord(new CustomWord(paddingImage));
//
//        return paddingExpression;
//
//    }

}
