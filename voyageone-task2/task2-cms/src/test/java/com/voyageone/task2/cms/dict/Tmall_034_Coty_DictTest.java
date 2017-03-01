package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/12/23.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_034_Coty_DictTest extends BaseDictTest{

    @Test
    public void startupTest() {
        doCreateJson("详情页描述", false, doDict_详情页描述());
        doCreateJson("无线描述", false, doDict_无线描述());
    }

    private RuleExpression doDict_详情页描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        ruleRoot.addRuleWord(getPC店铺介绍图Word(0));
        ruleRoot.addRuleWord(getPC店铺介绍图Word(1));
        ruleRoot.addRuleWord(getPC自定义图Word(0));
        ruleRoot.addRuleWord(getPC店铺介绍图Word(2));
        ruleRoot.addRuleWord(getPC自定义图Word(1));
        ruleRoot.addRuleWord(getPC店铺介绍图Word(3));
        ruleRoot.addRuleWord(getPC店铺介绍图Word(4));
        ruleRoot.addRuleWord(getPC自定义图Word(2));
        ruleRoot.addRuleWord(getPC店铺介绍图Word(5));
        ruleRoot.addRuleWord(getPC店铺介绍图Word(6));

        return ruleRoot;
    }

    private CustomWord getPC店铺介绍图Word(int intIndex) {
        // 店铺介绍图
        RuleExpression htmlTemplate = new RuleExpression();
        htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

        RuleExpression imageType = new RuleExpression();
        imageType.addRuleWord(new TextWord("5"));

        RuleExpression viewType = new RuleExpression();
        viewType.addRuleWord(new TextWord("1"));

        RuleExpression imageIndex = new RuleExpression();
        imageIndex.addRuleWord(new TextWord(String.valueOf(intIndex)));

        CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, imageIndex);

        return new CustomWord(word);
    }

    private CustomWord getPC自定义图Word(int intIndex) {
        // 自定义图
        RuleExpression htmlTemplate = new RuleExpression();
        htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

        RuleExpression imageTemplate = null;

        RuleExpression imageType = new RuleExpression();
        imageType.addRuleWord(new TextWord(C_自定义图片));

        RuleExpression useOriUrl = new RuleExpression();
        useOriUrl.addRuleWord(new TextWord("1"));

        RuleExpression imageIndex = new RuleExpression();
        imageIndex.addRuleWord(new TextWord(String.valueOf(intIndex)));

        CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, imageIndex);

        return new CustomWord(word);
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

            do处理无线端20张图片(0, ruleRoot, getMobile店铺介绍图Word(0));
            do处理无线端20张图片(1, ruleRoot, getMobile店铺介绍图Word(1));
            do处理无线端20张图片(2, ruleRoot, new DictWord("无线自定义图片-1"));
            do处理无线端20张图片(3, ruleRoot, getMobile店铺介绍图Word(2));
            do处理无线端20张图片(4, ruleRoot, new DictWord("无线自定义图片-2"));
            do处理无线端20张图片(5, ruleRoot, getMobile店铺介绍图Word(3));
            do处理无线端20张图片(6, ruleRoot, getMobile店铺介绍图Word(4));
            do处理无线端20张图片(7, ruleRoot, new DictWord("无线自定义图片-3"));
            do处理无线端20张图片(8, ruleRoot, getMobile店铺介绍图Word(5));
            do处理无线端20张图片(9, ruleRoot, getMobile店铺介绍图Word(6));

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

    private CustomWord getMobile店铺介绍图Word(int intIndex) {
        // 第1张 店铺介绍图
        RuleExpression imageType = new RuleExpression();
        imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

        RuleExpression viewType = new RuleExpression();
        viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

        RuleExpression imageIndex = new RuleExpression();
        imageIndex.addRuleWord(new TextWord(String.valueOf(intIndex)));

        CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);

        return new CustomWord(getCommonImagesWord);
    }

}
