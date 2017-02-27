package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东国际平台详情页描述JSON生成工具
 * (928) 匠心界
 *
 * @author tom on 2016/7/6.
 * @version 2.1.0
 * @since 2.1.0
 */
public class Jingdong_928_Jiangxinjie_DictTest extends BaseDictTest {

    @Test
    public void startupTest() {
        doCreateJson("京东详情页描述", false, doDict_京东详情页描述(1));
        doCreateJson("京东详情页描述-重点商品", false, doDict_京东详情页描述(2));
        doCreateJson("京东详情页描述-无属性图", false, doDict_京东详情页描述(3));
        doCreateJson("京东详情页描述-非重点之英文长描述", false, doDict_京东详情页描述(4));
        doCreateJson("京东详情页描述-非重点之中文长描述", false, doDict_京东详情页描述(5));
        doCreateJson("京东详情页描述-非重点之中文使用说明", false, doDict_京东详情页描述(6));
        doCreateJson("京东详情页描述-爆款商品", false, doDict_京东详情页描述(7));
    }

    private RuleExpression doDict_京东详情页描述(int propType) {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // 店铺介绍图 - 0
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("5"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }
        {
            // 店铺介绍图 - 1
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("5"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            // 固定图片 - 商品信息
            ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "http://img10.360buyimg.com/imgzone/jfs/t3115/318/6412780379/20393/c1ba43c5/58a26d60N588c7ae8.jpg")));
        }

        if (propType != 7) { // 爆款商品之外的都需要
            // 品牌故事图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

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
                String html = "<img width=790px src=\"";
                ruleRoot.addRuleWord(new TextWord(html));
            }

            {
                // imageTemplate
                RuleExpression imageTemplate = new RuleExpression();
                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/liking-18-790X260?$790X300$&$wenzi=%s";
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
                String html = "<img width=790px src=\"";
                ruleRoot.addRuleWord(new TextWord(html));
            }

            {
                // imageTemplate
                RuleExpression imageTemplate = new RuleExpression();
                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/liking790X373xinxi?$790X373$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s&$6=%s&$7=%s&$8=%s&$9=%s&$10=%s&$11=%s&$12=%s&$13=%s&$14=%s&$15=%s&$16=%s";
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

        // 自定义图
        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageTemplate = null;

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_自定义图片));

            RuleExpression useOriUrl = new RuleExpression();
            useOriUrl.addRuleWord(new TextWord("1")); // 使用原图

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        if (propType != 7) { // 爆款商品之外的都需要
            {
                // 固定图片 - 商品展示
                ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "http://img10.360buyimg.com/imgzone/jfs/t4105/180/2050036540/25062/f941875f/58a26dc0Nc93421e8.jpg")));
            }

            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageTemplate = new RuleExpression();
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/jinxinjie790X740show?$790X740$&$image=%s"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_商品图片));

            RuleExpression useOriUrl = null;

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            // 尺码图 - 0
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("2"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }
        {
            // 尺码图 - 1
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("2"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }
        {
            // 尺码图 - 2
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("2"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("2"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            // 测量方式图 - 0
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("7"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            // 使用说明图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("6"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            // 购物流程图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("4"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        return ruleRoot;
    }


}
