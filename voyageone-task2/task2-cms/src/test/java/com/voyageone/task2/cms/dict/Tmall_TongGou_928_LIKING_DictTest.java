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
 * Created by dell on 2016/12/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_TongGou_928_LIKING_DictTest extends BaseDictTest{
    @Autowired
    private SxProductService sxProductService;


    @Test
    public void startupTest() {
//        doCreateJson("天猫同购描述", false, doDict_天猫同购描述(1));
//        doCreateJson("天猫同购描述-重点商品", false, doDict_天猫同购描述(2));
//        doCreateJson("天猫同购描述-无属性图", false, doDict_天猫同购描述(3));
//        doCreateJson("天猫同购描述-非重点之英文长描述", false, doDict_天猫同购描述(4));
//        doCreateJson("天猫同购描述-非重点之中文长描述", false, doDict_天猫同购描述(5));
//        doCreateJson("天猫同购描述-非重点之中文使用说明", false, doDict_天猫同购描述(6));
//        doCreateJson("天猫同购描述-爆款商品", false, doDict_天猫同购描述(7));

//        doCreateJson("天猫同购无线描述", false, doDict_天猫同购无线描述(1));
//        doCreateJson("天猫同购无线描述-重点商品", false, doDict_天猫同购无线描述(2));
//        doCreateJson("天猫同购无线描述-无属性图", false, doDict_天猫同购无线描述(3));
//        doCreateJson("天猫同购无线描述-非重点之英文长描述", false, doDict_天猫同购无线描述(4));
//        doCreateJson("天猫同购无线描述-非重点之中文长描述", false, doDict_天猫同购无线描述(5));
//        doCreateJson("天猫同购无线描述-非重点之中文使用说明", false, doDict_天猫同购无线描述(6));
//        doCreateJson("天猫同购无线描述-爆款商品", false, doDict_天猫同购无线描述(7));

        doCreateJson("天猫同购描述-重点",false, doDict_天猫同购描述(2));
        doCreateJson("天猫同购描述-非重点", false, doDict_天猫同购描述(4));
        doCreateJson("天猫同购描述-爆款", false, doDict_天猫同购描述(7));

        doCreateJson("天猫同购无线描述-重点", false, doDict_天猫同购无线描述(2));
        doCreateJson("天猫同购无线描述-非重点", false, doDict_天猫同购无线描述(4));
        doCreateJson("天猫同购无线描述-爆款", false, doDict_天猫同购无线描述(7));
        doCreateJson("天猫同购无线描述-全自定义", false, doDict_天猫同购无线描述(1));

        doCreateJson("天猫同购描述", false, doDict_天猫同购描述()); // 默认
        doCreateJson("天猫同购无线描述", false, doDict_天猫同购无线描述());// 默认

    }

    @Test
    public void dictTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("928", 3208115L);
        sxData.setCartId(31);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = new ShopBean();
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());

        try {
            System.out.println("=====================================");
            System.out.println("字典: 天猫同购无线描述");
            String result = sxProductService.resolveDict("天猫同购无线描述", expressionParser, shopProp, "天猫同购无线描述测试charis", null);
            result = "<div style=\"width:790px; margin: 0 auto;\">" + result + "</div>";
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getTaskName() {
        return getClass().getName();
    }

    private RuleExpression doDict_天猫同购描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

//        {
//            // 固定图片 - AMEX项目介绍
//            ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i3/3081276392/TB2t9l8XsfhFuJjSZFDXXXJfpXa_!!3081276392.jpg")));
//        }
        {
            // 店铺介绍图 - 0
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("<a href=\"http://liking.tmall.com/p/rd137248.htm\" target=\"_blank\">" + C_TEMPLATE_IMG_790 + "</a>"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("5"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, imageIndex);
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

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }
        {
            // 固定图片 - 商品展示
            ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i3/3081276392/TB200AeXaPeFuJjy0FlXXbdcpXa_!!3081276392.jpg")));
        }
        {
            // 商品图片
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageTemplate = new RuleExpression();
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/liking-tmall-product-img790x740?$790X740$&$product-img=%s"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_商品图片));

            RuleExpression useOriUrl = null;

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }
        {
            // 固定图片 - 商品信息
            ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i2/3081276392/TB2TaMcXnzfFuJjSsphXXc_xpXa_!!3081276392.jpg")));
        }
        {
            String tableTem = "<div style=\"margin-top: -3px; border-width: 0 15px 0 15px; padding: 0 20px 20px 20px; width: 720px; \">" +
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
            // 尺码图 - 0
            // 固定图片 - 商品尺码
            String sizeChartImage = String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i2/3081276392/TB2T5t6XyYiFuJjSZFkXXaQ_XXa_!!3081276392.jpg");

            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(sizeChartImage + C_TEMPLATE_IMG_790));

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
        // 尺码图 - 1 && 尺码图 - 2
        {
            for (int i = 1; i < 3; i++) {

                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("2"));

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));

                RuleExpression useOriUrl = null;

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord(String.valueOf(i)));

                CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(word));
            }
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
            // 物流介绍图
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(getCommonImagesWord));
        }
        return ruleRoot;
    }

    private RuleExpression doDict_天猫同购无线描述() {
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
        {
            // item_picture 商品图片 （每张图片url是放在content里面的）
            {
                String kv = "\"item_picture\": {\"content\": [";
                TextWord word = new TextWord(kv);
                ruleRoot.addRuleWord(word);
            }
            {
                // 固定图片 - AMEX项目介绍
//                do处理天猫同购无线端20张图片(0, ruleRoot, new TextWord("https://img.alicdn.com/imgextra/i3/3081276392/TB2t9l8XsfhFuJjSZFDXXXJfpXa_!!3081276392.jpg"));

                // 店铺介绍图 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(0, ruleRoot, new CustomWord(getCommonImagesWord));
            }
            {
                // 固定图片 - 商品展示
                do处理天猫同购无线端20张图片(1, ruleRoot, new TextWord("https://img.alicdn.com/imgextra/i3/3081276392/TB200AeXaPeFuJjy0FlXXbdcpXa_!!3081276392.jpg"));
            }
            {
                // 主商品
                for (int i = 0; i < 5; i++){
                    // 主商品的5张图
                    CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord(String.valueOf(i)));
                    userParam.setImageIndex(imageIndex);
                    RuleExpression img_imageType = new RuleExpression();
                    img_imageType.addRuleWord(new TextWord(C_商品图片));
                    userParam.setImageType(img_imageType);

                    RuleExpression imageTemplate = new RuleExpression();
                    imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/liking-tmall-product-img790x740?$790X740$&$product-img=%s"));
                    userParam.setImageTemplate(imageTemplate);

                    CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
                    wordValueGetMainProductImages.setUserParam(userParam);

                    do处理天猫同购无线端20张图片(2 + i, ruleRoot, new CustomWord(wordValueGetMainProductImages));
                }
            }
            for (int i = 0; i < 4; i++) {
				// 第7+i张, 非主商品的第一张图
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/liking-tmall-product-img790x740?$790X740$&$product-img=%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord(C_商品图片));

				RuleExpression codeIndex = new RuleExpression();
				codeIndex.addRuleWord(new TextWord(String.valueOf(i))); // 第i个非主商品

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0")); // 第一张图

				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, null);
                do处理天猫同购无线端20张图片(7 + i, ruleRoot, new CustomWord(allImagesWord));
			}
            {
                // 商品信息logo
                do处理天猫同购无线端20张图片(11, ruleRoot, new TextWord("https://img.alicdn.com/imgextra/i2/3081276392/TB2TaMcXnzfFuJjSsphXXc_xpXa_!!3081276392.jpg"));
            }
            {
                // 商品参数图
                RuleExpression imageTemplate = new RuleExpression();
                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/liking790X280-product-info?$liking_790*280$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s&$6=%s&$7=%s&$8=%s&$9=%s&$10=%s&$11=%s&$12=%s&$13=%s&$14=%s&$15=%s&$16=%s";
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
                do处理天猫同购无线端20张图片(12, ruleRoot, new CustomWord(word));
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
                do处理天猫同购无线端20张图片(13, ruleRoot, new CustomWord(getCommonImagesWord));
            }
            {
                // 尺码图 - 0
                // 固定图片 - 商品尺码
                String sizeChartImage = String.format(C_TEMPLATE_IMG_SIZECHART, "https://img.alicdn.com/imgextra/i2/3081276392/TB2T5t6XyYiFuJjSZFkXXaQ_XXa_!!3081276392.jpg");

                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord(sizeChartImage + C_TEMPLATE_IMG_SIZECHART));

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
            // 尺码图 - 1 && 尺码图 - 2
            {
                for (int i = 1; i < 2; i++) {


                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("2"));

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("1"));

                    RuleExpression useOriUrl = null;

                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord(String.valueOf(i)));

                    CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(null, imageType, viewType, useOriUrl, imageIndex);
                    do处理天猫同购无线端20张图片(15 + i, ruleRoot, new CustomWord(word));
                }
            }
            {
                // 测量方式图 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("7"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(17, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            {
                // 使用说明图 - 0
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("6"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理天猫同购无线端20张图片(18, ruleRoot, new CustomWord(getCommonImagesWord));
            }
            {
                // 物流介绍图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

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

    private RuleExpression doDict_天猫同购描述(int propType) {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // 店铺介绍图 - 0
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
            // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
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
            ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i1/3081276392/TB2nbvndHlmpuFjSZFlXXbdQXXa_!!3081276392.jpg")));
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
//            {
//                // 前缀
//                String html = "<img width=790px src=\"";
//                ruleRoot.addRuleWord(new TextWord(html));
//            }
//
//            {
//                // imageTemplate
//                RuleExpression imageTemplate = new RuleExpression();
//                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/liking-18-790X260?$790X300$&$wenzi=%s";
//                imageTemplate.addRuleWord(new TextWord(htmlTemplate));
//
//                // 参数imageParams
//                List<RuleExpression> imageParams = new ArrayList<>();
//
//                {
//                    // 第一个参数是描述
//                    RuleExpression ruleExpression = new RuleExpression();
//                    switch (propType) {
//                        case 1:
//                            ruleExpression.addRuleWord(new MasterClrHtmlWord("usageEn")); // 英文使用方法
//                            break;
//                        case 4:
//                            ruleExpression.addRuleWord(new MasterClrHtmlWord("longDesEn")); // 英文长描述
//                            break;
//                        case 5:
//                            ruleExpression.addRuleWord(new MasterClrHtmlWord("longDesCn")); // 中文长描述
//                            break;
//                        case 6:
//                            ruleExpression.addRuleWord(new MasterClrHtmlWord("usageCn")); // 非重点之中文使用说明
//                            break;
//                    }
//                    imageParams.add(ruleExpression);
//                }
//
//                CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
//                ruleRoot.addRuleWord(new CustomWord(word));
//            }
//
//            {
//                // 后缀
//                String html = "\">";
//                ruleRoot.addRuleWord(new TextWord(html));
//            }
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


//            {
//                // 前缀
//                String html = "<img width=790px src=\"";
//                ruleRoot.addRuleWord(new TextWord(html));
//            }
//
//            {
//                // imageTemplate
//                RuleExpression imageTemplate = new RuleExpression();
//                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/liking790X373xinxi?$790X373$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s&$6=%s&$7=%s&$8=%s&$9=%s&$10=%s&$11=%s&$12=%s&$13=%s&$14=%s&$15=%s&$16=%s";
//                imageTemplate.addRuleWord(new TextWord(htmlTemplate));
//
//                // 参数imageParams
//                List<RuleExpression> imageParams = new ArrayList<>();
//
//                {
//                    // 共7个属性
//                    for (int index = 0; index < 16; index++) {
//                        RuleExpression ruleExpression = new RuleExpression();
//                        ruleExpression.addRuleWord(new FeedCnWord(true, index));
//                        ruleExpression.addRuleWord(new TextWord("   "));
//                        ruleExpression.addRuleWord(new FeedCnWord(false, index));
//                        imageParams.add(ruleExpression);
//                    }
//                }
//
//                CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
//                ruleRoot.addRuleWord(new CustomWord(word));
//            }
//
//            {
//                // 后缀
//                String html = "\">";
//                ruleRoot.addRuleWord(new TextWord(html));
//            }
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
                ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i3/3081276392/TB2MVjBc9tkpuFjy0FhXXXQzFXa_!!3081276392.jpg")));
            }
            // 商品图片
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageTemplate = new RuleExpression();
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/likingtmall790X740?$790X740$&$image=%s"));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_商品图片));

            RuleExpression useOriUrl = null;

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        {
            for (int i = 0; i < 3; i++) {
                // 尺码图 - 三张
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("2"));

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));

                RuleExpression useOriUrl = null;

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord(String.valueOf(i)));

                CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(word));
            }
        }
//        {
//            // 尺码图 - 1
//            RuleExpression htmlTemplate = new RuleExpression();
//            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
//
//            RuleExpression imageType = new RuleExpression();
//            imageType.addRuleWord(new TextWord("2"));
//
//            RuleExpression viewType = new RuleExpression();
//            viewType.addRuleWord(new TextWord("1"));
//
//            RuleExpression useOriUrl = null;
//
//            RuleExpression imageIndex = new RuleExpression();
//            imageIndex.addRuleWord(new TextWord("1"));
//
//            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
//            ruleRoot.addRuleWord(new CustomWord(word));
//        }
//        {
//            // 尺码图 - 2
//            RuleExpression htmlTemplate = new RuleExpression();
//            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
//
//            RuleExpression imageType = new RuleExpression();
//            imageType.addRuleWord(new TextWord("2"));
//
//            RuleExpression viewType = new RuleExpression();
//            viewType.addRuleWord(new TextWord("1"));
//
//            RuleExpression useOriUrl = null;
//
//            RuleExpression imageIndex = new RuleExpression();
//            imageIndex.addRuleWord(new TextWord("2"));
//
//            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
//            ruleRoot.addRuleWord(new CustomWord(word));
//        }

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

    private RuleExpression doDict_天猫同购无线描述(int propType) {
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

//
            // item_picture 商品图片 （每张图片url是放在content里面的）
            {
                String kv = "\"item_picture\": {\"content\": [";
                TextWord word = new TextWord(kv);
                ruleRoot.addRuleWord(word);
            }

            if (propType == 1) {
                for (int i = 0; i < 20; i++) {
                    // 20张自定义图
                    int j = i + 1;
                    do处理天猫同购无线端20张图片(i, ruleRoot, new DictWord("真的无线自定义图片-" + j)); // 原图，参照target
                }
            } else {

                {
                    // 店铺介绍图 - 0
                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

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
                    viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("1"));

                    CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                    do处理天猫同购无线端20张图片(1, ruleRoot, new CustomWord(getCommonImagesWord));
                }

                {
                    do处理天猫同购无线端20张图片(2, ruleRoot, new TextWord("https://img.alicdn.com/imgextra/i1/3081276392/TB2nbvndHlmpuFjSZFlXXbdQXXa_!!3081276392.jpg"));
                }

                if (propType != 7) { // 爆款商品之外的都需要
                    // 品牌故事图 - 0
                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("0"));

                    CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                    do处理天猫同购无线端20张图片(3, ruleRoot, new CustomWord(getCommonImagesWord));
                }

                if (propType == 4 ) {  // 参数图 - 非重点之英文长描述
                    // imageTemplate
                    RuleExpression imageTemplate = new RuleExpression();
//                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/likingtmall_790X200?$pc790X200$&$wenzi=%s";
                    String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/liking-18-790X260?$790X300$&$wenzi=%s";
                    imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                    // 参数imageParams
                    List<RuleExpression> imageParams = new ArrayList<>();

                    {
                        // 第一个参数是描述
                        RuleExpression ruleExpression = new RuleExpression();

                        ruleExpression.addRuleWord(new MasterClrHtmlWord("longDesEn")); // 英文长描述

                        imageParams.add(ruleExpression);
                    }

                    CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                    do处理天猫同购无线端20张图片(4, ruleRoot, new CustomWord(word));

                }

                if (propType == 2) {   // 参数图 - 重点商品

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
                    do处理天猫同购无线端20张图片(4, ruleRoot, new CustomWord(word));

                }

                if (propType != 7) { // 爆款商品之外的都需要
                    for (int i = 0; i < 3; i++) {
                        // 3张自定义图
                        int j = i + 1;
                        do处理天猫同购无线端20张图片((i + 5), ruleRoot, new DictWord("无线自定义图片-" + j)); // 原图，参照target
                    }

                } else {
                    // 爆款商品的场合
                    for (int i = 0; i < 11; i++) {
                        // 11张自定义图
                        int j = i + 1;
                        do处理天猫同购无线端20张图片((i + 3), ruleRoot, new DictWord("无线自定义图片-" + j)); // 原图，参照target
                    }
                }

                if (propType != 7) { // 爆款商品之外的都需要
                    {
                        do处理天猫同购无线端20张图片(8, ruleRoot, new TextWord("https://img.alicdn.com/imgextra/i3/3081276392/TB2MVjBc9tkpuFjy0FhXXXQzFXa_!!3081276392.jpg"));
                    }

                    for (int i = 0; i < 4; i++) {
                        // 4张商品图
                        int j = i + 1;
                        do处理天猫同购无线端20张图片((i + 9), ruleRoot, new DictWord("无线商品图片-" + j)); // url用详情页790*790的
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
                    do处理天猫同购无线端20张图片(13, ruleRoot, new CustomWord(getCommonImagesWord));
                }

                {
                    // 尺码表 - 1
                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("2"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("1"));

                    CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                    do处理天猫同购无线端20张图片(14, ruleRoot, new CustomWord(getCommonImagesWord));
                }

                {
                    // 尺码表 - 2
                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("2"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("1"));   // viewType 1:PC端 2：APP端

                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("2"));

                    CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                    do处理天猫同购无线端20张图片(15, ruleRoot, new CustomWord(getCommonImagesWord));
                }

                {
                    // 测量方式图 - 0
                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("7"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("0"));

                    CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                    do处理天猫同购无线端20张图片(16, ruleRoot, new CustomWord(getCommonImagesWord));
                }

                {
                    // 使用说明图 - 0
                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("6"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图 6：使用说明图 7：测量方式图

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

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
                    viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

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
                    viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("1"));

                    CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                    do处理天猫同购无线端20张图片(19, ruleRoot, new CustomWord(getCommonImagesWord));
                }
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

//    private RuleExpression doDict_天猫同购描述_bcbg() {
//        // 根字典
//        RuleExpression ruleRoot = new RuleExpression();
//        {
//            // bcbg二级页面
//            TextWord word = new TextWord(String.format(C_TEMPLATE_IMG_BCBG,
//                    "https://h5.m.taobao.com/weapp/view_page.htm?page=shop/activity&userId=2939402618&pageId=74570068",
//                    "https://img.alicdn.com/imgextra/i3/2939402618/TB2xqM1kwJkpuFjSszcXXXfsFXa-2939402618.jpg"));
//            ruleRoot.addRuleWord(word);
//        }
//
//        // 自定义图
//        {
//            RuleExpression htmlTemplate = new RuleExpression();
//            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
//
//            RuleExpression imageTemplate = null;
//
//            RuleExpression imageType = new RuleExpression();
//            imageType.addRuleWord(new TextWord(C_自定义图片));
//
//            RuleExpression useOriUrl = new RuleExpression();
//            useOriUrl.addRuleWord(new TextWord("1")); // 使用原图
//
//            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
//            ruleRoot.addRuleWord(new CustomWord(word));
//        }
//
//        return ruleRoot;
//    }
}
