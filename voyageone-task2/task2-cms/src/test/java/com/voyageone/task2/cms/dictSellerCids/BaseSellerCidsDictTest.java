package com.voyageone.task2.cms.dictSellerCids;

import com.voyageone.common.util.StringUtils;
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
 * @author tom.zhu on 2017/2/9.
 * @version 2.4.0
 * @since 2.4.0
 *
 */
public class BaseSellerCidsDictTest {

	protected enum CompareType {
		Eq,
		Neq,
		Like
	}

	protected class SellerCids {
		private String parentId;
		private String childId;
		private String parentName;
		private String childName;

		/**
		 * 如果匹配到的类目是一级类目的话， 那么parentId和parentName就设置为""就行了
		 * @param parentId
		 * @param childId
		 * @param parentName
		 * @param childName
		 */
		public SellerCids(String parentId, String childId, String parentName, String childName) {
			this.parentId = parentId;
			this.childId = childId;
			this.parentName = parentName;
			this.childName = childName;
		}

		public String getTextSellerCidsValue() {
			StringBuffer sbResult = new StringBuffer();

			// propValue "cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类名字>子分类名字)|cNames(父分类名字,子分类名字)"
			if (StringUtils.isEmpty(parentId)) {
				sbResult.append(childId).append("|");
				sbResult.append(childId).append("|");
				sbResult.append(childName).append("|");
				sbResult.append(childName);
			} else {
				sbResult.append(childId).append("|");
				sbResult.append(parentId).append(",").append(childId).append("|");
				sbResult.append(parentName).append(">").append(childName).append("|");
				sbResult.append(parentName).append(",").append(childName);
			}

			return sbResult.toString();
		}

	}

	/**
	 * 生成json
	 *
	 * @param ruleRoot rule
	 */
	protected void doCreateJson(RuleExpression ruleRoot) {

		RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
		String json = ruleJsonMapper.serializeRuleExpression(ruleRoot);

		System.out.println("=====================================");
		System.out.println(json);

	}

	protected RuleExpression doCreateSimpleIf_Feed_Text(CompareType compareType, String ignoreCaseFlg, String ruleWordLeft, String ruleWordRight, SellerCids value) {
		if (StringUtils.isEmpty(ignoreCaseFlg)) {
			return doCreateSimpleIf(compareType, null, new FeedOrgWord(ruleWordLeft), new TextWord(ruleWordRight), value);
		} else {
			return doCreateSimpleIf(compareType, new TextWord(ignoreCaseFlg), new FeedOrgWord(ruleWordLeft), new TextWord(ruleWordRight), value);
		}
	}


	protected RuleExpression doCreateSimpleIf(CompareType compareType, RuleWord ignoreCaseFlg, RuleWord ruleWordLeft, RuleWord ruleWordRight, SellerCids value) {
		// 根字典
		RuleExpression ruleRoot = new RuleExpression();

		List<RuleWord> ruleWordListIf = new ArrayList<>();

		// 设置条件
		if (compareType.equals(CompareType.Eq)) {
			// 参数
			CustomModuleUserParamConditionEq conditionParamEq = new CustomModuleUserParamConditionEq();

			// Eq的左边
			{
				RuleExpression param = new RuleExpression();
				param.addRuleWord(ruleWordLeft);
				conditionParamEq.setFirstParam(param);
			}

			// Eq的右边
			{
				RuleExpression param = new RuleExpression();
				param.addRuleWord(ruleWordRight);
				conditionParamEq.setSecondParam(param);
			}

			// 是否忽略大小写
			if (ignoreCaseFlg != null) {
				RuleExpression param = new RuleExpression();
				param.addRuleWord(ignoreCaseFlg);
				conditionParamEq.setIgnoreCaseFlg(param);
			}

			CustomWordValueConditionEq conditionEq = new CustomWordValueConditionEq();
			conditionEq.setUserParam(conditionParamEq);

			ruleWordListIf.add(new CustomWord(conditionEq));
		} else if (compareType.equals(CompareType.Neq)) {
			// 参数
			CustomModuleUserParamConditionNeq conditionParamNeq = new CustomModuleUserParamConditionNeq();

			// Neq的左边
			{
				RuleExpression param = new RuleExpression();
				param.addRuleWord(ruleWordLeft);
				conditionParamNeq.setFirstParam(param);
			}

			// Neq的右边
			{
				RuleExpression param = new RuleExpression();
				param.addRuleWord(ruleWordRight);
				conditionParamNeq.setSecondParam(param);
			}

			// 是否忽略大小写
			if (ignoreCaseFlg != null) {
				RuleExpression param = new RuleExpression();
				param.addRuleWord(ignoreCaseFlg);
				conditionParamNeq.setIgnoreCaseFlg(param);
			}

			CustomWordValueConditionNeq conditionNeq = new CustomWordValueConditionNeq();
			conditionNeq.setUserParam(conditionParamNeq);

			ruleWordListIf.add(new CustomWord(conditionNeq));
		} else if (compareType.equals(CompareType.Like)) {
			// 参数
			CustomModuleUserParamConditionLike conditionParamLike = new CustomModuleUserParamConditionLike();

			// Like的左边
			{
				RuleExpression param = new RuleExpression();
				param.addRuleWord(ruleWordLeft);
				conditionParamLike.setFirstParam(param);
			}

			// Like的右边
			{
				RuleExpression param = new RuleExpression();
				param.addRuleWord(ruleWordRight);
				conditionParamLike.setSecondParam(param);
			}

			CustomWordValueConditionLike conditionLike = new CustomWordValueConditionLike();
			conditionLike.setUserParam(conditionParamLike);

			ruleWordListIf.add(new CustomWord(conditionLike));
		}

		RuleExpression conditionListExpressionIf = new RuleExpression();
		conditionListExpressionIf.setRuleWordList(ruleWordListIf);

		// 最终的条件
		CustomModuleUserParamIf customModuleUserParamIf = new CustomModuleUserParamIf();
		customModuleUserParamIf.setCondition(conditionListExpressionIf);

		// 最终的值
		// propValue "cId(子分类id)|cIds(父分类id,子分类id)|cName(父分类名字>子分类名字)|cNames(父分类名字,子分类名字)"
		RuleExpression propValue = new RuleExpression();
		propValue.addRuleWord(new TextWord(value.getTextSellerCidsValue()));
		customModuleUserParamIf.setPropValue(propValue);

		// 设置返回值
		CustomWordValueIf customWordValueIf = new CustomWordValueIf();
		customWordValueIf.setUserParam(customModuleUserParamIf);

		RuleWord customWordIf = new CustomWord(customWordValueIf);

		ruleRoot.addRuleWord(customWordIf);

		return ruleRoot;
	}


}