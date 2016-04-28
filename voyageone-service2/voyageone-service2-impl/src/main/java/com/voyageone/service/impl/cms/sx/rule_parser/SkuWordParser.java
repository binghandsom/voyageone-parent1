package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.SkuWord;
import com.voyageone.ims.rule_expression.WordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class SkuWordParser extends VOAbsLoggable {

    private Map<String, Object> evaluationContext;

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord) {
        if (!WordType.SKU.equals(ruleWord.getWordType())) {
            return null;
        } else {
            SkuWord skuWord = (SkuWord) ruleWord;
            String propName = skuWord.getValue();
            Object plainPropValueObj;

            if (evaluationContext == null || evaluationContext.isEmpty()) {
                $warn("No sku evaluation Context when parse sku world!");
                return null;
            } else {
                plainPropValueObj = getPropValue(evaluationContext, propName);
            }
            // 20160120 tom sku级别的数据不一定是string,也可能是数字型 START
//            if (plainPropValueObj instanceof String) {
//                return String.valueOf(plainPropValueObj);
//            } else if (plainPropValueObj instanceof  ArrayList) {
//                List<String> plainPropValues = (List<String>) plainPropValueObj;
//                return ExpressionParser.encodeStringArray(plainPropValues);
//            } else {
//                logger.error("Sku value must be String or String[]");
//                return null;
//            }

            if (plainPropValueObj instanceof  ArrayList) {
                List<String> plainPropValues = (List<String>) plainPropValueObj;
                return ExpressionParser.encodeStringArray(plainPropValues);
            } else {
                if (plainPropValueObj == null) {
                    return null;
                } else {
                    return plainPropValueObj.toString();
                }
            }
            // 20160120 tom sku级别的数据不一定是string,也可能是数字型 END
        }
    }


    private Object getPropValue(Map<String, Object> evaluationContext, String propName) {
        char separator = '.';
        if (evaluationContext == null) {
            return null;
        }
        int separatorPos = propName.indexOf(separator);
        if (separatorPos == -1) {
            return evaluationContext.get(propName);
        }
        String firstPropName = propName.substring(0, separatorPos);
        String leftPropName = propName.substring(separatorPos + 1);
        return getPropValue((Map<String, Object>) evaluationContext.get(firstPropName), leftPropName);
    }


    public Map<String, Object> getEvaluationContext() {
        return evaluationContext;
    }

    public void setEvaluationContext(Map<String, Object> evaluationContext) {
        this.evaluationContext = evaluationContext;
    }
}
