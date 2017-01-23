package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2016/12/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_030_Wella_DictTest extends BaseDictTest{

    @Test
    public void startupTest() {
        doCreateJson("详情页描述", false, doDict_详情页描述());
        doCreateJson("无线描述", false, doDict_无线描述());
    }

    private RuleExpression doDict_详情页描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // 店铺介绍图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("5"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            // 自定义图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageTemplate = null;

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_自定义图片));

            RuleExpression useOriUrl = new RuleExpression();
            useOriUrl.addRuleWord(new TextWord("1"));

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
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

    private RuleExpression doDict_无线描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // start
            String kv = "{\"wireless_desc\":{";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // item_info 商品信息
            String kv = "\"item_info\":{\"item_info_enable\":\"true\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // coupon 优惠
            String kv = "\"coupon\":{\"coupon_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // hot_recommanded 同店推荐
            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // shop_discount 店铺活动
            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // user_define 自定义
            String kv = "\"user_define\":{\"user_define_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // item_picture 商品图片
            String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
            TextWord prefix = new TextWord(kv);
            ruleRoot.addRuleWord(prefix);

            // 共通图片-物流图  (3张无线图片)
            for (int i = 0; i < 3; i++) {
                // 第1张 店铺介绍图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord(String.valueOf(i)));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理无线端20张图片(i, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            for (int i = 0; i < 12; i++) {
                // 八张自定义图
                do处理无线端20张图片((i + 3), ruleRoot, new DictWord("无线自定义图片-" + (i + 1))); // 原图，参照target
            }

            // 共通图片-物流图  (5张无线图片)
            for (int i = 0; i < 5; i++) {
                // 物流图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord(String.valueOf(i)));   // 图片index(0,1)

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理无线端20张图片((15 + i), ruleRoot, new CustomWord(getCommonImagesWord));
            }

            // end
            String endStr = "}";
            TextWord endWord = new TextWord(endStr);
            ruleRoot.addRuleWord(endWord);
        }

        {
            // end
            String kv = "}}";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        return ruleRoot;
    }

}
