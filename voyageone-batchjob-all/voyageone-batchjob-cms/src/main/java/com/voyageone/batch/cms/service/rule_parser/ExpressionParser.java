package com.voyageone.batch.cms.service.rule_parser;

import com.voyageone.batch.cms.bean.CustomValueSystemParam;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.ims.rule_expression.RuleWord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.Set;

/**
 * Created by Leo on 15-6-2.
 */
public class ExpressionParser {

    private TextWordParser textWordParser;
    private DictWordParser dictWordParser;
    private CustomWordParser customWordParser;
    private MasterWordParser masterWordParser;
    private CustomValueSystemParam customValueSystemParam;

    private static Log logger = LogFactory.getLog(ExpressionParser.class);

    public ExpressionParser(String orderChannelId, int cartId, CmsBtProductModel cmsBtProductModel) {
        customValueSystemParam = new CustomValueSystemParam();
        customValueSystemParam.setOrderChannelId(orderChannelId);
        customValueSystemParam.setCartId(cartId);

        this.dictWordParser = new DictWordParser(orderChannelId);
        this.textWordParser = new TextWordParser();
        this.customWordParser = new CustomWordParser(this, customValueSystemParam);

        this.masterWordParser = new MasterWordParser(cmsBtProductModel);
    }

    public String parse(RuleExpression ruleExpression, Set<String> imageSet) {
        StringBuilder resultStr = new StringBuilder();

        if (ruleExpression != null) {
            for (RuleWord ruleWord : ruleExpression.getRuleWordList()) {
                String plainValue = "";
                switch (ruleWord.getWordType()) {
                    case TEXT:
                        plainValue = textWordParser.parse(ruleWord);
                        break;
                    case MASTER: {
                        String expressionValue = masterWordParser.parse(ruleWord);
                        if (expressionValue == null || "".equals(expressionValue)) {
                            plainValue = "";
                            break;
                        }
                        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                        RuleExpression masterExpression = ruleJsonMapper.deserializeRuleExpression(expressionValue);
                        plainValue = parse(masterExpression, imageSet);
                        break;
                    }
                }

                if (resultStr != null) {
                    if (plainValue != null) {
                        resultStr.append(plainValue);
                    }
                    else {
                        return null;
                    }
                }
            }
        }
        else
            return null;
        return resultStr.toString();
    }

    public Map<String, Object> popMasterPropContext() {
        return masterWordParser.popEvaluationContext();
    }

    public void pushMasterPropContext(Map<String, Object> masterPropContext) {
        masterWordParser.pushEvaluationContext(masterPropContext);
    }
}
