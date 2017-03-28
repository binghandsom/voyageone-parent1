package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/2/23.
 */
public class Jingdong_030_Wella_DictTest extends BaseDictTest {

	@Test
	public void startupTest() {

		doCreateJson("京东详情页描述", false, doDict_详情页描述());

	}

	private RuleExpression doDict_详情页描述() {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		{
			// 店铺介绍图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("5"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 自定义图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			RuleExpression imageTemplate = null;

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord(C_自定义图片));

			RuleExpression useOriUrl = new RuleExpression();
			useOriUrl.addRuleWord(new TextWord("1"));

			CustomWordValueGetAllImages word = new CustomWordValueGetAllImages(htmlTemplate, imageTemplate, imageType, useOriUrl, null, null, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		{
			// 购物流程图
			RuleExpression htmlTemplate = new RuleExpression();
			htmlTemplate.addRuleWord(new TextWord(C_TEMPLATE_IMG_790));

			RuleExpression imageType = new RuleExpression();
			imageType.addRuleWord(new TextWord("4"));

			RuleExpression viewType = new RuleExpression();
			viewType.addRuleWord(new TextWord("1"));

			CustomWordValueGetCommonImages word = new CustomWordValueGetCommonImages(htmlTemplate, imageType, viewType, null, null);
			ruleRoot.addRuleWord(new CustomWord(word));
		}

		return ruleRoot;
	}


}