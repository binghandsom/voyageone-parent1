package com.voyageone.batch.ims.service.sx;

/**
 * Created by Tom on 16-1-26.
 */

import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.rule_expression.*;
import org.junit.Test;

public class RuleTest {

    @Test
    public void startupTest() {

        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_cn, // 当中文标题 like
                        "heart",
                        "1177929260"                        // 返回值: 浪漫爱心
        ));
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_en, // 当英文标题 like
                        "heart",
                        "1177929260"                        // 返回值: 浪漫爱心
                ));

    }

    private String do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
            CmsFieldEnum.CmsFieldEnumIntf cmsValue,
            String likeValue,
            String returnValue
    ) {

        // 当什么什么
        CmsWord ruleWordPara1 = new CmsWord(cmsValue);
        RuleExpression ruleExpressionPara1 = new RuleExpression();
        ruleExpressionPara1.addRuleWord(ruleWordPara1);

        // like什么什么
        TextWord ruleWordPara2 = new TextWord(likeValue);
        RuleExpression ruleExpressionPara2 = new RuleExpression();
        ruleExpressionPara2.addRuleWord(ruleWordPara2);

        // 的场合
        RuleExpression ruleExpression = new RuleExpression();
        CustomWordValueConditionLike like = new CustomWordValueConditionLike();
        CustomModuleUserParamConditionLike likePara = new CustomModuleUserParamConditionLike(ruleExpressionPara1, ruleExpressionPara2);
        like.setUserParam(likePara);
        ruleExpression.addRuleWord(new CustomWord(like));

        // 返回值
        RuleExpression rulePropValue = new RuleExpression();
        TextWord valueWord = new TextWord(returnValue);
        rulePropValue.addRuleWord(valueWord);

        // 创建最外层规则
        CustomWordValueIf wordValueRoot = new CustomWordValueIf(ruleExpression, rulePropValue);
        CustomWord wordRoot = new CustomWord(wordValueRoot);

        RuleExpression ruleRoot = new RuleExpression();
        ruleRoot.addRuleWord(wordRoot);

        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
        return ruleJsonMapper.serializeRuleExpression(ruleRoot);

    }

}