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

/**
 * 天猫同购平台商品5张主图JSON生成工具
 *  (017)LuckyVitamin天猫同购店
 *
 * @author desmond on 2016/8/23.
 * @version 2.5.0
 * @since 2.5.0
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_TongGou_017_LuckyVitamin_DictTest extends BaseDictTest {
    @Autowired
    private SxProductService sxProductService;

    String C_TEXT_BR = "<br />";
    String C_TEMPLATE_IMG = "<img src=%s>";

    // 字典解析时实际取得的是CmsBtProductConstants中定义的FieldImageType枚举类型的值
    // type: 1-商品图片, 2-包装图片, 3-带角度图片, 4-自定义图片,5-移动端自定义图片,6-PC端自拍商品图,7-APP端自拍商品图,8-吊牌图
    String C_商品图片 = "PRODUCT_IMAGE";   // PRODUCT_IMAGE("image1") 商品图片是可以有模板的
    String C_包装图片 = "PACKAGE_IMAGE";   // PACKAGE_IMAGE("image2")
    String C_带角度图片 = "ANGLE_IMAGE";   // ANGLE_IMAGE("image3")
    String C_自定义图片 = "CUSTOM_IMAGE";  // CUSTOM_IMAGE("image4")  自定义图是没有模板的

    // -------------------------------------------------
    // 切换平台时，下列商品5张主图的固定图片URL需要修改
    // -------------------------------------------------

    @Test
    public void startupTest() {

        // 天猫同购商品主图5张
//        doCreateJson("天猫同购商品主图5张", false, doDict_天猫同购商品主图5张());

        // 天猫同购描述(PC端详情页描述)
        doCreateJson("天猫同购描述", false, doDict_天猫同购描述());

        // 天猫同购无线商品图片
        for (int i = 1; i <= 5; i++) {
            int index = i - 1;
            // 商品图片的index(0~4)(只有target店铺是取得1-5)
            doCreateJson("无线商品图片-" + i, false, doDict_无线商品图片(String.valueOf(index)));   // index(0~4)
        }
        // 天猫同购无线描述(APP端无线描述)
        doCreateJson("天猫同购无线描述", false, doDict_天猫同购无线描述());

        // 属性图片模板

        //
//        // 聚美使用方法
//        doCreateJson("聚美使用方法", false, doDict_聚美使用方法());
//
//        // 聚美实拍
//        doCreateJson("聚美实拍", false, doDict_聚美实拍());

    }

    @Test
    public void dictTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("017", 20893L);
        sxData.setCartId(31);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = Shops.getShop("017", 30);
//        shopProp.setCart_id("27");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.JM.getId());

        // 最终测试结果是这样的 ======================================== START
// =====================================
// 字典: 聚美详情
// <div><img src="http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/3b17329e-1af1-4e1f-adc1-e4451cfa1477.jpeg"></div>
// =====================================
// 字典: 聚美使用方法
// <div><img src="http://p12.jmstatic.com/open_api/gPop_131/010/2/20160617165939.jpeg" /></div>
// =====================================
// 字典: 聚美实拍
// <div><img src="http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/5eca6152-21fb-4352-a647-9d122b63c7a1.jpeg" /></div><div><img src="http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/f439cf4b-9791-45fe-9b3d-6d3960261612.jpeg" /></div>
// =====================================
// 字典: 聚美白底方图
// http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/56dbb1c0-ef09-48f6-9530-80d734653155.jpeg,http://p12.jmstatic.com/dev_test/open_api/gPop_110/010/2ed210b5-10da-49b9-a7cf-49b1e89b9fe9.jpeg,
        // 最终测试结果是这样的 ======================================== END

        try {
            String result = "";

            // 天猫同购商品主图5张
            System.out.println("=====================================");
            System.out.println("字典: 天猫同购商品主图5张");
//            result = sxProductService.resolveDict("天猫同购商品主图5张", expressionParser, shopProp, getTaskName(), null);
            System.out.println(result);
            for (String picUrl : result.split(",")) {
//                String jmPicUrl = sxProductService.uploadImageByUrl_JM(picUrl, shopProp);
//                System.out.println(jmPicUrl);
                System.out.println(picUrl);
            }

            // 天猫同购无线描述
            System.out.println("=====================================");
            System.out.println("字典: 天猫同购无线描述");
            result = sxProductService.resolveDict("天猫同购无线描述", expressionParser, shopProp, getTaskName(), null);
            System.out.println(result);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getTaskName() {
        return getClass().getName();
    }


    /**
     * 生成json
     *
     * @param title    字典名字
     * @param isUrl    生成出来的内容是否是url(一般是图片的话就是true, 其他有文字的都是false)
     * @param ruleRoot rule
     */
//    private void doCreateJson(String title, boolean isUrl, RuleExpression ruleRoot) {
//
//        DictWord dictRoot = new DictWord();
//        dictRoot.setName(title);
//        dictRoot.setExpression(ruleRoot);
//        dictRoot.setIsUrl(isUrl);
//
//        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
//        String json = ruleJsonMapper.serializeRuleWord(dictRoot);
//
//        System.out.println("=====================================");
//        System.out.println("字典: " + title);
//        System.out.println(json);
//
//    }



    /**
     * 天猫同购商品主图5张
     * 1. (要上传)(非html)(逗号分隔的商品主图)
     *   模板:http://s7d5.scene7.com/is/image/sneakerhead/BHFO%5F2015%5Fx1000%5F1000x?$jc1000_1000$&$product=%s
     */
    private RuleExpression doDict_天猫同购商品主图5张() {

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        // 生成内容
        for (int i = 0; i < 5; i++) {

            // 第一个参数是product_id(GetMainProductImages)
            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();

            RuleExpression imageTemplate = new RuleExpression();
            // 带logo商品图片（不是产品图片）的源图片模板
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/BHFO%%5F2015%%5Fx1000%%5F1000x?$jc1000_1000$&$product=%s"));
            userParam.setImageTemplate(imageTemplate);
            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord(String.valueOf(i)));
            userParam.setImageIndex(imageIndex);
            RuleExpression img_imageType = new RuleExpression();
            img_imageType.addRuleWord(new TextWord(C_商品图片));
            userParam.setImageType(img_imageType);
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("%s,"));
            userParam.setHtmlTemplate(htmlTemplate);

            CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
            wordValueGetMainProductImages.setUserParam(userParam);

            ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));

        }

        return ruleRoot;

    }

    private RuleExpression doDict_天猫同购描述() {
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
                // div-001-start
                String html = "<div style=\"margin-bottom: 15px;\">";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }

            {
                // 店铺介绍图 - 0
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<div><img width=\"790px\" src=\"%s\" /></div>"));

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
                htmlTemplate.addRuleWord(new TextWord("<div><img width=\"790px\" src=\"%s\" /></div>"));

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
                // 所有自定义图(getAllImages（注意参数里要设置使用原图）)
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<img src=\"%s\" width=790px />"));

                RuleExpression imageTemplate = new RuleExpression();
                imageTemplate.addRuleWord(new TextWord(""));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_自定义图片));

                RuleExpression useOriUrl = new RuleExpression();
                useOriUrl.addRuleWord(new TextWord("1")); // 使用原图

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }
            {
                // 店铺介绍图 - 2
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<div><img width=\"790px\" src=\"%s\" /></div>"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("5"));

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));

                RuleExpression useOriUrl = null;

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("2"));

                CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                // 描述前加一个换行
                String html = "<br /><br />";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }

//            {
//                // 英文长描述(把这段英文传到天猫平台后，平台会自动翻译并设置格式)
//                String column = "longDesEn";
//                CommonWord commonWord = new CommonWord(column);
//                ruleRoot.addRuleWord(commonWord);
//            }
            {
                // 中文长描述
                MasterWord word = new MasterWord("longDesCn");
                ruleRoot.addRuleWord(word);
            }

			{
				// 描述前加一个换行
				String html = "<br /><br />";
				TextWord word = new TextWord(html);
				ruleRoot.addRuleWord(word);
			}

//			{
//				// 英文使用说明(把这段英文传到天猫平台后，平台会自动翻译并设置格式)
//				// 备注： LuckyVitamin的使用说明是在材质上的
//				String column = "materialEn";
//				CommonWord commonWord = new CommonWord(column);
//				ruleRoot.addRuleWord(commonWord);
//			}
            {
                // 中文使用说明
                // 备注： LuckyVitamin的使用说明是在材质上的
                String column = "materialCn";
                CommonWord commonWord = new CommonWord(column);
                ruleRoot.addRuleWord(commonWord);
            }

            {
                // 描述后加一个换行
                String html = "<br /><br />";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }

            {
                // 中间插入自定义部分_产品实拍分隔文字
                String html = "<img width=\"790px\" src=\"https://img.alicdn.com/imgextra/i1/3031513024/TB2lLA_cEWO.eBjSZPcXXbopVXa_!!3031513024.jpg\"/>";
                TextWord word = new TextWord(html);
                ruleRoot.addRuleWord(word);
            }

            {
                // 商品图片(带模板的产品图片image1)
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<div><img width=\"790px\" src=\"%s\" /></div>"));

                // SxProductService.getImageTemplate()方法的retUrl = String.format(matchModels.get(0).getImageTemplateContent(), imageParam);
                // 报错，如果是java.util.UnknownFormatConversionException: Conversion = 'F'
                // 对应方法：模板url中的%（%s除外）换成%%
                RuleExpression imageTemplate = new RuleExpression();
                imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/VTM%%2DTM%%2DSKU?$img=%s&layer=comp&wid=1242&hei=1000"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord(C_商品图片));   // image1

                RuleExpression useOriUrl = null;

                CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                // 购物流程图
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("<div><img width=\"790px\" src=\"%s\" /></div>"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("4"));

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("1"));

                RuleExpression useOriUrl = null;

                CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
                ruleRoot.addRuleWord(new CustomWord(word));
            }

            {
                // div-001-end
                String html = "</div>";
                TextWord word = new TextWord(html);
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

    // 天猫同购专用无线描述，跟一般天猫无线描述不一样，根据【商家接入标准文档-V6.pdf】文件作成
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

//        {
//            // shop_discount 店铺活动
////            String kv = "\"shop_discount\":{\"enable\":\"true\",\"shop_discount_id\":\"342160\"},";
//            String kv = "\"shop_discount\":{\"order\": \"3\",\"enable\":\"false\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }

        // 图片全是DictWord(总共20张图片,但最后5张不用设置图片)
        {
            // item_picture 商品图片 （每张图片url是放在content里面的）
            {
                String kv = "\"item_picture\": {\"content\": [";
                TextWord word = new TextWord(kv);
                ruleRoot.addRuleWord(word);
            }

            {
//                // 第1张, 顶部固定图_品牌信息
//                String strImgJiage = "http://img.alicdn.com/imgextra/i3/2640015666/TB2.J7faOGO.eBjSZFpXXb3tFXa_!!2640015666.jpg";
//                do处理天猫同购无线端20张图片(0, ruleRoot, new TextWord(strImgJiage));

				// 店铺图
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord("%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression useOriUrl = null;

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                do处理天猫同购无线端20张图片(0, ruleRoot, new CustomWord(getCommonImagesWord));
			}

            {
//                // 第2张, 顶部固定图_消费者告知书
//                String strImgJiage = "http://img.alicdn.com/imgextra/i4/2640015666/TB2hD3OaseJ.eBjy0FiXXXqapXa_!!2640015666.jpg";
//                do处理天猫同购无线端20张图片(1, ruleRoot, new TextWord(strImgJiage));

				// 店铺图
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord("%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression useOriUrl = null;

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("1"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
				do处理天猫同购无线端20张图片(1, ruleRoot, new CustomWord(getCommonImagesWord));
            }

			{
                // 无线自定义图片
                do处理天猫同购无线端20张图片(2, ruleRoot, new DictWord("无线自定义图片-1"));
                do处理天猫同购无线端20张图片(3, ruleRoot, new DictWord("无线自定义图片-2"));
                do处理天猫同购无线端20张图片(4, ruleRoot, new DictWord("无线自定义图片-3"));
                do处理天猫同购无线端20张图片(5, ruleRoot, new DictWord("无线自定义图片-4"));
                do处理天猫同购无线端20张图片(6, ruleRoot, new DictWord("无线自定义图片-5"));
                do处理天猫同购无线端20张图片(7, ruleRoot, new DictWord("无线自定义图片-6"));
                do处理天猫同购无线端20张图片(8, ruleRoot, new DictWord("无线自定义图片-7"));
                do处理天猫同购无线端20张图片(9, ruleRoot, new DictWord("无线自定义图片-8"));
                do处理天猫同购无线端20张图片(10, ruleRoot, new DictWord("无线自定义图片-9"));
            }

            {
                // 第3张, 顶部固定图_产品实拍分隔文字
//                String strImgJiage = "http://img.alicdn.com/imgextra/i3/2640015666/TB2pRsVar5K.eBjy0FnXXaZzVXa_!!2640015666.jpg";
//                do处理天猫同购无线端20张图片(2, ruleRoot, new TextWord(strImgJiage));

                // 店铺图
                RuleExpression htmlTemplate = new RuleExpression();
                htmlTemplate.addRuleWord(new TextWord("%s"));

                RuleExpression imageType = new RuleExpression();
                imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

                RuleExpression viewType = new RuleExpression();
                viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

                RuleExpression useOriUrl = null;

                RuleExpression imageIndex = new RuleExpression();
                imageIndex.addRuleWord(new TextWord("2"));

                CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
                do处理天猫同购无线端20张图片(11, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            {
                // 第10张, 底部固定图_温馨提示
//                String strImgJiage = "http://img.alicdn.com/imgextra/i2/2640015666/TB2KOwjaJuO.eBjSZFCXXXULFXa_!!2640015666.jpg";
//                do处理天猫同购无线端20张图片(9, ruleRoot, new TextWord(strImgJiage));

                // 生成描述图片
                {
                    RuleExpression ruleExpressionField = new RuleExpression();
                    ruleExpressionField.addRuleWord(new TextWord("shortDesCn"));
                    RuleExpression ruleExpressionFontSize = new RuleExpression();
                    ruleExpressionFontSize.addRuleWord(new TextWord("23"));
                    RuleExpression ruleExpressionOneLineBit = new RuleExpression();
                    ruleExpressionOneLineBit.addRuleWord(new TextWord("60"));
                    RuleExpression ruleExpressionSectionSize = new RuleExpression();
                    ruleExpressionSectionSize.addRuleWord(new TextWord("5"));
                    CustomWordValueGetDescImage img = new CustomWordValueGetDescImage(ruleExpressionField, null, null, null, ruleExpressionSectionSize, ruleExpressionFontSize, ruleExpressionOneLineBit);
                    do处理天猫同购无线端20张图片(12, ruleRoot, new CustomWord(img));
                }
            }



            {
                // 第4~8张, 无线商品图片(四张)
                do处理天猫同购无线端20张图片(13, ruleRoot, new DictWord("无线商品图片-1"));
                do处理天猫同购无线端20张图片(14, ruleRoot, new DictWord("无线商品图片-2"));
                do处理天猫同购无线端20张图片(15, ruleRoot, new DictWord("无线商品图片-3"));
                do处理天猫同购无线端20张图片(16, ruleRoot, new DictWord("无线商品图片-4"));
                do处理天猫同购无线端20张图片(17, ruleRoot, new DictWord("无线商品图片-5"));
            }

            {
                // 第9张, 底部固定图_购物流程
//                String strImgJiage = "http://img.alicdn.com/imgextra/i2/2640015666/TB2fIsiaNaK.eBjSZFwXXXjsFXa_!!2640015666.jpg";
//                do处理天猫同购无线端20张图片(8, ruleRoot, new TextWord(strImgJiage));

				// 店铺图
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord("%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression useOriUrl = null;

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
				do处理天猫同购无线端20张图片(18, ruleRoot, new CustomWord(getCommonImagesWord));
            }

            {
                // 第11张, 底部固定图_售后须知，关于价格
//                String strImgJiage = "http://img.alicdn.com/imgextra/i4/2640015666/TB2It7oaF5N.eBjSZFvXXbvMFXa_!!2640015666.jpg";
//                do处理天猫同购无线端20张图片(10, ruleRoot, new TextWord(strImgJiage));

				// 店铺图
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord("%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression useOriUrl = null;

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("2"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
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

    // 用于一般天猫上新，不能用于同购上新
    private RuleExpression doDict_天猫无线描述() {
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
            // coupon 优惠
//            String kv = "\"coupon\":{\"enable\":\"true\",\"coupon_id\":\"342115\"},";
            String kv = "\"coupon\":{\"enable\":\"false\"},";
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
//            String kv = "\"hot_recommanded\":{\"enable\":\"true\",\"hot_recommanded_id\":\"341911\"},";
            String kv = "\"hot_recommanded\":{\"enable\":\"false\"},";
            TextWord word = new TextWord(kv);
            ruleRoot.addRuleWord(word);
        }

        {
            // shop_discount 店铺活动
//            String kv = "\"shop_discount\":{\"enable\":\"true\",\"shop_discount_id\":\"342160\"},";
            String kv = "\"shop_discount\":{\"enable\":\"false\"},";
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
            // item_picture 商品图片
            {
                String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
//                String kv = "\"item_picture\":{\"order\": \"6\",\"enable\":\"true\",";
                TextWord word = new TextWord(kv);
                ruleRoot.addRuleWord(word);
            }

            {
                // 第1张, 顶部固定图_品牌信息
                String strImgJiage = "https://img.alicdn.com/imgextra/i3/2183719539/TB2CfX1aLOM.eBjSZFqXXculVXa_!!2183719539.jpg";
                do处理无线端20张图片(0, ruleRoot, new TextWord(strImgJiage));
            }

            {
                // 第2张, 顶部固定图_消费者告知书
                String strImgJiage = "https://img.alicdn.com/imgextra/i4/2183719539/TB2llGRaraI.eBjSszdXXaB6XXa_!!2183719539.jpg";
                do处理无线端20张图片(1, ruleRoot, new TextWord(strImgJiage));
            }

            {
                // 第3张, 顶部固定图_产品实拍分隔文字
                String strImgJiage = "https://img.alicdn.com/imgextra/i3/2183719539/TB2lgt4aOGO.eBjSZFjXXcU9FXa_!!2183719539.jpg";
                do处理无线端20张图片(2, ruleRoot, new TextWord(strImgJiage));
            }

            {
                // 第4~8张, 无线商品图片(四张)
                do处理无线端20张图片(3, ruleRoot, new DictWord("无线商品图片-1"));
                do处理无线端20张图片(4, ruleRoot, new DictWord("无线商品图片-2"));
                do处理无线端20张图片(5, ruleRoot, new DictWord("无线商品图片-3"));
                do处理无线端20张图片(6, ruleRoot, new DictWord("无线商品图片-4"));
                do处理无线端20张图片(7, ruleRoot, new DictWord("无线商品图片-5"));
            }

            {
                // 第9张, 底部固定图_购物流程
                String strImgJiage = "https://img.alicdn.com/imgextra/i4/2183719539/TB2wymOaq9I.eBjy0FeXXXqwFXa_!!2183719539.jpg";
                do处理无线端20张图片(8, ruleRoot, new TextWord(strImgJiage));
            }

            {
                // 第10张, 底部固定图_温馨提示
                String strImgJiage = "https://img.alicdn.com/imgextra/i3/2183719539/TB2kACQaseJ.eBjy0FiXXXqapXa_!!2183719539.jpg";
                do处理无线端20张图片(9, ruleRoot, new TextWord(strImgJiage));
            }

            {
                // 第11张, 底部固定图_售后须知，关于价格
                String strImgJiage = "https://img.alicdn.com/imgextra/i4/2183719539/TB2W_uSar1J.eBjy1zeXXX9kVXa_!!2183719539.jpg";
                do处理无线端20张图片(10, ruleRoot, new TextWord(strImgJiage));
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

    /**
     * 根据index生成天猫无线商品图1-5的图片字典
     *
     * @param index String  自定义图片index
     * @return RuleExpression 无线商品图片字典
     */
    private RuleExpression doDict_无线商品图片(String index) {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // 商品图（图1~图5）
            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord("%s"));
            userParam.setHtmlTemplate(htmlTemplate);
            RuleExpression imageTemplate = new RuleExpression();
            // %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
//            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/VTM%%2DTM%%2DSKU?$img=%s&layer=comp&wid=1242&hei=1000"));
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/VTM%%2DTM%%2DSKU?$img=%s&layer=comp&wid=1242&hei=1000"));
            userParam.setImageTemplate(imageTemplate);
            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord(String.valueOf(index)));   // 商品图片(图1~图5)index(0~4)
            userParam.setImageIndex(imageIndex);
            RuleExpression img_imageType = new RuleExpression();
            img_imageType.addRuleWord(new TextWord(C_商品图片));
            userParam.setImageType(img_imageType);

            CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
            wordValueGetMainProductImages.setUserParam(userParam);
            ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));
        }

        return ruleRoot;
    }


//    private RuleExpression doDict_天猫同购无线描述() {
//        // 根字典
//        RuleExpression ruleRoot = new RuleExpression();
//
//        {
//            // start
//            String kv = "{";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
////        {
////            // K-V 模板
////            String kv = "\"k1\":{\"k1-1\":\"v1\",\"k1-2\":\"v2\"},";
////            TextWord word = new TextWord(kv);
////            ruleRoot.addRuleWord(word);
////
////            <field id="xxx_enable" name="是否启用" type="singleCheck">
////                <options>
////                <option displayName="启用" value="true"/>
////                <option displayName="不启用" value="false"/>
////                </options>
////            </field>
////        }
//
//        {
//            // coupon 优惠
////            String kv = "\"coupon\":{\"enable\":\"true\",\"coupon_id\":\"342115\"},";
//            String kv = "\"coupon\":{\"enable\":\"false\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // hot_recommanded 同店推荐
////            <field id="hot_recommanded_id" name="选择模板" type="singleCheck">
////                <options>
////                <option displayName="商品推荐" value="520277"/>
////                </options>
////            </field>
////            String kv = "\"hot_recommanded\":{\"enable\":\"true\",\"hot_recommanded_id\":\"341911\"},";
//            String kv = "\"hot_recommanded\":{\"enable\":\"false\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        {
//            // shop_discount 店铺活动
////            String kv = "\"shop_discount\":{\"enable\":\"true\",\"shop_discount_id\":\"342160\"},";
//            String kv = "\"shop_discount\":{\"enable\":\"false\"},";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
////        {
////            // user_define 自定义
////            String kv = "\"user_define\":{\"user_define_enable\":\"true\",\"user_define_name\":\"xxx\",\"user_define_image_0\":\"xxx\",\"user_define_image_1\":\"xxx\"},";
////            TextWord word = new TextWord(kv);
////            ruleRoot.addRuleWord(word);
////        }
//
//        // 图片全是DictWord(总共20张图片,但最后5张不用设置图片)
//        {
//            // item_picture 商品图片
//            {
//                String kv = "\"item_picture\":{\"order\": \"6\",\"enable\":\"true\",";
//                TextWord word = new TextWord(kv);
//                ruleRoot.addRuleWord(word);
//            }
//
//            {
//                TextWord word = new TextWord("\"content\":[");
//                ruleRoot.addRuleWord(word);
//            }
//
//            {
//                // 5张无线商品图片
//                for (int i = 0; i < 5; i++) {
//                    int j = i + 1;
//                    String imageStr = ", {\"img\":\"";
//                    if (i == 0) {
//                        imageStr = "{\"img\":\"";
//                    }
//
//                    TextWord imageWord = new TextWord(imageStr);
//                    ruleRoot.addRuleWord(imageWord);
//
//                    DictWord dictRoot = new DictWord();
//                    dictRoot.setName("无线商品图片-" + j);
//                    ruleRoot.addRuleWord(dictRoot);
//
//                    imageStr = "\"}";
//                    imageWord = new TextWord(imageStr);
//                    ruleRoot.addRuleWord(imageWord);
//                }
//
//                // 5张无线自定义图片
//                for (int i = 5; i < 10; i++) {
//
//                    int j = i - 4;
//                    String imageStr = ", {\"img\":\"";
//                    TextWord imageWord = new TextWord(imageStr);
//                    ruleRoot.addRuleWord(imageWord);
//
//                    DictWord dictRoot = new DictWord();
//                    dictRoot.setName("无线自定义图片-" + j);
//                    ruleRoot.addRuleWord(dictRoot);
//
//                    imageStr = "\"}";
//                    imageWord = new TextWord(imageStr);
//                    ruleRoot.addRuleWord(imageWord);
//                }
//            }
//
//            {
//                TextWord word = new TextWord("]");
//                ruleRoot.addRuleWord(word);
//            }
//
//
//
////            // 5张无线固定图
////            for (int i = 10; i < 15; i++) {
////                // 5张无线商品图片
////                int j = i - 9;
////                String imageStr = ",\"image_hot_area_" + i + "\":{\"item_picture_image\":\"";
////                TextWord imageWord = new TextWord(imageStr);
////                ruleRoot.addRuleWord(imageWord);
////
////                DictWord dictRoot = new DictWord();
////                dictRoot.setName("无线固定图-" + j);
////                ruleRoot.addRuleWord(dictRoot);
////
////                imageStr = "\"}";
////                imageWord = new TextWord(imageStr);
////                ruleRoot.addRuleWord(imageWord);
////            }
//
//            // item_picture_image_15 ~ item_picture_image_19不用设置
//
//            // end
//            String endStr = "}";
//            TextWord endWord = new TextWord(endStr);
//            ruleRoot.addRuleWord(endWord);
//        }
//
//        {
//            // end
//            String kv = "}";
//            TextWord word = new TextWord(kv);
//            ruleRoot.addRuleWord(word);
//        }
//
//        return ruleRoot;
//    }

}
