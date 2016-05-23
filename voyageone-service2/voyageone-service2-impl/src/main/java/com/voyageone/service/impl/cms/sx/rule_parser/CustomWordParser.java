package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.word.CustomWordModule;
import com.voyageone.service.impl.cms.sx.word.DefaultCustomRuleModules;

import java.util.Set;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordParser extends VOAbsLoggable {

    private DefaultCustomRuleModules ruleModules;
    private ExpressionParser expressionParser;
    private SxProductService sxProductService;
    private SxData sxData;

    public CustomWordParser(ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData) {
        this.expressionParser = expressionParser;
        this.sxProductService = sxProductService;
        this.sxData = sxData;
        this.ruleModules = DefaultCustomRuleModules.getInstance();
    }

//    public String parse(RuleWord ruleWord, ShopBean shopBean, String user, Set<String> imageSet) {
    public String parse(RuleWord ruleWord, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        if (!WordType.CUSTOM.equals(ruleWord.getWordType())) {
            return null;
        }

        CustomWord customWord = (CustomWord) ruleWord;
        CustomWordModule customWordModule = ruleModules.getRuleModule(customWord.getValue().getModuleName());
        if (customWordModule == null) {
            $error("没有找到匹配的模块-moduleName:" + customWord.getValue().getModuleName());
            return null;
        }
        return customWordModule.parse(customWord, expressionParser, sxProductService, sxData, shopBean, user, extParameter);
    }
}
