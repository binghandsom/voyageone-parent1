package com.voyageone.batch.ims.service.rule_parser;

import com.voyageone.batch.Context;
import com.voyageone.batch.ims.bean.CustomValueSystemParam;
import com.voyageone.batch.ims.dao.PropDao;
import com.voyageone.batch.ims.dao.PropValueDao;
import com.voyageone.batch.ims.modelbean.CmsCodePropBean;
import com.voyageone.batch.ims.modelbean.CmsModelPropBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.ims.rule_expression.RuleWord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.util.Set;

/**
 * Created by Leo on 15-6-2.
 */
public class ExpressionParser {

    private TextWordParser textWordParser;
    private CmsWordParser cmsWordParser;
    private DictWordParser dictWordParser;
    private CustomWordParser customWordParser;
    private MasterWordParser masterWordParser;
    private CustomValueSystemParam customValueSystemParam;

    private static Log logger = LogFactory.getLog(ExpressionParser.class);

    public ExpressionParser(CmsModelPropBean cmsModelProp, CmsCodePropBean mainProductCodeProp, String orderChannelId, int cartId, int level, String levelValue) {
        Context context = Context.getContext();
        ApplicationContext ctx = (ApplicationContext) context.getAttribute("springContext");

        customValueSystemParam = new CustomValueSystemParam();
        customValueSystemParam.setCmsModelProp(cmsModelProp);
        customValueSystemParam.setMainProductProp(mainProductCodeProp);
        customValueSystemParam.setOrderChannelId(orderChannelId);
        customValueSystemParam.setCartId(cartId);

        String categoryId = cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.category_id);

        this.cmsWordParser = new CmsWordParser(cmsModelProp,mainProductCodeProp);
        this.dictWordParser = new DictWordParser(orderChannelId);
        this.textWordParser = new TextWordParser();
        this.customWordParser = new CustomWordParser(this, customValueSystemParam);

        PropDao propDao = ctx.getBean(PropDao.class);
        PropValueDao propValueDao = ctx.getBean(PropValueDao.class);
        this.masterWordParser = new MasterWordParser(categoryId, propDao, propValueDao, orderChannelId, level, levelValue);
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
                    case CMS:
                        plainValue = cmsWordParser.parse(ruleWord);
                        break;
                    case DICT: {
                        DictWord dictWordDefine = dictWordParser.parseToDefineDict(ruleWord);
                        if (dictWordDefine == null)
                        {
                            logger.error("unknow dict word:" + ruleWord);
                            return null;
                        }

                        plainValue = parse(dictWordDefine.getExpression(), imageSet);

                        if (plainValue != null && imageSet != null && dictWordDefine.getIsUrl()) {
                            imageSet.add(plainValue);
                        }
                        break;
                    }
                    case CUSTOM: {
                        plainValue = customWordParser.parse(ruleWord, imageSet);
                        break;
                    }
                    case MASTER: {
                        String expressionValue = masterWordParser.parse(ruleWord);
                        if (expressionValue == null || "".equals(expressionValue)) {
                            return "";
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
}
