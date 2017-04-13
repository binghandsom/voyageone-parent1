package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class MasterWordParser extends VOAbsLoggable {

    private CmsBtProductModel cmsBtProductModel;
    private int cartId;
    private ExpressionParser expressionParser;

    private List<Map<String, Object>> evaluationContextStack;

    public MasterWordParser(CmsBtProductModel cmsBtProductModel, int cartId, ExpressionParser expressionParser) {
        evaluationContextStack = new ArrayList<>();
        this.cmsBtProductModel = cmsBtProductModel;
        this.cartId = cartId;
        this.expressionParser = expressionParser;
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        if (!WordType.MASTER.equals(ruleWord.getWordType())
                && !WordType.MASTER_HTML.equals(ruleWord.getWordType())
                && !WordType.MASTER_CLR_HTML.equals(ruleWord.getWordType())
                ) {
            return null;
        } else {
            MasterWord masterWord = (MasterWord) ruleWord;
            String propName = masterWord.getValue();
            Map<String, String> extra = masterWord.getExtra();
            Object plainPropValueObj = null;
            if (evaluationContextStack.isEmpty()) {
                // modified by morse.lu 2016/06/24 start
//                plainPropValueObj = getPropValue(cmsBtProductModel.getFields(), propName);
                // 优先从各自平台的fields里去取，取不到再从common的fields里取
                plainPropValueObj = getPropValueFromProductModel(propName);
                // modified by morse.lu 2016/06/24 end
            } else {
                for (int i = evaluationContextStack.size(); i > 0; i--) {
                    Map<String, Object> evaluationContext = evaluationContextStack.get(i - 1);
                    plainPropValueObj = getPropValue(evaluationContext, propName);
                    if (plainPropValueObj != null) {
                        break;
                    }
                }
                //如果evaluationContext存在，但其中的某属性为空，那么从全局取
                if (plainPropValueObj == null) {
                    // modified by morse.lu 2016/06/24 start
//                    plainPropValueObj = getPropValue(cmsBtProductModel.getFields(), propName);
                    plainPropValueObj = getPropValueFromProductModel(propName);
                    // modified by morse.lu 2016/06/24 end
                }
            }

            // modified by morse.lu 2016/11/22 start
            // 追加默认值属性
//            if (plainPropValueObj == null) {
//                return null;
//            }
            if (plainPropValueObj == null
                    || (plainPropValueObj instanceof String && StringUtils.isEmpty(plainPropValueObj.toString()))
                    || (plainPropValueObj instanceof List && ((List) plainPropValueObj).isEmpty())
                    ) {
                RuleExpression defaultExpression = masterWord.getDefaultExpression();
                if (defaultExpression == null) {
                    return null;
                } else {
                    return expressionParser.parse(defaultExpression, shopBean, user, extParameter);
                }
            }
            // modified by morse.lu 2016/11/22 end
            if (extra == null || extra.isEmpty()) {
                // modified by morse.lu 2016/06/24 start
                // 追加判断ArrayList
//                return String.valueOf(plainPropValueObj);
                if (plainPropValueObj instanceof List) {
                    if (((List) plainPropValueObj).isEmpty()) {
                        // 检查一下, 如果没有值的话, 后面的也不用做了
                        return null;
                    }
                    return ExpressionParser.encodeStringArray((List<String>) plainPropValueObj); // 用"~~"分隔
                } else {
                    return String.valueOf(plainPropValueObj);
                }
                // modified by morse.lu 2016/06/24 end
            } else {
                if (plainPropValueObj instanceof String) {
                    return extra.get(plainPropValueObj);
                } else if (plainPropValueObj instanceof List) {
                    // 20160120 tom ims1->ims2升级导致的问题, 增加检查 START
                    if (((List) plainPropValueObj).isEmpty()) {
                        // 检查一下, 如果没有值的话, 后面的也不用做了
                        return null;
                    }
                    // 20160120 tom ims1->ims2升级导致的问题, 增加检查 END
                    List<String> plainPropValues = (List<String>) plainPropValueObj;
                    List<String> mappedPropValues = new ArrayList<>();
                    for (String plainPropValue : plainPropValues) {
                        mappedPropValues.add(extra.get(plainPropValue));
                    }
                    return ExpressionParser.encodeStringArray(mappedPropValues);
                } else {
                    $error("Master value must be String or String[]");
                    return null;
                }
            }
        }
    }

    /**
     * 优先从各自平台的fields里去取，取不到再从common的fields里取
     */
    private Object getPropValueFromProductModel(String propName) {
        Object plainPropValueObj = getPropValue(cmsBtProductModel.getPlatform(cartId).getFields(), propName);
        if (plainPropValueObj == null) {
            plainPropValueObj = getPropValue(cmsBtProductModel.getCommon().getFields(), propName);
        }
        if (plainPropValueObj == null) {
            plainPropValueObj = getPropValue(cmsBtProductModel.getCommon(), propName);
        }
        return plainPropValueObj;
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
        Map<String, Object> evaluationContext = evaluationContextStack.get(evaluationContextStack.size() - 1);
        evaluationContextStack.remove(evaluationContext);
        return evaluationContext;
    }

    public void pushEvaluationContext(Map<String, Object> evaluationContext) {
        evaluationContextStack.add(evaluationContext);
    }

    public Map<String, Object> getLastEvaluationContext() {
        if (!evaluationContextStack.isEmpty()) {
            return evaluationContextStack.get(evaluationContextStack.size() - 1);
        } else {
            return null;
        }
    }

    public CmsBtProductModel getCmsBtProductModel() {
        return cmsBtProductModel;
    }

    public void setCmsBtProductModel(CmsBtProductModel cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }
}
