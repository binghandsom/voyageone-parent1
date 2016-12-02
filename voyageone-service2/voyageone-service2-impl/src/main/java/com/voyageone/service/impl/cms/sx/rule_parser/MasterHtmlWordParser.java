package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class MasterHtmlWordParser extends MasterWordParser {

    public MasterHtmlWordParser(CmsBtProductModel cmsBtProductModel, int cartId, ExpressionParser expressionParser) {
        super(cmsBtProductModel, cartId, expressionParser);
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        String superResult = super.parse(ruleWord, shopBean, user, extParameter);
        if (superResult == null) {
            return null;
        }
        superResult = superResult.replaceAll("<br>", "<br />");
        superResult = superResult.replaceAll("\\*", "");

        return superResult;
    }

}
