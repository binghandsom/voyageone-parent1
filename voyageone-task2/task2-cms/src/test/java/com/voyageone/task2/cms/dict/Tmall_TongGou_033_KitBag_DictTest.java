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
 * Created by Charis on 2017/4/24.
 */
@SuppressWarnings("ALL")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_TongGou_033_KitBag_DictTest extends BaseDictTest{
    @Autowired
    private SxProductService sxProductService;

    @Test
    public void startupTest() {

        System.out.println("=====================================");

        doCreateJson("天猫同购描述", false, doPC详情());

        doCreateJson("天猫同购描述-全自定义", false, doPC详情_全自定义());

    }

    @Test
    public void dictTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("033", 10840338L);
        sxData.setCartId(30);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = new ShopBean();
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());

        try {
            System.out.println("=====================================");
            System.out.println("字典: 天猫同购描述");
            String result = sxProductService.resolveDict("天猫同购描述", expressionParser, shopProp, "kitbag_dict", null);
            result = "<div style=\"width:790px; margin: 0 auto;\">" + result + "</div>";
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private RuleExpression doPC详情_全自定义() {
        RuleExpression ruleRoot = new RuleExpression();
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
        return ruleRoot;
    }

    private RuleExpression doPC详情() {

        RuleExpression ruleRoot = new RuleExpression();
        // 品牌故事图
        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("3"));

            // 1:PC端 2:APP端
            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            // 1:使用原图 其它或者未设置，使用天猫平台图
            RuleExpression useOriUrl = new RuleExpression();
            useOriUrl.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }
        // 参数图
        {
            {
                // 前缀
                String html = "<div><img src=\"";
                ruleRoot.addRuleWord(new TextWord(html));
            }
            RuleExpression imageTemplate = new RuleExpression();
            String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/KITBAG20170424790X1300TEST3?$KITBAG20170421790x1300TEST1$" +
                    "&$PRODUCT=%s" + // 主商品第一张图
                    "&$BRAND=%s" + // master brand
                    "&$MATERIAL=%s" + // 材质中文
                    "&$TITLE=%s" + // 产品名称中文
                    "&$CLUB=%s"; // 简短描述中文
            imageTemplate.addRuleWord(new TextWord(htmlTemplate));

            // 参数imageParams
            List<RuleExpression> imageParams = new ArrayList<>();
            // 参数
            {
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
                    RuleExpression ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(new MasterWord("brand"));
                    imageParams.add(ruleExpression);
                }
                {
                    RuleExpression ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(new MasterWord("materialCn"));
                    imageParams.add(ruleExpression);
                }
                {
                    RuleExpression ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(new MasterWord("originalTitleCn"));
                    imageParams.add(ruleExpression);
                }
                {
                    RuleExpression ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(new MasterWord("shortDesCn"));
                    imageParams.add(ruleExpression);
                }

            }

            RuleExpression useCmsBtImageTemplate = null;

            CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, useCmsBtImageTemplate, null);
            ruleRoot.addRuleWord(new CustomWord(word));

            {
                // 后缀
                String html = "\"></div>";
                ruleRoot.addRuleWord(new TextWord(html));
            }
        }

        {
            // 详情描述英文
            String html = "<div style=\"margin-left: 35px; margin-right: 40px;\">";
            ruleRoot.addRuleWord(new TextWord(html));

            ruleRoot.addRuleWord(new MasterHtmlWord("longDesEn")); // 英文长描述

            ruleRoot.addRuleWord(new TextWord("</div><br /><br />"));
        }
        // 主商品实拍图
        for (int i = 0; i < 4; i++) {
            CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
            userParam.setHtmlTemplate(htmlTemplate);
            // %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
            RuleExpression imageTemplate = new RuleExpression();
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/KITBAG20170421790x750TEST1?$PRODUCT=%s&layer=comp&wid=790&hei=750"));
            userParam.setImageTemplate(imageTemplate);
            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord(String.valueOf(i)));
            userParam.setImageIndex(imageIndex);
            RuleExpression img_imageType = new RuleExpression();
            img_imageType.addRuleWord(new TextWord(C_商品图片));
            userParam.setImageType(img_imageType);

            CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
            wordValueGetMainProductImages.setUserParam(userParam);
            ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));
        }
        // 尺码图
        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("2"));

            // 1:PC端 2:APP端
            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            // 1:使用原图 其它或者未设置，使用天猫平台图
            RuleExpression useOriUrl = new RuleExpression();
            useOriUrl.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));

        }
        // 尺码图(指引到各品牌详细尺码)
        {
            String template = "<div style='width:790px;height:508px;overflow:hidden;font-size:0px;line-height:0px;'>\n" +
                    "\t<img src=\"%s\" border='0' usemap='#map8739897477' alt=''/>\n" +
                    "\t<map name='map8739897477' id='map8739897477'>\n" +
                    "\t\t<area target='_blank' title='nb' outline='none' shape='rect' coords='43,98,259,244'  href='%s' />\n" +
                    "\t\t<area target='_blank' title='nike' outline='none' shape='rect' coords='286,98,502,244'  href='%s' />\n" +
                    "\t\t<area target='_blank' title='umbro' outline='none' shape='rect' coords='527,98,743,243'  href='%s' />\n" +
                    "\t\t<area target='_blank' title='puma' outline='none' shape='rect' coords='43,268,259,411'  href='%s' />\n" +
                    "\t\t<area target='_blank' title='adidas' outline='none' shape='rect' coords='285,267,501,411'  href='%s' />\n" +
                    "\t\t<area target='_blank' title='ua' outline='none' shape='rect' coords='527,266,743,412'  href='%s' />\n" +
                    "\t</map>\n" +
                    "</div>";
            TextWord word = new TextWord(String.format(template,
                    "https://img.alicdn.com/imgextra/i2/3247884386/TB2oI0poMNlpuFjy0FfXXX3CpXa_!!3247884386.jpg",
                    "https://h5.m.taobao.com/weapp/view_page.htm?page=shop/activity&userId=3247884386&shopId=404370509&pageId=78526031",
                    "https://h5.m.taobao.com/weapp/view_page.htm?page=shop/activity&userId=3247884386&shopId=404370509&pageId=78916026",
                    "https://h5.m.taobao.com/weapp/view_page.htm?page=shop/activity&userId=3247884386&shopId=404370509&pageId=78241036",
                    "https://h5.m.taobao.com/weapp/view_page.htm?page=shop/activity&userId=3247884386&shopId=404370509&pageId=78626022",
                    "https://h5.m.taobao.com/weapp/view_page.htm?page=shop/activity&userId=3247884386&shopId=404370509&pageId=79239026",
                    "https://h5.m.taobao.com/weapp/view_page.htm?page=shop/activity&userId=3247884386&shopId=404370509&pageId=78310034"));
            ruleRoot.addRuleWord(word);
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

        // 非主商品第一张图
        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_商品图片));

            RuleExpression imageTemplate = new RuleExpression();
            imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/KITBAG20170421790x750TEST1?$PRODUCT=%s&layer=comp&wid=790&hei=750"));

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            RuleExpression codeIndex = new RuleExpression();
            codeIndex.addRuleWord(new TextWord("-1")); // 所有非主商品

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null, null, null, codeIndex, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));

        }
        // 物流介绍图
        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("4"));

            // 1:PC端 2:APP端
            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            // 1:使用原图 其它或者未设置，使用天猫平台图
            RuleExpression useOriUrl = new RuleExpression();
            useOriUrl.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));

        }
        // 店铺介绍图
        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

            // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("5"));

            // 1:PC端 2:APP端
            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            // 1:使用原图 其它或者未设置，使用天猫平台图
            RuleExpression useOriUrl = new RuleExpression();
            useOriUrl.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
            ruleRoot.addRuleWord(new CustomWord(word));

        }


        return ruleRoot;
    }
}
