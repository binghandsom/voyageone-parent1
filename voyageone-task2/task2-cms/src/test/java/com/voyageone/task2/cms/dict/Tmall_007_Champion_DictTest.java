package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/5/20.
 */

@SuppressWarnings("ALL")
public class Tmall_007_Champion_DictTest extends BaseDictTest {

	@Test
	public void startupTest() {

		System.out.println("=====================================");
		{
			// 产品图片
			String templateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x1200_1200x?$1200x1200$&$product=%s";
			for (int i = 0; i < 5; i++) {
				doCreateJson("产品图片-" + ( i + 1 ), false, doDict_商品图片(templateUrl, String.valueOf(i)));
			}
		}
		{
			// 商品图片
			String templateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/champion_zhutu_moban?$900x900$&$layer_1_src=%s";
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
		doCreateJson("无线描述_全自定义", false, doDict_无线描述_全自定义());
		doCreateJson("无线描述_系列通用", false, doDict_无线描述_系列通用());
		doCreateJson("无线描述_basic系列", false, doDict_无线描述_basic系列());

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
			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_pc_detail?$cp_790x720$&$layer_2_src=%s"));
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
			{
				// 前缀
				String html = "<div><img src=\"";
				ruleRoot.addRuleWord(new TextWord(html));
			}

			// 商品信息模板
			RuleExpression imageTemplate = new RuleExpression();
			String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_pc_info?$cp_790x265$" +
					"&$text01=%s" + // 主商品的中文商品名称
					"&$text02=%s" + // model名
					"&$text03=%s" + // 自定义属性商品风格
					"&$text04=%s" + // 主商品的中文材质
					"&$text05=%s"; // 中文长描述
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
//				{
//					RuleExpression ruleExpression = new RuleExpression();
//					ruleExpression.addRuleWord(new FeedCnWord("style"));
//					imageParams.add(ruleExpression);
//				}
				{ // 最初yaoshasha想要用feed的style， 后来说是要用中文短描述
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("shortDesCn"));
					imageParams.add(ruleExpression);
				}
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("materialCn"));
					imageParams.add(ruleExpression);
				}
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new MasterWord("longDesCn"));
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

		{
			// 所有自定义图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

//			RuleExpression imageTemplate = new RuleExpression();
//			imageTemplate.addRuleWord(new TextWord(""));
			RuleExpression imageTemplate = null;

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord(C_移动端自定义图片));

			RuleExpression useOriUrl = new RuleExpression();
			useOriUrl.addRuleWord(new TextWord("1"));

			CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 固定图片 - DISPLAY商品展示
			ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i4/2533968112/TB2jcgRbUlnpuFjSZFjXXXTaVXa_!!2533968112.jpg")));
		}

		for (int i = 1; i < 5; i++) {
			// 商品展示模板
			CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));
			userParam.setHtmlTemplate(htmlTemplate);
			RuleExpression imageTemplate = new RuleExpression();
			// %5F -> _ 或者 %%5F  (不该会报 convention = F 的异常)
			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_pc_detail?$cp_790x720$&$layer_2_src=%s"));
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

//		for (int i = 0; i < 10; i++) {
//			{
//
//				// 非主商品第一张图
//				RuleExpression subHtmlTemplate = new RuleExpression();
//				subHtmlTemplate.addRuleWord(new TextWord(""));
//
//				RuleExpression subImageTemplate = new RuleExpression();
//				CustomWordValueImageWithParam wordParam;
//				{
//					// 非主商品 （参数图）
//					RuleExpression imageTemplate = new RuleExpression();
//					String htmlTemplate = String.format(C_TEMPLATE_IMG_790, "http://s7d5.scene7.com/is/image/sneakerhead/champion%%5Fxq%%5Fpc%%5Fcolor?$cp%%5F790x720$" +
//							"&$layer_2_src=%s" +
//							"&$text1=%s" +
//							"&$text2=%s");
//					imageTemplate.addRuleWord(new TextWord(htmlTemplate));
//
//					// 参数imageParams
//					List<RuleExpression> imageParams = new ArrayList<>();
//					// 参数
//					{
//
//						{
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new TextWord("%s"));
//							imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new SubCodeWord(i, "codeDiff"));
//							imageParams.add(ruleExpression);
//						}
//						{
//							RuleExpression ruleExpression = new RuleExpression();
//							ruleExpression.addRuleWord(new SubCodeWord(i, "code"));
//							imageParams.add(ruleExpression);
//						}
//					}
//
//					RuleExpression useCmsBtImageTemplate = new RuleExpression();
//					useCmsBtImageTemplate.addRuleWord(new TextWord("true"));
//
//					wordParam = new CustomWordValueImageWithParam(imageTemplate, imageParams, useCmsBtImageTemplate, null);
//				}
//				subImageTemplate.addRuleWord(new CustomWord(wordParam));
//
//				RuleExpression subImageType = new RuleExpression();
//				subImageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression subCodeIndex = new RuleExpression();
//				subCodeIndex.addRuleWord(new TextWord(String.valueOf(i))); // 第n个code
//
//				RuleExpression subImageIndex = new RuleExpression();
//				subImageIndex.addRuleWord(new TextWord("0"));
//
//				CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(subHtmlTemplate, subImageTemplate, subImageType, null, null, null, subCodeIndex, subImageIndex);
//
//				ruleRoot.addRuleWord(new CustomWord(word));
//			}
//
//
//		}

		{
			// 非主商品第一张图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			RuleExpression imageTemplate = new RuleExpression();
			imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_pc_color?$cp_790x720$&$layer_2_src=%s&$text1=%s&$text2=%s"));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord(C_商品图片));

			RuleExpression codeIndex = new RuleExpression();
			codeIndex.addRuleWord(new TextWord("-1")); // 所有非主商品

			RuleExpression imageIndex = new RuleExpression();
			imageIndex.addRuleWord(new TextWord("0")); // 第一张图
			// 20160116 姚莎莎新增需求， PC端描述里， 非主商品的所有图片都要显示 START
			imageIndex.addRuleWord(new TextWord("1"));
			imageIndex.addRuleWord(new TextWord("2"));
			imageIndex.addRuleWord(new TextWord("3"));
			imageIndex.addRuleWord(new TextWord("4"));
			imageIndex.addRuleWord(new TextWord("5"));
			imageIndex.addRuleWord(new TextWord("6"));
			imageIndex.addRuleWord(new TextWord("7"));
			imageIndex.addRuleWord(new TextWord("8"));
			// 20160116 姚莎莎新增需求， PC端描述里， 非主商品的所有图片都要显示 END

			RuleExpression imageParamWord = new RuleExpression();
			imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
			imageParamWord.addRuleWord(new TextWord("code")); // 参数 code

			CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
			ruleRoot.addRuleWord(new CustomWord(allImagesWord));
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
			String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"1305964\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// hot_recommanded 同店推荐
			String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

			// 同店推荐的field_id
			MasterWord masterWord = new MasterWord("hot_recommanded_id");
			ruleRoot.addRuleWord(masterWord);

			kv = "\"},";
			word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// shop_discount 店铺活动
			String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"1305997\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// item_picture 商品图片
			String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

			{
				// 第1张 品牌故事图
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(0, ruleRoot, new CustomWord(getCommonImagesWord));
			}

			{
				// 第2张, 参数图片（商品信息模板）
				RuleExpression imageTemplate = new RuleExpression();
				String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_info?$cp_750x1110$&$layer_8_src=%s&$text1=%s&$text2=%s&$text3=%s&$text4=%s&$text5=%s";
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
					// 商品名称：主商品的中文商品名称
					// 商品货号：model名
					// 商品材质：主商品的中文材质
					// 商品风格：自定义属性商品风格
					// 商品特点：中文长描述
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
//					{
//						RuleExpression ruleExpression = new RuleExpression();
//						ruleExpression.addRuleWord(new FeedCnWord("style"));
//						imageParams.add(ruleExpression);
//					}
					{ // 最初yaoshasha想要用feed的style， 后来说是要用中文短描述
						RuleExpression ruleExpression = new RuleExpression();
						ruleExpression.addRuleWord(new MasterWord("shortDesCn"));
						imageParams.add(ruleExpression);
					}
					{
						RuleExpression ruleExpression = new RuleExpression();
						ruleExpression.addRuleWord(new MasterWord("longDesCn"));
						imageParams.add(ruleExpression);
					}
				}

				CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
				do处理无线端20张图片(1, ruleRoot, new CustomWord(imagesWithParamWord));
			}

			for (int i = 2; i < 10; i++) {
				// 八张自定义图
				do处理无线端20张图片(i, ruleRoot, new DictWord("无线自定义图片-" + (i - 1))); // 原图，参照target
			}

			{
				// 固定图片 - DISPLAY商品展示
				String strImg = "https://img.alicdn.com/imgextra/i4/2533968112/TB2jcgRbUlnpuFjSZFjXXXTaVXa_!!2533968112.jpg";
				do处理无线端20张图片(10, ruleRoot, new TextWord(strImg));
			}

			{
				// 主商品
				for (int i = 1; i < 5; i++){
					// 主商品的第2-5张图
					CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
					RuleExpression imageIndex = new RuleExpression();
					imageIndex.addRuleWord(new TextWord(String.valueOf(i)));   // 第2-5张商品图片
					userParam.setImageIndex(imageIndex);
					RuleExpression img_imageType = new RuleExpression();
					img_imageType.addRuleWord(new TextWord(C_商品图片));
					userParam.setImageType(img_imageType);

					RuleExpression imageTemplate = new RuleExpression();
					imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_detail?$cp_750x720$&$layer_1_src=%s"));
					userParam.setImageTemplate(imageTemplate);

					CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
					wordValueGetMainProductImages.setUserParam(userParam);

					do处理无线端20张图片(10 + i, ruleRoot, new CustomWord(wordValueGetMainProductImages));
				}
			}

//			{
//				// 参数图片（商品展示模板1）主商品的主图-1,2
//				RuleExpression imageTemplate = new RuleExpression();
//				String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_detail?$cp_750x1450$&$layer_1_src=%s&$layer_2_src=%s";
//				imageTemplate.addRuleWord(new TextWord(htmlTemplate));
//
//				// 设置参数imageParams的值
//				List<RuleExpression> imageParams = new ArrayList<>();
//				{
//					// 第一个参数是product_id(GetMainProductImages)
//					CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
//					RuleExpression imageIndex = new RuleExpression();
//					imageIndex.addRuleWord(new TextWord("0"));   // 第一张商品图片
//					userParam.setImageIndex(imageIndex);
//					RuleExpression img_imageType = new RuleExpression();
//					img_imageType.addRuleWord(new TextWord(C_商品图片));
//					userParam.setImageType(img_imageType);
//
//					CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
//					wordValueGetMainProductImages.setUserParam(userParam);
//
//					RuleExpression imgWord = new RuleExpression();
//					imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
//					imageParams.add(imgWord);
//				}
//				{
//					// 第二个参数是product_id(GetMainProductImages)
//					CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
//					RuleExpression imageIndex = new RuleExpression();
//					imageIndex.addRuleWord(new TextWord("1"));   // 第二张商品图片
//					userParam.setImageIndex(imageIndex);
//					RuleExpression img_imageType = new RuleExpression();
//					img_imageType.addRuleWord(new TextWord(C_商品图片));
//					userParam.setImageType(img_imageType);
//
//					CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
//					wordValueGetMainProductImages.setUserParam(userParam);
//
//					RuleExpression imgWord = new RuleExpression();
//					imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
//					imageParams.add(imgWord);
//				}
//
//				CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
//				do处理无线端20张图片(13, ruleRoot, new CustomWord(imagesWithParamWord));
//			}
//
//			{
//				// 第15张, 参数图片（商品展示模板1）主商品的主图-3,4
//				RuleExpression imageTemplate = new RuleExpression();
//				String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_detail?$cp_750x1450$&$layer_1_src=%s&$layer_2_src=%s";
//				imageTemplate.addRuleWord(new TextWord(htmlTemplate));
//
//				// 设置参数imageParams的值
//				List<RuleExpression> imageParams = new ArrayList<>();
//				{
//					// 第一个参数是product_id(GetMainProductImages)
//					CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
//					RuleExpression imageIndex = new RuleExpression();
//					imageIndex.addRuleWord(new TextWord("2"));   // 第三张商品图片
//					userParam.setImageIndex(imageIndex);
//					RuleExpression img_imageType = new RuleExpression();
//					img_imageType.addRuleWord(new TextWord(C_商品图片));
//					userParam.setImageType(img_imageType);
//
//					CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
//					wordValueGetMainProductImages.setUserParam(userParam);
//
//					RuleExpression imgWord = new RuleExpression();
//					imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
//					imageParams.add(imgWord);
//				}
//				{
//					// 第二个参数是product_id(GetMainProductImages)
//					CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
//					RuleExpression imageIndex = new RuleExpression();
//					imageIndex.addRuleWord(new TextWord("3"));   // 第四张商品图片
//					userParam.setImageIndex(imageIndex);
//					RuleExpression img_imageType = new RuleExpression();
//					img_imageType.addRuleWord(new TextWord(C_商品图片));
//					userParam.setImageType(img_imageType);
//
//					CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
//					wordValueGetMainProductImages.setUserParam(userParam);
//
//					RuleExpression imgWord = new RuleExpression();
//					imgWord.addRuleWord(new CustomWord(wordValueGetMainProductImages));
//					imageParams.add(imgWord);
//				}
//
//				CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
//				do处理无线端20张图片(14, ruleRoot, new CustomWord(imagesWithParamWord));
//			}

			{
				// 第16张, 参数图片（商品展示模板2）非主商品的第一张图
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord(C_商品图片));

				RuleExpression codeIndex = new RuleExpression();
				codeIndex.addRuleWord(new TextWord("0")); // 第一个非主商品

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0")); // 第一张图

				RuleExpression imageParamWord = new RuleExpression();
				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code

				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
				do处理无线端20张图片(15, ruleRoot, new CustomWord(allImagesWord));
			}
			{
				// 第17张, 参数图片（商品展示模板2）非主商品的第一张图
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord(C_商品图片));

				RuleExpression codeIndex = new RuleExpression();
				codeIndex.addRuleWord(new TextWord("1")); // 第二个非主商品

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0")); // 第一张图

				RuleExpression imageParamWord = new RuleExpression();
				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code

				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
				do处理无线端20张图片(16, ruleRoot, new CustomWord(allImagesWord));
			}
			{
				// 第18张, 参数图片（商品展示模板2）非主商品的第一张图
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord(C_商品图片));

				RuleExpression codeIndex = new RuleExpression();
				codeIndex.addRuleWord(new TextWord("2")); // 第三个非主商品

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0")); // 第一张图

				RuleExpression imageParamWord = new RuleExpression();
				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code

				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
				do处理无线端20张图片(17, ruleRoot, new CustomWord(allImagesWord));
			}
			{
				// 第19张, 参数图片（商品展示模板2）非主商品的第一张图
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord(C_商品图片));

				RuleExpression codeIndex = new RuleExpression();
				codeIndex.addRuleWord(new TextWord("3")); // 第四个非主商品

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0")); // 第一张图

				RuleExpression imageParamWord = new RuleExpression();
				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code

				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
				do处理无线端20张图片(18, ruleRoot, new CustomWord(allImagesWord));
			}

			{
				// 第20张 店铺介绍图
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(19, ruleRoot, new CustomWord(getCommonImagesWord));
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

	private RuleExpression doDict_无线描述_全自定义() {
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
			String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"1305964\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// hot_recommanded 同店推荐
			String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

			// 同店推荐的field_id
			MasterWord masterWord = new MasterWord("hot_recommanded_id");
			ruleRoot.addRuleWord(masterWord);

			kv = "\"},";
			word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// shop_discount 店铺活动
			String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"1305997\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// item_picture 商品图片
			String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

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
//
		return ruleRoot;
	}

	private RuleExpression doDict_无线描述_系列通用() {
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
			String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"1305964\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// hot_recommanded 同店推荐
			String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

			// 同店推荐的field_id
			MasterWord masterWord = new MasterWord("hot_recommanded_id");
			ruleRoot.addRuleWord(masterWord);

			kv = "\"},";
			word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// shop_discount 店铺活动
			String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"1305997\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// item_picture 商品图片
			String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

			{
				// 第1张 品牌故事图
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(0, ruleRoot, new CustomWord(getCommonImagesWord));
			}

			{
				// 第2张, 参数图片（商品信息模板）
				RuleExpression imageTemplate = new RuleExpression();
				String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_info?$cp_750x1110$&$layer_8_src=%s&$text1=%s&$text2=%s&$text3=%s&$text4=%s&$text5=%s";
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
					// 商品名称：主商品的中文商品名称
					// 商品货号：model名
					// 商品材质：主商品的中文材质
					// 商品风格：自定义属性商品风格
					// 商品特点：中文长描述
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
//					{
//						RuleExpression ruleExpression = new RuleExpression();
//						ruleExpression.addRuleWord(new FeedCnWord("style"));
//						imageParams.add(ruleExpression);
//					}
					{ // 最初yaoshasha想要用feed的style， 后来说是要用中文短描述
						RuleExpression ruleExpression = new RuleExpression();
						ruleExpression.addRuleWord(new MasterWord("shortDesCn"));
						imageParams.add(ruleExpression);
					}
					{
						RuleExpression ruleExpression = new RuleExpression();
						ruleExpression.addRuleWord(new MasterWord("longDesCn"));
						imageParams.add(ruleExpression);
					}
				}

				CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
				do处理无线端20张图片(1, ruleRoot, new CustomWord(imagesWithParamWord));
			}

			for (int i = 2; i < 4; i++) {
				// 两张自定义图
				do处理无线端20张图片(i, ruleRoot, new DictWord("无线自定义图片-" + (i - 1))); // 原图，参照target
			}

			{
				// 固定图片 - DISPLAY商品展示
				String strImg = "https://img.alicdn.com/imgextra/i4/2533968112/TB2jcgRbUlnpuFjSZFjXXXTaVXa_!!2533968112.jpg";
				do处理无线端20张图片(4, ruleRoot, new TextWord(strImg));
			}

			{
				// 主商品
				for (int i = 1; i < 6; i++){
					// 主商品的第2-6张图
					CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
					RuleExpression imageIndex = new RuleExpression();
					imageIndex.addRuleWord(new TextWord(String.valueOf(i)));   // 第2-6张商品图片
					userParam.setImageIndex(imageIndex);
					RuleExpression img_imageType = new RuleExpression();
					img_imageType.addRuleWord(new TextWord(C_商品图片));
					userParam.setImageType(img_imageType);

					RuleExpression imageTemplate = new RuleExpression();
					imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_detail?$cp_750x720$&$layer_1_src=%s"));
					userParam.setImageTemplate(imageTemplate);

					CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
					wordValueGetMainProductImages.setUserParam(userParam);

					do处理无线端20张图片(4 + i, ruleRoot, new CustomWord(wordValueGetMainProductImages));
				}
			}

			for (int i = 10; i < 13; i++) {
				// 两张自定义图
				do处理无线端20张图片(i, ruleRoot, new DictWord("非主商品1自拍图片-" + (i - 9))); // 原图，参照target
			}
			for (int i = 13; i < 16; i++) {
				// 两张自定义图
				do处理无线端20张图片(i, ruleRoot, new DictWord("非主商品2自拍图片-" + (i - 12))); // 原图，参照target
			}
			for (int i = 16; i < 19; i++) {
				// 两张自定义图
				do处理无线端20张图片(i, ruleRoot, new DictWord("非主商品3自拍图片-" + (i - 15))); // 原图，参照target
			}

//			{
//				// 第11张, 参数图片（商品展示模板2）非主商品1的第一张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("0")); // 第一个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("0")); // 第一张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(10, ruleRoot, new CustomWord(allImagesWord));
//			}
//			{
//				// 第12张, 参数图片（商品展示模板2）非主商品1的第二张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("0")); // 第一个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("1")); // 第二张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(11, ruleRoot, new CustomWord(allImagesWord));
//			}
//			{
//				// 第13张, 参数图片（商品展示模板2）非主商品1的第三张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("0")); // 第一个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("2")); // 第三张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(12, ruleRoot, new CustomWord(allImagesWord));
//			}
//			{
//				// 第14张, 参数图片（商品展示模板2）非主商品2的第一张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("1")); // 第二个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("0")); // 第一张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(13, ruleRoot, new CustomWord(allImagesWord));
//			}
//			{
//				// 第15张, 参数图片（商品展示模板2）非主商品2的第二张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("1")); // 第二个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("1")); // 第二张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(14, ruleRoot, new CustomWord(allImagesWord));
//			}
//			{
//				// 第16张, 参数图片（商品展示模板2）非主商品2的第三张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("1")); // 第二个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("2")); // 第三张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(15, ruleRoot, new CustomWord(allImagesWord));
//			}
//			{
//				// 第17张, 参数图片（商品展示模板2）非主商品3的第一张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("2")); // 第三个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("0")); // 第一张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(16, ruleRoot, new CustomWord(allImagesWord));
//			}
//			{
//				// 第18张, 参数图片（商品展示模板2）非主商品3的第二张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("2")); // 第三个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("1")); // 第二张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(17, ruleRoot, new CustomWord(allImagesWord));
//			}
//			{
//				// 第19张, 参数图片（商品展示模板2）非主商品3的第三张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord("2")); // 第三个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("2")); // 第三张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(18, ruleRoot, new CustomWord(allImagesWord));
//			}
			{
				// 第20张 店铺介绍图
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(19, ruleRoot, new CustomWord(getCommonImagesWord));
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
//
		return ruleRoot;
	}
	private RuleExpression doDict_无线描述_basic系列() {
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
			String kv = "\"coupon\":{\"coupon_enable\":\"true\",\"coupon_id\":\"1305964\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// hot_recommanded 同店推荐
			String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"true\",\"hot_recommanded_id\":\"";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

			// 同店推荐的field_id
			MasterWord masterWord = new MasterWord("hot_recommanded_id");
			ruleRoot.addRuleWord(masterWord);

			kv = "\"},";
			word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// shop_discount 店铺活动
			String kv = "\"shop_discount\":{\"shop_discount_enable\":\"true\",\"shop_discount_id\":\"1305997\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// item_picture 商品图片
			String kv = "\"item_picture\":{\"item_picture_enable\":\"true\"";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

			{
				// 第1张 品牌故事图
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("3"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(0, ruleRoot, new CustomWord(getCommonImagesWord));
			}

			{
				// 第2张, 参数图片（商品信息模板）
				RuleExpression imageTemplate = new RuleExpression();
				String htmlTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_info?$cp_750x1110$&$layer_8_src=%s&$text1=%s&$text2=%s&$text3=%s&$text4=%s&$text5=%s";
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
					// 商品名称：主商品的中文商品名称
					// 商品货号：model名
					// 商品材质：主商品的中文材质
					// 商品风格：自定义属性商品风格
					// 商品特点：中文长描述
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
//					{
//						RuleExpression ruleExpression = new RuleExpression();
//						ruleExpression.addRuleWord(new FeedCnWord("style"));
//						imageParams.add(ruleExpression);
//					}
					{ // 最初yaoshasha想要用feed的style， 后来说是要用中文短描述
						RuleExpression ruleExpression = new RuleExpression();
						ruleExpression.addRuleWord(new MasterWord("shortDesCn"));
						imageParams.add(ruleExpression);
					}
					{
						RuleExpression ruleExpression = new RuleExpression();
						ruleExpression.addRuleWord(new MasterWord("longDesCn"));
						imageParams.add(ruleExpression);
					}
				}

				CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
				do处理无线端20张图片(1, ruleRoot, new CustomWord(imagesWithParamWord));
			}

			for (int i = 2; i < 4; i++) {
				// 两张自定义图
				do处理无线端20张图片(i, ruleRoot, new DictWord("无线自定义图片-" + (i - 1))); // 原图，参照target
			}

			{
				// 固定图片 - DISPLAY商品展示
				String strImg = "https://img.alicdn.com/imgextra/i4/2533968112/TB2jcgRbUlnpuFjSZFjXXXTaVXa_!!2533968112.jpg";
				do处理无线端20张图片(4, ruleRoot, new TextWord(strImg));
			}

			{
				// 主商品
				for (int i = 1; i < 5; i++){
					// 主商品的第2-5张图
					CustomModuleUserParamGetMainPrductImages userParam = new CustomModuleUserParamGetMainPrductImages();
					RuleExpression imageIndex = new RuleExpression();
					imageIndex.addRuleWord(new TextWord(String.valueOf(i)));   // 第2-5张商品图片
					userParam.setImageIndex(imageIndex);
					RuleExpression img_imageType = new RuleExpression();
					img_imageType.addRuleWord(new TextWord(C_商品图片));
					userParam.setImageType(img_imageType);

					RuleExpression imageTemplate = new RuleExpression();
					imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_detail?$cp_750x720$&$layer_1_src=%s"));
					userParam.setImageTemplate(imageTemplate);

					CustomWordValueGetMainProductImages wordValueGetMainProductImages = new CustomWordValueGetMainProductImages();
					wordValueGetMainProductImages.setUserParam(userParam);

					do处理无线端20张图片(4 + i, ruleRoot, new CustomWord(wordValueGetMainProductImages));
				}
			}

			for (int i = 9; i < 19; i++) {
				// 两张自定义图
				do处理无线端20张图片(i, ruleRoot, new DictWord("非主商品" + (i - 8) + "自拍图片-1")); // 原图，参照target
			}

//			for (int i=0;i< 10;i++) {
//				// 第10+i张, 参数图片（商品展示模板2）非主商品的第一张图
//				RuleExpression imageTemplate = new RuleExpression();
//				imageTemplate.addRuleWord(new TextWord("http://s7d5.scene7.com/is/image/sneakerhead/champion_xq_app_color?$cp_750x780$&$layer_2_src=%s&$text1=%s&$text2=%s"));
//
//				RuleExpression imageType = new RuleExpression();
//				imageType.addRuleWord(new TextWord(C_商品图片));
//
//				RuleExpression codeIndex = new RuleExpression();
//				codeIndex.addRuleWord(new TextWord(String.valueOf(i))); // 第i个非主商品
//
//				RuleExpression imageIndex = new RuleExpression();
//				imageIndex.addRuleWord(new TextWord("0")); // 第一张图
//
//				RuleExpression imageParamWord = new RuleExpression();
//				imageParamWord.addRuleWord(new TextWord("color")); // 参数 颜色
//				imageParamWord.addRuleWord(new TextWord("code")); // 参数 code
//
//				CustomWordValueGetAllImages allImagesWord = new CustomWordValueGetAllImages(null, imageTemplate, imageType, null, null, null, codeIndex, imageIndex, imageParamWord);
//				do处理无线端20张图片(9 + i, ruleRoot, new CustomWord(allImagesWord));
//			}

			{
				// 第20张 店铺介绍图
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(19, ruleRoot, new CustomWord(getCommonImagesWord));
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
//
		return ruleRoot;
	}

}