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
            Object plainPropValueObj;
            Map<String, Object> evaluationContext = null;
            if (!evaluationContextStack.isEmpty()) {
                evaluationContext = evaluationContextStack.get(0);
            }
            if (evaluationContext == null) {
                //TODO 将要删除
                plainPropValueObj = cmsBtProductModel.getFields().getAttribute(propName);
            } else {
                plainPropValueObj = evaluationContext.get(propName);
            }
            //如果evaluetionContext存在，但其中的某属性为空，那么从全局取
            if (plainPropValueObj == null) {
                //TODO 将要删除
                plainPropValueObj = cmsBtProductModel.getFields().getAttribute(propName);
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

    public Map<String, Object> popEvaluationContext() {
        Map<String, Object> evaluationContext = evaluationContextStack.get(evaluationContextStack.size()-1);
        evaluationContextStack.remove(evaluationContext);
        return evaluationContext;
    }

    public void pushEvaluationContext(Map<String, Object> evaluationContext) {
        evaluationContextStack.add(evaluationContext);
    }
}
