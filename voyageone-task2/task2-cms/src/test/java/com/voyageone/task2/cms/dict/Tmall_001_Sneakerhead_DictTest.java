package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/5/20.
 */
public class Tmall_001_Sneakerhead_DictTest extends BaseDictTest {

	@Test
	public void startupTest() {
		// 天猫国际
		doCreateJson("详情页描述", false, doDict_详情页描述(23));
		doCreateJson("无线描述", false, doDict_无线描述(23));

		for (int i = 0; i < 10; i++) {
			doCreateJson("商品标题与图片-" + i, false, doDict_商品标题与图片(23, i));
		}
		for (int i = 0; i < 10; i++) {
			doCreateJson("商品标题与图片-APP-" + i, false, doDict_商品标题与图片_APP(23, i));
		}

		// 天猫
		doCreateJson("详情页描述", false, doDict_详情页描述(20));
		doCreateJson("无线描述", false, doDict_无线描述(20));

		for (int i = 0; i < 10; i++) {
			doCreateJson("商品标题与图片-" + i, false, doDict_商品标题与图片(20, i));
		}
		for (int i = 0; i < 10; i++) {
			doCreateJson("商品标题与图片-APP-" + i, false, doDict_商品标题与图片_APP(20, i));
		}

		// 京东
		doCreateJson("详情页描述", false, doDict_详情页描述(24));
		for (int i = 0; i < 10; i++) {
			doCreateJson("商品标题与图片-" + i, false, doDict_商品标题与图片(24, i));
		}
		// 京东国际
		doCreateJson("详情页描述", false, doDict_详情页描述(26));
		for (int i = 0; i < 10; i++) {
			doCreateJson("商品标题与图片-" + i, false, doDict_商品标题与图片(26, i));
		}

//		doJM(); // 聚美

	}

	private RuleExpression doDict_商品标题与图片(int cartId, int idx) {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		String imageTemplateUrl = "";
		if (cartId == 20) {
			imageTemplateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn20170117-790x1000-pc-1?$sn790x1000$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s";
		} else if (cartId == 23) {
			imageTemplateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn20170112-790x1000-pc-1?$sn790x1000$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s";
		} else if (cartId == 24) {
			imageTemplateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/snJD20170122-990x1253?$990x1253$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s";
		} else if (cartId == 26) {
			imageTemplateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/snJD20170122-990x1253?$990x1253$&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s";
		}

		if (idx == 0) {
			// 主商品
			ruleRoot.addRuleWord(new CommonWord("code"));
			ruleRoot.addRuleWord(new TextWord(" "));
			ruleRoot.addRuleWord(new CommonWord("codeDiff"));
			ruleRoot.addRuleWord(new TextWord(" "));
			ruleRoot.addRuleWord(new CommonWord("color"));

			// 前缀
			ruleRoot.addRuleWord(new TextWord("<img src=\""));

			{
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord(imageTemplateUrl));

				// 设置参数imageParams的值
				List<RuleExpression> imageParams = new ArrayList<>();

				for (int iImgIdx = 0; iImgIdx < 5; iImgIdx++) {
					// 主商品， 第1~5张商品图
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
			ruleRoot.addRuleWord(new SubCodeWord(idx - 1 , "code"));
			ruleRoot.addRuleWord(new TextWord(" "));
			ruleRoot.addRuleWord(new SubCodeWord(idx - 1 , "codeDiff"));
			ruleRoot.addRuleWord(new TextWord(" "));
			ruleRoot.addRuleWord(new SubCodeWord(idx - 1 , "color"));

			// 前缀
			ruleRoot.addRuleWord(new TextWord("<img src=\""));

			{
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord(imageTemplateUrl));

				// 设置参数imageParams的值
				List<RuleExpression> imageParams = new ArrayList<>();

				for (int iImgIdx = 0; iImgIdx < 5; iImgIdx++) {
					// 非主商品， 第1~5张商品图
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

	private RuleExpression doDict_商品标题与图片_APP(int cartId, int idx) {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		String imageTemplateUrl = "";
		if (cartId == 20) {
			imageTemplateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn20170118-750x1050-wuxian-TM?$sn750x1050$&$name=%s&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s";
		} else if (cartId == 23) {
			imageTemplateUrl = "http://s7d5.scene7.com/is/image/sneakerhead/sn20170118-750x1050-wuxian-TMG?$sn750x1050$&$name=%s&$1=%s&$2=%s&$3=%s&$4=%s&$5=%s";
		}

		if (idx == 0) {
			// 主商品
			{
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord(imageTemplateUrl));

				// 设置参数imageParams的值
				List<RuleExpression> imageParams = new ArrayList<>();
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new CommonWord("code"));
					ruleExpression.addRuleWord(new TextWord(" "));
					ruleExpression.addRuleWord(new CommonWord("codeDiff"));
					ruleExpression.addRuleWord(new TextWord(" "));
					ruleExpression.addRuleWord(new CommonWord("color"));
					imageParams.add(ruleExpression);
				}

				for (int iImgIdx = 0; iImgIdx < 5; iImgIdx++) {
					// 主商品， 第1~5张商品图
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

					CustomWordValueGetProductFieldInfo customWord = new CustomWordValueGetProductFieldInfo();
					customWord.setUserParam(customModuleParam);

					ruleExpression.addRuleWord(new CustomWord(customWord));
					imageParams.add(ruleExpression);
				}

				CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
				ruleRoot.addRuleWord(new CustomWord(imagesWithParamWord));
			}

		} else {
			// 非主商品
			{
				RuleExpression imageTemplate = new RuleExpression();
				imageTemplate.addRuleWord(new TextWord(imageTemplateUrl));

				// 设置参数imageParams的值
				List<RuleExpression> imageParams = new ArrayList<>();
				{
					RuleExpression ruleExpression = new RuleExpression();
					ruleExpression.addRuleWord(new SubCodeWord(idx - 1 , "code"));
					ruleExpression.addRuleWord(new TextWord(" "));
					ruleExpression.addRuleWord(new SubCodeWord(idx - 1 , "codeDiff"));
					ruleExpression.addRuleWord(new TextWord(" "));
					ruleExpression.addRuleWord(new SubCodeWord(idx - 1 , "color"));
					imageParams.add(ruleExpression);
				}

				for (int iImgIdx = 0; iImgIdx < 5; iImgIdx++) {
					// 非主商品， 第1~5张商品图
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

					CustomWordValueGetProductFieldInfo customWord = new CustomWordValueGetProductFieldInfo();
					customWord.setUserParam(customModuleParam);

					ruleExpression.addRuleWord(new CustomWord(customWord));
					imageParams.add(ruleExpression);
				}

				CustomWordValueImageWithParam imagesWithParamWord = new CustomWordValueImageWithParam(imageTemplate, imageParams, null, null);
				ruleRoot.addRuleWord(new CustomWord(imagesWithParamWord));
			}

		}

		return ruleRoot;
	}

	private RuleExpression doDict_详情页描述(int cartId) {
		String strPlatformTemplate = "";
		if (cartId == 23 || cartId == 20) {
			strPlatformTemplate = C_TEMPLATE_IMG_790;
		} else if (cartId == 24 || cartId == 26) {
			strPlatformTemplate = C_TEMPLATE_IMG_990;
		}

		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		{
			// 物流图 - 0
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(strPlatformTemplate));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("4"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			RuleExpression imageIndex = new RuleExpression();
			imageIndex.addRuleWord(new TextWord("0"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, imageIndex);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			if (cartId == 23) {
				// 固定图（产品信息）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img.alicdn.com/imgextra/i1/1792368114/TB2hE26cHplpuFjSspiXXcdfFXa_!!1792368114.jpg")));

				// 产品信息style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"color: #acacac;padding: 0 60.0px 80.0px 60.0px;line-height: 16.0pt;background-color: #0c0c0c;font-family: arial;\">"));
			} else if (cartId == 20) {
				// 固定图（产品信息）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img.alicdn.com/imgextra/i1/907029661/TB24yAvc9FjpuFjSspbXXXagVXa-907029661.jpg")));

				// 产品信息style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"color: #acacac;padding: 0 60.0px 80.0px 60.0px;line-height: 16.0pt;font-family: arial;\">"));
			} else if (cartId == 24) {
				// 固定图（产品信息）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "http://img10.360buyimg.com/imgzone/jfs/t3112/87/5788074319/34714/6977432/58844413N02924d00.jpg")));

				// 产品信息style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"width:990px; background-color:#0c0c0c; padding-bottom:5em; font-size:1.2em; color:#acacac;line-height:33px; font-family:arial;\"><p style=\"padding-left:2em; padding-right:2em; text-align:left; word-break:normal;\">"));
			} else if (cartId == 26) {
				// 固定图（产品信息）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img10.360buyimg.com/imgzone/jfs/t3112/87/5788074319/34714/6977432/58844413N02924d00.jpg")));

				// 产品信息style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"width:990px; background-color:#0c0c0c; padding-bottom:5em; font-size:1.2em; color:#acacac;line-height:33px; font-family:arial;\"><p style=\"padding-left:2em; padding-right:2em; text-align:left; word-break:normal;\">"));
			}

			// 英文长描述
			ruleRoot.addRuleWord(new MasterWord("longDesEn"));

			// 产品信息style div end
			if (cartId == 23 || cartId == 20) {
				ruleRoot.addRuleWord(new TextWord("</div>"));
			} else if (cartId == 24 || cartId == 26) {
				ruleRoot.addRuleWord(new TextWord("</p></div>"));
			}

			if (cartId == 23) {
				// 固定图（产品展示）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img.alicdn.com/imgextra/i2/1792368114/TB2mA7jcMxlpuFjy0FoXXa.lXXa_!!1792368114.jpg")));

				// 产品展示style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"font-family: 黑体;font-weight: bold;width: 790.0px;background-color: #0c0c0c;text-align: center;color: #acacac;line-height: 50.0px;\">"));
			} else if (cartId == 20) {
				// 固定图（产品展示）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img.alicdn.com/imgextra/i2/907029661/TB21xUsc3FkpuFjSspnXXb4qFXa-907029661.jpg")));

				// 产品展示style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"font-family: 黑体;font-weight: bold;width: 790.0px;text-align: center;color: #acacac;line-height: 50.0px;\">"));
			} else if (cartId == 24) {
				// 固定图（产品展示）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "http://img10.360buyimg.com/imgzone/jfs/t3916/115/1598662108/32654/5e2b8840/58844413Nfd1ede19.jpg")));

				// 产品展示style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"background-color:#0c0c0c; width:990px;\"><div style=\"font-family:黑体; font-weight:bold; font-size:1.2em; padding-top:3px;color=#acacac; width:990px; background-color:#0c0c0c; text-align: center; color:#acacac; line-height:50px;\">"));
			} else if (cartId == 26) {
				// 固定图（产品展示）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img10.360buyimg.com/imgzone/jfs/t3916/115/1598662108/32654/5e2b8840/58844413Nfd1ede19.jpg")));

				// 产品展示style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"background-color:#0c0c0c; width:990px;\"><div style=\"font-family:黑体; font-weight:bold; font-size:1.2em; padding-top:3px;color=#acacac; width:990px; background-color:#0c0c0c; text-align: center; color:#acacac; line-height:50px;\">"));
			}

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

			// 产品展示style div end
			if (cartId == 23 || cartId == 20) {
				ruleRoot.addRuleWord(new TextWord("</div>"));
			} else if (cartId == 24 || cartId == 26) {
				ruleRoot.addRuleWord(new TextWord("</div></div>"));
			}

			if (cartId == 23) {
				// 固定图（尺码表）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img.alicdn.com/imgextra/i4/1792368114/TB2IWutc3JkpuFjSszcXXXfsFXa_!!1792368114.jpg")));
			} else if (cartId == 20) {
				// 固定图（尺码表）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img.alicdn.com/imgextra/i3/907029661/TB2JvQhc3NlpuFjy0FfXXX3CpXa-907029661.jpg")));
			} else if (cartId == 24) {
				// 固定图（尺码表）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img10.360buyimg.com/imgzone/jfs/t3271/15/5857953385/23451/d32b8c57/58844413Ned0eb128.jpg")));
			} else if (cartId == 26) {
				// 固定图（尺码表）
				ruleRoot.addRuleWord(new TextWord(String.format(strPlatformTemplate, "https://img10.360buyimg.com/imgzone/jfs/t3271/15/5857953385/23451/d32b8c57/58844413Ned0eb128.jpg")));
			}

			{
				// 尺码图
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord(strPlatformTemplate));

				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("2"));

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("1"));

				CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
				ruleRoot.addRuleWord(new CustomWord(word));
			}
		}

		{
			// 物流图 - 1
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(strPlatformTemplate));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("4"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			RuleExpression imageIndex = new RuleExpression();
			imageIndex.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, imageIndex);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		// 店铺介绍图
		{
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(strPlatformTemplate));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("5"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		return ruleRoot;

	}

	private RuleExpression doDict_无线描述(int cartId) {
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
			String kv = "\"item_info\":{\"item_info_enable\":\"false\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// coupon 优惠
			String kv = "\"coupon\":{\"coupon_enable\":\"false\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// hot_recommanded 同店推荐
			String kv = "\"hot_recommanded\":{\"hot_recommanded_enable\":\"false\"},";
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);
		}

		{
			// shop_discount 店铺活动
			String kv = "\"shop_discount\":{\"shop_discount_enable\":\"false\"},";
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
			TextWord word = new TextWord(kv);
			ruleRoot.addRuleWord(word);

			{
				// 物流图 APP-第一张
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(0, ruleRoot, new CustomWord(getCommonImagesWord));
			}

			{
				// 固定图片 - 产品展示
				String strImg = "";
				if (cartId == 20) {
					strImg = "https://img.alicdn.com/imgextra/i4/907029661/TB2PjLEdUhnpuFjSZFpXXcpuXXa-907029661.jpg";
				} else if (cartId == 23) {
					strImg = "https://img.alicdn.com/imgextra/i3/1792368114/TB2CrOHdItnpuFjSZFKXXalFFXa_!!1792368114.jpg";
				}
				do处理无线端20张图片(1, ruleRoot, new TextWord(strImg));
			}

			// 商品图片组（五图模板）
			do处理无线端20张图片(2, ruleRoot, new DictWord("商品标题与图片-APP-0"));
			do处理无线端20张图片(3, ruleRoot, new DictWord("商品标题与图片-APP-1"));
			do处理无线端20张图片(4, ruleRoot, new DictWord("商品标题与图片-APP-2"));
			do处理无线端20张图片(5, ruleRoot, new DictWord("商品标题与图片-APP-3"));
			do处理无线端20张图片(6, ruleRoot, new DictWord("商品标题与图片-APP-4"));
			do处理无线端20张图片(7, ruleRoot, new DictWord("商品标题与图片-APP-5"));
			do处理无线端20张图片(8, ruleRoot, new DictWord("商品标题与图片-APP-6"));
			do处理无线端20张图片(9, ruleRoot, new DictWord("商品标题与图片-APP-7"));
			do处理无线端20张图片(10, ruleRoot, new DictWord("商品标题与图片-APP-8"));
			do处理无线端20张图片(11, ruleRoot, new DictWord("商品标题与图片-APP-9"));

			{
				// 固定图片 - 尺码
				String strImg = "";
				if (cartId == 20) {
					strImg = "https://img.alicdn.com/imgextra/i1/907029661/TB2J9YCdUlnpuFjSZFjXXXTaVXa-907029661.jpg";
				} else if (cartId == 23) {
					strImg = "https://img.alicdn.com/imgextra/i3/1792368114/TB2Sz_Tc3NlpuFjy0FfXXX3CpXa_!!1792368114.jpg";
				}
				do处理无线端20张图片(14, ruleRoot, new TextWord(strImg));
			}

			{
				// 尺码图 APP-第一张
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("2"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(15, ruleRoot, new CustomWord(getCommonImagesWord));
			}

			{
				// 物流图 APP-第二张
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("4"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("1"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(16, ruleRoot, new CustomWord(getCommonImagesWord));
			}

			{
				// 店铺介绍图 APP-第一张
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("0"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(17, ruleRoot, new CustomWord(getCommonImagesWord));
			}

			{
				// 店铺介绍图 APP-第二张
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("1"));

				CustomWordValueGetCommonImages getCommonImagesWord = new CustomWordValueGetCommonImages(null, imageType, viewType, null, imageIndex);
				do处理无线端20张图片(18, ruleRoot, new CustomWord(getCommonImagesWord));
			}

			{
				// 店铺介绍图 APP-第三张
				RuleExpression imageType = new RuleExpression();
				imageType.addRuleWord(new TextWord("5"));  // imageType 1:商品图 2:尺码图 3：品牌故事图 4：物流介绍图 5:店铺图

				RuleExpression viewType = new RuleExpression();
				viewType.addRuleWord(new TextWord("2"));   // viewType 1:PC端 2：APP端

				RuleExpression imageIndex = new RuleExpression();
				imageIndex.addRuleWord(new TextWord("2"));

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

		return ruleRoot;
	}


}