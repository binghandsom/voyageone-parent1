package com.voyageone.task2.cms.dict;

import com.voyageone.common.configs.Enums.PlatFormEnums;
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
 * 京东国际平台详情页描述JSON生成工具
 * (928) 匠心界
 *
 * @author tom on 2016/7/6.
 * @version 2.1.0
 * @since 2.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Jingdong_928_Jiangxinjie_DictTest extends BaseDictTest {
    @Autowired
    private SxProductService sxProductService;
    @Test
    public void startupTest() {
//        doCreateJson("京东详情页描述", false, doDict_京东详情页描述(1));
//        doCreateJson("京东详情页描述-重点商品", false, doDict_京东详情页描述(2));
//        doCreateJson("京东详情页描述-无属性图", false, doDict_京东详情页描述(3));
//        doCreateJson("京东详情页描述-非重点之英文长描述", false, doDict_京东详情页描述(4));
//        doCreateJson("京东详情页描述-非重点之中文长描述", false, doDict_京东详情页描述(5));
//        doCreateJson("京东详情页描述-非重点之中文使用说明", false, doDict_京东详情页描述(6));
//        doCreateJson("京东详情页描述-爆款商品", false, doDict_京东详情页描述(7));

        doCreateJson("京东详情页描述-重点", false, doDict_京东详情页描述(2));
        doCreateJson("京东详情页描述-非重点", false, doDict_京东详情页描述(4));
        doCreateJson("京东详情页描述-爆款", false, doDict_京东详情页描述(7));


    }
    @Test
    public void testDict() {

        SxData sxData = sxProductService.getSxProductDataByGroupId("928", 10614204L);
        sxData.setCartId(28);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = new ShopBean();
        shopProp.setApp_url(""); // https://api.jd.com/routerjson
        shopProp.setAppKey("");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.JD.getId());

        try {
            System.out.println("=====================================");
            System.out.println("字典: 京东详情页描述");
            String result = sxProductService.resolveDict("京东详情页描述-重点", expressionParser, shopProp, "Jingdong_928_Jiangxinjie", null);
//            result = "<div style=\"width:790px; margin: 0 auto;\">" + result + "</div>";
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getTaskName() {
        return getClass().getName();
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

        if (propType == 4) { // 参数图 - 非重点之英文长描述
            // charis sta
            String html = "<div style=\"margin-top: -3px; border-width: 0 15px 0 15px; border-color: #f6f2f1 ; border-style: double solid; padding: 50px 20px 20px 20px; width: 720px;font-family:'microsoft yahei'; font-size: 13pt; color: #000000; \">";
            ruleRoot.addRuleWord(new TextWord(html));

            ruleRoot.addRuleWord(new MasterClrHtmlWord("longDesEn")); // 英文长描述

            ruleRoot.addRuleWord(new TextWord("</div>"));
            // charis end
        }

        if (propType == 2) {   // 参数图 - 重点商品

            {
                String tableTem = "<div style=\"margin-top: -3px; border-width: 0 15px 0 15px; border-color: #f6f2f1; border-style: double solid; padding: 50px 20px 20px 20px; width: 720px; \">" +
                        "<table style=\"width: 720px; font-family: microsoft yahei; font-size: 13pt; color: #000000; cellspacing: 0; cellpadding: 0\">";
                int columnCount = 2;
                RuleExpression tableTemplate = new RuleExpression();
                tableTemplate.addRuleWord(new TextWord(tableTem));

                // 参数tableParams
                List<RuleExpression> tableParams = new ArrayList<>();
                {
                    // 共7个属性
                    for (int index = 0; index < 16; index++) {
                        RuleExpression ruleExpression = new RuleExpression();
                        ruleExpression.addRuleWord(new FeedCnWord(true, index));
                        ruleExpression.addRuleWord(new TextWord("   "));
                        ruleExpression.addRuleWord(new FeedCnWord(false, index));
                        tableParams.add(ruleExpression);
                    }
                }

                CustomWordValueTableWithParam word = new CustomWordValueTableWithParam(tableTemplate, columnCount, tableParams);
                ruleRoot.addRuleWord(new CustomWord(word));
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
