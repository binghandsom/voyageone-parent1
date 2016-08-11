package com.voyageone.task2.cms.dict;

import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成店铺内分类字典
 * 放入voyageone_cms2.cms_mt_channel_condition_config表中
 *
 * 关于IF条件结果值里面4个propValue的说明：
 * 1.propValue    (cId    子分类id)          (例："1124130584")
 * 2.propValue_2  (cIds   父分类id,子分类id)  (例："1124130579,1124130584")
 * 3.propValue_3  (cName  父分类id,子分类id)  (例："系列>彩色宝石")
 * 4.propValue_4  (cNames 父分类id,子分类id)  (例："系列,彩色宝石")
 *          ↓   ↓   ↓
 * 1."cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"
 *  例："1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"
 *
 * ※feed->master导入的时候，会读取从表中读取这里生成的字典数据，然后设置到Product.platform.PXX.sellerCarts[]里面
 *
 * @author desmond on 2016/8/10.
 * @version 2.4.0
 * @since 2.4.0
 *
 */
public class SellerCats_DictTest {

	@Test
	public void startupTest() {

        // if (条件1 And 条件2) （只有条件1也可以And）
        doCreateJson("店铺内分类", false, doDict_店铺内分类());
        // if (项目1 Like 值)
        doCreateJson("店铺内分类", false, doDict_店铺内分类2());

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
     * 店铺内分类字典(And条件，一个条件也可以用And)
     *
     * @return RuleExpression 店铺内分类字典
     */
    private RuleExpression doDict_店铺内分类() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // 店铺内分类字典
            // And条件1
            CustomModuleUserParamConditionEq conditionEqParam1 = new CustomModuleUserParamConditionEq();
            // firstParam
            RuleExpression firstParam1 = new RuleExpression();
            firstParam1.addRuleWord(new FeedOrgWord("Stone"));
            conditionEqParam1.setFirstParam(firstParam1);
            // secondParam
            RuleExpression secondParam1 = new RuleExpression();
            secondParam1.addRuleWord(new TextWord("Diamond"));
            conditionEqParam1.setSecondParam(secondParam1);

            CustomWordValueConditionEq conditionEq1 = new CustomWordValueConditionEq();
            conditionEq1.setUserParam(conditionEqParam1);

            // And条件2
            CustomModuleUserParamConditionEq conditionEqParam2 = new CustomModuleUserParamConditionEq();
            // firstParam
            RuleExpression firstParam2 = new RuleExpression();
            firstParam2.addRuleWord(new FeedOrgWord("Title"));
            conditionEqParam2.setFirstParam(firstParam2);
            // secondParam
            RuleExpression secondParam2 = new RuleExpression();
            secondParam2.addRuleWord(new TextWord("testTitle"));
            conditionEqParam2.setSecondParam(secondParam2);

            CustomWordValueConditionEq conditionEq2 = new CustomWordValueConditionEq();
            conditionEq2.setUserParam(conditionEqParam2);

            List<RuleWord> ruleWordList = new ArrayList<>();
            ruleWordList.add(new CustomWord(conditionEq1));
            ruleWordList.add(new CustomWord(conditionEq2));

            RuleExpression conditionListExpression = new RuleExpression();
            conditionListExpression.setRuleWordList(ruleWordList);

            CustomModuleUserParamConditionAnd conditionAndParam = new CustomModuleUserParamConditionAnd();
            conditionAndParam.setConditionListExpression(conditionListExpression);

            CustomWordValueConditionAnd conditionAnd = new CustomWordValueConditionAnd();
            conditionAnd.setUserParam(conditionAndParam);

            List<RuleWord> ruleWordListIf = new ArrayList<>();
            ruleWordListIf.add(new CustomWord(conditionAnd));

            RuleExpression conditionListExpressionIf = new RuleExpression();
            conditionListExpressionIf.setRuleWordList(ruleWordListIf);

            CustomModuleUserParamIf customModuleUserParamIf = new CustomModuleUserParamIf();
            customModuleUserParamIf.setCondition(conditionListExpressionIf);

            // propValue "cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"
            RuleExpression propValue = new RuleExpression();
            propValue.addRuleWord(new TextWord("1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"));
            customModuleUserParamIf.setPropValue(propValue);

//            // propValue_2(cIds   父分类id,子分类id)  (例："1124130579,1124130584")
//            RuleExpression propValue2 = new RuleExpression();
//            propValue2.addRuleWord(new TextWord("1124130579,1124130584"));
//            customModuleUserParamIf.setPropValue2(propValue2);
//
//            // propValue_3(cName  父分类id,子分类id)  (例："系列>彩色宝石")
//            RuleExpression propValue3 = new RuleExpression();
//            propValue3.addRuleWord(new TextWord("系列>彩色宝石"));
//            customModuleUserParamIf.setPropValue3(propValue3);
//
//            // propValue_4(cNames 父分类id,子分类id)  (例："系列,彩色宝石")
//            RuleExpression propValue4 = new RuleExpression();
//            propValue4.addRuleWord(new TextWord("系列,彩色宝石"));
//            customModuleUserParamIf.setPropValue4(propValue4);

            CustomWordValueIf customWordValueIf = new CustomWordValueIf();
            customWordValueIf.setUserParam(customModuleUserParamIf);

            RuleWord customWordIf = new CustomWord(customWordValueIf);

            ruleRoot.addRuleWord(customWordIf);

        }

        return ruleRoot;
    }

    /**
     * 店铺内分类字典2(Like条件)
     *
     * @return RuleExpression 店铺内分类字典
     */
    private RuleExpression doDict_店铺内分类2() {
        // 根字典
        RuleExpression ruleRoot = new RuleExpression();

        {
            // 店铺内分类字典
            CustomModuleUserParamConditionLike userParam = new CustomModuleUserParamConditionLike();
            // firstParam
            RuleExpression firstParam = new RuleExpression();
            firstParam.addRuleWord(new MasterWord("productNameEn"));
            userParam.setFirstParam(firstParam);
            // secondParam
            RuleExpression secondParam = new RuleExpression();
            secondParam.addRuleWord(new TextWord("heart"));
            userParam.setSecondParam(secondParam);

            CustomWordValueConditionLike wordValueConditionLike = new CustomWordValueConditionLike();
            wordValueConditionLike.setUserParam(userParam);
            RuleWord customWord = new CustomWord(wordValueConditionLike);

            RuleExpression conditionIf = new RuleExpression();
            conditionIf.addRuleWord(customWord);

            CustomModuleUserParamIf customModuleUserParamIf = new CustomModuleUserParamIf();
            customModuleUserParamIf.setCondition(conditionIf);

            // propValue "cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类id,子分类id)|cNames(父分类id,子分类id)"
            RuleExpression propValue = new RuleExpression();
            propValue.addRuleWord(new TextWord("1124130584|1124130579,1124130584|系列>彩色宝石|系列,彩色宝石"));
            customModuleUserParamIf.setPropValue(propValue);

//            // propValue_2(cIds   父分类id,子分类id)  (例："1124130579,1124130584")
//            RuleExpression propValue2 = new RuleExpression();
//            propValue2.addRuleWord(new TextWord("1124130579,1124130584"));
//            customModuleUserParamIf.setPropValue2(propValue2);
//
//            // propValue_3(cName  父分类id,子分类id)  (例："系列>彩色宝石")
//            RuleExpression propValue3 = new RuleExpression();
//            propValue3.addRuleWord(new TextWord("系列>彩色宝石"));
//            customModuleUserParamIf.setPropValue3(propValue3);
//
//            // propValue_4(cNames 父分类id,子分类id)  (例："系列,彩色宝石")
//            RuleExpression propValue4 = new RuleExpression();
//            propValue4.addRuleWord(new TextWord("系列,彩色宝石"));
//            customModuleUserParamIf.setPropValue4(propValue4);

            CustomWordValueIf customWordValueIf = new CustomWordValueIf();
            customWordValueIf.setUserParam(customModuleUserParamIf);

            RuleWord customWordIf = new CustomWord(customWordValueIf);

            ruleRoot.addRuleWord(customWordIf);
        }

        return ruleRoot;
    }

}