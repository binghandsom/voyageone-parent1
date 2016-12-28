package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/5/20.
 */
public class Tmall_007_Champion_DictTest extends BaseDictTest {

	@Test
	public void startupTest() {

		System.out.println("=====================================");
		{
			// 产品图片
			String templateUrl = "";
			for (int i = 0; i < 5; i++) {
				doCreateJson("产品图片-" + ( i + 1 ), false, doDict_商品图片(templateUrl, String.valueOf(i)));
			}
		}
		{
			// 商品图片
			String templateUrl = "";
			for (int i = 0; i < 5; i++) {
				doCreateJson("商品图片-" + ( i + 1 ), false, doDict_商品图片(templateUrl, String.valueOf(i)));
			}
		}
		{
			// 透明图片
			String templateUrl = "";
			doCreateJson("透明图片", false, doDict_商品图片(templateUrl, "0"));
		}

		doCreateJson("详情页描述", false, doPC端详情页描述());
		doCreateJson("无线描述", false, doDict_无线描述());

	}

	private RuleExpression doPC端详情页描述() {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		// 生成内容
		{
			// 品牌故事图(第一张)
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			// 图片类型, 1:商品图 2:尺码图 3:品牌故事图 4:物流介绍图 5:店铺图
			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("3"));

			// 1:PC端 2:APP端
			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			// 图片index 如果是null, 那么就获取全部, index从0开始, 指定但不存在的场合返回""
			RuleExpression imageIndex = new RuleExpression();
			imageIndex.addRuleWord(new TextWord("0"));  // 先设置第一张品牌故事图，后面会再设置一张品牌故事图

			// 1:使用原图 其它或者未设置，使用天猫平台图
			RuleExpression useOriUrl = new RuleExpression();
			useOriUrl.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, imageIndex);
			ruleRoot.addRuleWord(new CustomWord(word));

		}

		{
			// 商品展示模板
			CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
			userParam.setHtmlTemplate(htmlTemplate);
			RuleExpression imageTemplate = new RuleExpression();
			// %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_pc_detail?$cp_790x720$&$layer_2_src="));
			userParam.setImageTemplate(imageTemplate);
			RuleExpression imageIndex = new RuleExpression();
			imageIndex.addRuleWord(new TextWord(String.valueOf(0)));
			userParam.setImageIndex(imageIndex);
			RuleExpression img_imageType = new RuleExpression();
			img_imageType.addRuleWord(new TextWord(C_商品图片));
			userParam.setImageType(img_imageType);

			CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
			wordValueGetMainProductImages.setUserParam(userParam);
			ruleRoot.addRuleWord(new CustomWord(wordValueGetMainProductImages));
		}

		{
			// 商品信息模板
			RuleExpression imageTemplate = new RuleExpression();
			String htmlTemplate = String.format(C_TEMPLATE_IMG_790, "http://s7d5.scene7.com/is/image/sneakerhead/champion%%5Fxq%%5Fpc%%5Finfo?$cp%%5F790x265$" +
					"&$text01=%s" + // 主商品的中文商品名称
					"&$text02=%s" + // model名
					"&$text03=%s" + // 主商品的中文材质
					"&$text04=%s" + // 自定义属性商品风格
					"&$text05=%s"); // 中文长描述
			imageTemplate.addRuleWord(new TextWord(htmlTemplate));

			// 参数imageParams
			List<RuleExpression> imageParams = new ArrayList<>();
			// 参数
			{
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("originalTitleCn"));
					imageParams.add(ruleExpression);
				}
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("model"));
					imageParams.add(ruleExpression);
				}
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("materialCn"));
					imageParams.add(ruleExpression);
				}
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new FeedCnWord("style"));
					imageParams.add(ruleExpression);
				}
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("longDesCn"));
					imageParams.add(ruleExpression);
				}
			}

			RuleExpression useCmsBtImageTemplate = new RuleExpression();
			useCmsBtImageTemplate.addRuleWord(new TextWord("true"));

			CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, useCmsBtImageTemplate, null);
			ruleRoot.addRuleWord(new CustomWord(word));

		}

		{
			// 所有自定义图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			RuleExpression imageTemplate = new RuleExpression();
			imageTemplate.addRuleWord(new TextWord(""));


			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord(C_自定义图片));

			RuleExpression useOriUrl = new RuleExpression();
			useOriUrl.addRuleWord(new TextWord("1"));

			CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 固定图片 - DISPLAY商品展示
			ruleRoot.addRuleWord(new TextWord("https://img.alicdn.com/imgextra/i4/2533968112/TB2jcgRbUlnpuFjSZFjXXXTaVXa_!!2533968112.jpg"));
		}

		for (int i = 1; i < 5; i++) {
			// 商品展示模板
			CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
			userParam.setHtmlTemplate(htmlTemplate);
			RuleExpression imageTemplate = new RuleExpression();
			// %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_pc_detail?$cp_790x720$&$layer_2_src="));
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

//		for (int i = 0; i < 10; i++) {
//			// 非主商品 （参数图）
//			RuleExpression imageTemplate = new RuleExpression();
//			String htmlTemplate = String.format(C_TEMPLATE_IMG_790, "http://s7d5.scene7.com/is/image/sneakerhead/champion%5Fxq%5Fpc%5Fcolor?$cp%5F790x720$" +
//					"&$layer_2_src=%s" +
//					"&$text1=%s" +
//					"&$text2=%s");
//			imageTemplate.addRuleWord(new TextWord(htmlTemplate));
//
//			// 参数imageParams
//			List<RuleExpression> imageParams = new ArrayList<>();
//			// 参数
//			{
//				{
//					RuleExpression ruleExpression = new RuleExpression();
//
//					// 非主商品第一张图
//					RuleExpression subHtmlTemplate = new RuleExpression();
//					subHtmlTemplate.addRuleWord(new TextWord(""));
//
//					RuleExpression subImageTemplate = new RuleExpression();
//					subImageTemplate.addRuleWord(new TextWord("%s"));
//
//					RuleExpression subImageType = new RuleExpression();
//					subImageType.addRuleWord(new TextWord(C_商品图片));
//
//					RuleExpression subCodeIndex = new RuleExpression();
//					subCodeIndex.addRuleWord(new TextWord(String.valueOf(i))); // 第n个code
//
//					RuleExpression subImageIndex = new RuleExpression();
//					subImageIndex.addRuleWord(new TextWord("0"));
//
//					CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(subHtmlTemplate, subImageTemplate, subImageType, null, null, null, subCodeIndex, subImageIndex);
//
//					ruleExpression.addRuleWord(new CustomWord(word));
//					imageParams.add(ruleExpression);
//				}
//				{
//					RuleExpression ruleExpression = new RuleExpression();
//					ruleExpression.addRuleWord(new SubCodeWord(i, "colorDiff"));
//					imageParams.add(ruleExpression);
//				}
//				{
//					RuleExpression ruleExpression = new RuleExpression();
//					ruleExpression.addRuleWord(new SubCodeWord(i, "code"));
//					imageParams.add(ruleExpression);
//				}
//			}
//
//			RuleExpression useCmsBtImageTemplate = new RuleExpression();
//			useCmsBtImageTemplate.addRuleWord(new TextWord("true"));
//
//			CustomWordValueImageWithParam word = new CustomWordValueImageWithParam(imageTemplate, imageParams, useCmsBtImageTemplate, null);
//			ruleRoot.addRuleWord(new CustomWord(word));
//
//		}

		for (int i = 0; i < 10; i++) {
			{

				// 非主商品第一张图
				RuleExpression subHtmlTemplate = new RuleExpression();
				subHtmlTemplate.addRuleWord(new TextWord(""));

				RuleExpression subImageTemplate = new RuleExpression();
				CustomWordValueImageWithParam wordParam;
				{
					// 非主商品 （参数图）
					RuleExpression imageTemplate = new RuleExpression();
					String htmlTemplate = String.format(C_TEMPLATE_IMG_790, "http://s7d5.scene7.com/is/image/sneakerhead/champion%%5Fxq%%5Fpc%%5Fcolor?$cp%%5F790x720$" +
							"&$layer_2_src=%s" +
							"&$text1=%s" +
							"&$text2=%s");
					imageTemplate.addRuleWord(new TextWord(htmlTemplate));

					// 参数imageParams
					List<RuleExpression> imageParams = new ArrayList<>();
					// 参数
					{

						{
							RuleExpression ruleExpression = new RuleExpression();
							ruleExpression.addRuleWord(new TextWord("%s"));
							imageParams.add(ruleExpression);
						}
						{
							RuleExpression ruleExpression = new RuleExpression();
							ruleExpression.addRuleWord(new SubCodeWord(i, "colorDiff"));
							imageParams.add(ruleExpression);
						}
						{
							RuleExpression ruleExpression = new RuleExpression();
							ruleExpression.addRuleWord(new SubCodeWord(i, "code"));
							imageParams.add(ruleExpression);
						}
					}

					RuleExpression useCmsBtImageTemplate = new RuleExpression();
					useCmsBtImageTemplate.addRuleWord(new TextWord("true"));

					wordParam = new CustomWordValueImageWithParam(imageTemplate, imageParams, useCmsBtImageTemplate, null);
				}
				subImageTemplate.addRuleWord(new CustomWord(wordParam));

				RuleExpression subImageType = new RuleExpression();
				subImageType.addRuleWord(new TextWord(C_商品图片));

				RuleExpression subCodeIndex = new RuleExpression();
				subCodeIndex.addRuleWord(new TextWord(String.valueOf(i))); // 第n个code

				RuleExpression subImageIndex = new RuleExpression();
				subImageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(subHtmlTemplate, subImageTemplate, subImageType, null, null, null, subCodeIndex, subImageIndex);

				ruleRoot.addRuleWord(new CustomWord(word));
			}


		}

		{
			// 店铺介绍图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("5"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			RuleExpression useOriUrl = new RuleExpression();
			useOriUrl.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, useOriUrl, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		return ruleRoot;
	}

	private RuleExpression doDict_无线描述() {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();
//		{
//			// start
//			String kv = "{\"wireless_desc\":{";
//			TextWord word = new TextWord(kv);
//			ruleRoot.addRuleWord(word);
//		}
//
//		{
//			// item_info 商品信息
//			String kv = "\"item_info\":{\"item_info_enable\":\"true\"},";
//			TextWord word = new TextWord(kv);
//			ruleRoot.addRuleWord(word);
//		}
//
//		{
//			// coupon 优惠
//			String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"342115\"},";
//			TextWord word = new TextWord(kv);
//			ruleRoot.addRuleWord(word);
//		}
//
//		{
//			// hot_recommanded 同店推荐
//			String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"341911\"},";
//			TextWord word = new TextWord(kv);
//			ruleRoot.addRuleWord(word);
//		}
//
//		{
//			// shop_discount 店铺活动
//			String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"342160\"},";
//			TextWord word = new TextWord(kv);
//			ruleRoot.addRuleWord(word);
//		}
//
//		// 图片全是DictWord(总共20张图片,但最后5张不用设置图片)
//		{
//			// item_picture 商品图片
//			String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
//			TextWord word = new TextWord(kv);
//			ruleRoot.addRuleWord(word);
//
//			{
//				// 第一张, 固定图片:价格解释
//				String strImgJiage = "https://img.alicdn.com/imgextra/i3/2641101981/TB24I4SXF6AQeBjSZFFXXaiFpXa_!!2641101981.jpg";
//				do处理无线端20张图片(0, ruleRoot, new TextWord(strImgJiage));
//			}
//
//			{
//				// 文字描述(空两个位置)
//				{
//					RuleExpression ruleExpressionField = new RuleExpression();
//					ruleExpressionField.addRuleWord(new TextWord("longDesEn"));
//					RuleExpression ruleExpressionFontSize = new RuleExpression();
//					ruleExpressionFontSize.addRuleWord(new TextWord("23"));
//					RuleExpression ruleExpressionOneLineBit = new RuleExpression();
//					ruleExpressionOneLineBit.addRuleWord(new TextWord("50"));
//					RuleExpression ruleExpressionSectionSize = new RuleExpression();
//					ruleExpressionSectionSize.addRuleWord(new TextWord("5"));
//					CustomWordValueGetDescImage img = new CustomWordValueGetDescImage(ruleExpressionField, null, null, null, ruleExpressionSectionSize, ruleExpressionFontSize, ruleExpressionOneLineBit);
//					do处理无线端20张图片(1, ruleRoot, new CustomWord(img));
//				}
//				{
//					RuleExpression ruleExpressionField = new RuleExpression();
//					ruleExpressionField.addRuleWord(new TextWord("longDesCn"));
//					RuleExpression ruleExpressionFontSize = new RuleExpression();
//					ruleExpressionFontSize.addRuleWord(new TextWord("23"));
//					RuleExpression ruleExpressionOneLineBit = new RuleExpression();
//					ruleExpressionOneLineBit.addRuleWord(new TextWord("60"));
//					RuleExpression ruleExpressionSectionSize = new RuleExpression();
//					ruleExpressionSectionSize.addRuleWord(new TextWord("8"));
//					CustomWordValueGetDescImage img = new CustomWordValueGetDescImage(ruleExpressionField, null, null, null, ruleExpressionSectionSize, ruleExpressionFontSize, ruleExpressionOneLineBit);
//					do处理无线端20张图片(2, ruleRoot, new CustomWord(img));
//				}
//			}
//
//			{
//				// 参数图(一张)
//				// 测试临时
////				String strImgJiage = "https://img.alicdn.com/imgextra/i3/2641101981/TB24I4SXF6AQeBjSZFFXXaiFpXa_!!2641101981.jpg";
////				do处理无线端20张图片(3, ruleRoot, new TextWord(strImgJiage));
//			}
//
//			{
//				// 无线商品图片(五张)
//				do处理无线端20张图片(4, ruleRoot, new DictWord("无线商品图片-1"));
//				do处理无线端20张图片(5, ruleRoot, new DictWord("无线商品图片-2"));
//				do处理无线端20张图片(6, ruleRoot, new DictWord("无线商品图片-3"));
//				do处理无线端20张图片(7, ruleRoot, new DictWord("无线商品图片-4"));
//				do处理无线端20张图片(8, ruleRoot, new DictWord("无线商品图片-5"));
//			}
//			{
//				// 无线自定义图片(五张)
//				do处理无线端20张图片(9, ruleRoot, new DictWord("无线自定义图片-1"));
//				do处理无线端20张图片(10, ruleRoot, new DictWord("无线自定义图片-2"));
//				do处理无线端20张图片(11, ruleRoot, new DictWord("无线自定义图片-3"));
//				do处理无线端20张图片(12, ruleRoot, new DictWord("无线自定义图片-4"));
//				do处理无线端20张图片(13, ruleRoot, new DictWord("无线自定义图片-5"));
//			}
//			{
//				// 无线固定图(五张)
//				do处理无线端20张图片(14, ruleRoot, new DictWord("无线固定图-1"));
//				do处理无线端20张图片(15, ruleRoot, new DictWord("无线固定图-2"));
//				do处理无线端20张图片(16, ruleRoot, new DictWord("无线固定图-3"));
//				do处理无线端20张图片(17, ruleRoot, new DictWord("无线固定图-4"));
//				do处理无线端20张图片(18, ruleRoot, new DictWord("无线固定图-5"));
//			}
//
//
//			// end
//			String endStr = "}";
//			TextWord endWord = new TextWord(endStr);
//			ruleRoot.addRuleWord(endWord);
//		}
//
//		{
//			// end
//			String kv = "}}";
//			TextWord word = new TextWord(kv);
//			ruleRoot.addRuleWord(word);
//		}
//
		return ruleRoot;
	}

	/**
	 * 根据index生成天猫商品图
	 *
	 * @param templateUrl 模板url
	 * @param index String  自定义图片index
	 * @return RuleExpression 无线商品图片字典
	 */
	private RuleExpression doDict_商品图片(String templateUrl, String index) {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		{
			// 商品图
			CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord("%s"));
			userParam.setHtmlTemplate(htmlTemplate);
			RuleExpression imageTemplate = new RuleExpression();
			// %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
			imageTemplate.addRuleWord(new TextWord(templateUrl));
			userParam.setImageTemplate(imageTemplate);
			RuleExpression imageIndex = new RuleExpression();
			imageIndex.addRuleWord(new TextWord(String.valueOf(index)));
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


}