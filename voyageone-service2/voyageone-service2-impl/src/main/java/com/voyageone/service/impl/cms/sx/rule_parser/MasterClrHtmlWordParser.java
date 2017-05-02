package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class MasterClrHtmlWordParser extends MasterWordParser {

    public MasterClrHtmlWordParser(CmsBtProductModel cmsBtProductModel, int cartId, ExpressionParser expressionParser) {
        super(cmsBtProductModel, cartId, expressionParser);
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        String superResult = super.parse(ruleWord, shopBean, user, extParameter);
        if (superResult == null) {
            return null;
        }
        String prefix = "< *";
        String suffix = " *>";
        List<String> lstHtml = new ArrayList<>();
        lstHtml.add(prefix + "br" + suffix);
        lstHtml.add("< *br */>");
        lstHtml.add("< *br *\\>");
        lstHtml.add(prefix + "p" + suffix);
        lstHtml.add(prefix + "/p" + suffix);
        lstHtml.add(prefix + "/ *p" + suffix);
        lstHtml.add(prefix + "ul" + suffix);
        lstHtml.add(prefix + "/ul" + suffix);
        lstHtml.add(prefix + "/ *ul" + suffix);
//        lstHtml.add(prefix + "\\ul" + suffix);
        lstHtml.add(prefix + "\\ *ul" + suffix);
        lstHtml.add(prefix + "li" + suffix);
        lstHtml.add(prefix + "/li" + suffix);
        lstHtml.add(prefix + "/ *li" + suffix);
        lstHtml.add(prefix + "div" + suffix);
        lstHtml.add(prefix + "/div" + suffix);
        lstHtml.add(prefix + "/ *div" + suffix);
        lstHtml.add("&nbsp;");
        lstHtml.add(prefix + "a *href=.*" + suffix);
        lstHtml.add(prefix + "/a" + suffix);
        lstHtml.add(prefix + "/ *a" + suffix);
        for (String html : lstHtml) {
            superResult = superResult.replaceAll(html, " ");
        }

        return superResult;
    }

}
