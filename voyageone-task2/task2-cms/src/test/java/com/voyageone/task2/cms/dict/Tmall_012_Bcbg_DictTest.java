package com.voyageone.task2.cms.dict;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
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
 * Created by zhujiaye on 16/5/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_012_Bcbg_DictTest extends BaseDictTest{

    @Autowired
    private SxProductService sxProductService;

    String C_TEXT_BR = "<br />";
    String C_TEMPLATE_IMG = "<img src=%s>";

    String C_商品图片 = "PRODUCT_IMAGE";
    String C_包装图片 = "PACKAGE_IMAGE";
    String C_带角度图片 = "ANGLE_IMAGE";
    String C_自定义图片 = "CUSTOM_IMAGE";

    @Test
    public void startupTest() {

//		doCreateJson("详情页描述", false, doDict_详情页描述());
        doCreateJson("无线描述", false, doDict_无线描述());

    }

    /**
     * 测试解析字典
     */
    @Test
    public void dictTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("012", 977667L);
        sxData.setCartId(23);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = Shops.getShop("012", 23);
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());

        try {
//            String result = sxProductService.resolveDict("详情页描述", expressionParser, shopProp, getTaskName(getClass()), null);
            String result = sxProductService.resolveDict("无线描述", expressionParser, shopProp, getTaskName(getClass()), null);
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private RuleExpression doDict_详情页描述() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        {
            // div-000-start
            String html = "<div style=\"margin-left: auto;margin-right: auto;width: 790px;\">";
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
        }

        {
            {
                {
                    // header
                    String html = "<img src=\"https://img.alicdn.com/imgextra/i1/2694857307/TB2DbDBsFXXXXcbXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }

                {
                    // 尺码图
                    RuleExpression htmlTemplate = new RuleExpression();
                    htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

                    RuleExpression imageType = new RuleExpression();
                    imageType.addRuleWord(new TextWord("2"));

                    RuleExpression viewType = new RuleExpression();
                    viewType.addRuleWord(new TextWord("1"));

                    RuleExpression useOriUrl = null;

                    CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }

                {
                    String html = "<img src=\"https://img.alicdn.com/imgextra/i3/2694857307/TB2kCSUgVXXXXcHXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                    TextWord word = new TextWord(html);
                    ruleRoot.addRuleWord(word);

                    html = "<img src=\"https://img.alicdn.com/imgextra/i2/2694857307/TB2a3GBgVXXXXXsXpXXXXXXXXXX_!!2694857307.jpg\"/>";
                    word = new TextWord(html);
                    ruleRoot.addRuleWord(word);
                }
            }
            {
                // 英文长描述
                String html = "<div style=\"border-top:none; border-bottom:none; border-left:1px solid #d8d8d8; border-right:1px solid #d8d8d8; padding-left: 5px; padding-right: 5px;margin-bottom:-5px;\">";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);

                String column = "longDesEn";
                CommonWord commonWord = new CommonWord(column);
                ruleRoot.addRuleWord(commonWord);

                html = "</div>";
                word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }
            {
                // 中文长描述
                String html = "<div style=\"border-top:none; border-bottom:none; border-left:1px solid #d8d8d8; border-right:1px solid #d8d8d8; padding-left: 5px; padding-right: 5px;margin-bottom:-5px;\">";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);

                String column = "longDesCn";
                CommonWord commonWord = new CommonWord(column);
                ruleRoot.addRuleWord(commonWord);

                html = "</div>";
                word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }

            {
                // 参数图
                {
                    // 前缀
                    String html = "<div><img src=\"";
                    ruleRoot.addRuleWord(new TextWord(html));
                }

                {
                    // useCmsBtImageTemplate
                    RuleExpression useCmsBtImageTemplate = new RuleExpression();
                    useCmsBtImageTemplate.addRuleWord(new TextWord("true"));

                    // 参数imageParams
                    List<RuleExpression> imageParams = new ArrayList<>();
                    {
                        // 第一个参数是product_id(GetMainProductImages)
                        CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
                        RuleExpression imageIndex = new RuleExpression();
                        imageIndex.addRuleWord(new TextWord("0"));
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
                        // 第二个开始，共八个属性（品牌名称,产品类别,适用年龄,使用体重,固定方式,外形尺寸,材质用料,产品重量）
//						for (int index = 0; index < 8; index++) {
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new FeedCnWord(true, index));
//							ruleExpression.addRuleWord(new TextWord("   "));
//							ruleExpression.addRuleWord(new FeedCnWord(false, index));
//							imageParams.add(ruleExpression);
//						}
                        for (int index = 0; index < 4; index++) {
                            // name
                            RuleExpression ruleExpression = new RuleExpression();
                            ruleExpression.addRuleWord(new FeedCnWord(true, index));
                            imageParams.add(ruleExpression);

                            // value
                            ruleExpression = new RuleExpression();
                            ruleExpression.addRuleWord(new FeedCnWord(false, index));
                            imageParams.add(ruleExpression);
                        }
                    }

                    CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(null, imageParams, useCmsBtImageTemplate, null);
                    ruleRoot.addRuleWord(new CustomWord(word));
                }

                {
                    // 后缀
                    String html = "\"></div>";
                    ruleRoot.addRuleWord(new TextWord(html));
                }
            }

            // 以前字典里面就有尺码图，但CMS素材管理的共通图片一览里面没有上传尺码图到天猫平台，所以以前相亲页里面没有显示出来尺码图
//			{
//				// 尺码图 getCommonImages
//				RuleExpression htmlTemplate = new RuleExpression();
//				htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\"/>"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord("2")); // 尺码图
//
//				RuleExpression viewType = new RuleExpression();
//				viewType.addRuleWord(new TextWord("1")); // pc
//
//				RuleExpression useOriUrl = null;
//	//			RuleExpression useOriUrl = new RuleExpression();
//	//			useOriUrl.addRuleWord(new TextWord("1"));
//
//				CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
//				ruleRoot.addRuleWord(new CustomWord(word));
//			}

            {
                // 分割线 ----- detail -----
                String html = "<img src=\"https://img.alicdn.com/imgextra/i2/2694857307/TB2VcOHgVXXXXcmXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }

            {
                // 商品详情图getAllImages (FieldImageType.PRODUCT_IMAGE, useCmsBtImageTemplate = true)
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\"/>"));

                RuleExpression imageTemplate = null;
                //			RuleExpression imageTemplate = new RuleExpression();
                //			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x?$bbbbbbbb790x500bbbbbbbb$&$product=%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_商品图片));

                RuleExpression useOriUrl = null;
                //			RuleExpression useOriUrl = new RuleExpression();
                //			useOriUrl.addRuleWord(new TextWord("1"));

                // useCmsBtImageTemplate
                RuleExpression useCmsBtImageTemplate = new RuleExpression();
                useCmsBtImageTemplate.addRuleWord(new TextWord("true"));

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, useCmsBtImageTemplate, null, null, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                // 商品自定义图getAllImages (FieldImageType.CUSTOM_IMAGE, useOriUrlStr = 1 使用原图)
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\"/>"));

                RuleExpression imageTemplate = null;
                //			RuleExpression imageTemplate = new RuleExpression();
                //			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x790_500x?$bbbbbbbb790x500bbbbbbbb$&$product=%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_自定义图片));

                RuleExpression useOriUrl = new RuleExpression();
                useOriUrl.addRuleWord(new TextWord("1"));

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null,null,null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                // end
                String html = "<img src=\"https://img.alicdn.com/imgextra/i2/2694857307/TB2dRqWgVXXXXaXXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);

                html = "<a href=\"https://bcbgmaxazria.tmall.hk/p/rd397317.htm?spm=a1z10.1-b.w10842440-12684248065.9.IvnNih\">";
                word = new TextWord(html);
                ruleRoot.addRuleWord(word);

                html = "<img src=\"https://img.alicdn.com/imgextra/i4/2694857307/TB2ZtJfhXXXXXcmXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                word = new TextWord(html);
                ruleRoot.addRuleWord(word);

                html = "</a>";
                word = new TextWord(html);
                ruleRoot.addRuleWord(word);

                html = "<img src=\"https://img.alicdn.com/imgextra/i2/2694857307/TB2gdFdhXXXXXcLXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                word = new TextWord(html);
                ruleRoot.addRuleWord(word);

                html = "<img src=\"https://img.alicdn.com/imgextra/i3/2694857307/TB261bEsFXXXXbKXXXXXXXXXXXX_!!2694857307.jpg\"/>";
                word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }
        }

        {
            // div-000-end
            String html = "</div>";
            TextWord word = new TextWord(html);
            ruleRoot.addRuleWord(word);
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

//        {
//            // K-V 模板
//            String kv = "\"k1\":{\"k1-1\":\"v1\",\"k1-2\":\"v2\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//
//            <field id="xxx_enable" name="是否启用" type="singleCheck">
//                <options>
//                <option displayName="启用" value="true"/>
//                <option displayName="不启用" value="false"/>
//                </options>
//            </field>
//        }

        {
            // item_info 商品信息
            String kv = "\"item_info\":{\"item_info_enable\":\"true\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // coupon 优惠
//            String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"342115\"},";
            String kv = "\"coupon\":{\"coupon_enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // hot_recommanded 同店推荐
//            <field id="hot_recommanded_id" name="选择模板" type="singleCheck">
//                <options>
//                <option displayName="商品推荐" value="520277"/>
//                </options>
//            </field>
            String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"314371\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // shop_discount 店铺活动
            String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"385594\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

//        {
//            // user_define 自定义
//            String kv = "\"user_define\":{\"user_define_enable\":\"true\",\"user_define_name\":\"xxx\",\"user_define_image_0\":\"xxx\",\"user_define_image_1\":\"xxx\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }

        // 图片全是DictWord(总共20张图片,但最后5张不用设置图片)
        {
            {
                // item_picture 商品图片
                String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
                TextWord word = new TextWord(kv);
                ruleRoot.addRuleWord(word);
            }

            {
                // 第1张, 固定图片:价格解释
                String strImgJiage = "https://img.alicdn.com/imgextra/i1/2694857307/TB2X2ELbX6AQeBjSZFFXXaiFpXa_!!2694857307.jpg";
                do处理无线端20张图片(0, ruleRoot, new TextWord(strImgJiage));
            }

            {
                // 第2张, 分割线 ----- 商品展示 -----   URL不明
                String strImage = "https://img.alicdn.com/imgextra/i3/2694857307/TB2E7pYXKvB11BjSspnXXbE.pXa_!!2694857307.jpg";
                do处理无线端20张图片(1, ruleRoot, new TextWord(strImage));
            }

            {
                // 第3张, 参数图片:商品展示图片
                // 利用素材管理画面登录的模板（要先在CMS>素材管理>商品图片模板一览画面追加一条"参数模版 or 商品图片模板"）
                RuleExpression useCmsBtImageTemplate = new RuleExpression();
                useCmsBtImageTemplate.addRuleWord(new TextWord("true"));

                // 参数imageParams
                List<RuleExpression> imageParams = new ArrayList<>();
                {
                    // 第一个参数是product_id(GetMainProductImages)
                    CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
                    // 只取得商品图片的第一张图片
                    RuleExpression imageIndex = new RuleExpression();
                    imageIndex.addRuleWord(new TextWord("0"));

                    userParam.setImageIndex(imageIndex);
                    // 商品图片
                    RuleExpression img_imageType = new RuleExpression();
                    img_imageType.addRuleWord(new TextWord(C_商品图片));
                    userParam.setImageType(img_imageType);

                    CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
                    wordValueGetMainProductImages.setUserParam(userParam);

                    RuleExpression imgWord = new RuleExpression();
                    imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
                    imageParams.add(imgWord);
                }

                // viewType
                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));  // viewType 1:PC端 2：APP端

                // 从利用素材管理画面登录的模板（要先在CMS>素材管理>商品图片模板一览画面追加一条"参数模版 or 商品图片模板"）
                // 使用图片模板cms_bt_image_template表取出的url
                CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(null, imageParams, useCmsBtImageTemplate, viewType);
//                ruleRoot.addRuleWord(new CustomWord(word));
                do处理无线端20张图片(2, ruleRoot, new CustomWord(word));

//                RuleExpression htmlTemplate = new RuleExpression();
//                htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\"/>"));
//
//                // 只取得产品的第一张图片
//                CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
//                RuleExpression imageIndex = new RuleExpression();
//                imageIndex.addRuleWord(new TextWord("0"));
//                userParam.setImageIndex(imageIndex);
//
//                RuleExpression imageType = new RuleExpression();
//                imageType.addRuleWord(new TextWord(C_商品图片));
//
//                // 是否使用原图
//                RuleExpression useOriUrl = null;
//                // RuleExpression useOriUrl = new RuleExpression();
//                // useOriUrl.addRuleWord(new TextWord("1"));
//
//                // 以前是在这里固定写死templete，现在是利用CMS素材管理画面里面登录模板
//                RuleExpression imageTemplate = null;
////                RuleExpression imageTemplate = new RuleExpression();
////                imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/BCBG%5F1242X566shoes?$bcbg%5F1242x566$&$text01=wertwert&$images=%s"));
//
//                // 利用素材管理画面登录的模板（要先在CMS>素材管理>商品图片模板一览画面追加一条"参数模版 or 商品图片模板"）
//                RuleExpression useCmsBtImageTemplate = new RuleExpression();
//                useCmsBtImageTemplate.addRuleWord(new TextWord("true"));
//
//                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, useCmsBtImageTemplate);


//                CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(null, imageParams, useCmsBtImageTemplate);

//                do处理无线端20张图片(1, ruleRoot, new CustomWord(word));
            }

            {
                // 第4张, 分割线 ----- 商品介绍 -----
                String strImage = "https://img.alicdn.com/imgextra/i1/2694857307/TB24xdWXPzB11BjSspaXXcJ0VXa_!!2694857307.jpg";
                do处理无线端20张图片(3, ruleRoot, new TextWord(strImage));
            }

            {
                // 第5,6张, 英/中文文字描述图片
                // 文字描述(空两个位置)  把去掉HTML代码之后的文字转换为图片处理
                {
                    RuleExpression ruleExpressionField = new RuleExpression();
                    ruleExpressionField.addRuleWord(new TextWord("longDesEn"));
                    RuleExpression ruleExpressionFontSize = new RuleExpression();
                    ruleExpressionFontSize.addRuleWord(new TextWord("23"));
                    RuleExpression ruleExpressionOneLineBit = new RuleExpression();
                    ruleExpressionOneLineBit.addRuleWord(new TextWord("63"));
                    CustomWordValueGetDescImage img = new CustomWordValueGetDescImage(ruleExpressionField, null, null, null, null, ruleExpressionFontSize, ruleExpressionOneLineBit);
                    do处理无线端20张图片(4, ruleRoot, new CustomWord(img));
                }
                {
                    RuleExpression ruleExpressionField = new RuleExpression();
                    ruleExpressionField.addRuleWord(new TextWord("longDesCn"));
                    RuleExpression ruleExpressionFontSize = new RuleExpression();
                    ruleExpressionFontSize.addRuleWord(new TextWord("23"));
                    RuleExpression ruleExpressionOneLineBit = new RuleExpression();
                    ruleExpressionOneLineBit.addRuleWord(new TextWord("60"));
                    CustomWordValueGetDescImage img = new CustomWordValueGetDescImage(ruleExpressionField, null, null, null, null, ruleExpressionFontSize, ruleExpressionOneLineBit);
                    do处理无线端20张图片(5, ruleRoot, new CustomWord(img));
                }
            }

            {
                // 参数图(一张)
                // 测试临时
//				String strImgJiage = "https://img.alicdn.com/imgextra/i3/2641101981/TB24I4SXF6AQeBjSZFFXXaiFpXa_!!2641101981.jpg";
//				do处理无线端20张图片(3, ruleRoot, new TextWord(strImgJiage));
            }

            {
                // 第7~11张, 无线商品图片(五张)
                do处理无线端20张图片(6, ruleRoot, new DictWord("无线商品图片-1"));
                do处理无线端20张图片(7, ruleRoot, new DictWord("无线商品图片-2"));
                do处理无线端20张图片(8, ruleRoot, new DictWord("无线商品图片-3"));
                do处理无线端20张图片(9, ruleRoot, new DictWord("无线商品图片-4"));
                do处理无线端20张图片(10, ruleRoot, new DictWord("无线商品图片-5"));
            }

            {
                // 第12张, 尺码图(一张)
                RuleExpression htmlTemplate = new RuleExpression();
//                htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));
                htmlTemplate.addRuleWord(new TextWord(""));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("2"));        // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));         // viewType 1:PC端 2：APP端

                RuleExpression useOriUrl = null;

                CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
//                ruleRoot.addRuleWord(new CustomWord(word));

                do处理无线端20张图片(11, ruleRoot, new CustomWord(word));
            }

            {
                // 第13张, 分割线 ----- 退换货服务等 -----
                String strImage = "https://img.alicdn.com/imgextra/i2/2694857307/TB2y97pXVPcZ1BjSZFlXXb3PVXa_!!2694857307.jpg";
                do处理无线端20张图片(12, ruleRoot, new TextWord(strImage));
            }

            {
                // 第14张, 分割线 ----- 购物须知等 -----
                String strImage = "https://img.alicdn.com/imgextra/i3/2694857307/TB2KRzYXIrC11Bjy1zjXXcduVXa_!!2694857307.jpg";
                do处理无线端20张图片(13, ruleRoot, new TextWord(strImage));
            }

            {
                // 第15张, 分割线 ----- 品牌信息等 -----
                String strImage = "https://img.alicdn.com/imgextra/i4/2694857307/TB2sD6WXKzz11Bjy1XdXXbfqVXa_!!2694857307.jpg";
                do处理无线端20张图片(14, ruleRoot, new TextWord(strImage));
            }

//            {
//                // 第13~15张, 无线自定义图(四张)
////                do处理无线端20张图片(12, ruleRoot, new DictWord("无线自定义图片-1"));
//                do处理无线端20张图片(13, ruleRoot, new DictWord("无线自定义图片-2"));
//                do处理无线端20张图片(14, ruleRoot, new DictWord("无线自定义图片-3"));
////                do处理无线端20张图片(14, ruleRoot, new DictWord("无线自定义图片-4"));
////                do处理无线端20张图片(15, ruleRoot, new DictWord("无线自定义图片-5"));
//            }
//            {
//                // 第16~20张, 无线固定图(张)(包含客户服务，购物须知，品牌信息)
//                do处理无线端20张图片(15, ruleRoot, new DictWord("无线固定图-1"));
//                do处理无线端20张图片(16, ruleRoot, new DictWord("无线固定图-2"));
//                do处理无线端20张图片(17, ruleRoot, new DictWord("无线固定图-3"));
//                do处理无线端20张图片(18, ruleRoot, new DictWord("无线固定图-4"));
//                do处理无线端20张图片(19, ruleRoot, new DictWord("无线固定图-5"));
//            }

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