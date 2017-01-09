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

//        doCreateJson("无线描述", false, doDict_无线描述());
//        doCreateJson("无线描述-重点商品", false, doDict_无线描述_重点商品());
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
                String html = "<div><img src=\"";
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
                    // 第一个参数是英文长描述
                    MasterClrHtmlWord word = new MasterClrHtmlWord("longDesEn");

                    RuleExpression ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(word);
                    imageParams.add(ruleExpression);
                }

                CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                // 后缀
                String html = "\"></div>";
                ruleRoot.addRuleWord(new TextWord(html));
            }
        }

        if (blnImport) {   // 参数图 - 重点商品
            {
                // 前缀
                String html = "<div><img src=\"";
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
                String html = "\"></div>";
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
            String kv = "\"item_info\":{\"item_info_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // coupon 优惠
            String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"1300999\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // hot_recommanded 同店推荐
            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"1300981\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // shop_discount 店铺活动
            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"1301043\"},";
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

            for (int i = 0; i < 8; i++) {
                // 前八张自定义图
                do处理无线端20张图片(i, ruleRoot, new DictWord("无线自定义图片-" + (i + 1))); // 原图，参照target
            }

            {
                // 第9张, 参数图片
                RuleExpression imageTemplate = new RuleExpression();
                String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/shoujicanshu?$750x750$&$layer_2_src=%s&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s&$6=%s&$7=%s&$8=%s&$9=%s&$10=%s";
                imageTemplate.addRuleWord(new TextWord(htmlTemplate));

                // 设置参数imageParams的值
                List<RuleExpression> imageParams = new ArrayList<>();
                {
                    // 第一个参数是product_id(GetMainProductImages)
                    CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("0"));   // 第一张商品图片
                    userParam.setImageIndex(imageIndex);
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
                    // 第二个开始，共十个属性
                    for (int index = 0; index < 10; index++) {
                        RuleExpression ruleExpression = new RuleExpression();
                        ruleExpression.addRuleWord(new FeedCnWord(true, index));
                        ruleExpression.addRuleWord(new TextWord("   "));
                        ruleExpression.addRuleWord(new FeedCnWord(false, index));
                        imageParams.add(ruleExpression);
                    }
                }

                CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                do处理无线端20张图片(8, ruleRoot, new CustomWord(imagesWithParamWord));
            }

            for (int i = 9; i < 17; i++) {
                // 10~17
                int j = i - 8;
                do处理无线端20张图片(i, ruleRoot, new DictWord("无线商品图片-" + j)); // url用详情页790*790的
            }

            {
                // 第18张 品牌故事图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("0"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理无线端20张图片(17, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            // 19~20 共通图片-物流图  (2张无线图片)
            for (int i = 18; i < 20; i++) {
                int j = i - 18;
                // 物流图
                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord(String.valueOf(j)));   // 图片index(0,1)

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
                do处理无线端20张图片(i, ruleRoot, new CustomWord(getCommonImagesWord));
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

    private RuleExpression doDict_无线描述_重点商品() {
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
            String kv = "\"item_info\":{\"item_info_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // coupon 优惠
            String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"1300999\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // hot_recommanded 同店推荐
            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"1300981\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }
        {
            // shop_discount 店铺活动
            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"1301043\"},";
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

            for (int i = 0; i < 20; i++) {
                // 20张自定义图
                do处理无线端20张图片(i, ruleRoot, new DictWord("无线自定义图片-" + (i + 1))); // 原图，参照target
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
