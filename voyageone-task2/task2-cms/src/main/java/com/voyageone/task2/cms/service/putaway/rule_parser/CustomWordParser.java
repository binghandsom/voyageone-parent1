package com.voyageone.task2.cms.service.putaway.rule_parser;

import com.voyageone.task2.cms.bean.CustomValueSystemParam;
import com.voyageone.task2.cms.service.putaway.word.CustomWordModule;
import com.voyageone.task2.cms.service.putaway.DefaultCustomRuleModules;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by Leo on 15-6-18.
 */
public class CustomWordParser {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private DefaultCustomRuleModules ruleModules;
    private ExpressionParser expressionParser;
    private CustomValueSystemParam systemParam;

    public CustomWordParser(ExpressionParser expressionParser, CustomValueSystemParam systemParam) {
        this.expressionParser = expressionParser;
        this.systemParam = systemParam;
        this.ruleModules = DefaultCustomRuleModules.getInstance();
    }

    public String parse(RuleWord ruleWord, Set<String> imageSet) {
        if (!WordType.CUSTOM.equals(ruleWord.getWordType())) {
            return null;
        }

        CustomWord customWord = (CustomWord) ruleWord;
        CustomWordModule customWordModule = ruleModules.getRuleModule(customWord.getValue().getModuleName());
        if (customWordModule == null) {
            logger.error("没有找到匹配的模块-moduleName:" + customWord.getValue().getModuleName());
            return null;
        }
        return customWordModule.parse(customWord, expressionParser, systemParam, imageSet);
    }
}
