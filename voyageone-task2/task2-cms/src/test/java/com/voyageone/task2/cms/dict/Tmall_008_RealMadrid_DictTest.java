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
 * Created by zhujiaye on 17/6/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_008_RealMadrid_DictTest extends BaseDictTest {
	@Autowired
	private SxProductService sxProductService;
	@Test
	public void startupTest() {

		doCreateJson("详情页描述", false, doPC端详情页描述());

	}
	@Test
	public void dictTest() {

		SxData sxData = sxProductService.getSxProductDataByGroupId("008", 11396913L);
		sxData.setCartId(23);
		ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
		ShopBean shopProp = Shops.getShop("008", 23);
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());

		try {
			System.out.println("=====================================");
			System.out.println("字典: 详情页描述");
			String result = sxProductService.resolveDict("详情页描述", expressionParser, shopProp, "testhuangma", null);
			result = "<div style=\"width:790px; margin: 0 auto;\">" + result + "</div>";
			System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private RuleExpression doPC端详情页描述() {
		String strTemplate_Param_Image = "http://s7d5.scene7.com/is/image/sneakerhead/REALMADRIDzhutumuban20170615790x1157?$KITBAG20170421790x1300TEST1$&$MINGZI=%s&$PRODUCT=%s&$code=%s&$BRAND=%s&$MATERIAL=%s";
		String strTemplate_Main_Image = "http://s7d5.scene7.com/is/image/sneakerhead/REALMADRID20170615TEST1?$KITBAG20170421790x750TEST1$&$PRODUCT=%s";

		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		// 生成内容
		{
			// 品牌故事图
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

			// 商品信息模板
			RuleExpression imageTemplate = new RuleExpression();
			String htmlTemplate = strTemplate_Param_Image;
			imageTemplate.addRuleWord(new TextWord(htmlTemplate));

			// 参数imageParams
			List<RuleExpression> imageParams = new ArrayList<>();
			// 参数
			{
				{
					// 英文标题
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("productNameEn"));
					imageParams.add(ruleExpression);
				}
				{
					// 商品主图
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
					// model
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("model"));
					imageParams.add(ruleExpression);
				}
				{
					// 品牌
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("brand"));
					imageParams.add(ruleExpression);
				}
				{
					// 材质
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("materialEn"));
					imageParams.add(ruleExpression);
				}
			}

//			RuleExpression useCmsBtImageTemplate = new RuleExpression();
//			useCmsBtImageTemplate.addRuleWord(new TextWord("true"));
			RuleExpression useCmsBtImageTemplate = null;

			CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, useCmsBtImageTemplate, null);
			ruleRoot.addRuleWord(new CustomWord(word));

			{
				// 后缀
				String html = "\"></div>";
				ruleRoot.addRuleWord(new TextWord(html));
			}
		}


		// 详情描述英文
		ruleRoot.addRuleWord(new MasterWord("longDesEn"));


		// 主商品4张图
		for (int i = 1; i < 5; i++) {
			// 商品展示模板
			CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
			userParam.setHtmlTemplate(htmlTemplate);
			RuleExpression imageTemplate = new RuleExpression();
			// %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
			imageTemplate.addRuleWord(new TextWord(strTemplate_Main_Image));
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

		// 非主商品第一张图(4个商品)
		for (int i = 0; i < 4; i++) {
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord(C_商品图片));

			RuleExpression imageTemplate = new RuleExpression();
			imageTemplate.addRuleWord(new TextWord(strTemplate_Main_Image));

			RuleExpression imageIndex = new RuleExpression();
			imageIndex.addRuleWord(new TextWord("0")); // 第一张图片

			RuleExpression codeIndex = new RuleExpression();
			codeIndex.addRuleWord(new TextWord(String.valueOf(i)));

			CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null, null, null, codeIndex, imageIndex);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 尺码图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			// 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("2"));

			// 1:PC端 2:APP端
			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 物流介绍图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			// 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("4"));

			// 1:PC端 2:APP端
			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 店铺介绍图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			// 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("5"));

			// 1:PC端 2:APP端
			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}


		return ruleRoot;
	}

}