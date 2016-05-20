package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/5/20.
 */
public class TargetDictTest {
	String C_TEXT_BR = "<br />";
	String C_TEMPLATE_IMG = "<img src=%s>";

	String C_商品图片 = "PRODUCT_IMAGE";
	String C_包装图片 = "PACKAGE_IMAGE";
	String C_带角度图片 = "ANGLE_IMAGE";
	String C_自定义图片 = "CUSTOM_IMAGE";

	@Test
	public void startupTest() {

		doCreateJson("详情页描述", false, doDict_详情页描述());

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

	private RuleExpression doDict_详情页描述() {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		// 生成内容

		{
			// test
			String html = "这段是测试用的商品描述信息.";
			TextWord word = new TextWord(html);
			ruleRoot.addRuleWord(word);
		}

		return ruleRoot;
	}

	/**
	 * 获取手绘图的默认图
	 * 当手绘图不存在的场合, 用这张共通的图片替换掉
	 *
	 * @param paddingImageIndex 使用第几张替换的图
	 * @return 默认图
	 */

	private RuleExpression get获取手绘图的默认图(String paddingImageIndex) {
		RuleExpression paddingExpression = new RuleExpression();

		RuleExpression paddingPropName = new RuleExpression();
		paddingPropName.addRuleWord(new TextWord("Jewwery_handMade_image"));
		RuleExpression imageIndex = new RuleExpression();
		imageIndex.addRuleWord(new TextWord(paddingImageIndex));
		CustomWordValueGetPaddingImageKey paddingImage = new CustomWordValueGetPaddingImageKey(paddingPropName, imageIndex);

		paddingExpression.addRuleWord(new CustomWord(paddingImage));

		return paddingExpression;

	}

}