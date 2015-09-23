package com.voyageone.batch.ims.service.rule_parser;

import com.voyageone.batch.ims.modelbean.CmsModelPropBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.rule_expression.CmsWord;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;

/**
 * Created by Leo on 15-6-18.
 */
public class CmsWordParser {
    private CmsModelPropBean cmsModelProp;

    public CmsWordParser(CmsModelPropBean cmsModelProp) {
        this.cmsModelProp = cmsModelProp;
    }

    //目前只支持解析model级别的属性
    public String parse(RuleWord ruleWord)
    {
        if (!WordType.CMS.equals(ruleWord.getWordType()))
        {
            return null;
        }
        else
        {
            CmsWord cmsWord = (CmsWord) ruleWord;
            return cmsModelProp.getProp((CmsFieldEnum.CmsModelEnum) cmsWord.getValue());
        }
    }
}
