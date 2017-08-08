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
 * Created by morse on 17/8/7.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_044_ManCity_DictTest extends BaseDictTest {
	@Autowired
	private SxProductService sxProductService;
	@Test
	public void startupTest() {

		doCreateJson("详情页描述", false, doPC端详情页描述());

	}
	@Test
	public void dictTest() {

		SxData sxData = sxProductService.getSxProductDataByGroupId("044", 11396913L);
		sxData.setCartId(23);
		ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
		ShopBean shopProp = Shops.getShop("044", 23);
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("23647092");
        shopProp.setAppSecret("02c5e8b733f7b6856af5dcbc5598fb2d");
        shopProp.setSessionKey("6200b207ac470d1c01bc8d07ea18ZZ4240dc4aec191079f3362876888");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());

		try {
			System.out.println("=====================================");
			System.out.println("字典: 详情页描述");
			String result = sxProductService.resolveDict("详情页描述", expressionParser, shopProp, "morse.test", null);
//			result = "<div style=\"width:790px; margin: 0 auto;\">" + result + "</div>";
			System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private RuleExpression doPC端详情页描述() {
		String strTemplate_Param_Image = "http://s7d5.scene7.com/is/image/sneakerhead/MANCITY7901300ZHUTUMUBAN-20170724?$KITBAG20170421790x1300TEST1$&$TSXT11=%s&$product=%s&$MCjulebu=%s&$MCCAIZHI=%s&$MCPINGPAI=%s";
		String strTemplate_Main_Image = "http://s7d5.scene7.com/is/image/sneakerhead/mancityzhutumuban120170724?$champion20170314800x800baoyou1$&$product=%s";

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

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		// 参数图
		{
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			// 商品信息模板
			RuleExpression imageTemplate = new RuleExpression();
			imageTemplate.addRuleWord(new TextWord(strTemplate_Param_Image));

			// 参数imageParams
			List<RuleExpression> imageParams = new ArrayList<>();
			// 参数
			{
				{
					// 名字
					RuleExpression ruleExpression = new RuleExpression();
					MasterWord nameWord = new MasterWord("title");
					RuleExpression defaultExpression = new RuleExpression();
					defaultExpression.addRuleWord(new MasterWord("productNameEn"));
					nameWord.setDefaultExpression(defaultExpression);
					ruleExpression.addRuleWord(nameWord);
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
					// 俱乐部
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("shortDesEn"));
					imageParams.add(ruleExpression);
				}
				{
					// 材质
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("materialEn"));
					imageParams.add(ruleExpression);
				}
				{
					// 品牌
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("brand"));
					imageParams.add(ruleExpression);
				}
			}

			CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null, htmlTemplate);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 详情描述英文
			ruleRoot.addRuleWord(new MasterWord("longDesEn"));
		}

		{
			// 固定图片 - 产品展示
			ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i1/3362876888/TB2zht5aX_.F1JjSZFjXXahnXXa_!!3362876888.jpg")));
		}

		// 主商品4张图
		for (int i = 0; i < 4; i++) {
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
			// 固定图片 - 尺码展示
			ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i1/3362876888/TB2C66MXUifF1JjSspdXXclLpXa_!!3362876888.jpg")));
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