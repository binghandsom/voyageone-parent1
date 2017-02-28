package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

/**
 * Created by zhujiaye on 16/5/20.
 */
public class Tmall_001_Sneakerhead_DictTest extends BaseDictTest {

	@Test
	public void startupTest() {
		// 天猫国际
		doCreateJson("详情页描述", false, doDict_详情页描述(23));
		doCreateJson("无线描述", false, doDict_无线描述(23));

		// 天猫
		doCreateJson("详情页描述", false, doDict_详情页描述(20));
		doCreateJson("无线描述", false, doDict_无线描述(20));

		// 京东
		doCreateJson("详情页描述", false, doDict_详情页描述(24));
		// 京东国际
		doCreateJson("详情页描述", false, doDict_详情页描述(26));

//		doJM(); // 聚美

	}

	private RuleExpression doDict_详情页描述(int cartId) {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		{
			// 物流图 - 0
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

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
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i1/1792368114/TB2hE26cHplpuFjSspiXXcdfFXa_!!1792368114.jpg")));

				// 产品信息style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"color: #acacac;padding: 0 60.0px 80.0px 60.0px;line-height: 16.0pt;background-color: #0c0c0c;font-family: arial;\">"));
			} else if (cartId == 20) {
				// 固定图（产品信息）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i1/907029661/TB24yAvc9FjpuFjSspbXXXagVXa-907029661.jpg")));

				// 产品信息style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"color: #acacac;padding: 0 60.0px 80.0px 60.0px;line-height: 16.0pt;font-family: arial;\">"));
			} else if (cartId == 24) {
				// 固定图（产品信息）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "http://img10.360buyimg.com/imgzone/jfs/t3112/87/5788074319/34714/6977432/58844413N02924d00.jpg")));

				// 产品信息style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"width:990px; background-color:#0c0c0c; padding-bottom:5em; font-size:1.2em; color:#acacac;line-height:33px; font-family:arial;\"><p style=\"padding-left:2em; padding-right:2em; text-align:left; word-break:normal;\">"));
			} else if (cartId == 26) {
				// 固定图（产品信息）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img10.360buyimg.com/imgzone/jfs/t3112/87/5788074319/34714/6977432/58844413N02924d00.jpg")));

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
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i2/1792368114/TB2mA7jcMxlpuFjy0FoXXa.lXXa_!!1792368114.jpg")));

				// 产品展示style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"font-family: 黑体;font-weight: bold;height: 50.0px;width: 790.0px;background-color: #0c0c0c;text-align: center;color: #acacac;line-height: 50.0px;\">"));
			} else if (cartId == 20) {
				// 固定图（产品展示）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i2/907029661/TB21xUsc3FkpuFjSspnXXb4qFXa-907029661.jpg")));

				// 产品展示style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"font-family: 黑体;font-weight: bold;height: 50.0px;width: 790.0px;text-align: center;color: #acacac;line-height: 50.0px;\">"));
			} else if (cartId == 24) {
				// 固定图（产品展示）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "http://img10.360buyimg.com/imgzone/jfs/t3916/115/1598662108/32654/5e2b8840/58844413Nfd1ede19.jpg")));

				// 产品展示style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"background-color:#0c0c0c; width:990px;\"><div style=\"font-family:黑体; font-weight:bold; font-size:1.2em; padding-top:3px;color=#acacac; height:50px; width:990px; background-color:#0c0c0c; text-align: center; color:#acacac; line-height:50px;\">"));
			} else if (cartId == 26) {
				// 固定图（产品展示）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img10.360buyimg.com/imgzone/jfs/t3916/115/1598662108/32654/5e2b8840/58844413Nfd1ede19.jpg")));

				// 产品展示style div start
				ruleRoot.addRuleWord(new TextWord("<div style=\"background-color:#0c0c0c; width:990px;\"><div style=\"font-family:黑体; font-weight:bold; font-size:1.2em; padding-top:3px;color=#acacac; height:50px; width:990px; background-color:#0c0c0c; text-align: center; color:#acacac; line-height:50px;\">"));
			}

			ruleRoot.addRuleWord(new DictWord("商品标题与图片-0"));
			ruleRoot.addRuleWord(new DictWord("商品标题与图片-1"));
			ruleRoot.addRuleWord(new DictWord("商品标题与图片-2"));
			ruleRoot.addRuleWord(new DictWord("商品标题与图片-3"));
			ruleRoot.addRuleWord(new DictWord("商品标题与图片-4"));
			ruleRoot.addRuleWord(new DictWord("商品标题与图片-5"));
			ruleRoot.addRuleWord(new DictWord("商品标题与图片-6"));
			ruleRoot.addRuleWord(new DictWord("商品标题与图片-7"));

			// 产品展示style div end
			if (cartId == 23 || cartId == 20) {
				ruleRoot.addRuleWord(new TextWord("</div>"));
			} else if (cartId == 24 || cartId == 26) {
				ruleRoot.addRuleWord(new TextWord("</div></div>"));
			}

			if (cartId == 23) {
				// 固定图（尺码表）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i4/1792368114/TB2IWutc3JkpuFjSszcXXXfsFXa_!!1792368114.jpg")));
			} else if (cartId == 20) {
				// 固定图（尺码表）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img.alicdn.com/imgextra/i3/907029661/TB2JvQhc3NlpuFjy0FfXXX3CpXa-907029661.jpg")));
			} else if (cartId == 24) {
				// 固定图（尺码表）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img10.360buyimg.com/imgzone/jfs/t3271/15/5857953385/23451/d32b8c57/58844413Ned0eb128.jpg")));
			} else if (cartId == 26) {
				// 固定图（尺码表）
				ruleRoot.addRuleWord(new TextWord(String.format(C_TEMPLATE_IMG_790, "https://img10.360buyimg.com/imgzone/jfs/t3271/15/5857953385/23451/d32b8c57/58844413Ned0eb128.jpg")));
			}

			{
				// 尺码图
				RuleExpression htmlTemplate = new RuleExpression();
				htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

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
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

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
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

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



		return new RuleExpression();
	}


}