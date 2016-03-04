package com.voyageone.batch.cms.service.putaway.rule_parser;

import com.voyageone.batch.cms.bean.CustomValueSystemParam;
import com.voyageone.batch.cms.service.putaway.word.CustomWordModule;
import com.voyageone.batch.cms.service.putaway.DefaultCustomRuleModules;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * Created by Leo on 15-6-18.
 */
public class CustomWordParser {
    private DefaultCustomRuleModules ruleModules;
    private ExpressionParser expressionParser;
    private CustomValueSystemParam systemParam;
    private static Log logger = LogFactory.getLog(CustomWordParser.class);

    public CustomWordParser(ExpressionParser expressionParser, CustomValueSystemParam systemParam) {
        this.expressionParser = expressionParser;
        this.systemParam = systemParam;
        this.ruleModules = DefaultCustomRuleModules.getInstance();
    }

    public String parse(RuleWord ruleWord, Set<String> imageSet)
    {
        if (!WordType.CUSTOM.equals(ruleWord.getWordType()))
        {
            return null;
        }

        CustomWord customWord = (CustomWord) ruleWord;
        CustomWordModule customWordModule = ruleModules.getRuleModule(customWord.getValue().getModuleName());
        if (customWordModule == null)
        {
            logger.error("没有找到匹配的模块-moduleName:" + customWord.getValue().getModuleName());
            return null;
        }
        return customWordModule.parse(customWord, expressionParser, systemParam, imageSet);
    }
}
