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
            Map<String, Object> evaluationContext = evaluationContextStack.get(0);
            if (evaluationContext == null) {
                plainPropValueObj = cmsBtProductModel.getFields().getAttribute(propName);
            } else {
                plainPropValueObj = evaluationContext.get(propName);
            }

            if (extra == null) {
                return (String)plainPropValueObj;
            } else {
                if (plainPropValueObj instanceof String) {
                    return extra.get(plainPropValueObj);
                } else if (plainPropValueObj instanceof  String[]) {
                    String[] plainPropValues = (String[]) plainPropValueObj;
                    List<String> mappedPropValues = new ArrayList<>();
                    for (String plainPropValue : plainPropValues) {
                        mappedPropValues.add(extra.get(plainPropValue));
                    }
                    return encodeStringArray(mappedPropValues);
                } else {
                    logger.error("Master value must be String or String[]");
                    return null;
                }
            }
        }
    }

    public Map<String, Object> popEvaluationContext() {
        Map<String, Object> evaluationContext = evaluationContextStack.get(evaluationContextStack.size()-1);
        evaluationContextStack.remove(evaluationContext);
        return evaluationContext;
    }

    public void pushEvaluationContext(Map<String, Object> evaluationContext) {
        evaluationContextStack.add(evaluationContext);
    }

    public static String encodeStringArray(List<String> mappedPropValues) {
        final String seperator = "$~";
        StringBuilder encodedString = new StringBuilder();

        for (String mappedPropValue : mappedPropValues) {
            encodedString.append(mappedPropValue + seperator);
        }
        return encodedString.substring(0, encodedString.length() - seperator.length());
    }

    public static String[] decodeString(String encodedString) {
        final String seperator = "$~";
        return encodedString.split(seperator);
    }
}
