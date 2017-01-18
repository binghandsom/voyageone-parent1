package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/12/23.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_TongGou_928_LIKING_DictTest extends BaseDictTest{

    @Test
    public void startupTest() {
        doCreateJson("天猫同购描述", false, doDict_天猫同购描述(false));
        doCreateJson("天猫同购描述-重点商品", false, doDict_天猫同购描述(true));

        doCreateJson("天猫同购无线描述", false, doDict_天猫同购无线描述(false));
        doCreateJson("天猫同购无线描述-重点商品", false, doDict_天猫同购无线描述(true));
    }

    private RuleExpression doDict_天猫同购描述(boolean blnImport) {
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

        {
            // 固定图片 - 商品信息
            ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i1/3081276392/TB2Y4tkdbBnpuFjSZFGXXX51pXa_!!3081276392.jpg")));
        }

        if (!blnImport) {   // 参数图 - 普通商品
            {
                // 前缀
                String html = "<img width=790px src=\"";
                ruleRoot.addRuleWord(new TextWord(html));
            }

            {
                // imageTemplate
                RuleExpression imageTemplate = new RuleExpression();
                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/likingtmall_790X200?$pc790X200$&$wenzi=%s";
                imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                // 参数imageParams
                List<RuleExpression> imageParams = new ArrayList<>();

                {
                    // 第一个参数是描述
                    RuleExpression ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(new MasterClrHtmlWord("longDesEn")); // 英文长描述
                    ruleExpression.addRuleWord(new MasterClrHtmlWord("usageEn")); // 英文使用方法
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

        if (blnImport) {   // 参数图 - 重点商品
            {
                // 前缀
                String html = "<img width=790px src=\"";
                ruleRoot.addRuleWord(new TextWord(html));
            }

            {
                // imageTemplate
                RuleExpression imageTemplate = new RuleExpression();
                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/likingtmall_790X237?$pc790X237$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s&$6=%s&$7=%s";
                imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                // 参数imageParams
                List<RuleExpression> imageParams = new ArrayList<>();

                {
                    // 共7个属性
                    for (int index = 0; index < 7; index++) {
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
            // 固定图片 - 商品展示
            ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i1/3081276392/TB2Y4tkdbBnpuFjSZFGXXX51pXa_!!3081276392.jpg")));
        }

        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageTemplate = new RuleExpression();
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/likingtmall_790X720?$pc790X720$&$image=%s"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_自定义图片));

            RuleExpression useOriUrl = null;

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageTemplate = new RuleExpression();
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/likingtmall_790X720?$pc790X720$&$image=%s"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_商品图片));

            RuleExpression useOriUrl = null;

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            // 使用说明图 - 0
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("6"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression useOriUrl = null;

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
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

    private RuleExpression doDict_天猫同购无线描述(boolean blnImport) {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // start（这里不用写wireless_desc，在TmTongGouService里面会把这里的value加上key(wireless_desc)放进map里的）
            String kv = "{";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // coupon 优惠（order不加也可以）
            String kv = "\"coupon\": {\"enable\": \"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // hot_recommanded 同店推荐
            String kv = "\"hot_recommanded\": {\"enable\": \"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

//        {
//            // shop_discount 店铺活动
////            String kv = "\"shop_discount\":{\"enable\":\"true\",\"shop_discount_id\":\"342160\"},";
//            String kv = "\"shop_discount\":{\"order\": \"3\",\"enable\":\"false\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }

        // 图片全是DictWord(总共20张图片)
        {
            // item_picture 商品图片 （每张图片url是放在content里面的）
            {
                String kv = "\"item_picture\": {\"content\": [";
                TextWord word = new TextWord(kv);
                ruleRoot.addRuleWord(word);
            }

            {
                // 店铺介绍图 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(0, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            {
                // 店铺介绍图 - 1
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("1"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(1, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            {
                // 品牌故事图 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(2, ruleRoot, new CustomWord(getCommonImagesWord));
            }

			{
				do处理天猫同购无线端20张图片(3, ruleRoot, new TextWord("https://img.alicdn.com/imgextra/i1/3081276392/TB2Y4tkdbBnpuFjSZFGXXX51pXa_!!3081276392.jpg"));
			}

            if (!blnImport) {   // 参数图 - 普通商品

                // imageTemplate
                RuleExpression imageTemplate = new RuleExpression();
                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/likingtmall_790X200?$pc790X200$&$wenzi=%s";
                imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                // 参数imageParams
                List<RuleExpression> imageParams = new ArrayList<>();

                {
                    // 第一个参数是描述
                    RuleExpression ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(new MasterClrHtmlWord("longDesEn")); // 英文长描述
                    ruleExpression.addRuleWord(new MasterClrHtmlWord("usageEn")); // 英文使用方法
                    imageParams.add(ruleExpression);
                }

                CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                do处理天猫同购无线端20张图片(4, ruleRoot, new CustomWord(word));

            }

            if (blnImport) {   // 参数图 - 重点商品

                {
                    // imageTemplate
                    RuleExpression imageTemplate = new RuleExpression();
                    String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/likingtmall_790X237?$pc790X237$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s&$6=%s&$7=%s";
                    imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                    // 参数imageParams
                    List<RuleExpression> imageParams = new ArrayList<>();

                    {
                        // 共7个属性
                        for (int index = 0; index < 7; index++) {
                            RuleExpression ruleExpression = new RuleExpression();
                            ruleExpression.addRuleWord(new FeedCnWord(true, index));
                            ruleExpression.addRuleWord(new TextWord("   "));
                            ruleExpression.addRuleWord(new FeedCnWord(false, index));
                            imageParams.add(ruleExpression);
                        }
                    }

                    CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                    do处理天猫同购无线端20张图片(4, ruleRoot, new CustomWord(word));
                }

            }

            {
                // 尺码表 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("2"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(5, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            {
                // 测量方式图 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("7"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(6, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            for (int i = 7; i < 12; i++) {
                // 前八张自定义图
                int j = i - 6;
                do处理天猫同购无线端20张图片(i, ruleRoot, new DictWord("无线自定义图片-" + j)); // 原图，参照target
            }

            for (int i = 12; i < 16; i++) {
                int j = i - 11;
                do处理天猫同购无线端20张图片(i, ruleRoot, new DictWord("无线商品图片-" + j)); // url用详情页790*790的
            }

            {
                // 使用说明图 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("6"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(17, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            {
                // 购物流程图 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(18, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            {
                // 购物流程图 - 1
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("1"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(19, ruleRoot, new CustomWord(getCommonImagesWord));
            }


            // end
            String endStr = "],\"enable\": \"true\", \"order\": \"3\"}";
            TextWord endWord = new TextWord(endStr);
            ruleRoot.addRuleWord(endWord);

        }

        {
            // end
            String kv = "}";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        return ruleRoot;
    }

}
