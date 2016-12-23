package com.voyageone.task2.cms.dict;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
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
 * Created by zhujiaye on 16/7/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class Tmall_TongGou_024_OverStock_DictTest {
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
		doCreateJson("天猫同购描述", false, doDict_详情页描述(null, null));
		// 手表
		doCreateJson("天猫同购描述-手表", false, doDict_详情页描述("http://s7d5.scene7.com/is/image/sneakerhead/oswatch1?$790%%5F700$&$layer_17_src=%s&$layer_11_textps_0=%s&$layer_12_textps_0=%s&$layer_13_textps_0=%s&$layer_14_textps_0=%s&$layer_15_textps_0=%s&$layer_16_textps_0=%s&$layer_10_textps_0=%s&$layer_9_textps_0=%s&$layer_8_textps_0=%s&$layer_7_textps_0=%s&$layer_6_textps_0=%s&$layer_5_textps_0=%s&$layer_4_textps_0=%s&$layer_3_textps_0=%%20%s&$layer_2_textps_0=%s", false));

	}

	@Test
	public void dictTest() {
//		SxData sxData = sxProductService.getSxProductDataByGroupId("024", 890844L);
		SxData sxData = sxProductService.getSxProductDataByGroupId("024", 890914L);
		sxData.setCartId(23);
		ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
		ShopBean shopProp = Shops.getShop("024", 23);
//        shopProp.setCart_id("27");
		shopProp.setPlatform_id(PlatFormEnums.PlatForm.TM.getId());

		try {
			System.out.println("=====================================");
			System.out.println("字典: 详情页描述");
			String result = sxProductService.resolveDict("详情页描述", expressionParser, shopProp, getTaskName(), null);
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
	private void doCreateJson(String title, boolean isUrl, RuleExpression ruleRoot) {

		DictWord dictRoot = new DictWord();
		dictRoot.setName(title);
		dictRoot.setExpression(ruleRoot);
		dictRoot.setIsUrl(isUrl);

		RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
		String json = ruleJsonMapper.serializeRuleWord(dictRoot);

		System.out.println("=====================================");
		System.out.println("字典: " + title);
		System.out.println(json);

	}

	/**
	 * 详情页描述(PC端)
	 * 0. 固定图片（产品信息）
	 * 1. 英文短描述 回车
	 * 2. 共通图片 - 尺码图
	 * 3. 商品图前缀 + 商品图(模板:http://s7d5.scene7.com/is/image/sneakerhead/img-1?$790_600$&$img=%s)
	 * 4. 共通图片 - 购物流程(购物流程+购物须知+7天退货服务须知)
	 * 5. 共通图片 - 店铺介绍图
	 */
	private RuleExpression doDict_详情页描述(String 参数图url, Boolean containsTitle) {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		// 生成内容

//		// OverStock变成官网同购后， 参数图暂时不要了， 不确定以后还要不要
//		do参数图(ruleRoot);

		{
			// 产品信息 这几个字
			TextWord word = new TextWord(String.format(C_TEMPLATE_IMG, "https://img.alicdn.com/imgextra/i4/2939402618/TB2tT9Bb.OO.eBjSZFLXXcxmXXa-2939402618.jpg"));
			ruleRoot.addRuleWord(word);
		}

//		{
//			// 英文短描述
//			MasterHtmlWord word = new MasterHtmlWord("shortDesEn");
//			ruleRoot.addRuleWord(word);
//		}
		{
			// feed_info的modelLongdescription
			FeedOrgWord word = new FeedOrgWord("modelLongdescription");
			ruleRoot.addRuleWord(word);
		}
		{
			// 回车一个
			TextWord word = new TextWord(C_TEXT_BR + C_TEXT_BR);
			ruleRoot.addRuleWord(word);
		}
		{
			// 中文长描述
			MasterWord word = new MasterWord("longDesCn");
			ruleRoot.addRuleWord(word);
		}
		{
			// 回车一个
			TextWord word = new TextWord(C_TEXT_BR + C_TEXT_BR);
			ruleRoot.addRuleWord(word);
		}

		if (!StringUtils.isEmpty(参数图url)) {
			do参数图(ruleRoot, 参数图url, containsTitle);
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
			{
				// 商品图片前缀
				String html = "<div><img src=\"https://img.alicdn.com/imgextra/i1/2939402618/TB2M.5UsVXXXXcYXXXXXXXXXXXX_!!2939402618.jpg\" /></div>";
				ruleRoot.addRuleWord(new TextWord(html));
			}
			{
				String html = "<div style=\"height:80px;line-height:80px;overflow:auto;overflow-x:hidden;\">&nbsp;</div>";
				ruleRoot.addRuleWord(new TextWord(html));
			}
			{
				// 商品图片
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div><div style=\"height:80px;line-height:80px;overflow:auto;overflow-x:hidden;\">&nbsp;</div></div>"));

				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/img-1?$790_600$&$img=%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord(C_商品图片));

				RuleExpression useOriUrl = null;

				CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
				ruleRoot.addRuleWord(new CustomWord(word));
			}
		}

		{
			// 购物流程图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("4"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			RuleExpression useOriUrl = null;

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 店铺介绍图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord("<div><img src=\"%s\" /></div>"));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("5"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			RuleExpression useOriUrl = null;

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		return ruleRoot;
	}

	/**
	 * OverStock变成官网同购后， 参数图暂时不要了， 不确定以后还要不要
	 * @param ruleRoot
	 * @param containsTitle 参数是否包含title
	 */
	private void do参数图(RuleExpression ruleRoot, String 参数图url, Boolean containsTitle) {
		{
			// 商品参数图
			{
				// 前缀
				String html = "<div><img src=\"";
				ruleRoot.addRuleWord(new TextWord(html));
			}

			{
				// imageTemplate
				RuleExpression imageTemplate = new RuleExpression();
				String htmlTemplate = 参数图url;
				imageTemplate.addRuleWord(new TextWord(htmlTemplate));

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
					String sp = "%s";
					int paramCnt = 0;
					int offset = 0;
					while ((offset = 参数图url.indexOf(sp, offset)) != -1) {
						offset = offset + sp.length();
						paramCnt++;
					}
					for (int index = 0; index < paramCnt - 1; index++) {
						{
							if (containsTitle != null && containsTitle) {
								RuleExpression ruleExpression = new RuleExpression();
								ruleExpression.addRuleWord(new FeedCnWord(true, index));
								imageParams.add(ruleExpression);
							}
						}
						{
							RuleExpression ruleExpression = new RuleExpression();
							ruleExpression.addRuleWord(new FeedCnWord(false, index));
							imageParams.add(ruleExpression);
						}
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
	}

}