package com.voyageone.batch.cms.service.rule_parser;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-6-18.
 */
public class MasterWordParser {
    private CmsBtProductModel cmsBtProductModel;

    private Log logger = LogFactory.getLog(MasterWordParser.class);

    private List<Map<String, Object>> evaluationContextStack;

    public MasterWordParser(CmsBtProductModel cmsBtProductModel) {
        evaluationContextStack = new ArrayList<>();
        this.cmsBtProductModel = cmsBtProductModel;
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord)
    {
        if (!WordType.MASTER.equals(ruleWord.getWordType()))
        {
            return null;
        }
        else
        {
            MasterWord masterWord = (MasterWord) ruleWord;
            String propName = masterWord.getValue();
            Map<String, String> extra = masterWord.getExtra();
            Object plainPropValueObj = null;
            if (evaluationContextStack.isEmpty()) {
                plainPropValueObj = getPropValue(cmsBtProductModel.getFields(), propName);
            } else {
                for (int i = evaluationContextStack.size(); i>0; i--) {
                    Map<String, Object> evaluationContext = evaluationContextStack.get(i-1);
                    plainPropValueObj = getPropValue(evaluationContext, propName);
                    if (plainPropValueObj != null) {
                        break;
                    }
                }
                //如果evaluationContext存在，但其中的某属性为空，那么从全局取
                if (plainPropValueObj == null) {
                    plainPropValueObj = getPropValue(cmsBtProductModel.getFields(), propName);
                }
            }

            if (plainPropValueObj == null) {
                return null;
            }
            if (extra == null) {
                return String.valueOf(plainPropValueObj);
            } else {
                if (plainPropValueObj instanceof String) {
                    return extra.get(plainPropValueObj);
                } else if (plainPropValueObj instanceof  ArrayList) {
                    List<String> plainPropValues = (List<String>) plainPropValueObj;
                    List<String> mappedPropValues = new ArrayList<>();
                    for (String plainPropValue : plainPropValues) {
                        mappedPropValues.add(extra.get(plainPropValue));
                    }
                    return ExpressionParser.encodeStringArray(mappedPropValues);
                } else {
                    logger.error("Master value must be String or String[]");
                    return null;
                }
            }
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

    public Map<String, Object> popEvaluationContext() {
        Map<String, Object> evaluationContext = evaluationContextStack.get(evaluationContextStack.size()-1);
        evaluationContextStack.remove(evaluationContext);
        return evaluationContext;
    }

    public void pushEvaluationContext(Map<String, Object> evaluationContext) {
        evaluationContextStack.add(evaluationContext);
    }
}
