package com.voyageone.task2.cms.service.putaway.rule_parser;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.StringUtils;
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

            // 如果propName为空的话, 就说明是应该看index, 而不是看propName
            if (StringUtils.isEmpty(propName)) {
                // 看的内容是feed.customIds 和 feed.customIdsCn, 配合 feed.cnAtts的内容, 进行返回
                if (feedOrgWord.isTitle()) {
                    // 返回原始的名字
                    // ( 注: 其实这边取得的是feed原始的名字, 而不是真正的英文名字, 也无法自定义, 不过暂时也用不掉. 以后如果有需要, 表结构改善一下, 这段代码可以再改进下)
                    try {
                        return cmsBtProductModel.getFeed().getCustomIds().get(feedOrgWord.getFeedIndex());
                    } catch (Exception e) {
                        // 没有的话直接返回一个有值得空字符串
                        return "";
                    }
                } else {
                    // 返回原始的值
                    try {
                        String key = cmsBtProductModel.getFeed().getCustomIds().get(feedOrgWord.getFeedIndex());
                        return cmsBtProductModel.getFeed().getOrgAtts().getAttribute(key);
                    } catch (Exception e) {
                        // 没有的话直接返回一个有值得空字符串
                        return "";
                    }
                }
            }
            // 如果propName有内容的话, 那就按照原来的逻辑进行处理
            Map<String, String> extra = feedOrgWord.getExtra();
            Object plainPropValueObj;

            plainPropValueObj = cmsBtProductModel.getFeed().getOrgAtts().getAttribute(propName);

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
