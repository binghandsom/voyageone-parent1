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

        // ================================================================================================
        // FEED-品牌	==	Marie Claire	1175505996	美丽佳人
        System.out.println(
                do创建简单条件_当主数据里的xx等于YY的场合_就返回文本ZZ(
                        "FEED-品牌",
                        "Marie Claire",
                        "1175505996"
                ));

        // ================================================================================================
        // title_cn	like	心	1177929260	浪漫爱心
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_cn,
                        "心",
                        "1177929260"
        ));
        // title_en	like	heart	1177929260	浪漫爱心
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_en,
                        "heart",
                        "1177929260"
                ));

        // ================================================================================================
        // title_cn	like	十字	1177929261	十字情节
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_cn,
                        "十字",
                        "1177929261"
                ));
        // title_en	like	cross	1177929261	十字情节
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_en,
                        "cross",
                        "1177929261"
                ));

        // ================================================================================================
        // title_cn	like	水滴	1172855661	唯美水滴
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_cn,
                        "水滴",
                        "1172855661"
                ));
        // title_cn	like	泪滴	1172855661	唯美水滴
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_cn,
                        "泪滴",
                        "1172855661"
                ));
        // title_en	like	teardrop	1172855661	唯美水滴
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_en,
                        "teardrop",
                        "1172855661"
                ));

        // ================================================================================================
        // title_cn	like	生辰石	1172540910    生辰石
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_cn,
                        "生辰石",
                        "1172540910"
                ));
        // title_en	like	Birthstones	1172540910	生辰石
        System.out.println(
                do创建简单条件_当CMS里的xxLikeYY的场合_就返回文本ZZ(
                        CmsFieldEnum.CmsModelEnum.title_en,
                        "Birthstones",
                        "1172540910"
                ));

        // ================================================================================================
        // ================================================================================================

    }

    private String do创建简单条件_当主数据里的xx等于YY的场合_就返回文本ZZ(
            String masterValue,
            String eqValue,
            String returnValue
    ) {

        // 当什么什么
        MasterWord ruleWordPara1 = new MasterWord(masterValue);
        RuleExpression ruleExpressionPara1 = new RuleExpression();
        ruleExpressionPara1.addRuleWord(ruleWordPara1);

        // eq什么什么
        TextWord ruleWordPara2 = new TextWord(eqValue);
        RuleExpression ruleExpressionPara2 = new RuleExpression();
        ruleExpressionPara2.addRuleWord(ruleWordPara2);

        // 的场合
        RuleExpression ruleExpression = new RuleExpression();
        CustomWordValueConditionEq eq = new CustomWordValueConditionEq();
        CustomModuleUserParamConditionEq eqPara = new CustomModuleUserParamConditionEq(ruleExpressionPara1, ruleExpressionPara2);
        eq.setUserParam(eqPara);
        ruleExpression.addRuleWord(new CustomWord(eq));

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