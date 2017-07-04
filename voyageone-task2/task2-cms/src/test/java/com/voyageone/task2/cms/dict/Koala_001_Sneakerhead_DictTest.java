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
 * Created by Charis on 2017/6/16.
 */
@SuppressWarnings("ALL")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Koala_001_Sneakerhead_DictTest extends BaseDictTest {

    @Autowired
    private SxProductService sxProductService;


    @Test
    public void startUpTest() {

        // 天猫国际
        doCreateJson("详情页描述", false, doDict_详情页描述(34));
        for (int i = 0; i < 10; i++) {
            doCreateJson("商品标题与图片-" + i, false, doDict_商品标题与图片(34, i));
        }

    }


    @Test
    public void dictTest() {

        SxData sxData = sxProductService.getSxProductDataByGroupId("001", 11292497L);
        sxData.setCartId(34);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ShopBean shopProp = Shops.getShop("001", 34);
//        shopProp.setApp_url("https://openapi.kaola.com/router");
//        shopProp.setAppKey("781bc2f98fe49e38b646bd486d78051e");
//        shopProp.setAppSecret("");
//        shopProp.setSessionKey("");
//        shopProp.setPlatform_id(PlatFormEnums.PlatForm.NTES.getId());

        try {
            System.out.println("=====================================");
            System.out.println("字典: 详情页描述");
            String result = sxProductService.resolveDict("详情页描述", expressionParser, shopProp, "testkoala", null);
            result = "<div style=\"width:750px; margin: 0 auto;\">" + result + "</div>";
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private RuleExpression doDict_详情页描述(int cartId) {
        String strPlatformTemplate = C_TEMPLATE_IMG_750;

        // 根字典
        RuleExpression ruleRoot = new RuleExpression();
        // 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
        {
            // 品牌故事图 - 0
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(strPlatformTemplate));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("3"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        // 固定图（产品信息）
        ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate,
                "http://pop.nosdn.127.net/d4a0d61b-505b-4d30-b839-a9c8a66ed18c")));

        // 参数图(自定义图)
        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_750));

            RuleExpression imageTemplate = null;

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord(C_自定义图片));

            RuleExpression useOriUrl = new RuleExpression();
            useOriUrl.addRuleWord(new TextWord("1")); // 使用原图

            RuleExpression imageIndex = new RuleExpression();
            imageIndex.addRuleWord(new TextWord("0"));

            CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, imageIndex);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        // 固定图（温馨提示）
        ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate,
                "http://pop.nosdn.127.net/81439f91-04a3-4880-9ee4-71c3487d7620")));
        // 固定图（产品实拍）
        ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate,
                "http://pop.nosdn.127.net/7e0a8fac-25b9-4dce-84db-9bdae851b898")));

        // 模板图
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-0"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-1"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-2"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-3"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-4"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-5"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-6"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-7"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-8"));
        ruleRoot.addRuleWord(new DictWord("商品标题与图片-9"));

        // 固定图(尺码信息)
        ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate,
                "http://pop.nosdn.127.net/8b2fdf18-bf06-4601-af42-3742a32a14d7")));

        // 尺码图
        {
            RuleExpression htmlTemplate = new RuleExpression();
            htmlTemplate.addRuleWord(new TextWord(strPlatformTemplate));

            RuleExpression imageType = new RuleExpression();
            imageType.addRuleWord(new TextWord("2"));

            RuleExpression viewType = new RuleExpression();
            viewType.addRuleWord(new TextWord("1"));

            CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
            ruleRoot.addRuleWord(new CustomWord(word));
        }

        // 固定图（尺码表温馨提示）
        ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate,
                "http://pop.nosdn.127.net/8abf3ba8-4f72-4685-af3f-2173b46bdb50")));

        // 固定图（物流须知）
        ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate,
                "http://pop.nosdn.127.net/1cddb124-2dc0-442f-b958-78fdf5a633a4")));

        // 固定图（购物须知）
        ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate,
                "http://pop.nosdn.127.net/70507819-e9a8-4d11-90eb-aa9c90d23ec8")));

        return ruleRoot;

    }


    private RuleExpression doDict_商品标题与图片(int cartId, int idx) {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        String imageTemplateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn-kaola-xq-750x2220?$750x2220$&$kl1=%s&$kl2=%s&$kl3=%s&$kl4=%s&$kl5=%s&$kl6=%s&$kaola-wz=%s";
        if (idx == 0) {

            // 前缀
            ruleRoot.addRuleWord(new TextWord("<img src=\""));

            {
                RuleExpression imageTemplate = new RuleExpression();
                imageTemplate.addRuleWord(new TextWord(imageTemplateUrl));

                // 设置参数imageParams的值
                List<RuleExpression> imageParams = new ArrayList<>();
                // 主商品， 第1~5张商品图
                for (int iImgIdx = 0; iImgIdx < 4; iImgIdx++) {

                    RuleExpression ruleExpression = new RuleExpression();
                    CustomModuleUserParamGetProductFieldInfo customModuleParam = new CustomModuleUserParamGetProductFieldInfo();

                    RuleExpression rIsMain = new RuleExpression();
                    rIsMain.addRuleWord(new TextWord("1"));
                    customModuleParam.setIsMain(rIsMain);

                    RuleExpression rDataType = new RuleExpression();
                    rDataType.addRuleWord(new TextWord("image"));
                    customModuleParam.setDataType(rDataType);

                    RuleExpression rImageType = new RuleExpression();
                    rImageType.addRuleWord(new TextWord(C_商品图片));
                    customModuleParam.setImageType(rImageType);

                    RuleExpression rImageIdx = new RuleExpression();
                    rImageIdx.addRuleWord(new TextWord(String.valueOf(iImgIdx)));
                    customModuleParam.setImageIdx(rImageIdx);

                    RuleExpression rPaddingImageType = new RuleExpression();
                    rPaddingImageType.addRuleWord(new TextWord("1stProductImage"));
                    customModuleParam.setPaddingImageType(rPaddingImageType);

                    CustomWordValueGetProductFieldInfo customWord = new CustomWordValueGetProductFieldInfo();
                    customWord.setUserParam(customModuleParam);

                    ruleExpression.addRuleWord(new CustomWord(customWord));
                    imageParams.add(ruleExpression);
                }
                // 包装图
                for (int iImgIdx = 21; iImgIdx < 23; iImgIdx++) {

                    RuleExpression ruleExpression = new RuleExpression();
                    CustomModuleUserParamGetProductFieldInfo customModuleParam = new CustomModuleUserParamGetProductFieldInfo();

                    RuleExpression rIsMain = new RuleExpression();
                    rIsMain.addRuleWord(new TextWord("1"));
                    customModuleParam.setIsMain(rIsMain);

                    RuleExpression rDataType = new RuleExpression();
                    rDataType.addRuleWord(new TextWord("image"));
                    customModuleParam.setDataType(rDataType);

                    RuleExpression rImageType = new RuleExpression();
                    rImageType.addRuleWord(new TextWord(C_包装图片));
                    customModuleParam.setImageType(rImageType);

                    RuleExpression rImageIdx = new RuleExpression();
                    rImageIdx.addRuleWord(new TextWord(String.valueOf(iImgIdx)));
                    customModuleParam.setImageIdx(rImageIdx);

                    RuleExpression rPaddingImageType = new RuleExpression();
                    rPaddingImageType.addRuleWord(new TextWord("1stProductImage"));
                    customModuleParam.setPaddingImageType(rPaddingImageType);

                    CustomWordValueGetProductFieldInfo customWord = new CustomWordValueGetProductFieldInfo();
                    customWord.setUserParam(customModuleParam);

                    ruleExpression.addRuleWord(new CustomWord(customWord));
                    imageParams.add(ruleExpression);
                }
                // code
                {
                    RuleExpression ruleExpression = new RuleExpression();
                    CustomModuleUserParamGetProductFieldInfo customModuleParam = new CustomModuleUserParamGetProductFieldInfo();

                    RuleExpression rIsMain = new RuleExpression();
                    rIsMain.addRuleWord(new TextWord("1"));
                    customModuleParam.setIsMain(rIsMain);

                    RuleExpression rDataType = new RuleExpression();
                    rDataType.addRuleWord(new TextWord("prop"));
                    customModuleParam.setDataType(rDataType);

                    RuleExpression rPropName = new RuleExpression();
                    rPropName.addRuleWord(new TextWord("code"));
                    customModuleParam.setPropName(rPropName);

                    CustomWordValueGetProductFieldInfo customWord = new CustomWordValueGetProductFieldInfo();
                    customWord.setUserParam(customModuleParam);

                    ruleExpression.addRuleWord(new CustomWord(customWord));
                    imageParams.add(ruleExpression);
                }

                CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                ruleRoot.addRuleWord(new CustomWord(imagesWithParamWord));
            }

            // 后缀
            ruleRoot.addRuleWord(new TextWord("\">"));

        } else {
            // 非主商品
//            ruleRoot.addRuleWord(new SubCodeWord(idx - 1 , "code"));

            // 前缀
            ruleRoot.addRuleWord(new TextWord("<img src=\""));

            {
                RuleExpression imageTemplate = new RuleExpression();
                imageTemplate.addRuleWord(new TextWord(imageTemplateUrl));

                // 设置参数imageParams的值
                List<RuleExpression> imageParams = new ArrayList<>();
                // 非主商品， 第1~5张商品图
                for (int iImgIdx = 0; iImgIdx < 4; iImgIdx++) {

                    RuleExpression ruleExpression = new RuleExpression();
                    CustomModuleUserParamGetProductFieldInfo customModuleParam = new CustomModuleUserParamGetProductFieldInfo();

                    RuleExpression rIsMain = new RuleExpression();
                    rIsMain.addRuleWord(new TextWord("0"));
                    customModuleParam.setIsMain(rIsMain);

                    RuleExpression rCodeIdx = new RuleExpression();
                    rCodeIdx.addRuleWord(new TextWord(String.valueOf(idx - 1)));
                    customModuleParam.setCodeIdx(rCodeIdx);

                    RuleExpression rDataType = new RuleExpression();
                    rDataType.addRuleWord(new TextWord("image"));
                    customModuleParam.setDataType(rDataType);

                    RuleExpression rImageType = new RuleExpression();
                    rImageType.addRuleWord(new TextWord(C_商品图片));
                    customModuleParam.setImageType(rImageType);

                    RuleExpression rImageIdx = new RuleExpression();
                    rImageIdx.addRuleWord(new TextWord(String.valueOf(iImgIdx)));
                    customModuleParam.setImageIdx(rImageIdx);

                    RuleExpression rPaddingImageType = new RuleExpression();
                    rPaddingImageType.addRuleWord(new TextWord("1stProductImage"));
                    customModuleParam.setPaddingImageType(rPaddingImageType);

                    CustomWordValueGetProductFieldInfo customWord = new CustomWordValueGetProductFieldInfo();
                    customWord.setUserParam(customModuleParam);

                    ruleExpression.addRuleWord(new CustomWord(customWord));
                    imageParams.add(ruleExpression);
                }
                // 包装图
                for (int iImgIdx = 21; iImgIdx < 23; iImgIdx++) {
                    RuleExpression ruleExpression = new RuleExpression();
                    CustomModuleUserParamGetProductFieldInfo customModuleParam = new CustomModuleUserParamGetProductFieldInfo();

                    RuleExpression rIsMain = new RuleExpression();
                    rIsMain.addRuleWord(new TextWord("0"));
                    customModuleParam.setIsMain(rIsMain);

                    RuleExpression rCodeIdx = new RuleExpression();
                    rCodeIdx.addRuleWord(new TextWord(String.valueOf(idx - 1)));
                    customModuleParam.setCodeIdx(rCodeIdx);

                    RuleExpression rDataType = new RuleExpression();
                    rDataType.addRuleWord(new TextWord("image"));
                    customModuleParam.setDataType(rDataType);

                    RuleExpression rImageType = new RuleExpression();
                    rImageType.addRuleWord(new TextWord(C_包装图片));
                    customModuleParam.setImageType(rImageType);

                    RuleExpression rImageIdx = new RuleExpression();
                    rImageIdx.addRuleWord(new TextWord(String.valueOf(iImgIdx)));
                    customModuleParam.setImageIdx(rImageIdx);

                    RuleExpression rPaddingImageType = new RuleExpression();
                    rPaddingImageType.addRuleWord(new TextWord("1stProductImage"));
                    customModuleParam.setPaddingImageType(rPaddingImageType);

                    CustomWordValueGetProductFieldInfo customWord = new CustomWordValueGetProductFieldInfo();
                    customWord.setUserParam(customModuleParam);

                    ruleExpression.addRuleWord(new CustomWord(customWord));
                    imageParams.add(ruleExpression);
                }
                // code
                {
                    RuleExpression ruleExpression = new RuleExpression();
                    CustomModuleUserParamGetProductFieldInfo customModuleParam = new CustomModuleUserParamGetProductFieldInfo();

                    RuleExpression rIsMain = new RuleExpression();
                    rIsMain.addRuleWord(new TextWord("1"));
                    customModuleParam.setIsMain(rIsMain);

                    RuleExpression rCodeIdx = new RuleExpression();
                    rCodeIdx.addRuleWord(new TextWord(String.valueOf(idx - 1)));
                    customModuleParam.setCodeIdx(rCodeIdx);

                    RuleExpression rDataType = new RuleExpression();
                    rDataType.addRuleWord(new TextWord("prop"));
                    customModuleParam.setDataType(rDataType);

                    RuleExpression rPropName = new RuleExpression();
                    rPropName.addRuleWord(new TextWord("code"));
                    customModuleParam.setPropName(rPropName);

                    CustomWordValueGetProductFieldInfo customWord = new CustomWordValueGetProductFieldInfo();
                    customWord.setUserParam(customModuleParam);

                    ruleExpression.addRuleWord(new CustomWord(customWord));
                    imageParams.add(ruleExpression);
                }



                CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
                ruleRoot.addRuleWord(new CustomWord(imagesWithParamWord));
            }

            // 后缀
            ruleRoot.addRuleWord(new TextWord("\">"));

        }
        return ruleRoot;
    }

}
