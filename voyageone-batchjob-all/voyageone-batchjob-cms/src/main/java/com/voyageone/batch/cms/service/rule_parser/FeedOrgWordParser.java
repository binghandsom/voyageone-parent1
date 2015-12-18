package com.voyageone.batch.cms.service.rule_parser;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.ims.rule_expression.FeedOrgWord;
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
public class FeedOrgWordParser {
    private CmsBtProductModel cmsBtProductModel;

    private Log logger = LogFactory.getLog(FeedOrgWordParser.class);

    public FeedOrgWordParser(CmsBtProductModel cmsBtProductModel) {
        this.cmsBtProductModel = cmsBtProductModel;
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord)
    {
        if (!WordType.FEED_ORG.equals(ruleWord.getWordType()))
        {
            return null;
        }
        else
        {
            FeedOrgWord feedOrgWord = (FeedOrgWord) ruleWord;
            String propName = feedOrgWord.getValue();
            Map<String, String> extra = feedOrgWord.getExtra();
            Object plainPropValueObj;

            plainPropValueObj = cmsBtProductModel.getFeedOrgAtts().getAttribute(propName);

            if (plainPropValueObj == null) {
                return null;
            }

            if (extra == null) {
                return String.valueOf(plainPropValueObj);
            } else {
                if (plainPropValueObj instanceof String) {
                    return extra.get(plainPropValueObj);
                } else if (plainPropValueObj instanceof  String[]) {
                    String[] plainPropValues = (String[]) plainPropValueObj;
                    List<String> mappedPropValues = new ArrayList<>();
                    for (String plainPropValue : plainPropValues) {
                        mappedPropValues.add(extra.get(plainPropValue));
                    }
                    return ExpressionParser.encodeStringArray(mappedPropValues);
                } else {
                    logger.error("Feed org value must be String or String[]");
                    return null;
                }
            }
        }
    }
}
