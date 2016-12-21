package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamTranslateBaidu;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueTranslateBaidu;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度翻译
 *
 * @author morse.lu 2016/10/26
 * @version 2.6.0
 * @since 2.6.0
 */
public class CustomWordModuleTranslateBaidu extends CustomWordModule {

    public final static String moduleName = "TranslateBaidu";

    public CustomWordModuleTranslateBaidu() {
        super(moduleName);
    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamTranslateBaidu customModuleUserParamTranslateBaidu = ((CustomWordValueTranslateBaidu) customWord.getValue()).getUserParam();

        RuleExpression transTargetExpression = customModuleUserParamTranslateBaidu.getTransTarget();
        RuleExpression transTypeExpression = customModuleUserParamTranslateBaidu.getTransType();
        RuleExpression separatorExpression = customModuleUserParamTranslateBaidu.getSeparator();
        RuleExpression paddingExpression = customModuleUserParamTranslateBaidu.getPaddingExpression();

        List<String> transTargetList = parseRuleExpression(transTargetExpression, expressionParser, shopBean, user, extParameter);
        List<String> transTypeList = parseRuleExpression(transTypeExpression, expressionParser, shopBean, user, extParameter);
        String separator = StringUtils.null2Space(expressionParser.parse(separatorExpression, shopBean, user, extParameter));
        List<String> paddingList = parseRuleExpression(paddingExpression, expressionParser, shopBean, user, extParameter);

        if (ListUtils.notNull(transTypeList) && transTargetList.size() != transTypeList.size()) {
            throw new BusinessException("dict设定不正确!翻译对象数(transTarget)不等于翻译类别数(transType)!");
        }
        if (ListUtils.notNull(paddingList) && transTargetList.size() != paddingList.size()) {
            throw new BusinessException("dict设定不正确!翻译对象数(transTarget)不等于默认值数(transType)!");
        }

        String[] result = new String[transTargetList.size()];
        List<Integer> indexList = new ArrayList<>(); // 需要翻译的参数序号
        List<String> transBaiduOrg = new ArrayList<>(); // 需要翻译的值
        for (int index = 0; index < transTargetList.size(); index++) {
            String targetVal = transTargetList.get(index);
            if (StringUtils.isEmpty(targetVal)) {
                // 取得的翻译对象值为空，直接用padding，如果padding 也没有，就是空
                if (ListUtils.isNull(paddingList) || StringUtils.isEmpty(paddingList.get(index))) {
                    result[index] = "";
                } else {
                    result[index] = paddingList.get(index);
                }
            } else {
                // 取得的翻译对象值不为空
                // 看看是否需要翻译
                if (ListUtils.notNull(transTypeList) && "0".equals(transTypeList.get(index))) {
                    // 只有设值成0了,才不翻译
                    result[index] = targetVal;
                } else {
                    if (ListUtils.isNull(transTypeList) || StringUtils.isEmpty(transTypeList.get(index)) || "1".equals(transTypeList.get(index))) {
                        // 直接翻译
                        // 加进翻译List
                        indexList.add(index);
                        transBaiduOrg.add(targetVal);
                    } else {
                        // transType为检索cms_mt_feed_custom_prop,cms_mt_feed_custom_option的key
                        String searchKey = transTypeList.get(index);
                        String val = sxProductService.getCustomPropService().getPropTrans(sxData.getChannelId(), "0", searchKey, targetVal);
                        if (StringUtils.isEmpty(val)) {
                            // 没有预设或翻译过的值
                            // 加进翻译List
                            indexList.add(index);
                            transBaiduOrg.add(targetVal);
                        } else {
                            // 有预设或翻译过的值
                            result[index] = val;
                        }
                    }
                }
            }
        }

        // 翻译一下
        if (ListUtils.notNull(transBaiduOrg)) {
            List<String> transBaiduCn = sxProductService.transBaidu(transBaiduOrg);
            if (ListUtils.isNull(transBaiduCn) || transBaiduCn.size() != transBaiduOrg.size()) {
                throw new BusinessException("百度翻译失败!");
            }

            for (int index = 0; index < indexList.size(); index++) {
                result[indexList.get(index)] = transBaiduCn.get(index);
            }
        }

        // 拼装成String返回
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < result.length; i++) {
            sb.append(result[i]);
            if (i != result.length - 1) {
                // 不是最后一个，增加分隔符
                sb.append(separator);
            }
        }

        return sb.toString();
    }

}
